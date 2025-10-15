package org.studioapp.cinemy.utils

import android.content.Context
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
     * Device type enumeration for categorizing Android devices
     * 
     * @property PHONE Standard smartphone device
     * @property TABLET Tablet device with larger screen
     * @property FOLDABLE Foldable device with flexible screen configuration
     * @property DESKTOP Desktop mode device (Chrome OS, Samsung DeX)
     */
    enum class DeviceType {
        PHONE,
        TABLET,
        FOLDABLE,
        DESKTOP
    }

    /**
     * Screen size categories based on screen width in density-independent pixels
     * 
     * @property SMALL Screen width < 600dp (phones and small devices)
     * @property MEDIUM Screen width 600dp - 840dp (large phones and small tablets)
     * @property LARGE Screen width 840dp - 1200dp (tablets and small foldable devices)
     * @property EXTRA_LARGE Screen width > 1200dp (large tablets and unfolded foldable devices)
     */
    enum class ScreenSize {
        SMALL,      // < 600dp
        MEDIUM,     // 600dp - 840dp
        LARGE,      // 840dp - 1200dp
        EXTRA_LARGE // > 1200dp
    }

    /**
     * Determines if the device is a foldable device based on screen characteristics
     * 
     * Checks for foldable device characteristics including aspect ratio and screen dimensions.
     * Foldable devices typically have aspect ratios > 2.0 or screen dimensions > 600dp.
     * 
     * @param context Android context for accessing system services
     * @return true if the device is detected as foldable, false otherwise
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
     * Gets the device type based on screen characteristics and configuration
     * 
     * Determines device type using screen width and foldable detection.
     * Priority: FOLDABLE > DESKTOP (>=1200dp) > TABLET (>=600dp) > PHONE.
     * 
     * @param context Android context for accessing system services
     * @return DeviceType enum indicating the detected device type
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
     * Gets the screen size category based on screen width in density-independent pixels
     * 
     * Categorizes screen size using standard Android breakpoints:
     * SMALL (<600dp), MEDIUM (600-840dp), LARGE (840-1200dp), EXTRA_LARGE (>1200dp).
     * 
     * @param context Android context for accessing system services
     * @return ScreenSize enum indicating the detected screen size category
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
     * Determines if the device supports dual pane layout based on device type and screen size
     * 
     * Dual pane support logic:
     * - FOLDABLE: Only when unfolded (screen width >= 840dp)
     * - TABLET/DESKTOP: Always supported
     * - PHONE: Only on extra large screens (>=1200dp)
     * 
     * @param context Android context for accessing system services
     * @return true if dual pane layout is supported, false otherwise
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
     * Gets optimal spacing based on device type for responsive UI design
     * 
     * Returns device-specific spacing values:
     * - Large devices (FOLDABLE, TABLET, DESKTOP): 24dp for better visual hierarchy
     * - Phones: 16dp for compact layout
     * 
     * @param context Android context for accessing system services
     * @return Dp value representing optimal spacing for the device type
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
 * Composable function to get device type using LocalContext
 * 
 * Provides reactive device type detection in Compose UI.
 * Automatically updates when device configuration changes.
 * 
 * @return DeviceType enum indicating the current device type
 */
@Composable
fun getDeviceType(): DeviceUtils.DeviceType {
    val context = LocalContext.current
    return DeviceUtils.getDeviceType(context)
}


/**
 * Composable function to check if device supports dual pane layout
 * 
 * Provides reactive dual pane support detection in Compose UI.
 * Automatically updates when device configuration changes.
 * 
 * @return true if dual pane layout is supported, false otherwise
 */
@Composable
fun supportsDualPane(): Boolean {
    val context = LocalContext.current
    return DeviceUtils.supportsDualPane(context)
}


/**
 * Composable function to get optimal spacing for responsive UI design
 * 
 * Provides reactive spacing values in Compose UI.
 * Automatically updates when device configuration changes.
 * 
 * @return Dp value representing optimal spacing for the current device type
 */
@Composable
fun getOptimalSpacing(): Dp {
    val context = LocalContext.current
    return DeviceUtils.getOptimalSpacing(context)
}
