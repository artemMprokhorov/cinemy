package org.studioapp.cinemy.utils

import androidx.compose.ui.graphics.Color
import android.graphics.Color as AndroidColor

/**
 * Utility class for color parsing that can be mocked in tests
 */
object ColorUtils {
    /**
     * Parses a color string (e.g., "#FF0000") to a Compose Color
     * @param colorString The color string to parse
     * @return The parsed Compose Color
     */
    fun parseColor(colorString: String): Color {
        return Color(AndroidColor.parseColor(colorString))
    }
}