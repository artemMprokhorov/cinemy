package com.example.tmdbai.navigation

/**
 * Type-safe navigation routes using sealed classes
 *
 * This replaces the old string-based navigation with type-safe route objects
 * that can be passed directly to Navigation Compose.
 */

sealed class Screen(val route: String) {
    object Splash : Screen(NavigationConstants.ROUTE_SPLASH)
    object MoviesList : Screen(NavigationConstants.ROUTE_MOVIES_LIST)
    data class MovieDetail(val movieId: Int) : Screen(NavigationConstants.ROUTE_MOVIE_DETAIL) {
        fun createRoute() = NavigationConstants.ROUTE_MOVIE_DETAIL.replace(
            "{${NavigationConstants.PARAM_MOVIE_ID}}",
            movieId.toString()
        )
    }
}
