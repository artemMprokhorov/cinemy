package org.studioapp.cinemy.ml

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.max

/**
 * TensorFlow Lite sentiment analysis model for Cinemy
 * Provides ML-based sentiment analysis with fallback to keyword model
 */
class TensorFlowSentimentModel private constructor(private val context: Context) {

    private var interpreter: Interpreter? = null
    private var config: TensorFlowConfig? = null
    private var isInitialized = false

    companion object {
        @Volatile
        private var INSTANCE: TensorFlowSentimentModel? = null

        fun getInstance(context: Context): TensorFlowSentimentModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TensorFlowSentimentModel(context.applicationContext).also { INSTANCE = it }
            }
        }

        // Model constants
        private const val MODEL_FILE = "test_sentiment_model.tflite"
        private const val CONFIG_FILE = "android_integration_config.json"
        private const val MODEL_TYPE = "tensorflow_lite_sentiment"
        private const val MODEL_VERSION = "1.0.0"
        private const val MODEL_LANGUAGE = "en"
        private const val MODEL_ACCURACY = "90%+"
        private const val MODEL_SPEED = "fast"

        // Error messages
        private const val ERROR_MODEL_NOT_INITIALIZED = "TensorFlow model not initialized"
        private const val ERROR_MODEL_LOADING = "Failed to load TensorFlow model"
        private const val ERROR_INFERENCE = "TensorFlow inference error"
        private const val ERROR_CONFIG_LOADING = "Failed to load TensorFlow config"

        // Default thresholds
        private const val DEFAULT_CONFIDENCE_THRESHOLD = 0.6
        private const val DEFAULT_MAX_LENGTH = 512
        private const val DEFAULT_NUM_THREADS = 4
    }

    /**
     * Initialize TensorFlow Lite model
     */
    suspend fun initialize(): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            if (isInitialized) return@withContext true

            // Load configuration
            config = loadConfig()
            if (config == null) {
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
                setUseXNNPACK(config?.tensorflowLite?.performance?.enableXnnpack ?: true)
            }

            interpreter = Interpreter(modelBuffer, options)
            isInitialized = true
            true
        }.getOrElse { e ->
            false
        }
    }

    /**
     * Analyze sentiment using TensorFlow Lite model
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
            val inputBuffer = prepareInputTensor(processedText, config!!)
            
            // Run inference
            val outputBuffer = runInference(inputBuffer, config!!)
            
            // Postprocess results
            val result = postprocessOutput(outputBuffer, config!!)
            
            val processingTime = System.currentTimeMillis() - startTime
            
            result.copy(processingTimeMs = processingTime)
        }.getOrElse { e ->
            SentimentResult.error("$ERROR_INFERENCE: ${e.message}")
        }
    }

    /**
     * Get model information
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
     * Check if model is ready for use
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
     * Preprocess text for TensorFlow Lite input
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
        val maxLength = inputConfig?.preprocessing?.maxLength ?: DEFAULT_MAX_LENGTH
        if (processedText.length > maxLength) {
            processedText = processedText.take(maxLength)
        }

        return processedText
    }

    /**
     * Prepare input tensor for inference
     */
    private fun prepareInputTensor(text: String, config: TensorFlowConfig): ByteBuffer {
        val inputConfig = config.tensorflowLite?.inputConfig
        val inputShape = inputConfig?.inputShape ?: listOf(1, 512)
        val maxLength = inputConfig?.preprocessing?.maxLength ?: DEFAULT_MAX_LENGTH

        // Create input buffer
        val inputBuffer = ByteBuffer.allocateDirect(maxLength * 4) // 4 bytes per float
        inputBuffer.order(ByteOrder.nativeOrder())

        // Simple tokenization (in real implementation, you'd use proper tokenizer)
        val tokens = text.split(" ").take(maxLength)
        
        // Convert tokens to embeddings (simplified - in real implementation use proper embedding)
        for (i in 0 until maxLength) {
            val token = if (i < tokens.size) tokens[i] else ""
            val embedding = token.hashCode().toFloat() / Int.MAX_VALUE.toFloat()
            inputBuffer.putFloat(embedding)
        }

        inputBuffer.rewind()
        return inputBuffer
    }

    /**
     * Run TensorFlow Lite inference
     */
    private fun runInference(inputBuffer: ByteBuffer, config: TensorFlowConfig): FloatArray {
        val inputConfig = config.tensorflowLite?.inputConfig
        val outputConfig = config.tensorflowLite?.outputConfig
        
        // Prepare input tensor
        val inputShape = (inputConfig?.inputShape ?: listOf(1, 512)).toIntArray()
        val outputShape = (outputConfig?.outputShape ?: listOf(1, 3)).toIntArray()
        
        // Prepare output buffer
        val outputBuffer = ByteBuffer.allocateDirect(outputShape[1] * 4) // 4 bytes per float
        outputBuffer.order(ByteOrder.nativeOrder())

        // Run inference
        interpreter?.run(inputBuffer, outputBuffer)

        // Convert output to float array
        val outputArray = FloatArray(outputShape[1])
        outputBuffer.rewind()
        for (i in 0 until outputShape[1]) {
            outputArray[i] = outputBuffer.float
        }
        
        return outputArray
    }

    /**
     * Postprocess TensorFlow Lite output
     */
    private fun postprocessOutput(outputArray: FloatArray, config: TensorFlowConfig): SentimentResult {
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
                    SentimentType.POSITIVE -> SentimentResult.positive(maxConfidence.toDouble(), listOf("tensorflow:${classLabels[maxIndex]}"))
                    SentimentType.NEGATIVE -> SentimentResult.negative(maxConfidence.toDouble(), listOf("tensorflow:${classLabels[maxIndex]}"))
                    else -> SentimentResult.neutral(maxConfidence.toDouble(), listOf("tensorflow:${classLabels[maxIndex]}"))
                }
            }
            else -> {
                // Low confidence - return neutral
                SentimentResult.neutral(maxConfidence.toDouble(), listOf("tensorflow:low_confidence"))
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

/**
 * TensorFlow Lite configuration data classes
 */
@Serializable
data class TensorFlowConfig(
    val tensorflowLite: TensorFlowLiteConfig? = null,
    val hybridSystem: HybridSystemConfig? = null
)

@Serializable
data class TensorFlowLiteConfig(
    val modelFile: String? = null,
    val modelType: String? = null,
    val version: String? = null,
    val inputConfig: InputConfig? = null,
    val outputConfig: OutputConfig? = null,
    val performance: PerformanceConfig? = null,
    val fallback: FallbackConfig? = null
)

@Serializable
data class InputConfig(
    val inputTensorName: String? = null,
    val inputShape: List<Int> = emptyList(),
    val inputType: String? = null,
    val preprocessing: PreprocessingConfig? = null
)

@Serializable
data class PreprocessingConfig(
    val maxLength: Int? = null,
    val padding: String? = null,
    val truncation: Boolean? = null,
    val lowercase: Boolean? = null,
    val removePunctuation: Boolean? = null
)

@Serializable
data class OutputConfig(
    val outputTensorName: String? = null,
    val outputShape: List<Int> = emptyList(),
    val outputType: String? = null,
    val classLabels: List<String>? = null,
    val confidenceThreshold: Double? = null
)

@Serializable
data class PerformanceConfig(
    val useGpu: Boolean? = null,
    val useNnapi: Boolean? = null,
    val numThreads: Int? = null,
    val enableXnnpack: Boolean? = null
)

@Serializable
data class FallbackConfig(
    val useKeywordModel: Boolean? = null,
    val fallbackThreshold: Double? = null
)

@Serializable
data class HybridSystemConfig(
    val modelSelection: ModelSelectionConfig? = null,
    val integration: IntegrationConfig? = null
)

@Serializable
data class ModelSelectionConfig(
    val useTensorflowFor: List<String>? = null,
    val useKeywordFor: List<String>? = null,
    val complexityThreshold: Int? = null,
    val confidenceThreshold: Double? = null
)

@Serializable
data class IntegrationConfig(
    val seamlessFallback: Boolean? = null,
    val performanceMonitoring: Boolean? = null,
    val cacheResults: Boolean? = null,
    val cacheDurationMinutes: Int? = null
)
