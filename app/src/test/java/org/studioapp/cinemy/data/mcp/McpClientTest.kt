package org.studioapp.cinemy.data.mcp

import android.content.Context
import androidx.compose.ui.graphics.Color
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
import org.studioapp.cinemy.data.model.ColorScheme
import org.studioapp.cinemy.data.model.GeminiColors
import org.studioapp.cinemy.data.model.Genre
import org.studioapp.cinemy.data.model.Meta
import org.studioapp.cinemy.data.model.Movie
import org.studioapp.cinemy.data.model.MovieDetails
import org.studioapp.cinemy.data.model.MovieListData
import org.studioapp.cinemy.data.model.MovieListResponse
import org.studioapp.cinemy.data.model.Pagination
import org.studioapp.cinemy.data.model.ProductionCompany
import org.studioapp.cinemy.data.model.Result
import org.studioapp.cinemy.data.model.SearchInfo
import org.studioapp.cinemy.data.model.TextConfiguration
import org.studioapp.cinemy.data.model.UiConfiguration
import org.studioapp.cinemy.data.remote.dto.ButtonConfigurationDto
import org.studioapp.cinemy.data.remote.dto.ColorSchemeDto
import org.studioapp.cinemy.data.remote.dto.GeminiColorsDto
import org.studioapp.cinemy.data.remote.dto.GenreDto
import org.studioapp.cinemy.data.remote.dto.McpMovieListResponseDto
import org.studioapp.cinemy.data.remote.dto.MetaDto
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

    fun mapMcpMovieListResponseDtoToMovieListResponse(dto: McpMovieListResponseDto): MovieListResponse {
        return MovieListResponse(
            success = true,
            data = MovieListData(
                movies = dto.movies.map { mapMovieDtoToMovie(it) },
                pagination = mapPaginationDtoToPagination(dto.pagination)
            ),
            uiConfig = UiConfiguration(
                colors = ColorScheme(
                    primary = Color.Red,
                    secondary = Color.Green,
                    background = Color.Blue,
                    surface = Color.Yellow,
                    onPrimary = Color.White,
                    onSecondary = Color.Black,
                    onBackground = Color.White,
                    onSurface = Color.Black,
                    moviePosterColors = emptyList()
                ),
                texts = TextConfiguration(
                    appTitle = "Test App",
                    loadingText = "Loading...",
                    errorMessage = "Error",
                    noMoviesFound = "No movies",
                    retryButton = "Retry",
                    backButton = "Back",
                    playButton = "Play"
                ),
                buttons = ButtonConfiguration(
                    primaryButtonColor = Color.Red,
                    secondaryButtonColor = Color.Green,
                    buttonTextColor = Color.White,
                    buttonCornerRadius = 8
                ),
                searchInfo = null
            ),
            error = null,
            meta = Meta(
                timestamp = System.currentTimeMillis().toString(),
                method = "testMethod",
                searchQuery = null,
                movieId = null,
                resultsCount = dto.movies.size,
                aiGenerated = true,
                geminiColors = GeminiColors(
                    primary = "#FF0000",
                    secondary = "#00FF00",
                    accent = "#0000FF"
                ),
                avgRating = null,
                movieRating = null,
                version = "2.0.0"
            )
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
            adult = dto.adult
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
            println("ColorUtils.parseColor called with: $colorString")
            TestColorUtils.parseColor(colorString)
        }

        // Mock MovieMapper methods to use TestMovieMapper
        mockkObject(MovieMapper)
        every { MovieMapper.mapMcpMovieListResponseDtoToMovieListResponse(any()) } answers { call ->
            val dto = call.invocation.args[0] as McpMovieListResponseDto
            TestMovieMapper.mapMcpMovieListResponseDtoToMovieListResponse(dto)
        }
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
        assertEquals(1, result.data?.movies?.size)
        assertEquals("Test Movie", result.data?.movies?.get(0)?.title)

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
        val mockResponse = createMockMovieDetailsMcpResponse()
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
        println("Result type: ${result::class.simpleName}")
        println("Result: $result")
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
    fun `getTopRatedMovies should return success response`() = runBlocking {
        // Given
        val page = 1
        val mockMovies = listOf(createMockMovieDto())

        every { mockAssetDataLoader.loadMockMovies() } returns mockMovies
        every { mockAssetDataLoader.loadUiConfig() } returns createMockUiConfigDto()
        every { mockAssetDataLoader.loadMetaData(any(), any()) } returns createMockMetaDto()

        // When
        val result = mcpClient.getTopRatedMovies(page)

        // Then
        assertTrue(result.success)
        assertNotNull(result.data)
        assertNotNull(result.uiConfig)
        assertNotNull(result.meta)
        assertEquals(1, result.data?.movies?.size)
        assertTrue(result.data?.movies?.get(0)?.title?.startsWith("Top Rated") == true)

        verify { mockAssetDataLoader.loadMockMovies() }
        verify { mockAssetDataLoader.loadUiConfig() }
        verify { mockAssetDataLoader.loadMetaData(any(), any()) }
    }

    @Test
    fun `getNowPlayingMovies should return success response`() = runBlocking {
        // Given
        val page = 1
        val mockMovies = listOf(createMockMovieDto())

        every { mockAssetDataLoader.loadMockMovies() } returns mockMovies
        every { mockAssetDataLoader.loadUiConfig() } returns createMockUiConfigDto()
        every { mockAssetDataLoader.loadMetaData(any(), any()) } returns createMockMetaDto()

        // When
        val result = mcpClient.getNowPlayingMovies(page)

        // Then
        assertTrue(result.success)
        assertNotNull(result.data)
        assertNotNull(result.uiConfig)
        assertNotNull(result.meta)
        assertEquals(1, result.data?.movies?.size)
        assertTrue(result.data?.movies?.get(0)?.title?.startsWith("Now Playing") == true)

        verify { mockAssetDataLoader.loadMockMovies() }
        verify { mockAssetDataLoader.loadUiConfig() }
        verify { mockAssetDataLoader.loadMetaData(any(), any()) }
    }

    @Test
    fun `getMovieRecommendations should return success response`() = runBlocking {
        // Given
        val movieId = 123
        val page = 1
        val mockMovies = listOf(createMockMovieDto())

        every { mockAssetDataLoader.loadMockMovies() } returns mockMovies
        every { mockAssetDataLoader.loadUiConfig() } returns createMockUiConfigDto()
        every { mockAssetDataLoader.loadMetaData(any(), any()) } returns createMockMetaDto()

        // When
        val result = mcpClient.getMovieRecommendations(movieId, page)

        // Then
        assertTrue(result.success)
        assertNotNull(result.data)
        assertNotNull(result.uiConfig)
        assertNotNull(result.meta)
        assertEquals(1, result.data?.movies?.size)
        assertTrue(result.data?.movies?.get(0)?.title?.startsWith("Recommended") == true)

        verify { mockAssetDataLoader.loadMockMovies() }
        verify { mockAssetDataLoader.loadUiConfig() }
        verify { mockAssetDataLoader.loadMetaData(any(), any()) }
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
                "adult" to false
            )
        )

        val paginationData = mapOf(
            "page" to 1,
            "totalPages" to 10,
            "totalResults" to 100,
            "hasNext" to true,
            "hasPrevious" to false
        )

        val responseData = mapOf(
            "movies" to moviesData,
            "pagination" to paginationData
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

        val responseData = mapOf(
            "movieDetails" to movieDetailsData
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
            adult = false
        )
    }
}
