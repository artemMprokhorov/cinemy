package com.example.tmdbai.data.remote.dto

import com.google.gson.annotations.SerializedName

// Movie Data DTOs
data class MovieDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("overview")
    val description: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("vote_average")
    val rating: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("genre_ids")
    val genreIds: List<Int> = emptyList(),
    @SerializedName("popularity")
    val popularity: Double = 0.0
)

data class MovieListResponseDto(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val movies: List<MovieDto>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)

data class MovieDetailsDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("overview")
    val description: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("vote_average")
    val rating: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("runtime")
    val runtime: Int?,
    @SerializedName("genres")
    val genres: List<GenreDto>,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompanyDto>,
    @SerializedName("budget")
    val budget: Long,
    @SerializedName("revenue")
    val revenue: Long,
    @SerializedName("status")
    val status: String
)

data class GenreDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)

data class ProductionCompanyDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("logo_path")
    val logoPath: String?
)

// UI Configuration DTOs
data class UiConfigurationDto(
    @SerializedName("colors")
    val colors: ColorSchemeDto,
    @SerializedName("texts")
    val texts: TextConfigurationDto,
    @SerializedName("buttons")
    val buttons: ButtonConfigurationDto
)

data class ColorSchemeDto(
    @SerializedName("primary")
    val primary: String,
    @SerializedName("secondary")
    val secondary: String,
    @SerializedName("background")
    val background: String,
    @SerializedName("surface")
    val surface: String,
    @SerializedName("on_primary")
    val onPrimary: String,
    @SerializedName("on_secondary")
    val onSecondary: String,
    @SerializedName("on_background")
    val onBackground: String,
    @SerializedName("on_surface")
    val onSurface: String,
    @SerializedName("movie_poster_colors")
    val moviePosterColors: List<String>
)

data class TextConfigurationDto(
    @SerializedName("app_title")
    val appTitle: String,
    @SerializedName("loading_text")
    val loadingText: String,
    @SerializedName("error_message")
    val errorMessage: String,
    @SerializedName("no_movies_found")
    val noMoviesFound: String,
    @SerializedName("retry_button")
    val retryButton: String,
    @SerializedName("back_button")
    val backButton: String,
    @SerializedName("play_button")
    val playButton: String
)

data class ButtonConfigurationDto(
    @SerializedName("primary_button_color")
    val primaryButtonColor: String,
    @SerializedName("secondary_button_color")
    val secondaryButtonColor: String,
    @SerializedName("button_text_color")
    val buttonTextColor: String,
    @SerializedName("button_corner_radius")
    val buttonCornerRadius: Int
)

// MCP Response DTOs
data class McpResponseDto<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: T?,
    @SerializedName("ui_config")
    val uiConfig: UiConfigurationDto?,
    @SerializedName("error")
    val error: String?,
    @SerializedName("message")
    val message: String?
)

data class McpMovieListResponseDto(
    @SerializedName("movies")
    val movies: List<MovieDto>,
    @SerializedName("pagination")
    val pagination: PaginationDto
)

data class PaginationDto(
    @SerializedName("page")
    val page: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("has_next")
    val hasNext: Boolean,
    @SerializedName("has_previous")
    val hasPrevious: Boolean
)
