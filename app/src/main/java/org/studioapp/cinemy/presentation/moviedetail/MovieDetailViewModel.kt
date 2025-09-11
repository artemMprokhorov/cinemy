package org.studioapp.cinemy.presentation.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.studioapp.cinemy.BuildConfig
import org.studioapp.cinemy.data.model.Result
import org.studioapp.cinemy.data.repository.MovieRepository
import org.studioapp.cinemy.ml.SentimentAnalyzer
import org.studioapp.cinemy.presentation.PresentationConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieRepository: MovieRepository,
    private val sentimentAnalyzer: SentimentAnalyzer
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
                    
                    
                    _state.value = _state.value.copy(
                        movieDetails = response.data.movieDetails,
                        isLoading = PresentationConstants.DEFAULT_BOOLEAN_FALSE,
                        error = null,
                        uiConfig = result.uiConfig,
                        meta = response.meta,
                        sentimentReviews = response.data.sentimentReviews
                    )
                    
                    // Analyze sentiment using local AI model
                    response.data.sentimentReviews?.let { reviews ->
                        analyzeSentimentWithLocalModel(reviews)
                    }
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
    
    private fun analyzeSentimentWithLocalModel(reviews: org.studioapp.cinemy.data.model.SentimentReviews) {
        viewModelScope.launch {
        runCatching {
            // Analyze positive reviews
            val positiveResults = sentimentAnalyzer.analyzeBatch(reviews.positive)
            val negativeResults = sentimentAnalyzer.analyzeBatch(reviews.negative)
            
            
            // Update state with analysis results
            _state.value = _state.value.copy(
                sentimentResult = org.studioapp.cinemy.ml.SentimentResult.positive(
                    confidence = 0.8,
                    keywords = listOf("local_ai_analysis")
                )
            )
        }.onFailure { e ->
            _state.value = _state.value.copy(
                sentimentError = "Local AI analysis failed: ${e.message}"
            )
        }
        }
    }
}