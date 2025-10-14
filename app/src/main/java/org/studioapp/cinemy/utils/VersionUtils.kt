package org.studioapp.cinemy.utils

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.LOLLIPOP
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

/**
 * Utility class for handling Android version-specific features
 * Provides safe access to version-dependent APIs
 */
object VersionUtils {

    /**
     * Android version constants for better readability
     * Centralizes version-specific constants for maintainability
     */
    object Versions {
        const val ANDROID_5 = LOLLIPOP // API 21 - Minimum for edge-to-edge
    }

    /**
     * Safely enables edge-to-edge display on supported Android versions
     * enableEdgeToEdge() is available from API 21+ (Android 5.0)
     *
     * @param activity The activity to enable edge-to-edge for
     */
    fun safeEnableEdgeToEdge(activity: ComponentActivity) {
        if (SDK_INT >= Versions.ANDROID_5) {
            activity.enableEdgeToEdge()
        }
    }
}

