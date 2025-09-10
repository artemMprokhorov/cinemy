package com.example.tmdbai.data.model

/**
 * Data model for sentiment reviews from N8N backend
 * Represents the structure of sentiment analysis results
 */
data class SentimentReviews(
    val positive: List<String> = emptyList(),
    val negative: List<String> = emptyList()
) {
    val hasPositiveReviews: Boolean
        get() = positive.isNotEmpty()
    
    val hasNegativeReviews: Boolean
        get() = negative.isNotEmpty()
    
    val hasAnyReviews: Boolean
        get() = hasPositiveReviews || hasNegativeReviews
    
    val totalReviewsCount: Int
        get() = positive.size + negative.size
}
