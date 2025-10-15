package org.studioapp.cinemy.ml.model

import kotlinx.serialization.Serializable

/**
 * Main configuration container for TensorFlow Lite sentiment analysis models.
 * 
 * This data class serves as the root configuration container for TensorFlow Lite
 * sentiment analysis models, providing comprehensive configuration options for
 * model loading, performance optimization, and hybrid system integration.
 * 
 * The configuration supports both standalone TensorFlow Lite model execution
 * and hybrid system integration with keyword-based fallback models for optimal
 * sentiment analysis performance and reliability.
 * 
 * @param tensorflowLite TensorFlow Lite model configuration including model file,
 *                       input/output settings, and performance optimization
 * @param hybridSystem Hybrid system configuration for model selection and
 *                     integration with fallback mechanisms
 */
@Serializable
data class TensorFlowConfig(
    val tensorflowLite: TensorFlowLiteConfig? = null,
    val hybridSystem: HybridSystemConfig? = null
)

/**
 * TensorFlow Lite model configuration for sentiment analysis.
 * 
 * This data class provides comprehensive configuration options for TensorFlow Lite
 * sentiment analysis models, including model file specification, input/output
 * configuration, performance optimization settings, and fallback mechanisms.
 * 
 * The configuration supports BERT-based sentiment analysis models with hardware
 * acceleration, multi-threading, and intelligent fallback to keyword-based models
 * when TensorFlow Lite confidence is below specified thresholds.
 * 
 * @param modelFile Path to the TensorFlow Lite model file (e.g., "production_sentiment_full_manual.tflite")
 * @param modelType Type of the model (e.g., "BERT", "sentiment_analysis")
 * @param version Model version string for compatibility checking
 * @param inputConfig Input tensor configuration including shape, type, and preprocessing
 * @param outputConfig Output tensor configuration including class labels and confidence thresholds
 * @param performance Performance optimization settings including GPU, NNAPI, and threading
 * @param fallback Fallback configuration for keyword model integration
 */
@Serializable
data class TensorFlowLiteConfig(
    val modelFile: String? = null,
    val modelType: String? = null,
    val version: String? = null,
    val inputConfig: InputConfig? = null,
    val outputConfig: OutputConfig? = null,
    val performance: PerformanceConfig? = null,
    val fallback: FallbackConfig? = null
)

/**
 * Input tensor configuration for TensorFlow Lite models.
 * 
 * This data class defines the input tensor configuration for TensorFlow Lite
 * sentiment analysis models, including tensor name, shape, data type, and
 * preprocessing options for text input preparation.
 * 
 * The configuration supports BERT-based input preprocessing with tokenization,
 * padding, truncation, and vocabulary mapping for optimal model performance.
 * 
 * @param inputTensorName Name of the input tensor in the TensorFlow Lite model
 * @param inputShape Shape of the input tensor (e.g., [1, 512] for batch_size=1, max_length=512)
 * @param inputType Data type of the input tensor (e.g., "int32", "float32")
 * @param preprocessing Text preprocessing configuration for input preparation
 */
@Serializable
data class InputConfig(
    val inputTensorName: String? = null,
    val inputShape: List<Int> = emptyList(),
    val inputType: String? = null,
    val preprocessing: PreprocessingConfig? = null
)

/**
 * Text preprocessing configuration for TensorFlow Lite input preparation.
 * 
 * This data class defines text preprocessing options for preparing input text
 * for TensorFlow Lite sentiment analysis models, including length management,
 * padding strategies, and text normalization options.
 * 
 * The configuration supports BERT-compatible preprocessing with tokenization,
 * sequence length management, and text normalization for optimal model accuracy.
 * 
 * @param maxLength Maximum sequence length for input text (e.g., 512 for BERT)
 * @param padding Padding strategy for sequences shorter than maxLength ("post", "pre")
 * @param truncation Whether to truncate sequences longer than maxLength
 * @param lowercase Whether to convert text to lowercase before processing
 * @param removePunctuation Whether to remove punctuation marks from input text
 */
@Serializable
data class PreprocessingConfig(
    val maxLength: Int? = null,
    val padding: String? = null,
    val truncation: Boolean? = null,
    val lowercase: Boolean? = null,
    val removePunctuation: Boolean? = null
)

/**
 * Output tensor configuration for TensorFlow Lite models.
 * 
 * This data class defines the output tensor configuration for TensorFlow Lite
 * sentiment analysis models, including tensor name, shape, data type, class labels,
 * and confidence thresholds for sentiment classification.
 * 
 * The configuration supports sentiment classification output with class labels
 * (negative, neutral, positive) and confidence thresholding for reliable results.
 * 
 * @param outputTensorName Name of the output tensor in the TensorFlow Lite model
 * @param outputShape Shape of the output tensor (e.g., [1, 3] for batch_size=1, num_classes=3)
 * @param outputType Data type of the output tensor (e.g., "float32")
 * @param classLabels List of class labels for sentiment classification (e.g., ["negative", "neutral", "positive"])
 * @param confidenceThreshold Minimum confidence threshold for reliable sentiment classification
 */
@Serializable
data class OutputConfig(
    val outputTensorName: String? = null,
    val outputShape: List<Int> = emptyList(),
    val outputType: String? = null,
    val classLabels: List<String>? = null,
    val confidenceThreshold: Double? = null
)

/**
 * Performance optimization configuration for TensorFlow Lite models.
 * 
 * This data class defines performance optimization settings for TensorFlow Lite
 * sentiment analysis models, including hardware acceleration options and
 * multi-threading configuration for optimal inference performance.
 * 
 * The configuration supports GPU acceleration, NNAPI delegation, XNNPACK
 * optimization, and multi-threading for maximum performance on mobile devices.
 * 
 * @param useGpu Whether to enable GPU acceleration for TensorFlow Lite inference
 * @param useNnapi Whether to enable NNAPI delegation for hardware acceleration
 * @param useXnnpack Whether to enable XNNPACK optimization for CPU inference
 * @param numThreads Number of threads to use for TensorFlow Lite inference
 */
@Serializable
data class PerformanceConfig(
    val useGpu: Boolean? = null,
    val useNnapi: Boolean? = null,
    val useXnnpack: Boolean? = null,
    val numThreads: Int? = null
)

/**
 * Fallback configuration for TensorFlow Lite model integration.
 * 
 * This data class defines fallback settings for TensorFlow Lite sentiment analysis
 * models, including keyword model integration and confidence thresholds for
 * automatic fallback to keyword-based sentiment analysis.
 * 
 * The configuration supports intelligent fallback mechanisms when TensorFlow Lite
 * confidence is below specified thresholds, ensuring reliable sentiment analysis
 * even when the primary model provides uncertain results.
 * 
 * @param useKeywordModel Whether to enable keyword model fallback for low confidence results
 * @param fallbackThreshold Confidence threshold below which to trigger keyword model fallback
 */
@Serializable
data class FallbackConfig(
    val useKeywordModel: Boolean? = null,
    val fallbackThreshold: Double? = null
)

/**
 * Hybrid system configuration for TensorFlow Lite and keyword model integration.
 * 
 * This data class defines configuration options for hybrid sentiment analysis systems
 * that combine TensorFlow Lite models with keyword-based fallback models for
 * optimal performance and reliability.
 * 
 * The configuration supports intelligent model selection, seamless fallback
 * mechanisms, and performance monitoring for hybrid sentiment analysis systems.
 * 
 * @param modelSelection Model selection configuration for choosing between TensorFlow and keyword models
 * @param integration Integration configuration for seamless model switching and performance monitoring
 */
@Serializable
data class HybridSystemConfig(
    val modelSelection: ModelSelectionConfig? = null,
    val integration: IntegrationConfig? = null
)

/**
 * Model selection configuration for hybrid sentiment analysis systems.
 * 
 * This data class defines model selection criteria for hybrid sentiment analysis
 * systems, including conditions for choosing between TensorFlow Lite and keyword
 * models based on text complexity and confidence thresholds.
 * 
 * The configuration supports intelligent model selection based on text complexity,
 * confidence levels, and specific use cases to optimize sentiment analysis accuracy
 * and performance across different scenarios.
 * 
 * @param useTensorflowFor List of use cases where TensorFlow Lite should be preferred
 * @param useKeywordFor List of use cases where keyword models should be preferred
 * @param complexityThreshold Text complexity threshold for model selection decisions
 * @param confidenceThreshold Confidence threshold for model selection and fallback decisions
 */
@Serializable
data class ModelSelectionConfig(
    val useTensorflowFor: List<String>? = null,
    val useKeywordFor: List<String>? = null,
    val complexityThreshold: Int? = null,
    val confidenceThreshold: Double? = null
)

/**
 * Integration configuration for hybrid sentiment analysis systems.
 * 
 * This data class defines integration settings for hybrid sentiment analysis
 * systems, including seamless fallback mechanisms, performance monitoring,
 * and result caching for optimal system performance and reliability.
 * 
 * The configuration supports seamless model switching, performance tracking,
 * and intelligent caching to ensure smooth user experience and optimal
 * resource utilization in hybrid sentiment analysis systems.
 * 
 * @param seamlessFallback Whether to enable seamless fallback between models without user interruption
 * @param performanceMonitoring Whether to enable performance monitoring and metrics collection
 * @param cacheResults Whether to enable result caching for improved performance
 * @param cacheDurationMinutes Duration in minutes for caching sentiment analysis results
 */
@Serializable
data class IntegrationConfig(
    val seamlessFallback: Boolean? = null,
    val performanceMonitoring: Boolean? = null,
    val cacheResults: Boolean? = null,
    val cacheDurationMinutes: Int? = null
)
