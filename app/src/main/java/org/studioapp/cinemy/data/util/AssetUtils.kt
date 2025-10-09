package org.studioapp.cinemy.data.util

import android.content.Context

/**
 * Utility class for loading assets
 * Provides shared functionality for loading JSON and other asset files
 */
object AssetUtils {

    /**
     * Loads JSON content from assets
     * @param context Android context
     * @param fileName Name of the asset file
     * @return JSON string content or null if loading fails
     */
    fun loadJsonFromAssets(context: Context, fileName: String): String? {
        return runCatching {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        }.getOrElse { e ->
            null
        }
    }
}
