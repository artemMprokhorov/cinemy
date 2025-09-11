package org.studioapp.cinemy.utils

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

/**
 * Utility class for handling Android version-specific features
 */
object VersionUtils {

    /**
     * Android version constants for better readability
     */
    object Versions {
        const val ANDROID_14 = Build.VERSION_CODES.UPSIDE_DOWN_CAKE // API 34
        const val ANDROID_13 = Build.VERSION_CODES.TIRAMISU // API 33
        const val ANDROID_12 = Build.VERSION_CODES.S_V2 // API 32
        const val ANDROID_11 = Build.VERSION_CODES.R // API 30
        const val ANDROID_10 = Build.VERSION_CODES.Q // API 29
        const val ANDROID_5 = Build.VERSION_CODES.LOLLIPOP // API 21 - Minimum for edge-to-edge
    }

    /**
     * Safely enables edge-to-edge display on supported Android versions
     * enableEdgeToEdge() is available from API 21+ (Android 5.0)
     *
     * @param activity The activity to enable edge-to-edge for
     */
    fun safeEnableEdgeToEdge(activity: ComponentActivity) {
        if (Build.VERSION.SDK_INT >= Versions.ANDROID_5) {
            activity.enableEdgeToEdge()
        }
    }
}

