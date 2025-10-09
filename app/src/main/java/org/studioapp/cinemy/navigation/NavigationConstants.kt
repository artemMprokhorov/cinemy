package org.studioapp.cinemy.navigation

/**
 * Constants for navigation package
 * Contains all hardcoded strings and integers used in navigation
 * Centralizes navigation-related constants for maintainability
 */
object NavigationConstants {

    /** Route constants for navigation destinations */
    const val ROUTE_SPLASH = "splash"
    const val ROUTE_MOVIES_LIST = "movies_list"
    const val ROUTE_DUAL_PANE = "dual_pane"
    const val ROUTE_DUAL_PANE_WITH_MOVIE = "dual_pane/{movieId}"
    const val ROUTE_MOVIE_DETAIL = "movie_detail/{movieId}"

    /** Route parameter constants for dynamic routes */
    const val PARAM_MOVIE_ID = "movieId"

    /** Default values for navigation parameters */
    const val DEFAULT_MOVIE_ID = 1
    const val DEFAULT_MOVIE_ID_FOR_ROUTE = 0

    /** Navigation argument names for type-safe navigation */
    const val NAV_ARG_MOVIE_ID = "movieId"
}
