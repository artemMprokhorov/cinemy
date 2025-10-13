package org.studioapp.cinemy.ml

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.studioapp.cinemy.BuildConfig
import org.studioapp.cinemy.ml.model.SentimentResult
import org.studioapp.cinemy.ml.model.SentimentType
import org.studioapp.cinemy.ml.model.KeywordSentimentModel
import org.studioapp.cinemy.ml.model.ModelInfo
import org.studioapp.cinemy.ml.model.AlgorithmConfig
import org.studioapp.cinemy.ml.model.ContextBoosters
import org.studioapp.cinemy.ml.model.EnhancedModelData
import org.studioapp.cinemy.ml.model.ProductionModelData
import org.studioapp.cinemy.ml.model.TensorFlowConfig
import org.studioapp.cinemy.ml.model.HybridSystemConfig
import kotlin.math.abs
import java.lang.ref.WeakReference

/**
 * Hybrid sentiment analyzer for Cinemy
 * Uses TensorFlow Lite as primary model with keyword-based fallback
 * Provides ML-based sentiment analysis with automatic model selection
 */
class SentimentAnalyzer private constructor(private val context: Context) {

    private var keywordModel: KeywordSentimentModel? = null
    private var tensorFlowModel: TensorFlowSentimentModel? = null
    private var isInitialized = false
    private var hybridConfig: HybridSystemConfig? = null

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
        private const val KEYWORD_MODEL_FILE = "multilingual_sentiment_production.json"


        // Error messages
        private const val ERROR_ANALYZER_NOT_INITIALIZED = "Analyzer not initialized"
        private const val ERROR_ANALYSIS_ERROR = "Analysis error: "
    }

    /**
     * Initializes hybrid sentiment analyzer
     * Loads TensorFlow Lite model as primary and keyword model as fallback
     * @return Boolean indicating if initialization was successful
     */
    suspend fun initialize(): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            if (isInitialized) return@withContext true

            // Load hybrid configuration
            hybridConfig = loadHybridConfig()

            // Initialize TensorFlow Lite model as primary
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
            context.assets.open("ml_models/$KEYWORD_MODEL_FILE").use { inputStream ->
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
            // Use TensorFlow Lite model if available and ready
            if (tensorFlowModel?.isReady() == true) {
                val tensorFlowResult = tensorFlowModel!!.analyzeSentiment(text)

                // Check if fallback to keyword model is needed
                if (shouldFallbackToKeyword(tensorFlowResult) && keywordModel != null) {
                    analyzeWithKeywords(text, keywordModel!!)
                } else {
                    tensorFlowResult
                }
            } else if (keywordModel != null) {
                // Use keyword model if TensorFlow is not available
                analyzeWithKeywords(text, keywordModel!!)
            } else {
                SentimentResult.error("No sentiment model available")
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
     * Checks if TensorFlow model is available and ready for inference
     * @return Boolean indicating TensorFlow model availability
     */
    fun isTensorFlowAvailable(): Boolean = tensorFlowModel?.isReady() ?: false



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
        val positiveKeywords = createMultilingualKeywords("positive")
        val negativeKeywords = createMultilingualKeywords("negative")
        val neutralIndicators = createMultilingualKeywords("neutral")

        val intensityModifiers = mapOf(
            "absolutely" to 1.5,
            "completely" to 1.4,
            "totally" to 1.3,
            "extremely" to 1.3,
            "incredibly" to 1.3,
            "very" to 1.2,
            "really" to 1.1,
            "pretty" to 0.8,
            "somewhat" to 0.7,
            "slightly" to 0.6,
            "not" to -1.0,
            "never" to -1.0,
            "barely" to -0.5
        )

        val contextBoosters = ContextBoosters(
            movieTerms = listOf(
                "cinematography", "acting", "plot", "story", "director", "performance",
                "script", "dialogue", "visuals", "effects", "soundtrack", "editing"
            ),
            positiveContext = listOf(
                "masterpiece", "artistry", "brilliant", "genius", "innovative",
                "groundbreaking", "revolutionary", "timeless", "classic"
            ),
            negativeContext = listOf(
                "flop", "disaster", "failure", "ruined", "destroyed", "butchered",
                "mangled", "torture", "nightmare"
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
        val words = textLower.split(Regex("\\W+")).filter { it.isNotBlank() }

        var positiveScore = 0.0
        var negativeScore = 0.0
        var neutralScore = 0.0
        var foundWords = mutableListOf<String>()

        // Basic keyword analysis
        for (word in words) {
            when {
                model.positiveKeywords.contains(word) -> {
                    positiveScore += 1.0
                    foundWords.add("+$word")
                }

                model.negativeKeywords.contains(word) -> {
                    negativeScore += 1.0
                    foundWords.add("-$word")
                }

                model.neutralIndicators?.contains(word) == true -> {
                    neutralScore += 0.5
                    foundWords.add("~$word")
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
                foundWords.add("*$modifier")
            }
        }

        // Context boosters (if available in model)
        model.contextBoosters?.let { boosters ->
            boosters.movieTerms?.forEach { term ->
                if (textLower.contains(term)) {
                    foundWords.add("🎬$term")
                }
            }

            boosters.positiveContext?.forEach { context ->
                if (textLower.contains(context)) {
                    positiveScore += 0.3
                    foundWords.add("✨$context")
                }
            }

            boosters.negativeContext?.forEach { context ->
                if (textLower.contains(context)) {
                    negativeScore += 0.3
                    foundWords.add("💥$context")
                }
            }
        }

        // Enhanced context pattern matching (v2.0 feature)
        model.contextBoosters?.let { boosters ->
            boosters.positiveContext?.forEach { pattern ->
                if (textLower.contains(pattern)) {
                    positiveScore += 0.5 // Higher bonus for context patterns
                    foundWords.add("🔥$pattern")
                }
            }

            boosters.negativeContext?.forEach { pattern ->
                if (textLower.contains(pattern)) {
                    negativeScore += 0.5 // Higher bonus for context patterns
                    foundWords.add("💀$pattern")
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
        val words = textLower.split(Regex("\\W+")).filter { it.isNotBlank() }

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
     * Load hybrid system configuration
     */
    private fun loadHybridConfig(): HybridSystemConfig? {
        return runCatching {
            val configJson = context.assets.open("ml_models/android_integration_config.json")
                .use { inputStream ->
                    inputStream.bufferedReader().readText()
                }

            val json = Json { ignoreUnknownKeys = true }
            val tfConfig = json.decodeFromString<TensorFlowConfig>(configJson)
            tfConfig.hybridSystem
        }.getOrNull()
    }


    /**
     * Determines if TensorFlow result should fallback to keyword model
     * Checks confidence threshold and low confidence indicators
     * @param tensorFlowResult Result from TensorFlow model
     * @return Boolean indicating if fallback is needed
     */
    private fun shouldFallbackToKeyword(tensorFlowResult: SentimentResult): Boolean {
        val config = hybridConfig?.modelSelection ?: return false

        // Check if confidence is below threshold (default: 0.7)
        val confidenceThreshold = config.confidenceThreshold ?: 0.7
        if (tensorFlowResult.confidence < confidenceThreshold) {
            return true
        }

        // Check if TensorFlow indicates low confidence in keywords
        if (tensorFlowResult.foundKeywords.any { it.contains("low_confidence") }) {
            return true
        }

        return false
    }

    /**
     * Clean up resources and clear singleton reference
     */
    fun cleanup() {
        tensorFlowModel?.cleanup()
        isInitialized = false
        // Clear the singleton reference to prevent memory leaks
        synchronized(this) {
            INSTANCE?.clear()
            INSTANCE = null
        }
    }
}

