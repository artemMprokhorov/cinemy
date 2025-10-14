package org.studioapp.cinemy.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import org.studioapp.cinemy.R
import org.studioapp.cinemy.ui.theme.Dimens32
import org.studioapp.cinemy.ui.theme.Dimens8
import org.studioapp.cinemy.ui.theme.SplashBackground
import org.studioapp.cinemy.ui.theme.TextSecondary
import org.studioapp.cinemy.ui.theme.Typography16
import kotlinx.coroutines.delay
import org.studioapp.cinemy.ui.theme.Typography32

private const val SPLASH_DISPLAY_DURATION_MS = 3000L

/**
 * Splash screen for the Cinemy app
 * Displays app branding and initializes the application
 * @param onSplashComplete Callback when splash animation completes
 */
@Composable
fun MovieAppSplashScreen(
    onSplashComplete: () -> Unit = {}
) {
    LaunchedEffect(key1 = true) {
        delay(SPLASH_DISPLAY_DURATION_MS) // 3 seconds
        onSplashComplete()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashBackground)
            .systemBarsPadding()
            .padding(Dimens32)
    ) {
        // Main content centered
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App title
            Text(
                text = stringResource(R.string.app_title),
                fontSize = Typography32,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily.Default
            )

            Spacer(modifier = Modifier.height(Dimens8))

            // Subtitle
            Text(
                text = stringResource(R.string.splash_subtitle),
                fontSize = Typography16,
                color = TextSecondary,
                fontFamily = FontFamily.Default
            )
        }

        // TMDB disclaimer at bottom
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = Dimens8),
            text = stringResource(R.string.tmdb_disclaimer),
            fontSize = Typography16,
            color = TextSecondary,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MovieAppSplashScreenPreview() {
    MaterialTheme {
        MovieAppSplashScreen()
    }
}