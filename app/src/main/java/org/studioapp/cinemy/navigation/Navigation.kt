package org.studioapp.cinemy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.koin.androidx.compose.koinViewModel
import org.studioapp.cinemy.presentation.moviedetail.MovieDetailViewModel
import org.studioapp.cinemy.presentation.movieslist.MoviesListViewModel
import org.studioapp.cinemy.ui.dualpane.DualPaneScreen
import org.studioapp.cinemy.ui.moviedetail.MovieDetailScreen
import org.studioapp.cinemy.ui.movieslist.MoviesListScreen
import org.studioapp.cinemy.ui.splash.MovieAppSplashScreen
import org.studioapp.cinemy.utils.supportsDualPane
import org.studioapp.cinemy.navigation.NavigationConstants.DEFAULT_MOVIE_ID_FOR_ROUTE
import org.studioapp.cinemy.navigation.NavigationConstants.NAV_ARG_MOVIE_ID
import org.studioapp.cinemy.navigation.NavigationConstants.DEFAULT_MOVIE_ID

/**
 * Main navigation composable for the Cinemy app
 * Handles navigation between splash, movies list, movie detail, and dual pane screens
 * @param navController NavHostController for navigation management
 */
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            val supportsDual = supportsDualPane()
            MovieAppSplashScreen(
                onSplashComplete = {
                    // Navigate to dual pane if device supports it, otherwise single pane
                    val destination = if (supportsDual) {
                        Screen.DualPane.route
                    } else {
                        Screen.MoviesList.route
                    }
                    navController.navigate(destination) {
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

        composable(Screen.DualPane.route) {
            DualPaneScreen(
                selectedMovieId = null,
                onMovieSelected = { movie ->
                    navController.navigate(Screen.DualPaneWithMovie(movie.id).createRoute())
                }
            )
        }

        composable(
            route = Screen.DualPaneWithMovie(DEFAULT_MOVIE_ID_FOR_ROUTE).route,
            arguments = listOf(
                navArgument(NAV_ARG_MOVIE_ID) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt(NAV_ARG_MOVIE_ID)
                ?: DEFAULT_MOVIE_ID
            DualPaneScreen(
                selectedMovieId = movieId,
                onMovieSelected = { movie ->
                    navController.navigate(Screen.DualPaneWithMovie(movie.id).createRoute())
                }
            )
        }

        composable(
            route = Screen.MovieDetail(DEFAULT_MOVIE_ID_FOR_ROUTE).route,
            arguments = listOf(
                navArgument(NAV_ARG_MOVIE_ID) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt(NAV_ARG_MOVIE_ID)
                ?: DEFAULT_MOVIE_ID
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
