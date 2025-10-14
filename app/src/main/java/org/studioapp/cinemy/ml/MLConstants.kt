package org.studioapp.cinemy.ml

/**
 * Constants for ML layer
 * Contains error messages and configuration values specific to ML functionality
 */
object MLConstants {
    
    // ML Runtime Error Messages
    const val ML_RUNTIME_NOT_INITIALIZED_ERROR = "ML runtime not initialized"
    const val LITERT_MODEL_NOT_AVAILABLE_ERROR = "LiteRT model not available"
    const val TENSORFLOW_LITE_MODEL_NOT_AVAILABLE_ERROR = "TensorFlow Lite model not available"
    const val KEYWORD_MODEL_NOT_AVAILABLE_ERROR = "Keyword model not available"
    const val LITERT_MODEL_NOT_READY_ERROR = "LiteRT model not ready"
    const val LITERT_ANALYSIS_FAILED_ERROR = "LiteRT analysis failed: %s"
    const val ML_KIT_INITIALIZATION_FAILED_ERROR = "Failed to initialize ML Kit text analyzer"
    
    // ML Processing Constants
    const val WORD_SPLIT_REGEX = "\\s+"
    const val DEFAULT_SCORE = 0.0
    const val SCORE_INCREMENT = 1.0
    const val MIN_CONFIDENCE_THRESHOLD = 0.3
    
    // Hardware Detection Constants
    const val TENSORFLOW_LITE_NNAPI_DELEGATE_CLASS = "org.tensorflow.lite.nnapi.NnApiDelegate"
    const val TENSORFLOW_LITE_XNNPACK_DELEGATE_CLASS = "org.tensorflow.lite.xnnpack.XnnpackDelegate"
    const val GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms"
    const val MLKIT_EXCEPTION_CLASS = "com.google.mlkit.common.MlKitException"
    const val MLKIT_DOWNLOAD_CONDITIONS_CLASS = "com.google.mlkit.common.model.DownloadConditions"
    const val MIN_PLAY_SERVICES_VERSION = 20000000
    
    // Sentiment Analysis Keywords
    const val POSITIVE_KEYWORD_GOOD = "good"
    const val POSITIVE_KEYWORD_GREAT = "great"
    const val POSITIVE_KEYWORD_EXCELLENT = "excellent"
    const val POSITIVE_KEYWORD_AMAZING = "amazing"
    const val POSITIVE_KEYWORD_WONDERFUL = "wonderful"
    const val POSITIVE_KEYWORD_FANTASTIC = "fantastic"
    const val POSITIVE_KEYWORD_LOVE = "love"
    const val POSITIVE_KEYWORD_BEST = "best"
    const val POSITIVE_KEYWORD_PERFECT = "perfect"
    const val POSITIVE_KEYWORD_AWESOME = "awesome"
    
    const val NEGATIVE_KEYWORD_BAD = "bad"
    const val NEGATIVE_KEYWORD_TERRIBLE = "terrible"
    const val NEGATIVE_KEYWORD_AWFUL = "awful"
    const val NEGATIVE_KEYWORD_HORRIBLE = "horrible"
    const val NEGATIVE_KEYWORD_HATE = "hate"
    const val NEGATIVE_KEYWORD_WORST = "worst"
    const val NEGATIVE_KEYWORD_DISGUSTING = "disgusting"
    const val NEGATIVE_KEYWORD_BORING = "boring"
    const val NEGATIVE_KEYWORD_STUPID = "stupid"
    const val NEGATIVE_KEYWORD_UGLY = "ugly"
    
    const val UNKNOWN_ERROR_MESSAGE = "Unknown error"
}
