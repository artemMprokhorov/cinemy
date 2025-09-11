package org.studioapp.cinemy.data.util

import androidx.compose.ui.graphics.Color

/**
 * Test utility for color parsing that can be mocked
 */
object TestColorUtils {
    /**
     * Parses a color string to a Compose Color for testing
     * This version doesn't use Android's Color.parseColor which is not available in unit tests
     * @param colorString The color string to parse (e.g., "#FF0000")
     * @return The parsed Compose Color
     */
    fun parseColor(colorString: String): Color {
        // Remove the # prefix if present
        val cleanColor = if (colorString.startsWith("#")) {
            colorString.substring(1)
        } else {
            colorString
        }

        // Parse the hex color and ensure alpha channel is set to 1.0 (0xFF)
        // Format: ARGB (Alpha, Red, Green, Blue)
        val colorValue = when (cleanColor.length) {
            6 -> ("FF" + cleanColor).toLong(16) // Add alpha at the beginning
            8 -> cleanColor.toLong(16) // Already has alpha
            else -> cleanColor.toLong(16) // Fallback
        }

        return Color(colorValue)
    }
}
