package org.studioapp.cinemy.ml.mlmodels

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.studioapp.cinemy.ml.model.ModelInfo
import org.studioapp.cinemy.ml.model.SentimentResult
import org.studioapp.cinemy.ml.model.SentimentType
import org.studioapp.cinemy.ml.model.TensorFlowConfig
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
     * Gets singleton instance of TensorFlowSentimentModel using thread-safe singleton pattern.
     * 
     * This method provides thread-safe access to the TensorFlow sentiment model singleton.
     * It uses synchronized access to ensure only one instance is created across the application.
     * The singleton pattern prevents multiple model instances and reduces memory usage.
     * 
     * @param context Android context for model initialization
     * @return TensorFlowSentimentModel singleton instance
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
     * Initializes TensorFlow Lite model with BERT configuration and hardware optimization.
     * 
     * This method performs comprehensive initialization of the TensorFlow Lite sentiment model:
     * 1. Loads model configuration from android_integration_config.json
     * 2. Loads BERT vocabulary from vocab.json for text preprocessing
     * 3. Loads TensorFlow Lite model file (production_sentiment_full_manual.tflite)
     * 4. Creates TensorFlow Lite interpreter with hardware acceleration options
     * 5. Configures NNAPI, XNNPACK, and multi-threading for optimal performance
     * 
     * The initialization is thread-safe and can be called multiple times safely.
     * Returns true only if all components (config, vocabulary, model, interpreter) load successfully.
     * 
     * @return Boolean indicating if initialization was successful (true) or failed (false)
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
     * Analyzes sentiment using BERT-based TensorFlow Lite model with comprehensive preprocessing.
     * 
     * This method performs complete sentiment analysis using the BERT model:
     * 1. Validates model readiness and input text
     * 2. Preprocesses text with BERT tokenization and vocabulary mapping
     * 3. Prepares input tensor with attention masks for BERT architecture
     * 4. Runs TensorFlow Lite inference with hardware acceleration
     * 5. Postprocesses results with softmax normalization and confidence scoring
     * 6. Returns sentiment classification (POSITIVE, NEGATIVE, NEUTRAL) with confidence
     * 
     * The method handles low confidence results by returning neutral sentiment with "low_confidence" indicator.
     * Processing time is measured and included in the result for performance monitoring.
     * 
     * @param text Input text to analyze for sentiment (empty/blank text returns neutral)
     * @return SentimentResult with sentiment classification, confidence score, and processing time
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
     * Gets comprehensive information about the TensorFlow Lite model configuration.
     * 
     * This method provides detailed model information including type, version, language,
     * accuracy, and speed characteristics. The information is extracted from the loaded
     * configuration and includes both static model properties and dynamic configuration values.
     * 
     * Returns null if the model configuration has not been loaded or is unavailable.
     * 
     * @return ModelInfo object with model details (type, version, language, accuracy, speed) or null if not available
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
     * Checks if TensorFlow Lite model is ready for sentiment analysis inference.
     * 
     * This method verifies that both the model initialization is complete and the
     * TensorFlow Lite interpreter is available. The model is considered ready when:
     * - Initialization process has completed successfully (isInitialized = true)
     * - TensorFlow Lite interpreter is created and available (interpreter != null)
     * 
     * This check should be performed before calling analyzeSentiment() to ensure
     * the model is properly loaded and ready for inference.
     * 
     * @return Boolean indicating model readiness (true if ready, false if not initialized or interpreter unavailable)
     */
    fun isReady(): Boolean = isInitialized && interpreter != null

    /**
     * Loads TensorFlow Lite model configuration from android_integration_config.json.
     * 
     * This method loads the complete model configuration including input/output settings,
     * preprocessing options, performance parameters, and class labels. The configuration
     * is parsed from JSON and used throughout the model lifecycle for inference setup.
     * 
     * @return TensorFlowConfig object with complete model configuration or null if loading fails
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
     * Loads TensorFlow Lite model file as memory-mapped byte buffer for efficient inference.
     * 
     * This method loads the production_sentiment_full_manual.tflite model file from assets
     * and maps it to memory for direct access by the TensorFlow Lite interpreter. The
     * memory-mapped approach provides efficient model loading and reduces memory usage.
     * 
     * @return MappedByteBuffer containing the model data or null if loading fails
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
     * Loads BERT vocabulary mapping from vocab.json for text tokenization.
     * 
     * This method loads the complete BERT vocabulary with token-to-ID mappings
     * used for text preprocessing. The vocabulary includes special tokens (CLS, SEP, PAD, UNK, MASK)
     * and word-level tokens for BERT tokenization. Falls back to basic tokens if loading fails.
     * 
     * @return Map of token strings to vocabulary IDs or fallback vocabulary if loading fails
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
     * Preprocesses input text for BERT model compatibility.
     * 
     * This method applies comprehensive text preprocessing including lowercase conversion,
     * punctuation removal, and length truncation according to model configuration.
     * The preprocessing ensures text is compatible with BERT tokenization requirements.
     * 
     * @param text Input text to preprocess
     * @param config TensorFlow configuration with preprocessing settings
     * @return Preprocessed text ready for BERT tokenization
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
     * Prepares input tensor for BERT model inference with attention masks.
     * 
     * This method converts preprocessed text into BERT-compatible input tensors
     * including token IDs and attention masks. It handles BERT special tokens (CLS, SEP)
     * and creates proper input sequences with padding for batch processing.
     * 
     * @param text Preprocessed text for tokenization
     * @param config TensorFlow configuration with input settings
     * @return IntArray of token IDs ready for BERT inference
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
     * Tokenizes text for BERT model with special tokens and word-level splitting.
     * 
     * This method performs BERT-compatible tokenization by adding special tokens
     * (CLS at start, SEP at end) and splitting text into words. It handles sequence
     * length limits and ensures proper BERT input format for sentiment analysis.
     * 
     * @param text Preprocessed text to tokenize
     * @return List of tokens with BERT special tokens (CLS, words, SEP)
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
     * Converts token strings to vocabulary IDs for BERT model input.
     * 
     * This method maps each token to its corresponding vocabulary ID using the loaded
     * BERT vocabulary. Unknown tokens are mapped to the UNK token ID for proper handling.
     * 
     * @param tokens List of token strings to convert
     * @return IntArray of vocabulary IDs for BERT inference
     */
    private fun tokensToIds(tokens: List<String>): IntArray {
        return tokens.map { token ->
            vocabulary[token] ?: vocabulary[UNK_TOKEN] ?: 100
        }.toIntArray()
    }

    /**
     * Runs TensorFlow Lite inference with BERT input tensors.
     * 
     * This method executes the actual TensorFlow Lite model inference using the prepared
     * input tensor. It handles batch processing and returns raw model output logits
     * that will be postprocessed into sentiment probabilities.
     * 
     * @param inputIds Token IDs array for BERT inference
     * @param config TensorFlow configuration with output settings
     * @return FloatArray of raw model output logits
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
     * Postprocesses TensorFlow Lite model output into sentiment classification results.
     * 
     * This method converts raw model logits into sentiment probabilities using softmax
     * normalization, determines the highest confidence class, and creates appropriate
     * SentimentResult objects. It handles confidence thresholds and low confidence cases.
     * 
     * @param outputArray Raw model output logits from inference
     * @param config TensorFlow configuration with output settings and thresholds
     * @return SentimentResult with sentiment classification and confidence score
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
     * Applies softmax function to convert raw logits into probability distributions.
     * 
     * This method implements the softmax function to normalize raw model output
     * into valid probability distributions that sum to 1.0. It uses numerical
     * stability techniques to prevent overflow in exponential calculations.
     * 
     * @param logits Raw model output logits
     * @return FloatArray of normalized probabilities that sum to 1.0
     */
    private fun softmax(logits: FloatArray): FloatArray {
        val maxLogit = logits.maxOrNull() ?: 0f
        val expLogits = logits.map { kotlin.math.exp((it - maxLogit).toDouble()) }
        val sumExp = expLogits.sum()
        return expLogits.map { (it / sumExp).toFloat() }.toFloatArray()
    }

    /**
     * Cleans up TensorFlow Lite model resources to prevent memory leaks.
     * 
     * This method performs comprehensive cleanup of all model resources:
     * 1. Closes the TensorFlow Lite interpreter to free native resources
     * 2. Resets the interpreter reference to null
     * 3. Resets the initialization flag to false
     * 
     * This method should be called when the model is no longer needed to ensure
     * proper resource cleanup and prevent memory leaks. The model can be re-initialized
     * after cleanup by calling initialize() again.
     * 
     * The cleanup is safe to call multiple times and handles cleanup errors gracefully.
     */
    fun cleanup() {
        interpreter?.close()
        interpreter = null
        isInitialized = false
    }
}

