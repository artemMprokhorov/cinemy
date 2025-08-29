package com.example.tmdbai.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tmdbai.ui.theme.Alpha08
import com.example.tmdbai.ui.theme.Dimens1
import com.example.tmdbai.ui.theme.Dimens2
import com.example.tmdbai.ui.theme.Dimens3
import com.example.tmdbai.ui.theme.Dimens32
import com.example.tmdbai.ui.theme.Dimens4
import com.example.tmdbai.ui.theme.Dimens8
import com.example.tmdbai.ui.theme.Dimens80
import com.example.tmdbai.ui.theme.Dimens12
import com.example.tmdbai.ui.theme.Dimens20
import com.example.tmdbai.ui.theme.Dimens120
import com.example.tmdbai.ui.theme.Dimens140
import com.example.tmdbai.ui.theme.SplashBackground
import com.example.tmdbai.ui.theme.SplashGradientEnd
import com.example.tmdbai.ui.theme.SplashGradientStart
import com.example.tmdbai.ui.theme.TextSecondary
import com.example.tmdbai.ui.theme.Typography16
import com.example.tmdbai.ui.theme.Typography32
import com.example.tmdbai.ui.theme.Typography18

@Composable
fun DetailedClapperboard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        // Top section with pattern (clapper sticks area)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens20)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF2A2A2A), // Dark grey
                            Color(0xFF1A1A1A), // Black
                            Color(0xFF2A2A2A), // Dark grey
                            Color(0xFF1A1A1A)  // Black
                        )
                    )
                )
        ) {
            // Three dots on the left (hinge/bolt)
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = Dimens8),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens2)
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(Dimens3)
                            .background(Color.Black, RoundedCornerShape(Dimens2))
                    )
                }
            }
        }
        
        // Main slate area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens120)
                .background(
                    Color(0xFF3A3A3A), // Dark grey background
                    RoundedCornerShape(
                        bottomStart = Dimens8,
                        bottomEnd = Dimens8
                    )
                )
                .padding(Dimens12)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Dimens8)
            ) {
                // PROD.NO. row
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "PROD.NO.",
                        color = Color.White,
                        fontSize = Typography18,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens1)
                        .background(Color(0xFF666666))
                )
                
                // SCENE, TAKE, ROLL row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "SCENE",
                        color = Color.White,
                        fontSize = Typography18,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "TAKE",
                        color = Color.White,
                        fontSize = Typography18,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "ROLL",
                        color = Color.White,
                        fontSize = Typography18,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens1)
                        .background(Color(0xFF666666))
                )
                
                // DATE, SOUND row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "DATE",
                        color = Color.White,
                        fontSize = Typography18,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "SOUND",
                        color = Color.White,
                        fontSize = Typography18,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens1)
                        .background(Color(0xFF666666))
                )
                
                // PROD.CO. row
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "PROD.CO.",
                        color = Color.White,
                        fontSize = Typography18,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens1)
                        .background(Color(0xFF666666))
                )
                
                // DIRECTOR row
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "DIRECTOR",
                        color = Color.White,
                        fontSize = Typography18,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens1)
                        .background(Color(0xFF666666))
                )
            }
        }
    }
}

@Composable
fun MovieAppSplashScreen(
    onSplashComplete: () -> Unit = {}
) {
    LaunchedEffect(key1 = true) {
        kotlinx.coroutines.delay(3000) // 3 seconds
        onSplashComplete()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashBackground)
            .padding(Dimens32),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Movie clapperboard icon
            DetailedClapperboard(
                modifier = Modifier.size(Dimens140)
            )

            Spacer(modifier = Modifier.height(Dimens32))

            // App title
            Text(
                text = "Movie App",
                fontSize = Typography32,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily.Default
            )

            Spacer(modifier = Modifier.height(Dimens8))

            // Subtitle
            Text(
                text = "Discover new films from TMDB",
                fontSize = Typography16,
                color = TextSecondary,
                fontFamily = FontFamily.Default
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieAppSplashScreenPreview() {
    MaterialTheme {
        MovieAppSplashScreen()
    }
}