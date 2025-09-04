package com.example.tmdbai.ui.movieslist

import androidx.compose.material.ExperimentalMaterialApi
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.example.tmdbai.BuildConfig
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tmdbai.data.model.Movie
import com.example.tmdbai.data.model.UiConfiguration
import com.example.tmdbai.presentation.movieslist.MoviesListIntent
import com.example.tmdbai.presentation.movieslist.MoviesListState
import com.example.tmdbai.presentation.movieslist.MoviesListViewModel
import com.example.tmdbai.ui.theme.AppBackground
import com.example.tmdbai.ui.theme.Dimens12
import com.example.tmdbai.ui.theme.Dimens16
import com.example.tmdbai.ui.theme.Dimens24
import com.example.tmdbai.ui.theme.Dimens32
import com.example.tmdbai.ui.theme.Dimens40
import com.example.tmdbai.ui.theme.Dimens8
import com.example.tmdbai.ui.theme.MoviePosterBlue
import com.example.tmdbai.ui.theme.MoviePosterDarkBlue
import org.koin.androidx.compose.koinViewModel

// Import the new server-driven UI components
import com.example.tmdbai.ui.components.ConfigurableButton
import com.example.tmdbai.ui.components.ConfigurableText
import com.example.tmdbai.ui.components.ConfigurableMovieCard
import com.example.tmdbai.ui.components.ConfigurableTextByType
import com.example.tmdbai.ui.components.TextType

@Composable
fun MoviesListScreen(
    onMovieClick: (Int) -> Unit = {},
    onBackPressed: () -> Unit = {},
    viewModel: MoviesListViewModel = koinViewModel()
) {
    BackHandler {
        onBackPressed()
    }
    
    val state by viewModel.state.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                state.uiConfig?.colors?.background ?: AppBackground
            )
    ) {
        // Search Header
        SearchHeader(
            searchQuery = state.searchQuery,
            screenMode = state.screenMode,
            onSearchQueryChange = { query ->
                viewModel.processIntent(MoviesListIntent.SearchMovies(query))
            },
            onClearSearch = {
                viewModel.processIntent(MoviesListIntent.ClearSearch)
            },
            uiConfig = state.uiConfig
        )
        
        // NEW: Connection status bar
        ConnectionStatusBar(
            state = state,
            onRetry = { viewModel.processIntent(MoviesListIntent.RetryConnection) },
            onRefresh = { viewModel.processIntent(MoviesListIntent.RefreshData) },
            onDismissError = { viewModel.processIntent(MoviesListIntent.DismissError) }
        )
        
        // Debug indicator for mock data
        if (BuildConfig.DEBUG && BuildConfig.USE_MOCK_DATA) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Yellow.copy(alpha = 0.3f))
            ) {
                Text(
                    text = "🔧 Using Mock Data - ${BuildConfig.FLAVOR_NAME} ${BuildConfig.BUILD_TYPE}",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
            }
        }
        
        // Search metadata display
        state.searchMetadata?.let { metadata ->
            SearchMetadataCard(metadata, state.uiConfig)
        }
        
        // Main content
        when {
            state.isLoading && state.movies.isEmpty() -> LoadingContent(uiConfig = state.uiConfig)
            state.error != null && state.movies.isEmpty() -> ErrorContent(
                error = state.error!!,
                uiConfig = state.uiConfig,
                onRetry = { viewModel.processIntent(MoviesListIntent.RetryLastOperation) }
            )
            else -> MoviesContent(
                state = state,
                onMovieClick = onMovieClick,
                onIntent = { viewModel.processIntent(it) }
            )
        }
    }
}

@Composable
private fun LoadingContent(uiConfig: UiConfiguration? = null) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ConfigurableTextByType(
            textType = TextType.LOADING_TEXT,
            uiConfig = uiConfig,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
private fun ErrorContent(
    error: String,
    uiConfig: UiConfiguration?,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens32),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ConfigurableText(
            text = error,
            style = MaterialTheme.typography.bodyLarge,
            uiConfig = uiConfig,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(Dimens24))
        
        ConfigurableButton(
            text = uiConfig?.texts?.retryButton ?: "Retry",
            onClick = onRetry,
            uiConfig = uiConfig,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MoviesContent(
    state: MoviesListState,
    onMovieClick: (Int) -> Unit,
    onIntent: (MoviesListIntent) -> Unit
) {
    // Pull to refresh state
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = { onIntent(MoviesListIntent.LoadPopularMovies) }
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        // Movies List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = Dimens16, vertical = Dimens8),
            verticalArrangement = Arrangement.spacedBy(Dimens16)
        ) {
            items(state.movies) { movie ->
                MovieItem(
                    movie = movie,
                    uiConfig = state.uiConfig,
                    onClick = { onMovieClick(movie.id) }
                )
            }
            
            // Load more button
            if (state.hasMore && !state.isLoading) {
                item {
                    LoadMoreButton(
                        onLoadMore = { onIntent(MoviesListIntent.LoadMoreMovies) },
                        uiConfig = state.uiConfig
                    )
                }
            }
            
            // Loading indicator for pagination
            if (state.isLoading && state.movies.isNotEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens16),
                        contentAlignment = Alignment.Center
                    ) {
                        ConfigurableText(
                            text = "Loading more movies...",
                            style = MaterialTheme.typography.bodyMedium,
                            uiConfig = state.uiConfig
                        )
                    }
                }
            }
        }
        
        // Pull refresh indicator
        PullRefreshIndicator(
            refreshing = state.isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = state.uiConfig?.colors?.surface ?: Color.White,
            contentColor = state.uiConfig?.colors?.primary ?: MoviePosterBlue
        )
    }
}

@Composable
private fun SearchHeader(
    searchQuery: String,
    screenMode: MoviesListState.ScreenMode,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    uiConfig: UiConfiguration?
) {
    var query by remember { mutableStateOf(searchQuery) }
    
    OutlinedTextField(
        value = query,
        onValueChange = { 
            query = it
            if (it.length > 2) onSearchQueryChange(it)
        },
        label = { Text("Search movies...") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = uiConfig?.colors?.primary ?: MoviePosterBlue,
            unfocusedBorderColor = Color.Gray,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = {
                    query = ""
                    onClearSearch()
                }) {
                    Icon(Icons.Default.Clear, "Clear")
                }
            }
        }
    )
}

@Composable
private fun SearchMetadataCard(
    metadata: com.example.tmdbai.data.model.SearchInfo,
    uiConfig: UiConfiguration?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = uiConfig?.colors?.surface ?: Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ConfigurableText(
                text = "Search Results for: ${metadata.query}",
                style = MaterialTheme.typography.titleMedium,
                uiConfig = uiConfig
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ConfigurableText(
                    text = "${metadata.resultCount} results found",
                    style = MaterialTheme.typography.bodyMedium,
                    uiConfig = uiConfig
                )
                
                ConfigurableText(
                    text = "Avg Rating: ${metadata.avgRating}",
                    style = MaterialTheme.typography.bodyMedium,
                    uiConfig = uiConfig
                )
            }
        }
    }
}

@Composable
private fun LoadMoreButton(
    onLoadMore: () -> Unit,
    uiConfig: UiConfiguration?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens16),
        contentAlignment = Alignment.Center
    ) {
        ConfigurableButton(
            text = "Load More Movies",
            onClick = onLoadMore,
            uiConfig = uiConfig,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun MovieItem(
    movie: Movie,
    uiConfig: UiConfiguration?,
    onClick: () -> Unit
) {
    ConfigurableMovieCard(
        movie = movie,
        onClick = onClick,
        uiConfig = uiConfig,
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens12)
    )
}

@Composable
private fun ConnectionStatusBar(
    state: MoviesListState,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onDismissError: () -> Unit
) {
    // Status indicator bar
    AnimatedVisibility(
        visible = state.statusMessage.isNotEmpty() || state.error != null
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = when (state.connectionStatus) {
                    MoviesListState.ConnectionStatus.Connected -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                    MoviesListState.ConnectionStatus.Disconnected -> Color(0xFFFF9800).copy(alpha = 0.1f)
                    MoviesListState.ConnectionStatus.MockOnly -> Color(0xFF2196F3).copy(alpha = 0.1f)
                    else -> Color(0xFF9E9E9E).copy(alpha = 0.1f)
                }
            )
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Status message
                    if (state.statusMessage.isNotEmpty()) {
                        Text(
                            text = state.statusMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    
                    // Error message
                    state.error?.let { error ->
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    
                    // Last sync time
                    state.lastSyncTime?.let { time ->
                        Text(
                            text = "Last updated: ${formatSyncTime(time)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                // Action buttons
                Row {
                    if (state.canRetry) {
                        IconButton(onClick = onRetry) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Retry",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    
                    IconButton(onClick = onRefresh) {
                        Icon(
                            Icons.Default.Sync,
                            contentDescription = "Refresh",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    if (state.error != null) {
                        IconButton(onClick = onDismissError) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Dismiss",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable 
private fun formatSyncTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    return when {
        diff < 60_000 -> "just now"
        diff < 3600_000 -> "${diff / 60_000}m ago"
        diff < 86400_000 -> "${diff / 3600_000}h ago"
        else -> "${diff / 86400_000}d ago"
    }
}

@Preview(showBackground = true)
@Composable
fun MoviesListScreenPreview() {
    MoviesListScreen()
}
