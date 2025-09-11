package org.studioapp.cinemy.data.mcp

import android.content.Context
import androidx.compose.ui.graphics.Color
import org.studioapp.cinemy.BuildConfig
import org.studioapp.cinemy.data.mapper.MovieMapper
import org.studioapp.cinemy.data.mcp.models.McpRequest
import org.studioapp.cinemy.data.model.ButtonConfiguration
import org.studioapp.cinemy.data.model.ColorScheme
import org.studioapp.cinemy.data.model.GeminiColors
import org.studioapp.cinemy.data.model.Meta
import org.studioapp.cinemy.data.model.MovieDetailsData
import org.studioapp.cinemy.data.model.MovieDetailsResponse
import org.studioapp.cinemy.data.model.MovieListResponse
import org.studioapp.cinemy.data.model.Result
import org.studioapp.cinemy.data.model.StringConstants
import org.studioapp.cinemy.data.model.TextConfiguration
import org.studioapp.cinemy.data.model.UiConfiguration
import org.studioapp.cinemy.data.remote.api.MovieApiService
import org.studioapp.cinemy.data.remote.dto.GeminiColorsDto
import org.studioapp.cinemy.data.remote.dto.GenreDto
import org.studioapp.cinemy.data.remote.dto.McpMovieListResponseDto
import org.studioapp.cinemy.data.remote.dto.McpResponseDto
import org.studioapp.cinemy.data.remote.dto.MetaDto
import org.studioapp.cinemy.data.remote.dto.MovieDetailsDto
import org.studioapp.cinemy.data.remote.dto.MovieDto
import org.studioapp.cinemy.data.remote.dto.PaginationDto
import org.studioapp.cinemy.data.remote.dto.ProductionCompanyDto
import com.google.gson.Gson
import kotlinx.coroutines.delay
import java.io.IOException

class McpClient(private val context: Context) : MovieApiService {
    private val assetDataLoader = AssetDataLoader(context)

    private val gson = Gson()
    private val mcpHttpClient = McpHttpClient(context)

    override suspend fun getPopularMovies(page: Int): McpResponseDto<McpMovieListResponseDto> {
        val request = McpRequest(
            method = StringConstants.MCP_METHOD_GET_POPULAR_MOVIES,
            params = mapOf(StringConstants.FIELD_PAGE to page.toString())
        )

        val response = mcpHttpClient.sendRequest<Map<String, Any>>(request)

        // Debug logging for MCP response
        if (BuildConfig.DEBUG) {
        }

        return if (response.success && response.data != null) {
            // Convert the response data to DTO format
            val data = response.data
            val movies =
                (data[StringConstants.FIELD_MOVIES] as? List<Map<String, Any>>)?.map { movieData ->
                    MovieDto(
                        id = (movieData[StringConstants.FIELD_ID] as? Number)?.toInt()
                            ?: StringConstants.DEFAULT_INT_VALUE,
                        title = movieData[StringConstants.FIELD_TITLE] as? String
                            ?: StringConstants.EMPTY_STRING,
                        description = movieData[StringConstants.FIELD_DESCRIPTION] as? String
                            ?: StringConstants.EMPTY_STRING,
                        posterPath = movieData[StringConstants.FIELD_POSTER_PATH] as? String,
                        backdropPath = movieData[StringConstants.FIELD_BACKDROP_PATH] as? String,
                        rating = (movieData[StringConstants.FIELD_RATING] as? Number)?.toDouble()
                            ?: StringConstants.DEFAULT_DOUBLE_VALUE,
                        voteCount = (movieData[StringConstants.FIELD_VOTE_COUNT] as? Number)?.toInt()
                            ?: StringConstants.DEFAULT_INT_VALUE,
                        releaseDate = movieData[StringConstants.FIELD_RELEASE_DATE] as? String
                            ?: StringConstants.EMPTY_STRING,
                        genreIds = (movieData[StringConstants.FIELD_GENRE_IDS] as? List<Number>)?.map { it.toInt() }
                            ?: emptyList(),
                        popularity = (movieData[StringConstants.FIELD_POPULARITY] as? Number)?.toDouble()
                            ?: StringConstants.DEFAULT_DOUBLE_VALUE,
                        adult = movieData[StringConstants.FIELD_ADULT] as? Boolean
                            ?: StringConstants.DEFAULT_BOOLEAN_VALUE
                    )
                } ?: emptyList()

            val paginationData = data[StringConstants.FIELD_PAGINATION] as? Map<String, Any>
            val pagination = PaginationDto(
                page = (paginationData?.get(StringConstants.FIELD_PAGE) as? Number)?.toInt()
                    ?: page,
                totalPages = (paginationData?.get(StringConstants.FIELD_TOTAL_PAGES) as? Number)?.toInt()
                    ?: StringConstants.DEFAULT_TOTAL_PAGES,
                totalResults = (paginationData?.get(StringConstants.FIELD_TOTAL_RESULTS) as? Number)?.toInt()
                    ?: StringConstants.DEFAULT_INT_VALUE,
                hasNext = paginationData?.get(StringConstants.FIELD_HAS_NEXT) as? Boolean
                    ?: StringConstants.DEFAULT_BOOLEAN_VALUE,
                hasPrevious = paginationData?.get(StringConstants.FIELD_HAS_PREVIOUS) as? Boolean
                    ?: StringConstants.DEFAULT_BOOLEAN_VALUE
            )

            val uiConfig = assetDataLoader.loadUiConfig()
            
            // Debug logging for uiConfig
            if (BuildConfig.DEBUG) {
            }

            McpResponseDto(
                success = true,
                data = McpMovieListResponseDto(movies, pagination),
                uiConfig = uiConfig,
                error = null,
                meta = assetDataLoader.loadMetaData(
                    StringConstants.MCP_METHOD_GET_POPULAR_MOVIES,
                    movies.size
                )
            )
        } else {
            McpResponseDto(
                success = false,
                data = null,
                uiConfig = assetDataLoader.loadUiConfig(),
                error = response.error ?: StringConstants.ERROR_FETCHING_POPULAR_MOVIES.format(
                    StringConstants.ERROR_UNKNOWN
                ),
                meta = assetDataLoader.loadMetaData(
                    StringConstants.MCP_METHOD_GET_POPULAR_MOVIES,
                    StringConstants.META_RESULTS_COUNT_ZERO
                )
            )
        }
    }

    override suspend fun getTopRatedMovies(page: Int): McpResponseDto<McpMovieListResponseDto> {
        return simulateNetworkCall {
            val mockMovies = assetDataLoader.loadMockMovies()
                .map { it.copy(title = "${StringConstants.TOP_RATED_PREFIX}${it.title}") }
            val pagination = PaginationDto(
                page = page,
                totalPages = StringConstants.PAGINATION_TOP_RATED_TOTAL_PAGES,
                totalResults = StringConstants.PAGINATION_TOP_RATED_TOTAL_RESULTS,
                hasNext = page < StringConstants.PAGINATION_TOP_RATED_TOTAL_PAGES,
                hasPrevious = page > StringConstants.PAGINATION_FIRST_PAGE
            )

            McpMovieListResponseDto(mockMovies, pagination)
        }
    }

    override suspend fun getNowPlayingMovies(page: Int): McpResponseDto<McpMovieListResponseDto> {
        return simulateNetworkCall {
            val mockMovies = assetDataLoader.loadMockMovies()
                .map { it.copy(title = "${StringConstants.NOW_PLAYING_PREFIX}${it.title}") }
            val pagination = PaginationDto(
                page = page,
                totalPages = StringConstants.PAGINATION_NOW_PLAYING_TOTAL_PAGES,
                totalResults = StringConstants.PAGINATION_NOW_PLAYING_TOTAL_RESULTS,
                hasNext = page < StringConstants.PAGINATION_NOW_PLAYING_TOTAL_PAGES,
                hasPrevious = page > StringConstants.PAGINATION_FIRST_PAGE
            )

            McpMovieListResponseDto(mockMovies, pagination)
        }
    }


    override suspend fun getMovieDetails(movieId: Int): McpResponseDto<MovieDetailsDto> {
        val request = McpRequest(
            method = StringConstants.MCP_METHOD_GET_MOVIE_DETAILS,
            params = mapOf(StringConstants.PARAM_MOVIE_ID to movieId.toString())
        )

        val response = mcpHttpClient.sendRequest<Map<String, Any>>(request)

        // Debug logging for MCP response
        if (BuildConfig.DEBUG) {
            if (BuildConfig.DEBUG) {
            }
        }

        return if (response.success && response.data != null) {
            // Convert the response data to DTO format
            val data = response.data
            val movieDetailsData = data[StringConstants.FIELD_MOVIE_DETAILS] as? Map<String, Any>

            if (movieDetailsData != null) {
                val movieDetails = MovieDetailsDto(
                    id = (movieDetailsData[StringConstants.FIELD_ID] as? Number)?.toInt()
                        ?: movieId,
                    title = movieDetailsData[StringConstants.FIELD_TITLE] as? String
                        ?: StringConstants.EMPTY_STRING,
                    description = movieDetailsData[StringConstants.FIELD_DESCRIPTION] as? String
                        ?: StringConstants.EMPTY_STRING,
                    posterPath = movieDetailsData[StringConstants.FIELD_POSTER_PATH] as? String,
                    backdropPath = movieDetailsData[StringConstants.FIELD_BACKDROP_PATH] as? String,
                    rating = (movieDetailsData[StringConstants.FIELD_RATING] as? Number)?.toDouble()
                        ?: StringConstants.DEFAULT_DOUBLE_VALUE,
                    voteCount = (movieDetailsData[StringConstants.FIELD_VOTE_COUNT] as? Number)?.toInt()
                        ?: StringConstants.DEFAULT_INT_VALUE,
                    releaseDate = movieDetailsData[StringConstants.FIELD_RELEASE_DATE] as? String
                        ?: StringConstants.EMPTY_STRING,
                    runtime = (movieDetailsData[StringConstants.FIELD_RUNTIME] as? Number)?.toInt()
                        ?: StringConstants.DEFAULT_INT_VALUE,
                    genres = (movieDetailsData[StringConstants.FIELD_GENRES] as? List<Map<String, Any>>)?.map { genreData ->
                        GenreDto(
                            id = (genreData[StringConstants.FIELD_ID] as? Number)?.toInt()
                                ?: StringConstants.DEFAULT_INT_VALUE,
                            name = genreData[StringConstants.FIELD_NAME] as? String
                                ?: StringConstants.EMPTY_STRING
                        )
                    } ?: emptyList(),
                    productionCompanies = (movieDetailsData[StringConstants.FIELD_PRODUCTION_COMPANIES] as? List<Map<String, Any>>)?.map { companyData ->
                        ProductionCompanyDto(
                            id = (companyData[StringConstants.FIELD_ID] as? Number)?.toInt()
                                ?: StringConstants.DEFAULT_INT_VALUE,
                            logoPath = companyData[StringConstants.FIELD_LOGO_PATH] as? String,
                            name = companyData[StringConstants.FIELD_NAME] as? String
                                ?: StringConstants.EMPTY_STRING,
                            originCountry = companyData[StringConstants.FIELD_ORIGIN_COUNTRY] as? String
                                ?: StringConstants.EMPTY_STRING
                        )
                    } ?: emptyList(),
                    budget = (movieDetailsData[StringConstants.FIELD_BUDGET] as? Number)?.toLong()
                        ?: StringConstants.DEFAULT_LONG_VALUE,
                    revenue = (movieDetailsData[StringConstants.FIELD_REVENUE] as? Number)?.toLong()
                        ?: StringConstants.DEFAULT_LONG_VALUE,
                    status = movieDetailsData[StringConstants.FIELD_STATUS] as? String
                        ?: StringConstants.EMPTY_STRING
                )

                val uiConfig = assetDataLoader.loadUiConfig()
                
                // Debug logging for uiConfig
                if (BuildConfig.DEBUG) {
                    if (BuildConfig.DEBUG) {
                    }
                }

                McpResponseDto(
                    success = true,
                    data = movieDetails,
                    uiConfig = uiConfig,
                    error = null,
                    meta = assetDataLoader.loadMetaData(
                        StringConstants.MCP_METHOD_GET_MOVIE_DETAILS,
                        1,
                        movieId
                    )
                )
            } else {
                McpResponseDto(
                    success = false,
                    data = null,
                    uiConfig = assetDataLoader.loadUiConfig(),
                    error = StringConstants.INVALID_MOVIE_DETAILS_DATA,
                    meta = assetDataLoader.loadMetaData(
                        StringConstants.MCP_METHOD_GET_MOVIE_DETAILS,
                        StringConstants.META_RESULTS_COUNT_ZERO,
                        movieId
                    )
                )
            }
        } else {
            McpResponseDto(
                success = false,
                data = null,
                uiConfig = assetDataLoader.loadUiConfig(),
                error = response.error ?: StringConstants.ERROR_FETCHING_MOVIE_DETAILS.format(
                    StringConstants.ERROR_UNKNOWN
                ),
                meta = assetDataLoader.loadMetaData(
                    StringConstants.MCP_METHOD_GET_MOVIE_DETAILS,
                    StringConstants.META_RESULTS_COUNT_ZERO,
                    movieId
                )
            )
        }
    }

    override suspend fun getMovieRecommendations(
        movieId: Int,
        page: Int
    ): McpResponseDto<McpMovieListResponseDto> {
        return simulateNetworkCall {
            val mockMovies = assetDataLoader.loadMockMovies()
                .map { it.copy(title = "${StringConstants.RECOMMENDED_PREFIX}${it.title}") }
            val pagination = PaginationDto(
                page = page,
                totalPages = StringConstants.PAGINATION_RECOMMENDATIONS_TOTAL_PAGES,
                totalResults = StringConstants.PAGINATION_RECOMMENDATIONS_TOTAL_RESULTS,
                hasNext = page < StringConstants.PAGINATION_RECOMMENDATIONS_TOTAL_PAGES,
                hasPrevious = page > StringConstants.PAGINATION_FIRST_PAGE
            )

            McpMovieListResponseDto(mockMovies, pagination)
        }
    }

    /**
     * Business method: Get popular movies using MCP HTTP client
     */
    suspend fun getPopularMoviesViaMcp(page: Int): Result<MovieListResponse> {
        return runCatching {
            val request = McpRequest(
                method = StringConstants.MCP_METHOD_GET_POPULAR_MOVIES,
                params = mapOf(StringConstants.FIELD_PAGE to page.toString())
            )

            val response = mcpHttpClient.sendRequest<Map<String, Any>>(request)

            if (response.success && response.data != null) {
                val data = response.data
                val movies =
                    (data[StringConstants.FIELD_MOVIES] as? List<Map<String, Any>>)?.map { movieData ->
                        MovieDto(
                            id = (movieData[StringConstants.FIELD_ID] as? Number)?.toInt()
                                ?: StringConstants.DEFAULT_INT_VALUE,
                            title = movieData[StringConstants.FIELD_TITLE] as? String
                                ?: StringConstants.EMPTY_STRING,
                            description = movieData[StringConstants.FIELD_DESCRIPTION] as? String
                                ?: StringConstants.EMPTY_STRING,
                            posterPath = movieData[StringConstants.FIELD_POSTER_PATH] as? String,
                            backdropPath = movieData[StringConstants.FIELD_BACKDROP_PATH] as? String,
                            rating = (movieData[StringConstants.FIELD_RATING] as? Number)?.toDouble()
                                ?: StringConstants.DEFAULT_DOUBLE_VALUE,
                            voteCount = (movieData[StringConstants.FIELD_VOTE_COUNT] as? Number)?.toInt()
                                ?: StringConstants.DEFAULT_INT_VALUE,
                            releaseDate = movieData[StringConstants.FIELD_RELEASE_DATE] as? String
                                ?: StringConstants.EMPTY_STRING,
                            genreIds = (movieData[StringConstants.FIELD_GENRE_IDS] as? List<Number>)?.map { it.toInt() }
                                ?: emptyList(),
                            popularity = (movieData[StringConstants.FIELD_POPULARITY] as? Number)?.toDouble()
                                ?: StringConstants.DEFAULT_DOUBLE_VALUE,
                            adult = movieData[StringConstants.FIELD_ADULT] as? Boolean
                                ?: StringConstants.DEFAULT_BOOLEAN_VALUE
                        )
                    } ?: emptyList()

                val paginationData = data[StringConstants.FIELD_PAGINATION] as? Map<String, Any>
                val pagination = PaginationDto(
                    page = (paginationData?.get(StringConstants.FIELD_PAGE) as? Number)?.toInt()
                        ?: page,
                    totalPages = (paginationData?.get(StringConstants.FIELD_TOTAL_PAGES) as? Number)?.toInt()
                        ?: StringConstants.DEFAULT_TOTAL_PAGES,
                    totalResults = (paginationData?.get(StringConstants.FIELD_TOTAL_RESULTS) as? Number)?.toInt()
                        ?: StringConstants.DEFAULT_INT_VALUE,
                    hasNext = paginationData?.get(StringConstants.FIELD_HAS_NEXT) as? Boolean
                        ?: StringConstants.DEFAULT_BOOLEAN_VALUE,
                    hasPrevious = paginationData?.get(StringConstants.FIELD_HAS_PREVIOUS) as? Boolean
                        ?: StringConstants.DEFAULT_BOOLEAN_VALUE
                )

                val movieListResponse = MovieMapper.mapMcpMovieListResponseDtoToMovieListResponse(
                    McpMovieListResponseDto(movies, pagination)
                )

                val uiConfig = assetDataLoader.loadUiConfig()
                
                // Debug logging for uiConfig
                if (BuildConfig.DEBUG) {
                    if (BuildConfig.DEBUG) {
                    }
                }

                Result.Success(
                    data = movieListResponse,
                    uiConfig = MovieMapper.mapUiConfigurationDtoToUiConfiguration(uiConfig)
                )
            } else {
                Result.Error(
                    response.error ?: StringConstants.ERROR_FETCHING_POPULAR_MOVIES.format(
                        StringConstants.ERROR_UNKNOWN
                    )
                )
            }
        }.getOrElse { exception ->
            Result.Error("${StringConstants.ERROR_FETCHING_POPULAR_MOVIES.format(exception.message ?: StringConstants.ERROR_UNKNOWN)}")
        }
    }

    /**
     * Business method: Get movie details using MCP HTTP client
     */
    suspend fun getMovieDetailsViaMcp(movieId: Int): Result<MovieDetailsResponse> {
        return runCatching {
            val request = McpRequest(
                method = StringConstants.MCP_METHOD_GET_MOVIE_DETAILS,
                params = mapOf(StringConstants.PARAM_MOVIE_ID to movieId.toString())
            )

            val response = mcpHttpClient.sendRequest<Map<String, Any>>(request)

            if (response.success && response.data != null) {
                val data = response.data
                val movieDetailsData =
                    data[StringConstants.FIELD_MOVIE_DETAILS] as? Map<String, Any>

                if (movieDetailsData != null) {
                    val movieDetails = MovieDetailsDto(
                        id = (movieDetailsData[StringConstants.FIELD_ID] as? Number)?.toInt()
                            ?: movieId,
                        title = movieDetailsData[StringConstants.FIELD_TITLE] as? String
                            ?: StringConstants.EMPTY_STRING,
                        description = movieDetailsData[StringConstants.FIELD_DESCRIPTION] as? String
                            ?: StringConstants.EMPTY_STRING,
                        posterPath = movieDetailsData[StringConstants.FIELD_POSTER_PATH] as? String,
                        backdropPath = movieDetailsData[StringConstants.FIELD_BACKDROP_PATH] as? String,
                        rating = (movieDetailsData[StringConstants.FIELD_RATING] as? Number)?.toDouble()
                            ?: StringConstants.DEFAULT_DOUBLE_VALUE,
                        voteCount = (movieDetailsData[StringConstants.FIELD_VOTE_COUNT] as? Number)?.toInt()
                            ?: StringConstants.DEFAULT_INT_VALUE,
                        releaseDate = movieDetailsData[StringConstants.FIELD_RELEASE_DATE] as? String
                            ?: StringConstants.EMPTY_STRING,
                        runtime = (movieDetailsData[StringConstants.FIELD_RUNTIME] as? Number)?.toInt()
                            ?: StringConstants.DEFAULT_INT_VALUE,
                        genres = (movieDetailsData[StringConstants.FIELD_GENRES] as? List<Map<String, Any>>)?.map { genreData ->
                            GenreDto(
                                id = (genreData[StringConstants.FIELD_ID] as? Number)?.toInt()
                                    ?: StringConstants.DEFAULT_INT_VALUE,
                                name = genreData[StringConstants.FIELD_NAME] as? String
                                    ?: StringConstants.EMPTY_STRING
                            )
                        } ?: emptyList(),
                        productionCompanies = (movieDetailsData[StringConstants.FIELD_PRODUCTION_COMPANIES] as? List<Map<String, Any>>)?.map { companyData ->
                            ProductionCompanyDto(
                                id = (companyData[StringConstants.FIELD_ID] as? Number)?.toInt()
                                    ?: StringConstants.DEFAULT_INT_VALUE,
                                logoPath = companyData[StringConstants.FIELD_LOGO_PATH] as? String,
                                name = companyData[StringConstants.FIELD_NAME] as? String
                                    ?: StringConstants.EMPTY_STRING,
                                originCountry = companyData[StringConstants.FIELD_ORIGIN_COUNTRY] as? String
                                    ?: StringConstants.EMPTY_STRING
                            )
                        } ?: emptyList(),
                        budget = (movieDetailsData[StringConstants.FIELD_BUDGET] as? Number)?.toLong()
                            ?: StringConstants.DEFAULT_LONG_VALUE,
                        revenue = (movieDetailsData[StringConstants.FIELD_REVENUE] as? Number)?.toLong()
                            ?: StringConstants.DEFAULT_LONG_VALUE,
                        status = movieDetailsData[StringConstants.FIELD_STATUS] as? String
                            ?: StringConstants.EMPTY_STRING
                    )

                    val domainMovieDetails =
                        MovieMapper.mapMovieDetailsDtoToMovieDetails(movieDetails)

                    // Parse sentiment reviews from response data
                    val sentimentReviewsData = data[StringConstants.FIELD_SENTIMENT_REVIEWS] as? Map<String, Any>
                    val sentimentReviews = sentimentReviewsData?.let { reviewsData ->
                        val positive = (reviewsData[StringConstants.FIELD_POSITIVE] as? List<*>)?.mapNotNull { it as? String } ?: emptyList()
                        val negative = (reviewsData[StringConstants.FIELD_NEGATIVE] as? List<*>)?.mapNotNull { it as? String } ?: emptyList()
                        org.studioapp.cinemy.data.model.SentimentReviews(positive = positive, negative = negative)
                    }

                    val uiConfig = assetDataLoader.loadUiConfig()
                    val domainUiConfig = MovieMapper.mapUiConfigurationDtoToUiConfiguration(uiConfig)
                    
                    // Debug logging for uiConfig
                    if (BuildConfig.DEBUG) {
                        if (BuildConfig.DEBUG) {
                        }
                    }

                    Result.Success(
                        data = MovieDetailsResponse(
                            success = true,
                            data = MovieDetailsData(
                                movieDetails = domainMovieDetails,
                                sentimentReviews = sentimentReviews
                            ),
                            uiConfig = domainUiConfig,
                            error = null,
                            meta = Meta(
                                timestamp = System.currentTimeMillis().toString(),
                                method = StringConstants.MCP_METHOD_GET_MOVIE_DETAILS,
                                searchQuery = null,
                                movieId = movieId,
                                resultsCount = null,
                                aiGenerated = true,
                                geminiColors = GeminiColors(
                                    primary = StringConstants.COLOR_PRIMARY,
                                    secondary = StringConstants.COLOR_SECONDARY,
                                    accent = StringConstants.COLOR_ACCENT
                                ),
                                avgRating = null,
                                movieRating = domainMovieDetails.rating,
                                version = StringConstants.VERSION_2_0_0
                            )
                        ),
                        uiConfig = domainUiConfig
                    )
                } else {
                    Result.Error(StringConstants.INVALID_MOVIE_DETAILS_DATA)
                }
            } else {
                Result.Error(
                    response.error ?: StringConstants.ERROR_FETCHING_MOVIE_DETAILS.format(
                        StringConstants.ERROR_UNKNOWN
                    )
                )
            }
        }.getOrElse { exception ->
            Result.Error("${StringConstants.ERROR_FETCHING_MOVIE_DETAILS.format(exception.message ?: StringConstants.ERROR_UNKNOWN)}")
        }
    }


    /**
     * Clean up resources
     */
    fun close() {
        mcpHttpClient.close()
    }

    private suspend fun <T> simulateNetworkCall(block: () -> T): McpResponseDto<T> {
        return runCatching {
            // Simulate network delay
            delay(StringConstants.NETWORK_DELAY_BASE_MS + (Math.random() * StringConstants.NETWORK_DELAY_RANDOM_MAX_MS).toLong())

            // Simulate occasional network errors (5% chance)
            if (Math.random() < StringConstants.NETWORK_ERROR_PROBABILITY) {
                throw IOException(StringConstants.SIMULATED_NETWORK_ERROR)
            }

            val data = block()
            val uiConfig = assetDataLoader.loadUiConfig()
            val meta = assetDataLoader.loadMetaData(
                StringConstants.META_METHOD_UNKNOWN,
                StringConstants.META_RESULTS_COUNT_ZERO
            )

            McpResponseDto(
                success = true,
                data = data,
                uiConfig = uiConfig,
                error = null,
                meta = meta
            )
        }.getOrElse { exception ->
            McpResponseDto(
                success = false,
                data = null,
                uiConfig = assetDataLoader.loadUiConfig(),
                error = exception.message ?: StringConstants.ERROR_UNKNOWN,
                meta = MetaDto(
                    timestamp = System.currentTimeMillis().toString(),
                    method = StringConstants.METHOD_UNKNOWN,
                    searchQuery = null,
                    movieId = null,
                    resultsCount = null,
                    aiGenerated = false,
                    geminiColors = GeminiColorsDto(
                        primary = StringConstants.COLOR_ERROR,
                        secondary = StringConstants.COLOR_ERROR,
                        accent = StringConstants.COLOR_ERROR
                    ),
                    avgRating = null,
                    movieRating = null,
                    version = StringConstants.VERSION_2_0_0
                )
            )
        }
    }

}
