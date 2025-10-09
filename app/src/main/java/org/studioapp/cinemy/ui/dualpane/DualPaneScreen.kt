package org.studioapp.cinemy.ui.dualpane

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.studioapp.cinemy.data.model.Movie
import org.studioapp.cinemy.presentation.moviedetail.MovieDetailViewModel
import org.studioapp.cinemy.presentation.movieslist.MoviesListViewModel
import org.studioapp.cinemy.ui.components.AdaptiveLayout
import org.studioapp.cinemy.ui.moviedetail.MovieDetailScreen
import org.studioapp.cinemy.ui.movieslist.MoviesListScreen

/**
 * Dual pane screen that combines movies list and movie details
 * Uses AdaptiveLayout for responsive design across different device types
 * @param selectedMovieId ID of the currently selected movie (optional)
 * @param onMovieSelected Callback when a movie is selected
 * @param onBackClick Callback when back button is pressed
 * @param modifier Modifier for the composable
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
                            text = "Select a movie",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "Choose a movie from the list to view details",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
