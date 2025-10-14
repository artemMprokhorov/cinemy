package org.studioapp.cinemy.data.remote.dto

import com.google.gson.annotations.SerializedName
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ID
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_TITLE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_OVERVIEW
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_POSTER_PATH
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_VOTE_AVERAGE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_VOTE_COUNT
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RELEASE_DATE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_BACKDROP_PATH
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_GENRE_IDS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_POPULARITY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ADULT
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ORIGINAL_LANGUAGE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ORIGINAL_TITLE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_VIDEO
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_COLORS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_PAGE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RESULTS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_TOTAL_PAGES
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_TOTAL_RESULTS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RUNTIME
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_GENRES
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_PRODUCTION_COMPANIES
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_BUDGET
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_REVENUE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_STATUS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_SENTIMENT_REVIEWS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_SENTIMENT_METADATA
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_NAME
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_LOGO_PATH
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ORIGIN_COUNTRY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_TEXTS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_BUTTONS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_SEARCH_INFO
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_PRIMARY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_SECONDARY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_BACKGROUND
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_SURFACE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ON_PRIMARY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ON_SECONDARY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ON_BACKGROUND
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ON_SURFACE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_MOVIE_POSTER_COLORS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_APP_TITLE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_LOADING_TEXT
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ERROR_MESSAGE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_NO_MOVIES_FOUND
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RETRY_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_BACK_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_PLAY_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_PRIMARY_BUTTON_COLOR
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_SECONDARY_BUTTON_COLOR
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_BUTTON_TEXT_COLOR
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_BUTTON_CORNER_RADIUS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_QUERY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RESULT_COUNT
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_AVG_RATING
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RATING_TYPE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_COLOR_BASED
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_TIMESTAMP
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_METHOD
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_SEARCH_QUERY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_MOVIE_ID
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RESULTS_COUNT
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_AI_GENERATED
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_GEMINI_COLORS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_MOVIE_RATING
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_VERSION
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ACCENT
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_SUCCESS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_DATA
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_UI_CONFIG
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ERROR
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_META
import org.studioapp.cinemy.data.model.StringConstants.FIELD_POSITIVE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_NEGATIVE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_TOTAL_REVIEWS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_POSITIVE_COUNT
import org.studioapp.cinemy.data.model.StringConstants.FIELD_NEGATIVE_COUNT
import org.studioapp.cinemy.data.model.StringConstants.FIELD_SOURCE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_TIMESTAMP
import org.studioapp.cinemy.data.model.StringConstants.FIELD_API_SUCCESS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_METADATA
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_CATEGORY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_MODEL_USED
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RATING

// Movie Data DTOs
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

data class GenreDto(
    @SerializedName(SERIALIZED_ID)
    val id: Int,
    @SerializedName(SERIALIZED_NAME)
    val name: String
)

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

data class GeminiColorsDto(
    @SerializedName(SERIALIZED_PRIMARY)
    val primary: String,
    @SerializedName(SERIALIZED_SECONDARY)
    val secondary: String,
    @SerializedName(SERIALIZED_ACCENT)
    val accent: String
)

// MCP Response DTOs
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


data class SentimentReviewsDto(
    @SerializedName(FIELD_POSITIVE)
    val positive: List<String> = emptyList(),
    @SerializedName(FIELD_NEGATIVE)
    val negative: List<String> = emptyList()
)

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

data class ColorMetadataDto(
    @SerializedName(SERIALIZED_CATEGORY)
    val category: String,
    @SerializedName(SERIALIZED_MODEL_USED)
    val modelUsed: Boolean,
    @SerializedName(SERIALIZED_RATING)
    val rating: Double
)
