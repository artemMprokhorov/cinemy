package org.studioapp.cinemy.presentation.moviedetail

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.studioapp.cinemy.data.model.Meta
import org.studioapp.cinemy.data.model.MovieDetails
import org.studioapp.cinemy.data.model.SentimentReviews
import org.studioapp.cinemy.data.model.UiConfiguration
import org.studioapp.cinemy.ml.model.SentimentResult

class MovieDetailStateTest {

    @Test
    fun `default state should have correct default values`() {
        // When
        val state = MovieDetailState()

        // Then
        assertFalse(state.isLoading)
        assertNull(state.movieDetails)
        assertNull(state.error)
        assertNull(state.uiConfig)
        // meta, movieId, showFullDescription, showProductionDetails were removed during cleanup
        assertNull(state.sentimentResult)
        assertNull(state.sentimentError)
        assertNull(state.sentimentReviews)
    }

    @Test
    fun `formattedRuntime should format runtime correctly for hours and minutes`() {
        // Given
        val movieDetails = createMockMovieDetails(runtime = 125) // 2 hours 5 minutes
        val state = MovieDetailState(movieDetails = movieDetails)

        // When
        val formattedRuntime = state.formattedRuntime

        // Then
        assertEquals("2h 5m", formattedRuntime)
    }

    @Test
    fun `formattedRuntime should format runtime correctly for only minutes`() {
        // Given
        val movieDetails = createMockMovieDetails(runtime = 45) // 45 minutes
        val state = MovieDetailState(movieDetails = movieDetails)

        // When
        val formattedRuntime = state.formattedRuntime

        // Then
        assertEquals("0h 45m", formattedRuntime)
    }

    @Test
    fun `formattedRuntime should format runtime correctly for only hours`() {
        // Given
        val movieDetails = createMockMovieDetails(runtime = 120) // 2 hours exactly
        val state = MovieDetailState(movieDetails = movieDetails)

        // When
        val formattedRuntime = state.formattedRuntime

        // Then
        assertEquals("2h 0m", formattedRuntime)
    }

    @Test
    fun `formattedRuntime should return empty string when movieDetails is null`() {
        // Given
        val state = MovieDetailState(movieDetails = null)

        // When
        val formattedRuntime = state.formattedRuntime

        // Then
        assertEquals("", formattedRuntime)
    }

    @Test
    fun `formattedBudget should format budget correctly for large amounts`() {
        // Given
        val movieDetails = createMockMovieDetails(budget = 150000000L) // $150M
        val state = MovieDetailState(movieDetails = movieDetails)

        // When
        val formattedBudget = state.formattedBudget

        // Then
        assertEquals("$150M", formattedBudget)
    }

    @Test
    fun `formattedBudget should format budget correctly for very large amounts`() {
        // Given
        val movieDetails = createMockMovieDetails(budget = 1500000000L) // $1.5B
        val state = MovieDetailState(movieDetails = movieDetails)

        // When
        val formattedBudget = state.formattedBudget

        // Then
        assertEquals("$1500M", formattedBudget)
    }

    @Test
    fun `formattedBudget should format small amounts as zero`() {
        // Given
        val movieDetails = createMockMovieDetails(budget = 100000L) // Small amount
        val state = MovieDetailState(movieDetails = movieDetails)

        // When
        val formattedBudget = state.formattedBudget

        // Then
        assertEquals("$0M", formattedBudget)
    }

    @Test
    fun `formattedBudget should return empty string when movieDetails is null`() {
        // Given
        val state = MovieDetailState(movieDetails = null)

        // When
        val formattedBudget = state.formattedBudget

        // Then
        assertEquals("", formattedBudget)
    }

    @Test
    fun `state should support all properties`() {
        // Given
        val movieDetails = createMockMovieDetails()
        val uiConfig = createMockUiConfiguration()
        val meta = createMockMeta()
        val sentimentResult = createMockSentimentResult()
        val sentimentReviews = createMockSentimentReviews()

        // When
        val state = MovieDetailState(
            isLoading = true,
            movieDetails = movieDetails,
            error = "Test error",
            uiConfig = uiConfig,
            meta = meta,
            // movieId, showFullDescription, showProductionDetails were removed during cleanup
            sentimentResult = sentimentResult,
            sentimentError = "Sentiment error",
            sentimentReviews = sentimentReviews
        )

        // Then
        assertTrue(state.isLoading)
        assertEquals(movieDetails, state.movieDetails)
        assertEquals("Test error", state.error)
        assertEquals(uiConfig, state.uiConfig)
        assertEquals(meta, state.meta)
        // movieId, showFullDescription, showProductionDetails were removed during cleanup
        assertEquals(sentimentResult, state.sentimentResult)
        assertEquals("Sentiment error", state.sentimentError)
        assertEquals(sentimentReviews, state.sentimentReviews)
    }

    // Helper methods to create mock objects
    private fun createMockMovieDetails(
        id: Int = 123,
        title: String = "Test Movie",
        runtime: Int = 120,
        budget: Long = 100000000L
    ): MovieDetails {
        return mockk<MovieDetails> {
            every { this@mockk.id } returns id
            every { this@mockk.title } returns title
            every { this@mockk.runtime } returns runtime
            every { this@mockk.budget } returns budget
        }
    }

    private fun createMockUiConfiguration(): UiConfiguration {
        return mockk<UiConfiguration>()
    }

    private fun createMockMeta(): Meta {
        return mockk<Meta>()
    }

    private fun createMockSentimentResult(): SentimentResult {
        return mockk<SentimentResult>()
    }

    private fun createMockSentimentReviews(): SentimentReviews {
        return mockk<SentimentReviews>()
    }
}
