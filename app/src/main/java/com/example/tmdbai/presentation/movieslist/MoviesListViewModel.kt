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

    init {
        // Load popular movies by default
        processIntent(MoviesListIntent.LoadPopularMovies)
    }

    fun processIntent(intent: MoviesListIntent) {
        when (intent) {
            is MoviesListIntent.LoadPopularMovies -> loadPopularMovies()
            is MoviesListIntent.SearchMovies -> searchMovies(intent.query)
            is MoviesListIntent.ClearSearch -> clearSearch()
            is MoviesListIntent.LoadMoreMovies -> loadMoreMovies()
            is MoviesListIntent.NavigateToDetails -> {
                // This will be handled by the UI layer for navigation
            }
            is MoviesListIntent.RetryLastOperation -> retryLastOperation()
        }
    }

    private fun loadPopularMovies(page: Int = 1) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null,
                screenMode = MoviesListState.ScreenMode.POPULAR
            )

            when (val result = movieRepository.getPopularMovies(page)) {
                is Result.Success -> {
                    val response = result.data
                    _state.value = _state.value.copy(
                        isLoading = false,
                        movies = response.data.movies,
                        pagination = response.data.pagination,
                        currentPage = page,
                        hasMore = response.data.pagination.hasNext,
                        uiConfig = response.uiConfig,
                        meta = response.meta
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

    private fun searchMovies(query: String, page: Int = 1) {
        if (query.isBlank()) return
        
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isSearching = true,
                searchQuery = query,
                screenMode = MoviesListState.ScreenMode.SEARCH
            )

            when (val result = movieRepository.searchMovies(query, page)) {
                is Result.Success -> {
                    val response = result.data
                    _state.value = _state.value.copy(
                        isSearching = false,
                        movies = response.data.movies,
                        pagination = response.data.pagination,
                        currentPage = page,
                        hasMore = response.data.pagination.hasNext,
                        searchMetadata = response.uiConfig.searchInfo,
                        uiConfig = response.uiConfig,
                        meta = response.meta
                    )
                }
                is Result.Error -> {
                    _state.value = _state.value.copy(
                        isSearching = false,
                        error = result.message,
                        uiConfig = result.uiConfig
                    )
                }
                is Result.Loading -> {
                    _state.value = _state.value.copy(isSearching = true)
                }
            }
        }
    }

    private fun clearSearch() {
        _state.value = _state.value.copy(
            searchQuery = "",
            isSearching = false,
            searchMetadata = null,
            screenMode = MoviesListState.ScreenMode.POPULAR,
            currentPage = 1
        )
        loadPopularMovies()
    }

    private fun loadMoreMovies() {
        val currentState = _state.value
        if (currentState.hasMore && !currentState.isLoading) {
            val nextPage = currentState.currentPage + 1
            when (currentState.screenMode) {
                MoviesListState.ScreenMode.POPULAR -> loadPopularMovies(nextPage)
                MoviesListState.ScreenMode.SEARCH -> searchMovies(currentState.searchQuery, nextPage)
            }
        }
    }

    private fun retryLastOperation() {
        val currentState = _state.value
        when (currentState.screenMode) {
            MoviesListState.ScreenMode.POPULAR -> loadPopularMovies(currentState.currentPage)
            MoviesListState.ScreenMode.SEARCH -> searchMovies(currentState.searchQuery, currentState.currentPage)
        }
    }
}