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
import org.studioapp.cinemy.data.model.StringConstants
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
            method = StringConstants.MCP_METHOD_GET_POPULAR_MOVIES,
            params = mapOf(StringConstants.FIELD_PAGE to page.toString())
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

            val movies =
                (data?.get(StringConstants.SERIALIZED_RESULTS) as? List<Map<String, Any>>)?.map { movieData ->
                    val colorsData =
                        movieData[StringConstants.SERIALIZED_COLORS] as? Map<String, Any>
                    val metadataData =
                        colorsData?.get(StringConstants.SERIALIZED_METADATA) as? Map<String, Any>

                    MovieDto(
                        id = (movieData[StringConstants.SERIALIZED_ID] as? Number)?.toInt()
                            ?: StringConstants.DEFAULT_INT_VALUE,
                        title = movieData[StringConstants.SERIALIZED_TITLE] as? String
                            ?: StringConstants.EMPTY_STRING,
                        description = movieData[StringConstants.SERIALIZED_OVERVIEW] as? String
                            ?: StringConstants.EMPTY_STRING,
                        posterPath = movieData[StringConstants.SERIALIZED_POSTER_PATH] as? String,
                        backdropPath = movieData[StringConstants.SERIALIZED_BACKDROP_PATH] as? String,
                        rating = (movieData[StringConstants.SERIALIZED_VOTE_AVERAGE] as? Number)?.toDouble()
                            ?: StringConstants.DEFAULT_DOUBLE_VALUE,
                        voteCount = (movieData[StringConstants.SERIALIZED_VOTE_COUNT] as? Number)?.toInt()
                            ?: StringConstants.DEFAULT_INT_VALUE,
                        releaseDate = movieData[StringConstants.SERIALIZED_RELEASE_DATE] as? String
                            ?: StringConstants.EMPTY_STRING,
                        genreIds = (movieData[StringConstants.SERIALIZED_GENRE_IDS] as? List<Number>)?.map { it.toInt() }
                            ?: emptyList(),
                        popularity = (movieData[StringConstants.SERIALIZED_POPULARITY] as? Number)?.toDouble()
                            ?: StringConstants.DEFAULT_DOUBLE_VALUE,
                        adult = movieData[StringConstants.SERIALIZED_ADULT] as? Boolean
                            ?: StringConstants.DEFAULT_BOOLEAN_VALUE,
                        originalLanguage = movieData[StringConstants.SERIALIZED_ORIGINAL_LANGUAGE] as? String
                            ?: StringConstants.EMPTY_STRING,
                        originalTitle = movieData[StringConstants.SERIALIZED_ORIGINAL_TITLE] as? String
                            ?: StringConstants.EMPTY_STRING,
                        video = movieData[StringConstants.SERIALIZED_VIDEO] as? Boolean
                            ?: StringConstants.DEFAULT_BOOLEAN_VALUE,
                        colors = MovieColorsDto(
                            accent = colorsData?.get(StringConstants.SERIALIZED_ACCENT) as? String
                                ?: StringConstants.EMPTY_STRING,
                            primary = colorsData?.get(StringConstants.SERIALIZED_PRIMARY) as? String
                                ?: StringConstants.EMPTY_STRING,
                            secondary = colorsData?.get(StringConstants.SERIALIZED_SECONDARY) as? String
                                ?: StringConstants.EMPTY_STRING,
                            metadata = ColorMetadataDto(
                                category = metadataData?.get(StringConstants.SERIALIZED_CATEGORY) as? String
                                    ?: StringConstants.EMPTY_STRING,
                                modelUsed = metadataData?.get(StringConstants.SERIALIZED_MODEL_USED) as? Boolean
                                    ?: StringConstants.DEFAULT_BOOLEAN_VALUE,
                                rating = (metadataData?.get(StringConstants.SERIALIZED_RATING) as? Number)?.toDouble()
                                    ?: StringConstants.DEFAULT_DOUBLE_VALUE
                            )
                        )
                    )
                } ?: emptyList()

            val movieListResponse = MovieListResponseDto(
                page = (data?.get(StringConstants.SERIALIZED_PAGE) as? Number)?.toInt() ?: page,
                results = movies,
                totalPages = (data?.get(StringConstants.SERIALIZED_TOTAL_PAGES) as? Number)?.toInt()
                    ?: StringConstants.DEFAULT_TOTAL_PAGES,
                totalResults = (data?.get(StringConstants.SERIALIZED_TOTAL_RESULTS) as? Number)?.toInt()
                    ?: StringConstants.DEFAULT_INT_VALUE
            )

            val uiConfig = assetDataLoader.loadUiConfig()

            // Debug logging for uiConfig

            McpResponseDto(
                success = true,
                data = movieListResponse,
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


    /**
     * Fetches movie details from MCP backend
     * @param movieId Unique identifier of the movie
     * @return McpResponseDto containing MovieDetailsDto with complete movie details and UI config
     */
    override suspend fun getMovieDetails(movieId: Int): McpResponseDto<MovieDetailsDto> {
        val request = McpRequest(
            method = StringConstants.MCP_METHOD_GET_MOVIE_DETAILS,
            params = mapOf(StringConstants.PARAM_MOVIE_ID to movieId.toString())
        )

        val response = mcpHttpClient.sendRequest<Map<String, Any>>(request)


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


    /**
     * Fetches popular movies using MCP HTTP client and returns domain models
     * @param page Page number for pagination
     * @return Result containing MovieListResponse with domain models and UI config
     */
    suspend fun getPopularMoviesViaMcp(page: Int): Result<MovieListResponse> {
        return runCatching {
            val request = McpRequest(
                method = StringConstants.MCP_METHOD_GET_POPULAR_MOVIES,
                params = mapOf(StringConstants.FIELD_PAGE to page.toString())
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
                    (data?.get(StringConstants.SERIALIZED_RESULTS) as? List<Map<String, Any>>)?.map { movieData ->
                        val colorsData =
                            movieData[StringConstants.SERIALIZED_COLORS] as? Map<String, Any>
                        val metadataData =
                            colorsData?.get(StringConstants.SERIALIZED_METADATA) as? Map<String, Any>

                        MovieDto(
                            id = (movieData[StringConstants.SERIALIZED_ID] as? Number)?.toInt()
                                ?: StringConstants.DEFAULT_INT_VALUE,
                            title = movieData[StringConstants.SERIALIZED_TITLE] as? String
                                ?: StringConstants.EMPTY_STRING,
                            description = movieData[StringConstants.SERIALIZED_OVERVIEW] as? String
                                ?: StringConstants.EMPTY_STRING,
                            posterPath = movieData[StringConstants.SERIALIZED_POSTER_PATH] as? String,
                            backdropPath = movieData[StringConstants.SERIALIZED_BACKDROP_PATH] as? String,
                            rating = (movieData[StringConstants.SERIALIZED_VOTE_AVERAGE] as? Number)?.toDouble()
                                ?: StringConstants.DEFAULT_DOUBLE_VALUE,
                            voteCount = (movieData[StringConstants.SERIALIZED_VOTE_COUNT] as? Number)?.toInt()
                                ?: StringConstants.DEFAULT_INT_VALUE,
                            releaseDate = movieData[StringConstants.SERIALIZED_RELEASE_DATE] as? String
                                ?: StringConstants.EMPTY_STRING,
                            genreIds = (movieData[StringConstants.SERIALIZED_GENRE_IDS] as? List<Number>)?.map { it.toInt() }
                                ?: emptyList(),
                            popularity = (movieData[StringConstants.SERIALIZED_POPULARITY] as? Number)?.toDouble()
                                ?: StringConstants.DEFAULT_DOUBLE_VALUE,
                            adult = movieData[StringConstants.SERIALIZED_ADULT] as? Boolean
                                ?: StringConstants.DEFAULT_BOOLEAN_VALUE,
                            originalLanguage = movieData[StringConstants.SERIALIZED_ORIGINAL_LANGUAGE] as? String
                                ?: StringConstants.EMPTY_STRING,
                            originalTitle = movieData[StringConstants.SERIALIZED_ORIGINAL_TITLE] as? String
                                ?: StringConstants.EMPTY_STRING,
                            video = movieData[StringConstants.SERIALIZED_VIDEO] as? Boolean
                                ?: StringConstants.DEFAULT_BOOLEAN_VALUE,
                            colors = MovieColorsDto(
                                accent = colorsData?.get(StringConstants.SERIALIZED_ACCENT) as? String
                                    ?: StringConstants.EMPTY_STRING,
                                primary = colorsData?.get(StringConstants.SERIALIZED_PRIMARY) as? String
                                    ?: StringConstants.EMPTY_STRING,
                                secondary = colorsData?.get(StringConstants.SERIALIZED_SECONDARY) as? String
                                    ?: StringConstants.EMPTY_STRING,
                                metadata = ColorMetadataDto(
                                    category = metadataData?.get(StringConstants.SERIALIZED_CATEGORY) as? String
                                        ?: StringConstants.EMPTY_STRING,
                                    modelUsed = metadataData?.get(StringConstants.SERIALIZED_MODEL_USED) as? Boolean
                                        ?: StringConstants.DEFAULT_BOOLEAN_VALUE,
                                    rating = (metadataData?.get(StringConstants.SERIALIZED_RATING) as? Number)?.toDouble()
                                        ?: StringConstants.DEFAULT_DOUBLE_VALUE
                                )
                            )
                        )
                    } ?: emptyList()

                val backendPage =
                    (data?.get(StringConstants.SERIALIZED_PAGE) as? Number)?.toInt() ?: page
                val backendTotalPages =
                    (data?.get(StringConstants.SERIALIZED_TOTAL_PAGES) as? Number)?.toInt()
                        ?: StringConstants.DEFAULT_TOTAL_PAGES
                val backendTotalResults =
                    (data?.get(StringConstants.SERIALIZED_TOTAL_RESULTS) as? Number)?.toInt()
                        ?: StringConstants.DEFAULT_INT_VALUE


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
     * Fetches movie details using MCP HTTP client and returns domain models
     * @param movieId Unique identifier of the movie
     * @return Result containing MovieDetailsResponse with domain models and UI config
     */
    suspend fun getMovieDetailsViaMcp(movieId: Int): Result<MovieDetailsResponse> {
        return runCatching {
            val request = McpRequest(
                method = StringConstants.MCP_METHOD_GET_MOVIE_DETAILS,
                params = mapOf(StringConstants.PARAM_MOVIE_ID to movieId.toString())
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
                    innerData?.get(StringConstants.FIELD_MOVIE_DETAILS) as? Map<String, Any>

                if (movieDetailsData != null) {
                    // Parse sentiment reviews and metadata
                    val sentimentReviewsData =
                        innerData?.get(StringConstants.FIELD_SENTIMENT_REVIEWS) as? Map<String, Any>
                    val sentimentMetadataData =
                        innerData?.get(StringConstants.FIELD_SENTIMENT_METADATA) as? Map<String, Any>

                    val sentimentReviews = sentimentReviewsData?.let { reviewsData ->
                        SentimentReviewsDto(
                            positive = (reviewsData[StringConstants.FIELD_POSITIVE] as? List<String>)
                                ?: emptyList(),
                            negative = (reviewsData[StringConstants.FIELD_NEGATIVE] as? List<String>)
                                ?: emptyList()
                        )
                    }

                    val sentimentMetadata = sentimentMetadataData?.let { metadataData ->
                        SentimentMetadataDto(
                            totalReviews = (metadataData[StringConstants.FIELD_TOTAL_REVIEWS] as? Number)?.toInt()
                                ?: 0,
                            positiveCount = (metadataData[StringConstants.FIELD_POSITIVE_COUNT] as? Number)?.toInt()
                                ?: 0,
                            negativeCount = (metadataData[StringConstants.FIELD_NEGATIVE_COUNT] as? Number)?.toInt()
                                ?: 0,
                            source = metadataData[StringConstants.FIELD_SOURCE] as? String ?: "",
                            timestamp = metadataData[StringConstants.FIELD_TIMESTAMP] as? String
                                ?: "",
                            apiSuccess = (metadataData[StringConstants.FIELD_API_SUCCESS] as? Map<String, Boolean>)
                                ?: emptyMap()
                        )
                    }

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
                            ?: StringConstants.EMPTY_STRING,
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
     * Closes MCP HTTP client and cleans up resources
     */
    fun close() {
        mcpHttpClient.close()
    }


}
