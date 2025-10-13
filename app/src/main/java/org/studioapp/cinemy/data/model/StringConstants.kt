package org.studioapp.cinemy.data.model

/**
 * String constants for the application
 * These should be used in data layer classes where Context is not available
 * For UI layer, use string resources instead
 */
object StringConstants {

    // Error Messages
    const val ERROR_UNKNOWN = "Unknown error"

    // Error Messages with Parameters
    const val ERROR_FETCHING_POPULAR_MOVIES = "Error fetching popular movies: %s"
    const val ERROR_FETCHING_MOVIE_DETAILS = "Error fetching movie details: %s"
    const val ERROR_NETWORK_WITH_MESSAGE = "Network error: %s"

    // Data Layer Constants
    const val INVALID_MOVIE_DETAILS_DATA = "Invalid movie details data"
    const val LOADING_MOVIES_TEXT = "Loading movies..."

    // MCP Method Names
    const val MCP_METHOD_GET_POPULAR_MOVIES = "getPopularMovies"
    const val MCP_METHOD_GET_MOVIE_DETAILS = "getMovieDetails"

    // UI Text Constants (for data layer)
    const val MOVIES_TITLE = "Movies"
    const val LOADING_TEXT = "Loading..."
    const val NO_MOVIES_FOUND = "No movies found"
    const val RETRY_BUTTON = "Retry"
    const val BACK_BUTTON = "Back"
    const val PLAY_BUTTON = "Play"


    // Error Messages
    const val UNKNOWN_MOVIE_TITLE = "Unknown Movie"
    const val NO_DESCRIPTION_AVAILABLE = "No description available"


    // Asset File Names
    const val ASSET_MOCK_MOVIES = "mock_movies.json"
    const val ASSET_MOCK_MOVIE_DETAILS = "mock_movie_details.json"

    // Error Messages for Mock Data
    const val ERROR_UNKNOWN_METHOD = "Unknown method: %s"
    const val ERROR_LOADING_MOCK_DATA = "Failed to load mock data from assets"

    // Pagination Constants
    const val PAGINATION_FIRST_PAGE = 1

    // Meta Data Constants
    const val META_RESULTS_COUNT_ZERO = 0

    // Network Simulation Constants
    const val NETWORK_DELAY_BASE_MS = 500L

    // Fake Interceptor Delay Constants
    const val FAKE_INTERCEPTOR_DELAY_BASE_MS = 300L
    const val FAKE_INTERCEPTOR_DELAY_RANDOM_MAX_MS = 500L


    // HTTP Timeout Constants
    const val HTTP_REQUEST_TIMEOUT_MS = 30000L
    const val HTTP_CONNECT_TIMEOUT_MS = 10000L

    // MCP HTTP Client User-Facing Messages
    const val MCP_MESSAGE_ALL_ENDPOINTS_FAILED = "All endpoints failed"
    const val MCP_MESSAGE_REAL_REQUEST_SUCCESSFUL = "Real request successful"
    const val MCP_MESSAGE_REAL_REQUEST_RAW_RESPONSE = "Real request successful (raw response)"
    const val MCP_MESSAGE_MOCK_DATA_LOADED_SUCCESSFULLY = "Mock data loaded successfully"

    // JSON Structure Constants
    const val JSON_OPEN_BRACE = "{"
    const val JSON_CLOSE_BRACE = "}"
    const val JSON_METHOD_FIELD = "\"method\":\"%s\","
    const val JSON_PARAMS_FIELD = "\"params\":{"
    const val JSON_PARAM_ENTRY = "\"%s\":\"%s\""
    const val JSON_COMMA = ","

    // HTML Error Detection Constants
    const val HTML_DOCTYPE = "<!DOCTYPE html>"
    const val HTML_CANNOT_POST = "Cannot POST"
    const val HTML_TAG = "<html"
    const val HTML_ERROR_TITLE = "<title>Error</title>"
    const val JSON_ARRAY_START = "["

    // Common String Constants
    const val EMPTY_STRING = ""

    // Default Value Constants
    const val DEFAULT_INT_VALUE = 0
    const val DEFAULT_DOUBLE_VALUE = 0.0
    const val DEFAULT_LONG_VALUE = 0L
    const val DEFAULT_PAGE_NUMBER = 1
    const val DEFAULT_TOTAL_PAGES = 1
    const val DEFAULT_BOOLEAN_VALUE = false

    // UI Constants
    const val BUTTON_CORNER_RADIUS = 8

    // Color Constants
    const val COLOR_PRIMARY = "#2C5F6F"
    const val COLOR_SECONDARY = "#4A90A4"
    const val COLOR_ACCENT = "#FFD700"
    const val COLOR_BACKGROUND = "#1A1A1A"
    const val COLOR_SURFACE = "#2A2A2A"
    const val COLOR_ON_PRIMARY = "#FFFFFF"
    const val COLOR_ON_SECONDARY = "#FFFFFF"
    const val COLOR_ON_BACKGROUND = "#FFFFFF"
    const val COLOR_ON_SURFACE = "#FFFFFF"
    const val COLOR_BUTTON_TEXT = "#FFFFFF"

    // Version and Status Constants
    const val VERSION_2_0_0 = "2.0.0"

    // JSON Field Names
    const val FIELD_TITLE = "title"
    const val FIELD_DESCRIPTION = "description"
    const val FIELD_POSTER_PATH = "posterPath"
    const val FIELD_BACKDROP_PATH = "backdropPath"
    const val FIELD_RATING = "rating"
    const val FIELD_VOTE_COUNT = "voteCount"
    const val FIELD_RELEASE_DATE = "releaseDate"
    const val FIELD_RUNTIME = "runtime"
    const val FIELD_GENRES = "genres"
    const val FIELD_PRODUCTION_COMPANIES = "productionCompanies"
    const val FIELD_BUDGET = "budget"
    const val FIELD_REVENUE = "revenue"
    const val FIELD_STATUS = "status"

    // Additional JSON Field Names
    const val FIELD_ID = "id"
    const val FIELD_NAME = "name"
    const val FIELD_PAGE = "page"
    const val FIELD_GENRE_IDS = "genreIds"
    const val FIELD_POPULARITY = "popularity"
    const val FIELD_ADULT = "adult"
    const val FIELD_ORIGINAL_LANGUAGE = "originalLanguage"
    const val FIELD_ORIGINAL_TITLE = "originalTitle"
    const val FIELD_VIDEO = "video"
    const val FIELD_MOVIES = "movies"
    const val FIELD_PAGINATION = "pagination"
    const val FIELD_MOVIE_DETAILS = "movieDetails"
    const val FIELD_TOTAL_PAGES = "totalPages"
    const val FIELD_TOTAL_RESULTS = "totalResults"
    const val FIELD_HAS_NEXT = "hasNext"
    const val FIELD_HAS_PREVIOUS = "hasPrevious"
    const val FIELD_LOGO_PATH = "logoPath"
    const val FIELD_ORIGIN_COUNTRY = "originCountry"

    // JSON Structure Field Names
    const val FIELD_UI_CONFIG = "uiConfig"
    const val FIELD_META = "meta"
    const val FIELD_DATA = "data"
    const val FIELD_COLORS = "colors"
    const val FIELD_TEXTS = "texts"
    const val FIELD_BUTTONS = "buttons"
    const val FIELD_MOVIE_POSTER_COLORS = "moviePosterColors"
    const val FIELD_GEMINI_COLORS = "geminiColors"

    // Response Field Names
    const val FIELD_SUCCESS = "success"
    const val FIELD_ERROR = "error"
    const val FIELD_MESSAGE = "message"

    // Color Field Names
    const val FIELD_PRIMARY = "primary"
    const val FIELD_SECONDARY = "secondary"
    const val FIELD_BACKGROUND = "background"
    const val FIELD_SURFACE = "surface"
    const val FIELD_ON_PRIMARY = "onPrimary"

    // Sentiment Reviews Field Names
    const val FIELD_SENTIMENT_REVIEWS = "sentimentReviews"
    const val FIELD_SENTIMENT_METADATA = "sentimentMetadata"
    const val FIELD_TOTAL_REVIEWS = "totalReviews"
    const val FIELD_POSITIVE_COUNT = "positiveCount"
    const val FIELD_NEGATIVE_COUNT = "negativeCount"
    const val FIELD_SOURCE = "source"
    const val FIELD_TIMESTAMP = "timestamp"
    const val FIELD_API_SUCCESS = "apiSuccess"
    const val FIELD_POSITIVE = "positive"
    const val FIELD_NEGATIVE = "negative"
    const val FIELD_ON_SECONDARY = "onSecondary"
    const val FIELD_ON_BACKGROUND = "onBackground"
    const val FIELD_ON_SURFACE = "onSurface"
    const val FIELD_PRIMARY_BUTTON_COLOR = "primaryButtonColor"
    const val FIELD_SECONDARY_BUTTON_COLOR = "secondaryButtonColor"
    const val FIELD_BUTTON_TEXT_COLOR = "buttonTextColor"
    const val FIELD_BUTTON_CORNER_RADIUS = "buttonCornerRadius"
    const val FIELD_ACCENT = "accent"
    const val FIELD_VALUE = "value"

    // Text Field Names
    const val FIELD_APP_TITLE = "appTitle"
    const val FIELD_LOADING_TEXT = "loadingText"
    const val FIELD_ERROR_MESSAGE = "errorMessage"
    const val FIELD_NO_MOVIES_FOUND = "noMoviesFound"
    const val FIELD_RETRY_BUTTON = "retryButton"
    const val FIELD_BACK_BUTTON = "backButton"
    const val FIELD_PLAY_BUTTON = "playButton"

    // Meta Field Names
    const val FIELD_SEARCH_QUERY = "searchQuery"
    const val FIELD_AI_GENERATED = "aiGenerated"
    const val FIELD_AVG_RATING = "avgRating"
    const val FIELD_MOVIE_RATING = "movieRating"
    const val FIELD_VERSION = "version"

    // Parameter Names
    const val PARAM_MOVIE_ID = "movieId"


    // Serialized Name Constants for MovieDto
    // Movie Data Fields
    const val SERIALIZED_ID = "id"
    const val SERIALIZED_TITLE = "title"
    const val SERIALIZED_OVERVIEW = "overview"
    const val SERIALIZED_POSTER_PATH = "poster_path"
    const val SERIALIZED_VOTE_AVERAGE = "vote_average"
    const val SERIALIZED_VOTE_COUNT = "vote_count"
    const val SERIALIZED_RELEASE_DATE = "release_date"
    const val SERIALIZED_BACKDROP_PATH = "backdrop_path"
    const val SERIALIZED_GENRE_IDS = "genre_ids"
    const val SERIALIZED_POPULARITY = "popularity"
    const val SERIALIZED_ADULT = "adult"
    const val SERIALIZED_ORIGINAL_LANGUAGE = "original_language"
    const val SERIALIZED_ORIGINAL_TITLE = "original_title"
    const val SERIALIZED_VIDEO = "video"
    const val SERIALIZED_METADATA = "metadata"
    const val SERIALIZED_CATEGORY = "category"
    const val SERIALIZED_MODEL_USED = "model_used"
    const val SERIALIZED_RATING = "rating"

    // Pagination Fields
    const val SERIALIZED_PAGE = "page"
    const val SERIALIZED_RESULTS = "results"
    const val SERIALIZED_TOTAL_PAGES = "total_pages"
    const val SERIALIZED_TOTAL_RESULTS = "total_results"

    // Movie Details Fields
    const val SERIALIZED_RUNTIME = "runtime"
    const val SERIALIZED_GENRES = "genres"
    const val SERIALIZED_PRODUCTION_COMPANIES = "production_companies"
    const val SERIALIZED_BUDGET = "budget"
    const val SERIALIZED_REVENUE = "revenue"
    const val SERIALIZED_STATUS = "status"
    const val SERIALIZED_NAME = "name"
    const val SERIALIZED_LOGO_PATH = "logo_path"
    const val SERIALIZED_ORIGIN_COUNTRY = "origin_country"

    // UI Configuration Fields
    const val SERIALIZED_COLORS = "colors"
    const val SERIALIZED_TEXTS = "texts"
    const val SERIALIZED_BUTTONS = "buttons"
    const val SERIALIZED_SEARCH_INFO = "search_info"

    // Color Scheme Fields
    const val SERIALIZED_PRIMARY = "primary"
    const val SERIALIZED_SECONDARY = "secondary"
    const val SERIALIZED_BACKGROUND = "background"
    const val SERIALIZED_SURFACE = "surface"
    const val SERIALIZED_ON_PRIMARY = "on_primary"
    const val SERIALIZED_ON_SECONDARY = "on_secondary"
    const val SERIALIZED_ON_BACKGROUND = "on_background"
    const val SERIALIZED_ON_SURFACE = "on_surface"
    const val SERIALIZED_MOVIE_POSTER_COLORS = "movie_poster_colors"

    // Text Configuration Fields
    const val SERIALIZED_APP_TITLE = "app_title"
    const val SERIALIZED_LOADING_TEXT = "loading_text"
    const val SERIALIZED_ERROR_MESSAGE = "error_message"
    const val SERIALIZED_NO_MOVIES_FOUND = "no_movies_found"
    const val SERIALIZED_RETRY_BUTTON = "retry_button"
    const val SERIALIZED_BACK_BUTTON = "back_button"
    const val SERIALIZED_PLAY_BUTTON = "play_button"

    // Button Configuration Fields
    const val SERIALIZED_PRIMARY_BUTTON_COLOR = "primary_button_color"
    const val SERIALIZED_SECONDARY_BUTTON_COLOR = "secondary_button_color"
    const val SERIALIZED_BUTTON_TEXT_COLOR = "button_text_color"
    const val SERIALIZED_BUTTON_CORNER_RADIUS = "button_corner_radius"

    // Search Info Fields
    const val SERIALIZED_QUERY = "query"
    const val SERIALIZED_RESULT_COUNT = "result_count"
    const val SERIALIZED_AVG_RATING = "avg_rating"
    const val SERIALIZED_RATING_TYPE = "rating_type"
    const val SERIALIZED_COLOR_BASED = "color_based"

    // Meta Fields
    const val SERIALIZED_TIMESTAMP = "timestamp"
    const val SERIALIZED_METHOD = "method"
    const val SERIALIZED_SEARCH_QUERY = "search_query"
    const val SERIALIZED_MOVIE_ID = "movie_id"
    const val SERIALIZED_RESULTS_COUNT = "results_count"
    const val SERIALIZED_AI_GENERATED = "ai_generated"
    const val SERIALIZED_GEMINI_COLORS = "gemini_colors"
    const val SERIALIZED_MOVIE_RATING = "movie_rating"
    const val SERIALIZED_VERSION = "version"
    const val SERIALIZED_ACCENT = "accent"

    // MCP Response Fields
    const val SERIALIZED_SUCCESS = "success"
    const val SERIALIZED_DATA = "data"
    const val SERIALIZED_UI_CONFIG = "ui_config"
    const val SERIALIZED_ERROR = "error"
    const val SERIALIZED_META = "meta"
}
