package org.studioapp.cinemy.data.mcp

import android.content.Context
import org.studioapp.cinemy.data.mapper.MovieMapper.mapBackendMovieDetailsResponseToDomainModels
import org.studioapp.cinemy.data.mapper.MovieMapper.mapBackendMovieListResponseToMovieListResponseDto
import org.studioapp.cinemy.data.mapper.MovieMapper.mapBackendUiConfigToDomainUiConfig
import org.studioapp.cinemy.data.mapper.MovieMapper.mapJsonToMovieDetailsDto
import org.studioapp.cinemy.data.mapper.MovieMapper.mapMovieListResponseDtoToMovieListResponse
import org.studioapp.cinemy.data.mapper.MovieMapper.mapUiConfigurationDtoToUiConfiguration
import org.studioapp.cinemy.data.mcp.models.McpRequest
import org.studioapp.cinemy.data.model.GeminiColors
import org.studioapp.cinemy.data.model.Meta
import org.studioapp.cinemy.data.model.MovieDetailsData
import org.studioapp.cinemy.data.model.MovieDetailsResponse
import org.studioapp.cinemy.data.model.MovieListResponse
import org.studioapp.cinemy.data.model.Result
import org.studioapp.cinemy.data.model.StringConstants.COLOR_ACCENT
import org.studioapp.cinemy.data.model.StringConstants.COLOR_PRIMARY
import org.studioapp.cinemy.data.model.StringConstants.COLOR_SECONDARY
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_INT_VALUE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_TOTAL_PAGES
import org.studioapp.cinemy.data.model.StringConstants.ERROR_FETCHING_MOVIE_DETAILS
import org.studioapp.cinemy.data.model.StringConstants.ERROR_FETCHING_POPULAR_MOVIES
import org.studioapp.cinemy.data.model.StringConstants.ERROR_UNKNOWN
import org.studioapp.cinemy.data.model.StringConstants.FIELD_MOVIE_DETAILS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_PAGE
import org.studioapp.cinemy.data.model.StringConstants.INVALID_MOVIE_DETAILS_DATA
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_DATA
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_UI_CONFIG
import org.studioapp.cinemy.data.model.StringConstants.MCP_METHOD_GET_MOVIE_DETAILS
import org.studioapp.cinemy.data.model.StringConstants.MCP_METHOD_GET_POPULAR_MOVIES
import org.studioapp.cinemy.data.model.StringConstants.META_RESULTS_COUNT_ZERO
import org.studioapp.cinemy.data.model.StringConstants.PARAM_MOVIE_ID
import org.studioapp.cinemy.data.model.StringConstants.VERSION_2_0_0
import org.studioapp.cinemy.data.remote.api.MovieApiService
import org.studioapp.cinemy.data.remote.dto.McpResponseDto
import org.studioapp.cinemy.data.remote.dto.MovieDetailsDto
import org.studioapp.cinemy.data.remote.dto.MovieListResponseDto

class McpClient(private val context: Context) : MovieApiService {
    private val assetDataLoader = AssetDataLoader(context)
    private val mcpHttpClient = McpHttpClient(context)

    /**
     * Fetches popular movies from MCP backend
     * @param page Page number for pagination
     * @return McpResponseDto containing MovieListResponseDto with movies and UI config
     */
    override suspend fun getPopularMovies(page: Int): McpResponseDto<MovieListResponseDto> {
        val request = McpRequest(
            method = MCP_METHOD_GET_POPULAR_MOVIES,
            params = mapOf(FIELD_PAGE to page.toString())
        )

        val response = mcpHttpClient.sendRequest<Any>(request)

        // Debug logging for MCP response

        return if (response.success && response.data != null) {
            // Convert the response data to DTO format - new contract structure
            // Backend returns an array with a single object, so we need to get the first element
            val responseData = response.data
            val data = if (responseData is List<*>) {
                responseData.firstOrNull() as? Map<String, Any>
            } else {
                responseData as? Map<String, Any>
            }

            val movieListResponse = if (data != null) {
                mapBackendMovieListResponseToMovieListResponseDto(data, page)
            } else {
                MovieListResponseDto(
                    page = page,
                    results = emptyList(),
                    totalPages = DEFAULT_TOTAL_PAGES,
                    totalResults = DEFAULT_INT_VALUE
                )
            }

            val uiConfig = assetDataLoader.loadUiConfig()

            // Debug logging for uiConfig

            McpResponseDto(
                success = true,
                data = movieListResponse,
                uiConfig = uiConfig,
                error = null,
                meta = assetDataLoader.loadMetaData(
                    MCP_METHOD_GET_POPULAR_MOVIES,
                    movieListResponse.results.size
                )
            )
        } else {
            McpResponseDto(
                success = false,
                data = null,
                uiConfig = assetDataLoader.loadUiConfig(),
                error = response.error ?: ERROR_FETCHING_POPULAR_MOVIES.format(
                    ERROR_UNKNOWN
                ),
                meta = assetDataLoader.loadMetaData(
                    MCP_METHOD_GET_POPULAR_MOVIES,
                    META_RESULTS_COUNT_ZERO
                )
            )
        }
    }


    /**
     * Fetches movie details from MCP backend
     * @param movieId Unique identifier of the movie
     * @return McpResponseDto containing MovieDetailsDto with complete movie details and UI config
     */
    override suspend fun getMovieDetails(movieId: Int): McpResponseDto<MovieDetailsDto> {
        val request = McpRequest(
            method = MCP_METHOD_GET_MOVIE_DETAILS,
            params = mapOf(PARAM_MOVIE_ID to movieId.toString())
        )

        val response = mcpHttpClient.sendRequest<Map<String, Any>>(request)


        return if (response.success && response.data != null) {
            // Convert the response data to DTO format
            val data = response.data
            val movieDetailsData = data[FIELD_MOVIE_DETAILS] as? Map<String, Any>

            if (movieDetailsData != null) {
                val movieDetails = mapJsonToMovieDetailsDto(movieDetailsData, movieId)

                val uiConfig = assetDataLoader.loadUiConfig()

                // Debug logging for uiConfig

                McpResponseDto(
                    success = true,
                    data = movieDetails,
                    uiConfig = uiConfig,
                    error = null,
                    meta = assetDataLoader.loadMetaData(
                        MCP_METHOD_GET_MOVIE_DETAILS,
                        1,
                        movieId
                    )
                )
            } else {
                McpResponseDto(
                    success = false,
                    data = null,
                    uiConfig = assetDataLoader.loadUiConfig(),
                    error = INVALID_MOVIE_DETAILS_DATA,
                    meta = assetDataLoader.loadMetaData(
                        MCP_METHOD_GET_MOVIE_DETAILS,
                        META_RESULTS_COUNT_ZERO,
                        movieId
                    )
                )
            }
        } else {
            McpResponseDto(
                success = false,
                data = null,
                uiConfig = assetDataLoader.loadUiConfig(),
                error = response.error ?: ERROR_FETCHING_MOVIE_DETAILS.format(
                    ERROR_UNKNOWN
                ),
                meta = assetDataLoader.loadMetaData(
                    MCP_METHOD_GET_MOVIE_DETAILS,
                    META_RESULTS_COUNT_ZERO,
                    movieId
                )
            )
        }
    }


    /**
     * Fetches popular movies using MCP HTTP client and returns domain models
     * @param page Page number for pagination
     * @return Result containing MovieListResponse with domain models and UI config
     */
    suspend fun getPopularMoviesViaMcp(page: Int): Result<MovieListResponse> {
        return runCatching {
            val request = McpRequest(
                method = MCP_METHOD_GET_POPULAR_MOVIES,
                params = mapOf(FIELD_PAGE to page.toString())
            )

            val response = mcpHttpClient.sendRequest<Any>(request)

            if (response.success && response.data != null) {
                // Backend returns an array with a single object, so we need to get the first element
                val responseData = response.data
                val data = if (responseData is List<*>) {
                    responseData.firstOrNull() as? Map<String, Any>
                } else {
                    responseData as? Map<String, Any>
                }

                val movieListResponse = if (data != null) {
                    mapBackendMovieListResponseToMovieListResponseDto(data, page)
                } else {
                    MovieListResponseDto(
                        page = page,
                        results = emptyList(),
                        totalPages = DEFAULT_TOTAL_PAGES,
                        totalResults = DEFAULT_INT_VALUE
                    )
                }

                val domainMovieListResponse =
                    mapMovieListResponseDtoToMovieListResponse(movieListResponse)

                val uiConfig = assetDataLoader.loadUiConfig()

                // Debug logging for uiConfig

                Result.Success(
                    data = domainMovieListResponse,
                    uiConfig = mapUiConfigurationDtoToUiConfiguration(uiConfig)
                )
            } else {
                Result.Error(
                    response.error ?: ERROR_FETCHING_POPULAR_MOVIES.format(
                        ERROR_UNKNOWN
                    )
                )
            }
        }.getOrElse { exception ->
            Result.Error("${ERROR_FETCHING_POPULAR_MOVIES.format(exception.message ?: ERROR_UNKNOWN)}")
        }
    }

    /**
     * Fetches movie details using MCP HTTP client and returns domain models
     * @param movieId Unique identifier of the movie
     * @return Result containing MovieDetailsResponse with domain models and UI config
     */
    suspend fun getMovieDetailsViaMcp(movieId: Int): Result<MovieDetailsResponse> {
        return runCatching {
            val request = McpRequest(
                method = MCP_METHOD_GET_MOVIE_DETAILS,
                params = mapOf(PARAM_MOVIE_ID to movieId.toString())
            )

            val response = mcpHttpClient.sendRequest<Any>(request)

            if (response.success && response.data != null) {
                // Backend returns an array with a single object, so we need to get the first element
                val responseData = response.data
                val data = if (responseData is List<*>) {
                    responseData.firstOrNull() as? Map<String, Any>
                } else {
                    responseData as? Map<String, Any>
                }

                // The backend response structure is: [{"success": true, "data": {"movieDetails": {...}, ...}, ...}]
                // So we need to access data["data"]["movieDetails"]
                val innerData = data?.get(JSON_FIELD_DATA) as? Map<String, Any>
                val movieDetailsData =
                    innerData?.get(FIELD_MOVIE_DETAILS) as? Map<String, Any>

                if (movieDetailsData != null) {
                    // Extract uiConfig from backend response
                    val backendUiConfig = data?.get(JSON_FIELD_UI_CONFIG) as? Map<String, Any>

                    val (domainMovieDetails, domainSentimentReviews, domainSentimentMetadata) =
                        mapBackendMovieDetailsResponseToDomainModels(
                            innerData,
                            movieId,
                            backendUiConfig,
                            assetDataLoader
                        )

                    val domainUiConfig =
                        mapBackendUiConfigToDomainUiConfig(backendUiConfig, assetDataLoader)

                    // Debug logging for uiConfig

                    Result.Success(
                        data = MovieDetailsResponse(
                            success = true,
                            data = MovieDetailsData(
                                movieDetails = domainMovieDetails,
                                sentimentReviews = domainSentimentReviews,
                                sentimentMetadata = domainSentimentMetadata
                            ),
                            uiConfig = domainUiConfig,
                            error = null,
                            meta = Meta(
                                timestamp = System.currentTimeMillis().toString(),
                                method = MCP_METHOD_GET_MOVIE_DETAILS,
                                searchQuery = null,
                                movieId = movieId,
                                resultsCount = null,
                                aiGenerated = true,
                                geminiColors = GeminiColors(
                                    primary = COLOR_PRIMARY,
                                    secondary = COLOR_SECONDARY,
                                    accent = COLOR_ACCENT
                                ),
                                avgRating = null,
                                movieRating = domainMovieDetails.rating,
                                version = VERSION_2_0_0
                            )
                        ),
                        uiConfig = domainUiConfig
                    )
                } else {
                    Result.Error(INVALID_MOVIE_DETAILS_DATA)
                }
            } else {
                Result.Error(
                    response.error ?: ERROR_FETCHING_MOVIE_DETAILS.format(
                        ERROR_UNKNOWN
                    )
                )
            }
        }.getOrElse { exception ->
            Result.Error("${ERROR_FETCHING_MOVIE_DETAILS.format(exception.message ?: ERROR_UNKNOWN)}")
        }
    }


    /**
     * Closes MCP HTTP client and cleans up resources
     */
    fun close() {
        mcpHttpClient.close()
    }


}
