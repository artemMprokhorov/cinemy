package com.example.tmdbai.ui.movieslist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.tmdbai.ui.theme.SplashBackground
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tmdbai.data.model.Movie
import com.example.tmdbai.data.model.UiConfiguration
import com.example.tmdbai.presentation.movieslist.MoviesListIntent
import com.example.tmdbai.presentation.movieslist.MoviesListState
import com.example.tmdbai.presentation.movieslist.MoviesListViewModel
import com.example.tmdbai.ui.components.ConfigurableMovieCard
import com.example.tmdbai.ui.components.PullToReloadIndicator
import com.example.tmdbai.ui.components.PullToReloadArrow
import com.example.tmdbai.ui.theme.TmdbAiTheme

@Composable
fun MoviesListScreen(
    viewModel: MoviesListViewModel,
    onMovieClick: (Movie) -> Unit
) {
    val state by viewModel.state.collectAsState()

    MoviesListContent(
        state = state,
        onIntent = viewModel::processIntent,
        onMovieClick = onMovieClick
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MoviesListContent(
    state: MoviesListState,
    onIntent: (MoviesListIntent) -> Unit,
    onMovieClick: (Movie) -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = { 
            android.util.Log.d("MoviesList", "Pull to refresh triggered")
            onIntent(MoviesListIntent.LoadPopularMovies)
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashBackground)
            .pullRefresh(pullRefreshState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 20.dp)
        ) {
            // Content
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
                state.movies.isEmpty() -> {
                    EmptyState()
                }
                else -> {
                    MoviesGrid(
                        movies = state.movies,
                        uiConfig = state.uiConfig,
                        onMovieClick = onMovieClick
                    )
                }
            }
        }
        
        // No pull refresh indicator - only custom spinner on loading screen
    }
}


@Composable
private fun MoviesGrid(
    movies: List<Movie>,
    uiConfig: UiConfiguration?,
    onMovieClick: (Movie) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        // Add "Popular" title at the top
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Popular",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
            }
        }
        
        items(movies) { movie ->
            ConfigurableMovieCard(
                movie = movie,
                uiConfig = uiConfig,
                onClick = { onMovieClick(movie) }
            )
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No movies found",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Try searching for a different movie",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MoviesListScreenPreview() {
    TmdbAiTheme {
        MoviesListContent(
            state = MoviesListState(),
            onIntent = {},
            onMovieClick = {}
        )
    }
}