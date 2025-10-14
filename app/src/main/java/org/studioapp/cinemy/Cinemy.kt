package org.studioapp.cinemy

import android.app.Application
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.N
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.studioapp.cinemy.data.di.dataModule
import org.studioapp.cinemy.di.mlModule
import org.studioapp.cinemy.ml.SentimentAnalyzer
import org.studioapp.cinemy.navigation.AppNavigation
import org.studioapp.cinemy.presentation.di.presentationModule
import org.studioapp.cinemy.ui.theme.CinemyTheme
import org.studioapp.cinemy.utils.DeviceUtils
import org.studioapp.cinemy.utils.DeviceUtils.DeviceType.DESKTOP
import org.studioapp.cinemy.utils.DeviceUtils.DeviceType.FOLDABLE
import org.studioapp.cinemy.utils.DeviceUtils.getDeviceType
import org.studioapp.cinemy.utils.VersionUtils.safeEnableEdgeToEdge

class CinemyApplication : Application() {

    private val sentimentAnalyzer: SentimentAnalyzer by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@CinemyApplication)
            modules(dataModule, presentationModule, mlModule)
        }

        // Initialize ML analyzer
        CoroutineScope(Dispatchers.IO).launch {
            sentimentAnalyzer.initialize()
        }
    }
}

/**
 * Main activity for Cinemy app
 * Handles device configuration changes and foldable device support
 * Manages dependency injection and app initialization
 */
class MainActivity : ComponentActivity() {

    private var currentDeviceType: DeviceUtils.DeviceType? = null

    /**
     * Initialize the main activity and set up the app
     * @param savedInstanceState Saved instance state
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Safely enable edge-to-edge display only on supported Android versions
        // This prevents UI issues on older devices that don't support this feature
        safeEnableEdgeToEdge(this)

        // Initialize device type detection
        currentDeviceType = getDeviceType(this)

        setContent {
            CinemyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = White
                ) {
                    val navController = rememberNavController()
                    AppNavigation(navController = navController)
                }
            }
        }
    }

    /**
     * Handle configuration changes for foldable devices
     * This is called when the device is folded/unfolded or orientation changes
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Update device type detection
        val newDeviceType = getDeviceType(this)

        // Log configuration change for debugging
        if (newDeviceType != currentDeviceType) {
            currentDeviceType = newDeviceType
            // Handle device type change if needed
            when (newDeviceType) {
                FOLDABLE -> {
                    // Optimize for foldable device
                    optimizeForFoldableDevice()
                }

                DESKTOP -> {
                    // Optimize for desktop
                    optimizeForDesktop()
                }

                else -> {
                    // Tablet/Phone optimization handled by UI components
                }
            }
        }

        // Handle orientation changes
        val isLandscape = newConfig.orientation == ORIENTATION_LANDSCAPE
        if (isLandscape) {
            // Optimize for landscape orientation
            handleOrientationChange(true)
        } else {
            // Optimize for portrait orientation
            handleOrientationChange(false)
        }
    }


    /**
     * Handle orientation changes
     * @param isLandscape Whether the device is in landscape orientation
     */
    private fun handleOrientationChange(isLandscape: Boolean) {
        // Use cached device type to avoid redundant calls
        val deviceType = currentDeviceType ?: getDeviceType(this)

        when (deviceType) {
            FOLDABLE -> {
                // For foldable devices, always optimize regardless of orientation
                // The device might be folded/unfolded or just rotated
                optimizeForFoldableDevice()
            }

            DESKTOP -> {
                // Desktop orientation changes are rare but handled
                optimizeForDesktop()
            }

            else -> {
                // For tablets and phones, UI components handle orientation changes automatically
                // No additional optimization needed
            }
        }
    }

    /**
     * Set up orientation support for devices that support multi-window
     */
    private fun setupOrientationSupport() {
        if (SDK_INT >= N) {
            setRequestedOrientation(SCREEN_ORIENTATION_UNSPECIFIED)
        }
    }

    /**
     * Optimize UI for foldable devices
     */
    private fun optimizeForFoldableDevice() {
        // Enable multi-window mode support for foldable devices
        setupOrientationSupport()
    }

    /**
     * Optimize UI for desktop devices
     */
    private fun optimizeForDesktop() {
        // Enable window resizing for desktop devices
        setupOrientationSupport()
    }

}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    CinemyTheme {
        val navController = rememberNavController()
        AppNavigation(navController = navController)
    }
}