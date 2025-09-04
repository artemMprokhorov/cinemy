package com.example.tmdbai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.tmdbai.data.model.Movie
import com.example.tmdbai.data.model.UiConfiguration

/**
 * Configurable movie card component that supports server-driven styling
 * 
 * This component accepts UiConfig parameters for dynamic theming while
 * falling back to Material3 defaults when no configuration is provided.
 * 
 * @param movie The movie data to display
 * @param onClick The action to perform when the card is clicked
 * @param uiConfig Optional UI configuration for dynamic styling
 * @param modifier Modifier to apply to the card
 * @param showRating Whether to display the movie rating
 * @param showReleaseDate Whether to display the release date
 */
@Composable
fun ConfigurableMovieCard(
    movie: Movie,
    onClick: () -> Unit,
    uiConfig: UiConfiguration? = null,
    modifier: Modifier = Modifier,
    showRating: Boolean = true,
    showReleaseDate: Boolean = true
) {
    // Extract color configuration from UiConfig
    val colorScheme = uiConfig?.colors
    
    // Determine colors with fallback to Material3 defaults
    val cardColor = colorScheme?.surface ?: MaterialTheme.colorScheme.surface
    val textColor = colorScheme?.onSurface ?: MaterialTheme.colorScheme.onSurface
    val primaryColor = colorScheme?.primary ?: MaterialTheme.colorScheme.primary
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Movie poster with backdrop support
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                // Try backdrop first, then poster
                val imagePath = movie.backdropPath?.let { "https://image.tmdb.org/t/p/w500$it" }
                    ?: movie.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
                
                imagePath?.let { path ->
                    AsyncImage(
                        model = path,
                        contentDescription = "Poster for ${movie.title}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } ?: run {
                    // Fallback placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        ConfigurableText(
                            text = "No Image",
                            style = MaterialTheme.typography.bodyMedium,
                            uiConfig = uiConfig,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                // Rating badge overlay
                if (showRating) {
                    Surface(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.TopEnd),
                        color = primaryColor,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        ConfigurableText(
                            text = "★ ${movie.rating} (${movie.voteCount})",
                            style = MaterialTheme.typography.labelSmall,
                            uiConfig = uiConfig,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Movie title
            ConfigurableText(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium,
                uiConfig = uiConfig,
                color = textColor,
                maxLines = 2,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Movie description
            ConfigurableText(
                text = movie.description,
                style = MaterialTheme.typography.bodySmall,
                uiConfig = uiConfig,
                color = textColor.copy(alpha = 0.7f),
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Additional movie info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Release date
                if (showReleaseDate) {
                    ConfigurableText(
                        text = movie.releaseDate,
                        style = MaterialTheme.typography.labelSmall,
                        uiConfig = uiConfig,
                        color = textColor.copy(alpha = 0.6f)
                    )
                }
                
                // Adult content indicator
                if (movie.adult) {
                    Surface(
                        color = Color.Red,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        ConfigurableText(
                            text = "18+",
                            style = MaterialTheme.typography.labelSmall,
                            uiConfig = uiConfig,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            
            // Popularity indicator
            if (movie.popularity > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                ConfigurableText(
                    text = "Popularity: ${String.format("%.1f", movie.popularity)}",
                    style = MaterialTheme.typography.labelSmall,
                    uiConfig = uiConfig,
                    color = textColor.copy(alpha = 0.5f)
                )
            }
        }
    }
}

/**
 * Compact version of the movie card for list views
 * 
 * @param movie The movie data to display
 * @param onClick The action to perform when the card is clicked
 * @param uiConfig Optional UI configuration for dynamic styling
 * @param modifier Modifier to apply to the card
 */
@Composable
fun ConfigurableMovieCardCompact(
    movie: Movie,
    onClick: () -> Unit,
    uiConfig: UiConfiguration? = null,
    modifier: Modifier = Modifier
) {
    ConfigurableMovieCard(
        movie = movie,
        onClick = onClick,
        uiConfig = uiConfig,
        modifier = modifier,
        showRating = false,
        showReleaseDate = false
    )
}
