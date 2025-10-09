package org.studioapp.cinemy.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.studioapp.cinemy.utils.DeviceUtils
import org.studioapp.cinemy.utils.getDeviceType
import org.studioapp.cinemy.utils.isFoldableDevice

/**
 * WindowInsets utilities for foldable devices
 * Provides proper insets handling for different device types
 */
object FoldableInsets {

    /**
     * Get appropriate window insets for the current device type
     */
    @Composable
    fun getWindowInsets(): WindowInsets {
        val deviceType = getDeviceType()
        val isFoldable = isFoldableDevice()

        return when {
            isFoldable -> {
                // For foldable devices, use safe drawing insets
                // This ensures content doesn't overlap with system UI
                WindowInsets.safeDrawing
            }

            deviceType == DeviceUtils.DeviceType.TABLET ||
                    deviceType == DeviceUtils.DeviceType.DESKTOP -> {
                // For tablets and desktop, use system bars
                WindowInsets.systemBars
            }

            else -> {
                // For phones, use system bars
                WindowInsets.systemBars
            }
        }
    }

    /**
     * Get display cutout insets for devices with notches
     */
    @Composable
    fun getDisplayCutoutInsets(): WindowInsets {
        return WindowInsets.displayCutout
    }

    /**
     * Get system bars insets
     */
    @Composable
    fun getSystemBarsInsets(): WindowInsets {
        return WindowInsets.systemBars
    }
}

/**
 * Modifier for applying appropriate window insets based on device type
 */
@Composable
fun Modifier.foldableWindowInsetsPadding(): Modifier {
    val windowInsets = FoldableInsets.getWindowInsets()
    return this.windowInsetsPadding(windowInsets)
}

/**
 * Modifier for applying safe drawing insets (recommended for foldable devices)
 */
@Composable
fun Modifier.safeDrawingPadding(): Modifier {
    return this.windowInsetsPadding(WindowInsets.safeDrawing)
}

/**
 * Modifier for applying system bars insets
 */
@Composable
fun Modifier.systemBarsPadding(): Modifier {
    return this.windowInsetsPadding(WindowInsets.systemBars)
}

/**
 * Modifier for applying display cutout insets
 */
@Composable
fun Modifier.displayCutoutPadding(): Modifier {
    return this.windowInsetsPadding(WindowInsets.displayCutout)
}

/**
 * Modifier for consuming window insets
 * Use this when you want to handle insets manually
 */
@Composable
fun Modifier.consumeFoldableInsets(): Modifier {
    val windowInsets = FoldableInsets.getWindowInsets()
    return this.consumeWindowInsets(windowInsets)
}

/**
 * Get padding values for window insets
 */
@Composable
fun getFoldableInsetsPadding(): androidx.compose.foundation.layout.PaddingValues {
    val windowInsets = FoldableInsets.getWindowInsets()
    return windowInsets.asPaddingValues()
}

/**
 * Get safe drawing padding values
 */
@Composable
fun getSafeDrawingPadding(): androidx.compose.foundation.layout.PaddingValues {
    return WindowInsets.safeDrawing.asPaddingValues()
}

/**
 * Get system bars padding values
 */
@Composable
fun getSystemBarsPadding(): androidx.compose.foundation.layout.PaddingValues {
    return WindowInsets.systemBars.asPaddingValues()
}

/**
 * Modifier for handling foldable device specific insets
 * Automatically adjusts based on device type
 */
@Composable
fun Modifier.adaptiveInsetsPadding(): Modifier {
    val deviceType = getDeviceType()
    val isFoldable = isFoldableDevice()

    return when {
        isFoldable -> {
            // For foldable devices, use safe drawing insets
            this.safeDrawingPadding()
        }

        deviceType == DeviceUtils.DeviceType.TABLET ||
                deviceType == DeviceUtils.DeviceType.DESKTOP -> {
            // For tablets and desktop, use system bars
            this.systemBarsPadding()
        }

        else -> {
            // For phones, use system bars
            this.systemBarsPadding()
        }
    }
}

/**
 * Modifier for handling only specific sides of window insets
 */
@Composable
fun Modifier.selectiveInsetsPadding(
    sides: WindowInsetsSides = WindowInsetsSides.Horizontal + WindowInsetsSides.Vertical
): Modifier {
    val windowInsets = FoldableInsets.getWindowInsets()
    return this.windowInsetsPadding(windowInsets.only(sides))
}

/**
 * Modifier for handling top and bottom insets only
 */
@Composable
fun Modifier.verticalInsetsPadding(): Modifier {
    return this.selectiveInsetsPadding(WindowInsetsSides.Vertical)
}

/**
 * Modifier for handling left and right insets only
 */
@Composable
fun Modifier.horizontalInsetsPadding(): Modifier {
    return this.selectiveInsetsPadding(WindowInsetsSides.Horizontal)
}
