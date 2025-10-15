package org.studioapp.cinemy.data.model

/**
 * Data model for sentiment reviews from N8N backend.
 * 
 * Represents the structure of sentiment analysis results containing categorized
 * positive and negative review texts. This model is used to display sentiment
 * analysis data in the movie details screen and provides convenient computed
 * properties for checking the presence of different review types.
 * 
 * @param positive List of positive review texts, defaults to empty list
 * @param negative List of negative review texts, defaults to empty list
 */
data class SentimentReviews(
    val positive: List<String> = emptyList(),
    val negative: List<String> = emptyList()
) {
    /**
     * Indicates whether there are any positive reviews available.
     * 
     * @return true if the positive list is not empty, false otherwise
     */
    val hasPositiveReviews: Boolean
        get() = positive.isNotEmpty()

    /**
     * Indicates whether there are any negative reviews available.
     * 
     * @return true if the negative list is not empty, false otherwise
     */
    val hasNegativeReviews: Boolean
        get() = negative.isNotEmpty()

    /**
     * Indicates whether there are any reviews available (positive or negative).
     * 
     * @return true if either positive or negative lists contain items, false otherwise
     */
    val hasAnyReviews: Boolean
        get() = hasPositiveReviews || hasNegativeReviews
}
