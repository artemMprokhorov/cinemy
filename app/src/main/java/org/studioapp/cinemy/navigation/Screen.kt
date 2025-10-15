package org.studioapp.cinemy.navigation

import org.studioapp.cinemy.navigation.NavigationConstants.ROUTE_SPLASH
import org.studioapp.cinemy.navigation.NavigationConstants.ROUTE_MOVIES_LIST
import org.studioapp.cinemy.navigation.NavigationConstants.ROUTE_DUAL_PANE
import org.studioapp.cinemy.navigation.NavigationConstants.ROUTE_MOVIE_DETAIL
import org.studioapp.cinemy.navigation.NavigationConstants.ROUTE_DUAL_PANE_WITH_MOVIE
import org.studioapp.cinemy.navigation.NavigationConstants.PARAM_MOVIE_ID

/**
 * Type-safe navigation routes using sealed classes
 *
 * This replaces the old string-based navigation with type-safe route objects
 * that can be passed directly to Navigation Compose.
 */

/**
 * Type-safe navigation routes using sealed classes
 * Provides compile-time safety for navigation routes and parameters
 */
sealed class Screen(val route: String) {
    /** Splash screen route */
    object Splash : Screen(ROUTE_SPLASH)

    /** Movies list screen route */
    object MoviesList : Screen(ROUTE_MOVIES_LIST)

    /** Dual pane screen route for tablets and foldable devices */
    object DualPane : Screen(ROUTE_DUAL_PANE)

    /**
     * Movie detail screen route with movie ID parameter
     * @param movieId Unique identifier of the movie
     */
    data class MovieDetail(val movieId: Int) : Screen(ROUTE_MOVIE_DETAIL) {
        /**
         * Creates route string with movie ID parameter
         * @return Route string with movie ID substituted
         */
        fun createRoute() = ROUTE_MOVIE_DETAIL.replace(
            "{${PARAM_MOVIE_ID}}",
            movieId.toString()
        )
    }

    /**
     * Dual pane screen route with movie ID parameter for tablets and foldable devices
     * @param movieId Unique identifier of the selected movie
     */
    data class DualPaneWithMovie(val movieId: Int) :
        Screen(ROUTE_DUAL_PANE_WITH_MOVIE) {
        /**
         * Creates route string with movie ID parameter
         * @return Route string with movie ID substituted
         */
        fun createRoute() = ROUTE_DUAL_PANE_WITH_MOVIE.replace(
            "{${PARAM_MOVIE_ID}}",
            movieId.toString()
        )
    }
}
