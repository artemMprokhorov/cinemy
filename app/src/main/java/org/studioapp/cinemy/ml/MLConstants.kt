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
    
    // Hardware Detection Constants
    const val TENSORFLOW_LITE_NNAPI_DELEGATE_CLASS = "org.tensorflow.lite.nnapi.NnApiDelegate"
    const val TENSORFLOW_LITE_XNNPACK_DELEGATE_CLASS = "org.tensorflow.lite.xnnpack.XnnpackDelegate"
    const val GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms"
    const val MLKIT_EXCEPTION_CLASS = "com.google.mlkit.common.MlKitException"
    const val MLKIT_DOWNLOAD_CONDITIONS_CLASS = "com.google.mlkit.common.model.DownloadConditions"
    const val MIN_PLAY_SERVICES_VERSION = 20000000
}
