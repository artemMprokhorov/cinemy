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
        fun positive(confidence: Double, keywords: List<String> = emptyList()) =
            SentimentResult(SentimentType.POSITIVE, confidence, true, foundKeywords = keywords)

        fun negative(confidence: Double, keywords: List<String> = emptyList()) =
            SentimentResult(SentimentType.NEGATIVE, confidence, true, foundKeywords = keywords)

        fun neutral(confidence: Double = 0.5, keywords: List<String> = emptyList()) =
            SentimentResult(SentimentType.NEUTRAL, confidence, true, foundKeywords = keywords)

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
