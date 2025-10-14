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
import org.studioapp.cinemy.ml.MLConstants.KEYWORD_MODEL_FILE
import org.studioapp.cinemy.ml.MLConstants.ERROR_ANALYZER_NOT_INITIALIZED
import org.studioapp.cinemy.ml.MLConstants.ERROR_ANALYSIS_ERROR
import org.studioapp.cinemy.ml.MLConstants.NO_MODELS_AVAILABLE_ERROR
import org.studioapp.cinemy.ml.MLConstants.ML_MODELS_PATH
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_ABSOLUTELY
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_COMPLETELY
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_TOTALLY
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_EXTREMELY
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_INCREDIBLY
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_VERY
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_REALLY
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_PRETTY
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_SOMEWHAT
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_SLIGHTLY
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_NOT
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_NEVER
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_BARELY
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_CINEMATOGRAPHY
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_ACTING
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_PLOT
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_STORY
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_DIRECTOR
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_PERFORMANCE
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_SCRIPT
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_DIALOGUE
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_VISUALS
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_EFFECTS
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_SOUNDTRACK
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_EDITING
import org.studioapp.cinemy.ml.MLConstants.QUALITY_MASTERPIECE
import org.studioapp.cinemy.ml.MLConstants.QUALITY_ARTISTRY
import org.studioapp.cinemy.ml.MLConstants.QUALITY_BRILLIANT
import org.studioapp.cinemy.ml.MLConstants.QUALITY_GENIUS
import org.studioapp.cinemy.ml.MLConstants.QUALITY_INNOVATIVE
import org.studioapp.cinemy.ml.MLConstants.QUALITY_GROUNDBREAKING
import org.studioapp.cinemy.ml.MLConstants.QUALITY_REVOLUTIONARY
import org.studioapp.cinemy.ml.MLConstants.QUALITY_TIMELESS
import org.studioapp.cinemy.ml.MLConstants.QUALITY_CLASSIC
import org.studioapp.cinemy.ml.MLConstants.FAILURE_FLOP
import org.studioapp.cinemy.ml.MLConstants.FAILURE_DISASTER
import org.studioapp.cinemy.ml.MLConstants.FAILURE_FAILURE
import org.studioapp.cinemy.ml.MLConstants.FAILURE_RUINED
import org.studioapp.cinemy.ml.MLConstants.FAILURE_DESTROYED
import org.studioapp.cinemy.ml.MLConstants.FAILURE_BUTCHERED
import org.studioapp.cinemy.ml.MLConstants.FAILURE_MANGLED
import org.studioapp.cinemy.ml.MLConstants.FAILURE_TORTURE
import org.studioapp.cinemy.ml.MLConstants.FAILURE_NIGHTMARE
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

        // Keyword model file for fallback
        private const val KEYWORD_MODEL_FILE = MLConstants.KEYWORD_MODEL_FILE


        // Error messages
        private const val ERROR_ANALYZER_NOT_INITIALIZED = MLConstants.ERROR_ANALYZER_NOT_INITIALIZED
        private const val ERROR_ANALYSIS_ERROR = MLConstants.ERROR_ANALYSIS_ERROR
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
            context.assets.open("$ML_MODELS_PATH$KEYWORD_MODEL_FILE").use { inputStream ->
                inputStream.bufferedReader().readText()
            }
        }.getOrNull()

        keywordModel = if (modelJson != null) {
            loadModelFromJson(modelJson, KEYWORD_MODEL_FILE)
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
            return@withContext SentimentResult.error(ERROR_ANALYZER_NOT_INITIALIZED)
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
            SentimentResult.error("$ERROR_ANALYSIS_ERROR${e.message}")
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
                KEYWORD_MODEL_FILE -> {
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

        val algorithm = AlgorithmConfig(
            baseConfidence = modelData.algorithm.base_confidence,
            keywordWeight = 1.0,
            contextWeight = 0.3,
            modifierWeight = 0.4,
            neutralThreshold = modelData.algorithm.neutral_threshold,
            minConfidence = modelData.algorithm.min_confidence,
            maxConfidence = modelData.algorithm.max_confidence
        )

        val contextBoosters = ContextBoosters(
            movieTerms = null, // Not in v2 model
            positiveContext = modelData.context_patterns?.strong_positive,
            negativeContext = modelData.context_patterns?.strong_negative
        )

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
        val algorithm = AlgorithmConfig(
            baseConfidence = 0.8, // Higher confidence for production model
            keywordWeight = 1.0,
            contextWeight = 0.4,
            modifierWeight = 0.5,
            neutralThreshold = 0.5,
            minConfidence = 0.4,
            maxConfidence = 0.95
        )

        // Create multilingual keywords based on production model
        val positiveKeywords = createMultilingualKeywords(SENTIMENT_POSITIVE)
        val negativeKeywords = createMultilingualKeywords(SENTIMENT_NEGATIVE)
        val neutralIndicators = createMultilingualKeywords(SENTIMENT_NEUTRAL)

        val intensityModifiers = mapOf(
            MODIFIER_ABSOLUTELY to 1.5,
            MODIFIER_COMPLETELY to 1.4,
            MODIFIER_TOTALLY to 1.3,
            MODIFIER_EXTREMELY to 1.3,
            MODIFIER_INCREDIBLY to 1.3,
            MODIFIER_VERY to 1.2,
            MODIFIER_REALLY to 1.1,
            MODIFIER_PRETTY to 0.8,
            MODIFIER_SOMEWHAT to 0.7,
            MODIFIER_SLIGHTLY to 0.6,
            MODIFIER_NOT to -1.0,
            MODIFIER_NEVER to -1.0,
            MODIFIER_BARELY to -0.5
        )

        val contextBoosters = ContextBoosters(
            movieTerms = listOf(
                CONTEXT_CINEMATOGRAPHY, CONTEXT_ACTING, CONTEXT_PLOT, CONTEXT_STORY, CONTEXT_DIRECTOR, CONTEXT_PERFORMANCE,
                CONTEXT_SCRIPT, CONTEXT_DIALOGUE, CONTEXT_VISUALS, CONTEXT_EFFECTS, CONTEXT_SOUNDTRACK, CONTEXT_EDITING
            ),
            positiveContext = listOf(
                QUALITY_MASTERPIECE, QUALITY_ARTISTRY, QUALITY_BRILLIANT, QUALITY_GENIUS, QUALITY_INNOVATIVE,
                QUALITY_GROUNDBREAKING, QUALITY_REVOLUTIONARY, QUALITY_TIMELESS, QUALITY_CLASSIC
            ),
            negativeContext = listOf(
                FAILURE_FLOP, FAILURE_DISASTER, FAILURE_FAILURE, FAILURE_RUINED, FAILURE_DESTROYED, FAILURE_BUTCHERED,
                FAILURE_MANGLED, FAILURE_TORTURE, FAILURE_NIGHTMARE
            )
        )

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
     * Create multilingual keywords for production model
     */
    private fun createMultilingualKeywords(type: String): List<String> {
        return when (type) {
            "positive" -> listOf(
                // English
                "amazing", "fantastic", "great", "excellent", "wonderful", "brilliant",
                "outstanding", "superb", "magnificent", "perfect", "incredible", "awesome",
                "beautiful", "lovely", "good", "nice", "best", "favorite", "love", "enjoy",
                "phenomenal", "spectacular", "remarkable", "exceptional", "marvelous",
                "stunning", "impressive", "captivating", "engaging", "compelling",
                // Spanish
                "increíble", "fantástico", "excelente", "maravilloso", "brillante",
                "sobresaliente", "magnífico", "perfecto", "asombroso", "impresionante",
                "hermoso", "encantador", "bueno", "mejor", "favorito", "amar", "disfrutar",
                // Russian
                "потрясающий", "фантастический", "отличный", "замечательный", "блестящий",
                "выдающийся", "великолепный", "идеальный", "невероятный", "впечатляющий",
                "красивый", "прекрасный", "хороший", "лучший", "любимый", "любить", "наслаждаться"
            )

            "negative" -> listOf(
                // English
                "terrible", "awful", "horrible", "bad", "worst", "hate", "disgusting",
                "boring", "stupid", "dumb", "annoying", "frustrating", "disappointing",
                "waste", "rubbish", "garbage", "trash", "sucks", "pathetic", "lame",
                "atrocious", "dreadful", "appalling", "mediocre", "unwatchable",
                "cringe", "cheesy", "predictable", "cliché", "overrated",
                // Spanish
                "terrible", "horrible", "malo", "peor", "odiar", "asqueroso",
                "aburrido", "estúpido", "molesto", "frustrante", "decepcionante",
                "basura", "patético", "atroz", "espantoso", "mediocre",
                // Russian
                "ужасный", "отвратительный", "плохой", "худший", "ненавидеть", "мерзкий",
                "скучный", "глупый", "раздражающий", "разочаровывающий",
                "мусор", "жалкий", "отвратительный", "ужасный", "посредственный"
            )

            "neutral" -> listOf(
                // English
                "okay", "decent", "average", "fine", "acceptable", "reasonable",
                "standard", "typical", "normal", "ordinary", "mediocre", "so-so",
                // Spanish
                "bien", "decente", "promedio", "aceptable", "razonable",
                "estándar", "típico", "normal", "ordinario", "mediocre",
                // Russian
                "нормально", "приличный", "средний", "приемлемый", "разумный",
                "стандартный", "типичный", "обычный", "посредственный"
            )

            else -> emptyList()
        }
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

