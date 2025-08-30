package com.example.tmdbai.presentation.movieslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbai.data.model.MovieListResponse
import com.example.tmdbai.data.model.Result
import com.example.tmdbai.data.repository.MovieRepository
import com.example.tmdbai.presentation.movieslist.MoviesListIntent
import com.example.tmdbai.presentation.movieslist.MoviesListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MoviesListViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MoviesListState())
    val state: StateFlow<MoviesListState> = _state.asStateFlow()

    private var currentMovieType: MovieType = MovieType.POPULAR

    enum class MovieType {
        POPULAR, TOP_RATED, NOW_PLAYING, SEARCH
    }

    init {
        // Load popular movies by default
        processIntent(MoviesListIntent.LoadPopularMovies)
    }

    fun processIntent(intent: MoviesListIntent) {
        when (intent) {
            is MoviesListIntent.LoadPopularMovies -> {
                currentMovieType = MovieType.POPULAR
                loadMovies()
            }
            is MoviesListIntent.LoadTopRatedMovies -> {
                currentMovieType = MovieType.TOP_RATED
                loadMovies()
            }
            is MoviesListIntent.LoadNowPlayingMovies -> {
                currentMovieType = MovieType.NOW_PLAYING
                loadMovies()
            }
            is MoviesListIntent.SearchMovies -> {
                currentMovieType = MovieType.SEARCH
                _state.value = _state.value.copy(
                    searchQuery = intent.query,
                    currentPage = 1
                )
                loadMovies()
            }
            is MoviesListIntent.LoadNextPage -> {
                if (_state.value.hasNextPage) {
                    _state.value = _state.value.copy(currentPage = _state.value.currentPage + 1)
                    loadMovies()
                }
            }
            is MoviesListIntent.LoadPreviousPage -> {
                if (_state.value.hasPreviousPage) {
                    _state.value = _state.value.copy(currentPage = _state.value.currentPage - 1)
                    loadMovies()
                }
            }
            is MoviesListIntent.MovieClicked -> {
                // This will be handled by the UI layer for navigation
            }
            is MoviesListIntent.ClearSearch -> {
                _state.value = _state.value.copy(
                    searchQuery = "",
                    isSearchMode = false,
                    currentPage = 1
                )
                currentMovieType = MovieType.POPULAR
                loadMovies()
            }
            is MoviesListIntent.ToggleSearchMode -> {
                _state.value = _state.value.copy(
                    isSearchMode = !_state.value.isSearchMode
                )
            }
            is MoviesListIntent.Retry -> {
                loadMovies()
            }
            is MoviesListIntent.Refresh -> {
                _state.value = _state.value.copy(currentPage = 1)
                loadMovies()
            }
            is MoviesListIntent.BackPressed -> {
                // This will be handled by the UI layer
            }
        }
    }

    private fun loadMovies() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            val result = when (currentMovieType) {
                MovieType.POPULAR -> movieRepository.getPopularMovies(_state.value.currentPage)
                MovieType.TOP_RATED -> movieRepository.getTopRatedMovies(_state.value.currentPage)
                MovieType.NOW_PLAYING -> movieRepository.getNowPlayingMovies(_state.value.currentPage)
                MovieType.SEARCH -> movieRepository.searchMovies(_state.value.searchQuery, _state.value.currentPage)
            }

            when (result) {
                is Result.Success -> {
                    val movieListResponse = result.data as MovieListResponse
                    _state.value = _state.value.copy(
                        movies = movieListResponse.movies,
                        isLoading = false,
                        error = null,
                        uiConfig = result.uiConfig,
                        hasNextPage = movieListResponse.pagination.hasNext,
                        hasPreviousPage = movieListResponse.pagination.hasPrevious
                    )
                }
                is Result.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message,
                        uiConfig = result.uiConfig
                    )
                }
                is Result.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }
}