package org.studioapp.cinemy.data.model

import androidx.compose.ui.graphics.Color

/**
 * Represents a movie item used across the data and presentation layers.
 *
 * This model is the domain representation returned by repositories and consumed by UI.
 * It contains core identifiers, descriptive text, imagery paths, ratings, metadata,
 * and a color palette used for theming the UI around the movie.
 *
 * @property id Unique identifier of the movie.
 * @property title Localized display title of the movie.
 * @property description Short overview/description text of the movie.
 * @property posterPath Optional relative path to the poster image; null if unavailable.
 * @property backdropPath Optional relative path to the backdrop image; null if unavailable.
 * @property rating Average user rating on a 0..10 scale.
 * @property voteCount Number of votes used to compute [rating].
 * @property releaseDate ISO-8601 formatted release date (e.g., "2024-11-07").
 * @property genreIds List of numeric genre identifiers associated with the movie.
 * @property popularity Popularity metric as provided by the backend.
 * @property adult Whether the title targets adult audiences.
 * @property originalLanguage ISO 639-1 language code of the original production language.
 * @property originalTitle Original, unlocalized title.
 * @property video Whether the entry represents a video.
 * @property colors Color palette inferred for the movie poster/backdrop for UI theming.
 *
 * Exceptions: none. Construction is pure and non-throwing.
 */
data class Movie(
    val id: Int,
    val title: String,
    val description: String,
    val posterPath: String?,
    val backdropPath: String?,
    val rating: Double,
    val voteCount: Int,
    val releaseDate: String,
    val genreIds: List<Int> = emptyList(),
    val popularity: Double,
    val adult: Boolean,
    val originalLanguage: String,
    val originalTitle: String,
    val video: Boolean,
    val colors: MovieColors
)

/**
 * Detailed information about a specific movie.
 *
 * Extends the basic [Movie] information with runtime, production, and financial data.
 *
 * @property id Unique identifier of the movie.
 * @property title Display title of the movie.
 * @property description Overview/description text.
 * @property posterPath Optional relative path to the poster image; null if unavailable.
 * @property backdropPath Optional relative path to the backdrop image; null if unavailable.
 * @property rating Average rating on a 0..10 scale.
 * @property voteCount Number of votes used to compute [rating].
 * @property releaseDate ISO-8601 formatted release date.
 * @property runtime Runtime in minutes.
 * @property genres Ordered list of genres associated with the movie.
 * @property productionCompanies Companies involved in production.
 * @property budget Production budget in the smallest currency unit (e.g., USD).
 * @property revenue Revenue in the smallest currency unit (e.g., USD).
 * @property status Current release status (e.g., "Released").
 *
 * Exceptions: none. Construction is pure and non-throwing.
 */
data class MovieDetails(
    val id: Int,
    val title: String,
    val description: String,
    val posterPath: String?,
    val backdropPath: String?,
    val rating: Double,
    val voteCount: Int,
    val releaseDate: String,
    val runtime: Int,
    val genres: List<Genre> = emptyList(),
    val productionCompanies: List<ProductionCompany> = emptyList(),
    val budget: Long = 0,
    val revenue: Long = 0,
    val status: String
)

/**
 * Describes a content genre.
 *
 * @property id Numeric identifier of the genre.
 * @property name Display name of the genre.
 */
data class Genre(
    val id: Int,
    val name: String
)

/**
 * Represents a production company involved with a movie.
 *
 * @property id Numeric identifier of the company.
 * @property logoPath Optional relative path to the company's logo.
 * @property name Official display name of the company.
 * @property originCountry ISO 3166-1 alpha-2 country code of origin.
 */
data class ProductionCompany(
    val id: Int,
    val logoPath: String?,
    val name: String,
    val originCountry: String
)

/**
 * Color palette for a movie used to drive dynamic theming in the UI.
 *
 * Hex color strings are expected to be valid 6/8-digit hex values (e.g., "#AABBCC").
 *
 * @property accent Accent color derived from the poster/backdrop.
 * @property primary Primary color derived from the poster/backdrop.
 * @property secondary Secondary color derived from the poster/backdrop.
 * @property metadata Additional metadata describing the color extraction process.
 */
data class MovieColors(
    val accent: String,
    val primary: String,
    val secondary: String,
    val metadata: ColorMetadata
)

/**
 * Metadata describing how movie colors were derived.
 *
 * @property category Category assigned by the color model (e.g., mood/theme).
 * @property modelUsed Whether an ML model was used for color extraction.
 * @property rating Confidence or quality rating for the extracted palette.
 */
data class ColorMetadata(
    val category: String,
    val modelUsed: Boolean,
    val rating: Double
)

/**
 * Paginated response for lists of movies returned by remote sources.
 *
 * @property page Current page index starting from 1.
 * @property results List of [Movie] items for the current page.
 * @property totalPages Total number of pages available.
 * @property totalResults Total number of results across all pages.
 */
data class MovieListResponse(
    val page: Int,
    val results: List<Movie>,
    val totalPages: Int,
    val totalResults: Int
)

/**
 * Domain wrapper for a list of movies together with pagination and optional search query.
 *
 * @property movies List of domain [Movie] items.
 * @property pagination Pagination information for the result set.
 * @property searchQuery Optional original search query used to fetch results.
 */
data class MovieListData(
    val movies: List<Movie>,
    val pagination: Pagination,
    val searchQuery: String = ""
)

/**
 * Pagination information for list responses.
 *
 * @property page Current page index starting from 1.
 * @property totalPages Total number of pages available.
 * @property totalResults Total number of results across all pages.
 * @property hasNext True if a next page exists.
 * @property hasPrevious True if a previous page exists.
 */
data class Pagination(
    val page: Int,
    val totalPages: Int,
    val totalResults: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean
)

/**
 * Complete UI configuration used by the presentation layer. Values are typically
 * provided by the backend to enable backend-driven theming.
 *
 * @property colors Color palette configuration for surfaces and content.
 * @property texts Text strings for various UI elements.
 * @property buttons Button look-and-feel configuration.
 * @property searchInfo Optional search context/configuration.
 */
data class UiConfiguration(
    val colors: ColorScheme,
    val texts: TextConfiguration,
    val buttons: ButtonConfiguration,
    val searchInfo: SearchInfo? = null
)

/**
 * Color palette for the application surfaces and content.
 *
 * @property primary Primary brand color.
 * @property secondary Secondary brand color.
 * @property background Background surface color.
 * @property surface Foreground surface color.
 * @property onPrimary Content color to be used on [primary].
 * @property onSecondary Content color to be used on [secondary].
 * @property onBackground Content color to be used on [background].
 * @property onSurface Content color to be used on [surface].
 * @property moviePosterColors List of poster-derived colors for decorative accents.
 */
data class ColorScheme(
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val surface: Color,
    val onPrimary: Color,
    val onSecondary: Color,
    val onBackground: Color,
    val onSurface: Color,
    val moviePosterColors: List<Color>
)

/**
 * Text content configuration returned by the backend.
 *
 * @property appTitle Application title string.
 * @property loadingText User-visible text shown while loading.
 * @property errorMessage Generic error message text.
 * @property noMoviesFound Message shown when search/list is empty.
 * @property retryButton Label for retry action.
 * @property backButton Label for back action.
 * @property playButton Label for play action.
 */
data class TextConfiguration(
    val appTitle: String,
    val loadingText: String,
    val errorMessage: String,
    val noMoviesFound: String,
    val retryButton: String,
    val backButton: String,
    val playButton: String
)

/**
 * Button styling configuration.
 *
 * @property primaryButtonColor Background color for primary buttons.
 * @property secondaryButtonColor Background color for secondary buttons.
 * @property buttonTextColor Text color used on buttons.
 * @property buttonCornerRadius Corner radius in device-independent pixels.
 */
data class ButtonConfiguration(
    val primaryButtonColor: Color,
    val secondaryButtonColor: Color,
    val buttonTextColor: Color,
    val buttonCornerRadius: Int
)

/**
 * Search context information used for analytics and UI hints.
 *
 * @property query Original search query string.
 * @property resultCount Number of results returned for the query.
 * @property avgRating Average rating across the results.
 * @property ratingType Descriptor for how rating is computed (e.g., "mean").
 * @property colorBased True if UI used color-based logic for ranking/presentation.
 */
data class SearchInfo(
    val query: String,
    val resultCount: Int,
    val avgRating: Double,
    val ratingType: String,
    val colorBased: Boolean
)

/**
 * Response metadata attached to MCP results for observability and diagnostics.
 *
 * @property timestamp ISO-8601 timestamp when the response was produced.
 * @property method MCP method associated with the response.
 * @property searchQuery Optional search query that led to the response.
 * @property movieId Optional movie identifier context.
 * @property resultsCount Optional number of results in the payload.
 * @property aiGenerated Whether parts of the response are AI-generated.
 * @property geminiColors AI-suggested color palette associated with the response.
 * @property avgRating Optional average rating across a set of results.
 * @property movieRating Optional rating specific to a single movie.
 * @property version Backend-provided version string of the response schema/content.
 */
data class Meta(
    val timestamp: String,
    val method: String,
    val searchQuery: String? = null,
    val movieId: Int? = null,
    val resultsCount: Int? = null,
    val aiGenerated: Boolean,
    val geminiColors: GeminiColors,
    val avgRating: Double? = null,
    val movieRating: Double? = null,
    val version: String
)

/**
 * AI-generated color suggestions associated with a response or entity.
 *
 * @property primary Primary suggested color.
 * @property secondary Secondary suggested color.
 * @property accent Accent suggested color.
 */
data class GeminiColors(
    val primary: String,
    val secondary: String,
    val accent: String
)

/**
 * Type-safe result wrapper for data operations used by the presentation layer.
 *
 * Expresses Success, Error, and Loading states without throwing exceptions.
 *
 * - Success: carries [data] of type [T] and optional [uiConfig] for theming.
 * - Error: carries a human-readable [message] and optional [uiConfig].
 * - Loading: sentinel representing an in-progress operation.
 *
 * Exceptions: none. Error semantics are represented by the [Error] case.
 */
sealed class Result<out T> {
    /**
     * Successful operation result.
     *
     * @param T Type of the payload.
     * @property data Payload returned by the operation.
     * @property uiConfig Optional UI configuration to apply.
     */
    data class Success<T>(val data: T, val uiConfig: UiConfiguration? = null) : Result<T>()
    /**
     * Failed operation result.
     *
     * @property message Human-readable error description.
     * @property uiConfig Optional UI configuration to apply on error screens.
     */
    data class Error(val message: String, val uiConfig: UiConfiguration? = null) : Result<Nothing>()
    /** Loading state sentinel. */
    object Loading : Result<Nothing>()
}

// Response models for MCP client with UiConfig

/**
 * Complete response for movie details including UI configuration and metadata.
 *
 * This structure mirrors what repositories return to the presentation layer when
 * sourcing data from the MCP client.
 *
 * @property success Whether the request was successful.
 * @property data Payload containing [MovieDetailsData].
 * @property uiConfig Backend-provided UI configuration.
 * @property error Optional error description when [success] is false.
 * @property meta Metadata attached to the response for diagnostics.
 */
data class MovieDetailsResponse(
    val success: Boolean,
    val data: MovieDetailsData,
    val uiConfig: UiConfiguration,
    val error: String? = null,
    val meta: Meta
)


/**
 * Data payload for [MovieDetailsResponse].
 *
 * @property movieDetails The detailed movie information.
 * @property sentimentReviews Optional sentiment analysis reviews.
 * @property sentimentMetadata Optional sentiment analysis metadata.
 */
data class MovieDetailsData(
    val movieDetails: MovieDetails,
    val sentimentReviews: SentimentReviews? = null,
    val sentimentMetadata: SentimentMetadata? = null
)
