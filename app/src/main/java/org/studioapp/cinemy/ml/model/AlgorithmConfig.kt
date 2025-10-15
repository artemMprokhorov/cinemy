package org.studioapp.cinemy.ml.model

/**
 * Configuration class for sentiment analysis algorithms.
 * 
 * This data class provides comprehensive configuration parameters for sentiment analysis
 * algorithms used across different ML models in the Cinemy application. It serves as
 * the central configuration for algorithm behavior, including confidence thresholds,
 * weight distributions, and sentiment classification parameters.
 * 
 * The configuration supports both production-optimized settings and dynamic configuration
 * from enhanced model data, allowing for flexible algorithm tuning based on model
 * requirements and performance characteristics.
 * 
 * @param baseConfidence Base confidence level for sentiment analysis (0.0-1.0)
 * @param keywordWeight Weight multiplier for keyword matching influence (default: 1.0)
 * @param contextWeight Weight multiplier for context analysis influence (default: 0.3)
 * @param modifierWeight Weight multiplier for intensity modifiers influence (default: 0.4)
 * @param neutralThreshold Threshold for neutral sentiment classification (default: 0.5)
 * @param minConfidence Minimum acceptable confidence threshold (default: 0.3)
 * @param maxConfidence Maximum confidence threshold cap (default: 0.9)
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
