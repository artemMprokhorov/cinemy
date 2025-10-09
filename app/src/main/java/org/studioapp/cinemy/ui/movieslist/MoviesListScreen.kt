package org.studioapp.cinemy.ui.movieslist

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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import kotlinx.coroutines.launch
import org.studioapp.cinemy.R
import org.studioapp.cinemy.data.model.Movie
import org.studioapp.cinemy.data.model.UiConfiguration
import org.studioapp.cinemy.presentation.movieslist.MoviesListIntent
import org.studioapp.cinemy.presentation.movieslist.MoviesListState
import org.studioapp.cinemy.presentation.movieslist.MoviesListViewModel
import org.studioapp.cinemy.ui.components.ConfigurableMovieCard
import org.studioapp.cinemy.ui.components.PullToReloadArrow
import org.studioapp.cinemy.ui.theme.CinemyTheme
import org.studioapp.cinemy.ui.theme.Dimens100
import org.studioapp.cinemy.ui.theme.Dimens112
import org.studioapp.cinemy.ui.theme.Dimens12
import org.studioapp.cinemy.ui.theme.Dimens16
import org.studioapp.cinemy.ui.theme.Dimens8
import org.studioapp.cinemy.ui.theme.Float02
import org.studioapp.cinemy.ui.theme.SplashBackground

private const val SWIPE_THRESHOLD = 50
private const val SNACKBAR_DEBOUNCE_MS = 2000

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
                .padding(start = Dimens16, end = Dimens16)
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
                            Spacer(modifier = Modifier.height(Dimens16))
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
                            Spacer(modifier = Modifier.height(Dimens100))
                            Text(
                                text = stringResource(R.string.error_generic),
                                color = Color.White,
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Text(
                                text = stringResource(R.string.movie_details),
                                color = Color.White,
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Spacer(modifier = Modifier.height(Dimens16))
                            PullToReloadArrow()
                            Spacer(modifier = Modifier.height(Dimens16))
                            Text(
                                text = stringResource(R.string.pull_to_reload),
                                color = Color.White,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Spacer(modifier = Modifier.height(Dimens100))
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
                        snackbarHostState = snackbarHostState,
                        pullRefreshState = pullRefreshState
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MoviesGrid(
    movies: List<Movie>,
    uiConfig: UiConfiguration?,
    onMovieClick: (Movie) -> Unit,
    pagination: org.studioapp.cinemy.data.model.Pagination?,
    onNextPage: () -> Unit,
    onPreviousPage: () -> Unit,
    snackbarHostState: SnackbarHostState,
    pullRefreshState: androidx.compose.material.pullrefresh.PullRefreshState
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var lastSnackbarTime by remember { mutableStateOf(0L) }
    val lastPageMessage = stringResource(R.string.last_page_message)

    // Track scroll position to show pagination controls when at bottom
    // Use derivedStateOf to prevent infinite recompositions
    val showPaginationControls by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val isAtBottom = layoutInfo.visibleItemsInfo.lastOrNull()?.let { lastVisibleItem ->
                lastVisibleItem.index >= movies.size - 1
            } ?: false
            isAtBottom && pagination != null
        }
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
                            if (dragAmount.x > SWIPE_THRESHOLD) { // Swipe right threshold
                                if (pagination?.hasPrevious == true) {
                                    onPreviousPage()
                                }
                                // If it's first page, do nothing
                            }
                            // Swipe left (negative X) = Next page
                            else if (dragAmount.x < -SWIPE_THRESHOLD) { // Swipe left threshold
                                if (pagination?.hasNext == true) {
                                    onNextPage()
                                } else {
                                    // Show snackbar if it's the last page (with debouncing)
                                    val currentTime = System.currentTimeMillis()
                                    if (currentTime - lastSnackbarTime > SNACKBAR_DEBOUNCE_MS) { // 2 second debounce
                                        lastSnackbarTime = currentTime
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar(
                                                message = lastPageMessage,
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    )
                },
            verticalArrangement = Arrangement.spacedBy(Dimens16),
            contentPadding = PaddingValues(
                top = Dimens8,
                bottom = if (showPaginationControls) {
                    // Calculate based on actual content: text + spacing + buttons + padding
                    // Text height (~20dp) + spacing (12dp) + button height (~48dp) + padding (16dp * 2) = ~112dp
                    Dimens112
                } else Dimens8
            )
        ) {
            // Add "Popular" title at the top
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens16),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.popular_title),
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
                    .padding(Dimens16)
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
    pagination: org.studioapp.cinemy.data.model.Pagination,
    onNextPage: () -> Unit,
    onPreviousPage: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens12)
    ) {
        // Page info
        Text(
            text = stringResource(R.string.page_info, pagination.page, pagination.totalPages),
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
                        containerColor = Color.White.copy(alpha = Float02)
                    )
                ) {
                    Text(
                        text = stringResource(R.string.previous_button),
                        color = Color.White
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(Dimens100))
            }

            // Next button
            if (pagination.hasNext) {
                androidx.compose.material3.Button(
                    onClick = onNextPage,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = Float02)
                    )
                ) {
                    Text(
                        text = stringResource(R.string.next_button),
                        color = Color.White
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(Dimens100))
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
            Spacer(modifier = Modifier.height(Dimens8))
            Text(
                text = stringResource(R.string.pull_to_refresh_try_again),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MoviesListScreenPreview() {
    CinemyTheme {
        MoviesListContent(
            state = MoviesListState(),
            onIntent = {},
            onMovieClick = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}