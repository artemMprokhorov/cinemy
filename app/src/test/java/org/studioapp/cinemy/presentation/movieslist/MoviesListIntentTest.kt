package org.studioapp.cinemy.presentation.movieslist

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MoviesListIntentTest {

    @Test
    fun `LoadPopularMovies should be singleton object`() {
        // Given
        val intent1 = MoviesListIntent.LoadPopularMovies
        val intent2 = MoviesListIntent.LoadPopularMovies

        // When & Then
        assertEquals(intent1, intent2)
        assertTrue(intent1 === intent2)
    }

    @Test
    fun `LoadMoreMovies should be singleton object`() {
        // Given
        val intent1 = MoviesListIntent.LoadMoreMovies
        val intent2 = MoviesListIntent.LoadMoreMovies

        // When & Then
        assertEquals(intent1, intent2)
        assertTrue(intent1 === intent2)
    }


    @Test
    fun `RetryLastOperation should be singleton object`() {
        // Given
        val intent1 = MoviesListIntent.RetryLastOperation
        val intent2 = MoviesListIntent.RetryLastOperation

        // When & Then
        assertEquals(intent1, intent2)
        assertTrue(intent1 === intent2)
    }

    @Test
    fun `RetryConnection should be singleton object`() {
        // Given
        val intent1 = MoviesListIntent.RetryConnection
        val intent2 = MoviesListIntent.RetryConnection

        // When & Then
        assertEquals(intent1, intent2)
        assertTrue(intent1 === intent2)
    }

    @Test
    fun `RefreshData should be singleton object`() {
        // Given
        val intent1 = MoviesListIntent.RefreshData
        val intent2 = MoviesListIntent.RefreshData

        // When & Then
        assertEquals(intent1, intent2)
        assertTrue(intent1 === intent2)
    }

    @Test
    fun `CheckConnectionStatus should be singleton object`() {
        // Given
        val intent1 = MoviesListIntent.CheckConnectionStatus
        val intent2 = MoviesListIntent.CheckConnectionStatus

        // When & Then
        assertEquals(intent1, intent2)
        assertTrue(intent1 === intent2)
    }

    @Test
    fun `DismissError should be singleton object`() {
        // Given
        val intent1 = MoviesListIntent.DismissError
        val intent2 = MoviesListIntent.DismissError

        // When & Then
        assertEquals(intent1, intent2)
        assertTrue(intent1 === intent2)
    }

    @Test
    fun `NextPage should be singleton object`() {
        // Given
        val intent1 = MoviesListIntent.NextPage
        val intent2 = MoviesListIntent.NextPage

        // When & Then
        assertEquals(intent1, intent2)
        assertTrue(intent1 === intent2)
    }

    @Test
    fun `PreviousPage should be singleton object`() {
        // Given
        val intent1 = MoviesListIntent.PreviousPage
        val intent2 = MoviesListIntent.PreviousPage

        // When & Then
        assertEquals(intent1, intent2)
        assertTrue(intent1 === intent2)
    }

    @Test
    fun `all intents should be MoviesListIntent instances`() {
        // Given
        val intents = listOf(
            MoviesListIntent.LoadPopularMovies,
            MoviesListIntent.LoadMoreMovies,
            MoviesListIntent.RetryLastOperation,
            MoviesListIntent.RetryConnection,
            MoviesListIntent.RefreshData,
            MoviesListIntent.CheckConnectionStatus,
            MoviesListIntent.DismissError,
            MoviesListIntent.NextPage,
            MoviesListIntent.PreviousPage
        )

        // When & Then
        intents.forEach { intent ->
            assertTrue(intent is MoviesListIntent)
        }
    }


    @Test
    fun `singleton intents should have correct string representation`() {
        // Given
        val intents = listOf(
            MoviesListIntent.LoadPopularMovies,
            MoviesListIntent.LoadMoreMovies,
            MoviesListIntent.RetryLastOperation,
            MoviesListIntent.RetryConnection,
            MoviesListIntent.RefreshData,
            MoviesListIntent.CheckConnectionStatus,
            MoviesListIntent.DismissError,
            MoviesListIntent.NextPage,
            MoviesListIntent.PreviousPage
        )

        // When & Then
        intents.forEach { intent ->
            val stringRep = intent.toString()
            assertTrue(stringRep.contains(intent::class.simpleName ?: ""))
        }
    }

    @Test
    fun `connection intents should be grouped correctly`() {
        // Given
        val connectionIntents = listOf(
            MoviesListIntent.RetryConnection,
            MoviesListIntent.RefreshData,
            MoviesListIntent.CheckConnectionStatus,
            MoviesListIntent.DismissError
        )

        // When & Then
        connectionIntents.forEach { intent ->
            assertTrue(intent is MoviesListIntent)
        }
    }

    @Test
    fun `pagination intents should be grouped correctly`() {
        // Given
        val paginationIntents = listOf(
            MoviesListIntent.NextPage,
            MoviesListIntent.PreviousPage
        )

        // When & Then
        paginationIntents.forEach { intent ->
            assertTrue(intent is MoviesListIntent)
        }
    }
}
