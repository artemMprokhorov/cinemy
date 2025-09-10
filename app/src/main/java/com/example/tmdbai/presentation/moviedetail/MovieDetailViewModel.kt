package com.example.tmdbai.presentation.moviedetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbai.BuildConfig
import com.example.tmdbai.data.model.Result
import com.example.tmdbai.data.repository.MovieRepository
import com.example.tmdbai.presentation.PresentationConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MovieDetailState())
    val state: StateFlow<MovieDetailState> = _state.asStateFlow()

    private var currentMovieId: Int = PresentationConstants.DEFAULT_MOVIE_ID

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
                if (currentMovieId > PresentationConstants.DEFAULT_MOVIE_ID) {
                    loadMovieDetails(currentMovieId)
                }
            }

            is MovieDetailIntent.Refresh -> {
                // Refresh current movie details using stored movie ID
                if (currentMovieId > PresentationConstants.DEFAULT_MOVIE_ID) {
                    loadMovieDetails(currentMovieId)
                }
            }

            is MovieDetailIntent.BackPressed -> {
                // This will be handled by the UI layer
            }
            
            is MovieDetailIntent.ClearSentimentResult -> clearSentimentResult()
        }
    }

    private fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = PresentationConstants.DEFAULT_BOOLEAN_TRUE,
                error = null
            )

            val result = movieRepository.getMovieDetails(movieId)

            when (result) {
                is Result.Success -> {
                    val response = result.data
                    
                    // Debug logging for ViewModel uiConfig
                    if (BuildConfig.DEBUG) {
                        Log.d("VIEWMODEL", "MovieDetailViewModel uiConfig received - primary: ${result.uiConfig?.colors?.primary}, secondary: ${result.uiConfig?.colors?.secondary}")
                    }
                    
                    _state.value = _state.value.copy(
                        movieDetails = response.data.movieDetails,
                        isLoading = PresentationConstants.DEFAULT_BOOLEAN_FALSE,
                        error = null,
                        uiConfig = result.uiConfig,
                        meta = response.meta
                    )
                }

                is Result.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = PresentationConstants.DEFAULT_BOOLEAN_FALSE,
                        error = result.message,
                        uiConfig = result.uiConfig
                    )
                }

                is Result.Loading -> {
                    _state.value =
                        _state.value.copy(isLoading = PresentationConstants.DEFAULT_BOOLEAN_TRUE)
                }
            }
        }
    }
    
    private fun clearSentimentResult() {
        _state.value = _state.value.copy(
            sentimentResult = null,
            sentimentError = null
        )
    }
}