package org.studioapp.cinemy.presentation.moviedetail

/**
 * Intent class for the Movie Detail screen
 * Defines all possible user interactions for the movie detail functionality
 */
sealed class MovieDetailIntent {
    data class LoadMovieDetails(val movieId: Int) : MovieDetailIntent()
    object LoadRecommendations : MovieDetailIntent()
    object Retry : MovieDetailIntent()
    object Refresh : MovieDetailIntent()
    object BackPressed : MovieDetailIntent()

    // ML intents (only for displaying ready reviews):
    object ClearSentimentResult : MovieDetailIntent()
}