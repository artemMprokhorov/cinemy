package org.studioapp.cinemy.presentation

/**
 * Constants for presentation layer
 * Contains all hardcoded strings, integers, and other values used in presentation
 * Centralizes presentation-related constants for maintainability
 */
object PresentationConstants {

    /** Default values for presentation layer */
    
    /**
     * Default movie ID used when no specific movie is selected
     */
    const val DEFAULT_MOVIE_ID = 0
    
    /**
     * Default page number for pagination
     */
    const val DEFAULT_PAGE_NUMBER = 1
    
    /**
     * Default retry count for failed operations
     */
    const val DEFAULT_RETRY_COUNT = 0
    
    /**
     * Default boolean false value for UI state
     */
    const val DEFAULT_BOOLEAN_FALSE = false
    
    /**
     * Default boolean true value for UI state
     */
    const val DEFAULT_BOOLEAN_TRUE = true

    /** Runtime formatting constants */
    
    /**
     * Number of minutes in one hour for runtime calculations
     */
    const val MINUTES_PER_HOUR = 60
    
    /**
     * Format string for displaying hours in runtime
     */
    const val RUNTIME_HOURS_FORMAT = "h"
    
    /**
     * Format string for displaying minutes in runtime
     */
    const val RUNTIME_MINUTES_FORMAT = "m"

    /** Budget formatting constants */
    
    /**
     * Divisor for converting budget values to millions
     */
    const val BUDGET_DIVISOR = 1_000_000
    
    /**
     * Currency symbol for budget display
     */
    const val BUDGET_CURRENCY_SYMBOL = "$"
    
    /**
     * Suffix for million values in budget display
     */
    const val BUDGET_SUFFIX = "M"
    
    /**
     * Threshold value for budget calculations
     */
    const val BUDGET_THRESHOLD = 0

    /** Connection status messages */
    
    /**
     * Message displayed when using demo data
     */
    const val MESSAGE_USING_DEMO_DATA = "Using demo data"
    
    /**
     * Message displayed when backend is unavailable
     */
    const val MESSAGE_BACKEND_UNAVAILABLE = "Backend unavailable - showing demo data"
    
    /**
     * Message displayed when connected to live data
     */
    const val MESSAGE_CONNECTED_TO_LIVE_DATA = "Connected to live data"
    
    /**
     * Empty message string
     */
    const val MESSAGE_EMPTY = ""

    /** Connection status detection keywords */
    
    /**
     * Keyword used to detect backend unavailability
     */
    const val BACKEND_UNAVAILABLE_KEYWORD = "backend unavailable"
    
    /**
     * Keyword used to detect mock data usage
     */
    const val MOCK_KEYWORD = "mock"

    /** Pagination constants */
    
    /**
     * Value to increment page number
     */
    const val PAGE_INCREMENT = 1
    
    /**
     * Value to decrement page number
     */
    const val PAGE_DECREMENT = 1

    /** UI state default values */
    
    /**
     * Default value indicating if more content is available
     */
    const val DEFAULT_HAS_MORE = true
    
    /**
     * Default value indicating if retry is possible
     */
    const val DEFAULT_CAN_RETRY = false
}
