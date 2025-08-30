package com.example.tmdbai.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tmdbai.ui.moviedetail.MovieDetailScreen
import com.example.tmdbai.ui.movieslist.MoviesListScreen
import com.example.tmdbai.ui.splash.MovieAppSplashScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object List : Screen("list")
    object Details : Screen("details/{movieId}") {
        fun createRoute(movieId: String) = "details/$movieId"
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            MovieAppSplashScreen(
                onSplashComplete = {
                    navController.navigate(Screen.List.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.List.route) {
            MoviesListScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Screen.Details.createRoute(movieId.toString()))
                },
                onBackPressed = {
                    // Close the app when back is pressed on List screen
                    android.os.Process.killProcess(android.os.Process.myPid())
                }
            )
        }

        composable(Screen.Details.route) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull() ?: 1
            MovieDetailScreen(
                movieId = movieId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
