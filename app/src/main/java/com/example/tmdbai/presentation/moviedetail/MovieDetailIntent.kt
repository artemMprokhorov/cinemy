package com.example.tmdbai.presentation.moviedetail

import com.example.tmdbai.presentation.commons.CommonIntent

/**
 * Intent class for the Movie Detail screen
 * Defines all possible user interactions for the movie detail functionality
 */
sealed class MovieDetailIntent : CommonIntent {
    data class LoadMovieDetails(val movieId: Int) : MovieDetailIntent()
    object LoadRecommendations : MovieDetailIntent()
    object Retry : MovieDetailIntent()
    object Refresh : MovieDetailIntent()
    object BackPressed : MovieDetailIntent()
    
    // ML intents (only for displaying ready reviews):
    object ClearSentimentResult : MovieDetailIntent()
}