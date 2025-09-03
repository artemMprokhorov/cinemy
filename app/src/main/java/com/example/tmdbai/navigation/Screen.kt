package com.example.tmdbai.navigation

/**
 * Type-safe navigation routes using sealed classes
 * 
 * This replaces the old string-based navigation with type-safe route objects
 * that can be passed directly to Navigation Compose.
 */

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object MoviesList : Screen("movies_list")
    data class MovieDetail(val movieId: Int) : Screen("movie_detail/{movieId}") {
        fun createRoute() = "movie_detail/$movieId"
    }
}
