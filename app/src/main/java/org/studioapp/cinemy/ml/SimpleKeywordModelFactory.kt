package org.studioapp.cinemy.ml

import org.studioapp.cinemy.ml.model.KeywordSentimentModel
import org.studioapp.cinemy.ml.model.ModelInfo
import org.studioapp.cinemy.ml.model.AlgorithmConfig
import org.studioapp.cinemy.ml.model.ContextBoosters

/**
 * Factory for creating simple keyword-based sentiment models
 * Used as last resort fallback when production models fail to load
 */
class SimpleKeywordModelFactory {

    companion object {
        // Model constants
        private const val MODEL_TYPE = "keyword_sentiment_analysis"
        private const val MODEL_VERSION = "2.0.0"
        private const val MODEL_LANGUAGE = "en"
        private const val MODEL_ACCURACY = "85%+"
        private const val MODEL_SPEED = "very_fast"

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
     * Creates a simple keyword-based sentiment model
     * Used as last resort fallback when production models fail to load
     * @return KeywordSentimentModel with basic English keywords
     */
    fun createSimpleModel(): KeywordSentimentModel {
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
                "mangled", "torture", "nightmare"
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
}
