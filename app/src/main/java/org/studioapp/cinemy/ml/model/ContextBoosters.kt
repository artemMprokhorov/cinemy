package org.studioapp.cinemy.ml.model

/**
 * Data class representing context boosters for enhanced sentiment analysis.
 *
 * ContextBoosters provides movie-specific terminology and quality indicators that enhance
 * sentiment analysis by providing contextual information about movie-related content.
 * This data class is used across different sentiment analysis models in the ML layer
 * to improve accuracy and context awareness.
 *
 * The class supports three types of context boosters:
 * - Movie terms: General movie-related terminology (cinematography, acting, plot, etc.)
 * - Positive context: Quality indicators that boost positive sentiment (masterpiece, brilliant, etc.)
 * - Negative context: Failure indicators that boost negative sentiment (flop, disaster, etc.)
 *
 * All parameters are optional and can be null, allowing for flexible configuration
 * based on the specific sentiment analysis requirements and model capabilities.
 *
 * @param movieTerms Optional list of movie-related terms for general context enhancement.
 *                   These terms help identify movie-specific content in text analysis.
 *                   Typically contains 12 movie-specific terms like cinematography, acting, plot, etc.
 * @param positiveContext Optional list of positive quality indicators for sentiment boosting.
 *                       These terms enhance positive sentiment when found in analyzed text.
 *                       Typically contains 9 quality indicators like masterpiece, brilliant, genius, etc.
 * @param negativeContext Optional list of negative failure indicators for sentiment boosting.
 *                       These terms enhance negative sentiment when found in analyzed text.
 *                       Typically contains 9 failure indicators like flop, disaster, nightmare, etc.
 */
data class ContextBoosters(
    val movieTerms: List<String>? = null,
    val positiveContext: List<String>? = null,
    val negativeContext: List<String>? = null
)
