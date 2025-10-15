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
 * This sealed class defines all navigation routes in the application, providing
 * compile-time safety for navigation routes and parameters. It replaces string-based
 * navigation with type-safe route objects that can be passed directly to Navigation Compose.
 * 
 * Each route can contain parameters that are validated at compile time, ensuring
 * type safety and preventing navigation to non-existent routes.
 */
sealed class Screen(val route: String) {
    /**
     * Splash screen route
     * 
     * Initial screen displayed when the application starts, typically used
     * for loading resources and initializing the app.
     */
    object Splash : Screen(ROUTE_SPLASH)

    /**
     * Movies list screen route
     * 
     * Main screen displaying the list of movies available in the application.
     * This is the primary navigation destination after the splash screen.
     */
    object MoviesList : Screen(ROUTE_MOVIES_LIST)

    /**
     * Dual pane screen route for tablets and foldable devices
     * 
     * Specialized navigation route for devices that support dual-pane layout,
     * allowing simultaneous display of movies list and movie details.
     */
    object DualPane : Screen(ROUTE_DUAL_PANE)

    /**
     * Movie detail screen route with movie ID parameter
     * 
     * Represents a navigation route to a specific movie's detail screen.
     * The movie ID parameter is embedded in the route for type-safe navigation.
     * 
     * @param movieId Unique identifier of the movie to display
     */
    data class MovieDetail(val movieId: Int) : Screen(ROUTE_MOVIE_DETAIL) {
        /**
         * Creates a complete route string with the movie ID parameter substituted
         * 
         * Replaces the placeholder movie ID parameter in the route template
         * with the actual movie ID value, creating a valid navigation route.
         * 
         * @return Complete route string with movie ID parameter substituted
         */
        fun createRoute() = ROUTE_MOVIE_DETAIL.replace(
            "{${PARAM_MOVIE_ID}}",
            movieId.toString()
        )
    }

    /**
     * Dual pane screen route with movie ID parameter for tablets and foldable devices
     * 
     * Represents a navigation route to a dual-pane layout with a specific movie
     * selected. This route is used on devices that support dual-pane navigation,
     * allowing simultaneous display of the movies list and the selected movie's details.
     * 
     * @param movieId Unique identifier of the selected movie to display in the detail pane
     */
    data class DualPaneWithMovie(val movieId: Int) :
        Screen(ROUTE_DUAL_PANE_WITH_MOVIE) {
        /**
         * Creates a complete route string with the movie ID parameter substituted
         * 
         * Replaces the placeholder movie ID parameter in the dual-pane route template
         * with the actual movie ID value, creating a valid navigation route for
         * dual-pane devices with a pre-selected movie.
         * 
         * @return Complete route string with movie ID parameter substituted
         */
        fun createRoute() = ROUTE_DUAL_PANE_WITH_MOVIE.replace(
            "{${PARAM_MOVIE_ID}}",
            movieId.toString()
        )
    }
}
