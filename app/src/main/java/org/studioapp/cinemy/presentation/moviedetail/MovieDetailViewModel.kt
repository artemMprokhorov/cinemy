package org.studioapp.cinemy.presentation.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.studioapp.cinemy.data.model.Result
import org.studioapp.cinemy.data.repository.MovieRepository
import org.studioapp.cinemy.ml.SentimentAnalyzer
import org.studioapp.cinemy.presentation.PresentationConstants

/**
 * ViewModel for Movie Detail screen following MVI pattern
 * Manages state and handles user intents for movie detail functionality
 * @param movieRepository Repository for movie data operations
 * @param sentimentAnalyzer ML analyzer for sentiment analysis
 */
class MovieDetailViewModel(
    private val movieRepository: MovieRepository,
    private val sentimentAnalyzer: SentimentAnalyzer
) : ViewModel() {

    private val _state = MutableStateFlow(MovieDetailState())
    val state: StateFlow<MovieDetailState> = _state.asStateFlow()

    private var currentMovieId: Int = PresentationConstants.DEFAULT_MOVIE_ID

    /**
     * Processes user intents and updates state accordingly
     * @param intent User intent to process
     */
    fun processIntent(intent: MovieDetailIntent) {
        when (intent) {
            is MovieDetailIntent.LoadMovieDetails -> {
                currentMovieId = intent.movieId
                loadMovieDetails(intent.movieId)
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

                // Calculate overall sentiment from batch results
                val overallSentiment = calculateOverallSentiment(positiveResults, negativeResults)

                // Update state with analysis results
                _state.value = _state.value.copy(
                    sentimentResult = overallSentiment
                )
            }.onFailure { e ->
                _state.value = _state.value.copy(
                    sentimentError = "Local AI analysis failed: ${e.message}"
                )
            }
        }
    }

    private fun calculateOverallSentiment(
        positiveResults: List<org.studioapp.cinemy.ml.SentimentResult>,
        negativeResults: List<org.studioapp.cinemy.ml.SentimentResult>
    ): org.studioapp.cinemy.ml.SentimentResult {
        // Calculate average confidence for positive and negative results
        val positiveConfidence = if (positiveResults.isNotEmpty()) {
            positiveResults.filter { it.isSuccess }.map { it.confidence }.average()
        } else 0.0

        val negativeConfidence = if (negativeResults.isNotEmpty()) {
            negativeResults.filter { it.isSuccess }.map { it.confidence }.average()
        } else 0.0

        // Collect all found keywords
        val allKeywords = (positiveResults.flatMap { it.foundKeywords } +
                negativeResults.flatMap { it.foundKeywords }).distinct()

        // Determine overall sentiment based on confidence comparison
        return when {
            positiveConfidence > negativeConfidence && positiveConfidence > 0.5 -> {
                org.studioapp.cinemy.ml.SentimentResult.positive(
                    confidence = positiveConfidence,
                    keywords = allKeywords
                )
            }

            negativeConfidence > positiveConfidence && negativeConfidence > 0.5 -> {
                org.studioapp.cinemy.ml.SentimentResult.negative(
                    confidence = negativeConfidence,
                    keywords = allKeywords
                )
            }

            else -> {
                org.studioapp.cinemy.ml.SentimentResult.neutral(
                    confidence = maxOf(positiveConfidence, negativeConfidence),
                    keywords = allKeywords
                )
            }
        }
    }
}