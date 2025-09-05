package com.example.tmdbai.presentation.movieslist

import com.example.tmdbai.data.model.Meta
import com.example.tmdbai.data.model.Movie
import com.example.tmdbai.data.model.Pagination
import com.example.tmdbai.data.model.UiConfiguration

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

    // NEW: Connection and data source status
    val isUsingMockData: Boolean = false,
    val connectionStatus: ConnectionStatus = ConnectionStatus.Unknown,
    val lastSyncTime: Long? = null,

    // Enhanced error handling
    val canRetry: Boolean = false,
    val retryCount: Int = 0,

    val uiConfig: UiConfiguration? = null,
    val meta: Meta? = null
) {
    enum class ConnectionStatus {
        Unknown,
        Connected,      // Real backend working
        Disconnected,   // Network/backend issues
        MockOnly        // Intentionally using mock data
    }

    val statusMessage: String
        get() = when {
            isUsingMockData && connectionStatus == ConnectionStatus.MockOnly ->
                "Using demo data"

            isUsingMockData && connectionStatus == ConnectionStatus.Disconnected ->
                "Backend unavailable - showing demo data"

            connectionStatus == ConnectionStatus.Connected ->
                "Connected to live data"

            else -> ""
        }
}
