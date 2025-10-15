package org.studioapp.cinemy.ui.moviedetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import org.studioapp.cinemy.R
import org.studioapp.cinemy.data.model.MovieDetails
import org.studioapp.cinemy.data.model.UiConfiguration
import org.studioapp.cinemy.presentation.moviedetail.MovieDetailIntent
import org.studioapp.cinemy.presentation.moviedetail.MovieDetailState
import org.studioapp.cinemy.presentation.moviedetail.MovieDetailViewModel
import org.studioapp.cinemy.ui.components.ConfigurableText
import org.studioapp.cinemy.ui.components.PullToReloadArrow
import org.studioapp.cinemy.ui.components.SentimentAnalysisCard
import org.studioapp.cinemy.ui.constants.UiConstants.ERROR_LOADING_MOVIE_DETAILS_RETRY
import org.studioapp.cinemy.ui.constants.UiConstants.ERROR_SUBTITLE
import org.studioapp.cinemy.ui.constants.UiConstants.ERROR_TITLE
import org.studioapp.cinemy.ui.constants.UiConstants.LOADING_INDICATOR
import org.studioapp.cinemy.ui.constants.UiConstants.LOADING_MOVIE_DETAILS
import org.studioapp.cinemy.ui.constants.UiConstants.LOADING_TEXT
import org.studioapp.cinemy.ui.constants.UiConstants.RETRY_INSTRUCTION
import org.studioapp.cinemy.ui.theme.CinemyTheme
import org.studioapp.cinemy.ui.theme.Dimens100
import org.studioapp.cinemy.ui.theme.Dimens12
import org.studioapp.cinemy.ui.theme.Dimens16
import org.studioapp.cinemy.ui.theme.Dimens400
import org.studioapp.cinemy.ui.theme.Dimens8
import org.studioapp.cinemy.ui.theme.ImageConfig
import org.studioapp.cinemy.ui.theme.SplashBackground


/**
 * Main screen for displaying movie details with sentiment analysis and ML-powered review analysis.
 * Handles loading states, error states, and displays complete movie information with pull-to-refresh functionality.
 *
 * @param movieId Unique identifier of the movie to display
 * @param viewModel ViewModel for movie detail state management and intent processing
 * @param onBackClick Callback function triggered when back button is pressed
 */
@Composable
fun MovieDetailScreen(
    movieId: Int,
    viewModel: MovieDetailViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()


    LaunchedEffect(movieId) {
        viewModel.processIntent(MovieDetailIntent.LoadMovieDetails(movieId))
    }

    BackHandler {
        onBackClick()
    }

    MovieDetailContent(
        state = state,
        onIntent = viewModel::processIntent
    )
}

/**
 * Internal content composable that handles the main UI states (loading, error, success) with pull-to-refresh functionality.
 * Manages state transitions and provides appropriate UI feedback for each state.
 *
 * @param state Current movie detail state containing loading status, error information, and movie data
 * @param onIntent Intent handler for processing user actions and state updates
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MovieDetailContent(
    state: MovieDetailState,
    onIntent: (MovieDetailIntent) -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = {
            onIntent(MovieDetailIntent.Retry)
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashBackground)
            .systemBarsPadding()
            .pullRefresh(pullRefreshState)
    ) {
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ConfigurableText(
                            text = stringResource(R.string.loading_text),
                            style = typography.headlineMedium,
                            uiConfig = state.uiConfig,
                            color = Color.White,
                            contentDescription = stringResource(R.string.loading_movie_details_please_wait),
                            testTag = LOADING_TEXT
                        )
                        Spacer(modifier = Modifier.height(Dimens16))
                        CircularProgressIndicator(
                            color = state.uiConfig?.colors?.primary ?: Color.White,
                            modifier = Modifier
                                .semantics {
                                    contentDescription = LOADING_MOVIE_DETAILS
                                }
                                .testTag(LOADING_INDICATOR)
                        )
                    }
                }
            }

            state.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .pullRefresh(pullRefreshState)
                        .semantics {
                            contentDescription = ERROR_LOADING_MOVIE_DETAILS_RETRY
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(Dimens100))
                    ConfigurableText(
                        text = stringResource(R.string.error_generic),
                        style = typography.headlineLarge,
                        uiConfig = state.uiConfig,
                        color = Color.White,
                        contentDescription = stringResource(R.string.error_failed_load_movie_details),
                        testTag = ERROR_TITLE
                    )
                    ConfigurableText(
                        text = stringResource(R.string.movie_details),
                        style = typography.headlineLarge,
                        uiConfig = state.uiConfig,
                        color = Color.White,
                        contentDescription = stringResource(R.string.movie_details_screen),
                        testTag = ERROR_SUBTITLE
                    )
                    Spacer(modifier = Modifier.height(Dimens16))
                    PullToReloadArrow()
                    Spacer(modifier = Modifier.height(Dimens16))
                    ConfigurableText(
                        text = stringResource(R.string.pull_to_reload),
                        style = typography.headlineMedium,
                        uiConfig = state.uiConfig,
                        color = Color.White,
                        contentDescription = stringResource(R.string.pull_down_retry_movie_details),
                        testTag = RETRY_INSTRUCTION
                    )
                    Spacer(modifier = Modifier.height(Dimens100))
                }
            }

            state.movieDetails != null -> {
                MovieDetailsContent(
                    movieDetails = state.movieDetails,
                    uiConfig = state.uiConfig,
                    state = state
                )
            }
        }

        // No pull refresh indicator - only custom spinner on loading screen
    }
}

/**
 * Internal composable that renders the complete movie details content including poster, information card, and sentiment analysis.
 * Displays movie information with server-driven theming and includes ML-powered sentiment analysis results.
 *
 * @param movieDetails Complete movie data including title, description, rating, genres, and metadata
 * @param uiConfig Optional server-driven UI configuration for dynamic theming
 * @param state Current movie detail state containing sentiment analysis data and errors
 */
@Composable
private fun MovieDetailsContent(
    movieDetails: MovieDetails,
    uiConfig: UiConfiguration?,
    state: MovieDetailState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Movie poster
        AsyncImage(
            model = ImageConfig.buildPosterUrl(movieDetails.posterPath),
            contentDescription = movieDetails.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens400)
                .padding(horizontal = Dimens16)
                .clip(RoundedCornerShape(Dimens8))
        )

        Spacer(modifier = Modifier.height(Dimens16))

        // Movie details
        Card(
            modifier = Modifier.padding(Dimens16),
            colors = CardDefaults.cardColors(
                containerColor = if (uiConfig?.colors?.surface != null) {
                    uiConfig.colors.surface
                } else {
                    colorScheme.surface
                }
            )
        ) {
            Column(
                modifier = Modifier.padding(Dimens16),
                verticalArrangement = Arrangement.spacedBy(Dimens12)
            ) {
                // Title
                ConfigurableText(
                    text = movieDetails.title,
                    style = typography.headlineMedium,
                    uiConfig = uiConfig
                )

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ConfigurableText(
                        text = stringResource(R.string.rating_label, movieDetails.rating),
                        style = typography.bodyLarge,
                        uiConfig = uiConfig,
                        color = uiConfig?.colors?.primary
                    )
                    Spacer(modifier = Modifier.width(Dimens16))
                    ConfigurableText(
                        text = stringResource(R.string.votes_label, movieDetails.voteCount),
                        style = typography.bodyMedium,
                        uiConfig = uiConfig
                    )
                }

                // Release date
                ConfigurableText(
                    text = stringResource(R.string.release_date_label, movieDetails.releaseDate),
                    style = typography.bodyMedium,
                    uiConfig = uiConfig
                )

                // Description
                ConfigurableText(
                    text = movieDetails.description,
                    style = typography.bodyMedium,
                    uiConfig = uiConfig
                )

                // Genres
                if (movieDetails.genres.isNotEmpty()) {
                    ConfigurableText(
                        text = stringResource(
                            R.string.genres_label,
                            movieDetails.genres.joinToString(", ") { it.name }),
                        style = typography.bodyMedium,
                        uiConfig = uiConfig
                    )
                }

            }
        }

        // ML Sentiment Analysis Card
        SentimentAnalysisCard(
            sentimentReviews = state.sentimentReviews,
            error = state.sentimentError,
            modifier = Modifier.padding(horizontal = Dimens16)
        )

        Spacer(modifier = Modifier.height(Dimens16))
    }
}

/**
 * Preview composable for MovieDetailScreen with sample data for design-time preview.
 * Used for UI development and testing without requiring real movie data.
 */
@Preview(showBackground = true)
@Composable
private fun MovieDetailScreenPreview() {
    CinemyTheme {
        MovieDetailsContent(
            movieDetails = MovieDetails(
                id = 1,
                title = stringResource(R.string.sample_movie),
                description = stringResource(R.string.sample_movie_description),
                posterPath = null,
                backdropPath = null,
                rating = 8.5,
                voteCount = 1000,
                releaseDate = stringResource(R.string.sample_release_date),
                runtime = 120,
                genres = emptyList(),
                productionCompanies = emptyList(),
                budget = 50000000,
                revenue = 100000000,
                status = stringResource(R.string.sample_status)
            ),
            uiConfig = null,
            state = MovieDetailState()
        )
    }
}