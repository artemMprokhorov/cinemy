package com.example.tmdbai.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tmdbai.ui.moviedetail.MovieDetailScreen
import com.example.tmdbai.ui.movieslist.MoviesListScreen
import com.example.tmdbai.ui.splash.MovieAppSplashScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            MovieAppSplashScreen(
                onSplashComplete = {
                    navController.navigate(Screen.MoviesList.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.MoviesList.route) {
            MoviesListScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Screen.MovieDetail(movieId).createRoute())
                },
                onBackPressed = {
                    // Close the app when back is pressed on List screen
                    android.os.Process.killProcess(android.os.Process.myPid())
                }
            )
        }

        composable(
            route = Screen.MovieDetail(0).route,
            arguments = listOf(
                navArgument("movieId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 1
            MovieDetailScreen(
                movieId = movieId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
