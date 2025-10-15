package org.studioapp.cinemy.ui.dualpane

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.studioapp.cinemy.R
import org.studioapp.cinemy.data.model.Movie
import org.studioapp.cinemy.presentation.moviedetail.MovieDetailViewModel
import org.studioapp.cinemy.presentation.movieslist.MoviesListViewModel
import org.studioapp.cinemy.ui.components.AdaptiveLayout
import org.studioapp.cinemy.ui.moviedetail.MovieDetailScreen
import org.studioapp.cinemy.ui.movieslist.MoviesListScreen

/**
 * Dual pane screen that combines movies list and movie details for tablets and foldable devices.
 * Uses AdaptiveLayout for responsive design across different device types.
 * Automatically manages ViewModels for both movies list and movie detail screens.
 * 
 * @param selectedMovieId ID of the currently selected movie, null if no movie is selected
 * @param onMovieSelected Callback invoked when a movie is selected from the list
 * @param onBackClick Callback invoked when back button is pressed in movie detail
 * @param modifier Modifier to be applied to the composable
 */
@Composable
fun DualPaneScreen(
    selectedMovieId: Int? = null,
    onMovieSelected: (Movie) -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val moviesListViewModel: MoviesListViewModel = koinViewModel()
    val movieDetailViewModel: MovieDetailViewModel = koinViewModel()

    AdaptiveLayout(
        leftPane = {
            MoviesListScreen(
                viewModel = moviesListViewModel,
                onMovieClick = onMovieSelected
            )
        },
        rightPane = {
            if (selectedMovieId != null) {
                MovieDetailScreen(
                    movieId = selectedMovieId,
                    viewModel = movieDetailViewModel,
                    onBackClick = onBackClick
                )
            } else {
                // Placeholder content when no movie is selected
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.select_movie),
                            style = typography.headlineMedium,
                            color = colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = stringResource(R.string.choose_movie_from_list),
                            style = typography.bodyLarge,
                            color = colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                }
            }
        },
        modifier = modifier,
        showRightPane = selectedMovieId != null
    )
}
