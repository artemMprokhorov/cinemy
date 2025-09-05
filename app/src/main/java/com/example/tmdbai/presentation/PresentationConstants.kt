package com.example.tmdbai.presentation

/**
 * Constants for presentation layer
 * Contains all hardcoded strings, integers, and other values used in presentation
 */
object PresentationConstants {
    
    // Default Values
    const val DEFAULT_MOVIE_ID = 0
    const val DEFAULT_PAGE_NUMBER = 1
    const val DEFAULT_RETRY_COUNT = 0
    const val DEFAULT_BOOLEAN_FALSE = false
    const val DEFAULT_BOOLEAN_TRUE = true
    const val DEFAULT_LONG_VALUE = 0L
    
    // Runtime Formatting
    const val MINUTES_PER_HOUR = 60
    const val RUNTIME_HOURS_FORMAT = "h"
    const val RUNTIME_MINUTES_FORMAT = "m"
    
    // Budget Formatting
    const val BUDGET_DIVISOR = 1_000_000
    const val BUDGET_CURRENCY_SYMBOL = "$"
    const val BUDGET_SUFFIX = "M"
    const val BUDGET_THRESHOLD = 0
    
    // Connection Status Messages
    const val MESSAGE_USING_DEMO_DATA = "Using demo data"
    const val MESSAGE_BACKEND_UNAVAILABLE = "Backend unavailable - showing demo data"
    const val MESSAGE_CONNECTED_TO_LIVE_DATA = "Connected to live data"
    const val MESSAGE_EMPTY = ""
    
    // Connection Status Detection
    const val BACKEND_UNAVAILABLE_KEYWORD = "backend unavailable"
    const val MOCK_KEYWORD = "mock"
    
    // Pagination
    const val PAGE_INCREMENT = 1
    const val PAGE_DECREMENT = 1
    
    // UI State Defaults
    const val DEFAULT_SHOW_FULL_DESCRIPTION = false
    const val DEFAULT_SHOW_PRODUCTION_DETAILS = false
    const val DEFAULT_HAS_MORE = true
    const val DEFAULT_CAN_RETRY = false
}
