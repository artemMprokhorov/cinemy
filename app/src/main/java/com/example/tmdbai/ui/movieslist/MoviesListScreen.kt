package com.example.tmdbai.ui.movieslist

import androidx.compose.material.ExperimentalMaterialApi

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tmdbai.data.model.Movie
import com.example.tmdbai.data.model.UiConfiguration
import com.example.tmdbai.presentation.movieslist.MoviesListIntent
import com.example.tmdbai.presentation.movieslist.MoviesListState
import com.example.tmdbai.presentation.movieslist.MoviesListViewModel
import com.example.tmdbai.ui.theme.Alpha06
import com.example.tmdbai.ui.theme.AppBackground
import com.example.tmdbai.ui.theme.ButtonContainer
import com.example.tmdbai.ui.theme.Dimens1
import com.example.tmdbai.ui.theme.Dimens100
import com.example.tmdbai.ui.theme.Dimens12
import com.example.tmdbai.ui.theme.Dimens140
import com.example.tmdbai.ui.theme.Dimens16
import com.example.tmdbai.ui.theme.Dimens20
import com.example.tmdbai.ui.theme.Dimens24
import com.example.tmdbai.ui.theme.Dimens32
import com.example.tmdbai.ui.theme.Dimens4
import com.example.tmdbai.ui.theme.Dimens40
import com.example.tmdbai.ui.theme.Dimens8
import com.example.tmdbai.ui.theme.MoviePosterBlue
import com.example.tmdbai.ui.theme.MoviePosterBrown
import com.example.tmdbai.ui.theme.MoviePosterDarkBlue
import com.example.tmdbai.ui.theme.MoviePosterGreen
import com.example.tmdbai.ui.theme.MoviePosterNavy
import com.example.tmdbai.ui.theme.TextSecondary
import com.example.tmdbai.ui.theme.TextTertiary
import com.example.tmdbai.ui.theme.Typography12
import com.example.tmdbai.ui.theme.Typography14
import com.example.tmdbai.ui.theme.Typography16
import com.example.tmdbai.ui.theme.Typography20
import com.example.tmdbai.ui.theme.Typography24
import com.example.tmdbai.ui.theme.Typography8
import org.koin.androidx.compose.koinViewModel

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
                LoadingContent()
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
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Loading movies...",
            color = Color.White,
            fontSize = Typography20
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
        Text(
            text = error,
            color = Color.White,
            fontSize = Typography16,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(Dimens24))
        
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = uiConfig?.buttons?.primaryButtonColor ?: MoviePosterBlue
            )
        ) {
            Text(
                text = uiConfig?.texts?.retryButton ?: "Retry",
                color = uiConfig?.buttons?.buttonTextColor ?: Color.White
            )
        }
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
        androidx.compose.material3.OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search movies...") },
            modifier = Modifier.weight(1f),
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedBorderColor = uiConfig?.colors?.primary ?: MoviePosterBlue,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
        
        Spacer(modifier = Modifier.width(Dimens8))
        
        Button(
            onClick = { onSearch(query) },
            colors = ButtonDefaults.buttonColors(
                containerColor = uiConfig?.buttons?.primaryButtonColor ?: MoviePosterBlue
            )
        ) {
            Icon(Icons.Default.Search, contentDescription = "Search")
        }
        
        Spacer(modifier = Modifier.width(Dimens8))
        
        Button(
            onClick = onClear,
            colors = ButtonDefaults.buttonColors(
                containerColor = uiConfig?.buttons?.secondaryButtonColor ?: MoviePosterDarkBlue
            )
        ) {
            Text("Clear")
        }
    }
}

@Composable
private fun MovieItem(
    movie: Movie,
    uiConfig: UiConfiguration?,
    onClick: () -> Unit
) {
    val posterColors = uiConfig?.colors?.moviePosterColors ?: listOf(
        MoviePosterBlue, MoviePosterBrown, MoviePosterGreen, MoviePosterNavy, MoviePosterDarkBlue
    )
    val posterColor = posterColors[movie.id % posterColors.size]
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(Dimens12),
        verticalAlignment = Alignment.Top
    ) {
        // Movie Poster
        Box(
            modifier = Modifier
                .size(Dimens100, Dimens140)
                .clip(RoundedCornerShape(Dimens12))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            posterColor,
                            posterColor.copy(alpha = Alpha06)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                modifier = Modifier.size(Dimens32),
                tint = Color.White
            )
        }
        
        Spacer(modifier = Modifier.width(Dimens16))
        
        // Movie Info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = movie.title,
                fontSize = Typography20,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2
            )
            
            Spacer(modifier = Modifier.height(Dimens8))
            
            Text(
                text = movie.description,
                fontSize = Typography14,
                color = TextSecondary,
                maxLines = 3
            )
            
            Spacer(modifier = Modifier.height(Dimens12))
            
            // Rating and Release Date
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "★ ${movie.rating}",
                    fontSize = Typography12,
                    color = TextTertiary
                )
                
                Spacer(modifier = Modifier.width(Dimens16))
                
                Text(
                    text = movie.releaseDate,
                    fontSize = Typography12,
                    color = TextTertiary
                )
            }
        }
    }
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
        Button(
            onClick = onPrevious,
            enabled = hasPrevious,
            colors = ButtonDefaults.buttonColors(
                containerColor = uiConfig?.buttons?.secondaryButtonColor ?: MoviePosterDarkBlue
            )
        ) {
            Text("Previous")
        }
        
        Text(
            text = "Page $currentPage",
            color = Color.White,
            fontSize = Typography14
        )
        
        Button(
            onClick = onNext,
            enabled = hasNext,
            colors = ButtonDefaults.buttonColors(
                containerColor = uiConfig?.buttons?.primaryButtonColor ?: MoviePosterBlue
            )
        ) {
            Text("Next")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoviesListScreenPreview() {
    MoviesListScreen()
}
