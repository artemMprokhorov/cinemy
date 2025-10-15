package org.studioapp.cinemy.presentation.movieslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.studioapp.cinemy.BuildConfig
import org.studioapp.cinemy.data.model.Result
import org.studioapp.cinemy.data.repository.MovieRepository
import org.studioapp.cinemy.presentation.PresentationConstants.DEFAULT_PAGE_NUMBER
import org.studioapp.cinemy.presentation.PresentationConstants.DEFAULT_BOOLEAN_FALSE
import org.studioapp.cinemy.presentation.PresentationConstants.DEFAULT_BOOLEAN_TRUE
import org.studioapp.cinemy.presentation.PresentationConstants.DEFAULT_CAN_RETRY
import org.studioapp.cinemy.presentation.PresentationConstants.DEFAULT_RETRY_COUNT
import org.studioapp.cinemy.presentation.PresentationConstants.PAGE_INCREMENT
import org.studioapp.cinemy.presentation.PresentationConstants.PAGE_DECREMENT
import org.studioapp.cinemy.presentation.PresentationConstants.BACKEND_UNAVAILABLE_KEYWORD
import org.studioapp.cinemy.presentation.PresentationConstants.MOCK_KEYWORD

/**
 * ViewModel for Movies List screen following MVI pattern
 * Manages state and handles user intents for movies list functionality
 * @param movieRepository Repository for movie data operations
 */
class MoviesListViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MoviesListState())
    val state: StateFlow<MoviesListState> = _state.asStateFlow()

    init {
        // Load popular movies by default
        processIntent(MoviesListIntent.LoadPopularMovies)
    }

    /**
     * Processes user intents and updates state accordingly
     * @param intent User intent to process
     */
    fun processIntent(intent: MoviesListIntent) {
        when (intent) {
            is MoviesListIntent.LoadPopularMovies -> loadPopularMovies()
            is MoviesListIntent.LoadMoreMovies -> loadMoreMovies()
            is MoviesListIntent.RetryLastOperation -> retryLastOperation()

            // NEW: Connection handling cases
            is MoviesListIntent.RetryConnection,
            is MoviesListIntent.RefreshData,
            is MoviesListIntent.CheckConnectionStatus,
            is MoviesListIntent.DismissError -> processConnectionIntent(intent)

            // NEW: Pagination navigation cases
            is MoviesListIntent.NextPage -> navigateToNextPage()
            is MoviesListIntent.PreviousPage -> navigateToPreviousPage()
        }
    }

    private fun loadPopularMovies(
        page: Int = DEFAULT_PAGE_NUMBER,
        isRetry: Boolean = DEFAULT_BOOLEAN_FALSE
    ) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = DEFAULT_BOOLEAN_TRUE,
                error = if (isRetry) null else _state.value.error,
                canRetry = DEFAULT_CAN_RETRY
            )

            when (val result = movieRepository.getPopularMovies(page)) {
                is Result.Success -> {
                    val response = result.data

                    // Create pagination from the new response structure
                    val pagination = org.studioapp.cinemy.data.model.Pagination(
                        page = response.page,
                        totalPages = response.totalPages,
                        totalResults = response.totalResults,
                        hasNext = response.page < response.totalPages,
                        hasPrevious = response.page > 1
                    )

                    _state.value = _state.value.copy(
                        isLoading = DEFAULT_BOOLEAN_FALSE,
                        error = null, // Clear any previous error
                        movies = response.results,
                        pagination = pagination,
                        currentPage = page,
                        hasMore = pagination.hasNext,

                        // NEW: Connection status handling
                        isUsingMockData = DEFAULT_BOOLEAN_FALSE, // Will be determined by BuildConfig
                        connectionStatus = determineConnectionStatus(null),
                        retryCount = DEFAULT_RETRY_COUNT,
                        canRetry = DEFAULT_CAN_RETRY,

                        uiConfig = result.uiConfig
                    )
                }

                is Result.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = DEFAULT_BOOLEAN_FALSE,
                        error = result.message,
                        canRetry = DEFAULT_BOOLEAN_TRUE,
                        retryCount = if (isRetry) _state.value.retryCount + PAGE_INCREMENT else DEFAULT_RETRY_COUNT,
                        connectionStatus = MoviesListState.ConnectionStatus.Disconnected,
                        uiConfig = result.uiConfig
                    )
                }

                is Result.Loading -> {
                    _state.value =
                        _state.value.copy(isLoading = DEFAULT_BOOLEAN_TRUE)
                }
            }
        }
    }


    private fun loadMoreMovies() {
        val currentState = _state.value
        if (currentState.hasMore && !currentState.isLoading) {
            val nextPage = currentState.currentPage + PAGE_INCREMENT
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
            message?.contains(BACKEND_UNAVAILABLE_KEYWORD) == true -> MoviesListState.ConnectionStatus.Disconnected
            message?.contains(MOCK_KEYWORD) == true -> MoviesListState.ConnectionStatus.Disconnected
            else -> MoviesListState.ConnectionStatus.Connected
        }
    }

    // NEW: Process retry and connection intents
    private fun processConnectionIntent(intent: MoviesListIntent) {
        when (intent) {
            is MoviesListIntent.RetryConnection -> {
                loadPopularMovies(
                    page = DEFAULT_PAGE_NUMBER,
                    isRetry = DEFAULT_BOOLEAN_TRUE
                )
            }

            is MoviesListIntent.RefreshData -> {
                _state.value =
                    _state.value.copy(connectionStatus = MoviesListState.ConnectionStatus.Unknown)
                loadPopularMovies(page = DEFAULT_PAGE_NUMBER)
            }

            is MoviesListIntent.CheckConnectionStatus -> {
                checkConnectionStatus()
            }

            is MoviesListIntent.DismissError -> {
                _state.value = _state.value.copy(
                    error = null,
                    canRetry = DEFAULT_CAN_RETRY
                )
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
            runCatching {
                movieRepository.getPopularMovies(DEFAULT_PAGE_NUMBER)
            }.fold(
                onSuccess = { result ->
                    _state.value = _state.value.copy(
                        connectionStatus = determineConnectionStatus(
                            when (result) {
                                is Result.Success -> null
                                is Result.Error -> result.message
                                else -> null
                            }
                        )
                    )
                },
                onFailure = { _ ->
                    _state.value = _state.value.copy(
                        connectionStatus = MoviesListState.ConnectionStatus.Disconnected
                    )
                }
            )
        }
    }

    private fun navigateToNextPage() {
        val currentState = _state.value
        val pagination = currentState.pagination

        if (pagination != null && pagination.hasNext && !currentState.isLoading) {
            loadPopularMovies(pagination.page + PAGE_INCREMENT)
        }
    }

    private fun navigateToPreviousPage() {
        val currentState = _state.value
        val pagination = currentState.pagination

        if (pagination != null && pagination.hasPrevious && !currentState.isLoading) {
            loadPopularMovies(pagination.page - PAGE_DECREMENT)
        }
    }
}