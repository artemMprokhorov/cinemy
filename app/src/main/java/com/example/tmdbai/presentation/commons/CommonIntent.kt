package com.example.tmdbai.presentation.commons

/**
 * Common Intents for the application
 * Base interface for all user interactions
 */
interface CommonIntent {
    object Retry : CommonIntent
    object Refresh : CommonIntent
    object BackPressed : CommonIntent
}
