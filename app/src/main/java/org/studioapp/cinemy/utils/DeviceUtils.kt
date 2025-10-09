package org.studioapp.cinemy.utils

import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Utility class for device type detection and foldable device support
 */
object DeviceUtils {

    /**
     * Device type enumeration
     */
    enum class DeviceType {
        PHONE,
        TABLET,
        FOLDABLE,
        DESKTOP
    }

    /**
     * Screen size categories
     */
    enum class ScreenSize {
        SMALL,      // < 600dp
        MEDIUM,     // 600dp - 840dp
        LARGE,      // 840dp - 1200dp
        EXTRA_LARGE // > 1200dp
    }

    /**
     * Determines if the device is a foldable device
     */
    fun isFoldableDevice(context: Context): Boolean {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        // Check for foldable characteristics
        val screenWidth = displayMetrics.widthPixels / displayMetrics.density
        val screenHeight = displayMetrics.heightPixels / displayMetrics.density

        // Foldable devices typically have aspect ratios that change significantly
        val aspectRatio = maxOf(screenWidth, screenHeight) / minOf(screenWidth, screenHeight)

        return aspectRatio > 2.0f || screenWidth > 600f || screenHeight > 600f
    }

    /**
     * Gets the device type based on screen size
     */
    fun getDeviceType(context: Context): DeviceType {
        val configuration = context.resources.configuration
        val screenWidthDp = configuration.screenWidthDp
        val screenHeightDp = configuration.screenHeightDp

        return when {
            isFoldableDevice(context) -> DeviceType.FOLDABLE
            screenWidthDp >= 1200 -> DeviceType.DESKTOP
            screenWidthDp >= 600 -> DeviceType.TABLET
            else -> DeviceType.PHONE
        }
    }

    /**
     * Gets the screen size category
     */
    fun getScreenSize(context: Context): ScreenSize {
        val configuration = context.resources.configuration
        val screenWidthDp = configuration.screenWidthDp

        return when {
            screenWidthDp >= 1200 -> ScreenSize.EXTRA_LARGE
            screenWidthDp >= 840 -> ScreenSize.LARGE
            screenWidthDp >= 600 -> ScreenSize.MEDIUM
            else -> ScreenSize.SMALL
        }
    }

    /**
     * Determines if the device is in landscape mode
     */
    fun isLandscape(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    /**
     * Determines if the device supports dual pane layout
     * For foldable devices, checks if they're unfolded (large screen)
     */
    fun supportsDualPane(context: Context): Boolean {
        val deviceType = getDeviceType(context)
        val screenSize = getScreenSize(context)
        val configuration = context.resources.configuration
        val screenWidthDp = configuration.screenWidthDp

        return when (deviceType) {
            DeviceType.FOLDABLE -> {
                // Foldable devices only support dual pane when unfolded (wide screen)
                screenWidthDp >= 840 // Large screen when unfolded
            }
            DeviceType.TABLET, DeviceType.DESKTOP -> true
            DeviceType.PHONE -> {
                // Phones only support dual pane on very large screens
                screenSize == ScreenSize.EXTRA_LARGE
            }
        }
    }

    /**
     * Gets optimal column count for grid layouts
     */
    fun getOptimalColumnCount(context: Context): Int {
        val deviceType = getDeviceType(context)
        val screenSize = getScreenSize(context)

        return when {
            deviceType == DeviceType.FOLDABLE && screenSize == ScreenSize.EXTRA_LARGE -> 3
            deviceType == DeviceType.TABLET || deviceType == DeviceType.DESKTOP -> 2
            screenSize == ScreenSize.LARGE -> 2
            else -> 1
        }
    }

    /**
     * Gets optimal spacing for different device types
     */
    fun getOptimalSpacing(context: Context): Dp {
        val deviceType = getDeviceType(context)

        return when (deviceType) {
            DeviceType.FOLDABLE, DeviceType.TABLET, DeviceType.DESKTOP -> 24.dp
            DeviceType.PHONE -> 16.dp
        }
    }
}

/**
 * Composable function to get device type in Compose
 */
@Composable
fun getDeviceType(): DeviceUtils.DeviceType {
    val context = LocalContext.current
    return DeviceUtils.getDeviceType(context)
}

/**
 * Composable function to get screen size in Compose
 */
@Composable
fun getScreenSize(): DeviceUtils.ScreenSize {
    val context = LocalContext.current
    return DeviceUtils.getScreenSize(context)
}

/**
 * Composable function to check if device supports dual pane
 */
@Composable
fun supportsDualPane(): Boolean {
    val context = LocalContext.current
    return DeviceUtils.supportsDualPane(context)
}

/**
 * Composable function to check if device is foldable
 */
@Composable
fun isFoldableDevice(): Boolean {
    val context = LocalContext.current
    return DeviceUtils.isFoldableDevice(context)
}

/**
 * Composable function to get optimal column count
 */
@Composable
fun getOptimalColumnCount(): Int {
    val context = LocalContext.current
    return DeviceUtils.getOptimalColumnCount(context)
}

/**
 * Composable function to get optimal spacing
 */
@Composable
fun getOptimalSpacing(): Dp {
    val context = LocalContext.current
    return DeviceUtils.getOptimalSpacing(context)
}
