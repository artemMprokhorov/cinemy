package org.studioapp.cinemy.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = SplashBackground,
    surface = SplashBackground
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = SplashBackground,
    surface = SplashBackground
)

@Composable
fun CinemyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled to use our custom background
    content: @Composable () -> Unit
) {
    // CRITICAL FIX: Use default MaterialTheme WITHOUT custom colorScheme
    // This allows ConfigurableComponents to use uiConfig colors without override
    MaterialTheme(
        typography = Typography,
        content = content
    )
}