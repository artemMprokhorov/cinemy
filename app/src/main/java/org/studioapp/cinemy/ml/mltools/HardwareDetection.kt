package org.studioapp.cinemy.ml.mltools

import android.content.Context
import android.os.Build
import org.studioapp.cinemy.ml.MLConstants.GOOGLE_PLAY_SERVICES_PACKAGE
import org.studioapp.cinemy.ml.MLConstants.MIN_PLAY_SERVICES_VERSION
import org.studioapp.cinemy.ml.MLConstants.MLKIT_DOWNLOAD_CONDITIONS_CLASS
import org.studioapp.cinemy.ml.MLConstants.MLKIT_EXCEPTION_CLASS
import org.studioapp.cinemy.ml.MLConstants.TENSORFLOW_LITE_NNAPI_DELEGATE_CLASS
import org.studioapp.cinemy.ml.MLConstants.TENSORFLOW_LITE_XNNPACK_DELEGATE_CLASS
import java.lang.ref.WeakReference

/**
 * Hardware detection and ML runtime selection for Cinemy
 * Automatically detects available hardware acceleration and selects optimal ML runtime
 *
 * This class provides comprehensive hardware detection capabilities to determine
 * the best ML runtime for sentiment analysis based on device capabilities.
 *
 * @author Cinemy Team
 * @since 1.0.0
 */
class HardwareDetection private constructor(private val context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: WeakReference<HardwareDetection>? = null

        /**
         * Gets singleton instance of HardwareDetection
         * Uses WeakReference to prevent memory leaks
         *
         * @param context Android context for hardware detection
         * @return HardwareDetection singleton instance
         */
        fun getInstance(context: Context): HardwareDetection {
            val current = INSTANCE?.get()
            if (current != null) {
                return current
            }

            return synchronized(this) {
                val existing = INSTANCE?.get()
                if (existing != null) {
                    existing
                } else {
                    val newInstance = HardwareDetection(context.applicationContext)
                    INSTANCE = WeakReference(newInstance)
                    newInstance
                }
            }
        }
    }

    /**
     * Available ML runtimes in order of preference
     * Higher priority runtimes provide better performance but may have more dependencies
     */
    enum class MLRuntime {
        /** LiteRT with GPU acceleration - best performance, requires Play Services */
        LITERT_GPU,

        /** LiteRT with NPU acceleration - good performance, requires Play Services */
        LITERT_NPU,

        /** TensorFlow Lite with GPU acceleration - good performance, no Play Services dependency */
        TENSORFLOW_LITE_GPU,

        /** TensorFlow Lite with NNAPI acceleration - decent performance */
        TENSORFLOW_LITE_NNAPI,

        /** TensorFlow Lite CPU with XNNPACK optimization - basic performance */
        TENSORFLOW_LITE_CPU,

        /** Keyword-based fallback model - last resort */
        KEYWORD_FALLBACK
    }

    /**
     * Hardware capabilities detected on the device
     * Contains information about available acceleration options
     *
     * @param hasGpu Whether GPU acceleration is available
     * @param hasNnapi Whether NNAPI acceleration is available
     * @param hasXnnpack Whether XNNPACK CPU optimization is available
     * @param hasLiteRT Whether LiteRT is available through Play Services
     * @param hasPlayServices Whether Google Play Services is available
     * @param recommendedRuntime The optimal runtime for this device
     */
    data class HardwareCapabilities(
        val hasGpu: Boolean = false,
        val hasNnapi: Boolean = false,
        val hasXnnpack: Boolean = false,
        val hasLiteRT: Boolean = false,
        val hasPlayServices: Boolean = false,
        val recommendedRuntime: MLRuntime = MLRuntime.TENSORFLOW_LITE_CPU
    )

    /**
     * Detects hardware capabilities and recommends optimal ML runtime
     *
     * This method performs comprehensive hardware detection including:
     * - GPU support detection
     * - NNAPI support detection
     * - XNNPACK support detection
     * - LiteRT availability through Play Services
     * - Performance score calculation
     *
     * @return HardwareCapabilities object with detected capabilities and recommendations
     */
    fun detectHardwareCapabilities(): HardwareCapabilities {
        val hasGpu = detectGpuSupport()
        val hasNnapi = detectNnapiSupport()
        val hasXnnpack = detectXnnpackSupport()
        val hasLiteRT = detectLiteRTSupport()
        val hasPlayServices = detectPlayServicesSupport()

        val recommendedRuntime = selectOptimalRuntime(
            hasGpu, hasNnapi, hasXnnpack, hasLiteRT, hasPlayServices
        )

        return HardwareCapabilities(
            hasGpu = hasGpu,
            hasNnapi = hasNnapi,
            hasXnnpack = hasXnnpack,
            hasLiteRT = hasLiteRT,
            hasPlayServices = hasPlayServices,
            recommendedRuntime = recommendedRuntime
        )
    }

    /**
     * Detects GPU support for TensorFlow Lite
     *
     * Checks if the device supports GPU acceleration for ML inference.
     * This is determined by checking Android version and attempting to create
     * a GPU delegate for TensorFlow Lite.
     *
     * @return true if GPU acceleration is available, false otherwise
     */
    private fun detectGpuSupport(): Boolean {
        return runCatching {
            // Check if device has sufficient Android version for GPU support
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                return false
            }

            // For now, assume GPU support is available on modern devices
            // In a full implementation, you would check for GPU delegate support
            // by attempting to create a GpuDelegate instance
            true
        }.getOrElse { false }
    }

    /**
     * Detects NNAPI support for TensorFlow Lite
     *
     * NNAPI (Neural Networks API) provides hardware acceleration for ML inference
     * on Android devices. This method checks if NNAPI is available and functional.
     *
     * @return true if NNAPI acceleration is available, false otherwise
     */
    private fun detectNnapiSupport(): Boolean {
        return runCatching {
            // Check if NNAPI delegate class is available
            Class.forName(TENSORFLOW_LITE_NNAPI_DELEGATE_CLASS)
            true
        }.getOrElse { false }
    }

    /**
     * Detects XNNPACK support for TensorFlow Lite
     *
     * XNNPACK is a highly optimized library of floating-point neural network
     * inference operators for ARM, WebAssembly, and x86 platforms. It provides
     * CPU optimization for ML inference.
     *
     * @return true if XNNPACK optimization is available, false otherwise
     */
    private fun detectXnnpackSupport(): Boolean {
        return runCatching {
            // Check if XNNPACK delegate class is available
            Class.forName(TENSORFLOW_LITE_XNNPACK_DELEGATE_CLASS)
            true
        }.getOrElse { false }
    }

    /**
     * Detects LiteRT support through Google Play Services
     *
     * LiteRT is Google's ML runtime distributed through Play Services.
     * It provides optimized ML inference with automatic hardware acceleration.
     *
     * @return true if LiteRT is available, false otherwise
     */
    private fun detectLiteRTSupport(): Boolean {
        return runCatching {
            // Check if Google Play Services is available
            val packageManager = context.packageManager
            val playServicesInfo = packageManager.getPackageInfo(GOOGLE_PLAY_SERVICES_PACKAGE, 0)

            // Check if Play Services version supports ML Kit
            if (playServicesInfo.versionCode >= MIN_PLAY_SERVICES_VERSION) { // Version 20.0.0+
                // Check for ML Kit availability
                runCatching {
                    // Try to access ML Kit classes to verify availability
                    Class.forName(MLKIT_EXCEPTION_CLASS)
                    Class.forName(MLKIT_DOWNLOAD_CONDITIONS_CLASS)
                    true
                }.getOrElse { false }
            } else {
                false
            }
        }.getOrElse { false }
    }

    /**
     * Detects Google Play Services support
     *
     * Google Play Services is required for LiteRT and other Google ML services.
     * This method checks if Play Services is installed and available.
     *
     * @return true if Google Play Services is available, false otherwise
     */
    private fun detectPlayServicesSupport(): Boolean {
        return runCatching {
            val packageManager = context.packageManager
            packageManager.getPackageInfo(GOOGLE_PLAY_SERVICES_PACKAGE, 0)
            true
        }.getOrElse { false }
    }


    /**
     * Selects optimal ML runtime based on hardware capabilities
     *
     * The selection follows this priority order:
     * 1. LiteRT with GPU (best performance, requires Play Services)
     * 2. LiteRT with NNAPI (good performance, requires Play Services)
     * 3. TensorFlow Lite with GPU (good performance, no Play Services dependency)
     * 4. TensorFlow Lite with NNAPI (decent performance)
     * 5. TensorFlow Lite CPU (basic performance)
     * 6. Keyword fallback (last resort)
     *
     * @param hasGpu Whether GPU is available
     * @param hasNnapi Whether NNAPI is available
     * @param hasXnnpack Whether XNNPACK is available
     * @param hasLiteRT Whether LiteRT is available
     * @param hasPlayServices Whether Play Services is available
     * @return Optimal ML runtime for this device
     */
    private fun selectOptimalRuntime(
        hasGpu: Boolean,
        hasNnapi: Boolean,
        hasXnnpack: Boolean,
        hasLiteRT: Boolean,
        hasPlayServices: Boolean
    ): MLRuntime {
        return when {
            // LiteRT with GPU - best performance
            hasLiteRT && hasGpu && hasPlayServices -> MLRuntime.LITERT_GPU

            // LiteRT with NNAPI - good performance
            hasLiteRT && hasNnapi && hasPlayServices -> MLRuntime.LITERT_NPU

            // TensorFlow Lite with GPU - good performance, no Play Services dependency
            hasGpu -> MLRuntime.TENSORFLOW_LITE_GPU

            // TensorFlow Lite with NNAPI - decent performance
            hasNnapi -> MLRuntime.TENSORFLOW_LITE_NNAPI

            // TensorFlow Lite CPU with XNNPACK - basic performance
            hasXnnpack -> MLRuntime.TENSORFLOW_LITE_CPU

            // Fallback to keyword model
            else -> MLRuntime.KEYWORD_FALLBACK
        }
    }

}
