package org.studioapp.cinemy.data.repository

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import org.studioapp.cinemy.BuildConfig
import org.studioapp.cinemy.data.mapper.MovieMapper
import org.studioapp.cinemy.data.mcp.AssetDataLoader
import org.studioapp.cinemy.data.mcp.McpClient
import org.studioapp.cinemy.data.model.Meta
import org.studioapp.cinemy.data.model.MovieDetails
import org.studioapp.cinemy.data.model.MovieDetailsData
import org.studioapp.cinemy.data.model.MovieDetailsResponse
import org.studioapp.cinemy.data.model.MovieListResponse
import org.studioapp.cinemy.data.model.Pagination
import org.studioapp.cinemy.data.model.Result
import org.studioapp.cinemy.data.model.StringConstants
import org.studioapp.cinemy.data.model.UiConfiguration

class MovieRepositoryImpl(
    private val mcpClient: McpClient,
    private val assetDataLoader: AssetDataLoader
) : MovieRepository {

    /**
     * Fetches popular movies with pagination support
     * Routes to mock data or MCP client based on BuildConfig.USE_MOCK_DATA
     * @param page Page number for pagination
     * @return Result containing MovieListResponse with movies, pagination, and UI config
     */
    override suspend fun getPopularMovies(page: Int): Result<MovieListResponse> {
        return if (BuildConfig.USE_MOCK_DATA) {
            loadMockPopularMovies(page)
        } else {
            runCatching {
                val response = mcpClient.getPopularMoviesViaMcp(page)

                when (response) {
                    is Result.Success -> {
                        Result.Success(
                            data = response.data,
                            uiConfig = response.uiConfig
                        )
                    }

                    is Result.Error -> {
                        Result.Error(response.message, response.uiConfig)
                    }

                    is Result.Loading -> Result.Loading
                }
            }.getOrElse { exception ->
                Result.Error("${StringConstants.ERROR_NETWORK_WITH_MESSAGE.format(exception.message ?: StringConstants.ERROR_UNKNOWN)}")
            }
        }
    }

    /**
     * Fetches detailed movie information by ID
     * Routes to mock data or MCP client based on BuildConfig.USE_MOCK_DATA
     * @param movieId Unique identifier of the movie
     * @return Result containing MovieDetailsResponse with complete movie details and UI config
     */
    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsResponse> {
        return if (BuildConfig.USE_MOCK_DATA) {
            loadMockMovieDetails(movieId)
        } else {
            runCatching {
                val response = mcpClient.getMovieDetailsViaMcp(movieId)

                when (response) {
                    is Result.Success -> {
                        Result.Success(
                            data = response.data,
                            uiConfig = response.uiConfig
                        )
                    }

                    is Result.Error -> Result.Error(response.message, response.uiConfig)
                    is Result.Loading -> Result.Loading
                }
            }.getOrElse { exception ->
                Result.Error("${StringConstants.ERROR_NETWORK_WITH_MESSAGE.format(exception.message ?: StringConstants.ERROR_UNKNOWN)}")
            }
        }
    }

    /**
     * Loads mock popular movies with pagination for dummy mode
     * @param page Page number for pagination
     * @return Result containing MovieListResponse with mock movies and UI config
     */
    private suspend fun loadMockPopularMovies(page: Int): Result<MovieListResponse> {
        delay(StringConstants.NETWORK_DELAY_BASE_MS)

        val movieDtos = assetDataLoader.loadMockMovies()
        val movies = movieDtos.map { MovieMapper.mapMovieDtoToMovie(it) }

        val pagination = Pagination(
            page = page,
            totalPages = 3,
            totalResults = 30,
            hasNext = page < 3,
            hasPrevious = page > StringConstants.PAGINATION_FIRST_PAGE
        )

        val uiConfig = createDefaultUiConfig()

        val response = MovieListResponse(
            page = pagination.page,
            results = movies,
            totalPages = pagination.totalPages,
            totalResults = pagination.totalResults
        )

        return Result.Success(response, uiConfig)
    }

    /**
     * Loads mock movie details for dummy mode
     * @param movieId Movie ID (ignored in mock mode)
     * @return Result containing MovieDetailsResponse with mock movie details and UI config
     */
    private suspend fun loadMockMovieDetails(movieId: Int): Result<MovieDetailsResponse> {
        delay(StringConstants.NETWORK_DELAY_BASE_MS)

        val movieDetails = loadMockMovieDetailsFromAssets()
        val sentimentReviews: org.studioapp.cinemy.data.model.SentimentReviews? = null

        val movieDetailsData = MovieDetailsData(
            movieDetails = movieDetails,
            sentimentReviews = sentimentReviews
        )

        val uiConfig = createDefaultUiConfig()
        val meta = createDefaultMeta()

        val response = MovieDetailsResponse(
            success = true,
            data = movieDetailsData,
            uiConfig = uiConfig,
            meta = meta
        )

        return Result.Success(response, uiConfig)
    }

    /**
     * Loads mock movie details from assets
     * @return Default MovieDetails object
     */
    private fun loadMockMovieDetailsFromAssets(): MovieDetails {
        return createDefaultMovieDetails()
    }

    /**
     * Creates default movie details for mock data
     * @return MovieDetails object with default values
     */
    private fun createDefaultMovieDetails(): MovieDetails {
        return MovieDetails(
            id = 1,
            title = "Default Movie",
            description = "Default description",
            posterPath = "/default.jpg",
            backdropPath = "/default_backdrop.jpg",
            rating = 5.0,
            voteCount = 100,
            releaseDate = "2024-01-01",
            runtime = 120,
            genres = listOf(
                org.studioapp.cinemy.data.model.Genre(1, "Action"),
                org.studioapp.cinemy.data.model.Genre(2, "Drama")
            ),
            productionCompanies = listOf(
                org.studioapp.cinemy.data.model.ProductionCompany(
                    id = 1,
                    logoPath = "/logo.jpg",
                    name = "Default Studio",
                    originCountry = "US"
                )
            ),
            budget = 1000000,
            revenue = 2000000,
            status = "Released"
        )
    }

    /**
     * Creates default UI configuration for mock data
     * @return UiConfiguration object with default theme values
     */
    private fun createDefaultUiConfig(): UiConfiguration {
        return UiConfiguration(
            colors = org.studioapp.cinemy.data.model.ColorScheme(
                primary = Color(0xFF6200EE),
                secondary = Color(0xFF03DAC6),
                background = Color(0xFFFFFFFF),
                surface = Color(0xFFFFFFFF),
                onPrimary = Color(0xFFFFFFFF),
                onSecondary = Color(0xFF000000),
                onBackground = Color(0xFF000000),
                onSurface = Color(0xFF000000),
                moviePosterColors = listOf(Color(0xFF6200EE), Color(0xFF03DAC6))
            ),
            texts = org.studioapp.cinemy.data.model.TextConfiguration(
                appTitle = "Cinemy",
                loadingText = "Loading...",
                errorMessage = "Error occurred",
                noMoviesFound = "No movies found",
                retryButton = "Retry",
                backButton = "Back",
                playButton = "Play"
            ),
            buttons = org.studioapp.cinemy.data.model.ButtonConfiguration(
                primaryButtonColor = Color(0xFF6200EE),
                secondaryButtonColor = Color(0xFF03DAC6),
                buttonTextColor = Color(0xFFFFFFFF),
                buttonCornerRadius = 8
            )
        )
    }

    /**
     * Creates default meta information for mock data
     * @return Meta object with default metadata values
     */
    private fun createDefaultMeta(): Meta {
        return Meta(
            timestamp = System.currentTimeMillis().toString(),
            method = "mock",
            resultsCount = 1,
            aiGenerated = false,
            geminiColors = org.studioapp.cinemy.data.model.GeminiColors(
                primary = "#6200EE",
                secondary = "#03DAC6",
                accent = "#FF6B6B"
            ),
            version = "1.0.0"
        )
    }
}
