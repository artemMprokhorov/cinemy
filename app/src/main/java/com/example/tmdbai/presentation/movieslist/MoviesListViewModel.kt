package com.example.tmdbai.presentation.movieslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbai.BuildConfig
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
            is MoviesListIntent.LoadMoreMovies -> loadMoreMovies()
            is MoviesListIntent.NavigateToDetails -> {
                // This will be handled by the UI layer for navigation
            }
            is MoviesListIntent.RetryLastOperation -> retryLastOperation()
            
            // NEW: Connection handling cases
            is MoviesListIntent.RetryConnection,
            is MoviesListIntent.RefreshData,
            is MoviesListIntent.CheckConnectionStatus,
            is MoviesListIntent.DismissError -> processConnectionIntent(intent)
        }
    }

    private fun loadPopularMovies(page: Int = 1, isRetry: Boolean = false) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = if (isRetry) null else _state.value.error,
                canRetry = false
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
                        
                        // NEW: Connection status handling
                        isUsingMockData = false, // Will be determined by BuildConfig
                        connectionStatus = determineConnectionStatus(null),
                        lastSyncTime = System.currentTimeMillis(),
                        retryCount = 0,
                        canRetry = false,
                        
                        uiConfig = response.uiConfig,
                        meta = response.meta
                    )
                }
                is Result.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message,
                        canRetry = true,
                        retryCount = if (isRetry) _state.value.retryCount + 1 else 0,
                        connectionStatus = MoviesListState.ConnectionStatus.Disconnected,
                        uiConfig = result.uiConfig
                    )
                }
                is Result.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }


    private fun loadMoreMovies() {
        val currentState = _state.value
        if (currentState.hasMore && !currentState.isLoading) {
            val nextPage = currentState.currentPage + 1
            loadPopularMovies(nextPage)
        }
    }

    private fun retryLastOperation() {
        val currentState = _state.value
        loadPopularMovies(currentState.currentPage)
    }
    
    // NEW: Helper method to determine connection status
    private fun determineConnectionStatus(message: String?): MoviesListState.ConnectionStatus {
        return when {
            BuildConfig.USE_MOCK_DATA -> MoviesListState.ConnectionStatus.MockOnly
            message?.contains("backend unavailable") == true -> MoviesListState.ConnectionStatus.Disconnected
            message?.contains("mock") == true -> MoviesListState.ConnectionStatus.Disconnected
            else -> MoviesListState.ConnectionStatus.Connected
        }
    }

    // NEW: Process retry and connection intents
    private fun processConnectionIntent(intent: MoviesListIntent) {
        when (intent) {
            is MoviesListIntent.RetryConnection -> {
                loadPopularMovies(page = 1, isRetry = true)
            }
            is MoviesListIntent.RefreshData -> {
                _state.value = _state.value.copy(connectionStatus = MoviesListState.ConnectionStatus.Unknown)
                loadPopularMovies(page = 1)
            }
            is MoviesListIntent.CheckConnectionStatus -> {
                checkConnectionStatus()
            }
            is MoviesListIntent.DismissError -> {
                _state.value = _state.value.copy(error = null, canRetry = false)
            }
            else -> {
                // Handle other intents that shouldn't be processed here
            }
        }
    }

    // NEW: Check connection status without loading data
    private fun checkConnectionStatus() {
        viewModelScope.launch {
            // Quick ping to check if backend is available
            try {
                val result = movieRepository.getPopularMovies(1)
                _state.value = _state.value.copy(
                    connectionStatus = determineConnectionStatus(
                        when (result) {
                            is Result.Success -> null
                            is Result.Error -> result.message
                            else -> null
                        }
                    )
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    connectionStatus = MoviesListState.ConnectionStatus.Disconnected
                )
            }
        }
    }
}