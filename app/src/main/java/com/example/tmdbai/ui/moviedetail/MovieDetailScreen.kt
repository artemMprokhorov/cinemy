package com.example.tmdbai.ui.moviedetail

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.tmdbai.ui.theme.SplashBackground
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tmdbai.data.model.MovieDetails
import com.example.tmdbai.data.model.UiConfiguration
import com.example.tmdbai.presentation.moviedetail.MovieDetailIntent
import com.example.tmdbai.presentation.moviedetail.MovieDetailState
import com.example.tmdbai.presentation.moviedetail.MovieDetailViewModel
import com.example.tmdbai.ui.components.PullToReloadIndicator
import com.example.tmdbai.ui.components.PullToReloadArrow
import com.example.tmdbai.ui.theme.TmdbAiTheme
import coil.compose.AsyncImage

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
            android.util.Log.d("MovieDetail", "Pull to refresh triggered")
            onIntent(MovieDetailIntent.Retry)
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashBackground)
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
                        Text(
                            text = "Loading...",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator(
                            color = Color.White
                        )
                    }
                }
            }
            state.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(100.dp))
                        Text(
                            text = "Failed to fetch",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineLarge
                        )
                        Text(
                            text = "movie details",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        PullToReloadArrow()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Pull to reload",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
            state.movieDetails != null -> {
                MovieDetailsContent(
                    movieDetails = state.movieDetails,
                    uiConfig = state.uiConfig,
                    onBackClick = onBackClick
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
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Movie poster
        AsyncImage(
            model = movieDetails.posterPath,
            contentDescription = movieDetails.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Movie details
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title
            Text(
                text = movieDetails.title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            // Rating
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rating: ${movieDetails.rating}/10",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "(${movieDetails.voteCount} votes)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }

            // Release date
            Text(
                text = "Release Date: ${movieDetails.releaseDate}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )

            // Description
            Text(
                text = movieDetails.description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify,
                color = Color.White
            )

            // Genres
            if (movieDetails.genres.isNotEmpty()) {
                Text(
                    text = "Genres: ${movieDetails.genres.joinToString(", ") { it.name }}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }

            // Runtime
            if (movieDetails.runtime > 0) {
                Text(
                    text = "Runtime: ${movieDetails.runtime} minutes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailScreenPreview() {
    TmdbAiTheme {
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
            onBackClick = {}
        )
    }
}