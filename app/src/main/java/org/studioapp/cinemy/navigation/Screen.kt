package org.studioapp.cinemy.navigation

/**
 * Type-safe navigation routes using sealed classes
 *
 * This replaces the old string-based navigation with type-safe route objects
 * that can be passed directly to Navigation Compose.
 */

sealed class Screen(val route: String) {
    object Splash : Screen(NavigationConstants.ROUTE_SPLASH)
    object MoviesList : Screen(NavigationConstants.ROUTE_MOVIES_LIST)
    object DualPane : Screen(NavigationConstants.ROUTE_DUAL_PANE)
    data class MovieDetail(val movieId: Int) : Screen(NavigationConstants.ROUTE_MOVIE_DETAIL) {
        fun createRoute() = NavigationConstants.ROUTE_MOVIE_DETAIL.replace(
            "{${NavigationConstants.PARAM_MOVIE_ID}}",
            movieId.toString()
        )
    }

    data class DualPaneWithMovie(val movieId: Int) :
        Screen(NavigationConstants.ROUTE_DUAL_PANE_WITH_MOVIE) {
        fun createRoute() = NavigationConstants.ROUTE_DUAL_PANE_WITH_MOVIE.replace(
            "{${NavigationConstants.PARAM_MOVIE_ID}}",
            movieId.toString()
        )
    }
}
