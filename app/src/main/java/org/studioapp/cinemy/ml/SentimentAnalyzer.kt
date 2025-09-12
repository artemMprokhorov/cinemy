package org.studioapp.cinemy.ml

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.math.abs
import org.studioapp.cinemy.BuildConfig

/**
 * Keyword-based sentiment analyzer for Cinemy
 * Fast and efficient implementation for mobile devices
 */
class SentimentAnalyzer private constructor(private val context: Context) {

    private var model: KeywordSentimentModel? = null
    private var isInitialized = false

    companion object {
        @Volatile
        private var INSTANCE: SentimentAnalyzer? = null

        fun getInstance(context: Context): SentimentAnalyzer {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SentimentAnalyzer(context.applicationContext).also { INSTANCE = it }
            }
        }

        // Model constants
        private const val MODEL_TYPE = "keyword_sentiment_analysis"
        private const val MODEL_VERSION = "2.0.0"
        private const val MODEL_LANGUAGE = "en"
        private const val MODEL_ACCURACY = "85%+"
        private const val MODEL_SPEED = "very_fast"

        // Error messages
        private const val ERROR_ANALYZER_NOT_INITIALIZED = "Analyzer not initialized"
        private const val ERROR_ANALYSIS_ERROR = "Analysis error: "

        // Performance thresholds
        private const val BASE_CONFIDENCE = 0.6
        private const val KEYWORD_WEIGHT = 1.0
        private const val CONTEXT_WEIGHT = 0.3
        private const val MODIFIER_WEIGHT = 0.4
        private const val NEUTRAL_THRESHOLD = 0.5
        private const val MIN_CONFIDENCE = 0.3
        private const val MAX_CONFIDENCE = 0.9

        // Intensity modifiers
        private const val INTENSITY_ABSOLUTELY = 1.5
        private const val INTENSITY_COMPLETELY = 1.4
        private const val INTENSITY_TOTALLY = 1.3
        private const val INTENSITY_EXTREMELY = 1.3
        private const val INTENSITY_INCREDIBLY = 1.3
        private const val INTENSITY_VERY = 1.2
        private const val INTENSITY_REALLY = 1.1
        private const val INTENSITY_PRETTY = 0.8
        private const val INTENSITY_SOMEWHAT = 0.7
        private const val INTENSITY_SLIGHTLY = 0.6
        private const val INTENSITY_NOT = -1.0
        private const val INTENSITY_NEVER = -1.0
        private const val INTENSITY_BARELY = -0.5
    }

    /**
     * Initialize analyzer (call at app startup)
     */
    suspend fun initialize(): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            if (isInitialized) return@withContext true

            // Load model based on build variant
            val modelFileName = getModelFileName()
            val modelJson = runCatching {
                context.assets.open("ml_models/$modelFileName").use { inputStream ->
                    inputStream.bufferedReader().readText()
                }
            }.getOrNull()

            model = if (modelJson != null) {
                loadModelFromJson(modelJson, modelFileName)
            } else {
                createSimpleModel()
            }

            isInitialized = true
            true
        }.getOrElse { e ->
            false
        }
    }

    /**
     * Analyze text sentiment
     */
    suspend fun analyzeSentiment(text: String): SentimentResult = withContext(Dispatchers.Default) {
        if (!isInitialized || model == null) {
            return@withContext SentimentResult.error(ERROR_ANALYZER_NOT_INITIALIZED)
        }

        if (text.isBlank()) {
            return@withContext SentimentResult.neutral()
        }

        runCatching {
            analyzeWithKeywords(text, model!!)
        }.getOrElse { e ->
            SentimentResult.error("$ERROR_ANALYSIS_ERROR${e.message}")
        }
    }

    /**
     * Batch analysis of multiple texts
     */
    suspend fun analyzeBatch(texts: List<String>): List<SentimentResult> =
        withContext(Dispatchers.Default) {
            texts.map { text -> analyzeSentiment(text) }
        }

    /**
     * Get model information
     */
    fun getModelInfo(): ModelInfo? = model?.modelInfo

    /**
     * Get model file name based on build variant
     */
    private fun getModelFileName(): String {
        return when (BuildConfig.BUILD_TYPE) {
            "debug" -> "sentiment_model_compact.json"
            "release" -> "multilingual_sentiment_production.json"
            else -> "sentiment_model_compact.json"
        }
    }

    /**
     * Load model from JSON string
     */
    private fun loadModelFromJson(jsonString: String, fileName: String): KeywordSentimentModel {
        return runCatching {
            val json = Json { ignoreUnknownKeys = true }
            
            when (fileName) {
                "sentiment_model_compact.json" -> {
                    loadCompactModel(json, jsonString)
                }
                "multilingual_sentiment_production.json" -> {
                    loadProductionModel(json, jsonString)
                }
                else -> {
                    // Fallback to enhanced model format
                    loadEnhancedModel(json, jsonString)
                }
            }
        }.getOrElse { e ->
            createSimpleModel()
        }
    }

    /**
     * Load compact model format
     */
    private fun loadCompactModel(json: Json, jsonString: String): KeywordSentimentModel {
        // For now, use simple model instead of parsing compact JSON
        return createSimpleModel()
    }

    /**
     * Load production model format
     */
    private fun loadProductionModel(json: Json, jsonString: String): KeywordSentimentModel {
        // For now, use simple model instead of parsing production JSON
        return createSimpleModel()
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
     * Create simple model for testing
     */
    private fun createSimpleModel(): KeywordSentimentModel {
        val modelInfo = ModelInfo(
            type = MODEL_TYPE,
            version = MODEL_VERSION,
            language = MODEL_LANGUAGE,
            accuracy = MODEL_ACCURACY,
            speed = MODEL_SPEED
        )

        val positiveKeywords = listOf(
            "amazing", "fantastic", "great", "excellent", "wonderful", "brilliant",
            "outstanding", "superb", "magnificent", "perfect", "incredible", "awesome",
            "beautiful", "lovely", "good", "nice", "best", "favorite", "love", "enjoy",
            "phenomenal", "spectacular", "remarkable", "exceptional", "marvelous",
            "stunning", "impressive", "captivating", "engaging", "compelling"
        )

        val negativeKeywords = listOf(
            "terrible", "awful", "horrible", "bad", "worst", "hate", "disgusting",
            "boring", "stupid", "dumb", "annoying", "frustrating", "disappointing",
            "waste", "rubbish", "garbage", "trash", "sucks", "pathetic", "lame",
            "atrocious", "dreadful", "appalling", "mediocre", "unwatchable",
            "cringe", "cheesy", "predictable", "cliché", "overrated"
        )

        val neutralIndicators = listOf(
            "okay", "decent", "average", "fine", "acceptable", "reasonable",
            "standard", "typical", "normal", "ordinary", "mediocre", "so-so"
        )

        val intensityModifiers = mapOf(
            "absolutely" to INTENSITY_ABSOLUTELY,
            "completely" to INTENSITY_COMPLETELY,
            "totally" to INTENSITY_TOTALLY,
            "extremely" to INTENSITY_EXTREMELY,
            "incredibly" to INTENSITY_INCREDIBLY,
            "very" to INTENSITY_VERY,
            "really" to INTENSITY_REALLY,
            "pretty" to INTENSITY_PRETTY,
            "somewhat" to INTENSITY_SOMEWHAT,
            "slightly" to INTENSITY_SLIGHTLY,
            "not" to INTENSITY_NOT,
            "never" to INTENSITY_NEVER,
            "barely" to INTENSITY_BARELY
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
                "mangled", "butchered", "torture", "nightmare"
            )
        )

        val algorithm = AlgorithmConfig(
            baseConfidence = BASE_CONFIDENCE,
            keywordWeight = KEYWORD_WEIGHT,
            contextWeight = CONTEXT_WEIGHT,
            modifierWeight = MODIFIER_WEIGHT,
            neutralThreshold = NEUTRAL_THRESHOLD,
            minConfidence = MIN_CONFIDENCE,
            maxConfidence = MAX_CONFIDENCE
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
                    algorithm.maxConfidence,
                    algorithm.baseConfidence + (positiveScore - maxOf(
                        negativeScore,
                        neutralScore
                    )) * 0.15
                )
                SentimentResult.positive(confidence, foundWords)
            }

            negativeScore > positiveScore && negativeScore > neutralScore -> {
                val confidence = minOf(
                    algorithm.maxConfidence,
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
                    algorithm.maxConfidence,
                    algorithm.baseConfidence + (positiveScore - negativeScore) * 0.1
                )
                SentimentResult.positive(confidence, foundWords)
            }

            negativeScore > positiveScore -> {
                val confidence = minOf(
                    algorithm.maxConfidence,
                    algorithm.baseConfidence + (negativeScore - positiveScore) * 0.1
                )
                SentimentResult.negative(confidence, foundWords)
            }

            else -> SentimentResult.neutral()
        }
    }
}

/**
 * Sentiment analysis result
 */
data class SentimentResult(
    val sentiment: SentimentType,
    val confidence: Double,
    val isSuccess: Boolean = true,
    val errorMessage: String? = null,
    val foundKeywords: List<String> = emptyList(),
    val processingTimeMs: Long = 0L
) {
    companion object {
        fun positive(confidence: Double, keywords: List<String> = emptyList()) =
            SentimentResult(SentimentType.POSITIVE, confidence, foundKeywords = keywords)

        fun negative(confidence: Double, keywords: List<String> = emptyList()) =
            SentimentResult(SentimentType.NEGATIVE, confidence, foundKeywords = keywords)

        fun neutral(confidence: Double = 0.5, keywords: List<String> = emptyList()) =
            SentimentResult(SentimentType.NEUTRAL, confidence, foundKeywords = keywords)

        fun error(message: String) =
            SentimentResult(SentimentType.NEUTRAL, 0.0, false, message)
    }

    /**
     * Returns human-readable description
     */
    fun getDescription(): String = when {
        !isSuccess -> "Error: $errorMessage"
        sentiment == SentimentType.POSITIVE -> "Positive (${(confidence * 100).toInt()}%)"
        sentiment == SentimentType.NEGATIVE -> "Negative (${(confidence * 100).toInt()}%)"
        else -> "Neutral"
    }
}

/**
 * Sentiment types
 */
enum class SentimentType {
    POSITIVE, NEGATIVE, NEUTRAL
}

/**
 * Data model for keyword-based analysis
 */
data class KeywordSentimentModel(
    val modelInfo: ModelInfo,
    val positiveKeywords: List<String>,
    val negativeKeywords: List<String>,
    val neutralIndicators: List<String>? = null,
    val intensityModifiers: Map<String, Double>? = null,
    val contextBoosters: ContextBoosters? = null,
    val algorithm: AlgorithmConfig
)

data class ModelInfo(
    val type: String,
    val version: String,
    val language: String,
    val accuracy: String,
    val speed: String
)

data class AlgorithmConfig(
    val baseConfidence: Double,
    val keywordWeight: Double? = 1.0,
    val contextWeight: Double? = 0.3,
    val modifierWeight: Double? = 0.4,
    val neutralThreshold: Double,
    val minConfidence: Double,
    val maxConfidence: Double
)

data class ContextBoosters(
    val movieTerms: List<String>? = null,
    val positiveContext: List<String>? = null,
    val negativeContext: List<String>? = null
)

/**
 * Data classes for enhanced model JSON structure
 */
data class EnhancedModelData(
    val model_info: EnhancedModelInfo,
    val positive_keywords: List<String>,
    val negative_keywords: List<String>,
    val neutral_indicators: List<String>,
    val intensity_modifiers: Map<String, Double>,
    val context_patterns: ContextPatterns? = null,
    val algorithm: EnhancedAlgorithmConfig
)

data class EnhancedModelInfo(
    val type: String,
    val version: String,
    val language: String,
    val accuracy: String,
    val speed: String,
    val improvements: String? = null
)

data class ContextPatterns(
    val strong_positive: List<String>? = null,
    val strong_negative: List<String>? = null
)

data class EnhancedAlgorithmConfig(
    val base_confidence: Double,
    val keyword_threshold: Int,
    val neutral_threshold: Double,
    val min_confidence: Double,
    val max_confidence: Double,
    val context_bonus: Double
)
