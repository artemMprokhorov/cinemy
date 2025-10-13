package org.studioapp.cinemy.ml.model

import kotlinx.serialization.Serializable

/**
 * Production model data structure
 */
@Serializable
data class ProductionModelData(
    val model_info: ProductionModelInfo,
    val model_data: ProductionModelWeights
)

/**
 * Production model information
 */
@Serializable
data class ProductionModelInfo(
    val version: String,
    val type: String,
    val languages: List<String>,
    val training_samples: Int,
    val accuracy: Double,
    val features: Int,
    val vocabulary_size: Int,
    val created: String,
    val performance: ProductionPerformance
)

/**
 * Production model performance metrics
 */
@Serializable
data class ProductionPerformance(
    val training_time_seconds: Double,
    val prediction_time_ms: String,
    val supported_complexity: List<String>
)

/**
 * Production model weights
 */
@Serializable
data class ProductionModelWeights(
    val weights: List<Double>
)
