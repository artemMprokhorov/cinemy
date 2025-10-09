package org.studioapp.cinemy.data.remote.dto

import com.google.gson.annotations.SerializedName
import org.studioapp.cinemy.data.model.StringConstants

// Movie Data DTOs
data class MovieDto(
    @SerializedName(StringConstants.SERIALIZED_ID)
    val id: Int,
    @SerializedName(StringConstants.SERIALIZED_TITLE)
    val title: String,
    @SerializedName(StringConstants.SERIALIZED_OVERVIEW)
    val description: String,
    @SerializedName(StringConstants.SERIALIZED_POSTER_PATH)
    val posterPath: String?,
    @SerializedName(StringConstants.SERIALIZED_VOTE_AVERAGE)
    val rating: Double,
    @SerializedName(StringConstants.SERIALIZED_VOTE_COUNT)
    val voteCount: Int,
    @SerializedName(StringConstants.SERIALIZED_RELEASE_DATE)
    val releaseDate: String,
    @SerializedName(StringConstants.SERIALIZED_BACKDROP_PATH)
    val backdropPath: String?,
    @SerializedName(StringConstants.SERIALIZED_GENRE_IDS)
    val genreIds: List<Int> = emptyList(),
    @SerializedName(StringConstants.SERIALIZED_POPULARITY)
    val popularity: Double,
    @SerializedName(StringConstants.SERIALIZED_ADULT)
    val adult: Boolean,
    @SerializedName(StringConstants.SERIALIZED_ORIGINAL_LANGUAGE)
    val originalLanguage: String,
    @SerializedName(StringConstants.SERIALIZED_ORIGINAL_TITLE)
    val originalTitle: String,
    @SerializedName(StringConstants.SERIALIZED_VIDEO)
    val video: Boolean,
    @SerializedName(StringConstants.SERIALIZED_COLORS)
    val colors: MovieColorsDto
)

data class MovieListResponseDto(
    @SerializedName(StringConstants.SERIALIZED_PAGE)
    val page: Int,
    @SerializedName(StringConstants.SERIALIZED_RESULTS)
    val results: List<MovieDto>,
    @SerializedName(StringConstants.SERIALIZED_TOTAL_PAGES)
    val totalPages: Int,
    @SerializedName(StringConstants.SERIALIZED_TOTAL_RESULTS)
    val totalResults: Int
)

data class MovieDetailsDto(
    @SerializedName(StringConstants.SERIALIZED_ID)
    val id: Int,
    @SerializedName(StringConstants.SERIALIZED_TITLE)
    val title: String,
    @SerializedName(StringConstants.SERIALIZED_OVERVIEW)
    val description: String,
    @SerializedName(StringConstants.SERIALIZED_POSTER_PATH)
    val posterPath: String?,
    @SerializedName(StringConstants.SERIALIZED_BACKDROP_PATH)
    val backdropPath: String?,
    @SerializedName(StringConstants.SERIALIZED_VOTE_AVERAGE)
    val rating: Double,
    @SerializedName(StringConstants.SERIALIZED_VOTE_COUNT)
    val voteCount: Int,
    @SerializedName(StringConstants.SERIALIZED_RELEASE_DATE)
    val releaseDate: String,
    @SerializedName(StringConstants.SERIALIZED_RUNTIME)
    val runtime: Int?,
    @SerializedName(StringConstants.SERIALIZED_GENRES)
    val genres: List<GenreDto>,
    @SerializedName(StringConstants.SERIALIZED_PRODUCTION_COMPANIES)
    val productionCompanies: List<ProductionCompanyDto>,
    @SerializedName(StringConstants.SERIALIZED_BUDGET)
    val budget: Long,
    @SerializedName(StringConstants.SERIALIZED_REVENUE)
    val revenue: Long,
    @SerializedName(StringConstants.SERIALIZED_STATUS)
    val status: String,
    @SerializedName(StringConstants.FIELD_SENTIMENT_REVIEWS)
    val sentimentReviews: SentimentReviewsDto? = null,
    @SerializedName(StringConstants.FIELD_SENTIMENT_METADATA)
    val sentimentMetadata: SentimentMetadataDto? = null
)

data class GenreDto(
    @SerializedName(StringConstants.SERIALIZED_ID)
    val id: Int,
    @SerializedName(StringConstants.SERIALIZED_NAME)
    val name: String
)

data class ProductionCompanyDto(
    @SerializedName(StringConstants.SERIALIZED_ID)
    val id: Int,
    @SerializedName(StringConstants.SERIALIZED_LOGO_PATH)
    val logoPath: String?,
    @SerializedName(StringConstants.SERIALIZED_NAME)
    val name: String,
    @SerializedName(StringConstants.SERIALIZED_ORIGIN_COUNTRY)
    val originCountry: String
)

// UI Configuration DTOs
data class UiConfigurationDto(
    @SerializedName(StringConstants.SERIALIZED_COLORS)
    val colors: ColorSchemeDto,
    @SerializedName(StringConstants.SERIALIZED_TEXTS)
    val texts: TextConfigurationDto,
    @SerializedName(StringConstants.SERIALIZED_BUTTONS)
    val buttons: ButtonConfigurationDto,
    @SerializedName(StringConstants.SERIALIZED_SEARCH_INFO)
    val searchInfo: SearchInfoDto? = null
)

data class ColorSchemeDto(
    @SerializedName(StringConstants.SERIALIZED_PRIMARY)
    val primary: String,
    @SerializedName(StringConstants.SERIALIZED_SECONDARY)
    val secondary: String,
    @SerializedName(StringConstants.SERIALIZED_BACKGROUND)
    val background: String,
    @SerializedName(StringConstants.SERIALIZED_SURFACE)
    val surface: String,
    @SerializedName(StringConstants.SERIALIZED_ON_PRIMARY)
    val onPrimary: String,
    @SerializedName(StringConstants.SERIALIZED_ON_SECONDARY)
    val onSecondary: String,
    @SerializedName(StringConstants.SERIALIZED_ON_BACKGROUND)
    val onBackground: String,
    @SerializedName(StringConstants.SERIALIZED_ON_SURFACE)
    val onSurface: String,
    @SerializedName(StringConstants.SERIALIZED_MOVIE_POSTER_COLORS)
    val moviePosterColors: List<String>
)

data class TextConfigurationDto(
    @SerializedName(StringConstants.SERIALIZED_APP_TITLE)
    val appTitle: String,
    @SerializedName(StringConstants.SERIALIZED_LOADING_TEXT)
    val loadingText: String,
    @SerializedName(StringConstants.SERIALIZED_ERROR_MESSAGE)
    val errorMessage: String,
    @SerializedName(StringConstants.SERIALIZED_NO_MOVIES_FOUND)
    val noMoviesFound: String,
    @SerializedName(StringConstants.SERIALIZED_RETRY_BUTTON)
    val retryButton: String,
    @SerializedName(StringConstants.SERIALIZED_BACK_BUTTON)
    val backButton: String,
    @SerializedName(StringConstants.SERIALIZED_PLAY_BUTTON)
    val playButton: String
)

data class ButtonConfigurationDto(
    @SerializedName(StringConstants.SERIALIZED_PRIMARY_BUTTON_COLOR)
    val primaryButtonColor: String,
    @SerializedName(StringConstants.SERIALIZED_SECONDARY_BUTTON_COLOR)
    val secondaryButtonColor: String,
    @SerializedName(StringConstants.SERIALIZED_BUTTON_TEXT_COLOR)
    val buttonTextColor: String,
    @SerializedName(StringConstants.SERIALIZED_BUTTON_CORNER_RADIUS)
    val buttonCornerRadius: Int
)

data class SearchInfoDto(
    @SerializedName(StringConstants.SERIALIZED_QUERY)
    val query: String,
    @SerializedName(StringConstants.SERIALIZED_RESULT_COUNT)
    val resultCount: Int,
    @SerializedName(StringConstants.SERIALIZED_AVG_RATING)
    val avgRating: Double,
    @SerializedName(StringConstants.SERIALIZED_RATING_TYPE)
    val ratingType: String,
    @SerializedName(StringConstants.SERIALIZED_COLOR_BASED)
    val colorBased: Boolean
)

data class MetaDto(
    @SerializedName(StringConstants.SERIALIZED_TIMESTAMP)
    val timestamp: String,
    @SerializedName(StringConstants.SERIALIZED_METHOD)
    val method: String,
    @SerializedName(StringConstants.SERIALIZED_SEARCH_QUERY)
    val searchQuery: String? = null,
    @SerializedName(StringConstants.SERIALIZED_MOVIE_ID)
    val movieId: Int? = null,
    @SerializedName(StringConstants.SERIALIZED_RESULTS_COUNT)
    val resultsCount: Int? = null,
    @SerializedName(StringConstants.SERIALIZED_AI_GENERATED)
    val aiGenerated: Boolean,
    @SerializedName(StringConstants.SERIALIZED_GEMINI_COLORS)
    val geminiColors: GeminiColorsDto,
    @SerializedName(StringConstants.SERIALIZED_AVG_RATING)
    val avgRating: Double? = null,
    @SerializedName(StringConstants.SERIALIZED_MOVIE_RATING)
    val movieRating: Double? = null,
    @SerializedName(StringConstants.SERIALIZED_VERSION)
    val version: String
)

data class GeminiColorsDto(
    @SerializedName(StringConstants.SERIALIZED_PRIMARY)
    val primary: String,
    @SerializedName(StringConstants.SERIALIZED_SECONDARY)
    val secondary: String,
    @SerializedName(StringConstants.SERIALIZED_ACCENT)
    val accent: String
)

// MCP Response DTOs
data class McpResponseDto<T>(
    @SerializedName(StringConstants.SERIALIZED_SUCCESS)
    val success: Boolean,
    @SerializedName(StringConstants.SERIALIZED_DATA)
    val data: T?,
    @SerializedName(StringConstants.SERIALIZED_UI_CONFIG)
    val uiConfig: UiConfigurationDto,
    @SerializedName(StringConstants.SERIALIZED_ERROR)
    val error: String? = null,
    @SerializedName(StringConstants.SERIALIZED_META)
    val meta: MetaDto
)


data class SentimentReviewsDto(
    @SerializedName(StringConstants.FIELD_POSITIVE)
    val positive: List<String> = emptyList(),
    @SerializedName(StringConstants.FIELD_NEGATIVE)
    val negative: List<String> = emptyList()
)

data class SentimentMetadataDto(
    @SerializedName(StringConstants.FIELD_TOTAL_REVIEWS)
    val totalReviews: Int = 0,
    @SerializedName(StringConstants.FIELD_POSITIVE_COUNT)
    val positiveCount: Int = 0,
    @SerializedName(StringConstants.FIELD_NEGATIVE_COUNT)
    val negativeCount: Int = 0,
    @SerializedName(StringConstants.FIELD_SOURCE)
    val source: String = "",
    @SerializedName(StringConstants.FIELD_TIMESTAMP)
    val timestamp: String = "",
    @SerializedName(StringConstants.FIELD_API_SUCCESS)
    val apiSuccess: Map<String, Boolean> = emptyMap()
)

data class MovieColorsDto(
    @SerializedName(StringConstants.SERIALIZED_ACCENT)
    val accent: String,
    @SerializedName(StringConstants.SERIALIZED_PRIMARY)
    val primary: String,
    @SerializedName(StringConstants.SERIALIZED_SECONDARY)
    val secondary: String,
    @SerializedName(StringConstants.SERIALIZED_METADATA)
    val metadata: ColorMetadataDto
)

data class ColorMetadataDto(
    @SerializedName(StringConstants.SERIALIZED_CATEGORY)
    val category: String,
    @SerializedName(StringConstants.SERIALIZED_MODEL_USED)
    val modelUsed: Boolean,
    @SerializedName(StringConstants.SERIALIZED_RATING)
    val rating: Double
)
