package org.studioapp.cinemy.utils

import androidx.compose.ui.graphics.Color
import android.graphics.Color as AndroidColor

/**
 * Utility class for color parsing that can be mocked in tests.
 *
 * This object provides static methods for converting color strings to Compose Color objects.
 * It serves as a bridge between Android's color parsing and Compose's color system.
 */
object ColorUtils {
    /**
     * Parses a color string to a Compose Color object.
     *
     * Supports various color formats including hex colors (e.g., "#FF0000", "#FF0000FF"),
     * named colors (e.g., "red", "blue"), and RGB values.
     *
     * @param colorString The color string to parse. Can be in hex format (#RRGGBB or #AARRGGBB),
     *                    named color, or RGB format.
     * @return The parsed Compose Color object.
     * @throws IllegalArgumentException if the color string is invalid or cannot be parsed.
     */
    fun parseColor(colorString: String): Color {
        return Color(AndroidColor.parseColor(colorString))
    }
}