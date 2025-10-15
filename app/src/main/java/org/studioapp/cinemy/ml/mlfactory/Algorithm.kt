package org.studioapp.cinemy.ml.mlfactory

import org.studioapp.cinemy.ml.model.AlgorithmConfig
import org.studioapp.cinemy.ml.model.EnhancedModelData

/**
 * Factory for creating algorithm configurations for sentiment analysis
 * Provides centralized algorithm configuration management for ML models
 */
object Algorithm {

    /**
     * Production model algorithm configuration with higher confidence settings
     * Optimized for production sentiment analysis with enhanced accuracy
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
     * Creates algorithm configuration from enhanced model data
     * Used when loading enhanced model format with custom algorithm settings
     *
     * @param modelData Enhanced model data containing algorithm configuration
     * @return AlgorithmConfig configured from the enhanced model data
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
