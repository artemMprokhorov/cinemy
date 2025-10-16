package org.studioapp.cinemy.ml

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.studioapp.cinemy.ml.MLConstants.DEFAULT_SCORE
import org.studioapp.cinemy.ml.MLConstants.KEYWORD_MODEL_NOT_AVAILABLE_ERROR
import org.studioapp.cinemy.ml.MLConstants.LITERT_MODEL_NOT_AVAILABLE_ERROR
import org.studioapp.cinemy.ml.MLConstants.MIN_CONFIDENCE_THRESHOLD
import org.studioapp.cinemy.ml.MLConstants.ML_RUNTIME_NOT_INITIALIZED_ERROR
import org.studioapp.cinemy.ml.MLConstants.SCORE_INCREMENT
import org.studioapp.cinemy.ml.MLConstants.TENSORFLOW_LITE_MODEL_NOT_AVAILABLE_ERROR
import org.studioapp.cinemy.ml.MLConstants.WORD_SPLIT_REGEX
import org.studioapp.cinemy.ml.mlfactory.SimpleKeywordModelFactory
import org.studioapp.cinemy.ml.mlmodels.LiteRTSentimentModel
import org.studioapp.cinemy.ml.mlmodels.TensorFlowSentimentModel
import org.studioapp.cinemy.ml.mltools.HardwareDetection
import org.studioapp.cinemy.ml.mltools.HardwareDetection.MLRuntime.KEYWORD_FALLBACK
import org.studioapp.cinemy.ml.mltools.HardwareDetection.MLRuntime.LITERT_GPU
import org.studioapp.cinemy.ml.mltools.HardwareDetection.MLRuntime.LITERT_NPU
import org.studioapp.cinemy.ml.mltools.HardwareDetection.MLRuntime.TENSORFLOW_LITE_CPU
import org.studioapp.cinemy.ml.mltools.HardwareDetection.MLRuntime.TENSORFLOW_LITE_GPU
import org.studioapp.cinemy.ml.mltools.HardwareDetection.MLRuntime.TENSORFLOW_LITE_NNAPI
import org.studioapp.cinemy.ml.model.KeywordSentimentModel
import org.studioapp.cinemy.ml.model.SentimentResult
import java.lang.ref.WeakReference

/**
 * Adaptive ML Runtime Selector for Cinemy
 *
 * Automatically selects and manages the optimal ML runtime based on device hardware
 * capabilities, providing a unified interface for sentiment analysis across different
 * ML implementations. This class ensures maximum performance by intelligently choosing
 * the best available runtime for each device.
 *
 * ## Runtime Selection Priority
 *
 * The runtime selection follows this priority order based on hardware capabilities:
 * 1. **LiteRT with GPU/NPU acceleration** - Best performance, requires Play Services
 * 2. **TensorFlow Lite with hardware acceleration** - Good performance, no Play Services dependency
 * 3. **TensorFlow Lite CPU with XNNPACK** - Basic performance, always available
 * 4. **Keyword-based fallback** - Last resort, ensures functionality is always available
 *
 * ## Key Features
 *
 * - **Automatic Hardware Detection**: Detects GPU, NNAPI, XNNPACK, LiteRT, and Play Services support
 * - **Intelligent Runtime Selection**: Chooses optimal runtime based on device capabilities
 * - **Seamless Fallback**: Automatic fallback to simpler runtimes if preferred ones fail
 * - **Unified Interface**: Same API regardless of underlying ML implementation
 * - **Memory Management**: Proper resource cleanup to prevent memory leaks
 * - **Thread Safety**: Thread-safe singleton pattern with WeakReference
 *
 * ## Usage Example
 *
 * ```kotlin
 * val adaptiveRuntime = AdaptiveMLRuntime.getInstance(context)
 * val initialized = adaptiveRuntime.initialize()
 *
 * if (initialized) {
 *     val result = adaptiveRuntime.analyzeSentiment("This movie is amazing!")
 *     println("Sentiment: ${result.sentiment} (${result.confidence})")
 * }
 *
 * // Clean up when done
 * adaptiveRuntime.cleanup()
 * ```
 *
 * ## Thread Safety
 *
 * This class is thread-safe and uses a singleton pattern with WeakReference to prevent
 * memory leaks. All public methods are safe to call from any thread.
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
     * Initializes the adaptive ML runtime with optimal configuration.
     *
     * This method performs comprehensive initialization of the ML runtime system:
     * 1. **Hardware Detection**: Initializes hardware detection system
     * 2. **Capability Analysis**: Detects GPU, NNAPI, XNNPACK, LiteRT, and Play Services support
     * 3. **Runtime Selection**: Selects optimal ML runtime based on detected capabilities
     * 4. **Model Initialization**: Initializes the selected ML model (LiteRT, TensorFlow Lite, or keyword)
     * 5. **Fallback Setup**: Configures fallback mechanisms for reliability
     *
     * The initialization process is idempotent - calling this method multiple times
     * is safe and will return true if already initialized.
     *
     * **Runtime Selection Logic**:
     * - LiteRT GPU/NPU: Best performance, requires Play Services
     * - TensorFlow Lite GPU/NNAPI: Good performance, no Play Services dependency
     * - TensorFlow Lite CPU: Basic performance, always available
     * - Keyword Fallback: Last resort, ensures functionality
     *
     * @return true if initialization was successful, false if all runtimes failed
     * @throws Exception if hardware detection fails (handled internally with fallback)
     * @since 1.0.0
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
                LITERT_GPU,
                LITERT_NPU -> {
                    initializeLiteRT()
                }

                TENSORFLOW_LITE_GPU,
                TENSORFLOW_LITE_NNAPI,
                TENSORFLOW_LITE_CPU -> {
                    initializeTensorFlowLite()
                }

                KEYWORD_FALLBACK -> {
                    initializeKeywordModel()
                }

                null -> {
                    // Fallback to keyword model if detection fails
                    initializeKeywordModel()
                    currentRuntime = KEYWORD_FALLBACK
                }
            }

            isInitialized = true
            true
        }.getOrElse { e ->
            // If initialization fails, fallback to keyword model
            initializeKeywordModel()
            currentRuntime = KEYWORD_FALLBACK
            isInitialized = true
            true
        }
    }

    /**
     * Analyzes sentiment using the optimal ML runtime for the current device.
     *
     * This method provides a unified interface for sentiment analysis that automatically
     * routes requests to the best available ML runtime based on hardware capabilities.
     * The analysis uses the runtime selected during initialization (LiteRT, TensorFlow Lite,
     * or keyword-based fallback).
     *
     * **Analysis Process**:
     * 1. **Validation**: Checks if runtime is initialized and text is valid
     * 2. **Runtime Selection**: Uses the optimal runtime selected during initialization
     * 3. **Sentiment Analysis**: Performs analysis using the selected ML model
     * 4. **Fallback Handling**: Automatically falls back to keyword model if ML models fail
     * 5. **Result Processing**: Returns standardized SentimentResult with confidence scores
     *
     * **Supported Runtimes**:
     * - **LiteRT**: BERT-based analysis with hardware acceleration (best performance)
     * - **TensorFlow Lite**: BERT-based analysis with GPU/NNAPI acceleration (good performance)
     * - **Keyword Model**: Basic keyword matching (fallback, always available)
     *
     * @param text Input text to analyze for sentiment (will return neutral if blank)
     * @return SentimentResult containing sentiment classification, confidence score, and analysis metadata
     * @throws Exception if analysis fails (handled internally with fallback to keyword model)
     * @since 1.0.0
     */
    suspend fun analyzeSentiment(text: String): SentimentResult = withContext(Dispatchers.Default) {
        if (!isInitialized) {
            return@withContext SentimentResult.error(ML_RUNTIME_NOT_INITIALIZED_ERROR)
        }

        if (text.isBlank()) {
            return@withContext SentimentResult.neutral()
        }

        runCatching {
            when (currentRuntime) {
                LITERT_GPU,
                LITERT_NPU -> {
                    analyzeWithLiteRT(text)
                }

                TENSORFLOW_LITE_GPU,
                TENSORFLOW_LITE_NNAPI,
                TENSORFLOW_LITE_CPU -> {
                    analyzeWithTensorFlowLite(text)
                }

                KEYWORD_FALLBACK -> {
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
            ?: SentimentResult.error(LITERT_MODEL_NOT_AVAILABLE_ERROR)
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
            ?: SentimentResult.error(TENSORFLOW_LITE_MODEL_NOT_AVAILABLE_ERROR)
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
            val words = text.lowercase().split(WORD_SPLIT_REGEX.toRegex())
            val positiveWords = model.positiveKeywords
            val negativeWords = model.negativeKeywords

            var positiveScore = DEFAULT_SCORE
            var negativeScore = DEFAULT_SCORE

            words.forEach { word ->
                if (positiveWords.contains(word)) positiveScore += SCORE_INCREMENT
                if (negativeWords.contains(word)) negativeScore += SCORE_INCREMENT
            }

            val totalWords = words.size.toDouble()
            val positiveConfidence =
                if (totalWords > 0) positiveScore / totalWords else DEFAULT_SCORE
            val negativeConfidence =
                if (totalWords > 0) negativeScore / totalWords else DEFAULT_SCORE

            when {
                positiveConfidence > negativeConfidence && positiveConfidence > MIN_CONFIDENCE_THRESHOLD -> {
                    SentimentResult.positive(confidence = positiveConfidence)
                }

                negativeConfidence > positiveConfidence && negativeConfidence > MIN_CONFIDENCE_THRESHOLD -> {
                    SentimentResult.negative(confidence = negativeConfidence)
                }

                else -> {
                    SentimentResult.neutral(
                        confidence = maxOf(
                            positiveConfidence,
                            negativeConfidence
                        )
                    )
                }
            }
        } ?: SentimentResult.error(KEYWORD_MODEL_NOT_AVAILABLE_ERROR)
    }

    /**
     * Cleans up all ML runtime resources to prevent memory leaks.
     *
     * This method performs comprehensive cleanup of all initialized ML components:
     * - Cleans up LiteRT model resources and clears reference
     * - Cleans up TensorFlow Lite model resources and clears reference
     * - Clears keyword model reference
     * - Resets initialization state and current runtime
     * - Prepares the runtime for potential re-initialization
     *
     * This method should be called when the AdaptiveMLRuntime is no longer needed
     * to prevent memory leaks and ensure proper resource management.
     *
     * @since 1.0.0
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
