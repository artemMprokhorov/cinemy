package com.example.tmdbai.presentation.movieslist

import com.example.tmdbai.data.model.Movie
import com.example.tmdbai.data.model.UiConfiguration
import com.example.tmdbai.data.model.Pagination
import com.example.tmdbai.data.model.SearchInfo
import com.example.tmdbai.data.model.Meta

/**
 * State class for the Movies List screen
 * Holds all UI state data for the movies list functionality
 */
data class MoviesListState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String? = null,
    val pagination: Pagination? = null,
    val currentPage: Int = 1,
    val hasMore: Boolean = true,
    
    // Search functionality
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val searchMetadata: SearchInfo? = null,
    val screenMode: ScreenMode = ScreenMode.POPULAR,
    
    val uiConfig: UiConfiguration? = null,
    val meta: Meta? = null
) {
    enum class ScreenMode {
        POPULAR, SEARCH
    }
}
