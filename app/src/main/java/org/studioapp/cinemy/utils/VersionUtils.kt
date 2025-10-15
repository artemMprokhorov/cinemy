package org.studioapp.cinemy.utils

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.LOLLIPOP
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

/**
 * Utility class for handling Android version-specific features.
 * Provides safe access to version-dependent APIs with proper version checking.
 */
object VersionUtils {

    /**
     * Android version constants for better readability and maintainability.
     * Centralizes version-specific constants to avoid magic numbers.
     */
    object Versions {
        /**
         * Android 5.0 (API 21) - Minimum version for edge-to-edge support.
         * Used as a threshold for enabling modern UI features.
         */
        const val ANDROID_5 = LOLLIPOP // API 21 - Minimum for edge-to-edge
    }

    /**
     * Safely enables edge-to-edge display on supported Android versions.
     * Only enables edge-to-edge on Android 5.0+ (API 21+) to prevent crashes.
     *
     * @param activity The activity to enable edge-to-edge for
     */
    fun safeEnableEdgeToEdge(activity: ComponentActivity) {
        if (SDK_INT >= Versions.ANDROID_5) {
            activity.enableEdgeToEdge()
        }
    }
}

