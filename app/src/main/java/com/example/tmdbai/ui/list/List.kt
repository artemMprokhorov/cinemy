package com.example.tmdbai.ui.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.tmdbai.ui.theme.Alpha02
import com.example.tmdbai.ui.theme.Alpha03
import com.example.tmdbai.ui.theme.Alpha06
import com.example.tmdbai.ui.theme.Alpha07
import com.example.tmdbai.ui.theme.Alpha08
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
import com.example.tmdbai.ui.theme.Typography20
import com.example.tmdbai.ui.theme.Typography24
import com.example.tmdbai.ui.theme.Typography8

data class Movie(
    val title: String,
    val description: String,
    val posterColor: Color,
    val posterImageUrl: String? = null,
    val rating: String = "",
    val voteCount: String = "",
    val releaseDate: String = ""
)

@Composable
fun MovieListScreen(
    onMovieClick: (String) -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    BackHandler {
        onBackPressed()
    }
    val movies = remember {
        listOf(
            Movie(
                title = "The Midnight Bloom",
                description = "A young botanist discovers a rare, bioluminescent flower with the power to heal any ailment, but must protect it from those who seek to exploit its magic.",
                posterColor = MoviePosterBlue,
                rating = "8.5",
                voteCount = "1,234",
                releaseDate = "2024-03-15"
            ),
            Movie(
                title = "Echoes of the Past",
                description = "A historian uncovers a hidden diary revealing a forgotten chapter of the city's history, leading to a quest for a lost artifact.",
                posterColor = MoviePosterBrown,
                rating = "7.8",
                voteCount = "856",
                releaseDate = "2024-02-28"
            ),
            Movie(
                title = "The Last Starfarer",
                description = "In a distant future, a lone pilot embarks on a perilous journey to the edge of the galaxy to deliver a message that could save humanity.",
                posterColor = MoviePosterDarkBlue,
                rating = "9.1",
                voteCount = "2,567",
                releaseDate = "2024-01-10"
            ),
            Movie(
                title = "Whispers of the Wind",
                description = "A reclusive artist living in a remote mountain village finds inspiration in the wind's whispers, creating breathtaking sculptures that capture the essence of nature.",
                posterColor = MoviePosterGreen,
                rating = "8.2",
                voteCount = "1,789",
                releaseDate = "2024-04-05"
            ),
            Movie(
                title = "The Silent Guardian",
                description = "A mysterious figure watches over a bustling metropolis, silently intervening to protect its citizens from unseen threats.",
                posterColor = MoviePosterNavy,
                rating = "7.9",
                voteCount = "1,432",
                releaseDate = "2024-03-22"
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimens24)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimens20),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Movies",
                    fontSize = Typography24,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White,
                    modifier = Modifier.size(Dimens24)
                )
            }

            // Movies list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(Dimens32),
                contentPadding = PaddingValues(bottom = Dimens24)
            ) {
                items(movies) { movie ->
                    MovieItem(movie = movie, onMovieClick = onMovieClick)
                }
            }
        }
    }
}

@Composable
fun MovieItem(
    movie: Movie,
    onMovieClick: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens20)
    ) {
        // Left content
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Trending tag
            Text(
                text = "Trending",
                fontSize = Typography12,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = Dimens8)
            )

            // Movie title
            Text(
                text = movie.title,
                fontSize = Typography20,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = Dimens12)
            )

            // Movie description
            Text(
                text = movie.description,
                fontSize = Typography14,
                color = TextTertiary,
                lineHeight = Typography20,
                modifier = Modifier.padding(bottom = Dimens16)
            )

            // Movie details row
            Row(
                modifier = Modifier.padding(bottom = Dimens16),
                horizontalArrangement = Arrangement.spacedBy(Dimens16)
            ) {
                // Rating
                Column {
                    Text(
                        text = "Rating",
                        fontSize = Typography12,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = Dimens4)
                    )
                    Text(
                        text = movie.rating,
                        fontSize = Typography14,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Vote Count
                Column {
                    Text(
                        text = "Votes",
                        fontSize = Typography12,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = Dimens4)
                    )
                    Text(
                        text = movie.voteCount,
                        fontSize = Typography14,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Release Date
                Column {
                    Text(
                        text = "Release",
                        fontSize = Typography12,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = Dimens4)
                    )
                    Text(
                        text = movie.releaseDate,
                        fontSize = Typography14,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // More Details button
            Button(
                onClick = { onMovieClick(movie.title) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonContainer
                ),
                shape = RoundedCornerShape(Dimens8),
                contentPadding = PaddingValues(horizontal = Dimens16, vertical = Dimens8)
            ) {
                Text(
                    text = "More Details",
                    color = Color.White,
                    fontSize = Typography14
                )
            }
        }

        // Movie poster
        MoviePoster(
            color = movie.posterColor,
            modifier = Modifier
                .width(Dimens100)
                .height(Dimens140)
        )
    }
}

@Composable
fun MoviePoster(
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(Dimens12))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        color.copy(alpha = Alpha08),
                        color
                    )
                )
            )
            .border(
                width = Dimens1,
                color = Color.White.copy(alpha = Alpha02),
                shape = RoundedCornerShape(Dimens12)
            )
    ) {
        // Placeholder for movie poster image
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens8),
            contentAlignment = Alignment.Center
        ) {
            // Simple movie icon placeholder
            Box(
                modifier = Modifier
                    .size(Dimens40)
                    .background(
                        Color.White.copy(alpha = Alpha03),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White.copy(alpha = Alpha08),
                    modifier = Modifier.size(Dimens20)
                )
            }
        }

        // Gradient overlay at bottom for text readability
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens40)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = Alpha06)
                        )
                    )
                )
        )

        // Movie title on poster (small text)
        Text(
            text = "POSTER",
            fontSize = Typography8,
            color = Color.White.copy(alpha = Alpha07),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(Dimens4)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MovieListScreenPreview() {
    MaterialTheme {
        MovieListScreen()
    }
}