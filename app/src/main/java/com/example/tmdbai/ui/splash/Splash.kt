package com.example.tmdbai.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.tmdbai.ui.theme.Dimens2
import com.example.tmdbai.ui.theme.Dimens32
import com.example.tmdbai.ui.theme.Dimens4
import com.example.tmdbai.ui.theme.Dimens8
import com.example.tmdbai.ui.theme.Dimens80
import com.example.tmdbai.ui.theme.SplashBackground
import com.example.tmdbai.ui.theme.SplashGradientEnd
import com.example.tmdbai.ui.theme.SplashGradientStart
import com.example.tmdbai.ui.theme.TextSecondary
import com.example.tmdbai.ui.theme.Typography16
import com.example.tmdbai.ui.theme.Typography32
import com.example.tmdbai.ui.theme.Dimens12

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
            Box(
                modifier = Modifier
                    .size(Dimens80)
                    .clip(RoundedCornerShape(Dimens8))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                SplashGradientStart,
                                SplashGradientEnd
                            )
                        )
                    ),
                contentAlignment = Alignment.TopEnd
            ) {
                // Clapperboard stripes
                val STRIPE_COUNT = 4
                repeat(STRIPE_COUNT) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(Alpha08)
                            .height(Dimens4)
                            .offset(y = (12 + index * 8).dp)
                            .background(
                                Color.Black,
                                RoundedCornerShape(Dimens2)
                            )
                    )
                }
            }

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