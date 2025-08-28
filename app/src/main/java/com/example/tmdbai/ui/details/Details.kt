package com.example.tmdbai.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.activity.compose.BackHandler
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.tmdbai.ui.theme.DetailsBackground
import com.example.tmdbai.ui.theme.DetailsDarkSlate
import com.example.tmdbai.ui.theme.DetailsMintGreen
import com.example.tmdbai.ui.theme.DetailsPosterDark
import com.example.tmdbai.ui.theme.DetailsPosterLight
import com.example.tmdbai.ui.theme.DetailsSkyBlue
import com.example.tmdbai.ui.theme.DetailsWheat
import com.example.tmdbai.ui.theme.Dimens12
import com.example.tmdbai.ui.theme.Dimens120
import com.example.tmdbai.ui.theme.Dimens16
import com.example.tmdbai.ui.theme.Dimens24
import com.example.tmdbai.ui.theme.Dimens280
import com.example.tmdbai.ui.theme.Dimens32
import com.example.tmdbai.ui.theme.Dimens4
import com.example.tmdbai.ui.theme.Dimens40
import com.example.tmdbai.ui.theme.Dimens400
import com.example.tmdbai.ui.theme.Dimens500
import com.example.tmdbai.ui.theme.Dimens8
import com.example.tmdbai.ui.theme.Dimens80
import com.example.tmdbai.ui.theme.TextTertiary
import com.example.tmdbai.ui.theme.Typography14
import com.example.tmdbai.ui.theme.Typography16
import com.example.tmdbai.ui.theme.Typography24
import com.example.tmdbai.ui.theme.Typography28
import com.example.tmdbai.ui.theme.Typography32
import com.example.tmdbai.ui.theme.Typography5
import com.example.tmdbai.ui.theme.Typography6
import com.example.tmdbai.ui.theme.Typography8
import com.example.tmdbai.ui.theme.LetterSpacing2

data class MovieDetail(
    val title: String,
    val description: String,
    val posterUrl: String? = null
)

@Composable
fun MovieDetailsScreen(
    movieId: String = "",
    onBackClick: () -> Unit = {}
) {
    BackHandler {
        onBackClick()
    }
    
    val movieDetail = remember {
        MovieDetail(
            title = "The Shawshank Redemption",
            description = "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DetailsBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Movie poster section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens500)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                DetailsPosterLight,
                                DetailsPosterDark
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Movie poster
                MoviePosterArt(
                    modifier = Modifier
                        .width(Dimens280)
                        .height(Dimens400)
                )
            }

            // Movie details section
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DetailsBackground)
                    .padding(Dimens24)
            ) {
                Column {
                    // Back button
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(bottom = Dimens16)
                            .size(Dimens40)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(Dimens24)
                        )
                    }

                    // Movie title
                    Text(
                        text = movieDetail.title,
                        fontSize = Typography28,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = Typography32,
                        modifier = Modifier.padding(bottom = Dimens16)
                    )

                    // Movie description
                    Text(
                        text = movieDetail.description,
                        fontSize = Typography16,
                        color = TextTertiary,
                        lineHeight = Typography24
                    )
                }
            }
        }
    }
}

@Composable
fun MoviePosterArt(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(Dimens12))
            .background(Color.White)
            .padding(Dimens16)
    ) {
        // Vintage movie poster recreation
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            DetailsMintGreen, // Mint green
                            DetailsSkyBlue, // Sky blue
                            DetailsWheat  // Wheat/sand
                        )
                    )
                )
        ) {
            // Movie poster placeholder
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DetailsDarkSlate)
            )

            // Movie title text overlay
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = Dimens32),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "STEPHEN KING'S RITA HAYWORTH",
                    fontSize = Typography8,
                    color = DetailsDarkSlate,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(Dimens8))

                Text(
                    text = "SHAWSHANK",
                    fontSize = Typography14,
                    color = DetailsDarkSlate,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = LetterSpacing2
                )

                Text(
                    text = "REDEMPTION",
                    fontSize = Typography14,
                    color = DetailsDarkSlate,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = LetterSpacing2
                )
            }

            // Silhouette placeholder
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = Dimens40)
                    .size(Dimens80, Dimens120)
                    .background(Color.Black, CircleShape)
            )

            // Bottom credits text
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = Dimens16),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "TIM ROBBINS    MORGAN FREEMAN",
                    fontSize = Typography6,
                    color = DetailsDarkSlate,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(Dimens4))

                Text(
                    text = "A FRANK DARABONT FILM",
                    fontSize = Typography5,
                    color = DetailsDarkSlate
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieDetailsScreenPreview() {
    MaterialTheme {
        MovieDetailsScreen()
    }
}