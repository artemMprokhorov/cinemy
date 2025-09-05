package com.example.tmdbai.navigation

/**
 * Constants for navigation package
 * Contains all hardcoded strings and integers used in navigation
 */
object NavigationConstants {
    
    // Route Constants
    const val ROUTE_SPLASH = "splash"
    const val ROUTE_MOVIES_LIST = "movies_list"
    const val ROUTE_MOVIE_DETAIL = "movie_detail/{movieId}"
    
    // Route Parameter Constants
    const val PARAM_MOVIE_ID = "movieId"
    
    // Default Values
    const val DEFAULT_MOVIE_ID = 1
    const val DEFAULT_MOVIE_ID_FOR_ROUTE = 0
    
    // Navigation Arguments
    const val NAV_ARG_MOVIE_ID = "movieId"
}
