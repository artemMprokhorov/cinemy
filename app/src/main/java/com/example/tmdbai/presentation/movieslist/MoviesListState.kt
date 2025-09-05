package com.example.tmdbai.presentation.movieslist

import com.example.tmdbai.data.model.Meta
import com.example.tmdbai.data.model.Movie
import com.example.tmdbai.data.model.Pagination
import com.example.tmdbai.data.model.UiConfiguration
import com.example.tmdbai.presentation.PresentationConstants

/**
 * State class for the Movies List screen
 * Holds all UI state data for the movies list functionality
 */
data class MoviesListState(
    val isLoading: Boolean = PresentationConstants.DEFAULT_BOOLEAN_FALSE,
    val movies: List<Movie> = emptyList(),
    val error: String? = null,
    val pagination: Pagination? = null,
    val currentPage: Int = PresentationConstants.DEFAULT_PAGE_NUMBER,
    val hasMore: Boolean = PresentationConstants.DEFAULT_HAS_MORE,

    // NEW: Connection and data source status
    val isUsingMockData: Boolean = PresentationConstants.DEFAULT_BOOLEAN_FALSE,
    val connectionStatus: ConnectionStatus = ConnectionStatus.Unknown,
    val lastSyncTime: Long? = null,

    // Enhanced error handling
    val canRetry: Boolean = PresentationConstants.DEFAULT_CAN_RETRY,
    val retryCount: Int = PresentationConstants.DEFAULT_RETRY_COUNT,

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
                PresentationConstants.MESSAGE_USING_DEMO_DATA

            isUsingMockData && connectionStatus == ConnectionStatus.Disconnected ->
                PresentationConstants.MESSAGE_BACKEND_UNAVAILABLE

            connectionStatus == ConnectionStatus.Connected ->
                PresentationConstants.MESSAGE_CONNECTED_TO_LIVE_DATA

            else -> PresentationConstants.MESSAGE_EMPTY
        }
}
