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
    /**
     * Indicates whether the movies list is currently loading
     */
    val isLoading: Boolean = DEFAULT_BOOLEAN_FALSE,
    
    /**
     * List of movies to display in the UI
     */
    val movies: List<Movie> = emptyList(),
    
    /**
     * Error message to display if an error occurred
     */
    val error: String? = null,
    
    /**
     * Pagination information for the movies list
     */
    val pagination: Pagination? = null,
    
    /**
     * Current page number for pagination
     */
    val currentPage: Int = DEFAULT_PAGE_NUMBER,
    
    /**
     * Indicates whether there are more movies available to load
     */
    val hasMore: Boolean = DEFAULT_HAS_MORE,

    /**
     * Indicates whether the app is currently using mock data instead of real backend
     */
    val isUsingMockData: Boolean = DEFAULT_BOOLEAN_FALSE,
    
    /**
     * Current connection status to the backend
     */
    val connectionStatus: ConnectionStatus = ConnectionStatus.Unknown,

    /**
     * Indicates whether retry operation is available
     */
    val canRetry: Boolean = DEFAULT_CAN_RETRY,
    
    /**
     * Number of retry attempts made
     */
    val retryCount: Int = DEFAULT_RETRY_COUNT,

    /**
     * UI configuration received from the backend for dynamic theming
     */
    val uiConfig: UiConfiguration? = null
) {
    /**
     * Enum representing different connection states to the backend
     */
    enum class ConnectionStatus {
        /**
         * Connection status is unknown
         */
        Unknown,
        
        /**
         * Successfully connected to real backend
         */
        Connected,
        
        /**
         * Network or backend issues preventing connection
         */
        Disconnected,
        
        /**
         * Intentionally using mock data instead of real backend
         */
        MockOnly
    }

    /**
     * Computed property that returns a user-friendly status message based on connection state
     * 
     * @return Status message describing the current connection and data source state
     */
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
