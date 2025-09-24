package org.studioapp.cinemy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import org.studioapp.cinemy.R
import org.studioapp.cinemy.data.model.Movie
import org.studioapp.cinemy.data.model.UiConfiguration
import org.studioapp.cinemy.ui.theme.Dimens12
import org.studioapp.cinemy.ui.theme.Dimens16
import org.studioapp.cinemy.ui.theme.Dimens2
import org.studioapp.cinemy.ui.theme.Dimens200
import org.studioapp.cinemy.ui.theme.Dimens4
import org.studioapp.cinemy.ui.theme.Dimens8
import org.studioapp.cinemy.ui.theme.Float05
import org.studioapp.cinemy.ui.theme.Float06
import org.studioapp.cinemy.ui.theme.Float07
import org.studioapp.cinemy.ui.theme.ImageConfig

private const val POPULARITY_THRESHOLD = 0.0
private const val MAX_LINES_TITLE = 2
private const val MAX_LINES_DESCRIPTION = 3

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

    // Determine colors with PRIORITY to uiConfig colors
    val cardColor = if (uiConfig?.colors != null) {
        colorScheme?.surface ?: MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.surface
    }

    val textColor = if (uiConfig?.colors != null) {
        colorScheme?.onSurface ?: Color.White // Force white text
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val primaryColor = if (uiConfig?.colors != null) {
        colorScheme?.primary ?: MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(cardColor), // FORCE background color with absolute priority
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens4
        ),
        shape = RoundedCornerShape(Dimens12)
    ) {
        Column(
            modifier = Modifier.padding(Dimens12)
        ) {
            // Movie poster with backdrop support
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens200)
                    .clip(RoundedCornerShape(Dimens8))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                // Try backdrop first, then poster
                val imagePath = ImageConfig.buildBackdropUrl(movie.backdropPath)
                    ?: ImageConfig.buildPosterUrl(movie.posterPath)

                imagePath?.let { path ->
                    AsyncImage(
                        model = path,
                        contentDescription = stringResource(
                            R.string.movie_poster_description,
                            movie.title
                        ),
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
                            text = stringResource(R.string.no_image),
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
                            .padding(Dimens8)
                            .align(Alignment.TopEnd),
                        color = primaryColor,
                        shape = RoundedCornerShape(Dimens16)
                    ) {
                        ConfigurableText(
                            text = stringResource(
                                R.string.movie_rating_format,
                                movie.rating,
                                movie.voteCount
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            uiConfig = uiConfig,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = Dimens8, vertical = Dimens4)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens12))

            // Movie title
            ConfigurableText(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium,
                uiConfig = uiConfig,
                color = textColor,
                maxLines = MAX_LINES_TITLE,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Dimens4))

            // Movie description
            ConfigurableText(
                text = movie.description,
                style = MaterialTheme.typography.bodySmall,
                uiConfig = uiConfig,
                color = textColor.copy(alpha = Float07),
                maxLines = MAX_LINES_DESCRIPTION,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Dimens8))

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
                        color = textColor.copy(alpha = Float06)
                    )
                }

                // Adult content indicator
                if (movie.adult) {
                    Surface(
                        color = Color.Red,
                        shape = RoundedCornerShape(Dimens4)
                    ) {
                        ConfigurableText(
                            text = stringResource(R.string.adult_content_indicator),
                            style = MaterialTheme.typography.labelSmall,
                            uiConfig = uiConfig,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = Dimens4, vertical = Dimens2)
                        )
                    }
                }
            }

            // Original language and title
            if (movie.originalLanguage.isNotEmpty() || movie.originalTitle.isNotEmpty()) {
                Spacer(modifier = Modifier.height(Dimens4))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Original language
                    if (movie.originalLanguage.isNotEmpty()) {
                        ConfigurableText(
                            text = stringResource(
                                R.string.original_language_label,
                                movie.originalLanguage.uppercase()
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            uiConfig = uiConfig,
                            color = textColor.copy(alpha = Float05)
                        )
                    }

                    // Video indicator
                    if (movie.video) {
                        Surface(
                            color = primaryColor.copy(alpha = Float07),
                            shape = RoundedCornerShape(Dimens4)
                        ) {
                            ConfigurableText(
                                text = stringResource(R.string.video_available),
                                style = MaterialTheme.typography.labelSmall,
                                uiConfig = uiConfig,
                                color = Color.White,
                                modifier = Modifier.padding(
                                    horizontal = Dimens4,
                                    vertical = Dimens2
                                )
                            )
                        }
                    }
                }
            }

            // Original title (if different from title)
            if (movie.originalTitle.isNotEmpty() && movie.originalTitle != movie.title) {
                Spacer(modifier = Modifier.height(Dimens4))
                ConfigurableText(
                    text = stringResource(R.string.original_title_label, movie.originalTitle),
                    style = MaterialTheme.typography.labelSmall,
                    uiConfig = uiConfig,
                    color = textColor.copy(alpha = Float05),
                    maxLines = 1
                )
            }

            // Color metadata indicator
            if (movie.colors.metadata.modelUsed) {
                Spacer(modifier = Modifier.height(Dimens4))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ConfigurableText(
                        text = stringResource(
                            R.string.color_category_label,
                            movie.colors.metadata.category
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        uiConfig = uiConfig,
                        color = textColor.copy(alpha = Float05)
                    )

                    // Color preview dots
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Dimens2)
                    ) {
                        // Primary color dot
                        Box(
                            modifier = Modifier
                                .height(Dimens8)
                                .width(Dimens8)
                                .background(
                                    try {
                                        androidx.compose.ui.graphics.Color(
                                            android.graphics.Color.parseColor(
                                                movie.colors.primary
                                            )
                                        )
                                    } catch (e: Exception) {
                                        primaryColor
                                    },
                                    RoundedCornerShape(Dimens4)
                                )
                        )
                        // Secondary color dot
                        Box(
                            modifier = Modifier
                                .height(Dimens8)
                                .width(Dimens8)
                                .background(
                                    try {
                                        androidx.compose.ui.graphics.Color(
                                            android.graphics.Color.parseColor(
                                                movie.colors.secondary
                                            )
                                        )
                                    } catch (e: Exception) {
                                        primaryColor.copy(alpha = Float07)
                                    },
                                    RoundedCornerShape(Dimens4)
                                )
                        )
                        // Accent color dot
                        Box(
                            modifier = Modifier
                                .height(Dimens8)
                                .width(Dimens8)
                                .background(
                                    try {
                                        androidx.compose.ui.graphics.Color(
                                            android.graphics.Color.parseColor(
                                                movie.colors.accent
                                            )
                                        )
                                    } catch (e: Exception) {
                                        primaryColor.copy(alpha = Float05)
                                    },
                                    RoundedCornerShape(Dimens4)
                                )
                        )
                    }
                }
            }

            // Popularity indicator
            if (movie.popularity > POPULARITY_THRESHOLD) {
                Spacer(modifier = Modifier.height(Dimens4))
                ConfigurableText(
                    text = stringResource(R.string.popularity_label, movie.popularity),
                    style = MaterialTheme.typography.labelSmall,
                    uiConfig = uiConfig,
                    color = textColor.copy(alpha = Float05)
                )
            }
        }
    }
}
