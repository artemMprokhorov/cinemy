package com.example.tmdbai.data.model

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
    val popularity: Double = 0.0
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
    val runtime: Int?,
    val genres: List<Genre>,
    val productionCompanies: List<ProductionCompany>,
    val budget: Long,
    val revenue: Long,
    val status: String
)

data class Genre(
    val id: Int,
    val name: String
)

data class ProductionCompany(
    val id: Int,
    val name: String,
    val logoPath: String?
)

data class MovieListResponse(
    val movies: List<Movie>,
    val pagination: Pagination
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
    val buttons: ButtonConfiguration
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
    val movieDetails: MovieDetails,
    val uiConfig: UiConfiguration? = null
)
