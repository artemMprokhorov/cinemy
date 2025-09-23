package org.studioapp.cinemy.data.repository

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import org.json.JSONObject
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

    override suspend fun getPopularMovies(page: Int): Result<MovieListResponse> {
        return if (BuildConfig.USE_MOCK_DATA) {
            // Dummy mode: use mock data
            loadMockPopularMovies(page)
        } else {
            // Prod mode: use MCP client
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


    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsResponse> {
        return if (BuildConfig.USE_MOCK_DATA) {
            // Dummy mode: use mock data
            loadMockMovieDetails(movieId)
        } else {
            // Prod mode: use MCP client
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

    // Mock data methods for dummy mode
    private suspend fun loadMockPopularMovies(page: Int): Result<MovieListResponse> {
        // Simulate network delay
        delay(StringConstants.NETWORK_DELAY_BASE_MS)

        // Load mock movies from assets
        val movieDtos = assetDataLoader.loadMockMovies()
        val movies = movieDtos.map { MovieMapper.mapMovieDtoToMovie(it) }

        // Create pagination
        val pagination = Pagination(
            page = page,
            totalPages = 3, // Default total pages for mock data
            totalResults = 30, // Default total results for mock data
            hasNext = page < 3,
            hasPrevious = page > StringConstants.PAGINATION_FIRST_PAGE
        )

        // Create UI config and meta (using defaults for now)
        val uiConfig = createDefaultUiConfig()

        val response = MovieListResponse(
            page = pagination.page,
            results = movies,
            totalPages = pagination.totalPages,
            totalResults = pagination.totalResults
        )

        return Result.Success(response, uiConfig)
    }

    private suspend fun loadMockMovieDetails(movieId: Int): Result<MovieDetailsResponse> {
        // Simulate network delay
        delay(StringConstants.NETWORK_DELAY_BASE_MS)

        // Load mock movie details from assets
        val movieDetails = loadMockMovieDetailsFromAssets()
        val sentimentReviews = loadSentimentReviewsFromAssets()

        // Create data wrapper
        val movieDetailsData = MovieDetailsData(
            movieDetails = movieDetails,
            sentimentReviews = sentimentReviews
        )

        // Create UI config and meta (using defaults for now)
        val uiConfig = createDefaultUiConfig()
        val meta = createDefaultMeta()

        val response = MovieDetailsResponse(
            success = true,
            data = movieDetailsData,
            uiConfig = uiConfig,
            meta = meta
        )

        return Result.Success(response)
    }

    /**
     * Loads mock movie details from assets
     */
    private fun loadMockMovieDetailsFromAssets(): MovieDetails {
        return runCatching {
            val jsonString = loadJsonFromAssets(StringConstants.ASSET_MOCK_MOVIE_DETAILS)
            if (jsonString != null) {
                val jsonObject = JSONObject(jsonString)
                val dataJson = jsonObject.optJSONObject(StringConstants.FIELD_DATA)
                val movieDetailsJson = dataJson?.optJSONObject("movieDetails")
                if (movieDetailsJson != null) {
                    parseMovieDetailsFromJson(movieDetailsJson)
                } else {
                    createDefaultMovieDetails()
                }
            } else {
                createDefaultMovieDetails()
            }
        }.getOrElse {
            createDefaultMovieDetails()
        }
    }

    /**
     * Loads sentiment reviews from assets
     */
    private fun loadSentimentReviewsFromAssets(): org.studioapp.cinemy.data.model.SentimentReviews? {
        return runCatching {
            val jsonString = loadJsonFromAssets(StringConstants.ASSET_MOCK_MOVIE_DETAILS)
            if (jsonString != null) {
                val jsonObject = JSONObject(jsonString)
                val dataJson = jsonObject.optJSONObject(StringConstants.FIELD_DATA)
                val sentimentReviewsJson =
                    dataJson?.optJSONObject(StringConstants.FIELD_SENTIMENT_REVIEWS)
                if (sentimentReviewsJson != null) {
                    parseSentimentReviewsFromJson(sentimentReviewsJson)
                } else {
                    null
                }
            } else {
                null
            }
        }.getOrElse { null }
    }

    /**
     * Loads JSON content from assets
     */
    private fun loadJsonFromAssets(fileName: String): String? {
        return runCatching {
            // Use AssetDataLoader's public method to load mock movies
            if (fileName == StringConstants.ASSET_MOCK_MOVIES) {
                // For movies, we can use the existing method
                val movies = assetDataLoader.loadMockMovies()
                // Convert to JSON string (simplified approach)
                return@runCatching "{\"movies\": []}" // Placeholder
            } else {
                // For other files, we need to implement a different approach
                return@runCatching null
            }
        }.getOrNull()
    }

    /**
     * Parses movie details from JSON
     */
    private fun parseMovieDetailsFromJson(json: JSONObject): MovieDetails {
        return MovieDetails(
            id = json.optInt("id", 1),
            title = json.optString("title", "Unknown Movie"),
            description = json.optString("description", "No description available"),
            posterPath = json.optString("posterPath"),
            backdropPath = json.optString("backdropPath"),
            rating = json.optDouble("rating", 0.0),
            voteCount = json.optInt("voteCount", 0),
            releaseDate = json.optString("releaseDate", ""),
            runtime = json.optInt("runtime", 0),
            genres = parseGenresFromJson(json.optJSONArray("genres")),
            productionCompanies = parseProductionCompaniesFromJson(json.optJSONArray("productionCompanies")),
            budget = json.optLong("budget", 0),
            revenue = json.optLong("revenue", 0),
            status = json.optString("status", "Unknown")
        )
    }

    /**
     * Parses genres from JSON array
     */
    private fun parseGenresFromJson(genresArray: org.json.JSONArray?): List<org.studioapp.cinemy.data.model.Genre> {
        if (genresArray == null) return emptyList()

        val genres = mutableListOf<org.studioapp.cinemy.data.model.Genre>()
        for (i in 0 until genresArray.length()) {
            val genreJson = genresArray.optJSONObject(i)
            if (genreJson != null) {
                genres.add(
                    org.studioapp.cinemy.data.model.Genre(
                        id = genreJson.optInt("id", 0),
                        name = genreJson.optString("name", "Unknown")
                    )
                )
            }
        }
        return genres
    }

    /**
     * Parses production companies from JSON array
     */
    private fun parseProductionCompaniesFromJson(companiesArray: org.json.JSONArray?): List<org.studioapp.cinemy.data.model.ProductionCompany> {
        if (companiesArray == null) return emptyList()

        val companies = mutableListOf<org.studioapp.cinemy.data.model.ProductionCompany>()
        for (i in 0 until companiesArray.length()) {
            val companyJson = companiesArray.optJSONObject(i)
            if (companyJson != null) {
                companies.add(
                    org.studioapp.cinemy.data.model.ProductionCompany(
                        id = companyJson.optInt("id", 0),
                        logoPath = companyJson.optString("logoPath"),
                        name = companyJson.optString("name", "Unknown"),
                        originCountry = companyJson.optString("originCountry", "")
                    )
                )
            }
        }
        return companies
    }

    /**
     * Parses sentiment reviews from JSON
     */
    private fun parseSentimentReviewsFromJson(json: JSONObject): org.studioapp.cinemy.data.model.SentimentReviews {
        val positiveArray = json.optJSONArray("positive")
        val negativeArray = json.optJSONArray("negative")

        val positive = mutableListOf<String>()
        val negative = mutableListOf<String>()

        if (positiveArray != null) {
            for (i in 0 until positiveArray.length()) {
                positive.add(positiveArray.optString(i, ""))
            }
        }

        if (negativeArray != null) {
            for (i in 0 until negativeArray.length()) {
                negative.add(negativeArray.optString(i, ""))
            }
        }

        return org.studioapp.cinemy.data.model.SentimentReviews(
            positive = positive,
            negative = negative
        )
    }

    /**
     * Creates default movie details
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
     * Creates default UI configuration
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
     * Creates default meta information
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
