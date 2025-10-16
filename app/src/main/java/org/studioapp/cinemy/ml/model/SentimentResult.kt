package org.studioapp.cinemy.ml.model

/**
 * Result of sentiment analysis
 * @param sentiment The detected sentiment type
 * @param confidence Confidence score (0.0 to 1.0)
 * @param isSuccess Whether the analysis was successful
 * @param errorMessage Error message if analysis failed
 * @param foundKeywords Keywords found during analysis
 * @param processingTimeMs Processing time in milliseconds
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
        /**
         * Creates a positive sentiment result with specified confidence and keywords.
         *
         * This factory method creates a SentimentResult indicating positive sentiment
         * with the provided confidence score and any keywords that contributed to the
         * positive sentiment classification.
         *
         * @param confidence Confidence score for positive sentiment (0.0 to 1.0)
         * @param keywords List of keywords that contributed to positive sentiment classification
         * @return SentimentResult with POSITIVE sentiment, specified confidence, and keywords
         */
        fun positive(confidence: Double, keywords: List<String> = emptyList()) =
            SentimentResult(SentimentType.POSITIVE, confidence, true, foundKeywords = keywords)

        /**
         * Creates a negative sentiment result with specified confidence and keywords.
         *
         * This factory method creates a SentimentResult indicating negative sentiment
         * with the provided confidence score and any keywords that contributed to the
         * negative sentiment classification.
         *
         * @param confidence Confidence score for negative sentiment (0.0 to 1.0)
         * @param keywords List of keywords that contributed to negative sentiment classification
         * @return SentimentResult with NEGATIVE sentiment, specified confidence, and keywords
         */
        fun negative(confidence: Double, keywords: List<String> = emptyList()) =
            SentimentResult(SentimentType.NEGATIVE, confidence, true, foundKeywords = keywords)

        /**
         * Creates a neutral sentiment result with specified confidence and keywords.
         *
         * This factory method creates a SentimentResult indicating neutral sentiment
         * with the provided confidence score and any keywords that contributed to the
         * neutral sentiment classification. Uses default confidence of 0.5 if not specified.
         *
         * @param confidence Confidence score for neutral sentiment (0.0 to 1.0), defaults to 0.5
         * @param keywords List of keywords that contributed to neutral sentiment classification
         * @return SentimentResult with NEUTRAL sentiment, specified confidence, and keywords
         */
        fun neutral(confidence: Double = 0.5, keywords: List<String> = emptyList()) =
            SentimentResult(SentimentType.NEUTRAL, confidence, true, foundKeywords = keywords)

        /**
         * Creates an error sentiment result indicating analysis failure.
         *
         * This factory method creates a SentimentResult indicating that sentiment analysis
         * failed with the provided error message. The result will have NEUTRAL sentiment
         * with 0.0 confidence and isSuccess set to false.
         *
         * @param message Error message describing why sentiment analysis failed
         * @return SentimentResult with NEUTRAL sentiment, 0.0 confidence, isSuccess=false, and error message
         */
        fun error(message: String) =
            SentimentResult(SentimentType.NEUTRAL, 0.0, false, message)
    }
}

/**
 * Sentiment types
 */
enum class SentimentType {
    POSITIVE, NEGATIVE, NEUTRAL
}
