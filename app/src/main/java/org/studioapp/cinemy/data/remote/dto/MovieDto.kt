package org.studioapp.cinemy.data.remote.dto

import com.google.gson.annotations.SerializedName
import org.studioapp.cinemy.data.model.StringConstants.FIELD_API_SUCCESS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_NEGATIVE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_NEGATIVE_COUNT
import org.studioapp.cinemy.data.model.StringConstants.FIELD_POSITIVE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_POSITIVE_COUNT
import org.studioapp.cinemy.data.model.StringConstants.FIELD_SENTIMENT_METADATA
import org.studioapp.cinemy.data.model.StringConstants.FIELD_SENTIMENT_REVIEWS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_SOURCE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_TIMESTAMP
import org.studioapp.cinemy.data.model.StringConstants.FIELD_TOTAL_REVIEWS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ACCENT
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ADULT
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_AI_GENERATED
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_APP_TITLE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_AVG_RATING
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_BACKDROP_PATH
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_BACKGROUND
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_BACK_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_BUDGET
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_BUTTONS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_BUTTON_CORNER_RADIUS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_BUTTON_TEXT_COLOR
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_CATEGORY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_COLORS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_COLOR_BASED
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_DATA
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ERROR
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ERROR_MESSAGE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_GEMINI_COLORS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_GENRES
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_GENRE_IDS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ID
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_LOADING_TEXT
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_LOGO_PATH
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_META
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_METADATA
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_METHOD
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_MODEL_USED
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_MOVIE_ID
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_MOVIE_POSTER_COLORS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_MOVIE_RATING
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_NAME
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_NO_MOVIES_FOUND
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ON_BACKGROUND
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ON_PRIMARY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ON_SECONDARY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ON_SURFACE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ORIGINAL_LANGUAGE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ORIGINAL_TITLE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ORIGIN_COUNTRY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_OVERVIEW
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_PAGE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_PLAY_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_POPULARITY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_POSTER_PATH
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_PRIMARY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_PRIMARY_BUTTON_COLOR
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_PRODUCTION_COMPANIES
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_QUERY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RATING
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RATING_TYPE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RELEASE_DATE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RESULTS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RESULTS_COUNT
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RESULT_COUNT
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RETRY_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_REVENUE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RUNTIME
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_SEARCH_INFO
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_SEARCH_QUERY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_SECONDARY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_SECONDARY_BUTTON_COLOR
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_STATUS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_SUCCESS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_SURFACE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_TEXTS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_TIMESTAMP
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_TITLE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_TOTAL_PAGES
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_TOTAL_RESULTS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_UI_CONFIG
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_VERSION
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_VIDEO
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_VOTE_AVERAGE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_VOTE_COUNT

// Movie Data DTOs

/**
 * Data Transfer Object for basic movie information.
 * 
 * This DTO represents the core movie data structure used for movie lists and basic movie information.
 * It includes essential movie details like title, description, ratings, and visual assets.
 * 
 * @property id Unique identifier for the movie
 * @property title The movie title
 * @property description Movie overview/description
 * @property posterPath Path to the movie poster image (nullable)
 * @property rating Average rating of the movie
 * @property voteCount Number of votes for the rating
 * @property releaseDate Release date of the movie
 * @property backdropPath Path to the movie backdrop image (nullable)
 * @property genreIds List of genre IDs associated with the movie
 * @property popularity Popularity score of the movie
 * @property adult Whether the movie is marked as adult content
 * @property originalLanguage Original language of the movie
 * @property originalTitle Original title of the movie
 * @property video Whether the movie has video content
 * @property colors Movie-specific color palette for UI theming
 */
data class MovieDto(
    @SerializedName(SERIALIZED_ID)
    val id: Int,
    @SerializedName(SERIALIZED_TITLE)
    val title: String,
    @SerializedName(SERIALIZED_OVERVIEW)
    val description: String,
    @SerializedName(SERIALIZED_POSTER_PATH)
    val posterPath: String?,
    @SerializedName(SERIALIZED_VOTE_AVERAGE)
    val rating: Double,
    @SerializedName(SERIALIZED_VOTE_COUNT)
    val voteCount: Int,
    @SerializedName(SERIALIZED_RELEASE_DATE)
    val releaseDate: String,
    @SerializedName(SERIALIZED_BACKDROP_PATH)
    val backdropPath: String?,
    @SerializedName(SERIALIZED_GENRE_IDS)
    val genreIds: List<Int> = emptyList(),
    @SerializedName(SERIALIZED_POPULARITY)
    val popularity: Double,
    @SerializedName(SERIALIZED_ADULT)
    val adult: Boolean,
    @SerializedName(SERIALIZED_ORIGINAL_LANGUAGE)
    val originalLanguage: String,
    @SerializedName(SERIALIZED_ORIGINAL_TITLE)
    val originalTitle: String,
    @SerializedName(SERIALIZED_VIDEO)
    val video: Boolean,
    @SerializedName(SERIALIZED_COLORS)
    val colors: MovieColorsDto
)

/**
 * Data Transfer Object for paginated movie list responses.
 * 
 * This DTO represents the response structure for movie list API calls, containing
 * the list of movies along with pagination metadata.
 * 
 * @property page Current page number in the pagination
 * @property results List of MovieDto objects for the current page
 * @property totalPages Total number of pages available
 * @property totalResults Total number of movies across all pages
 */
data class MovieListResponseDto(
    @SerializedName(SERIALIZED_PAGE)
    val page: Int,
    @SerializedName(SERIALIZED_RESULTS)
    val results: List<MovieDto>,
    @SerializedName(SERIALIZED_TOTAL_PAGES)
    val totalPages: Int,
    @SerializedName(SERIALIZED_TOTAL_RESULTS)
    val totalResults: Int
)

/**
 * Data Transfer Object for detailed movie information.
 * 
 * This DTO represents comprehensive movie details including extended information
 * such as runtime, genres, production companies, budget, revenue, and sentiment analysis data.
 * 
 * @property id Unique identifier for the movie
 * @property title The movie title
 * @property description Movie overview/description
 * @property posterPath Path to the movie poster image (nullable)
 * @property backdropPath Path to the movie backdrop image (nullable)
 * @property rating Average rating of the movie
 * @property voteCount Number of votes for the rating
 * @property releaseDate Release date of the movie
 * @property runtime Movie runtime in minutes (nullable)
 * @property genres List of genre information
 * @property productionCompanies List of production company information
 * @property budget Movie production budget
 * @property revenue Movie box office revenue
 * @property status Current status of the movie (e.g., "Released", "Post Production")
 * @property sentimentReviews Sentiment analysis results from reviews (nullable)
 * @property sentimentMetadata Metadata about sentiment analysis (nullable)
 */
data class MovieDetailsDto(
    @SerializedName(SERIALIZED_ID)
    val id: Int,
    @SerializedName(SERIALIZED_TITLE)
    val title: String,
    @SerializedName(SERIALIZED_OVERVIEW)
    val description: String,
    @SerializedName(SERIALIZED_POSTER_PATH)
    val posterPath: String?,
    @SerializedName(SERIALIZED_BACKDROP_PATH)
    val backdropPath: String?,
    @SerializedName(SERIALIZED_VOTE_AVERAGE)
    val rating: Double,
    @SerializedName(SERIALIZED_VOTE_COUNT)
    val voteCount: Int,
    @SerializedName(SERIALIZED_RELEASE_DATE)
    val releaseDate: String,
    @SerializedName(SERIALIZED_RUNTIME)
    val runtime: Int?,
    @SerializedName(SERIALIZED_GENRES)
    val genres: List<GenreDto>,
    @SerializedName(SERIALIZED_PRODUCTION_COMPANIES)
    val productionCompanies: List<ProductionCompanyDto>,
    @SerializedName(SERIALIZED_BUDGET)
    val budget: Long,
    @SerializedName(SERIALIZED_REVENUE)
    val revenue: Long,
    @SerializedName(SERIALIZED_STATUS)
    val status: String,
    @SerializedName(FIELD_SENTIMENT_REVIEWS)
    val sentimentReviews: SentimentReviewsDto? = null,
    @SerializedName(FIELD_SENTIMENT_METADATA)
    val sentimentMetadata: SentimentMetadataDto? = null
)

/**
 * Data Transfer Object for movie genre information.
 * 
 * Represents a movie genre with its identifier and name.
 * 
 * @property id Unique identifier for the genre
 * @property name Human-readable name of the genre
 */
data class GenreDto(
    @SerializedName(SERIALIZED_ID)
    val id: Int,
    @SerializedName(SERIALIZED_NAME)
    val name: String
)

/**
 * Data Transfer Object for movie production company information.
 * 
 * Represents a production company involved in movie creation with company details.
 * 
 * @property id Unique identifier for the production company
 * @property logoPath Path to the company logo image (nullable)
 * @property name Name of the production company
 * @property originCountry Country of origin for the production company
 */
data class ProductionCompanyDto(
    @SerializedName(SERIALIZED_ID)
    val id: Int,
    @SerializedName(SERIALIZED_LOGO_PATH)
    val logoPath: String?,
    @SerializedName(SERIALIZED_NAME)
    val name: String,
    @SerializedName(SERIALIZED_ORIGIN_COUNTRY)
    val originCountry: String
)

// UI Configuration DTOs

/**
 * Data Transfer Object for complete UI configuration.
 * 
 * This DTO represents the comprehensive UI configuration received from the backend,
 * including color schemes, text content, button styling, and search-specific settings.
 * 
 * @property colors Color palette configuration for the UI
 * @property texts Text content configuration for various UI elements
 * @property buttons Button styling configuration
 * @property searchInfo Search-specific UI configuration (nullable)
 */
data class UiConfigurationDto(
    @SerializedName(SERIALIZED_COLORS)
    val colors: ColorSchemeDto,
    @SerializedName(SERIALIZED_TEXTS)
    val texts: TextConfigurationDto,
    @SerializedName(SERIALIZED_BUTTONS)
    val buttons: ButtonConfigurationDto,
    @SerializedName(SERIALIZED_SEARCH_INFO)
    val searchInfo: SearchInfoDto? = null
)

/**
 * Data Transfer Object for color scheme configuration.
 * 
 * Represents the color palette used throughout the application UI,
 * including primary, secondary, background, and surface colors with their on-colors.
 * 
 * @property primary Primary color for the UI theme
 * @property secondary Secondary color for the UI theme
 * @property background Background color for the UI
 * @property surface Surface color for cards and elevated elements
 * @property onPrimary Color to use on primary color backgrounds
 * @property onSecondary Color to use on secondary color backgrounds
 * @property onBackground Color to use on background color
 * @property onSurface Color to use on surface color
 * @property moviePosterColors List of colors extracted from movie posters for theming
 */
data class ColorSchemeDto(
    @SerializedName(SERIALIZED_PRIMARY)
    val primary: String,
    @SerializedName(SERIALIZED_SECONDARY)
    val secondary: String,
    @SerializedName(SERIALIZED_BACKGROUND)
    val background: String,
    @SerializedName(SERIALIZED_SURFACE)
    val surface: String,
    @SerializedName(SERIALIZED_ON_PRIMARY)
    val onPrimary: String,
    @SerializedName(SERIALIZED_ON_SECONDARY)
    val onSecondary: String,
    @SerializedName(SERIALIZED_ON_BACKGROUND)
    val onBackground: String,
    @SerializedName(SERIALIZED_ON_SURFACE)
    val onSurface: String,
    @SerializedName(SERIALIZED_MOVIE_POSTER_COLORS)
    val moviePosterColors: List<String>
)

/**
 * Data Transfer Object for text content configuration.
 * 
 * Represents all text content used throughout the application UI,
 * including app title, loading messages, error messages, and button labels.
 * 
 * @property appTitle Main application title
 * @property loadingText Text displayed during loading operations
 * @property errorMessage Default error message for failed operations
 * @property noMoviesFound Message displayed when no movies are found
 * @property retryButton Text for retry button
 * @property backButton Text for back navigation button
 * @property playButton Text for play button
 */
data class TextConfigurationDto(
    @SerializedName(SERIALIZED_APP_TITLE)
    val appTitle: String,
    @SerializedName(SERIALIZED_LOADING_TEXT)
    val loadingText: String,
    @SerializedName(SERIALIZED_ERROR_MESSAGE)
    val errorMessage: String,
    @SerializedName(SERIALIZED_NO_MOVIES_FOUND)
    val noMoviesFound: String,
    @SerializedName(SERIALIZED_RETRY_BUTTON)
    val retryButton: String,
    @SerializedName(SERIALIZED_BACK_BUTTON)
    val backButton: String,
    @SerializedName(SERIALIZED_PLAY_BUTTON)
    val playButton: String
)

/**
 * Data Transfer Object for button styling configuration.
 * 
 * Represents the styling configuration for buttons throughout the application,
 * including colors and visual properties.
 * 
 * @property primaryButtonColor Color for primary buttons
 * @property secondaryButtonColor Color for secondary buttons
 * @property buttonTextColor Color for button text
 * @property buttonCornerRadius Corner radius for button styling
 */
data class ButtonConfigurationDto(
    @SerializedName(SERIALIZED_PRIMARY_BUTTON_COLOR)
    val primaryButtonColor: String,
    @SerializedName(SERIALIZED_SECONDARY_BUTTON_COLOR)
    val secondaryButtonColor: String,
    @SerializedName(SERIALIZED_BUTTON_TEXT_COLOR)
    val buttonTextColor: String,
    @SerializedName(SERIALIZED_BUTTON_CORNER_RADIUS)
    val buttonCornerRadius: Int
)

/**
 * Data Transfer Object for search-specific UI configuration.
 * 
 * Represents search-related configuration and metadata for search operations,
 * including query information, result counts, and rating information.
 * 
 * @property query The search query that was executed
 * @property resultCount Number of results returned by the search
 * @property avgRating Average rating of the search results
 * @property ratingType Type of rating system used
 * @property colorBased Whether the search results are based on color analysis
 */
data class SearchInfoDto(
    @SerializedName(SERIALIZED_QUERY)
    val query: String,
    @SerializedName(SERIALIZED_RESULT_COUNT)
    val resultCount: Int,
    @SerializedName(SERIALIZED_AVG_RATING)
    val avgRating: Double,
    @SerializedName(SERIALIZED_RATING_TYPE)
    val ratingType: String,
    @SerializedName(SERIALIZED_COLOR_BASED)
    val colorBased: Boolean
)

/**
 * Data Transfer Object for API response metadata.
 * 
 * Represents metadata about API responses including timing, method information,
 * context data, and AI generation details.
 * 
 * @property timestamp Timestamp when the response was generated
 * @property method The API method that was called
 * @property searchQuery The search query used (nullable)
 * @property movieId The movie ID for movie-specific requests (nullable)
 * @property resultsCount Number of results returned (nullable)
 * @property aiGenerated Whether the response was AI-generated
 * @property geminiColors AI-generated color suggestions
 * @property avgRating Average rating information (nullable)
 * @property movieRating Movie-specific rating (nullable)
 * @property version API version information
 */
data class MetaDto(
    @SerializedName(SERIALIZED_TIMESTAMP)
    val timestamp: String,
    @SerializedName(SERIALIZED_METHOD)
    val method: String,
    @SerializedName(SERIALIZED_SEARCH_QUERY)
    val searchQuery: String? = null,
    @SerializedName(SERIALIZED_MOVIE_ID)
    val movieId: Int? = null,
    @SerializedName(SERIALIZED_RESULTS_COUNT)
    val resultsCount: Int? = null,
    @SerializedName(SERIALIZED_AI_GENERATED)
    val aiGenerated: Boolean,
    @SerializedName(SERIALIZED_GEMINI_COLORS)
    val geminiColors: GeminiColorsDto,
    @SerializedName(SERIALIZED_AVG_RATING)
    val avgRating: Double? = null,
    @SerializedName(SERIALIZED_MOVIE_RATING)
    val movieRating: Double? = null,
    @SerializedName(SERIALIZED_VERSION)
    val version: String
)

/**
 * Data Transfer Object for AI-generated color suggestions.
 * 
 * Represents color palette suggestions generated by AI (Gemini) for dynamic theming.
 * 
 * @property primary AI-suggested primary color
 * @property secondary AI-suggested secondary color
 * @property accent AI-suggested accent color
 */
data class GeminiColorsDto(
    @SerializedName(SERIALIZED_PRIMARY)
    val primary: String,
    @SerializedName(SERIALIZED_SECONDARY)
    val secondary: String,
    @SerializedName(SERIALIZED_ACCENT)
    val accent: String
)

// MCP Response DTOs

/**
 * Generic Data Transfer Object for MCP (Model Context Protocol) responses.
 * 
 * This DTO represents the standard response structure from MCP backend calls,
 * containing success status, data payload, UI configuration, error information, and metadata.
 * 
 * @param T The type of data payload contained in the response
 * @property success Whether the operation was successful
 * @property data The response data payload (nullable)
 * @property uiConfig UI configuration for the response (nullable)
 * @property error Error message if the operation failed (nullable)
 * @property meta Metadata about the response
 */
data class McpResponseDto<T>(
    @SerializedName(SERIALIZED_SUCCESS)
    val success: Boolean,
    @SerializedName(SERIALIZED_DATA)
    val data: T?,
    @SerializedName(SERIALIZED_UI_CONFIG)
    val uiConfig: UiConfigurationDto,
    @SerializedName(SERIALIZED_ERROR)
    val error: String? = null,
    @SerializedName(SERIALIZED_META)
    val meta: MetaDto
)


/**
 * Data Transfer Object for sentiment analysis review results.
 * 
 * Represents categorized sentiment analysis results containing positive and negative review texts
 * from sentiment analysis processing.
 * 
 * @property positive List of positive review texts (defaults to empty list)
 * @property negative List of negative review texts (defaults to empty list)
 */
data class SentimentReviewsDto(
    @SerializedName(FIELD_POSITIVE)
    val positive: List<String> = emptyList(),
    @SerializedName(FIELD_NEGATIVE)
    val negative: List<String> = emptyList()
)

/**
 * Data Transfer Object for sentiment analysis metadata.
 * 
 * Represents high-level counters and provenance information for sentiment analysis results,
 * including review counts, source information, and API success tracking.
 * 
 * @property totalReviews Total number of reviews analyzed (defaults to 0)
 * @property positiveCount Count of reviews classified as positive (defaults to 0)
 * @property negativeCount Count of reviews classified as negative (defaults to 0)
 * @property source Free-form origin of the sentiment data (defaults to empty string)
 * @property timestamp Backend-provided timestamp for when analysis was produced (defaults to empty string)
 * @property apiSuccess Provider/system identifiers mapped to success flags (defaults to empty map)
 */
data class SentimentMetadataDto(
    @SerializedName(FIELD_TOTAL_REVIEWS)
    val totalReviews: Int = 0,
    @SerializedName(FIELD_POSITIVE_COUNT)
    val positiveCount: Int = 0,
    @SerializedName(FIELD_NEGATIVE_COUNT)
    val negativeCount: Int = 0,
    @SerializedName(FIELD_SOURCE)
    val source: String = "",
    @SerializedName(FIELD_TIMESTAMP)
    val timestamp: String = "",
    @SerializedName(FIELD_API_SUCCESS)
    val apiSuccess: Map<String, Boolean> = emptyMap()
)

/**
 * Data Transfer Object for movie-specific color palette.
 * 
 * Represents the color palette extracted from a specific movie for dynamic theming,
 * including accent, primary, and secondary colors with metadata about the color analysis.
 * 
 * @property accent Accent color extracted from the movie
 * @property primary Primary color extracted from the movie
 * @property secondary Secondary color extracted from the movie
 * @property metadata Metadata about the color analysis process
 */
data class MovieColorsDto(
    @SerializedName(SERIALIZED_ACCENT)
    val accent: String,
    @SerializedName(SERIALIZED_PRIMARY)
    val primary: String,
    @SerializedName(SERIALIZED_SECONDARY)
    val secondary: String,
    @SerializedName(SERIALIZED_METADATA)
    val metadata: ColorMetadataDto
)

/**
 * Data Transfer Object for color analysis metadata.
 * 
 * Represents metadata about the color analysis process used to extract colors from movies,
 * including the analysis category, model usage, and rating information.
 * 
 * @property category Category of the color analysis (e.g., "dominant", "complementary")
 * @property modelUsed Whether AI model was used for color analysis
 * @property rating Rating or confidence score for the color analysis
 */
data class ColorMetadataDto(
    @SerializedName(SERIALIZED_CATEGORY)
    val category: String,
    @SerializedName(SERIALIZED_MODEL_USED)
    val modelUsed: Boolean,
    @SerializedName(SERIALIZED_RATING)
    val rating: Double
)
