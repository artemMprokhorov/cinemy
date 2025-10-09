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
)
