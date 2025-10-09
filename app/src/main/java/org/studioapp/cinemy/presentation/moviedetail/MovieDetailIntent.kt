package org.studioapp.cinemy.presentation.moviedetail

/**
 * Intent class for the Movie Detail screen
 * Defines all possible user interactions for the movie detail functionality
 * Follows MVI pattern for unidirectional data flow
 */
sealed class MovieDetailIntent {
    data class LoadMovieDetails(val movieId: Int) : MovieDetailIntent()
    object Retry : MovieDetailIntent()
    object Refresh : MovieDetailIntent()

    // ML intents (only for displaying ready reviews):
    object ClearSentimentResult : MovieDetailIntent()
}