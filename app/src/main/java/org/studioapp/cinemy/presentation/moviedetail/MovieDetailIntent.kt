package org.studioapp.cinemy.presentation.moviedetail

/**
 * Intent class for the Movie Detail screen
 * Defines all possible user interactions for the movie detail functionality
 * Follows MVI pattern for unidirectional data flow
 */
sealed class MovieDetailIntent {
    /**
     * Intent to load movie details for a specific movie ID
     *
     * @param movieId The unique identifier of the movie to load details for
     */
    data class LoadMovieDetails(val movieId: Int) : MovieDetailIntent()
    
    /**
     * Intent to retry the last failed operation using the stored movie ID
     */
    object Retry : MovieDetailIntent()
    
    /**
     * Intent to refresh the current movie details
     */
    object Refresh : MovieDetailIntent()

    /**
     * Intent to clear the ML sentiment analysis result from the UI
     * Used for displaying ready reviews from backend
     */
    object ClearSentimentResult : MovieDetailIntent()
}