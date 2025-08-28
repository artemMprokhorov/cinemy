package com.example.tmdbai.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tmdbai.ui.details.MovieDetailsScreen
import com.example.tmdbai.ui.list.MovieListScreen
import com.example.tmdbai.ui.loading.MovieLoadingScreen
import com.example.tmdbai.ui.splash.MovieAppSplashScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Loading : Screen("loading")
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
                    navController.navigate(Screen.Loading.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Loading.route) {
            MovieLoadingScreen(
                onLoadingComplete = {
                    navController.navigate(Screen.List.route) {
                        popUpTo(Screen.Loading.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.List.route) {
            MovieListScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Screen.Details.createRoute(movieId))
                },
                onBackPressed = {
                    // Close the app when back is pressed on List screen
                    android.os.Process.killProcess(android.os.Process.myPid())
                }
            )
        }
        
        composable(Screen.Details.route) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId") ?: ""
            MovieDetailsScreen(
                movieId = movieId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
