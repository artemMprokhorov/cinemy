package com.example.tmdbai.presentation.movieslist

import com.example.tmdbai.presentation.commons.CommonIntent

/**
 * Intent class for the Movies List screen
 * Defines all possible user interactions for the movies list functionality
 */
sealed class MoviesListIntent : CommonIntent {
    object LoadPopularMovies : MoviesListIntent()
    object LoadTopRatedMovies : MoviesListIntent()
    object LoadNowPlayingMovies : MoviesListIntent()
    data class SearchMovies(val query: String) : MoviesListIntent()
    object LoadNextPage : MoviesListIntent()
    object LoadPreviousPage : MoviesListIntent()
    data class MovieClicked(val movieId: Int) : MoviesListIntent()
    object ClearSearch : MoviesListIntent()
    object ToggleSearchMode : MoviesListIntent()
    object Retry : MoviesListIntent()
    object Refresh : MoviesListIntent()
    object BackPressed : MoviesListIntent()
}