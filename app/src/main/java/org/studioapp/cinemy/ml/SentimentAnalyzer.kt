package org.studioapp.cinemy.ml

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.studioapp.cinemy.ml.model.AlgorithmConfig
import org.studioapp.cinemy.ml.model.ContextBoosters
import org.studioapp.cinemy.ml.model.EnhancedModelData
import org.studioapp.cinemy.ml.model.KeywordSentimentModel
import org.studioapp.cinemy.ml.model.ModelInfo
import org.studioapp.cinemy.ml.model.ProductionModelData
import org.studioapp.cinemy.ml.model.SentimentResult
import org.studioapp.cinemy.ml.MLConstants.NO_MODELS_AVAILABLE_ERROR
import org.studioapp.cinemy.ml.MLConstants.ML_MODELS_PATH
import org.studioapp.cinemy.ml.MLConstants.WORD_SPLIT_REGEX_PATTERN
import org.studioapp.cinemy.ml.MLConstants.EMOJI_POSITIVE
import org.studioapp.cinemy.ml.MLConstants.EMOJI_NEGATIVE
import org.studioapp.cinemy.ml.MLConstants.EMOJI_NEUTRAL
import org.studioapp.cinemy.ml.MLConstants.EMOJI_MODIFIER
import org.studioapp.cinemy.ml.MLConstants.EMOJI_MOVIE
import org.studioapp.cinemy.ml.MLConstants.EMOJI_QUALITY
import org.studioapp.cinemy.ml.MLConstants.EMOJI_FAILURE
import org.studioapp.cinemy.ml.MLConstants.EMOJI_PATTERN
import org.studioapp.cinemy.ml.MLConstants.EMOJI_NEGATIVE_PATTERN
import org.studioapp.cinemy.ml.MLConstants.SENTIMENT_POSITIVE
import org.studioapp.cinemy.ml.MLConstants.SENTIMENT_NEGATIVE
import org.studioapp.cinemy.ml.MLConstants.SENTIMENT_NEUTRAL
import org.studioapp.cinemy.ml.mlfactory.KeywordFactory.createMultilingualKeywords
import org.studioapp.cinemy.ml.mlfactory.ContextBoostersFactory.createMovieContextBoosters
import org.studioapp.cinemy.ml.mlfactory.ContextBoostersFactory.createFromEnhancedModel
import org.studioapp.cinemy.ml.mlfactory.IntensityModifiersFactory.createIntensityModifiers
import org.studioapp.cinemy.ml.mlfactory.SimpleKeywordModelFactory
import org.studioapp.cinemy.ml.mlfactory.Algorithm
import org.studioapp.cinemy.ml.mlmodels.LiteRTSentimentModel
import org.studioapp.cinemy.ml.mlmodels.TensorFlowSentimentModel
import java.lang.ref.WeakReference
import kotlin.math.abs

/**
 * Hybrid sentiment analyzer for Cinemy
 * Uses TensorFlow Lite as primary model with keyword-based fallback
 * Provides ML-based sentiment analysis with automatic model selection
 */
class SentimentAnalyzer private constructor(private val context: Context) {

    private var keywordModel: KeywordSentimentModel? = null
    private var tensorFlowModel: TensorFlowSentimentModel? = null
    private var liteRTModel: LiteRTSentimentModel? = null
    private var adaptiveRuntime: AdaptiveMLRuntime? = null
    var isInitialized = false
        private set

    companion object {
        @Volatile
        private var INSTANCE: WeakReference<SentimentAnalyzer>? = null

        /**
         * Gets singleton instance of SentimentAnalyzer
         * Uses WeakReference to prevent memory leaks
         * @param context Android context
         * @return SentimentAnalyzer instance
         */
        fun getInstance(context: Context): SentimentAnalyzer {
            val current = INSTANCE?.get()
            if (current != null) {
                return current
            }

            return synchronized(this) {
                val existing = INSTANCE?.get()
                if (existing != null) {
                    existing
                } else {
                    val newInstance = SentimentAnalyzer(context.applicationContext)
                    INSTANCE = WeakReference(newInstance)
                    newInstance
                }
            }
        }

        /**
         * Note: TensorFlow Lite model file is managed by TensorFlowSentimentModel class.
         * It uses "production_sentiment_full_manual.tflite" as defined in TensorFlowSentimentModel.MODEL_FILE
         *
         * TensorFlow model is PRIMARY for all build variants.
         * Keyword model serves as FALLBACK when TensorFlow has low confidence or is unavailable.
         */

    }

    /**
     * Initializes hybrid sentiment analyzer with adaptive runtime selection
     *
     * This method performs the following steps:
     * 1. Loads hybrid system configuration
     * 2. Initializes adaptive ML runtime for optimal performance
     * 3. Sets up fallback mechanisms for reliability
     * 4. Configures hardware-optimized model selection
     *
     * The adaptive runtime automatically selects the best available ML runtime
     * based on device hardware capabilities (LiteRT, TensorFlow Lite, or keyword fallback).
     *
     * @return true if initialization was successful, false otherwise
     */
    suspend fun initialize(): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            if (isInitialized) return@withContext true

            // Initialize adaptive ML runtime for optimal performance
            adaptiveRuntime = AdaptiveMLRuntime.getInstance(context)
            adaptiveRuntime?.initialize()

            // Initialize LiteRT model for best performance (if available)
            liteRTModel = LiteRTSentimentModel.getInstance(context)
            liteRTModel?.initialize()

            // Initialize TensorFlow Lite model as backup
            tensorFlowModel = TensorFlowSentimentModel.getInstance(context)
            tensorFlowModel?.initialize()

            // Initialize keyword model as fallback
            initializeKeywordModel()

            isInitialized = true
            true
        }.getOrElse { e ->
            false
        }
    }

    /**
     * Initializes multilingual production model from assets
     * Falls back to simple model only if production model fails to load
     */
    private suspend fun initializeKeywordModel() {
        val modelJson = runCatching {
            context.assets.open("$ML_MODELS_PATH$MLConstants.KEYWORD_MODEL_FILE").use { inputStream ->
                inputStream.bufferedReader().readText()
            }
        }.getOrNull()

        keywordModel = if (modelJson != null) {
            loadModelFromJson(modelJson, MLConstants.KEYWORD_MODEL_FILE)
        } else {
            // Only use simple model if production model file is not found
            SimpleKeywordModelFactory().createSimpleModel()
        }
    }

    /**
     * Analyzes sentiment using hybrid approach
     * Prioritizes TensorFlow Lite model, falls back to keyword model if needed
     * @param text Input text to analyze
     * @return SentimentResult with sentiment classification and confidence
     */
    suspend fun analyzeSentiment(text: String): SentimentResult = withContext(Dispatchers.Default) {
        if (!isInitialized) {
            return@withContext SentimentResult.error(MLConstants.ERROR_ANALYZER_NOT_INITIALIZED)
        }

        if (text.isBlank()) {
            return@withContext SentimentResult.neutral()
        }

        runCatching {
            // Try adaptive runtime first (optimal performance)
            val adaptiveResult = adaptiveRuntime?.analyzeSentiment(text)
            if (adaptiveResult != null && adaptiveResult.isSuccess && adaptiveResult.confidence > 0.6) {
                return@withContext adaptiveResult
            }

            // Try LiteRT model directly (best performance)
            if (liteRTModel?.isReady() == true) {
                val liteRTResult = liteRTModel!!.analyzeSentiment(text)
                if (liteRTResult.isSuccess && liteRTResult.confidence > 0.6) {
                    return@withContext liteRTResult
                }
            }

            // Fallback to TensorFlow Lite model
            if (tensorFlowModel?.isReady() == true) {
                val tensorFlowResult = tensorFlowModel!!.analyzeSentiment(text)
                if (tensorFlowResult.isSuccess && tensorFlowResult.confidence > 0.6) {
                    return@withContext tensorFlowResult
                }
            }

            // Last resort: keyword model
            if (keywordModel != null) {
                analyzeWithKeywords(text, keywordModel!!)
            } else {
                SentimentResult.error(NO_MODELS_AVAILABLE_ERROR)
            }
        }.getOrElse { e ->
            SentimentResult.error("$MLConstants.ERROR_ANALYSIS_ERROR${e.message}")
        }
    }

    /**
     * Analyzes sentiment for multiple texts in batch
     * @param texts List of texts to analyze
     * @return List of SentimentResult objects
     */
    suspend fun analyzeBatch(texts: List<String>): List<SentimentResult> =
        withContext(Dispatchers.Default) {
            texts.map { text -> analyzeSentiment(text) }
        }


    /**
     * Load model from JSON string
     */
    private fun loadModelFromJson(jsonString: String, fileName: String): KeywordSentimentModel {
        return runCatching {
            val json = Json { ignoreUnknownKeys = true }

            when (fileName) {
                MLConstants.KEYWORD_MODEL_FILE -> {
                    loadProductionModel(json, jsonString)
                }

                else -> {
                    // Fallback to enhanced model format
                    loadEnhancedModel(json, jsonString)
                }
            }
        }.getOrElse { e ->
            SimpleKeywordModelFactory().createSimpleModel()
        }
    }


    /**
     * Load production model format from multilingual_sentiment_production.json
     */
    private fun loadProductionModel(json: Json, jsonString: String): KeywordSentimentModel {
        return runCatching {
            val modelData = json.decodeFromString<ProductionModelData>(jsonString)
            createProductionModel(modelData)
        }.getOrElse { e ->
            // If production model fails to load, create simple model as last resort
            SimpleKeywordModelFactory().createSimpleModel()
        }
    }

    /**
     * Load enhanced model format (fallback)
     */
    private fun loadEnhancedModel(json: Json, jsonString: String): KeywordSentimentModel {
        val modelData = json.decodeFromString<EnhancedModelData>(jsonString)

        val modelInfo = ModelInfo(
            type = modelData.model_info.type,
            version = modelData.model_info.version,
            language = modelData.model_info.language,
            accuracy = modelData.model_info.accuracy,
            speed = modelData.model_info.speed
        )

        val algorithm = Algorithm.createFromEnhancedModel(modelData)

        val contextBoosters = createFromEnhancedModel(modelData)

        return KeywordSentimentModel(
            modelInfo = modelInfo,
            positiveKeywords = modelData.positive_keywords,
            negativeKeywords = modelData.negative_keywords,
            neutralIndicators = modelData.neutral_indicators,
            intensityModifiers = modelData.intensity_modifiers,
            contextBoosters = contextBoosters,
            algorithm = algorithm
        )
    }

    /**
     * Create production model from multilingual_sentiment_production.json
     */
    private fun createProductionModel(modelData: ProductionModelData): KeywordSentimentModel {
        val modelInfo = ModelInfo(
            type = modelData.model_info.type,
            version = modelData.model_info.version,
            language = modelData.model_info.languages.joinToString(", "),
            accuracy = "${(modelData.model_info.accuracy * 100).toInt()}%",
            speed = "very_fast"
        )

        // Use production model weights for advanced analysis
        val algorithm = Algorithm.PRODUCTION_CONFIG

        // Create multilingual keywords based on production model
        val positiveKeywords = createMultilingualKeywords(SENTIMENT_POSITIVE)
        val negativeKeywords = createMultilingualKeywords(SENTIMENT_NEGATIVE)
        val neutralIndicators = createMultilingualKeywords(SENTIMENT_NEUTRAL)

        val intensityModifiers = createIntensityModifiers()

        val contextBoosters = createMovieContextBoosters()

        return KeywordSentimentModel(
            modelInfo = modelInfo,
            positiveKeywords = positiveKeywords,
            negativeKeywords = negativeKeywords,
            neutralIndicators = neutralIndicators,
            intensityModifiers = intensityModifiers,
            contextBoosters = contextBoosters,
            algorithm = algorithm
        )
    }



    /**
     * Main keyword analysis algorithm with fallback
     */
    private fun analyzeWithKeywords(text: String, model: KeywordSentimentModel): SentimentResult {
        return runCatching {
            // Use enhanced algorithm
            analyzeWithEnhancedKeywords(text, model)
        }.getOrElse { e ->
            // Fallback to simple algorithm if something went wrong
            analyzeWithSimpleKeywords(text, model)
        }
    }

    /**
     * Enhanced analysis algorithm with support for all new features
     */
    private fun analyzeWithEnhancedKeywords(
        text: String,
        model: KeywordSentimentModel
    ): SentimentResult {
        val textLower = text.lowercase()
        val words = textLower.split(Regex(WORD_SPLIT_REGEX_PATTERN)).filter { it.isNotBlank() }

        var positiveScore = 0.0
        var negativeScore = 0.0
        var neutralScore = 0.0
        var foundWords = mutableListOf<String>()

        // Basic keyword analysis
        for (word in words) {
            when {
                model.positiveKeywords.contains(word) -> {
                    positiveScore += 1.0
                    foundWords.add("$EMOJI_POSITIVE$word")
                }

                model.negativeKeywords.contains(word) -> {
                    negativeScore += 1.0
                    foundWords.add("$EMOJI_NEGATIVE$word")
                }

                model.neutralIndicators?.contains(word) == true -> {
                    neutralScore += 0.5
                    foundWords.add("$EMOJI_NEUTRAL$word")
                }
            }
        }

        // Apply intensity modifiers
        model.intensityModifiers?.forEach { (modifier, multiplier) ->
            if (textLower.contains(modifier)) {
                when {
                    multiplier > 1.0 -> {
                        positiveScore *= multiplier
                        negativeScore *= multiplier
                    }

                    multiplier < 0 -> {
                        // Invert values for negative modifiers
                        val temp = positiveScore
                        positiveScore = negativeScore * abs(multiplier)
                        negativeScore = temp * abs(multiplier)
                    }

                    else -> {
                        positiveScore *= multiplier
                        negativeScore *= multiplier
                    }
                }
                    foundWords.add("$EMOJI_MODIFIER$modifier")
            }
        }

        // Context boosters (if available in model)
        model.contextBoosters?.let { boosters ->
            boosters.movieTerms?.forEach { term ->
                if (textLower.contains(term)) {
                    foundWords.add("$EMOJI_MOVIE$term")
                }
            }

            boosters.positiveContext?.forEach { context ->
                if (textLower.contains(context)) {
                    positiveScore += 0.3
                    foundWords.add("$EMOJI_QUALITY$context")
                }
            }

            boosters.negativeContext?.forEach { context ->
                if (textLower.contains(context)) {
                    negativeScore += 0.3
                    foundWords.add("$EMOJI_FAILURE$context")
                }
            }
        }

        // Enhanced context pattern matching (v2.0 feature)
        model.contextBoosters?.let { boosters ->
            boosters.positiveContext?.forEach { pattern ->
                if (textLower.contains(pattern)) {
                    positiveScore += 0.5 // Higher bonus for context patterns
                    foundWords.add("$EMOJI_PATTERN$pattern")
                }
            }

            boosters.negativeContext?.forEach { pattern ->
                if (textLower.contains(pattern)) {
                    negativeScore += 0.5 // Higher bonus for context patterns
                    foundWords.add("$EMOJI_NEGATIVE_PATTERN$pattern")
                }
            }
        }

        // Determine result with enhanced logic
        val algorithm = model.algorithm
        val totalScore = positiveScore + negativeScore + neutralScore

        return when {
            totalScore == 0.0 -> SentimentResult.neutral()

            positiveScore > negativeScore && positiveScore > neutralScore -> {
                val confidence = minOf(
                    algorithm.maxConfidence ?: 0.9,
                    algorithm.baseConfidence + (positiveScore - maxOf(
                        negativeScore,
                        neutralScore
                    )) * 0.15
                )
                SentimentResult.positive(confidence, foundWords)
            }

            negativeScore > positiveScore && negativeScore > neutralScore -> {
                val confidence = minOf(
                    algorithm.maxConfidence ?: 0.9,
                    algorithm.baseConfidence + (negativeScore - maxOf(
                        positiveScore,
                        neutralScore
                    )) * 0.15
                )
                SentimentResult.negative(confidence, foundWords)
            }

            else -> {
                val confidence = algorithm.baseConfidence
                SentimentResult.neutral(confidence, foundWords)
            }
        }
    }

    /**
     * Simple algorithm as fallback for backward compatibility
     */
    private fun analyzeWithSimpleKeywords(
        text: String,
        model: KeywordSentimentModel
    ): SentimentResult {
        val textLower = text.lowercase()
        val words = textLower.split(Regex(WORD_SPLIT_REGEX_PATTERN)).filter { it.isNotBlank() }

        var positiveScore = 0.0
        var negativeScore = 0.0
        var foundWords = mutableListOf<String>()

        for (word in words) {
            if (model.positiveKeywords.contains(word)) {
                positiveScore += 1.0
                foundWords.add("+$word")
            }
            if (model.negativeKeywords.contains(word)) {
                negativeScore += 1.0
                foundWords.add("-$word")
            }
        }

        val algorithm = model.algorithm

        return when {
            positiveScore > negativeScore -> {
                val confidence = minOf(
                    algorithm.maxConfidence ?: 0.9,
                    algorithm.baseConfidence + (positiveScore - negativeScore) * 0.1
                )
                SentimentResult.positive(confidence, foundWords)
            }

            negativeScore > positiveScore -> {
                val confidence = minOf(
                    algorithm.maxConfidence ?: 0.9,
                    algorithm.baseConfidence + (negativeScore - positiveScore) * 0.1
                )
                SentimentResult.negative(confidence, foundWords)
            }

            else -> SentimentResult.neutral()
        }
    }


    /**
     * Clean up resources and clear singleton reference
     */
    fun cleanup() {
        adaptiveRuntime?.cleanup()
        adaptiveRuntime = null
        liteRTModel?.cleanup()
        liteRTModel = null
        tensorFlowModel?.cleanup()
        tensorFlowModel = null
        keywordModel = null
        isInitialized = false
        // Clear the singleton reference to prevent memory leaks
        synchronized(this) {
            INSTANCE?.clear()
            INSTANCE = null
        }
    }
}

