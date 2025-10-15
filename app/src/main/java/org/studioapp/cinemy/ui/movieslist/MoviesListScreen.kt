package org.studioapp.cinemy.ui.movieslist

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import org.studioapp.cinemy.R
import org.studioapp.cinemy.data.model.Movie
import org.studioapp.cinemy.data.model.UiConfiguration
import org.studioapp.cinemy.presentation.movieslist.MoviesListIntent
import org.studioapp.cinemy.presentation.movieslist.MoviesListIntent.LoadPopularMovies
import org.studioapp.cinemy.presentation.movieslist.MoviesListIntent.NextPage
import org.studioapp.cinemy.presentation.movieslist.MoviesListIntent.PreviousPage
import org.studioapp.cinemy.presentation.movieslist.MoviesListState
import org.studioapp.cinemy.presentation.movieslist.MoviesListViewModel
import org.studioapp.cinemy.ui.components.ConfigurableMovieCard
import org.studioapp.cinemy.ui.components.PullToReloadArrow
import org.studioapp.cinemy.ui.constants.UiConstants.ERROR_FAILED_LOAD_MOVIES
import org.studioapp.cinemy.ui.constants.UiConstants.ERROR_LOADING_MOVIES_RETRY
import org.studioapp.cinemy.ui.constants.UiConstants.ERROR_SUBTITLE
import org.studioapp.cinemy.ui.constants.UiConstants.ERROR_TITLE
import org.studioapp.cinemy.ui.constants.UiConstants.LOADING_INDICATOR
import org.studioapp.cinemy.ui.constants.UiConstants.LOADING_MOVIES
import org.studioapp.cinemy.ui.constants.UiConstants.LOADING_TEXT
import org.studioapp.cinemy.ui.constants.UiConstants.MOVIES_LIST_SCREEN
import org.studioapp.cinemy.ui.constants.UiConstants.PULL_DOWN_RETRY_MOVIES
import org.studioapp.cinemy.ui.constants.UiConstants.RETRY_INSTRUCTION
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

/**
 * Main screen for displaying movies list with pull-to-refresh functionality and pagination support.
 * Handles back navigation to previous page when available.
 *
 * @param viewModel ViewModel for movies list state management and intent processing
 * @param onMovieClick Callback invoked when a movie is clicked for navigation to details
 */
@Composable
fun MoviesListScreen(
    viewModel: MoviesListViewModel,
    onMovieClick: (Movie) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }


    // Handle back navigation - navigate to previous page if available, otherwise allow default back behavior
    val pagination = state.pagination
    val hasPreviousPage = pagination != null && pagination.hasPrevious && !state.isLoading

    BackHandler(enabled = hasPreviousPage) {
        if (hasPreviousPage) {
            // Navigate to previous page
            viewModel.processIntent(PreviousPage)
        }
        // If hasPreviousPage is false, BackHandler is disabled and system will handle back press
    }

    MoviesListContent(
        state = state,
        onIntent = viewModel::processIntent,
        onMovieClick = onMovieClick,
        snackbarHostState = snackbarHostState
    )
}

/**
 * Content composable for movies list with pull-to-refresh functionality.
 * Displays loading, error, empty, or movies grid states based on current state.
 *
 * @param state Current state of the movies list containing movies, loading, error, and pagination info
 * @param onIntent Callback for processing user intents like pagination and refresh
 * @param onMovieClick Callback invoked when a movie is clicked for navigation
 * @param snackbarHostState State for snackbar display with user feedback
 */
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
            onIntent(LoadPopularMovies)
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
                                style = typography.headlineMedium,
                                modifier = Modifier
                                    .semantics {
                                        contentDescription = LOADING_MOVIES
                                    }
                                    .testTag(LOADING_TEXT)
                            )
                            Spacer(modifier = Modifier.height(Dimens16))
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier
                                    .semantics {
                                        contentDescription = LOADING_MOVIES
                                    }
                                    .testTag(LOADING_INDICATOR)
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
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .semantics {
                                    contentDescription = ERROR_LOADING_MOVIES_RETRY
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(Dimens100))
                            Text(
                                text = stringResource(R.string.error_generic),
                                color = Color.White,
                                style = typography.headlineLarge,
                                modifier = Modifier
                                    .semantics {
                                        contentDescription = ERROR_FAILED_LOAD_MOVIES
                                    }
                                    .testTag(ERROR_TITLE)
                            )
                            Text(
                                text = stringResource(R.string.movie_details),
                                color = Color.White,
                                style = typography.headlineLarge,
                                modifier = Modifier
                                    .semantics {
                                        contentDescription = MOVIES_LIST_SCREEN
                                    }
                                    .testTag(ERROR_SUBTITLE)
                            )
                            Spacer(modifier = Modifier.height(Dimens16))
                            PullToReloadArrow()
                            Spacer(modifier = Modifier.height(Dimens16))
                            Text(
                                text = stringResource(R.string.pull_to_reload),
                                color = Color.White,
                                style = typography.headlineMedium,
                                modifier = Modifier
                                    .semantics {
                                        contentDescription = PULL_DOWN_RETRY_MOVIES
                                    }
                                    .testTag(RETRY_INSTRUCTION)
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
                        onNextPage = { onIntent(NextPage) },
                        onPreviousPage = { onIntent(PreviousPage) },
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


/**
 * Grid layout for displaying movies with swipe gestures for pagination.
 * Shows pagination controls when scrolled to bottom and handles swipe navigation.
 *
 * @param movies List of movies to display in the grid
 * @param uiConfig Optional UI configuration for server-driven theming
 * @param onMovieClick Callback invoked when a movie is clicked
 * @param pagination Pagination information for navigation controls
 * @param onNextPage Callback for navigating to next page
 * @param onPreviousPage Callback for navigating to previous page
 * @param snackbarHostState State for displaying snackbar messages
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MoviesGrid(
    movies: List<Movie>,
    uiConfig: UiConfiguration?,
    onMovieClick: (Movie) -> Unit,
    pagination: org.studioapp.cinemy.data.model.Pagination?,
    onNextPage: () -> Unit,
    onPreviousPage: () -> Unit,
    snackbarHostState: SnackbarHostState
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
                        style = typography.headlineMedium,
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

/**
 * Pagination controls component with previous/next buttons and page information.
 * Only shows available navigation buttons based on pagination state.
 *
 * @param pagination Pagination information containing page numbers and navigation availability
 * @param onNextPage Callback for navigating to next page
 * @param onPreviousPage Callback for navigating to previous page
 */
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
            style = typography.bodyMedium
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

/**
 * Empty state component displayed when no movies are available.
 * Shows appropriate message and pull-to-refresh instruction.
 */
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
                style = typography.headlineSmall,
                color = colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(Dimens8))
            Text(
                text = stringResource(R.string.pull_to_refresh_try_again),
                style = typography.bodyMedium,
                color = colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Preview composable for MoviesListScreen with default state.
 * Used for Compose preview in Android Studio.
 */
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