package org.studioapp.cinemy.presentation.movieslist

/**
 * Intent class for the Movies List screen
 * Defines all possible user interactions for the movies list functionality
 */
sealed class MoviesListIntent {
    object LoadPopularMovies : MoviesListIntent()
    object LoadMoreMovies : MoviesListIntent()

    data class NavigateToDetails(val movieId: Int) : MoviesListIntent()
    object RetryLastOperation : MoviesListIntent()

    // NEW: Enhanced error handling intents
    object RetryConnection : MoviesListIntent()
    object RefreshData : MoviesListIntent()
    object CheckConnectionStatus : MoviesListIntent()
    object DismissError : MoviesListIntent()

    // NEW: Pagination navigation intents
    object NextPage : MoviesListIntent()
    object PreviousPage : MoviesListIntent()
}