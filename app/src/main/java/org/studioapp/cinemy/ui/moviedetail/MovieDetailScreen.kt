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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
import org.studioapp.cinemy.ui.theme.CinemyTheme
import org.studioapp.cinemy.ui.theme.Dimens100
import org.studioapp.cinemy.ui.theme.Dimens12
import org.studioapp.cinemy.ui.theme.Dimens16
import org.studioapp.cinemy.ui.theme.Dimens400
import org.studioapp.cinemy.ui.theme.Dimens8
import org.studioapp.cinemy.ui.theme.ImageConfig
import org.studioapp.cinemy.ui.theme.SplashBackground

private const val MIN_RUNTIME = 0

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
        onBackClick = onBackClick,
        onIntent = viewModel::processIntent
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MovieDetailContent(
    state: MovieDetailState,
    onBackClick: () -> Unit,
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
                            style = MaterialTheme.typography.headlineMedium,
                            uiConfig = state.uiConfig,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(Dimens16))
                        CircularProgressIndicator(
                            color = state.uiConfig?.colors?.primary ?: Color.White
                        )
                    }
                }
            }

            state.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .pullRefresh(pullRefreshState),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(Dimens100))
                    ConfigurableText(
                        text = stringResource(R.string.error_generic),
                        style = MaterialTheme.typography.headlineLarge,
                        uiConfig = state.uiConfig,
                        color = Color.White
                    )
                    ConfigurableText(
                        text = stringResource(R.string.movie_details),
                        style = MaterialTheme.typography.headlineLarge,
                        uiConfig = state.uiConfig,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(Dimens16))
                    PullToReloadArrow()
                    Spacer(modifier = Modifier.height(Dimens16))
                    ConfigurableText(
                        text = stringResource(R.string.pull_to_reload),
                        style = MaterialTheme.typography.headlineMedium,
                        uiConfig = state.uiConfig,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(Dimens100))
                }
            }

            state.movieDetails != null -> {
                MovieDetailsContent(
                    movieDetails = state.movieDetails,
                    uiConfig = state.uiConfig,
                    onBackClick = onBackClick,
                    state = state,
                    onIntent = onIntent
                )
            }
        }

        // No pull refresh indicator - only custom spinner on loading screen
    }
}

@Composable
private fun MovieDetailsContent(
    movieDetails: MovieDetails,
    uiConfig: UiConfiguration?,
    onBackClick: () -> Unit,
    state: MovieDetailState,
    onIntent: (MovieDetailIntent) -> Unit
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
                    MaterialTheme.colorScheme.surface
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
                    style = MaterialTheme.typography.headlineMedium,
                    uiConfig = uiConfig
                )

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ConfigurableText(
                        text = stringResource(R.string.rating_label, movieDetails.rating),
                        style = MaterialTheme.typography.bodyLarge,
                        uiConfig = uiConfig,
                        color = uiConfig?.colors?.primary
                    )
                    Spacer(modifier = Modifier.width(Dimens16))
                    ConfigurableText(
                        text = stringResource(R.string.votes_label, movieDetails.voteCount),
                        style = MaterialTheme.typography.bodyMedium,
                        uiConfig = uiConfig
                    )
                }

                // Release date
                ConfigurableText(
                    text = stringResource(R.string.release_date_label, movieDetails.releaseDate),
                    style = MaterialTheme.typography.bodyMedium,
                    uiConfig = uiConfig
                )

                // Description
                ConfigurableText(
                    text = movieDetails.description,
                    style = MaterialTheme.typography.bodyMedium,
                    uiConfig = uiConfig
                )

                // Genres
                if (movieDetails.genres.isNotEmpty()) {
                    ConfigurableText(
                        text = stringResource(
                            R.string.genres_label,
                            movieDetails.genres.joinToString(", ") { it.name }),
                        style = MaterialTheme.typography.bodyMedium,
                        uiConfig = uiConfig
                    )
                }

            }
        }

        // ML Sentiment Analysis Card
        SentimentAnalysisCard(
            sentimentResult = state.sentimentResult,
            sentimentReviews = state.sentimentReviews,
            isLoading = false,
            error = state.sentimentError,
            modifier = Modifier.padding(horizontal = Dimens16)
        )

        Spacer(modifier = Modifier.height(Dimens16))
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailScreenPreview() {
    CinemyTheme {
        MovieDetailsContent(
            movieDetails = MovieDetails(
                id = 1,
                title = "Sample Movie",
                description = "This is a sample movie description that shows how the detail screen looks.",
                posterPath = null,
                backdropPath = null,
                rating = 8.5,
                voteCount = 1000,
                releaseDate = "2023-01-01",
                runtime = 120,
                genres = emptyList(),
                productionCompanies = emptyList(),
                budget = 50000000,
                revenue = 100000000,
                status = "Released"
            ),
            uiConfig = null,
            onBackClick = {},
            state = MovieDetailState(),
            onIntent = {}
        )
    }
}