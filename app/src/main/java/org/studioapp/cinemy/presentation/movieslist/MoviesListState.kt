package org.studioapp.cinemy.presentation.movieslist

import org.studioapp.cinemy.data.model.Movie
import org.studioapp.cinemy.data.model.Pagination
import org.studioapp.cinemy.data.model.UiConfiguration
import org.studioapp.cinemy.presentation.PresentationConstants.DEFAULT_BOOLEAN_FALSE
import org.studioapp.cinemy.presentation.PresentationConstants.DEFAULT_PAGE_NUMBER
import org.studioapp.cinemy.presentation.PresentationConstants.DEFAULT_HAS_MORE
import org.studioapp.cinemy.presentation.PresentationConstants.DEFAULT_CAN_RETRY
import org.studioapp.cinemy.presentation.PresentationConstants.DEFAULT_RETRY_COUNT
import org.studioapp.cinemy.presentation.PresentationConstants.MESSAGE_USING_DEMO_DATA
import org.studioapp.cinemy.presentation.PresentationConstants.MESSAGE_BACKEND_UNAVAILABLE
import org.studioapp.cinemy.presentation.PresentationConstants.MESSAGE_CONNECTED_TO_LIVE_DATA
import org.studioapp.cinemy.presentation.PresentationConstants.MESSAGE_EMPTY

/**
 * State class for the Movies List screen
 * Holds all UI state data for the movies list functionality
 * Follows MVI pattern for state management
 */
data class MoviesListState(
    val isLoading: Boolean = DEFAULT_BOOLEAN_FALSE,
    val movies: List<Movie> = emptyList(),
    val error: String? = null,
    val pagination: Pagination? = null,
    val currentPage: Int = DEFAULT_PAGE_NUMBER,
    val hasMore: Boolean = DEFAULT_HAS_MORE,

    // NEW: Connection and data source status
    val isUsingMockData: Boolean = DEFAULT_BOOLEAN_FALSE,
    val connectionStatus: ConnectionStatus = ConnectionStatus.Unknown,

    // Enhanced error handling
    val canRetry: Boolean = DEFAULT_CAN_RETRY,
    val retryCount: Int = DEFAULT_RETRY_COUNT,

    val uiConfig: UiConfiguration? = null
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
                MESSAGE_USING_DEMO_DATA

            isUsingMockData && connectionStatus == ConnectionStatus.Disconnected ->
                MESSAGE_BACKEND_UNAVAILABLE

            connectionStatus == ConnectionStatus.Connected ->
                MESSAGE_CONNECTED_TO_LIVE_DATA

            else -> MESSAGE_EMPTY
        }
}
