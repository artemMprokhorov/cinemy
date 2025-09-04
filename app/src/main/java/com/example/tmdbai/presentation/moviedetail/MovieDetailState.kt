package com.example.tmdbai.presentation.moviedetail

import com.example.tmdbai.data.model.MovieDetails
import com.example.tmdbai.data.model.UiConfiguration
import com.example.tmdbai.data.model.Meta

/**
 * State class for the Movie Detail screen
 * Holds all UI state data for the movie detail functionality
 */
data class MovieDetailState(
    val isLoading: Boolean = false,
    val movieDetails: MovieDetails? = null,
    val error: String? = null,
    val uiConfig: UiConfiguration? = null,
    val meta: Meta? = null,
    val movieId: Int? = null,
    
    // UI helpers
    val showFullDescription: Boolean = false,
    val showProductionDetails: Boolean = false
) {
    val formattedRuntime: String
        get() = movieDetails?.runtime?.let { 
            "${it / 60}h ${it % 60}m" 
        } ?: ""
        
    val formattedBudget: String
        get() = movieDetails?.budget?.let { 
            if (it > 0) "$${it / 1_000_000}M" else "" 
        } ?: ""
}
