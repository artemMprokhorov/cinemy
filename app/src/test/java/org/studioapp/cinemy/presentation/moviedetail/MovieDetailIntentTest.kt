package org.studioapp.cinemy.presentation.moviedetail

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MovieDetailIntentTest {

    @Test
    fun `LoadMovieDetails should store movie ID correctly`() {
        // Given
        val movieId = 123

        // When
        val intent = MovieDetailIntent.LoadMovieDetails(movieId)

        // Then
        assertEquals(movieId, intent.movieId)
    }

    @Test
    fun `LoadMovieDetails should be equal when movie IDs are same`() {
        // Given
        val movieId = 123
        val intent1 = MovieDetailIntent.LoadMovieDetails(movieId)
        val intent2 = MovieDetailIntent.LoadMovieDetails(movieId)

        // When & Then
        assertEquals(intent1, intent2)
    }

    @Test
    fun `LoadMovieDetails should not be equal when movie IDs are different`() {
        // Given
        val intent1 = MovieDetailIntent.LoadMovieDetails(123)
        val intent2 = MovieDetailIntent.LoadMovieDetails(456)

        // When & Then
        assertFalse(intent1 == intent2)
    }

    @Test
    fun `LoadRecommendations should be singleton object`() {
        // Given
        val intent1 = MovieDetailIntent.LoadRecommendations
        val intent2 = MovieDetailIntent.LoadRecommendations

        // When & Then
        assertEquals(intent1, intent2)
        assertTrue(intent1 === intent2)
    }

    @Test
    fun `Retry should be singleton object`() {
        // Given
        val intent1 = MovieDetailIntent.Retry
        val intent2 = MovieDetailIntent.Retry

        // When & Then
        assertEquals(intent1, intent2)
        assertTrue(intent1 === intent2)
    }

    @Test
    fun `Refresh should be singleton object`() {
        // Given
        val intent1 = MovieDetailIntent.Refresh
        val intent2 = MovieDetailIntent.Refresh

        // When & Then
        assertEquals(intent1, intent2)
        assertTrue(intent1 === intent2)
    }

    @Test
    fun `BackPressed should be singleton object`() {
        // Given
        val intent1 = MovieDetailIntent.BackPressed
        val intent2 = MovieDetailIntent.BackPressed

        // When & Then
        assertEquals(intent1, intent2)
        assertTrue(intent1 === intent2)
    }

    @Test
    fun `ClearSentimentResult should be singleton object`() {
        // Given
        val intent1 = MovieDetailIntent.ClearSentimentResult
        val intent2 = MovieDetailIntent.ClearSentimentResult

        // When & Then
        assertEquals(intent1, intent2)
        assertTrue(intent1 === intent2)
    }

    @Test
    fun `all intents should be MovieDetailIntent instances`() {
        // Given
        val intents = listOf(
            MovieDetailIntent.LoadMovieDetails(123),
            MovieDetailIntent.LoadRecommendations,
            MovieDetailIntent.Retry,
            MovieDetailIntent.Refresh,
            MovieDetailIntent.BackPressed,
            MovieDetailIntent.ClearSentimentResult
        )

        // When & Then
        intents.forEach { intent ->
            assertTrue(intent is MovieDetailIntent)
        }
    }

    @Test
    fun `LoadMovieDetails should have correct string representation`() {
        // Given
        val movieId = 123
        val intent = MovieDetailIntent.LoadMovieDetails(movieId)

        // When
        val stringRep = intent.toString()

        // Then
        assertTrue(stringRep.contains("LoadMovieDetails"))
        assertTrue(stringRep.contains("movieId=123"))
    }

    @Test
    fun `LoadMovieDetails should support different movie IDs`() {
        // Given
        val movieIds = listOf(1, 123, 999, 1000)

        // When & Then
        movieIds.forEach { id ->
            val intent = MovieDetailIntent.LoadMovieDetails(id)
            assertEquals(id, intent.movieId)
        }
    }

    @Test
    fun `singleton intents should have correct string representation`() {
        // Given
        val intents = listOf(
            MovieDetailIntent.LoadRecommendations,
            MovieDetailIntent.Retry,
            MovieDetailIntent.Refresh,
            MovieDetailIntent.BackPressed,
            MovieDetailIntent.ClearSentimentResult
        )

        // When & Then
        intents.forEach { intent ->
            val stringRep = intent.toString()
            assertTrue(stringRep.contains(intent::class.simpleName ?: ""))
        }
    }
}
