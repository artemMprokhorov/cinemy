package org.studioapp.cinemy.navigation

/**
 * Constants for navigation package
 * Contains all hardcoded strings and integers used in navigation
 * Centralizes navigation-related constants for maintainability
 */
object NavigationConstants {

    /**
     * Route for the splash screen destination
     * Used as the initial screen when the app starts
     */
    const val ROUTE_SPLASH = "splash"

    /**
     * Route for the movies list screen destination
     * Displays the list of available movies
     */
    const val ROUTE_MOVIES_LIST = "movies_list"

    /**
     * Route for the dual-pane screen destination
     * Used on tablets and foldable devices for master-detail navigation
     */
    const val ROUTE_DUAL_PANE = "dual_pane"

    /**
     * Route for the dual-pane screen with a specific movie selected
     * Used on tablets and foldable devices when a movie is selected
     * @param movieId The ID of the selected movie
     */
    const val ROUTE_DUAL_PANE_WITH_MOVIE = "dual_pane/{movieId}"

    /**
     * Route for the movie detail screen destination
     * Displays detailed information about a specific movie
     * @param movieId The ID of the movie to display details for
     */
    const val ROUTE_MOVIE_DETAIL = "movie_detail/{movieId}"

    /**
     * Parameter name for movie ID in dynamic routes
     * Used in route templates for parameter substitution
     */
    const val PARAM_MOVIE_ID = "movieId"

    /**
     * Default movie ID for navigation fallback
     * Used when no specific movie ID is provided
     */
    const val DEFAULT_MOVIE_ID = 1

    /**
     * Default movie ID for route generation
     * Used when creating route templates with default values
     */
    const val DEFAULT_MOVIE_ID_FOR_ROUTE = 0

    /**
     * Navigation argument name for movie ID
     * Used for type-safe parameter passing in navigation
     */
    const val NAV_ARG_MOVIE_ID = "movieId"
}
