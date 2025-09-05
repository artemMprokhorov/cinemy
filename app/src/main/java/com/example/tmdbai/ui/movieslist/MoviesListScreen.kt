package com.example.tmdbai.ui.movieslist

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tmdbai.BuildConfig
import com.example.tmdbai.R
import com.example.tmdbai.data.model.Movie
import com.example.tmdbai.data.model.UiConfiguration
import com.example.tmdbai.presentation.movieslist.MoviesListIntent
import com.example.tmdbai.presentation.movieslist.MoviesListState
import com.example.tmdbai.presentation.movieslist.MoviesListViewModel
import com.example.tmdbai.ui.components.ConfigurableMovieCard
import com.example.tmdbai.ui.components.PullToReloadArrow
import com.example.tmdbai.ui.theme.SplashBackground
import com.example.tmdbai.ui.theme.TmdbAiTheme
import kotlinx.coroutines.launch

@Composable
fun MoviesListScreen(
    viewModel: MoviesListViewModel,
    onMovieClick: (Movie) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    MoviesListContent(
        state = state,
        onIntent = viewModel::processIntent,
        onMovieClick = onMovieClick,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MoviesListContent(
    state: MoviesListState,
    onIntent: (MoviesListIntent) -> Unit,
    onMovieClick: (Movie) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = {
            if (BuildConfig.DEBUG) {
                Log.d("MoviesList", "Pull to refresh triggered")
            }
            onIntent(MoviesListIntent.LoadPopularMovies)
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashBackground)
            .systemBarsPadding()
            .pullRefresh(pullRefreshState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp)
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
                                text = stringResource(R.string.loading_text),
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
                                text = stringResource(R.string.error_generic),
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
                        onMovieClick = onMovieClick,
                        pagination = state.pagination,
                        onNextPage = { onIntent(MoviesListIntent.NextPage) },
                        onPreviousPage = { onIntent(MoviesListIntent.PreviousPage) },
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }

        // Snackbar for user feedback with custom colors
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
            snackbar = { snackbarData ->
                androidx.compose.material3.Snackbar(
                    snackbarData = snackbarData,
                    containerColor = SplashBackground,
                    contentColor = Color.White,
                    actionColor = Color.White
                )
            }
        )

        // No pull refresh indicator - only custom spinner on loading screen
    }
}


@Composable
private fun MoviesGrid(
    movies: List<Movie>,
    uiConfig: UiConfiguration?,
    onMovieClick: (Movie) -> Unit,
    pagination: com.example.tmdbai.data.model.Pagination?,
    onNextPage: () -> Unit,
    onPreviousPage: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val listState = rememberLazyListState()
    var showPaginationControls by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var lastSnackbarTime by remember { mutableStateOf(0L) }

    // Track scroll position to show pagination controls when at bottom
    LaunchedEffect(listState.layoutInfo) {
        val layoutInfo = listState.layoutInfo
        val isAtBottom = layoutInfo.visibleItemsInfo.lastOrNull()?.let { lastVisibleItem ->
            lastVisibleItem.index >= movies.size - 1
        } ?: false

        showPaginationControls = isAtBottom && pagination != null
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = { },
                        onDrag = { _, dragAmount ->
                            // Swipe right (positive X) = Previous page
                            if (dragAmount.x > 50) { // Swipe right threshold
                                if (pagination?.hasPrevious == true) {
                                    onPreviousPage()
                                }
                                // If it's first page, do nothing
                            }
                            // Swipe left (negative X) = Next page
                            else if (dragAmount.x < -50) { // Swipe left threshold
                                if (pagination?.hasNext == true) {
                                    onNextPage()
                                } else {
                                    // Show snackbar if it's the last page (with debouncing)
                                    val currentTime = System.currentTimeMillis()
                                    if (currentTime - lastSnackbarTime > 2000) { // 2 second debounce
                                        lastSnackbarTime = currentTime
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar(
                                                message = "This is the last available page",
                                                duration = androidx.compose.material3.SnackbarDuration.Short
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    )
                },
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                top = 8.dp,
                bottom = if (showPaginationControls) 88.dp else 8.dp
            )
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

        // Pagination controls clipped to bottom of screen - only show when at bottom
        if (showPaginationControls) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(SplashBackground)
                    .padding(16.dp)
            ) {
                PaginationControls(
                    pagination = pagination!!,
                    onNextPage = onNextPage,
                    onPreviousPage = onPreviousPage
                )
            }
        }
    }
}

@Composable
private fun PaginationControls(
    pagination: com.example.tmdbai.data.model.Pagination,
    onNextPage: () -> Unit,
    onPreviousPage: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Page info
        Text(
            text = "Page ${pagination.page} of ${pagination.totalPages}",
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )

        // Navigation buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Previous button
            if (pagination.hasPrevious) {
                androidx.compose.material3.Button(
                    onClick = onPreviousPage,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    Text(
                        text = "Previous",
                        color = Color.White
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(100.dp))
            }

            // Next button
            if (pagination.hasNext) {
                androidx.compose.material3.Button(
                    onClick = onNextPage,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    Text(
                        text = "Next",
                        color = Color.White
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(100.dp))
            }
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
                text = stringResource(R.string.no_movies_found),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Pull to refresh to try again",
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
            onMovieClick = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}