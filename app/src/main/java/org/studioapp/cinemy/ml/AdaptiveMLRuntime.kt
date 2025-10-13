package org.studioapp.cinemy.ml

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.studioapp.cinemy.ml.model.SentimentResult
import org.studioapp.cinemy.ml.model.KeywordSentimentModel
import java.lang.ref.WeakReference

/**
 * Adaptive ML Runtime Selector for Cinemy
 * Automatically selects optimal ML runtime based on hardware capabilities
 * 
 * This class provides a unified interface for ML inference that automatically
 * selects the best available runtime (LiteRT, TensorFlow Lite, or keyword fallback)
 * based on device hardware capabilities.
 * 
 * The runtime selection follows this priority:
 * 1. LiteRT with GPU/NPU acceleration (best performance)
 * 2. TensorFlow Lite with hardware acceleration (good performance)
 * 3. TensorFlow Lite CPU (basic performance)
 * 4. Keyword-based fallback (last resort)
 * 
 * @author Cinemy Team
 * @since 1.0.0
 */
class AdaptiveMLRuntime private constructor(private val context: Context) {

    private var hardwareDetection: HardwareDetection? = null
    private var liteRTModel: LiteRTSentimentModel? = null
    private var tensorFlowModel: TensorFlowSentimentModel? = null
    private var keywordModel: KeywordSentimentModel? = null
    private var currentRuntime: HardwareDetection.MLRuntime? = null
    private var isInitialized = false

    companion object {
        @Volatile
        private var INSTANCE: WeakReference<AdaptiveMLRuntime>? = null

        /**
         * Gets singleton instance of AdaptiveMLRuntime
         * Uses WeakReference to prevent memory leaks
         * 
         * @param context Android context for ML runtime initialization
         * @return AdaptiveMLRuntime singleton instance
         */
        fun getInstance(context: Context): AdaptiveMLRuntime {
            val current = INSTANCE?.get()
            if (current != null) {
                return current
            }
            
            return synchronized(this) {
                val existing = INSTANCE?.get()
                if (existing != null) {
                    existing
                } else {
                    val newInstance = AdaptiveMLRuntime(context.applicationContext)
                    INSTANCE = WeakReference(newInstance)
                    newInstance
                }
            }
        }
    }

    /**
     * Initializes adaptive ML runtime
     * 
     * This method performs the following steps:
     * 1. Initializes hardware detection
     * 2. Detects hardware capabilities
     * 3. Selects optimal ML runtime
     * 4. Initializes the selected runtime
     * 5. Sets up fallback mechanisms
     * 
     * @return true if initialization was successful, false otherwise
     */
    suspend fun initialize(): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            if (isInitialized) return@withContext true

            // Initialize hardware detection
            hardwareDetection = HardwareDetection.getInstance(context)

            // Detect hardware capabilities
            val capabilities = hardwareDetection!!.detectHardwareCapabilities()
            currentRuntime = capabilities.recommendedRuntime

            // Initialize selected runtime
            when (currentRuntime) {
                HardwareDetection.MLRuntime.LITERT_GPU,
                HardwareDetection.MLRuntime.LITERT_NPU -> {
                    initializeLiteRT()
                }
                HardwareDetection.MLRuntime.TENSORFLOW_LITE_GPU,
                HardwareDetection.MLRuntime.TENSORFLOW_LITE_NNAPI,
                HardwareDetection.MLRuntime.TENSORFLOW_LITE_CPU -> {
                    initializeTensorFlowLite()
                }
                HardwareDetection.MLRuntime.KEYWORD_FALLBACK -> {
                    initializeKeywordModel()
                }
                null -> {
                    // Fallback to keyword model if detection fails
                    initializeKeywordModel()
                    currentRuntime = HardwareDetection.MLRuntime.KEYWORD_FALLBACK
                }
            }

            isInitialized = true
            true
        }.getOrElse { e ->
            // If initialization fails, fallback to keyword model
            initializeKeywordModel()
            currentRuntime = HardwareDetection.MLRuntime.KEYWORD_FALLBACK
            isInitialized = true
            true
        }
    }

    /**
     * Analyzes sentiment using adaptive runtime
     * 
     * This method automatically routes the sentiment analysis request to the
     * optimal runtime based on hardware capabilities. It provides a unified
     * interface regardless of the underlying ML implementation.
     * 
     * @param text Input text to analyze for sentiment
     * @return SentimentResult with sentiment classification and confidence
     */
    suspend fun analyzeSentiment(text: String): SentimentResult = withContext(Dispatchers.Default) {
        if (!isInitialized) {
            return@withContext SentimentResult.error("ML runtime not initialized")
        }

        if (text.isBlank()) {
            return@withContext SentimentResult.neutral()
        }

        runCatching {
            when (currentRuntime) {
                HardwareDetection.MLRuntime.LITERT_GPU,
                HardwareDetection.MLRuntime.LITERT_NPU -> {
                    analyzeWithLiteRT(text)
                }
                HardwareDetection.MLRuntime.TENSORFLOW_LITE_GPU,
                HardwareDetection.MLRuntime.TENSORFLOW_LITE_NNAPI,
                HardwareDetection.MLRuntime.TENSORFLOW_LITE_CPU -> {
                    analyzeWithTensorFlowLite(text)
                }
                HardwareDetection.MLRuntime.KEYWORD_FALLBACK -> {
                    analyzeWithKeywordModel(text)
                }
                null -> {
                    analyzeWithKeywordModel(text)
                }
            }
        }.getOrElse { e ->
            // If analysis fails, fallback to keyword model
            analyzeWithKeywordModel(text)
        }
    }

    /**
     * Analyzes sentiment for multiple texts in batch
     * 
     * Note: This is not a true batch operation - it processes texts sequentially.
     * For better performance with large batches, consider implementing parallel processing.
     * 
     * @param texts List of texts to analyze for sentiment
     * @return List of SentimentResult objects corresponding to input texts
     */
    suspend fun analyzeBatch(texts: List<String>): List<SentimentResult> =
        withContext(Dispatchers.Default) {
            texts.map { text -> analyzeSentiment(text) }
        }


    /**
     * Initializes LiteRT runtime
     * 
     * Sets up LiteRT for ML inference with hardware acceleration.
     * This is the preferred runtime for devices with Play Services support.
     * Uses the same local model as TensorFlow Lite for consistency.
     * 
     * LiteRT provides:
     * - Same local model as TensorFlow Lite (production_sentiment_full_manual.tflite)
     * - Automatic hardware acceleration (GPU, NPU, NNAPI)
     * - Optimized model execution
     * - Lower memory footprint
     * - Better performance on supported devices
     */
    private suspend fun initializeLiteRT() {
        try {
            // Initialize LiteRT sentiment model
            liteRTModel = LiteRTSentimentModel.getInstance(context)
            liteRTModel?.initialize()
            
            // If LiteRT initialization fails, fallback to TensorFlow Lite
            if (liteRTModel?.isReady() != true) {
                initializeTensorFlowLite()
            }
        } catch (e: Exception) {
            // If LiteRT fails, fallback to TensorFlow Lite
            initializeTensorFlowLite()
        }
    }

    /**
     * Initializes TensorFlow Lite runtime
     * 
     * Sets up TensorFlow Lite for ML inference with appropriate hardware
     * acceleration based on detected capabilities.
     */
    private suspend fun initializeTensorFlowLite() {
        tensorFlowModel = TensorFlowSentimentModel.getInstance(context)
        tensorFlowModel?.initialize()
    }

    /**
     * Initializes keyword model runtime
     * 
     * Sets up keyword-based sentiment analysis as a fallback option.
     * This provides basic sentiment analysis without ML model dependencies.
     */
    private suspend fun initializeKeywordModel() {
        keywordModel = SimpleKeywordModelFactory().createSimpleModel()
    }

    /**
     * Analyzes sentiment using LiteRT
     * 
     * Performs sentiment analysis using LiteRT with the same local model
     * as TensorFlow Lite but with hardware acceleration optimizations.
     * This provides the best performance for supported devices.
     * 
     * @param text Input text to analyze
     * @return SentimentResult with analysis results
     */
    private suspend fun analyzeWithLiteRT(text: String): SentimentResult {
        return liteRTModel?.analyzeSentiment(text) 
            ?: SentimentResult.error("LiteRT model not available")
    }

    /**
     * Analyzes sentiment using TensorFlow Lite
     * 
     * Performs sentiment analysis using TensorFlow Lite with appropriate
     * hardware acceleration based on detected capabilities.
     * 
     * @param text Input text to analyze
     * @return SentimentResult with analysis results
     */
    private suspend fun analyzeWithTensorFlowLite(text: String): SentimentResult {
        return tensorFlowModel?.analyzeSentiment(text) 
            ?: SentimentResult.error("TensorFlow Lite model not available")
    }

    /**
     * Analyzes sentiment using keyword model
     * 
     * Performs basic sentiment analysis using keyword matching.
     * This is the fallback option when ML models are not available.
     * 
     * @param text Input text to analyze
     * @return SentimentResult with analysis results
     */
    private suspend fun analyzeWithKeywordModel(text: String): SentimentResult {
        return keywordModel?.let { model ->
            // Simple keyword-based analysis
            val words = text.lowercase().split("\\s+".toRegex())
            val positiveWords = model.positiveKeywords
            val negativeWords = model.negativeKeywords
            
            var positiveScore = 0.0
            var negativeScore = 0.0
            
            words.forEach { word ->
                if (positiveWords.contains(word)) positiveScore += 1.0
                if (negativeWords.contains(word)) negativeScore += 1.0
            }
            
            val totalWords = words.size.toDouble()
            val positiveConfidence = if (totalWords > 0) positiveScore / totalWords else 0.0
            val negativeConfidence = if (totalWords > 0) negativeScore / totalWords else 0.0
            
            when {
                positiveConfidence > negativeConfidence && positiveConfidence > 0.3 -> {
                    SentimentResult.positive(confidence = positiveConfidence)
                }
                negativeConfidence > positiveConfidence && negativeConfidence > 0.3 -> {
                    SentimentResult.negative(confidence = negativeConfidence)
                }
                else -> {
                    SentimentResult.neutral(confidence = maxOf(positiveConfidence, negativeConfidence))
                }
            }
        } ?: SentimentResult.error("Keyword model not available")
    }

    /**
     * Cleans up resources
     * 
     * Properly cleans up all ML runtime resources to prevent memory leaks.
     * This should be called when the runtime is no longer needed.
     */
    fun cleanup() {
        liteRTModel?.cleanup()
        liteRTModel = null
        tensorFlowModel?.cleanup()
        tensorFlowModel = null
        keywordModel = null
        isInitialized = false
        currentRuntime = null
    }
}
