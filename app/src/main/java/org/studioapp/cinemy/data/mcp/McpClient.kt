package org.studioapp.cinemy.data.mcp

import android.content.Context
import org.studioapp.cinemy.data.mapper.MovieMapper
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
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BOOLEAN_VALUE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_DOUBLE_VALUE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_INT_VALUE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_LONG_VALUE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_TOTAL_PAGES
import org.studioapp.cinemy.data.model.StringConstants.EMPTY_STRING
import org.studioapp.cinemy.data.model.StringConstants.ERROR_FETCHING_MOVIE_DETAILS
import org.studioapp.cinemy.data.model.StringConstants.ERROR_FETCHING_POPULAR_MOVIES
import org.studioapp.cinemy.data.model.StringConstants.ERROR_UNKNOWN
import org.studioapp.cinemy.data.model.StringConstants.FIELD_API_SUCCESS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_BACKDROP_PATH
import org.studioapp.cinemy.data.model.StringConstants.FIELD_BUDGET
import org.studioapp.cinemy.data.model.StringConstants.FIELD_DESCRIPTION
import org.studioapp.cinemy.data.model.StringConstants.FIELD_GENRES
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ID
import org.studioapp.cinemy.data.model.StringConstants.FIELD_LOGO_PATH
import org.studioapp.cinemy.data.model.StringConstants.FIELD_MOVIE_DETAILS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_NAME
import org.studioapp.cinemy.data.model.StringConstants.FIELD_NEGATIVE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_NEGATIVE_COUNT
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ORIGIN_COUNTRY
import org.studioapp.cinemy.data.model.StringConstants.FIELD_PAGE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_POSITIVE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_POSITIVE_COUNT
import org.studioapp.cinemy.data.model.StringConstants.FIELD_POSTER_PATH
import org.studioapp.cinemy.data.model.StringConstants.FIELD_PRODUCTION_COMPANIES
import org.studioapp.cinemy.data.model.StringConstants.FIELD_RATING
import org.studioapp.cinemy.data.model.StringConstants.FIELD_RELEASE_DATE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_REVENUE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_RUNTIME
import org.studioapp.cinemy.data.model.StringConstants.FIELD_SENTIMENT_METADATA
import org.studioapp.cinemy.data.model.StringConstants.FIELD_SENTIMENT_REVIEWS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_SOURCE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_STATUS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_TIMESTAMP
import org.studioapp.cinemy.data.model.StringConstants.FIELD_TITLE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_TOTAL_REVIEWS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_VOTE_COUNT
import org.studioapp.cinemy.data.model.StringConstants.INVALID_MOVIE_DETAILS_DATA
import org.studioapp.cinemy.data.model.StringConstants.MCP_METHOD_GET_MOVIE_DETAILS
import org.studioapp.cinemy.data.model.StringConstants.MCP_METHOD_GET_POPULAR_MOVIES
import org.studioapp.cinemy.data.model.StringConstants.META_RESULTS_COUNT_ZERO
import org.studioapp.cinemy.data.model.StringConstants.PARAM_MOVIE_ID
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ACCENT
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ADULT
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_BACKDROP_PATH
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_CATEGORY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_COLORS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_GENRE_IDS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ID
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_METADATA
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_MODEL_USED
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ORIGINAL_LANGUAGE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ORIGINAL_TITLE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_OVERVIEW
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_PAGE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_POPULARITY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_POSTER_PATH
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_PRIMARY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RATING
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RELEASE_DATE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RESULTS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_SECONDARY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_TITLE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_TOTAL_PAGES
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_TOTAL_RESULTS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_VIDEO
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_VOTE_AVERAGE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_VOTE_COUNT
import org.studioapp.cinemy.data.model.StringConstants.VERSION_2_0_0
import org.studioapp.cinemy.data.remote.api.MovieApiService
import org.studioapp.cinemy.data.remote.dto.ButtonConfigurationDto
import org.studioapp.cinemy.data.remote.dto.ColorMetadataDto
import org.studioapp.cinemy.data.remote.dto.ColorSchemeDto
import org.studioapp.cinemy.data.remote.dto.GenreDto
import org.studioapp.cinemy.data.remote.dto.McpResponseDto
import org.studioapp.cinemy.data.remote.dto.MovieColorsDto
import org.studioapp.cinemy.data.remote.dto.MovieDetailsDto
import org.studioapp.cinemy.data.remote.dto.MovieDto
import org.studioapp.cinemy.data.remote.dto.MovieListResponseDto
import org.studioapp.cinemy.data.remote.dto.ProductionCompanyDto
import org.studioapp.cinemy.data.remote.dto.SentimentMetadataDto
import org.studioapp.cinemy.data.remote.dto.SentimentReviewsDto
import org.studioapp.cinemy.data.remote.dto.TextConfigurationDto
import org.studioapp.cinemy.data.remote.dto.UiConfigurationDto

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

            val movies = (data?.get(SERIALIZED_RESULTS) as? List<Map<String, Any>>)?.map { movieData ->
                MovieMapper.mapJsonToMovieDto(movieData)
            } ?: emptyList()

            val movieListResponse = MovieListResponseDto(
                page = (data?.get(SERIALIZED_PAGE) as? Number)?.toInt() ?: page,
                results = movies,
                totalPages = (data?.get(SERIALIZED_TOTAL_PAGES) as? Number)?.toInt()
                    ?: DEFAULT_TOTAL_PAGES,
                totalResults = (data?.get(SERIALIZED_TOTAL_RESULTS) as? Number)?.toInt()
                    ?: DEFAULT_INT_VALUE
            )

            val uiConfig = assetDataLoader.loadUiConfig()

            // Debug logging for uiConfig

            McpResponseDto(
                success = true,
                data = movieListResponse,
                uiConfig = uiConfig,
                error = null,
                meta = assetDataLoader.loadMetaData(
                    MCP_METHOD_GET_POPULAR_MOVIES,
                    movies.size
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
                val movieDetails = MovieMapper.mapJsonToMovieDetailsDto(movieDetailsData, movieId)

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

                val movies =
                    (data?.get(SERIALIZED_RESULTS) as? List<Map<String, Any>>)?.map { movieData ->
                        val colorsData =
                            movieData[SERIALIZED_COLORS] as? Map<String, Any>
                        val metadataData =
                            colorsData?.get(SERIALIZED_METADATA) as? Map<String, Any>

                        MovieDto(
                            id = (movieData[SERIALIZED_ID] as? Number)?.toInt()
                                ?: DEFAULT_INT_VALUE,
                            title = movieData[SERIALIZED_TITLE] as? String
                                ?: EMPTY_STRING,
                            description = movieData[SERIALIZED_OVERVIEW] as? String
                                ?: EMPTY_STRING,
                            posterPath = movieData[SERIALIZED_POSTER_PATH] as? String,
                            backdropPath = movieData[SERIALIZED_BACKDROP_PATH] as? String,
                            rating = (movieData[SERIALIZED_VOTE_AVERAGE] as? Number)?.toDouble()
                                ?: DEFAULT_DOUBLE_VALUE,
                            voteCount = (movieData[SERIALIZED_VOTE_COUNT] as? Number)?.toInt()
                                ?: DEFAULT_INT_VALUE,
                            releaseDate = movieData[SERIALIZED_RELEASE_DATE] as? String
                                ?: EMPTY_STRING,
                            genreIds = (movieData[SERIALIZED_GENRE_IDS] as? List<Number>)?.map { it.toInt() }
                                ?: emptyList(),
                            popularity = (movieData[SERIALIZED_POPULARITY] as? Number)?.toDouble()
                                ?: DEFAULT_DOUBLE_VALUE,
                            adult = movieData[SERIALIZED_ADULT] as? Boolean
                                ?: DEFAULT_BOOLEAN_VALUE,
                            originalLanguage = movieData[SERIALIZED_ORIGINAL_LANGUAGE] as? String
                                ?: EMPTY_STRING,
                            originalTitle = movieData[SERIALIZED_ORIGINAL_TITLE] as? String
                                ?: EMPTY_STRING,
                            video = movieData[SERIALIZED_VIDEO] as? Boolean
                                ?: DEFAULT_BOOLEAN_VALUE,
                            colors = MovieColorsDto(
                                accent = colorsData?.get(SERIALIZED_ACCENT) as? String
                                    ?: EMPTY_STRING,
                                primary = colorsData?.get(SERIALIZED_PRIMARY) as? String
                                    ?: EMPTY_STRING,
                                secondary = colorsData?.get(SERIALIZED_SECONDARY) as? String
                                    ?: EMPTY_STRING,
                                metadata = ColorMetadataDto(
                                    category = metadataData?.get(SERIALIZED_CATEGORY) as? String
                                        ?: EMPTY_STRING,
                                    modelUsed = metadataData?.get(SERIALIZED_MODEL_USED) as? Boolean
                                        ?: DEFAULT_BOOLEAN_VALUE,
                                    rating = (metadataData?.get(SERIALIZED_RATING) as? Number)?.toDouble()
                                        ?: DEFAULT_DOUBLE_VALUE
                                )
                            )
                        )
                    } ?: emptyList()

                val backendPage =
                    (data?.get(SERIALIZED_PAGE) as? Number)?.toInt() ?: page
                val backendTotalPages =
                    (data?.get(SERIALIZED_TOTAL_PAGES) as? Number)?.toInt()
                        ?: DEFAULT_TOTAL_PAGES
                val backendTotalResults =
                    (data?.get(SERIALIZED_TOTAL_RESULTS) as? Number)?.toInt()
                        ?: DEFAULT_INT_VALUE


                val movieListResponse = MovieListResponseDto(
                    page = backendPage,
                    results = movies,
                    totalPages = backendTotalPages,
                    totalResults = backendTotalResults
                )

                val domainMovieListResponse =
                    MovieMapper.mapMovieListResponseDtoToMovieListResponse(movieListResponse)

                val uiConfig = assetDataLoader.loadUiConfig()

                // Debug logging for uiConfig

                Result.Success(
                    data = domainMovieListResponse,
                    uiConfig = MovieMapper.mapUiConfigurationDtoToUiConfiguration(uiConfig)
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
                val innerData = data?.get("data") as? Map<String, Any>
                val movieDetailsData =
                    innerData?.get(FIELD_MOVIE_DETAILS) as? Map<String, Any>

                if (movieDetailsData != null) {
                    // Parse sentiment reviews and metadata
                    val sentimentReviewsData =
                        innerData?.get(FIELD_SENTIMENT_REVIEWS) as? Map<String, Any>
                    val sentimentMetadataData =
                        innerData?.get(FIELD_SENTIMENT_METADATA) as? Map<String, Any>

                    val sentimentReviews = sentimentReviewsData?.let { reviewsData ->
                        SentimentReviewsDto(
                            positive = (reviewsData[FIELD_POSITIVE] as? List<String>)
                                ?: emptyList(),
                            negative = (reviewsData[FIELD_NEGATIVE] as? List<String>)
                                ?: emptyList()
                        )
                    }

                    val sentimentMetadata = sentimentMetadataData?.let { metadataData ->
                        SentimentMetadataDto(
                            totalReviews = (metadataData[FIELD_TOTAL_REVIEWS] as? Number)?.toInt()
                                ?: 0,
                            positiveCount = (metadataData[FIELD_POSITIVE_COUNT] as? Number)?.toInt()
                                ?: 0,
                            negativeCount = (metadataData[FIELD_NEGATIVE_COUNT] as? Number)?.toInt()
                                ?: 0,
                            source = metadataData[FIELD_SOURCE] as? String ?: "",
                            timestamp = metadataData[FIELD_TIMESTAMP] as? String
                                ?: "",
                            apiSuccess = (metadataData[FIELD_API_SUCCESS] as? Map<String, Boolean>)
                                ?: emptyMap()
                        )
                    }

                    val movieDetails = MovieDetailsDto(
                        id = (movieDetailsData[FIELD_ID] as? Number)?.toInt()
                            ?: movieId,
                        title = movieDetailsData[FIELD_TITLE] as? String
                            ?: EMPTY_STRING,
                        description = movieDetailsData[FIELD_DESCRIPTION] as? String
                            ?: EMPTY_STRING,
                        posterPath = movieDetailsData[FIELD_POSTER_PATH] as? String,
                        backdropPath = movieDetailsData[FIELD_BACKDROP_PATH] as? String,
                        rating = (movieDetailsData[FIELD_RATING] as? Number)?.toDouble()
                            ?: DEFAULT_DOUBLE_VALUE,
                        voteCount = (movieDetailsData[FIELD_VOTE_COUNT] as? Number)?.toInt()
                            ?: DEFAULT_INT_VALUE,
                        releaseDate = movieDetailsData[FIELD_RELEASE_DATE] as? String
                            ?: EMPTY_STRING,
                        runtime = (movieDetailsData[FIELD_RUNTIME] as? Number)?.toInt()
                            ?: DEFAULT_INT_VALUE,
                        genres = (movieDetailsData[FIELD_GENRES] as? List<Map<String, Any>>)?.map { genreData ->
                            GenreDto(
                                id = (genreData[FIELD_ID] as? Number)?.toInt()
                                    ?: DEFAULT_INT_VALUE,
                                name = genreData[FIELD_NAME] as? String
                                    ?: EMPTY_STRING
                            )
                        } ?: emptyList(),
                        productionCompanies = (movieDetailsData[FIELD_PRODUCTION_COMPANIES] as? List<Map<String, Any>>)?.map { companyData ->
                            ProductionCompanyDto(
                                id = (companyData[FIELD_ID] as? Number)?.toInt()
                                    ?: DEFAULT_INT_VALUE,
                                logoPath = companyData[FIELD_LOGO_PATH] as? String,
                                name = companyData[FIELD_NAME] as? String
                                    ?: EMPTY_STRING,
                                originCountry = companyData[FIELD_ORIGIN_COUNTRY] as? String
                                    ?: EMPTY_STRING
                            )
                        } ?: emptyList(),
                        budget = (movieDetailsData[FIELD_BUDGET] as? Number)?.toLong()
                            ?: DEFAULT_LONG_VALUE,
                        revenue = (movieDetailsData[FIELD_REVENUE] as? Number)?.toLong()
                            ?: DEFAULT_LONG_VALUE,
                        status = movieDetailsData[FIELD_STATUS] as? String
                            ?: EMPTY_STRING,
                        sentimentReviews = sentimentReviews,
                        sentimentMetadata = sentimentMetadata
                    )

                    val domainMovieDetails =
                        MovieMapper.mapMovieDetailsDtoToMovieDetails(movieDetails)

                    // Map sentiment reviews and metadata to domain models
                    val domainSentimentReviews =
                        MovieMapper.mapSentimentReviewsDtoToSentimentReviews(sentimentReviews)
                    val domainSentimentMetadata =
                        MovieMapper.mapSentimentMetadataDtoToSentimentMetadata(sentimentMetadata)

                    // Extract uiConfig from backend response
                    val backendUiConfig = data?.get("uiConfig") as? Map<String, Any>
                    val uiConfig = if (backendUiConfig != null) {
                        // Parse uiConfig from backend response
                        val colors = backendUiConfig["colors"] as? Map<String, Any>
                        val texts = backendUiConfig["texts"] as? Map<String, Any>
                        val buttons = backendUiConfig["buttons"] as? Map<String, Any>

                        if (colors != null && texts != null && buttons != null) {
                            UiConfigurationDto(
                                colors = ColorSchemeDto(
                                    primary = colors["primary"] as? String ?: "#DC3528",
                                    secondary = colors["secondary"] as? String ?: "#E64539",
                                    background = colors["background"] as? String ?: "#121212",
                                    surface = colors["surface"] as? String ?: "#1E1E1E",
                                    onPrimary = colors["onPrimary"] as? String ?: "#FFFFFF",
                                    onSecondary = colors["onSecondary"] as? String ?: "#FFFFFF",
                                    onBackground = colors["onBackground"] as? String ?: "#FFFFFF",
                                    onSurface = colors["onSurface"] as? String ?: "#FFFFFF",
                                    moviePosterColors = (colors["moviePosterColors"] as? List<*>)?.mapNotNull { it as? String }
                                        ?: emptyList()
                                ),
                                texts = TextConfigurationDto(
                                    appTitle = texts["appTitle"] as? String
                                        ?: "TmdbAi - Movie Details",
                                    loadingText = texts["loadingText"] as? String
                                        ?: "Loading details...",
                                    errorMessage = texts["errorMessage"] as? String
                                        ?: "Error loading movie",
                                    noMoviesFound = texts["noMoviesFound"] as? String
                                        ?: "Movie not found",
                                    retryButton = texts["retryButton"] as? String ?: "Retry",
                                    backButton = texts["backButton"] as? String ?: "Back to list",
                                    playButton = texts["playButton"] as? String ?: "Watch"
                                ),
                                buttons = ButtonConfigurationDto(
                                    primaryButtonColor = buttons["primaryButtonColor"] as? String
                                        ?: "#DC3528",
                                    secondaryButtonColor = buttons["secondaryButtonColor"] as? String
                                        ?: "#E64539",
                                    buttonTextColor = buttons["buttonTextColor"] as? String
                                        ?: "#FFFFFF",
                                    buttonCornerRadius = (buttons["buttonCornerRadius"] as? Number)?.toInt()
                                        ?: 12
                                )
                            )
                        } else {
                            assetDataLoader.loadUiConfig()
                        }
                    } else {
                        assetDataLoader.loadUiConfig()
                    }

                    val domainUiConfig =
                        MovieMapper.mapUiConfigurationDtoToUiConfiguration(uiConfig)

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
