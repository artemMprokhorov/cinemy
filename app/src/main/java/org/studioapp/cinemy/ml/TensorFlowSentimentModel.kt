package org.studioapp.cinemy.ml

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.studioapp.cinemy.ml.model.SentimentResult
import org.studioapp.cinemy.ml.model.SentimentType
import org.studioapp.cinemy.ml.model.TensorFlowConfig
import org.studioapp.cinemy.ml.model.ModelInfo
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

/**
 * TensorFlow Lite sentiment analysis model for Cinemy
 * BERT-based sentiment analysis using TensorFlow Lite
 * Primary model in hybrid sentiment analysis system
 */
class TensorFlowSentimentModel private constructor(private val context: Context) {

    private var interpreter: Interpreter? = null
    private var config: TensorFlowConfig? = null
    private var vocabulary: Map<String, Int> = emptyMap()
    private var isInitialized = false

    companion object {
        @Volatile
        private var INSTANCE: TensorFlowSentimentModel? = null

        /**
         * Gets singleton instance of TensorFlowSentimentModel
         * @param context Android context
         * @return TensorFlowSentimentModel instance
         */
        fun getInstance(context: Context): TensorFlowSentimentModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TensorFlowSentimentModel(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }

        // Model constants
        private const val MODEL_FILE = "production_sentiment_full_manual.tflite"
        private const val CONFIG_FILE = "android_integration_config.json"
        private const val VOCAB_FILE = "vocab.json"
        private const val MODEL_TYPE = "bert_sentiment_analysis"
        private const val MODEL_VERSION = "2.0.0"
        private const val MODEL_LANGUAGE = "en"
        private const val MODEL_ACCURACY = "95%+"
        private const val MODEL_SPEED = "fast"

        // BERT model constants
        private const val MAX_SEQUENCE_LENGTH = 512
        private const val UNK_TOKEN = "[UNK]"
        private const val CLS_TOKEN = "[CLS]"
        private const val SEP_TOKEN = "[SEP]"
        private const val PAD_TOKEN = "[PAD]"
        private const val MASK_TOKEN = "[MASK]"

        // Error messages
        private const val ERROR_MODEL_NOT_INITIALIZED = "TensorFlow model not initialized"
        private const val ERROR_INFERENCE = "TensorFlow inference error"

        // Default thresholds
        private const val DEFAULT_CONFIDENCE_THRESHOLD = 0.6
        private const val DEFAULT_NUM_THREADS = 4
    }

    /**
     * Initializes TensorFlow Lite model with BERT configuration
     * Loads model file, vocabulary, and configuration from assets
     * @return Boolean indicating if initialization was successful
     */
    suspend fun initialize(): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            if (isInitialized) return@withContext true

            // Load configuration
            config = loadConfig()
            if (config == null) {
                return@withContext false
            }

            // Load vocabulary
            vocabulary = loadVocabulary()
            if (vocabulary.isEmpty()) {
                return@withContext false
            }

            // Load TensorFlow Lite model
            val modelBuffer = loadModelFile()
            if (modelBuffer == null) {
                return@withContext false
            }

            // Create interpreter with options
            val options = Interpreter.Options().apply {
                numThreads = config?.tensorflowLite?.performance?.numThreads ?: DEFAULT_NUM_THREADS
                setUseNNAPI(config?.tensorflowLite?.performance?.useNnapi ?: true)
                setUseXNNPACK(config?.tensorflowLite?.performance?.useXnnpack ?: true)
            }

            interpreter = Interpreter(modelBuffer, options)
            isInitialized = true
            true
        }.getOrElse { e ->
            false
        }
    }

    /**
     * Analyzes sentiment using BERT-based TensorFlow Lite model
     * Preprocesses text, runs inference, and postprocesses results
     * @param text Input text to analyze
     * @return SentimentResult with sentiment classification and confidence
     */
    suspend fun analyzeSentiment(text: String): SentimentResult = withContext(Dispatchers.Default) {
        if (!isInitialized || interpreter == null || config == null) {
            return@withContext SentimentResult.error(ERROR_MODEL_NOT_INITIALIZED)
        }

        if (text.isBlank()) {
            return@withContext SentimentResult.neutral()
        }

        runCatching {
            val startTime = System.currentTimeMillis()

            // Preprocess text
            val processedText = preprocessText(text, config!!)

            // Prepare input tensor
            val inputIds = prepareInputTensor(processedText, config!!)

            // Run inference
            val outputBuffer = runInference(inputIds, config!!)

            // Postprocess results
            val result = postprocessOutput(outputBuffer, config!!)

            val processingTime = System.currentTimeMillis() - startTime

            result.copy(processingTimeMs = processingTime)
        }.getOrElse { e ->
            SentimentResult.error("$ERROR_INFERENCE: ${e.message}")
        }
    }

    /**
     * Gets information about the TensorFlow Lite model
     * @return ModelInfo object with model details or null if not available
     */
    fun getModelInfo(): ModelInfo? = config?.let { tfConfig ->
        ModelInfo(
            type = MODEL_TYPE,
            version = tfConfig.tensorflowLite?.version ?: MODEL_VERSION,
            language = MODEL_LANGUAGE,
            accuracy = MODEL_ACCURACY,
            speed = MODEL_SPEED
        )
    }

    /**
     * Checks if TensorFlow Lite model is ready for inference
     * @return Boolean indicating model readiness (initialized and interpreter available)
     */
    fun isReady(): Boolean = isInitialized && interpreter != null

    /**
     * Load configuration from JSON
     */
    private fun loadConfig(): TensorFlowConfig? {
        return runCatching {
            val configJson = context.assets.open("ml_models/$CONFIG_FILE").use { inputStream ->
                inputStream.bufferedReader().readText()
            }

            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<TensorFlowConfig>(configJson)
        }.getOrNull()
    }

    /**
     * Load TensorFlow Lite model file
     */
    private fun loadModelFile(): MappedByteBuffer? {
        return runCatching {
            val assetFileDescriptor = context.assets.openFd("ml_models/$MODEL_FILE")
            val inputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
            val fileChannel = inputStream.channel
            val startOffset = assetFileDescriptor.startOffset
            val declaredLength = assetFileDescriptor.declaredLength
            fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        }.getOrNull()
    }

    /**
     * Load BERT vocabulary from JSON file
     */
    private fun loadVocabulary(): Map<String, Int> {
        return runCatching {
            val vocabJson = context.assets.open("ml_models/$VOCAB_FILE").use { inputStream ->
                inputStream.bufferedReader().readText()
            }

            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<Map<String, Int>>(vocabJson)
        }.getOrElse {
            // Fallback vocabulary with basic tokens
            mapOf(
                PAD_TOKEN to 0,
                UNK_TOKEN to 100,
                CLS_TOKEN to 101,
                SEP_TOKEN to 102,
                MASK_TOKEN to 103
            )
        }
    }

    /**
     * Preprocess text for BERT model input
     */
    private fun preprocessText(text: String, config: TensorFlowConfig): String {
        val inputConfig = config.tensorflowLite?.inputConfig
        var processedText = text

        // Apply preprocessing steps
        if (inputConfig?.preprocessing?.lowercase == true) {
            processedText = processedText.lowercase()
        }

        if (inputConfig?.preprocessing?.removePunctuation == true) {
            processedText = processedText.replace(Regex("[^a-zA-Z0-9\\s]"), "")
        }

        // Truncate if too long
        val maxLength = inputConfig?.preprocessing?.maxLength ?: MAX_SEQUENCE_LENGTH
        if (processedText.length > maxLength) {
            processedText = processedText.take(maxLength)
        }

        return processedText
    }

    /**
     * Prepare input tensor for BERT inference
     */
    private fun prepareInputTensor(text: String, config: TensorFlowConfig): IntArray {
        val inputConfig = config.tensorflowLite?.inputConfig
        val maxLength = inputConfig?.preprocessing?.maxLength ?: MAX_SEQUENCE_LENGTH

        // Tokenize text for BERT
        val tokens = tokenizeForBERT(text)

        // Convert tokens to IDs
        val tokenIds = tokensToIds(tokens)

        // Create input array with padding
        val inputIds = IntArray(maxLength)
        val attentionMask = IntArray(maxLength)

        // Copy token IDs and create attention mask
        for (i in 0 until maxLength) {
            if (i < tokenIds.size) {
                inputIds[i] = tokenIds[i]
                attentionMask[i] = 1
            } else {
                inputIds[i] = vocabulary[PAD_TOKEN] ?: 0
                attentionMask[i] = 0
            }
        }

        return inputIds
    }

    /**
     * Tokenize text for BERT model
     */
    private fun tokenizeForBERT(text: String): List<String> {
        // Add special tokens
        val tokens = mutableListOf<String>()
        tokens.add(CLS_TOKEN)

        // Simple word-level tokenization (in production, use proper BERT tokenizer)
        val words = text.split("\\s+").filter { it.isNotBlank() }
        tokens.addAll(words)

        // Truncate if too long (leave space for SEP token)
        val maxTokens = MAX_SEQUENCE_LENGTH - 2
        if (tokens.size > maxTokens) {
            tokens.subList(1, maxTokens + 1)
        }

        tokens.add(SEP_TOKEN)
        return tokens
    }

    /**
     * Convert tokens to vocabulary IDs
     */
    private fun tokensToIds(tokens: List<String>): IntArray {
        return tokens.map { token ->
            vocabulary[token] ?: vocabulary[UNK_TOKEN] ?: 100
        }.toIntArray()
    }

    /**
     * Run TensorFlow Lite inference with BERT input
     */
    private fun runInference(inputIds: IntArray, config: TensorFlowConfig): FloatArray {
        val outputConfig = config.tensorflowLite?.outputConfig

        // Prepare input tensor for BERT
        val outputShape = (outputConfig?.outputShape ?: listOf(1, 3)).toIntArray()

        // Create input array with batch dimension
        val inputArray = arrayOf(inputIds)

        // Prepare output buffer
        val outputArray = Array(1) { FloatArray(outputShape[1]) }

        // Run inference
        interpreter?.run(inputArray, outputArray)

        return outputArray[0]
    }

    /**
     * Postprocess TensorFlow Lite output
     */
    private fun postprocessOutput(
        outputArray: FloatArray,
        config: TensorFlowConfig
    ): SentimentResult {
        val outputConfig = config.tensorflowLite?.outputConfig
        val classLabels = outputConfig?.classLabels ?: listOf("negative", "neutral", "positive")
        val confidenceThreshold = outputConfig?.confidenceThreshold ?: DEFAULT_CONFIDENCE_THRESHOLD

        // Apply softmax to get probabilities
        val probabilities = softmax(outputArray)

        // Find the class with highest probability
        val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: 1
        val maxConfidence = probabilities[maxIndex]

        // Determine sentiment type
        val sentimentType = when (maxIndex) {
            0 -> SentimentType.NEGATIVE
            1 -> SentimentType.NEUTRAL
            2 -> SentimentType.POSITIVE
            else -> SentimentType.NEUTRAL
        }

        // Create result
        return when {
            maxConfidence >= confidenceThreshold -> {
                when (sentimentType) {
                    SentimentType.POSITIVE -> SentimentResult.positive(
                        maxConfidence.toDouble(),
                        listOf("tensorflow:${classLabels[maxIndex]}")
                    )

                    SentimentType.NEGATIVE -> SentimentResult.negative(
                        maxConfidence.toDouble(),
                        listOf("tensorflow:${classLabels[maxIndex]}")
                    )

                    else -> SentimentResult.neutral(
                        maxConfidence.toDouble(),
                        listOf("tensorflow:${classLabels[maxIndex]}")
                    )
                }
            }

            else -> {
                // Low confidence - return neutral
                SentimentResult.neutral(
                    maxConfidence.toDouble(),
                    listOf("tensorflow:low_confidence")
                )
            }
        }
    }

    /**
     * Apply softmax function to convert logits to probabilities
     */
    private fun softmax(logits: FloatArray): FloatArray {
        val maxLogit = logits.maxOrNull() ?: 0f
        val expLogits = logits.map { kotlin.math.exp((it - maxLogit).toDouble()) }
        val sumExp = expLogits.sum()
        return expLogits.map { (it / sumExp).toFloat() }.toFloatArray()
    }

    /**
     * Clean up resources
     */
    fun cleanup() {
        interpreter?.close()
        interpreter = null
        isInitialized = false
    }
}

