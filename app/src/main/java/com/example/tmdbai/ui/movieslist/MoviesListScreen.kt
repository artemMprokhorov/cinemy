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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                state.uiConfig?.colors?.background ?: AppBackground
            )
    ) {
        when {
            state.isLoading -> {
                LoadingContent(uiConfig = state.uiConfig)
            }
            state.error != null -> {
                ErrorContent(
                    error = state.error!!,
                    uiConfig = state.uiConfig,
                    onRetry = { viewModel.processIntent(MoviesListIntent.Retry) }
                )
            }
            else -> {
                MoviesContent(
                    state = state,
                    onMovieClick = onMovieClick,
                    onIntent = { viewModel.processIntent(it) }
                )
            }
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
    var searchQuery by remember { mutableStateOf("") }
    
    // Pull to refresh state
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = { onIntent(MoviesListIntent.Refresh) }
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Search Bar
            if (state.isSearchMode) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = { onIntent(MoviesListIntent.SearchMovies(it)) },
                    onClear = { onIntent(MoviesListIntent.ClearSearch) },
                    uiConfig = state.uiConfig
                )
            }
            
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
                
                // Pagination
                if (state.hasNextPage || state.hasPreviousPage) {
                    item {
                        PaginationControls(
                            hasNext = state.hasNextPage,
                            hasPrevious = state.hasPreviousPage,
                            currentPage = state.currentPage,
                            onNext = { onIntent(MoviesListIntent.LoadNextPage) },
                            onPrevious = { onIntent(MoviesListIntent.LoadPreviousPage) },
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
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
    uiConfig: UiConfiguration?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search movies...") },
            modifier = Modifier.weight(1f),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = uiConfig?.colors?.primary ?: MoviePosterBlue,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
        
        Spacer(modifier = Modifier.width(Dimens8))
        
        ConfigurableButton(
            text = "🔍",
            onClick = { onSearch(query) },
            uiConfig = uiConfig,
            modifier = Modifier.size(Dimens40)
        )
        
        Spacer(modifier = Modifier.width(Dimens8))
        
        ConfigurableButton(
            text = "Clear",
            onClick = onClear,
            uiConfig = uiConfig,
            isSecondary = true,
            modifier = Modifier.size(Dimens40)
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
private fun PaginationControls(
    hasNext: Boolean,
    hasPrevious: Boolean,
    currentPage: Int,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    uiConfig: UiConfiguration?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens16),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ConfigurableButton(
            text = "Previous",
            onClick = onPrevious,
            enabled = hasPrevious,
            uiConfig = uiConfig,
            isSecondary = true
        )
        
        ConfigurableText(
            text = "Page $currentPage",
            style = MaterialTheme.typography.bodyMedium,
            uiConfig = uiConfig
        )
        
        ConfigurableButton(
            text = "Next",
            onClick = onNext,
            enabled = hasNext,
            uiConfig = uiConfig
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MoviesListScreenPreview() {
    MoviesListScreen()
}
