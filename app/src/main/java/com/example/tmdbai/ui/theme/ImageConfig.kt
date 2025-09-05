package com.example.tmdbai.ui.theme

import com.example.tmdbai.BuildConfig

/**
 * Image configuration constants for UI layer
 * Contains URLs and configuration values that should not be hardcoded
 */
object ImageConfig {

    /**
     * TMDB Image API configuration
     */
    object TmdbImages {
        val BASE_URL = BuildConfig.TMDB_IMAGE_BASE_URL
        const val BACKDROP_SIZE = "w500"
        const val POSTER_SIZE = "w500"
        const val PROFILE_SIZE = "w185"
        const val LOGO_SIZE = "w92"
    }

    /**
     * Build image URL for TMDB images
     * @param imagePath The image path from TMDB API
     * @param size The image size (e.g., "w500")
     * @return Complete image URL
     */
    fun buildImageUrl(imagePath: String?, size: String = TmdbImages.BACKDROP_SIZE): String? {
        return imagePath?.let { "${TmdbImages.BASE_URL}$size$it" }
    }

    /**
     * Build backdrop image URL
     * @param imagePath The backdrop path from TMDB API
     * @return Complete backdrop image URL
     */
    fun buildBackdropUrl(imagePath: String?): String? {
        return buildImageUrl(imagePath, TmdbImages.BACKDROP_SIZE)
    }

    /**
     * Build poster image URL
     * @param imagePath The poster path from TMDB API
     * @return Complete poster image URL
     */
    fun buildPosterUrl(imagePath: String?): String? {
        return buildImageUrl(imagePath, TmdbImages.POSTER_SIZE)
    }
}
