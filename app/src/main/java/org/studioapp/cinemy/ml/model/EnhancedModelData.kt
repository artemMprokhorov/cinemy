package org.studioapp.cinemy.ml.model

import kotlinx.serialization.Serializable

/**
 * Enhanced model data structure
 */
@Serializable
data class EnhancedModelData(
    val model_info: EnhancedModelInfo,
    val positive_keywords: List<String>,
    val negative_keywords: List<String>,
    val neutral_indicators: List<String>,
    val intensity_modifiers: Map<String, Double>,
    val context_patterns: ContextPatterns? = null,
    val algorithm: EnhancedAlgorithmConfig
)

/**
 * Enhanced model information
 */
@Serializable
data class EnhancedModelInfo(
    val type: String,
    val version: String,
    val language: String,
    val accuracy: String,
    val speed: String
)

/**
 * Context patterns for enhanced analysis
 */
@Serializable
data class ContextPatterns(
    val strong_positive: List<String>? = null,
    val strong_negative: List<String>? = null
)

/**
 * Enhanced algorithm configuration
 */
@Serializable
data class EnhancedAlgorithmConfig(
    val base_confidence: Double,
    val keyword_threshold: Int,
    val context_weight: Double,
    val modifier_weight: Double,
    val neutral_threshold: Double,
    val min_confidence: Double,
    val max_confidence: Double
)
