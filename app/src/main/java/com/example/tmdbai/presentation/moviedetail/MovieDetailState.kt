package com.example.tmdbai.presentation.moviedetail

import com.example.tmdbai.data.model.Meta
import com.example.tmdbai.data.model.MovieDetails
import com.example.tmdbai.data.model.UiConfiguration
import com.example.tmdbai.ml.SentimentResult
import com.example.tmdbai.presentation.PresentationConstants

/**
 * State class for the Movie Detail screen
 * Holds all UI state data for the movie detail functionality
 */
data class MovieDetailState(
    val isLoading: Boolean = PresentationConstants.DEFAULT_BOOLEAN_FALSE,
    val movieDetails: MovieDetails? = null,
    val error: String? = null,
    val uiConfig: UiConfiguration? = null,
    val meta: Meta? = null,
    val movieId: Int? = null,

    // UI helpers
    val showFullDescription: Boolean = PresentationConstants.DEFAULT_SHOW_FULL_DESCRIPTION,
    val showProductionDetails: Boolean = PresentationConstants.DEFAULT_SHOW_PRODUCTION_DETAILS,
    
    // ML поля:
    val sentimentResult: SentimentResult? = null,
    val isSentimentAnalyzing: Boolean = false,
    val sentimentError: String? = null
) {
    val formattedRuntime: String
        get() = movieDetails?.runtime?.let {
            "${it / PresentationConstants.MINUTES_PER_HOUR}${PresentationConstants.RUNTIME_HOURS_FORMAT} ${it % PresentationConstants.MINUTES_PER_HOUR}${PresentationConstants.RUNTIME_MINUTES_FORMAT}"
        } ?: PresentationConstants.MESSAGE_EMPTY

    val formattedBudget: String
        get() = movieDetails?.budget?.let {
            if (it > PresentationConstants.BUDGET_THRESHOLD) "${PresentationConstants.BUDGET_CURRENCY_SYMBOL}${it / PresentationConstants.BUDGET_DIVISOR}${PresentationConstants.BUDGET_SUFFIX}" else PresentationConstants.MESSAGE_EMPTY
        } ?: PresentationConstants.MESSAGE_EMPTY
}
