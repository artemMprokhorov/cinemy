package org.studioapp.cinemy.presentation

/**
 * Constants for presentation layer
 * Contains all hardcoded strings, integers, and other values used in presentation
 * Centralizes presentation-related constants for maintainability
 */
object PresentationConstants {

    /** Default values for presentation layer */
    const val DEFAULT_MOVIE_ID = 0
    const val DEFAULT_PAGE_NUMBER = 1
    const val DEFAULT_RETRY_COUNT = 0
    const val DEFAULT_BOOLEAN_FALSE = false
    const val DEFAULT_BOOLEAN_TRUE = true

    /** Runtime formatting constants */
    const val MINUTES_PER_HOUR = 60
    const val RUNTIME_HOURS_FORMAT = "h"
    const val RUNTIME_MINUTES_FORMAT = "m"

    /** Budget formatting constants */
    const val BUDGET_DIVISOR = 1_000_000
    const val BUDGET_CURRENCY_SYMBOL = "$"
    const val BUDGET_SUFFIX = "M"
    const val BUDGET_THRESHOLD = 0

    /** Connection status messages */
    const val MESSAGE_USING_DEMO_DATA = "Using demo data"
    const val MESSAGE_BACKEND_UNAVAILABLE = "Backend unavailable - showing demo data"
    const val MESSAGE_CONNECTED_TO_LIVE_DATA = "Connected to live data"
    const val MESSAGE_EMPTY = ""

    /** Connection status detection keywords */
    const val BACKEND_UNAVAILABLE_KEYWORD = "backend unavailable"
    const val MOCK_KEYWORD = "mock"

    /** Pagination constants */
    const val PAGE_INCREMENT = 1
    const val PAGE_DECREMENT = 1

    /** UI state default values */
    const val DEFAULT_HAS_MORE = true
    const val DEFAULT_CAN_RETRY = false
}
