package org.studioapp.cinemy.data.model

import androidx.compose.ui.graphics.Color

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

data class Genre(
    val id: Int,
    val name: String
)

data class ProductionCompany(
    val id: Int,
    val logoPath: String?,
    val name: String,
    val originCountry: String
)

data class MovieColors(
    val accent: String,
    val primary: String,
    val secondary: String,
    val metadata: ColorMetadata
)

data class ColorMetadata(
    val category: String,
    val modelUsed: Boolean,
    val rating: Double
)

data class MovieListResponse(
    val page: Int,
    val results: List<Movie>,
    val totalPages: Int,
    val totalResults: Int
)

data class MovieListData(
    val movies: List<Movie>,
    val pagination: Pagination,
    val searchQuery: String = ""
)

data class Pagination(
    val page: Int,
    val totalPages: Int,
    val totalResults: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean
)

data class UiConfiguration(
    val colors: ColorScheme,
    val texts: TextConfiguration,
    val buttons: ButtonConfiguration,
    val searchInfo: SearchInfo? = null
)

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

data class TextConfiguration(
    val appTitle: String,
    val loadingText: String,
    val errorMessage: String,
    val noMoviesFound: String,
    val retryButton: String,
    val backButton: String,
    val playButton: String
)

data class ButtonConfiguration(
    val primaryButtonColor: Color,
    val secondaryButtonColor: Color,
    val buttonTextColor: Color,
    val buttonCornerRadius: Int
)

data class SearchInfo(
    val query: String,
    val resultCount: Int,
    val avgRating: Double,
    val ratingType: String,
    val colorBased: Boolean
)

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

data class GeminiColors(
    val primary: String,
    val secondary: String,
    val accent: String
)

sealed class Result<out T> {
    data class Success<T>(val data: T, val uiConfig: UiConfiguration? = null) : Result<T>()
    data class Error(val message: String, val uiConfig: UiConfiguration? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

// Response models for MCP client with UiConfig
data class MoviesResponse(
    val movies: List<Movie>,
    val pagination: Pagination,
    val uiConfig: UiConfiguration? = null
)

data class MovieDetailsResponse(
    val success: Boolean,
    val data: MovieDetailsData,
    val uiConfig: UiConfiguration,
    val error: String? = null,
    val meta: Meta
)


data class MovieDetailsData(
    val movieDetails: MovieDetails,
    val sentimentReviews: SentimentReviews? = null,
    val sentimentMetadata: SentimentMetadata? = null
)
