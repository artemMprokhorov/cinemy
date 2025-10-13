package org.studioapp.cinemy.ml.model

/**
 * Keyword-based sentiment analysis model
 * @param modelInfo Model information
 * @param positiveKeywords List of positive sentiment keywords
 * @param negativeKeywords List of negative sentiment keywords
 * @param neutralIndicators List of neutral sentiment indicators
 * @param intensityModifiers Map of intensity modifiers and their weights
 * @param contextBoosters Context boosters for movie-related terms
 * @param algorithm Algorithm configuration
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
