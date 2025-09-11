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

/**
 * Checks if the current device supports edge-to-edge display
 *
 * @return true if edge-to-edge is supported, false otherwise
 */
fun supportsEdgeToEdge(): Boolean {
    return Build.VERSION.SDK_INT >= VersionUtils.Versions.ANDROID_5
}

/**
 * Gets a human-readable Android version name
 *
 * @return String representation of the Android version
 */
fun getAndroidVersionName(): String {
    return when (Build.VERSION.SDK_INT) {
        Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> "Android 14"
        Build.VERSION_CODES.TIRAMISU -> "Android 13"
        Build.VERSION_CODES.S_V2 -> "Android 12"
        Build.VERSION_CODES.R -> "Android 11"
        Build.VERSION_CODES.Q -> "Android 10"
        else -> "Android ${Build.VERSION.RELEASE}"
    }
}

/**
 * Gets the Android API level
 *
 * @return API level as integer
 */
fun getApiLevel(): Int {
    return Build.VERSION.SDK_INT
}
