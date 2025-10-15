package org.studioapp.cinemy.ml.mlfactory

import org.studioapp.cinemy.ml.model.AlgorithmConfig
import org.studioapp.cinemy.ml.model.EnhancedModelData

/**
 * Factory for creating algorithm configurations for sentiment analysis.
 * 
 * This factory provides centralized algorithm configuration management for ML models,
 * offering both production-ready configurations and dynamic configuration creation
 * from enhanced model data. It serves as the primary source for algorithm parameters
 * used across different sentiment analysis models in the ML layer.
 * 
 * The factory supports two main use cases:
 * - Production configuration with optimized settings for high accuracy
 * - Dynamic configuration creation from enhanced model JSON data
 * 
 * @see AlgorithmConfig for the configuration data structure
 * @see EnhancedModelData for enhanced model data format
 */
object Algorithm {

    /**
     * Production model algorithm configuration with higher confidence settings.
     * 
     * This configuration is optimized for production sentiment analysis with enhanced
     * accuracy and reliability. It uses higher confidence thresholds and balanced
     * weight distribution to ensure consistent and accurate sentiment analysis results.
     * 
     * Configuration details:
     * - Base confidence: 0.8 (higher than default for production reliability)
     * - Keyword weight: 1.0 (full weight for keyword matching)
     * - Context weight: 0.4 (moderate context influence)
     * - Modifier weight: 0.5 (balanced intensity modifier influence)
     * - Neutral threshold: 0.5 (standard neutral sentiment threshold)
     * - Min confidence: 0.4 (minimum acceptable confidence)
     * - Max confidence: 0.95 (maximum confidence cap)
     * 
     * @return AlgorithmConfig with production-optimized settings
     */
    val PRODUCTION_CONFIG = AlgorithmConfig(
        baseConfidence = 0.8, // Higher confidence for production model
        keywordWeight = 1.0,
        contextWeight = 0.4,
        modifierWeight = 0.5,
        neutralThreshold = 0.5,
        minConfidence = 0.4,
        maxConfidence = 0.95
    )

    /**
     * Creates algorithm configuration from enhanced model data.
     * 
     * This method extracts algorithm configuration parameters from enhanced model data
     * and creates a new AlgorithmConfig instance. It's used when loading enhanced
     * model format with custom algorithm settings, allowing for dynamic configuration
     * based on model-specific parameters.
     * 
     * The method maps enhanced model algorithm parameters to the standard AlgorithmConfig
     * format, with some parameters using fixed values for consistency:
     * - keywordWeight: Fixed at 1.0 for full keyword influence
     * - contextWeight: Fixed at 0.3 for moderate context influence
     * - modifierWeight: Fixed at 0.4 for balanced modifier influence
     * 
     * All other parameters are extracted directly from the enhanced model data.
     * 
     * @param modelData Enhanced model data containing algorithm configuration
     * @return AlgorithmConfig configured from the enhanced model data
     * @throws IllegalArgumentException if modelData is null or contains invalid parameters
     */
    fun createFromEnhancedModel(modelData: EnhancedModelData): AlgorithmConfig {
        return AlgorithmConfig(
            baseConfidence = modelData.algorithm.base_confidence,
            keywordWeight = 1.0,
            contextWeight = 0.3,
            modifierWeight = 0.4,
            neutralThreshold = modelData.algorithm.neutral_threshold,
            minConfidence = modelData.algorithm.min_confidence,
            maxConfidence = modelData.algorithm.max_confidence
        )
    }
}
