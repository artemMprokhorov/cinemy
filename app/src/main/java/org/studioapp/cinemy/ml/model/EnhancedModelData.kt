package org.studioapp.cinemy.ml.model

import kotlinx.serialization.Serializable

/**
 * Enhanced model data structure for advanced sentiment analysis.
 * 
 * This data class represents the complete configuration for enhanced sentiment analysis
 * models, including multilingual keywords, intensity modifiers, context patterns, and
 * algorithm parameters. It serves as the primary data structure for loading and configuring
 * advanced sentiment analysis models from JSON configuration files.
 * 
 * The enhanced model format provides comprehensive sentiment analysis capabilities with:
 * - Multilingual keyword support for positive, negative, and neutral sentiment detection
 * - Intensity modifiers for sentiment strength adjustment
 * - Context patterns for domain-specific sentiment enhancement
 * - Advanced algorithm configuration for optimal analysis performance
 * 
 * This structure is used by the ML layer to configure enhanced sentiment analysis
 * models that provide higher accuracy and more sophisticated sentiment detection
 * compared to basic keyword-based models.
 * 
 * @param model_info Enhanced model information including type, version, language, accuracy, and speed
 * @param positive_keywords List of positive sentiment keywords for sentiment detection
 * @param negative_keywords List of negative sentiment keywords for sentiment detection
 * @param neutral_indicators List of neutral sentiment indicators for sentiment classification
 * @param intensity_modifiers Map of intensity modifiers with their corresponding weight values
 * @param context_patterns Optional context patterns for enhanced sentiment analysis
 * @param algorithm Enhanced algorithm configuration with advanced parameters
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
 * Enhanced model information containing metadata about the sentiment analysis model.
 * 
 * This data class provides comprehensive information about enhanced sentiment analysis
 * models, including technical specifications, performance metrics, and model characteristics.
 * It serves as the primary source of model metadata for enhanced sentiment analysis
 * configurations loaded from JSON files.
 * 
 * The model information includes:
 * - Model type and version for compatibility checking
 * - Language support for multilingual sentiment analysis
 * - Accuracy metrics for performance evaluation
 * - Speed characteristics for performance optimization
 * 
 * This information is used by the ML layer to validate model compatibility,
 * display model information, and make runtime decisions about model selection
 * and configuration.
 * 
 * @param type Model type identifier (e.g., "EnhancedMultilingualSentimentAnalyzer")
 * @param version Model version string for compatibility and updates
 * @param language Supported language(s) for sentiment analysis
 * @param accuracy Model accuracy as a string representation
 * @param speed Model processing speed characteristics
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
 * Context patterns for enhanced sentiment analysis with domain-specific terminology.
 * 
 * This data class provides context patterns that enhance sentiment analysis by
 * incorporating domain-specific terminology and quality indicators. It supports
 * both positive and negative context patterns that can be used to boost sentiment
 * analysis accuracy in specific domains, such as movie reviews or product evaluations.
 * 
 * Context patterns work by:
 * - Identifying domain-specific positive indicators (masterpiece, brilliant, genius)
 * - Detecting domain-specific negative indicators (flop, disaster, nightmare)
 * - Boosting sentiment confidence when context patterns are found in analyzed text
 * - Providing more accurate sentiment classification in specialized domains
 * 
 * The patterns are optional and can be null if not available in the model configuration.
 * When present, they significantly improve sentiment analysis accuracy for domain-specific
 * content by providing additional context for sentiment classification.
 * 
 * @param strong_positive Optional list of strong positive context patterns for sentiment boosting
 * @param strong_negative Optional list of strong negative context patterns for sentiment boosting
 */
@Serializable
data class ContextPatterns(
    val strong_positive: List<String>? = null,
    val strong_negative: List<String>? = null
)

/**
 * Enhanced algorithm configuration for advanced sentiment analysis parameters.
 * 
 * This data class provides comprehensive algorithm configuration for enhanced sentiment
 * analysis models, including confidence thresholds, weight parameters, and advanced
 * algorithm settings. It serves as the primary configuration source for sophisticated
 * sentiment analysis algorithms that provide higher accuracy and more nuanced sentiment
 * detection compared to basic keyword-based models.
 * 
 * The enhanced algorithm configuration includes:
 * - Base confidence settings for sentiment classification
 * - Keyword threshold parameters for keyword-based analysis
 * - Weight parameters for different analysis components
 * - Confidence thresholds for sentiment classification
 * - Advanced parameters for optimal algorithm performance
 * 
 * This configuration is used by the ML layer to configure enhanced sentiment analysis
 * algorithms with optimal parameters for maximum accuracy and performance.
 * 
 * @param base_confidence Base confidence level for sentiment analysis (0.0-1.0)
 * @param keyword_threshold Minimum number of keywords required for analysis
 * @param context_weight Weight multiplier for context analysis influence
 * @param modifier_weight Weight multiplier for intensity modifiers influence
 * @param neutral_threshold Threshold for neutral sentiment classification
 * @param min_confidence Minimum acceptable confidence threshold
 * @param max_confidence Maximum confidence threshold cap
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
