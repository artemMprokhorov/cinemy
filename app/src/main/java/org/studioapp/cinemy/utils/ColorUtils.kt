package org.studioapp.cinemy.utils

import androidx.compose.ui.graphics.Color
import android.graphics.Color as AndroidColor

/**
 * Utility functions for color operations
 */
object ColorUtils {

    /**
     * Safely parse a color string to Compose Color
     * @param colorString The color string to parse (e.g., "#FF0000", "red")
     * @return Parsed Color or null if parsing fails
     */
    fun safeParseColor(colorString: String?): Color? {
        return try {
            colorString?.let { Color(AndroidColor.parseColor(it)) }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    /**
     * Safely parse a color string with fallback
     * @param colorString The color string to parse
     * @param fallbackColor The fallback color to use if parsing fails
     * @return Parsed Color or fallback if parsing fails
     */
    fun parseColorWithFallback(colorString: String?, fallbackColor: Color): Color {
        return safeParseColor(colorString) ?: fallbackColor
    }
}
