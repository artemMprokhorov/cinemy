package org.studioapp.cinemy.presentation.moviedetail

import org.studioapp.cinemy.data.model.Meta
import org.studioapp.cinemy.data.model.MovieDetails
import org.studioapp.cinemy.data.model.SentimentReviews
import org.studioapp.cinemy.data.model.UiConfiguration
import org.studioapp.cinemy.ml.model.SentimentResult
import org.studioapp.cinemy.presentation.PresentationConstants.DEFAULT_BOOLEAN_FALSE
import org.studioapp.cinemy.presentation.PresentationConstants.MINUTES_PER_HOUR
import org.studioapp.cinemy.presentation.PresentationConstants.RUNTIME_HOURS_FORMAT
import org.studioapp.cinemy.presentation.PresentationConstants.RUNTIME_MINUTES_FORMAT
import org.studioapp.cinemy.presentation.PresentationConstants.MESSAGE_EMPTY
import org.studioapp.cinemy.presentation.PresentationConstants.BUDGET_THRESHOLD
import org.studioapp.cinemy.presentation.PresentationConstants.BUDGET_CURRENCY_SYMBOL
import org.studioapp.cinemy.presentation.PresentationConstants.BUDGET_DIVISOR
import org.studioapp.cinemy.presentation.PresentationConstants.BUDGET_SUFFIX

/**
 * State class for the Movie Detail screen
 * Holds all UI state data for the movie detail functionality
 * Follows MVI pattern for state management
 */
data class MovieDetailState(
    val isLoading: Boolean = DEFAULT_BOOLEAN_FALSE,
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
            "${it / MINUTES_PER_HOUR}${RUNTIME_HOURS_FORMAT} ${it % MINUTES_PER_HOUR}${RUNTIME_MINUTES_FORMAT}"
        } ?: MESSAGE_EMPTY

    val formattedBudget: String
        get() = movieDetails?.budget?.let {
            if (it > BUDGET_THRESHOLD) "${BUDGET_CURRENCY_SYMBOL}${it / BUDGET_DIVISOR}${BUDGET_SUFFIX}" else MESSAGE_EMPTY
        } ?: MESSAGE_EMPTY
}
