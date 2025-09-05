package com.example.tmdbai.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tmdbai.presentation.moviedetail.MovieDetailViewModel
import com.example.tmdbai.presentation.movieslist.MoviesListViewModel
import com.example.tmdbai.ui.moviedetail.MovieDetailScreen
import com.example.tmdbai.ui.movieslist.MoviesListScreen
import com.example.tmdbai.ui.splash.MovieAppSplashScreen
import org.koin.androidx.compose.koinViewModel

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
            val moviesListViewModel: MoviesListViewModel = koinViewModel()
            MoviesListScreen(
                viewModel = moviesListViewModel,
                onMovieClick = { movie ->
                    navController.navigate(Screen.MovieDetail(movie.id).createRoute())
                }
            )
        }

        composable(
            route = Screen.MovieDetail(NavigationConstants.DEFAULT_MOVIE_ID_FOR_ROUTE).route,
            arguments = listOf(
                navArgument(NavigationConstants.NAV_ARG_MOVIE_ID) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt(NavigationConstants.NAV_ARG_MOVIE_ID) ?: NavigationConstants.DEFAULT_MOVIE_ID
            val movieDetailViewModel: MovieDetailViewModel = koinViewModel()
            MovieDetailScreen(
                movieId = movieId,
                viewModel = movieDetailViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
