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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tmdbai.data.model.MovieDetails
import com.example.tmdbai.data.model.UiConfiguration
import com.example.tmdbai.presentation.moviedetail.MovieDetailIntent
import com.example.tmdbai.presentation.moviedetail.MovieDetailState
import com.example.tmdbai.presentation.moviedetail.MovieDetailViewModel
import com.example.tmdbai.ui.theme.Alpha06
import com.example.tmdbai.ui.theme.AppBackground
import com.example.tmdbai.ui.theme.Dimens12
import com.example.tmdbai.ui.theme.Dimens16
import com.example.tmdbai.ui.theme.Dimens24
import com.example.tmdbai.ui.theme.Dimens32
import com.example.tmdbai.ui.theme.Dimens48
import com.example.tmdbai.ui.theme.Dimens100
import com.example.tmdbai.ui.theme.Dimens200
import com.example.tmdbai.ui.theme.Dimens8
import com.example.tmdbai.ui.theme.Dimens6
import com.example.tmdbai.ui.theme.Dimens4
import com.example.tmdbai.ui.theme.Dimens2
import com.example.tmdbai.ui.theme.MoviePosterBlue
import com.example.tmdbai.ui.theme.MoviePosterBrown
import com.example.tmdbai.ui.theme.MoviePosterDarkBlue
import com.example.tmdbai.ui.theme.MoviePosterGreen
import com.example.tmdbai.ui.theme.MoviePosterNavy
import org.koin.androidx.compose.koinViewModel

// Import the new server-driven UI components
import com.example.tmdbai.ui.components.ConfigurableButton
import com.example.tmdbai.ui.components.ConfigurableText
import com.example.tmdbai.ui.components.ConfigurableTextByType
import com.example.tmdbai.ui.components.TextType

@Composable
fun MovieDetailScreen(
    movieId: Int,
    onBackClick: () -> Unit = {},
    viewModel: MovieDetailViewModel = koinViewModel()
) {
    BackHandler {
        onBackClick()
    }
    
    val state by viewModel.state.collectAsState()
    
    // Load movie details when the screen is first displayed
    LaunchedEffect(movieId) {
        viewModel.processIntent(MovieDetailIntent.LoadMovieDetails(movieId))
    }
    
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
                    onRetry = { viewModel.processIntent(MovieDetailIntent.Retry) },
                    onBackClick = onBackClick
                )
            }
            state.movieDetails != null -> {
                            MovieDetailContent(
                movieDetails = state.movieDetails!!,
                uiConfig = state.uiConfig,
                onBackClick = onBackClick
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
    onRetry: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens32),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Back button at the top
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(Dimens48)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
        
        Spacer(modifier = Modifier.height(Dimens32))
        
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
            uiConfig = uiConfig
        )
    }
}

@Composable
private fun MovieDetailContent(
    movieDetails: MovieDetails,
    uiConfig: UiConfiguration?,
    onBackClick: () -> Unit
) {
    val posterColors = uiConfig?.colors?.moviePosterColors ?: listOf(
        MoviePosterBlue, MoviePosterBrown, MoviePosterGreen, MoviePosterNavy, MoviePosterDarkBlue
    )
    val posterColor = posterColors[movieDetails.id % posterColors.size]
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            
            Spacer(modifier = Modifier.width(Dimens8))
            
            ConfigurableText(
                text = "Movie Details",
                style = MaterialTheme.typography.titleLarge,
                uiConfig = uiConfig
            )
        }
        
        // Movie Poster
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens200)
                .padding(Dimens16)
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
                modifier = Modifier.size(Dimens48),
                tint = Color.White
            )
        }
        
        // Movie Information
        Column(
            modifier = Modifier.padding(Dimens16)
        ) {
            // Title
            ConfigurableText(
                text = movieDetails.title,
                style = MaterialTheme.typography.headlineLarge,
                uiConfig = uiConfig
            )
            
            Spacer(modifier = Modifier.height(Dimens8))
            
            // Rating and Release Date
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ConfigurableText(
                    text = "★ ${movieDetails.rating}",
                    style = MaterialTheme.typography.bodyMedium,
                    uiConfig = uiConfig
                )
                
                Spacer(modifier = Modifier.width(Dimens16))
                
                ConfigurableText(
                    text = movieDetails.releaseDate,
                    style = MaterialTheme.typography.bodyMedium,
                    uiConfig = uiConfig
                )
            }
            
            Spacer(modifier = Modifier.height(Dimens16))
            
            // Description
            ConfigurableText(
                text = "Overview",
                style = MaterialTheme.typography.titleLarge,
                uiConfig = uiConfig
            )
            
            Spacer(modifier = Modifier.height(Dimens8))
            
            ConfigurableText(
                text = movieDetails.description,
                style = MaterialTheme.typography.bodyMedium,
                uiConfig = uiConfig
            )
            
            Spacer(modifier = Modifier.height(Dimens24))
            
            // Genres
            if (movieDetails.genres.isNotEmpty()) {
                ConfigurableText(
                    text = "Genres",
                    style = MaterialTheme.typography.titleLarge,
                    uiConfig = uiConfig
                )
                
                Spacer(modifier = Modifier.height(Dimens8))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens8)
                ) {
                    movieDetails.genres.forEach { genre ->
                        ConfigurableText(
                            text = genre.name,
                            style = MaterialTheme.typography.bodySmall,
                            uiConfig = uiConfig,
                            modifier = Modifier
                                .background(
                                    uiConfig?.buttons?.secondaryButtonColor ?: MoviePosterDarkBlue,
                                    RoundedCornerShape(Dimens12)
                                )
                                .padding(horizontal = Dimens12, vertical = Dimens6)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(Dimens24))
            }
            
            // Production Companies
            if (movieDetails.productionCompanies.isNotEmpty()) {
                ConfigurableText(
                    text = "Production Companies",
                    style = MaterialTheme.typography.titleLarge,
                    uiConfig = uiConfig
                )
                
                Spacer(modifier = Modifier.height(Dimens8))
                
                movieDetails.productionCompanies.forEach { company ->
                    ConfigurableText(
                        text = "• ${company.name}",
                        style = MaterialTheme.typography.bodySmall,
                        uiConfig = uiConfig,
                        modifier = Modifier.padding(vertical = Dimens2)
                    )
                }
                
                Spacer(modifier = Modifier.height(Dimens24))
            }
            
            // Additional Details
            ConfigurableText(
                text = "Additional Details",
                style = MaterialTheme.typography.titleLarge,
                uiConfig = uiConfig
            )
            
            Spacer(modifier = Modifier.height(Dimens8))
            
            DetailRow("Vote Count", movieDetails.voteCount.toString(), uiConfig)
            DetailRow("Runtime", "${movieDetails.runtime ?: "N/A"} minutes", uiConfig)
            DetailRow("Budget", "$${movieDetails.budget}", uiConfig)
            DetailRow("Revenue", "$${movieDetails.revenue}", uiConfig)
            
            Spacer(modifier = Modifier.height(Dimens32))
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    uiConfig: UiConfiguration? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens4)
    ) {
        ConfigurableText(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            uiConfig = uiConfig,
            modifier = Modifier.width(Dimens100)
        )
        
        ConfigurableText(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            uiConfig = uiConfig
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MovieDetailScreenPreview() {
    MovieDetailScreen(movieId = 1)
}
