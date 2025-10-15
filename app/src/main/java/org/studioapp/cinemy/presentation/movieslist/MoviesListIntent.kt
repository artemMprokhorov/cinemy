package org.studioapp.cinemy.presentation.movieslist

/**
 * Intent class for the Movies List screen
 * Defines all possible user interactions for the movies list functionality
 * Follows MVI pattern for unidirectional data flow
 */
sealed class MoviesListIntent {
    
    /**
     * Intent to load popular movies from the repository
     * Triggers initial data loading for the movies list screen
     */
    object LoadPopularMovies : MoviesListIntent()
    
    /**
     * Intent to load additional movies for pagination
     * Used when user scrolls to load more content
     */
    object LoadMoreMovies : MoviesListIntent()
    
    /**
     * Intent to retry the last failed operation
     * Provides error recovery mechanism for failed data loading
     */
    object RetryLastOperation : MoviesListIntent()

    /**
     * Intent to retry connection to the backend service
     * Used when connection issues are detected
     */
    object RetryConnection : MoviesListIntent()
    
    /**
     * Intent to refresh current data
     * Forces reload of the current movie list
     */
    object RefreshData : MoviesListIntent()
    
    /**
     * Intent to check current connection status
     * Verifies connectivity to backend services
     */
    object CheckConnectionStatus : MoviesListIntent()
    
    /**
     * Intent to dismiss current error message
     * Clears error state from the UI
     */
    object DismissError : MoviesListIntent()

    /**
     * Intent to navigate to the next page
     * Advances pagination to show next set of movies
     */
    object NextPage : MoviesListIntent()
    
    /**
     * Intent to navigate to the previous page
     * Goes back to previous set of movies in pagination
     */
    object PreviousPage : MoviesListIntent()
}