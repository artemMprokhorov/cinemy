package org.studioapp.cinemy.ml.model

import kotlinx.serialization.Serializable

/**
 * TensorFlow Lite configuration data classes
 */
@Serializable
data class TensorFlowConfig(
    val tensorflowLite: TensorFlowLiteConfig? = null,
    val hybridSystem: HybridSystemConfig? = null
)

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

@Serializable
data class InputConfig(
    val inputTensorName: String? = null,
    val inputShape: List<Int> = emptyList(),
    val inputType: String? = null,
    val preprocessing: PreprocessingConfig? = null
)

@Serializable
data class PreprocessingConfig(
    val maxLength: Int? = null,
    val padding: String? = null,
    val truncation: Boolean? = null,
    val lowercase: Boolean? = null,
    val removePunctuation: Boolean? = null
)

@Serializable
data class OutputConfig(
    val outputTensorName: String? = null,
    val outputShape: List<Int> = emptyList(),
    val outputType: String? = null,
    val classLabels: List<String>? = null,
    val confidenceThreshold: Double? = null
)

@Serializable
data class PerformanceConfig(
    val useGpu: Boolean? = null,
    val useNnapi: Boolean? = null,
    val useXnnpack: Boolean? = null,
    val numThreads: Int? = null
)

@Serializable
data class FallbackConfig(
    val useKeywordModel: Boolean? = null,
    val fallbackThreshold: Double? = null
)

@Serializable
data class HybridSystemConfig(
    val modelSelection: ModelSelectionConfig? = null,
    val integration: IntegrationConfig? = null
)

@Serializable
data class ModelSelectionConfig(
    val useTensorflowFor: List<String>? = null,
    val useKeywordFor: List<String>? = null,
    val complexityThreshold: Int? = null,
    val confidenceThreshold: Double? = null
)

@Serializable
data class IntegrationConfig(
    val seamlessFallback: Boolean? = null,
    val performanceMonitoring: Boolean? = null,
    val cacheResults: Boolean? = null,
    val cacheDurationMinutes: Int? = null
)
