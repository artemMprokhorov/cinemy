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

    // Sentiment Analyzer Constants
    const val KEYWORD_MODEL_FILE = "multilingual_sentiment_production.json"
    const val ERROR_ANALYZER_NOT_INITIALIZED = "Analyzer not initialized"
    const val ERROR_ANALYSIS_ERROR = "Analysis error: "
    const val NO_MODELS_AVAILABLE_ERROR = "No models available"
    const val ML_MODELS_PATH = "ml_models/"

    // Sentiment Analysis Modifiers
    const val MODIFIER_ABSOLUTELY = "absolutely"
    const val MODIFIER_COMPLETELY = "completely"
    const val MODIFIER_TOTALLY = "totally"
    const val MODIFIER_EXTREMELY = "extremely"
    const val MODIFIER_INCREDIBLY = "incredibly"
    const val MODIFIER_VERY = "very"
    const val MODIFIER_REALLY = "really"
    const val MODIFIER_PRETTY = "pretty"
    const val MODIFIER_SOMEWHAT = "somewhat"
    const val MODIFIER_SLIGHTLY = "slightly"
    const val MODIFIER_NOT = "not"
    const val MODIFIER_NEVER = "never"
    const val MODIFIER_BARELY = "barely"

    // Sentiment Analysis Context Terms
    const val CONTEXT_CINEMATOGRAPHY = "cinematography"
    const val CONTEXT_ACTING = "acting"
    const val CONTEXT_PLOT = "plot"
    const val CONTEXT_STORY = "story"
    const val CONTEXT_DIRECTOR = "director"
    const val CONTEXT_PERFORMANCE = "performance"
    const val CONTEXT_SCRIPT = "script"
    const val CONTEXT_DIALOGUE = "dialogue"
    const val CONTEXT_VISUALS = "visuals"
    const val CONTEXT_EFFECTS = "effects"
    const val CONTEXT_SOUNDTRACK = "soundtrack"
    const val CONTEXT_EDITING = "editing"

    // Sentiment Analysis Quality Terms
    const val QUALITY_MASTERPIECE = "masterpiece"
    const val QUALITY_ARTISTRY = "artistry"
    const val QUALITY_BRILLIANT = "brilliant"
    const val QUALITY_GENIUS = "genius"
    const val QUALITY_INNOVATIVE = "innovative"
    const val QUALITY_GROUNDBREAKING = "groundbreaking"
    const val QUALITY_REVOLUTIONARY = "revolutionary"
    const val QUALITY_TIMELESS = "timeless"
    const val QUALITY_CLASSIC = "classic"

    // Sentiment Analysis Failure Terms
    const val FAILURE_FLOP = "flop"
    const val FAILURE_DISASTER = "disaster"
    const val FAILURE_FAILURE = "failure"
    const val FAILURE_RUINED = "ruined"
    const val FAILURE_DESTROYED = "destroyed"
    const val FAILURE_BUTCHERED = "butchered"
    const val FAILURE_MANGLED = "mangled"
    const val FAILURE_TORTURE = "torture"
    const val FAILURE_NIGHTMARE = "nightmare"

    // Sentiment Analysis Regex
    const val WORD_SPLIT_REGEX_PATTERN = "\\W+"

    // Sentiment Analysis Emojis
    const val EMOJI_POSITIVE = "+"
    const val EMOJI_NEGATIVE = "-"
    const val EMOJI_NEUTRAL = "~"
    const val EMOJI_MODIFIER = "*"
    const val EMOJI_MOVIE = "🎬"
    const val EMOJI_QUALITY = "✨"
    const val EMOJI_FAILURE = "💥"
    const val EMOJI_PATTERN = "🔥"
    const val EMOJI_NEGATIVE_PATTERN = "💀"

    // Sentiment Analysis Categories
    const val SENTIMENT_POSITIVE = "positive"
    const val SENTIMENT_NEGATIVE = "negative"
    const val SENTIMENT_NEUTRAL = "neutral"
}
