package org.studioapp.cinemy.ml.model

/**
 * Algorithm configuration for sentiment analysis
 * @param baseConfidence Base confidence level
 * @param keywordWeight Weight for keyword matching
 * @param contextWeight Weight for context analysis
 * @param modifierWeight Weight for intensity modifiers
 * @param neutralThreshold Threshold for neutral sentiment
 * @param minConfidence Minimum confidence threshold
 * @param maxConfidence Maximum confidence threshold
 */
data class AlgorithmConfig(
    val baseConfidence: Double,
    val keywordWeight: Double? = 1.0,
    val contextWeight: Double? = 0.3,
    val modifierWeight: Double? = 0.4,
    val neutralThreshold: Double? = 0.5,
    val minConfidence: Double? = 0.3,
    val maxConfidence: Double? = 0.9
)
