package org.studioapp.cinemy.data.util

import android.content.Context

/**
 * Utility class for loading assets from Android assets directory
 *
 * This utility provides shared functionality for loading JSON and other asset files
 * from the app's assets folder. It implements safe asset loading with proper error
 * handling and resource management to prevent crashes during file operations.
 *
 * The class follows the singleton pattern as an object to ensure consistent
 * asset loading behavior across the application.
 *
 * @see android.content.Context.assets for Android asset management
 * @see kotlin.runCatching for error handling approach
 */
object AssetUtils {

    /**
     * Loads JSON content from Android assets directory
     *
     * This method safely reads JSON data from the app's assets folder using Android's
     * asset management system. It handles file I/O operations with proper resource management
     * and error handling to prevent crashes during asset loading.
     *
     * @param context Android context required for accessing the assets manager
     * @param fileName Name of the asset file to load (e.g., "mock_movies.json", "ui_config.json")
     * @return JSON string content if successfully loaded, null if file doesn't exist or loading fails
     *
     * @throws No exceptions are thrown - all errors are handled gracefully and return null
     *
     * @see android.content.Context.assets for asset management
     * @see kotlin.runCatching for error handling approach
     *
     * @sample
     * ```kotlin
     * val jsonContent = AssetUtils.loadJsonFromAssets(context, "mock_movies.json")
     * if (jsonContent != null) {
     *     // Process JSON content
     * } else {
     *     // Handle loading failure
     * }
     * ```
     */
    fun loadJsonFromAssets(context: Context, fileName: String): String? {
        return runCatching {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        }.getOrElse { e ->
            null
        }
    }
}
