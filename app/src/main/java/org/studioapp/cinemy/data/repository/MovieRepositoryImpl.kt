package org.studioapp.cinemy.data.repository

import kotlinx.coroutines.delay
import org.studioapp.cinemy.BuildConfig
import org.studioapp.cinemy.data.mapper.MovieMapper
import org.studioapp.cinemy.data.mcp.AssetDataLoader
import org.studioapp.cinemy.data.mcp.McpClient
import org.studioapp.cinemy.data.model.DefaultMeta
import org.studioapp.cinemy.data.model.DefaultMovieDetails
import org.studioapp.cinemy.data.model.DefaultUiConfiguration
import org.studioapp.cinemy.data.model.Meta
import org.studioapp.cinemy.data.model.MovieDetails
import org.studioapp.cinemy.data.model.MovieDetailsData
import org.studioapp.cinemy.data.model.MovieDetailsResponse
import org.studioapp.cinemy.data.model.MovieListResponse
import org.studioapp.cinemy.data.model.Pagination
import org.studioapp.cinemy.data.model.Result
import org.studioapp.cinemy.data.model.SentimentReviews
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOCK_TOTAL_PAGES
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOCK_TOTAL_RESULTS
import org.studioapp.cinemy.data.model.StringConstants.ERROR_NETWORK_WITH_MESSAGE
import org.studioapp.cinemy.data.model.StringConstants.ERROR_UNKNOWN
import org.studioapp.cinemy.data.model.StringConstants.NETWORK_DELAY_BASE_MS
import org.studioapp.cinemy.data.model.StringConstants.PAGINATION_FIRST_PAGE
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
     * 
     * This method simulates network behavior by adding a delay and loads mock movie data
     * from assets. It creates pagination metadata and UI configuration for the response.
     * Used when BuildConfig.USE_MOCK_DATA is true.
     * 
     * @param page Page number for pagination (1-based indexing)
     * @return Result.Success containing MovieListResponse with mock movies, pagination metadata, and UI configuration
     * @throws No exceptions thrown - uses runCatching for error handling
     */
    private suspend fun loadMockPopularMovies(page: Int): Result<MovieListResponse> {
        delay(NETWORK_DELAY_BASE_MS)

        val movieDtos = assetDataLoader.loadMockMovies()
        val movies = movieDtos.map { MovieMapper.mapMovieDtoToMovie(it) }

        val pagination = Pagination(
            page = page,
            totalPages = DEFAULT_MOCK_TOTAL_PAGES,
            totalResults = DEFAULT_MOCK_TOTAL_RESULTS,
            hasNext = page < DEFAULT_MOCK_TOTAL_PAGES,
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
     * 
     * This method simulates network behavior by adding a delay and loads mock movie details
     * from assets. It creates a complete MovieDetailsResponse with metadata and UI configuration.
     * Used when BuildConfig.USE_MOCK_DATA is true.
     * 
     * @param movieId Movie ID (ignored in mock mode, always returns the same mock data)
     * @return Result.Success containing MovieDetailsResponse with mock movie details, UI configuration, and metadata
     * @throws No exceptions thrown - uses runCatching for error handling
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
     * 
     * This method creates a default MovieDetails object using the DefaultMovieDetails class.
     * It provides a complete set of mock movie information for testing and development.
     * 
     * @return Default MovieDetails object with all required fields populated
     * @throws No exceptions thrown - uses default data generation
     */
    private fun loadMockMovieDetailsFromAssets(): MovieDetails {
        return DefaultMovieDetails().toMovieDetails()
    }

    /**
     * Creates default UI configuration for mock data
     * 
     * This method generates a default UI configuration using the DefaultUiConfiguration class.
     * It provides color schemes, text content, and button styling for the mock data mode.
     * 
     * @return UiConfiguration object with default theme values including colors, texts, and button configuration
     * @throws No exceptions thrown - uses default configuration generation
     */
    private fun createDefaultUiConfig(): UiConfiguration {
        return DefaultUiConfiguration().toUiConfiguration()
    }

    /**
     * Creates default meta information for mock data
     * 
     * This method generates default metadata using the DefaultMeta class.
     * It provides timestamp, method information, and other metadata for mock responses.
     * 
     * @return Meta object with default metadata values including timestamp, method, and AI generation flags
     * @throws No exceptions thrown - uses default metadata generation
     */
    private fun createDefaultMeta(): Meta {
        return DefaultMeta().toMeta()
    }
}
