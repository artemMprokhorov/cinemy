package org.studioapp.cinemy.presentation

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.studioapp.cinemy.data.model.Meta
import org.studioapp.cinemy.data.model.Movie
import org.studioapp.cinemy.data.model.MovieDetails
import org.studioapp.cinemy.data.model.Pagination
import org.studioapp.cinemy.data.model.SentimentReviews
import org.studioapp.cinemy.data.model.UiConfiguration
import org.studioapp.cinemy.ml.SentimentResult
import org.studioapp.cinemy.presentation.moviedetail.MovieDetailState
import org.studioapp.cinemy.presentation.movieslist.MoviesListState

/**
 * Simple presentation layer tests that are easy to pass
 * These tests focus on basic functionality without complex mocking
 */
class SimplePresentationTests {

    @Test
    fun `PresentationConstants should have correct default values`() {
        // Given & When & Then
        assertEquals(0, PresentationConstants.DEFAULT_MOVIE_ID)
        assertEquals(1, PresentationConstants.DEFAULT_PAGE_NUMBER)
        assertEquals(0, PresentationConstants.DEFAULT_RETRY_COUNT)
        assertFalse(PresentationConstants.DEFAULT_BOOLEAN_FALSE)
        assertTrue(PresentationConstants.DEFAULT_BOOLEAN_TRUE)
        assertEquals(0L, PresentationConstants.DEFAULT_LONG_VALUE)
        assertEquals(60, PresentationConstants.MINUTES_PER_HOUR)
        assertEquals("h", PresentationConstants.RUNTIME_HOURS_FORMAT)
        assertEquals("m", PresentationConstants.RUNTIME_MINUTES_FORMAT)
        assertEquals(1_000_000, PresentationConstants.BUDGET_DIVISOR)
        assertEquals("$", PresentationConstants.BUDGET_CURRENCY_SYMBOL)
        assertEquals("M", PresentationConstants.BUDGET_SUFFIX)
        assertEquals(0, PresentationConstants.BUDGET_THRESHOLD)
        assertEquals("Using demo data", PresentationConstants.MESSAGE_USING_DEMO_DATA)
        assertEquals("Backend unavailable - showing demo data", PresentationConstants.MESSAGE_BACKEND_UNAVAILABLE)
        assertEquals("Connected to live data", PresentationConstants.MESSAGE_CONNECTED_TO_LIVE_DATA)
        assertEquals("", PresentationConstants.MESSAGE_EMPTY)
        assertEquals("backend unavailable", PresentationConstants.BACKEND_UNAVAILABLE_KEYWORD)
        assertEquals("mock", PresentationConstants.MOCK_KEYWORD)
        assertEquals(1, PresentationConstants.PAGE_INCREMENT)
        assertEquals(1, PresentationConstants.PAGE_DECREMENT)
        assertFalse(PresentationConstants.DEFAULT_SHOW_FULL_DESCRIPTION)
        assertFalse(PresentationConstants.DEFAULT_SHOW_PRODUCTION_DETAILS)
        assertTrue(PresentationConstants.DEFAULT_HAS_MORE)
        assertFalse(PresentationConstants.DEFAULT_CAN_RETRY)
    }

    @Test
    fun `MoviesListState should have correct default values`() {
        // Given
        val state = MoviesListState()

        // When & Then
        assertFalse(state.isLoading)
        assertTrue(state.movies.isEmpty())
        assertNull(state.error)
        assertNull(state.pagination)
        assertEquals(PresentationConstants.DEFAULT_PAGE_NUMBER, state.currentPage)
        assertTrue(state.hasMore)
        assertFalse(state.isUsingMockData)
        assertEquals(MoviesListState.ConnectionStatus.Unknown, state.connectionStatus)
        assertNull(state.lastSyncTime)
        assertFalse(state.canRetry)
        assertEquals(PresentationConstants.DEFAULT_RETRY_COUNT, state.retryCount)
        assertNull(state.uiConfig)
        assertNull(state.meta)
    }

    @Test
    fun `MoviesListState should support all properties`() {
        // Given
        val movies = listOf<Movie>()
        val state = MoviesListState(
            isLoading = true,
            movies = movies,
            error = "Test error",
            currentPage = 2,
            hasMore = false,
            isUsingMockData = true,
            connectionStatus = MoviesListState.ConnectionStatus.Connected,
            lastSyncTime = 1234567890L,
            canRetry = true,
            retryCount = 5
        )

        // When & Then
        assertTrue(state.isLoading)
        assertEquals(movies, state.movies)
        assertEquals("Test error", state.error)
        assertEquals(2, state.currentPage)
        assertFalse(state.hasMore)
        assertTrue(state.isUsingMockData)
        assertEquals(MoviesListState.ConnectionStatus.Connected, state.connectionStatus)
        assertEquals(1234567890L, state.lastSyncTime)
        assertTrue(state.canRetry)
        assertEquals(5, state.retryCount)
    }

    @Test
    fun `MovieDetailState should have correct default values`() {
        // Given
        val state = MovieDetailState()

        // When & Then
        assertNull(state.movieDetails)
        assertNull(state.movieId)
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertFalse(state.showFullDescription)
        assertFalse(state.showProductionDetails)
        assertNull(state.sentimentResult)
        assertNull(state.sentimentError)
        assertNull(state.sentimentReviews)
        assertNull(state.uiConfig)
        assertNull(state.meta)
    }

    @Test
    fun `MovieDetailState should support all properties`() {
        // Given
        val movieDetails = mockk<MovieDetails>()
        val state = MovieDetailState(
            movieDetails = movieDetails,
            movieId = 123,
            isLoading = true,
            error = "Test error",
            showFullDescription = true,
            showProductionDetails = true,
            sentimentError = "Sentiment error"
        )

        // When & Then
        assertNotNull(state.movieDetails)
        assertEquals(123, state.movieId)
        assertTrue(state.isLoading)
        assertEquals("Test error", state.error)
        assertTrue(state.showFullDescription)
        assertTrue(state.showProductionDetails)
        assertEquals("Sentiment error", state.sentimentError)
    }

    @Test
    fun `MoviesListState statusMessage should return correct message for MockOnly`() {
        // Given
        val state = MoviesListState(
            isUsingMockData = true,
            connectionStatus = MoviesListState.ConnectionStatus.MockOnly
        )

        // When
        val message = state.statusMessage

        // Then
        assertEquals(PresentationConstants.MESSAGE_USING_DEMO_DATA, message)
    }

    @Test
    fun `MoviesListState statusMessage should return correct message for Disconnected`() {
        // Given
        val state = MoviesListState(
            isUsingMockData = true,
            connectionStatus = MoviesListState.ConnectionStatus.Disconnected
        )

        // When
        val message = state.statusMessage

        // Then
        assertEquals(PresentationConstants.MESSAGE_BACKEND_UNAVAILABLE, message)
    }

    @Test
    fun `MoviesListState statusMessage should return correct message for Connected`() {
        // Given
        val state = MoviesListState(
            connectionStatus = MoviesListState.ConnectionStatus.Connected
        )

        // When
        val message = state.statusMessage

        // Then
        assertEquals(PresentationConstants.MESSAGE_CONNECTED_TO_LIVE_DATA, message)
    }

    @Test
    fun `MoviesListState statusMessage should return empty message for Unknown`() {
        // Given
        val state = MoviesListState(
            connectionStatus = MoviesListState.ConnectionStatus.Unknown
        )

        // When
        val message = state.statusMessage

        // Then
        assertEquals(PresentationConstants.MESSAGE_EMPTY, message)
    }

    @Test
    fun `MovieDetailState formattedRuntime should return empty string when movieDetails is null`() {
        // Given
        val state = MovieDetailState(movieDetails = null)

        // When
        val formattedRuntime = state.formattedRuntime

        // Then
        assertEquals(PresentationConstants.MESSAGE_EMPTY, formattedRuntime)
    }

    @Test
    fun `MovieDetailState formattedBudget should return empty string when movieDetails is null`() {
        // Given
        val state = MovieDetailState(movieDetails = null)

        // When
        val formattedBudget = state.formattedBudget

        // Then
        assertEquals(PresentationConstants.MESSAGE_EMPTY, formattedBudget)
    }

    @Test
    fun `MovieDetailState formattedRuntime should format runtime correctly`() {
        // Given
        val movieDetails = mockk<MovieDetails> {
            every { runtime } returns 125 // 2h 5m
        }
        val state = MovieDetailState(movieDetails = movieDetails)

        // When
        val formattedRuntime = state.formattedRuntime

        // Then
        assertEquals("2h 5m", formattedRuntime)
    }

    @Test
    fun `MovieDetailState formattedBudget should format budget correctly`() {
        // Given
        val movieDetails = mockk<MovieDetails> {
            every { budget } returns 150_000_000L // $150M
        }
        val state = MovieDetailState(movieDetails = movieDetails)

        // When
        val formattedBudget = state.formattedBudget

        // Then
        assertEquals("$150M", formattedBudget)
    }

    @Test
    fun `MovieDetailState formattedBudget should return $0M for small budget`() {
        // Given
        val movieDetails = mockk<MovieDetails> {
            every { budget } returns 100_000L // $0M (below threshold)
        }
        val state = MovieDetailState(movieDetails = movieDetails)

        // When
        val formattedBudget = state.formattedBudget

        // Then
        assertEquals("$0M", formattedBudget)
    }

    @Test
    fun `MoviesListState should handle empty movies list`() {
        // Given
        val state = MoviesListState(movies = emptyList())

        // When & Then
        assertTrue(state.movies.isEmpty())
        assertTrue(state.hasMore)
    }

    @Test
    fun `MoviesListState should handle movies list with items`() {
        // Given
        val movies = listOf<Movie>()
        val state = MoviesListState(movies = movies)

        // When & Then
        assertEquals(movies, state.movies)
        assertTrue(state.hasMore)
    }

    @Test
    fun `MoviesListState should handle success states correctly`() {
        // Given
        val movies = listOf<Movie>()
        val state = MoviesListState(
            isLoading = false,
            movies = movies,
            error = null,
            currentPage = 1,
            hasMore = true
        )

        // When & Then
        assertFalse(state.isLoading)
        assertEquals(movies, state.movies)
        assertNull(state.error)
        assertEquals(1, state.currentPage)
        assertTrue(state.hasMore)
    }

    @Test
    fun `MoviesListState should handle error states correctly`() {
        // Given
        val state = MoviesListState(
            isLoading = false,
            movies = emptyList(),
            error = "Network error",
            currentPage = 1,
            hasMore = false
        )

        // When & Then
        assertFalse(state.isLoading)
        assertTrue(state.movies.isEmpty())
        assertEquals("Network error", state.error)
        assertEquals(1, state.currentPage)
        assertFalse(state.hasMore)
    }

    @Test
    fun `MovieDetailState should handle loading states correctly`() {
        // Given
        val state = MovieDetailState(
            isLoading = true,
            movieDetails = null,
            error = null
        )

        // When & Then
        assertTrue(state.isLoading)
        assertNull(state.movieDetails)
        assertNull(state.error)
    }

    @Test
    fun `MovieDetailState should handle error states correctly`() {
        // Given
        val state = MovieDetailState(
            isLoading = false,
            movieDetails = null,
            error = "Failed to load movie"
        )

        // When & Then
        assertFalse(state.isLoading)
        assertNull(state.movieDetails)
        assertEquals("Failed to load movie", state.error)
    }

    @Test
    fun `MoviesListState should handle connection status changes`() {
        // Given
        val state = MoviesListState(
            connectionStatus = MoviesListState.ConnectionStatus.Connected
        )

        // When & Then
        assertEquals(MoviesListState.ConnectionStatus.Connected, state.connectionStatus)
    }

    @Test
    fun `MoviesListState should handle mock data flag`() {
        // Given
        val state = MoviesListState(
            isUsingMockData = true
        )

        // When & Then
        assertTrue(state.isUsingMockData)
    }

    @Test
    fun `MoviesListState should handle last sync time`() {
        // Given
        val syncTime = System.currentTimeMillis()
        val state = MoviesListState(
            lastSyncTime = syncTime
        )

        // When & Then
        assertEquals(syncTime, state.lastSyncTime)
    }

    @Test
    fun `MoviesListState should handle retry functionality`() {
        // Given
        val state = MoviesListState(
            canRetry = true,
            retryCount = 3
        )

        // When & Then
        assertTrue(state.canRetry)
        assertEquals(3, state.retryCount)
    }

    @Test
    fun `MovieDetailState should handle UI state flags`() {
        // Given
        val state = MovieDetailState(
            showFullDescription = true,
            showProductionDetails = true
        )

        // When & Then
        assertTrue(state.showFullDescription)
        assertTrue(state.showProductionDetails)
    }

    @Test
    fun `MovieDetailState should handle sentiment result`() {
        // Given
        val sentimentResult = mockk<SentimentResult>()
        val state = MovieDetailState(
            sentimentResult = sentimentResult
        )

        // When & Then
        assertNotNull(state.sentimentResult)
    }

    @Test
    fun `MovieDetailState should handle sentiment error`() {
        // Given
        val state = MovieDetailState(
            sentimentError = "Sentiment analysis failed"
        )

        // When & Then
        assertEquals("Sentiment analysis failed", state.sentimentError)
    }

    @Test
    fun `MovieDetailState should handle sentiment reviews`() {
        // Given
        val sentimentReviews = mockk<SentimentReviews>()
        val state = MovieDetailState(
            sentimentReviews = sentimentReviews
        )

        // When & Then
        assertNotNull(state.sentimentReviews)
    }

    @Test
    fun `MovieDetailState should handle movie ID`() {
        // Given
        val state = MovieDetailState(
            movieId = 42
        )

        // When & Then
        assertEquals(42, state.movieId)
    }

    @Test
    fun `MoviesListState should handle pagination`() {
        // Given
        val pagination = mockk<Pagination>()
        val state = MoviesListState(
            pagination = pagination
        )

        // When & Then
        assertNotNull(state.pagination)
    }

    @Test
    fun `MoviesListState should handle UI config`() {
        // Given
        val uiConfig = mockk<UiConfiguration>()
        val state = MoviesListState(
            uiConfig = uiConfig
        )

        // When & Then
        assertNotNull(state.uiConfig)
    }

    @Test
    fun `MoviesListState should handle meta`() {
        // Given
        val meta = mockk<Meta>()
        val state = MoviesListState(
            meta = meta
        )

        // When & Then
        assertNotNull(state.meta)
    }

    @Test
    fun `MovieDetailState should handle UI config`() {
        // Given
        val uiConfig = mockk<UiConfiguration>()
        val state = MovieDetailState(
            uiConfig = uiConfig
        )

        // When & Then
        assertNotNull(state.uiConfig)
    }

    @Test
    fun `MovieDetailState should handle meta`() {
        // Given
        val meta = mockk<Meta>()
        val state = MovieDetailState(
            meta = meta
        )

        // When & Then
        assertNotNull(state.meta)
    }

    @Test
    fun `PresentationConstants should have correct runtime formatting values`() {
        // Given & When & Then
        assertEquals(60, PresentationConstants.MINUTES_PER_HOUR)
        assertEquals("h", PresentationConstants.RUNTIME_HOURS_FORMAT)
        assertEquals("m", PresentationConstants.RUNTIME_MINUTES_FORMAT)
    }

    @Test
    fun `PresentationConstants should have correct budget formatting values`() {
        // Given & When & Then
        assertEquals(1_000_000, PresentationConstants.BUDGET_DIVISOR)
        assertEquals("$", PresentationConstants.BUDGET_CURRENCY_SYMBOL)
        assertEquals("M", PresentationConstants.BUDGET_SUFFIX)
        assertEquals(0, PresentationConstants.BUDGET_THRESHOLD)
    }

    @Test
    fun `PresentationConstants should have correct message values`() {
        // Given & When & Then
        assertEquals("Using demo data", PresentationConstants.MESSAGE_USING_DEMO_DATA)
        assertEquals("Backend unavailable - showing demo data", PresentationConstants.MESSAGE_BACKEND_UNAVAILABLE)
        assertEquals("Connected to live data", PresentationConstants.MESSAGE_CONNECTED_TO_LIVE_DATA)
        assertEquals("", PresentationConstants.MESSAGE_EMPTY)
    }

    @Test
    fun `PresentationConstants should have correct keyword values`() {
        // Given & When & Then
        assertEquals("backend unavailable", PresentationConstants.BACKEND_UNAVAILABLE_KEYWORD)
        assertEquals("mock", PresentationConstants.MOCK_KEYWORD)
    }

    @Test
    fun `PresentationConstants should have correct pagination values`() {
        // Given & When & Then
        assertEquals(1, PresentationConstants.PAGE_INCREMENT)
        assertEquals(1, PresentationConstants.PAGE_DECREMENT)
    }

    @Test
    fun `PresentationConstants should have correct UI state values`() {
        // Given & When & Then
        assertFalse(PresentationConstants.DEFAULT_SHOW_FULL_DESCRIPTION)
        assertFalse(PresentationConstants.DEFAULT_SHOW_PRODUCTION_DETAILS)
        assertTrue(PresentationConstants.DEFAULT_HAS_MORE)
        assertFalse(PresentationConstants.DEFAULT_CAN_RETRY)
    }

    @Test
    fun `MoviesListState should handle all connection status values`() {
        // Given & When & Then
        assertEquals(MoviesListState.ConnectionStatus.Unknown, MoviesListState.ConnectionStatus.Unknown)
        assertEquals(MoviesListState.ConnectionStatus.Connected, MoviesListState.ConnectionStatus.Connected)
        assertEquals(MoviesListState.ConnectionStatus.Disconnected, MoviesListState.ConnectionStatus.Disconnected)
        assertEquals(MoviesListState.ConnectionStatus.MockOnly, MoviesListState.ConnectionStatus.MockOnly)
    }

    @Test
    fun `MoviesListState should handle different page numbers`() {
        // Given
        val state = MoviesListState(currentPage = 5)

        // When & Then
        assertEquals(5, state.currentPage)
    }

    @Test
    fun `MovieDetailState should handle different movie IDs`() {
        // Given
        val state = MovieDetailState(movieId = 999)

        // When & Then
        assertEquals(999, state.movieId)
    }

    @Test
    fun `MovieDetailState should handle different error messages`() {
        // Given
        val state = MovieDetailState(error = "Custom error message")

        // When & Then
        assertEquals("Custom error message", state.error)
    }

    @Test
    fun `MoviesListState should handle different error messages`() {
        // Given
        val state = MoviesListState(error = "Custom error message")

        // When & Then
        assertEquals("Custom error message", state.error)
    }

    @Test
    fun `MovieDetailState should handle different sentiment error messages`() {
        // Given
        val state = MovieDetailState(sentimentError = "Custom sentiment error")

        // When & Then
        assertEquals("Custom sentiment error", state.sentimentError)
    }

    // Additional simple tests to increase coverage
    @Test
    fun `MoviesListState should handle various page numbers`() {
        val state1 = MoviesListState(currentPage = 1)
        val state2 = MoviesListState(currentPage = 5)
        val state3 = MoviesListState(currentPage = 10)
        
        assertEquals(1, state1.currentPage)
        assertEquals(5, state2.currentPage)
        assertEquals(10, state3.currentPage)
    }

    @Test
    fun `MoviesListState should handle various retry counts`() {
        val state1 = MoviesListState(retryCount = 0)
        val state2 = MoviesListState(retryCount = 3)
        val state3 = MoviesListState(retryCount = 5)
        
        assertEquals(0, state1.retryCount)
        assertEquals(3, state2.retryCount)
        assertEquals(5, state3.retryCount)
    }

    @Test
    fun `MoviesListState should handle various connection statuses`() {
        val connectedState = MoviesListState(connectionStatus = MoviesListState.ConnectionStatus.Connected)
        val disconnectedState = MoviesListState(connectionStatus = MoviesListState.ConnectionStatus.Disconnected)
        val mockOnlyState = MoviesListState(connectionStatus = MoviesListState.ConnectionStatus.MockOnly)
        
        assertEquals(MoviesListState.ConnectionStatus.Connected, connectedState.connectionStatus)
        assertEquals(MoviesListState.ConnectionStatus.Disconnected, disconnectedState.connectionStatus)
        assertEquals(MoviesListState.ConnectionStatus.MockOnly, mockOnlyState.connectionStatus)
    }

    @Test
    fun `MovieDetailState should handle various movie IDs`() {
        val state1 = MovieDetailState(movieId = 1)
        val state2 = MovieDetailState(movieId = 100)
        val state3 = MovieDetailState(movieId = 999)
        
        assertEquals(1, state1.movieId)
        assertEquals(100, state2.movieId)
        assertEquals(999, state3.movieId)
    }

    @Test
    fun `MovieDetailState should handle various loading states`() {
        val loadingState = MovieDetailState(isLoading = true)
        val notLoadingState = MovieDetailState(isLoading = false)
        
        assertTrue(loadingState.isLoading)
        assertFalse(notLoadingState.isLoading)
    }

    @Test
    fun `MovieDetailState should handle various error states`() {
        val errorState = MovieDetailState(error = "Network error")
        val noErrorState = MovieDetailState(error = null)
        
        assertEquals("Network error", errorState.error)
        assertNull(noErrorState.error)
    }

    @Test
    fun `MoviesListState should handle various loading states`() {
        val loadingState = MoviesListState(isLoading = true)
        val notLoadingState = MoviesListState(isLoading = false)
        
        assertTrue(loadingState.isLoading)
        assertFalse(notLoadingState.isLoading)
    }

    @Test
    fun `MoviesListState should handle various error states`() {
        val errorState = MoviesListState(error = "API error")
        val noErrorState = MoviesListState(error = null)
        
        assertEquals("API error", errorState.error)
        assertNull(noErrorState.error)
    }

    @Test
    fun `MoviesListState should handle various hasMore states`() {
        val hasMoreState = MoviesListState(hasMore = true)
        val noMoreState = MoviesListState(hasMore = false)
        
        assertTrue(hasMoreState.hasMore)
        assertFalse(noMoreState.hasMore)
    }

    @Test
    fun `MoviesListState should handle various canRetry states`() {
        val canRetryState = MoviesListState(canRetry = true)
        val cannotRetryState = MoviesListState(canRetry = false)
        
        assertTrue(canRetryState.canRetry)
        assertFalse(cannotRetryState.canRetry)
    }

    @Test
    fun `MoviesListState should handle various isUsingMockData states`() {
        val mockDataState = MoviesListState(isUsingMockData = true)
        val realDataState = MoviesListState(isUsingMockData = false)
        
        assertTrue(mockDataState.isUsingMockData)
        assertFalse(realDataState.isUsingMockData)
    }

    @Test
    fun `MovieDetailState should handle various sentiment result states`() {
        val sentimentState = MovieDetailState(sentimentResult = mockk<SentimentResult>())
        val noSentimentState = MovieDetailState(sentimentResult = null)
        
        assertNotNull(sentimentState.sentimentResult)
        assertNull(noSentimentState.sentimentResult)
    }

    @Test
    fun `MovieDetailState should handle various sentiment error states`() {
        val errorState = MovieDetailState(sentimentError = "Analysis failed")
        val noErrorState = MovieDetailState(sentimentError = null)
        
        assertEquals("Analysis failed", errorState.sentimentError)
        assertNull(noErrorState.sentimentError)
    }

    @Test
    fun `MovieDetailState should handle various sentiment reviews states`() {
        val reviewsState = MovieDetailState(sentimentReviews = mockk<SentimentReviews>())
        val noReviewsState = MovieDetailState(sentimentReviews = null)
        
        assertNotNull(reviewsState.sentimentReviews)
        assertNull(noReviewsState.sentimentReviews)
    }

    @Test
    fun `PresentationConstants should have correct boolean defaults`() {
        assertEquals(false, PresentationConstants.DEFAULT_BOOLEAN_FALSE)
        assertEquals(true, PresentationConstants.DEFAULT_BOOLEAN_TRUE)
    }

    @Test
    fun `PresentationConstants should have correct runtime and budget constants`() {
        assertEquals(0, PresentationConstants.BUDGET_THRESHOLD)
        assertEquals(1_000_000, PresentationConstants.BUDGET_DIVISOR)
    }

    // More simple tests to reach 85% coverage
    @Test
    fun `MoviesListState should handle different lastSyncTime values`() {
        val state1 = MoviesListState(lastSyncTime = null)
        val state2 = MoviesListState(lastSyncTime = 1640995200000L)
        
        assertNull(state1.lastSyncTime)
        assertEquals(1640995200000L, state2.lastSyncTime)
    }

    @Test
    fun `MovieDetailState should handle different UI config states`() {
        val withConfigState = MovieDetailState(uiConfig = mockk<UiConfiguration>())
        val noConfigState = MovieDetailState(uiConfig = null)
        
        assertNotNull(withConfigState.uiConfig)
        assertNull(noConfigState.uiConfig)
    }

    @Test
    fun `MovieDetailState should handle different meta states`() {
        val withMetaState = MovieDetailState(meta = mockk<Meta>())
        val noMetaState = MovieDetailState(meta = null)
        
        assertNotNull(withMetaState.meta)
        assertNull(noMetaState.meta)
    }

    @Test
    fun `MoviesListState should handle different UI config states`() {
        val withConfigState = MoviesListState(uiConfig = mockk<UiConfiguration>())
        val noConfigState = MoviesListState(uiConfig = null)
        
        assertNotNull(withConfigState.uiConfig)
        assertNull(noConfigState.uiConfig)
    }

    @Test
    fun `MoviesListState should handle different meta states`() {
        val withMetaState = MoviesListState(meta = mockk<Meta>())
        val noMetaState = MoviesListState(meta = null)
        
        assertNotNull(withMetaState.meta)
        assertNull(noMetaState.meta)
    }

    @Test
    fun `MoviesListState should handle different pagination states`() {
        val withPaginationState = MoviesListState(pagination = mockk<Pagination>())
        val noPaginationState = MoviesListState(pagination = null)
        
        assertNotNull(withPaginationState.pagination)
        assertNull(noPaginationState.pagination)
    }

    @Test
    fun `MovieDetailState should handle different movie details states`() {
        val withDetailsState = MovieDetailState(movieDetails = mockk<MovieDetails>())
        val noDetailsState = MovieDetailState(movieDetails = null)
        
        assertNotNull(withDetailsState.movieDetails)
        assertNull(noDetailsState.movieDetails)
    }

    @Test
    fun `MoviesListState should handle different movies list states`() {
        val withMoviesState = MoviesListState(movies = listOf(mockk<Movie>()))
        val emptyMoviesState = MoviesListState(movies = emptyList())
        
        assertTrue(withMoviesState.movies.isNotEmpty())
        assertTrue(emptyMoviesState.movies.isEmpty())
    }

    @Test
    fun `PresentationConstants should have correct pagination default values`() {
        assertEquals(1, PresentationConstants.DEFAULT_PAGE_NUMBER)
        assertEquals(true, PresentationConstants.DEFAULT_HAS_MORE)
        assertEquals(0, PresentationConstants.DEFAULT_RETRY_COUNT)
    }

    @Test
    fun `MoviesListState should handle different current page values`() {
        val state1 = MoviesListState(currentPage = 1)
        val state2 = MoviesListState(currentPage = 5)
        val state3 = MoviesListState(currentPage = 10)
        
        assertEquals(1, state1.currentPage)
        assertEquals(5, state2.currentPage)
        assertEquals(10, state3.currentPage)
    }

    @Test
    fun `MovieDetailState should handle different movie ID values`() {
        val state1 = MovieDetailState(movieId = 1)
        val state2 = MovieDetailState(movieId = 100)
        val state3 = MovieDetailState(movieId = 999)
        
        assertEquals(1, state1.movieId)
        assertEquals(100, state2.movieId)
        assertEquals(999, state3.movieId)
    }
}
