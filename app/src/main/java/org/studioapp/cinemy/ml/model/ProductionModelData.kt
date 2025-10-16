package org.studioapp.cinemy.ml.model

import kotlinx.serialization.Serializable

/**
 * Production model data structure for advanced sentiment analysis models.
 *
 * This data class represents the complete configuration and data for production-grade
 * sentiment analysis models loaded from JSON files. It contains comprehensive model
 * information, performance metrics, and model weights for high-accuracy sentiment
 * analysis across multiple languages.
 *
 * The structure is designed for production models that provide enhanced accuracy
 * and multilingual support compared to basic keyword-based models. It serves as
 * the primary data structure for loading advanced sentiment analysis models from
 * JSON configuration files in the ML layer.
 *
 * @param model_info Comprehensive model information including version, type, languages, and performance metrics
 * @param model_data Model weights and data required for sentiment analysis inference
 */
@Serializable
data class ProductionModelData(
    val model_info: ProductionModelInfo,
    val model_data: ProductionModelWeights
)

/**
 * Production model information containing comprehensive metadata for sentiment analysis models.
 *
 * This data class provides detailed information about production-grade sentiment analysis
 * models, including version details, supported languages, training statistics, accuracy
 * metrics, and performance characteristics. It serves as the primary source for model
 * metadata used throughout the ML layer for model identification, compatibility checking,
 * and performance evaluation.
 *
 * The information is typically loaded from JSON configuration files and used to determine
 * model capabilities, language support, and performance expectations for sentiment analysis.
 *
 * @param version Model version string (e.g., "3.0.0") for compatibility checking
 * @param type Model type identifier (e.g., "ProductionMultilingualSentimentAnalyzer")
 * @param languages List of supported languages (e.g., ["English", "Spanish", "Russian"])
 * @param training_samples Number of training samples used to train the model
 * @param accuracy Model accuracy as a decimal value (0.0-1.0, where 1.0 = 100%)
 * @param features Number of features used in the model
 * @param vocabulary_size Size of the model's vocabulary
 * @param created Creation date/timestamp of the model
 * @param performance Performance metrics including training time and prediction speed
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
 * Production model performance metrics for sentiment analysis models.
 *
 * This data class contains performance-related information about production sentiment
 * analysis models, including training time, prediction speed, and supported complexity
 * levels. It provides essential metrics for performance evaluation, model comparison,
 * and runtime optimization decisions in the ML layer.
 *
 * The metrics are used to assess model efficiency, set performance expectations,
 * and make informed decisions about model selection based on device capabilities
 * and performance requirements.
 *
 * @param training_time_seconds Total training time in seconds for the model
 * @param prediction_time_ms Average prediction time in milliseconds (e.g., "<1ms", "2ms")
 * @param supported_complexity List of supported complexity levels (e.g., ["simple", "moderate", "complex"])
 */
@Serializable
data class ProductionPerformance(
    val training_time_seconds: Double,
    val prediction_time_ms: String,
    val supported_complexity: List<String>
)

/**
 * Production model weights containing the trained parameters for sentiment analysis.
 *
 * This data class stores the actual model weights and parameters required for
 * sentiment analysis inference. The weights represent the learned parameters
 * from the training process and are used during prediction to classify text
 * sentiment with high accuracy.
 *
 * The weights are typically loaded from JSON configuration files and used
 * by the sentiment analysis algorithms to perform accurate sentiment classification.
 * The number and values of weights depend on the specific model architecture
 * and training configuration.
 *
 * @param weights List of model weights as Double values used for sentiment analysis inference
 */
@Serializable
data class ProductionModelWeights(
    val weights: List<Double>
)
