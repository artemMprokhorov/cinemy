package org.studioapp.cinemy.data.mcp

import android.content.Context
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.studioapp.cinemy.data.mapper.MovieMapper
import org.studioapp.cinemy.data.mcp.models.McpResponse
import org.studioapp.cinemy.data.model.ButtonConfiguration
import org.studioapp.cinemy.data.model.ColorMetadata
import org.studioapp.cinemy.data.model.ColorScheme
import org.studioapp.cinemy.data.model.Genre
import org.studioapp.cinemy.data.model.Movie
import org.studioapp.cinemy.data.model.MovieColors
import org.studioapp.cinemy.data.model.MovieDetails
import org.studioapp.cinemy.data.model.Pagination
import org.studioapp.cinemy.data.model.ProductionCompany
import org.studioapp.cinemy.data.model.Result
import org.studioapp.cinemy.data.model.SearchInfo
import org.studioapp.cinemy.data.model.TextConfiguration
import org.studioapp.cinemy.data.model.UiConfiguration
import org.studioapp.cinemy.data.remote.dto.ButtonConfigurationDto
import org.studioapp.cinemy.data.remote.dto.ColorMetadataDto
import org.studioapp.cinemy.data.remote.dto.ColorSchemeDto
import org.studioapp.cinemy.data.remote.dto.GeminiColorsDto
import org.studioapp.cinemy.data.remote.dto.GenreDto
import org.studioapp.cinemy.data.remote.dto.MetaDto
import org.studioapp.cinemy.data.remote.dto.MovieColorsDto
import org.studioapp.cinemy.data.remote.dto.MovieDetailsDto
import org.studioapp.cinemy.data.remote.dto.MovieDto
import org.studioapp.cinemy.data.remote.dto.PaginationDto
import org.studioapp.cinemy.data.remote.dto.ProductionCompanyDto
import org.studioapp.cinemy.data.remote.dto.SearchInfoDto
import org.studioapp.cinemy.data.remote.dto.TextConfigurationDto
import org.studioapp.cinemy.data.remote.dto.UiConfigurationDto
import org.studioapp.cinemy.data.util.ColorUtils
import org.studioapp.cinemy.data.util.TestColorUtils

object TestMovieMapper {
    fun mapUiConfigurationDtoToUiConfiguration(dto: UiConfigurationDto): UiConfiguration {
        return UiConfiguration(
            colors = mapColorSchemeDtoToColorScheme(dto.colors),
            texts = mapTextConfigurationDtoToTextConfiguration(dto.texts),
            buttons = mapButtonConfigurationDtoToButtonConfiguration(dto.buttons),
            searchInfo = dto.searchInfo?.let { mapSearchInfoDtoToSearchInfo(it) }
        )
    }

    fun mapColorSchemeDtoToColorScheme(dto: ColorSchemeDto): ColorScheme {
        return ColorScheme(
            primary = TestColorUtils.parseColor(dto.primary),
            secondary = TestColorUtils.parseColor(dto.secondary),
            background = TestColorUtils.parseColor(dto.background),
            surface = TestColorUtils.parseColor(dto.surface),
            onPrimary = TestColorUtils.parseColor(dto.onPrimary),
            onSecondary = TestColorUtils.parseColor(dto.onSecondary),
            onBackground = TestColorUtils.parseColor(dto.onBackground),
            onSurface = TestColorUtils.parseColor(dto.onSurface),
            moviePosterColors = dto.moviePosterColors.map { TestColorUtils.parseColor(it) }
        )
    }

    fun mapTextConfigurationDtoToTextConfiguration(dto: TextConfigurationDto): TextConfiguration {
        return TextConfiguration(
            appTitle = dto.appTitle,
            loadingText = dto.loadingText,
            errorMessage = dto.errorMessage,
            noMoviesFound = dto.noMoviesFound,
            retryButton = dto.retryButton,
            backButton = dto.backButton,
            playButton = dto.playButton
        )
    }

    fun mapButtonConfigurationDtoToButtonConfiguration(dto: ButtonConfigurationDto): ButtonConfiguration {
        return ButtonConfiguration(
            primaryButtonColor = TestColorUtils.parseColor(dto.primaryButtonColor),
            secondaryButtonColor = TestColorUtils.parseColor(dto.secondaryButtonColor),
            buttonTextColor = TestColorUtils.parseColor(dto.buttonTextColor),
            buttonCornerRadius = dto.buttonCornerRadius
        )
    }

    fun mapSearchInfoDtoToSearchInfo(dto: SearchInfoDto): SearchInfo {
        return SearchInfo(
            query = dto.query,
            resultCount = dto.resultCount,
            avgRating = dto.avgRating,
            ratingType = dto.ratingType,
            colorBased = dto.colorBased
        )
    }


    fun mapMovieDtoToMovie(dto: MovieDto): Movie {
        return Movie(
            id = dto.id,
            title = dto.title,
            description = dto.description,
            posterPath = dto.posterPath,
            backdropPath = dto.backdropPath,
            rating = dto.rating,
            voteCount = dto.voteCount,
            releaseDate = dto.releaseDate,
            genreIds = dto.genreIds,
            popularity = dto.popularity,
            adult = dto.adult,
            originalLanguage = dto.originalLanguage,
            originalTitle = dto.originalTitle,
            video = dto.video,
            colors = MovieColors(
                accent = dto.colors.accent,
                primary = dto.colors.primary,
                secondary = dto.colors.secondary,
                metadata = ColorMetadata(
                    category = dto.colors.metadata.category,
                    modelUsed = dto.colors.metadata.modelUsed,
                    rating = dto.colors.metadata.rating
                )
            )
        )
    }

    fun mapPaginationDtoToPagination(dto: PaginationDto): Pagination {
        return Pagination(
            page = dto.page,
            totalPages = dto.totalPages,
            totalResults = dto.totalResults,
            hasNext = dto.hasNext,
            hasPrevious = dto.hasPrevious
        )
    }

    fun mapMovieDetailsDtoToMovieDetails(dto: MovieDetailsDto): MovieDetails {
        return MovieDetails(
            id = dto.id,
            title = dto.title,
            description = dto.description,
            posterPath = dto.posterPath,
            backdropPath = dto.backdropPath,
            rating = dto.rating,
            voteCount = dto.voteCount,
            releaseDate = dto.releaseDate,
            runtime = dto.runtime ?: 0,
            genres = dto.genres.map { mapGenreDtoToGenre(it) },
            productionCompanies = dto.productionCompanies.map {
                mapProductionCompanyDtoToProductionCompany(
                    it
                )
            },
            budget = dto.budget,
            revenue = dto.revenue,
            status = dto.status
        )
    }

    fun mapGenreDtoToGenre(dto: GenreDto): Genre {
        return Genre(
            id = dto.id,
            name = dto.name
        )
    }

    fun mapProductionCompanyDtoToProductionCompany(dto: ProductionCompanyDto): ProductionCompany {
        return ProductionCompany(
            id = dto.id,
            logoPath = dto.logoPath,
            name = dto.name,
            originCountry = dto.originCountry
        )
    }
}


class McpClientTest {

    private lateinit var mockContext: Context
    private lateinit var mockAssetDataLoader: AssetDataLoader
    private lateinit var mockMcpHttpClient: McpHttpClient
    private lateinit var mcpClient: McpClient

    @Before
    fun setUp() {
        mockContext = mockk()
        mockAssetDataLoader = mockk()
        mockMcpHttpClient = mockk()

        // Create McpClient and use reflection to inject mocks
        mcpClient = McpClient(mockContext)

        // Use reflection to replace private fields
        val assetDataLoaderField = McpClient::class.java.getDeclaredField("assetDataLoader")
        assetDataLoaderField.isAccessible = true
        assetDataLoaderField.set(mcpClient, mockAssetDataLoader)

        val mcpHttpClientField = McpClient::class.java.getDeclaredField("mcpHttpClient")
        mcpHttpClientField.isAccessible = true
        mcpHttpClientField.set(mcpClient, mockMcpHttpClient)

        // Mock ColorUtils to use TestColorUtils
        mockkObject(ColorUtils)
        every { ColorUtils.parseColor(any()) } answers {
            val colorString = firstArg<String>()
            TestColorUtils.parseColor(colorString)
        }

        // Mock MovieMapper methods to use TestMovieMapper
        mockkObject(MovieMapper)
        every { MovieMapper.mapUiConfigurationDtoToUiConfiguration(any()) } answers { call ->
            val dto = call.invocation.args[0] as UiConfigurationDto
            TestMovieMapper.mapUiConfigurationDtoToUiConfiguration(dto)
        }
        every { MovieMapper.mapMovieDetailsDtoToMovieDetails(any()) } answers { call ->
            val dto = call.invocation.args[0] as MovieDetailsDto
            TestMovieMapper.mapMovieDetailsDtoToMovieDetails(dto)
        }
    }

    @Test
    fun `getPopularMovies should return success response when MCP call succeeds`() = runBlocking {
        // Given
        val page = 1
        val mockResponse = createMockMcpResponse()
        val mockUiConfig = createMockUiConfigDto()
        val mockMeta = createMockMetaDto()

        coEvery { mockMcpHttpClient.sendRequest<Map<String, Any>>(any()) } returns mockResponse
        every { mockAssetDataLoader.loadUiConfig() } returns mockUiConfig
        every { mockAssetDataLoader.loadMetaData(any(), any()) } returns mockMeta

        // When
        val result = mcpClient.getPopularMovies(page)

        // Then
        assertTrue(result.success)
        assertNotNull(result.data)
        assertNotNull(result.uiConfig)
        assertNotNull(result.meta)
        assertEquals(1, result.data?.results?.size)
        assertEquals("Test Movie", result.data?.results?.get(0)?.title)

        coVerify { mockMcpHttpClient.sendRequest<Map<String, Any>>(any()) }
        verify { mockAssetDataLoader.loadUiConfig() }
        verify { mockAssetDataLoader.loadMetaData(any(), any()) }
    }

    @Test
    fun `getPopularMovies should return error response when MCP call fails`() = runBlocking {
        // Given
        val page = 1
        val mockResponse = McpResponse<Map<String, Any>>(
            success = false,
            data = null,
            error = "Network error"
        )
        val mockUiConfig = createMockUiConfigDto()
        val mockMeta = createMockMetaDto()

        coEvery { mockMcpHttpClient.sendRequest<Map<String, Any>>(any()) } returns mockResponse
        every { mockAssetDataLoader.loadUiConfig() } returns mockUiConfig
        every { mockAssetDataLoader.loadMetaData(any(), any()) } returns mockMeta

        // When
        val result = mcpClient.getPopularMovies(page)

        // Then
        assertFalse(result.success)
        assertNull(result.data)
        assertEquals("Network error", result.error)
        assertNotNull(result.uiConfig)
        assertNotNull(result.meta)

        coVerify { mockMcpHttpClient.sendRequest<Map<String, Any>>(any()) }
        verify { mockAssetDataLoader.loadUiConfig() }
        verify { mockAssetDataLoader.loadMetaData(any(), any()) }
    }

    @Test
    fun `getMovieDetails should return success response when MCP call succeeds`() = runBlocking {
        // Given
        val movieId = 123
        val mockResponse = createMockMovieDetailsResponse()
        val mockUiConfig = createMockUiConfigDto()
        val mockMeta = createMockMetaDto()

        coEvery { mockMcpHttpClient.sendRequest<Map<String, Any>>(any()) } returns mockResponse
        every { mockAssetDataLoader.loadUiConfig() } returns mockUiConfig
        every { mockAssetDataLoader.loadMetaData(any(), any(), any()) } returns mockMeta

        // When
        val result = mcpClient.getMovieDetails(movieId)

        // Then
        assertTrue(result.success)
        assertNotNull(result.data)
        assertNotNull(result.uiConfig)
        assertNotNull(result.meta)
        assertEquals(movieId, result.data?.id)
        assertEquals("Test Movie Details", result.data?.title)

        coVerify { mockMcpHttpClient.sendRequest<Map<String, Any>>(any()) }
        verify { mockAssetDataLoader.loadUiConfig() }
        verify { mockAssetDataLoader.loadMetaData(any(), any(), any()) }
    }

    @Test
    fun `getMovieDetails should return error response when MCP call fails`() = runBlocking {
        // Given
        val movieId = 123
        val mockResponse = McpResponse<Map<String, Any>>(
            success = false,
            data = null,
            error = "Movie not found"
        )
        val mockUiConfig = createMockUiConfigDto()
        val mockMeta = createMockMetaDto()

        coEvery { mockMcpHttpClient.sendRequest<Map<String, Any>>(any()) } returns mockResponse
        every { mockAssetDataLoader.loadUiConfig() } returns mockUiConfig
        every { mockAssetDataLoader.loadMetaData(any(), any(), any()) } returns mockMeta

        // When
        val result = mcpClient.getMovieDetails(movieId)

        // Then
        assertFalse(result.success)
        assertNull(result.data)
        assertEquals("Movie not found", result.error)
        assertNotNull(result.uiConfig)
        assertNotNull(result.meta)

        coVerify { mockMcpHttpClient.sendRequest<Map<String, Any>>(any()) }
        verify { mockAssetDataLoader.loadUiConfig() }
        verify { mockAssetDataLoader.loadMetaData(any(), any(), any()) }
    }

    @Test
    fun `getPopularMoviesViaMcp should return Success when MCP call succeeds`() = runBlocking {
        // Given
        val page = 1
        val mockResponse = createMockMcpResponse()
        val mockUiConfig = createMockUiConfigDto()
        val mockMeta = createMockMetaDto()

        coEvery { mockMcpHttpClient.sendRequest<Map<String, Any>>(any()) } returns mockResponse
        every { mockAssetDataLoader.loadUiConfig() } returns mockUiConfig
        every { mockAssetDataLoader.loadMetaData(any(), any()) } returns mockMeta

        // When
        val result = mcpClient.getPopularMoviesViaMcp(page)

        // Then
        assertTrue(result is Result.Success)
        val successResult = result as Result.Success
        assertNotNull(successResult.data)
        assertNotNull(successResult.uiConfig)

        coVerify { mockMcpHttpClient.sendRequest<Map<String, Any>>(any()) }
        verify { mockAssetDataLoader.loadUiConfig() }
    }

    @Test
    fun `getPopularMoviesViaMcp should return Error when MCP call fails`() = runBlocking {
        // Given
        val page = 1
        val mockResponse = McpResponse<Map<String, Any>>(
            success = false,
            data = null,
            error = "Network error"
        )

        coEvery { mockMcpHttpClient.sendRequest<Map<String, Any>>(any()) } returns mockResponse

        // When
        val result = mcpClient.getPopularMoviesViaMcp(page)

        // Then
        assertTrue(result is Result.Error)
        val errorResult = result as Result.Error
        assertTrue(errorResult.message.contains("Network error"))

        coVerify { mockMcpHttpClient.sendRequest<Map<String, Any>>(any()) }
    }

    @Test
    fun `getMovieDetailsViaMcp should return Success when MCP call succeeds`() = runBlocking {
        // Given
        val movieId = 123
        val mockResponse = createMockMovieDetailsMcpResponse()
        val mockUiConfig = createMockUiConfigDto()
        val mockMeta = createMockMetaDto()

        coEvery { mockMcpHttpClient.sendRequest<Map<String, Any>>(any()) } returns mockResponse
        every { mockAssetDataLoader.loadUiConfig() } returns mockUiConfig
        every { mockAssetDataLoader.loadMetaData(any(), any(), any()) } returns mockMeta

        // When
        val result = mcpClient.getMovieDetailsViaMcp(movieId)

        // Then
        assertTrue(result is Result.Success)
        val successResult = result as Result.Success
        assertNotNull(successResult.data)
        assertNotNull(successResult.uiConfig)

        coVerify { mockMcpHttpClient.sendRequest<Map<String, Any>>(any()) }
        verify { mockAssetDataLoader.loadUiConfig() }
    }

    @Test
    fun `getMovieDetailsViaMcp should return Error when MCP call fails`() = runBlocking {
        // Given
        val movieId = 123
        val mockResponse = McpResponse<Map<String, Any>>(
            success = false,
            data = null,
            error = "Movie not found"
        )

        coEvery { mockMcpHttpClient.sendRequest<Map<String, Any>>(any()) } returns mockResponse

        // When
        val result = mcpClient.getMovieDetailsViaMcp(movieId)

        // Then
        assertTrue(result is Result.Error)
        val errorResult = result as Result.Error
        assertTrue(errorResult.message.contains("Movie not found"))

        coVerify { mockMcpHttpClient.sendRequest<Map<String, Any>>(any()) }
    }

    @Test
    fun `getPopularMoviesViaMcp should handle exception gracefully`() = runBlocking {
        // Given
        val page = 1
        val exception = RuntimeException("Network failure")

        coEvery { mockMcpHttpClient.sendRequest<Map<String, Any>>(any()) } throws exception

        // When
        val result = mcpClient.getPopularMoviesViaMcp(page)

        // Then
        assertTrue(result is Result.Error)
        val errorResult = result as Result.Error
        assertTrue(errorResult.message.contains("Network failure"))

        coVerify { mockMcpHttpClient.sendRequest<Map<String, Any>>(any()) }
    }

    @Test
    fun `getMovieDetailsViaMcp should handle exception gracefully`() = runBlocking {
        // Given
        val movieId = 123
        val exception = RuntimeException("Service unavailable")

        coEvery { mockMcpHttpClient.sendRequest<Map<String, Any>>(any()) } throws exception

        // When
        val result = mcpClient.getMovieDetailsViaMcp(movieId)

        // Then
        assertTrue(result is Result.Error)
        val errorResult = result as Result.Error
        assertTrue(errorResult.message.contains("Service unavailable"))

        coVerify { mockMcpHttpClient.sendRequest<Map<String, Any>>(any()) }
    }


    @Test
    fun `close should close MCP HTTP client`() {
        // Given
        every { mockMcpHttpClient.close() } returns Unit

        // When
        mcpClient.close()

        // Then
        verify { mockMcpHttpClient.close() }
    }

    private fun createMockMcpResponse(): McpResponse<Map<String, Any>> {
        val moviesData = listOf(
            mapOf(
                "id" to 1,
                "title" to "Test Movie",
                "description" to "Test Description",
                "posterPath" to "/test.jpg",
                "backdropPath" to "/backdrop.jpg",
                "rating" to 8.5,
                "voteCount" to 1000,
                "releaseDate" to "2024-01-01",
                "genreIds" to listOf(1, 2, 3),
                "popularity" to 100.0,
                "adult" to false,
                "originalLanguage" to "en",
                "originalTitle" to "Test Movie",
                "video" to false,
                "colors" to mapOf(
                    "accent" to "#FF0000",
                    "primary" to "#00FF00",
                    "secondary" to "#0000FF",
                    "metadata" to mapOf(
                        "category" to "action",
                        "modelUsed" to true,
                        "rating" to 8.5
                    )
                )
            )
        )

        val responseData = mapOf(
            "page" to 1,
            "results" to moviesData,
            "totalPages" to 10,
            "totalResults" to 100
        )

        return McpResponse(
            success = true,
            data = responseData,
            error = null
        )
    }

    private fun createMockMovieDetailsResponse(): McpResponse<Map<String, Any>> {
        val movieDetailsData = mapOf(
            "id" to 123,
            "title" to "Test Movie Details",
            "description" to "Test Description",
            "posterPath" to "/test.jpg",
            "backdropPath" to "/backdrop.jpg",
            "rating" to 8.5,
            "voteCount" to 1000,
            "releaseDate" to "2024-01-01",
            "runtime" to 120,
            "genres" to listOf(
                mapOf("id" to 1, "name" to "Action"),
                mapOf("id" to 2, "name" to "Drama")
            ),
            "productionCompanies" to listOf(
                mapOf(
                    "id" to 1,
                    "logoPath" to "/logo.jpg",
                    "name" to "Test Studio",
                    "originCountry" to "US"
                )
            ),
            "budget" to 1000000L,
            "revenue" to 2000000L,
            "status" to "Released"
        )

        val responseData = mapOf(
            "movieDetails" to movieDetailsData
        )

        return McpResponse(
            success = true,
            data = responseData,
            error = null
        )
    }

    private fun createMockMovieDetailsMcpResponse(): McpResponse<Map<String, Any>> {
        val movieDetailsData = mapOf(
            "id" to 123,
            "title" to "Test Movie Details",
            "description" to "Test Description",
            "posterPath" to "/test.jpg",
            "backdropPath" to "/backdrop.jpg",
            "rating" to 8.5,
            "voteCount" to 1000,
            "releaseDate" to "2024-01-01",
            "runtime" to 120,
            "genres" to listOf(
                mapOf("id" to 1, "name" to "Action"),
                mapOf("id" to 2, "name" to "Drama")
            ),
            "productionCompanies" to listOf(
                mapOf(
                    "id" to 1,
                    "logoPath" to "/logo.jpg",
                    "name" to "Test Studio",
                    "originCountry" to "US"
                )
            ),
            "budget" to 1000000L,
            "revenue" to 2000000L,
            "status" to "Released"
        )

        val sentimentReviewsData = mapOf(
            "positive" to listOf("Great movie!", "Amazing!"),
            "negative" to listOf("Not good", "Boring")
        )

        val sentimentMetadataData = mapOf(
            "totalReviews" to 100,
            "positiveCount" to 60,
            "negativeCount" to 40,
            "sentimentScore" to 0.6
        )

        val innerData = mapOf(
            "movieDetails" to movieDetailsData,
            "sentimentReviews" to sentimentReviewsData,
            "sentimentMetadata" to sentimentMetadataData
        )

        val responseData = mapOf(
            "data" to innerData
        )

        return McpResponse(
            success = true,
            data = responseData,
            error = null
        )
    }

    private fun createMockUiConfigDto(): UiConfigurationDto {
        return UiConfigurationDto(
            colors = ColorSchemeDto(
                primary = "#FF0000",
                secondary = "#00FF00",
                background = "#0000FF",
                surface = "#FFFF00",
                onPrimary = "#FFFFFF",
                onSecondary = "#000000",
                onBackground = "#FFFFFF",
                onSurface = "#000000",
                moviePosterColors = emptyList()
            ),
            texts = TextConfigurationDto(
                appTitle = "Test App",
                loadingText = "Loading...",
                errorMessage = "Error",
                noMoviesFound = "No movies",
                retryButton = "Retry",
                backButton = "Back",
                playButton = "Play"
            ),
            buttons = ButtonConfigurationDto(
                primaryButtonColor = "#FF0000",
                secondaryButtonColor = "#00FF00",
                buttonTextColor = "#FFFFFF",
                buttonCornerRadius = 8
            ),
            searchInfo = null
        )
    }

    private fun createMockMetaDto(): MetaDto {
        return MetaDto(
            timestamp = "2024-01-01T00:00:00Z",
            method = "testMethod",
            searchQuery = null,
            movieId = null,
            resultsCount = 1,
            aiGenerated = true,
            geminiColors = GeminiColorsDto("#FF0000", "#00FF00", "#0000FF"),
            avgRating = null,
            movieRating = null,
            version = "2.0.0"
        )
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
            adult = false,
            originalLanguage = "en",
            originalTitle = "Test Movie",
            video = false,
            colors = MovieColorsDto(
                accent = "#FF0000",
                primary = "#00FF00",
                secondary = "#0000FF",
                metadata = ColorMetadataDto(
                    category = "action",
                    modelUsed = true,
                    rating = 8.5
                )
            )
        )
    }
}
