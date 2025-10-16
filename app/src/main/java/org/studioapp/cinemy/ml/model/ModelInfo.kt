package org.studioapp.cinemy.ml.model

/**
 * Information about a sentiment analysis model.
 *
 * This data class provides comprehensive metadata about ML models used in the sentiment analysis system.
 * It serves as a standardized way to store and access model information across different ML components,
 * enabling model comparison, performance evaluation, and runtime decision-making.
 *
 * The ModelInfo class is used throughout the ML layer to:
 * - Identify model types and versions for compatibility checking
 * - Display model capabilities and performance metrics
 * - Support model selection and fallback logic
 * - Provide debugging and monitoring information
 *
 * @param type Model type identifier (e.g., "TensorFlowLite", "ProductionMultilingual", "SimpleKeyword")
 * @param version Model version string (e.g., "3.0.0", "1.0.0")
 * @param language Supported languages as comma-separated string (e.g., "English,Spanish,Russian", "English")
 * @param accuracy Model accuracy percentage as string (e.g., "95%", "100%", "85%")
 * @param speed Model processing speed description (e.g., "<1ms", "fast", "very fast")
 */
data class ModelInfo(
    val type: String,
    val version: String,
    val language: String,
    val accuracy: String,
    val speed: String
)
