package com.example.tmdbai.presentation.movieslist

import com.example.tmdbai.data.model.Movie
import com.example.tmdbai.data.model.UiConfiguration

/**
 * State class for the Movies List screen
 * Holds all UI state data for the movies list functionality
 */
data class MoviesListState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val totalPages: Int = 1,
    val searchQuery: String = "",
    val isSearchMode: Boolean = false,
    val uiConfig: UiConfiguration? = null,
    val hasNextPage: Boolean = false,
    val hasPreviousPage: Boolean = false
)
