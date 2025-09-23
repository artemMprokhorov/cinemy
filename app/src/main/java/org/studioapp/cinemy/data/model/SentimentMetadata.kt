package org.studioapp.cinemy.data.model

/**
 * Data model for sentiment metadata from N8N backend
 * Represents metadata about sentiment analysis results
 */
data class SentimentMetadata(
    val totalReviews: Int = 0,
    val positiveCount: Int = 0,
    val negativeCount: Int = 0,
    val source: String = "",
    val timestamp: String = "",
    val apiSuccess: Map<String, Boolean> = emptyMap()
) {
    val hasValidData: Boolean
        get() = totalReviews > 0 && (positiveCount > 0 || negativeCount > 0)

    val positivePercentage: Double
        get() = if (totalReviews > 0) (positiveCount.toDouble() / totalReviews) * 100 else 0.0

    val negativePercentage: Double
        get() = if (totalReviews > 0) (negativeCount.toDouble() / totalReviews) * 100 else 0.0
}
