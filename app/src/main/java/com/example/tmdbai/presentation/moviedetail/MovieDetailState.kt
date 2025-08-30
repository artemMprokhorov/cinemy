package com.example.tmdbai.presentation.moviedetail

import com.example.tmdbai.data.model.MovieDetails
import com.example.tmdbai.data.model.UiConfiguration

/**
 * State class for the Movie Detail screen
 * Holds all UI state data for the movie detail functionality
 */
data class MovieDetailState(
    val movieDetails: MovieDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val uiConfig: UiConfiguration? = null
)
