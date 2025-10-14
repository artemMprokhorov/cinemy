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
    
    // ML Processing Constants
    const val WORD_SPLIT_REGEX = "\\s+"
    const val DEFAULT_SCORE = 0.0
    const val SCORE_INCREMENT = 1.0
    const val MIN_CONFIDENCE_THRESHOLD = 0.3
}
