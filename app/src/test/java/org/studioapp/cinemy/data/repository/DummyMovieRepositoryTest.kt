package org.studioapp.cinemy.data.repository

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.studioapp.cinemy.data.mcp.AssetDataLoader
import org.studioapp.cinemy.data.model.Genre
import org.studioapp.cinemy.data.model.MovieDetails
import org.studioapp.cinemy.data.model.ProductionCompany
import org.studioapp.cinemy.data.model.Result
import org.studioapp.cinemy.data.model.SentimentReviews
import org.studioapp.cinemy.data.remote.dto.MovieDto

class DummyMovieRepositoryTest {

    private lateinit var mockContext: Context
    private lateinit var mockAssetDataLoader: AssetDataLoader
    private lateinit var dummyRepository: DummyMovieRepository

    @Before
    fun setUp() {
        mockContext = mockk()
        mockAssetDataLoader = mockk()
        dummyRepository = DummyMovieRepository(mockContext)

        // Use reflection to replace the private assetDataLoader
        val field = DummyMovieRepository::class.java.getDeclaredField("assetDataLoader")
        field.isAccessible = true
        field.set(dummyRepository, mockAssetDataLoader)
    }

    @Test
    fun `getPopularMovies should return Success with mock data`() = runBlocking {
        // Given
        val page = 1
        val mockMovies = listOf(
            MovieDto(
                id = 1,
                title = "Test Movie 1",
                description = "Test Description 1",
                posterPath = "/test1.jpg",
                backdropPath = "/backdrop1.jpg",
                rating = 8.5,
                voteCount = 1000,
                releaseDate = "2024-01-01",
                genreIds = listOf(1, 2),
                popularity = 100.0,
                adult = false
            ),
            MovieDto(
                id = 2,
                title = "Test Movie 2",
                description = "Test Description 2",
                posterPath = "/test2.jpg",
                backdropPath = "/backdrop2.jpg",
                rating = 7.5,
                voteCount = 800,
                releaseDate = "2024-01-02",
                genreIds = listOf(3, 4),
                popularity = 90.0,
                adult = false
            )
        )

        every { mockAssetDataLoader.loadMockMovies() } returns mockMovies

        // When
        val result = dummyRepository.getPopularMovies(page)

        // Then
        assertTrue(result is Result.Success)
        val successResult = result as Result.Success
        val response = successResult.data

        assertTrue(response.success)
        assertEquals(2, response.data.movies.size)
        assertEquals("Test Movie 1", response.data.movies[0].title)
        assertEquals("Test Movie 2", response.data.movies[1].title)
        assertEquals(page, response.data.pagination.page)
        assertTrue(response.data.pagination.hasNext)
        assertFalse(response.data.pagination.hasPrevious)

        verify { mockAssetDataLoader.loadMockMovies() }
    }

    @Test
    fun `getPopularMovies should handle different page numbers`() = runBlocking {
        // Given
        val page = 3
        val mockMovies = listOf(createMockMovieDto())

        every { mockAssetDataLoader.loadMockMovies() } returns mockMovies

        // When
        val result = dummyRepository.getPopularMovies(page)

        // Then
        assertTrue(result is Result.Success)
        val successResult = result as Result.Success
        val response = successResult.data

        assertEquals(page, response.data.pagination.page)
        assertTrue(response.data.pagination.hasPrevious) // page > 1
        assertTrue(response.data.pagination.hasNext) // page < totalPages

        verify { mockAssetDataLoader.loadMockMovies() }
    }

    @Test
    fun `getMovieDetails should return Success with mock data`() = runBlocking {
        // Given
        val movieId = 123

        // When
        val result = dummyRepository.getMovieDetails(movieId)

        // Then
        assertTrue(result is Result.Success)
        val successResult = result as Result.Success
        val response = successResult.data

        assertTrue(response.success)
        assertNotNull(response.data.movieDetails)
        // sentimentReviews can be null if not found in assets
        // assertNotNull(response.data.sentimentReviews)
    }

    @Test
    fun `getPopularMovies should create correct pagination for first page`() = runBlocking {
        // Given
        val page = 1
        val mockMovies = listOf(createMockMovieDto())

        every { mockAssetDataLoader.loadMockMovies() } returns mockMovies

        // When
        val result = dummyRepository.getPopularMovies(page)

        // Then
        assertTrue(result is Result.Success)
        val successResult = result as Result.Success
        val pagination = successResult.data.data.pagination

        assertEquals(1, pagination.page)
        assertFalse(pagination.hasPrevious) // First page
        assertTrue(pagination.hasNext) // Has next page

        verify { mockAssetDataLoader.loadMockMovies() }
    }

    @Test
    fun `getPopularMovies should create correct pagination for last page`() = runBlocking {
        // Given
        val page = 8 // totalPages = 8 according to StringConstants
        val mockMovies = listOf(createMockMovieDto())

        every { mockAssetDataLoader.loadMockMovies() } returns mockMovies

        // When
        val result = dummyRepository.getPopularMovies(page)

        // Then
        assertTrue(result is Result.Success)
        val successResult = result as Result.Success
        val pagination = successResult.data.data.pagination

        assertEquals(page, pagination.page)
        assertTrue(pagination.hasPrevious) // Not first page
        assertFalse(pagination.hasNext) // Last page

        verify { mockAssetDataLoader.loadMockMovies() }
    }

    @Test
    fun `getPopularMovies should handle empty movie list`() = runBlocking {
        // Given
        val page = 1
        val emptyMovies = emptyList<MovieDto>()

        every { mockAssetDataLoader.loadMockMovies() } returns emptyMovies

        // When
        val result = dummyRepository.getPopularMovies(page)

        // Then
        assertTrue(result is Result.Success)
        val successResult = result as Result.Success
        val response = successResult.data

        assertTrue(response.success)
        assertTrue(response.data.movies.isEmpty())
        assertEquals(0, response.data.movies.size)

        verify { mockAssetDataLoader.loadMockMovies() }
    }

    @Test
    fun `getMovieDetails should handle different movie IDs`() = runBlocking {
        // Given
        val movieId1 = 123
        val movieId2 = 456

        // When
        val result1 = dummyRepository.getMovieDetails(movieId1)
        val result2 = dummyRepository.getMovieDetails(movieId2)

        // Then
        assertTrue(result1 is Result.Success)
        assertTrue(result2 is Result.Success)

        val successResult1 = result1 as Result.Success
        val successResult2 = result2 as Result.Success

        assertTrue(successResult1.data.success)
        assertTrue(successResult2.data.success)
    }

    @Test
    fun `getPopularMovies should include UI configuration`() = runBlocking {
        // Given
        val page = 1
        val mockMovies = listOf(createMockMovieDto())

        every { mockAssetDataLoader.loadMockMovies() } returns mockMovies

        // When
        val result = dummyRepository.getPopularMovies(page)

        // Then
        assertTrue(result is Result.Success)
        val successResult = result as Result.Success
        val response = successResult.data

        assertNotNull(response.uiConfig)
        assertNotNull(response.uiConfig.colors)
        assertNotNull(response.uiConfig.texts)
        assertNotNull(response.uiConfig.buttons)

        verify { mockAssetDataLoader.loadMockMovies() }
    }

    @Test
    fun `getMovieDetails should include UI configuration`() = runBlocking {
        // Given
        val movieId = 123

        // When
        val result = dummyRepository.getMovieDetails(movieId)

        // Then
        assertTrue(result is Result.Success)
        val successResult = result as Result.Success
        val response = successResult.data

        assertNotNull(response.uiConfig)
        assertNotNull(response.uiConfig.colors)
        assertNotNull(response.uiConfig.texts)
        assertNotNull(response.uiConfig.buttons)
    }

    @Test
    fun `getPopularMovies should include meta information`() = runBlocking {
        // Given
        val page = 1
        val mockMovies = listOf(createMockMovieDto())

        every { mockAssetDataLoader.loadMockMovies() } returns mockMovies

        // When
        val result = dummyRepository.getPopularMovies(page)

        // Then
        assertTrue(result is Result.Success)
        val successResult = result as Result.Success
        val response = successResult.data

        assertNotNull(response.meta)
        assertNotNull(response.meta.timestamp)
        assertNotNull(response.meta.method)
        assertNotNull(response.meta.version)

        verify { mockAssetDataLoader.loadMockMovies() }
    }

    @Test
    fun `getMovieDetails should include meta information`() = runBlocking {
        // Given
        val movieId = 123

        // When
        val result = dummyRepository.getMovieDetails(movieId)

        // Then
        assertTrue(result is Result.Success)
        val successResult = result as Result.Success
        val response = successResult.data

        assertNotNull(response.meta)
        assertNotNull(response.meta.timestamp)
        assertNotNull(response.meta.method)
        assertNotNull(response.meta.version)
    }

    private fun createMockMovieDto(): MovieDto {
        return MovieDto(
            id = 1,
            title = "Test Movie",
            description = "Test Description",
            posterPath = "/test.jpg",
            backdropPath = "/backdrop.jpg",
            rating = 8.5,
            voteCount = 1000,
            releaseDate = "2024-01-01",
            genreIds = listOf(1, 2, 3),
            popularity = 100.0,
            adult = false
        )
    }

    private fun createMockMovieDetails(): MovieDetails {
        return MovieDetails(
            id = 123,
            title = "Test Movie Details",
            description = "Test Description",
            posterPath = "/test.jpg",
            backdropPath = "/backdrop.jpg",
            rating = 8.5,
            voteCount = 1000,
            releaseDate = "2024-01-01",
            runtime = 120,
            genres = listOf(
                Genre(1, "Action"),
                Genre(2, "Drama")
            ),
            productionCompanies = listOf(
                ProductionCompany(
                    id = 1,
                    logoPath = "/logo.jpg",
                    name = "Test Studio",
                    originCountry = "US"
                )
            ),
            budget = 1000000L,
            revenue = 2000000L,
            status = "Released"
        )
    }

    private fun createMockSentimentReviews(): SentimentReviews {
        return SentimentReviews(
            positive = listOf("amazing", "brilliant", "outstanding"),
            negative = listOf("boring", "terrible", "awful")
        )
    }
}
