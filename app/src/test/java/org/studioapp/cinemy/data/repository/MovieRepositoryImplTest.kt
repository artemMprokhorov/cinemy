package org.studioapp.cinemy.data.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.studioapp.cinemy.data.mcp.AssetDataLoader
import org.studioapp.cinemy.data.mcp.McpClient
import org.studioapp.cinemy.data.model.ButtonConfiguration
import org.studioapp.cinemy.data.model.ColorScheme
import org.studioapp.cinemy.data.model.GeminiColors
import org.studioapp.cinemy.data.model.Genre
import org.studioapp.cinemy.data.model.Meta
import org.studioapp.cinemy.data.model.Movie
import org.studioapp.cinemy.data.model.MovieDetails
import org.studioapp.cinemy.data.model.MovieDetailsData
import org.studioapp.cinemy.data.model.MovieDetailsResponse
import org.studioapp.cinemy.data.model.MovieListResponse
import org.studioapp.cinemy.data.model.ProductionCompany
import org.studioapp.cinemy.data.model.Result
import org.studioapp.cinemy.data.model.SentimentReviews
import org.studioapp.cinemy.data.model.TextConfiguration
import org.studioapp.cinemy.data.model.UiConfiguration

class MovieRepositoryImplTest {

    private lateinit var mockMcpClient: McpClient
    private lateinit var movieRepository: MovieRepositoryImpl

    @Before
    fun setUp() {
        mockMcpClient = mockk()
        val mockAssetDataLoader = mockk<AssetDataLoader>()
        movieRepository = MovieRepositoryImpl(mockMcpClient, mockAssetDataLoader)
    }

    @Test
    fun `getPopularMovies should return Success when MCP client returns Success`() = runBlocking {
        // Given
        val page = 1
        val mockMovieListResponse = createMockMovieListResponse()
        val mockUiConfig = createMockUiConfig()

        coEvery {
            mockMcpClient.getPopularMoviesViaMcp(page)
        } returns Result.Success(
            data = mockMovieListResponse,
            uiConfig = mockUiConfig
        )

        // When
        val result = movieRepository.getPopularMovies(page)

        // Then
        assertTrue(result is Result.Success)
        val successResult = result as Result.Success
        assertEquals(mockMovieListResponse, successResult.data)
        assertEquals(mockUiConfig, successResult.uiConfig)

        coVerify { mockMcpClient.getPopularMoviesViaMcp(page) }
    }

    @Test
    fun `getPopularMovies should return Error when MCP client returns Error`() = runBlocking {
        // Given
        val page = 1
        val errorMessage = "Network error"
        val mockUiConfig = createMockUiConfig()

        coEvery {
            mockMcpClient.getPopularMoviesViaMcp(page)
        } returns Result.Error(errorMessage, mockUiConfig)

        // When
        val result = movieRepository.getPopularMovies(page)

        // Then
        assertTrue(result is Result.Error)
        val errorResult = result as Result.Error
        assertEquals(errorMessage, errorResult.message)
        assertEquals(mockUiConfig, errorResult.uiConfig)

        coVerify { mockMcpClient.getPopularMoviesViaMcp(page) }
    }

    @Test
    fun `getPopularMovies should return Loading when MCP client returns Loading`() = runBlocking {
        // Given
        val page = 1

        coEvery {
            mockMcpClient.getPopularMoviesViaMcp(page)
        } returns Result.Loading

        // When
        val result = movieRepository.getPopularMovies(page)

        // Then
        assertTrue(result is Result.Loading)

        coVerify { mockMcpClient.getPopularMoviesViaMcp(page) }
    }

    @Test
    fun `getPopularMovies should return Error when MCP client throws exception`() = runBlocking {
        // Given
        val page = 1
        val exception = RuntimeException("Network failure")

        coEvery {
            mockMcpClient.getPopularMoviesViaMcp(page)
        } throws exception

        // When
        val result = movieRepository.getPopularMovies(page)

        // Then
        assertTrue(result is Result.Error)
        val errorResult = result as Result.Error
        assertTrue(errorResult.message.contains("Network failure"))
        assertTrue(errorResult.message.contains("Network error"))

        coVerify { mockMcpClient.getPopularMoviesViaMcp(page) }
    }

    @Test
    fun `getMovieDetails should return Success when MCP client returns Success`() = runBlocking {
        // Given
        val movieId = 123
        val mockMovieDetailsResponse = createMockMovieDetailsResponse()
        val mockUiConfig = createMockUiConfig()

        coEvery {
            mockMcpClient.getMovieDetailsViaMcp(movieId)
        } returns Result.Success(
            data = mockMovieDetailsResponse,
            uiConfig = mockUiConfig
        )

        // When
        val result = movieRepository.getMovieDetails(movieId)

        // Then
        assertTrue(result is Result.Success)
        val successResult = result as Result.Success
        assertEquals(mockMovieDetailsResponse, successResult.data)
        assertEquals(mockUiConfig, successResult.uiConfig)

        coVerify { mockMcpClient.getMovieDetailsViaMcp(movieId) }
    }

    @Test
    fun `getMovieDetails should return Error when MCP client returns Error`() = runBlocking {
        // Given
        val movieId = 123
        val errorMessage = "Movie not found"
        val mockUiConfig = createMockUiConfig()

        coEvery {
            mockMcpClient.getMovieDetailsViaMcp(movieId)
        } returns Result.Error(errorMessage, mockUiConfig)

        // When
        val result = movieRepository.getMovieDetails(movieId)

        // Then
        assertTrue(result is Result.Error)
        val errorResult = result as Result.Error
        assertEquals(errorMessage, errorResult.message)
        assertEquals(mockUiConfig, errorResult.uiConfig)

        coVerify { mockMcpClient.getMovieDetailsViaMcp(movieId) }
    }

    @Test
    fun `getMovieDetails should return Loading when MCP client returns Loading`() = runBlocking {
        // Given
        val movieId = 123

        coEvery {
            mockMcpClient.getMovieDetailsViaMcp(movieId)
        } returns Result.Loading

        // When
        val result = movieRepository.getMovieDetails(movieId)

        // Then
        assertTrue(result is Result.Loading)

        coVerify { mockMcpClient.getMovieDetailsViaMcp(movieId) }
    }

    @Test
    fun `getMovieDetails should return Error when MCP client throws exception`() = runBlocking {
        // Given
        val movieId = 123
        val exception = RuntimeException("Service unavailable")

        coEvery {
            mockMcpClient.getMovieDetailsViaMcp(movieId)
        } throws exception

        // When
        val result = movieRepository.getMovieDetails(movieId)

        // Then
        assertTrue(result is Result.Error)
        val errorResult = result as Result.Error
        assertTrue(errorResult.message.contains("Service unavailable"))
        assertTrue(errorResult.message.contains("Network error"))

        coVerify { mockMcpClient.getMovieDetailsViaMcp(movieId) }
    }

    @Test
    fun `getPopularMovies should handle null exception message`() = runBlocking {
        // Given
        val page = 1
        val exception = RuntimeException()

        coEvery {
            mockMcpClient.getPopularMoviesViaMcp(page)
        } throws exception

        // When
        val result = movieRepository.getPopularMovies(page)

        // Then
        assertTrue(result is Result.Error)
        val errorResult = result as Result.Error
        assertTrue(errorResult.message.contains("Unknown error"))

        coVerify { mockMcpClient.getPopularMoviesViaMcp(page) }
    }

    @Test
    fun `getMovieDetails should handle null exception message`() = runBlocking {
        // Given
        val movieId = 123
        val exception = RuntimeException()

        coEvery {
            mockMcpClient.getMovieDetailsViaMcp(movieId)
        } throws exception

        // When
        val result = movieRepository.getMovieDetails(movieId)

        // Then
        assertTrue(result is Result.Error)
        val errorResult = result as Result.Error
        assertTrue(errorResult.message.contains("Unknown error"))

        coVerify { mockMcpClient.getMovieDetailsViaMcp(movieId) }
    }

    private fun createMockMovieListResponse(): MovieListResponse {
        return MovieListResponse(
            page = 1,
            results = listOf(
                Movie(
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
                    adult = false,
                    originalLanguage = "en",
                    originalTitle = "Test Movie",
                    video = false,
                    colors = org.studioapp.cinemy.data.model.MovieColors(
                        accent = "#FF0000",
                        primary = "#00FF00",
                        secondary = "#0000FF",
                        metadata = org.studioapp.cinemy.data.model.ColorMetadata(
                            category = "action",
                            modelUsed = true,
                            rating = 8.5
                        )
                    )
                )
            ),
            totalPages = 10,
            totalResults = 100
        )
    }

    private fun createMockMovieDetailsResponse(): MovieDetailsResponse {
        return MovieDetailsResponse(
            success = true,
            data = MovieDetailsData(
                movieDetails = MovieDetails(
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
                ),
                sentimentReviews = SentimentReviews(
                    positive = listOf("amazing", "brilliant"),
                    negative = listOf("boring", "terrible")
                )
            ),
            uiConfig = createMockUiConfig(),
            meta = Meta(
                timestamp = "2024-01-01T00:00:00Z",
                method = "getMovieDetails",
                movieId = 123,
                resultsCount = 1,
                aiGenerated = true,
                geminiColors = GeminiColors(
                    primary = "#6200EE",
                    secondary = "#03DAC6",
                    accent = "#FF6B6B"
                ),
                version = "2.0.0"
            )
        )
    }

    private fun createMockUiConfig(): UiConfiguration {
        return UiConfiguration(
            colors = ColorScheme(
                primary = androidx.compose.ui.graphics.Color(0xFF6200EE),
                secondary = androidx.compose.ui.graphics.Color(0xFF03DAC6),
                background = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
                surface = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
                onPrimary = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
                onSecondary = androidx.compose.ui.graphics.Color(0xFF000000),
                onBackground = androidx.compose.ui.graphics.Color(0xFF000000),
                onSurface = androidx.compose.ui.graphics.Color(0xFF000000),
                moviePosterColors = listOf(
                    androidx.compose.ui.graphics.Color(0xFF6200EE),
                    androidx.compose.ui.graphics.Color(0xFF03DAC6)
                )
            ),
            texts = TextConfiguration(
                appTitle = "Cinemy",
                loadingText = "Loading...",
                errorMessage = "An error occurred",
                noMoviesFound = "No movies found",
                retryButton = "Retry",
                backButton = "Back",
                playButton = "Play"
            ),
            buttons = ButtonConfiguration(
                primaryButtonColor = androidx.compose.ui.graphics.Color(0xFF6200EE),
                secondaryButtonColor = androidx.compose.ui.graphics.Color(0xFF03DAC6),
                buttonTextColor = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
                buttonCornerRadius = 8
            )
        )
    }
}
