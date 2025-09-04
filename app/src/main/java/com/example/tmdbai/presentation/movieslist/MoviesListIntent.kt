package com.example.tmdbai.presentation.movieslist

import com.example.tmdbai.presentation.commons.CommonIntent

/**
 * Intent class for the Movies List screen
 * Defines all possible user interactions for the movies list functionality
 */
sealed class MoviesListIntent : CommonIntent {
    object LoadPopularMovies : MoviesListIntent()
    object LoadMoreMovies : MoviesListIntent()
    
    // Search intents
    data class SearchMovies(val query: String) : MoviesListIntent()
    object ClearSearch : MoviesListIntent()
    
    data class NavigateToDetails(val movieId: Int) : MoviesListIntent()
    object RetryLastOperation : MoviesListIntent()
    
    // NEW: Enhanced error handling intents
    object RetryConnection : MoviesListIntent()
    object RefreshData : MoviesListIntent()
    object CheckConnectionStatus : MoviesListIntent()
    object DismissError : MoviesListIntent()
}