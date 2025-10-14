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
import org.studioapp.cinemy.data.model.StringConstants.ERROR_NETWORK_WITH_MESSAGE
import org.studioapp.cinemy.data.model.StringConstants.ERROR_UNKNOWN
import org.studioapp.cinemy.data.model.StringConstants.NETWORK_DELAY_BASE_MS
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_TITLE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_DESCRIPTION
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_POSTER_PATH
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_BACKDROP_PATH
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_RELEASE_DATE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_GENRE_ACTION
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_GENRE_DRAMA
import org.studioapp.cinemy.data.model.StringConstants.PAGINATION_FIRST_PAGE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_APP_TITLE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_LOADING_TEXT
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_ERROR_MESSAGE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_NO_MOVIES_FOUND
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_RETRY_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BACK_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_PLAY_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_PRIMARY_COLOR_HEX
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_SECONDARY_COLOR_HEX
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_ACCENT_COLOR_HEX
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_WHITE_COLOR_HEX
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BLACK_COLOR_HEX
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_METHOD
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_RESULTS_COUNT
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_VERSION
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BUTTON_CORNER_RADIUS
import org.studioapp.cinemy.data.model.UiConfiguration
import org.studioapp.cinemy.data.model.Genre
import org.studioapp.cinemy.data.model.ProductionCompany
import org.studioapp.cinemy.data.model.SentimentReviews
import org.studioapp.cinemy.data.model.ColorScheme
import org.studioapp.cinemy.data.model.TextConfiguration
import org.studioapp.cinemy.data.model.ButtonConfiguration
import org.studioapp.cinemy.data.model.GeminiColors

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
                Result.Error("${ERROR_NETWORK_WITH_MESSAGE.format(exception.message ?: ERROR_UNKNOWN)}")
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
                Result.Error("${ERROR_NETWORK_WITH_MESSAGE.format(exception.message ?: ERROR_UNKNOWN)}")
            }
        }
    }

    /**
     * Loads mock popular movies with pagination for dummy mode
     * @param page Page number for pagination
     * @return Result containing MovieListResponse with mock movies and UI config
     */
    private suspend fun loadMockPopularMovies(page: Int): Result<MovieListResponse> {
        delay(NETWORK_DELAY_BASE_MS)

        val movieDtos = assetDataLoader.loadMockMovies()
        val movies = movieDtos.map { MovieMapper.mapMovieDtoToMovie(it) }

        val pagination = Pagination(
            page = page,
            totalPages = 3,
            totalResults = 30,
            hasNext = page < 3,
            hasPrevious = page > PAGINATION_FIRST_PAGE
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
        delay(NETWORK_DELAY_BASE_MS)

        val movieDetails = loadMockMovieDetailsFromAssets()
        val sentimentReviews: SentimentReviews? = null

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
            title = DEFAULT_MOVIE_TITLE,
            description = DEFAULT_MOVIE_DESCRIPTION,
            posterPath = DEFAULT_MOVIE_POSTER_PATH,
            backdropPath = DEFAULT_MOVIE_BACKDROP_PATH,
            rating = 5.0,
            voteCount = 100,
            releaseDate = DEFAULT_MOVIE_RELEASE_DATE,
            runtime = 120,
            genres = listOf(
                Genre(1, DEFAULT_GENRE_ACTION),
                Genre(2, DEFAULT_GENRE_DRAMA)
            ),
            productionCompanies = listOf(
                ProductionCompany(
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
            colors = ColorScheme(
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
            texts = TextConfiguration(
                appTitle = DEFAULT_APP_TITLE,
                loadingText = DEFAULT_LOADING_TEXT,
                errorMessage = DEFAULT_ERROR_MESSAGE,
                noMoviesFound = DEFAULT_NO_MOVIES_FOUND,
                retryButton = DEFAULT_RETRY_BUTTON,
                backButton = DEFAULT_BACK_BUTTON,
                playButton = DEFAULT_PLAY_BUTTON
            ),
            buttons = ButtonConfiguration(
                primaryButtonColor = Color(0xFF6200EE),
                secondaryButtonColor = Color(0xFF03DAC6),
                buttonTextColor = Color(0xFFFFFFFF),
                buttonCornerRadius = DEFAULT_BUTTON_CORNER_RADIUS
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
            method = DEFAULT_METHOD,
            resultsCount = DEFAULT_RESULTS_COUNT,
            aiGenerated = false,
            geminiColors = GeminiColors(
                primary = DEFAULT_PRIMARY_COLOR_HEX,
                secondary = DEFAULT_SECONDARY_COLOR_HEX,
                accent = DEFAULT_ACCENT_COLOR_HEX
            ),
            version = DEFAULT_VERSION
        )
    }
}
