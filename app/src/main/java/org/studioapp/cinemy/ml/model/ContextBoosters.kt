package org.studioapp.cinemy.ml.model

/**
 * Context boosters for enhanced sentiment analysis
 * @param movieTerms Movie-related terms for context
 * @param positiveContext Positive context indicators
 * @param negativeContext Negative context indicators
 */
data class ContextBoosters(
    val movieTerms: List<String>? = null,
    val positiveContext: List<String>? = null,
    val negativeContext: List<String>? = null
)
