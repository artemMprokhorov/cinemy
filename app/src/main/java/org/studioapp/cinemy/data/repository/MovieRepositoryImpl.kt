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
     * @param page Page number for pagination
     * @return Result containing MovieListResponse with mock movies and UI config
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
        return DefaultMovieDetails().toMovieDetails()
    }

    /**
     * Creates default UI configuration for mock data
     * @return UiConfiguration object with default theme values
     */
    private fun createDefaultUiConfig(): UiConfiguration {
        return DefaultUiConfiguration().toUiConfiguration()
    }

    /**
     * Creates default meta information for mock data
     * @return Meta object with default metadata values
     */
    private fun createDefaultMeta(): Meta {
        return DefaultMeta().toMeta()
    }
}
