package org.studioapp.cinemy.presentation.movieslist

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.studioapp.cinemy.data.model.Meta
import org.studioapp.cinemy.data.model.Movie
import org.studioapp.cinemy.data.model.UiConfiguration
import org.studioapp.cinemy.presentation.PresentationConstants

class MoviesListStateTest {

    @Test
    fun `default state should have correct default values`() {
        // When
        val state = MoviesListState()

        // Then
        assertFalse(state.isLoading)
        assertTrue(state.movies.isEmpty())
        assertNull(state.error)
        assertNull(state.uiConfig)
        assertNull(state.meta)
        assertEquals(PresentationConstants.DEFAULT_PAGE_NUMBER, state.currentPage)
        assertTrue(state.hasMore)
        assertFalse(state.isUsingMockData)
        assertEquals(MoviesListState.ConnectionStatus.Unknown, state.connectionStatus)
        assertNull(state.lastSyncTime)
        assertFalse(state.canRetry)
        assertEquals(PresentationConstants.DEFAULT_RETRY_COUNT, state.retryCount)
    }

    @Test
    fun `state should support all properties`() {
        // Given
        val movies = createMockMovies()
        val uiConfig = createMockUiConfiguration()
        val meta = createMockMeta()

        // When
        val state = MoviesListState(
            isLoading = true,
            movies = movies,
            error = "Test error",
            uiConfig = uiConfig,
            meta = meta,
            currentPage = 2,
            hasMore = true,
            isUsingMockData = true,
            connectionStatus = MoviesListState.ConnectionStatus.Connected,
            lastSyncTime = 1234567890L,
            canRetry = true,
            retryCount = 3
        )

        // Then
        assertTrue(state.isLoading)
        assertEquals(movies, state.movies)
        assertEquals("Test error", state.error)
        assertEquals(uiConfig, state.uiConfig)
        assertEquals(meta, state.meta)
        assertEquals(2, state.currentPage)
        assertTrue(state.hasMore)
        assertTrue(state.isUsingMockData)
        assertEquals(MoviesListState.ConnectionStatus.Connected, state.connectionStatus)
        assertEquals(1234567890L, state.lastSyncTime)
        assertTrue(state.canRetry)
        assertEquals(3, state.retryCount)
    }

    @Test
    fun `state should handle empty movies list`() {
        // Given
        val state = MoviesListState(movies = emptyList())

        // When & Then
        assertTrue(state.movies.isEmpty())
        assertTrue(state.hasMore)
    }

    @Test
    fun `state should handle movies list with items`() {
        // Given
        val movies = createMockMovies(5)
        val state = MoviesListState(
            movies = movies,
            hasMore = true
        )

        // When & Then
        assertEquals(5, state.movies.size)
        assertTrue(state.hasMore)
    }

    @Test
    fun `state should handle loading states correctly`() {
        // Given
        val state = MoviesListState(
            isLoading = true
        )

        // When & Then
        assertTrue(state.isLoading)
    }

    @Test
    fun `state should handle error states correctly`() {
        // Given
        val state = MoviesListState(
            error = "Network error",
            canRetry = true
        )

        // When & Then
        assertEquals("Network error", state.error)
        assertTrue(state.canRetry)
    }

    @Test
    fun `state should handle success states correctly`() {
        // Given
        val movies = createMockMovies(3)
        val state = MoviesListState(
            movies = movies,
            error = null,
            canRetry = false
        )

        // When & Then
        assertEquals(3, state.movies.size)
        assertNull(state.error)
        assertFalse(state.canRetry)
    }

    @Test
    fun `state should handle pagination states correctly`() {
        // Given
        val state = MoviesListState(
            currentPage = 3,
            hasMore = true
        )

        // When & Then
        assertEquals(3, state.currentPage)
        assertTrue(state.hasMore)
    }

    @Test
    fun `state should handle connection status correctly`() {
        // Given
        val state = MoviesListState(
            connectionStatus = MoviesListState.ConnectionStatus.Connected,
            isUsingMockData = false
        )

        // When & Then
        assertEquals(MoviesListState.ConnectionStatus.Connected, state.connectionStatus)
        assertFalse(state.isUsingMockData)
    }

    @Test
    fun `state should handle mock data status correctly`() {
        // Given
        val state = MoviesListState(
            connectionStatus = MoviesListState.ConnectionStatus.MockOnly,
            isUsingMockData = true
        )

        // When & Then
        assertEquals(MoviesListState.ConnectionStatus.MockOnly, state.connectionStatus)
        assertTrue(state.isUsingMockData)
    }

    // Helper methods to create mock objects
    private fun createMockMovies(count: Int = 3): List<Movie> {
        return (1..count).map { id ->
            mockk<Movie> {
                every { this@mockk.id } returns id
                every { this@mockk.title } returns "Movie $id"
            }
        }
    }

    private fun createMockUiConfiguration(): UiConfiguration {
        return mockk<UiConfiguration>()
    }

    private fun createMockMeta(): Meta {
        return mockk<Meta>()
    }
}
