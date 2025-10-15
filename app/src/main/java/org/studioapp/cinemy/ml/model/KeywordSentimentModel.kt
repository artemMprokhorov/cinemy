package org.studioapp.cinemy.ml.model

/**
 * Keyword-based sentiment analysis model for text sentiment classification.
 * 
 * This data class represents a complete configuration for keyword-based sentiment analysis,
 * providing all necessary components for analyzing text sentiment using keyword matching,
 * intensity modifiers, context boosters, and algorithm parameters. It serves as the
 * primary data structure for keyword-based sentiment analysis models throughout the ML layer.
 * 
 * The model supports comprehensive sentiment analysis through:
 * - Multilingual keyword matching for positive, negative, and neutral sentiment detection
 * - Intensity modifiers for sentiment strength adjustment and confidence scoring
 * - Context boosters for domain-specific sentiment enhancement (movie reviews, etc.)
 * - Configurable algorithm parameters for optimal analysis performance
 * 
 * This model is used as a fallback mechanism when advanced ML models (TensorFlow Lite,
 * LiteRT) are unavailable or provide low confidence results, ensuring sentiment analysis
 * functionality remains available across all scenarios.
 * 
 * @param modelInfo Model information including type, version, language, accuracy, and speed
 * @param positiveKeywords List of positive sentiment keywords for positive sentiment detection
 * @param negativeKeywords List of negative sentiment keywords for negative sentiment detection
 * @param neutralIndicators List of neutral sentiment indicators for neutral sentiment classification
 * @param intensityModifiers Map of intensity modifiers and their corresponding weight values for sentiment adjustment
 * @param contextBoosters Optional context boosters for movie-related terms and domain-specific sentiment enhancement
 * @param algorithm Algorithm configuration with confidence thresholds, weight parameters, and classification settings
 */
data class KeywordSentimentModel(
    val modelInfo: ModelInfo,
    val positiveKeywords: List<String>,
    val negativeKeywords: List<String>,
    val neutralIndicators: List<String>,
    val intensityModifiers: Map<String, Double>,
    val contextBoosters: ContextBoosters? = null,
    val algorithm: AlgorithmConfig
)
