package org.studioapp.cinemy.ui.constants

/**
 * UI constants for content descriptions, test tags, and other UI-related strings
 */
object UiConstants {
    
    // Content Descriptions
    object ContentDescriptions {
        const val LOADING_MOVIES = "Loading movies, please wait"
        const val LOADING_MOVIE_DETAILS = "Loading movie details, please wait"
        const val ERROR_LOADING_MOVIES_RETRY = "Error loading movies, pull down to retry"
        const val ERROR_LOADING_MOVIE_DETAILS_RETRY = "Error loading movie details, pull down to retry"
        const val ERROR_FAILED_LOAD_MOVIES = "Error: Failed to load movies"
        const val MOVIES_LIST_SCREEN = "Movies list screen"
        const val PULL_DOWN_RETRY_MOVIES = "Pull down to retry loading movies"
    }
    
    // Test Tags
    object TestTags {
        const val LOADING_TEXT = "loading_text"
        const val LOADING_INDICATOR = "loading_indicator"
        const val ERROR_TITLE = "error_title"
        const val ERROR_SUBTITLE = "error_subtitle"
        const val RETRY_INSTRUCTION = "retry_instruction"
        const val SENTIMENT_ANALYSIS_CARD = "sentiment_analysis_card"
        const val SENTIMENT_TITLE = "sentiment_title"
    }
}
