package com.example.tmdbai.presentation.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbai.data.model.Result
import com.example.tmdbai.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MovieDetailState())
    val state: StateFlow<MovieDetailState> = _state.asStateFlow()

    private var currentMovieId: Int = 0

    fun processIntent(intent: MovieDetailIntent) {
        when (intent) {
            is MovieDetailIntent.LoadMovieDetails -> {
                currentMovieId = intent.movieId
                loadMovieDetails(intent.movieId)
            }

            is MovieDetailIntent.LoadRecommendations -> {
                // TODO: Implement recommendations loading
            }

            is MovieDetailIntent.Retry -> {
                // Retry loading the current movie details using stored movie ID
                if (currentMovieId > 0) {
                    loadMovieDetails(currentMovieId)
                }
            }

            is MovieDetailIntent.Refresh -> {
                // Refresh current movie details using stored movie ID
                if (currentMovieId > 0) {
                    loadMovieDetails(currentMovieId)
                }
            }

            is MovieDetailIntent.BackPressed -> {
                // This will be handled by the UI layer
            }
        }
    }

    private fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            val result = movieRepository.getMovieDetails(movieId)

            when (result) {
                is Result.Success -> {
                    val response = result.data
                    _state.value = _state.value.copy(
                        movieDetails = response.data.movieDetails,
                        isLoading = false,
                        error = null,
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
}