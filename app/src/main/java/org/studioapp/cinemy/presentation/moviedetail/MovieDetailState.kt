package org.studioapp.cinemy.presentation.moviedetail

import org.studioapp.cinemy.data.model.Meta
import org.studioapp.cinemy.data.model.MovieDetails
import org.studioapp.cinemy.data.model.SentimentReviews
import org.studioapp.cinemy.data.model.UiConfiguration
import org.studioapp.cinemy.ml.SentimentResult
import org.studioapp.cinemy.presentation.PresentationConstants

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


    // ML fields (only for displaying ready reviews):
    val sentimentResult: SentimentResult? = null,
    val sentimentError: String? = null,
    val sentimentReviews: SentimentReviews? = null
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
