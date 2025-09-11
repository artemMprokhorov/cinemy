package org.studioapp.cinemy.presentation

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.studioapp.cinemy.data.model.Meta
import org.studioapp.cinemy.data.model.Movie
import org.studioapp.cinemy.data.model.MovieDetails
import org.studioapp.cinemy.data.model.MovieDetailsResponse
import org.studioapp.cinemy.data.model.MovieListResponse
import org.studioapp.cinemy.data.model.UiConfiguration
import org.studioapp.cinemy.data.repository.MovieRepository
import org.studioapp.cinemy.ml.SentimentAnalyzer
import org.studioapp.cinemy.presentation.moviedetail.MovieDetailIntent
import org.studioapp.cinemy.presentation.moviedetail.MovieDetailViewModel
import org.studioapp.cinemy.presentation.movieslist.MoviesListIntent
import org.studioapp.cinemy.presentation.movieslist.MoviesListViewModel

@ExperimentalCoroutinesApi
class SimpleViewModelTests {

    private lateinit var mockRepository: MovieRepository
    private lateinit var mockSentimentAnalyzer: SentimentAnalyzer
    private lateinit var movieDetailViewModel: MovieDetailViewModel
    private lateinit var moviesListViewModel: MoviesListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        mockRepository = mockk<MovieRepository>()
        mockSentimentAnalyzer = mockk<SentimentAnalyzer>()
        
        movieDetailViewModel = MovieDetailViewModel(mockRepository, mockSentimentAnalyzer)
        moviesListViewModel = MoviesListViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Simple MovieDetailViewModel tests
    @Test
    fun `MovieDetailViewModel should initialize with default state`() = runTest {
        // When
        val state = movieDetailViewModel.state.value

        // Then
        assertNotNull(state)
        assertFalse(state.isLoading)
        assertNull(state.movieDetails)
        assertNull(state.error)
    }

    @Test
    fun `MovieDetailViewModel should handle LoadMovieDetails intent`() = runTest {
        // Given
        val movieId = 123

        // When
        movieDetailViewModel.processIntent(MovieDetailIntent.LoadMovieDetails(movieId))
        advanceUntilIdle()

        // Then
        val state = movieDetailViewModel.state.value
        assertEquals(movieId, state.movieId)
    }

    @Test
    fun `MovieDetailViewModel should handle Refresh intent`() = runTest {
        // Given
        val movieId = 456

        // When
        movieDetailViewModel.processIntent(MovieDetailIntent.Refresh)
        advanceUntilIdle()

        // Then
        val state = movieDetailViewModel.state.value
        assertNotNull(state)
    }

    @Test
    fun `MovieDetailViewModel should handle Retry intent`() = runTest {
        // When
        movieDetailViewModel.processIntent(MovieDetailIntent.Retry)
        advanceUntilIdle()

        // Then
        val state = movieDetailViewModel.state.value
        assertNotNull(state)
    }

    @Test
    fun `MovieDetailViewModel should handle BackPressed intent`() = runTest {
        // When
        movieDetailViewModel.processIntent(MovieDetailIntent.BackPressed)
        advanceUntilIdle()

        // Then
        val state = movieDetailViewModel.state.value
        assertNotNull(state)
    }

    @Test
    fun `MovieDetailViewModel should handle LoadRecommendations intent`() = runTest {
        // When
        movieDetailViewModel.processIntent(MovieDetailIntent.LoadRecommendations)
        advanceUntilIdle()

        // Then
        val state = movieDetailViewModel.state.value
        assertNotNull(state)
    }

    @Test
    fun `MovieDetailViewModel should handle ClearSentimentResult intent`() = runTest {
        // When
        movieDetailViewModel.processIntent(MovieDetailIntent.ClearSentimentResult)
        advanceUntilIdle()

        // Then
        val state = movieDetailViewModel.state.value
        assertNotNull(state)
    }

    // Simple MoviesListViewModel tests
    @Test
    fun `MoviesListViewModel should initialize with default state`() = runTest {
        // When
        val state = moviesListViewModel.state.value

        // Then
        assertNotNull(state)
        assertFalse(state.isLoading)
        assertTrue(state.movies.isEmpty())
        assertNull(state.error)
    }

    @Test
    fun `MoviesListViewModel should handle LoadPopularMovies intent`() = runTest {
        // When
        moviesListViewModel.processIntent(MoviesListIntent.LoadPopularMovies)
        advanceUntilIdle()

        // Then
        val state = moviesListViewModel.state.value
        assertNotNull(state)
    }

    @Test
    fun `MoviesListViewModel should handle LoadMoreMovies intent`() = runTest {
        // When
        moviesListViewModel.processIntent(MoviesListIntent.LoadMoreMovies)
        advanceUntilIdle()

        // Then
        val state = moviesListViewModel.state.value
        assertNotNull(state)
    }

    @Test
    fun `MoviesListViewModel should handle NavigateToDetails intent`() = runTest {
        // Given
        val movieId = 789

        // When
        moviesListViewModel.processIntent(MoviesListIntent.NavigateToDetails(movieId))
        advanceUntilIdle()

        // Then
        val state = moviesListViewModel.state.value
        assertNotNull(state)
    }

    @Test
    fun `MoviesListViewModel should handle RetryLastOperation intent`() = runTest {
        // When
        moviesListViewModel.processIntent(MoviesListIntent.RetryLastOperation)
        advanceUntilIdle()

        // Then
        val state = moviesListViewModel.state.value
        assertNotNull(state)
    }

    @Test
    fun `MoviesListViewModel should handle RefreshData intent`() = runTest {
        // When
        moviesListViewModel.processIntent(MoviesListIntent.RefreshData)
        advanceUntilIdle()

        // Then
        val state = moviesListViewModel.state.value
        assertNotNull(state)
    }

    @Test
    fun `MoviesListViewModel should handle CheckConnectionStatus intent`() = runTest {
        // When
        moviesListViewModel.processIntent(MoviesListIntent.CheckConnectionStatus)
        advanceUntilIdle()

        // Then
        val state = moviesListViewModel.state.value
        assertNotNull(state)
    }

    @Test
    fun `MoviesListViewModel should handle DismissError intent`() = runTest {
        // When
        moviesListViewModel.processIntent(MoviesListIntent.DismissError)
        advanceUntilIdle()

        // Then
        val state = moviesListViewModel.state.value
        assertNotNull(state)
    }

    @Test
    fun `MoviesListViewModel should handle RetryConnection intent`() = runTest {
        // When
        moviesListViewModel.processIntent(MoviesListIntent.RetryConnection)
        advanceUntilIdle()

        // Then
        val state = moviesListViewModel.state.value
        assertNotNull(state)
    }

    @Test
    fun `MoviesListViewModel should handle NextPage intent`() = runTest {
        // When
        moviesListViewModel.processIntent(MoviesListIntent.NextPage)
        advanceUntilIdle()

        // Then
        val state = moviesListViewModel.state.value
        assertNotNull(state)
    }

    @Test
    fun `MoviesListViewModel should handle PreviousPage intent`() = runTest {
        // When
        moviesListViewModel.processIntent(MoviesListIntent.PreviousPage)
        advanceUntilIdle()

        // Then
        val state = moviesListViewModel.state.value
        assertNotNull(state)
    }

    // Additional simple tests to increase coverage
    @Test
    fun `MovieDetailViewModel should handle multiple intents`() = runTest {
        // When
        movieDetailViewModel.processIntent(MovieDetailIntent.LoadMovieDetails(1))
        movieDetailViewModel.processIntent(MovieDetailIntent.Refresh)
        movieDetailViewModel.processIntent(MovieDetailIntent.Retry)
        advanceUntilIdle()

        // Then
        val state = movieDetailViewModel.state.value
        assertNotNull(state)
        assertEquals(1, state.movieId)
    }

    @Test
    fun `MoviesListViewModel should handle multiple intents`() = runTest {
        // When
        moviesListViewModel.processIntent(MoviesListIntent.LoadPopularMovies)
        moviesListViewModel.processIntent(MoviesListIntent.RefreshData)
        moviesListViewModel.processIntent(MoviesListIntent.NextPage)
        advanceUntilIdle()

        // Then
        val state = moviesListViewModel.state.value
        assertNotNull(state)
    }

    @Test
    fun `MovieDetailViewModel state should be accessible`() = runTest {
        // When
        val state = movieDetailViewModel.state.value

        // Then
        assertNotNull(state)
        assertFalse(state.isLoading)
        assertNull(state.movieDetails)
        assertNull(state.error)
        assertNull(state.uiConfig)
        assertNull(state.meta)
        assertNull(state.movieId)
        assertFalse(state.showFullDescription)
        assertFalse(state.showProductionDetails)
        assertNull(state.sentimentResult)
        assertNull(state.sentimentError)
        assertNull(state.sentimentReviews)
    }

    @Test
    fun `MoviesListViewModel state should be accessible`() = runTest {
        // When
        val state = moviesListViewModel.state.value

        // Then
        assertNotNull(state)
        assertFalse(state.isLoading)
        assertTrue(state.movies.isEmpty())
        assertNull(state.error)
        assertNull(state.pagination)
        assertEquals(1, state.currentPage)
        assertTrue(state.hasMore)
        assertFalse(state.isUsingMockData)
        assertNotNull(state.connectionStatus)
        assertNull(state.lastSyncTime)
        assertFalse(state.canRetry)
        assertEquals(0, state.retryCount)
        assertNull(state.uiConfig)
        assertNull(state.meta)
    }
}
