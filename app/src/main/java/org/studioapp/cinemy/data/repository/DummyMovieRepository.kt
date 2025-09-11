package org.studioapp.cinemy.data.repository

import android.content.Context
import androidx.compose.ui.graphics.Color
import org.studioapp.cinemy.data.mapper.MovieMapper
import org.studioapp.cinemy.data.mcp.AssetDataLoader
import org.studioapp.cinemy.data.model.Meta
import org.studioapp.cinemy.data.model.MovieDetails
import org.studioapp.cinemy.data.model.MovieDetailsData
import org.studioapp.cinemy.data.model.MovieDetailsResponse
import org.studioapp.cinemy.data.model.MovieListData
import org.studioapp.cinemy.data.model.MovieListResponse
import org.studioapp.cinemy.data.model.Pagination
import org.studioapp.cinemy.data.model.Result
import org.studioapp.cinemy.data.model.StringConstants
import org.studioapp.cinemy.data.model.UiConfiguration
import kotlinx.coroutines.delay
import org.json.JSONObject

/**
 * Dummy implementation of MovieRepository for testing and development purposes.
 * Returns mock data from assets without making actual network calls.
 */
class DummyMovieRepository(
    private val context: Context
) : MovieRepository {

    private val assetDataLoader = AssetDataLoader(context)

    override suspend fun getPopularMovies(page: Int): Result<MovieListResponse> {
        // Simulate network delay
        delay(StringConstants.NETWORK_DELAY_BASE_MS)

        // Load mock movies from assets
        val movieDtos = assetDataLoader.loadMockMovies()
        val movies = movieDtos.map { MovieMapper.mapMovieDtoToMovie(it) }

        // Create pagination
        val pagination = Pagination(
            page = page,
            totalPages = StringConstants.PAGINATION_TOP_RATED_TOTAL_PAGES,
            totalResults = StringConstants.PAGINATION_TOP_RATED_TOTAL_RESULTS,
            hasNext = page < StringConstants.PAGINATION_TOP_RATED_TOTAL_PAGES,
            hasPrevious = page > StringConstants.PAGINATION_FIRST_PAGE
        )

        // Create data wrapper
        val movieListData = MovieListData(
            movies = movies,
            pagination = pagination
        )

        // Create UI config and meta (using defaults for now)
        val uiConfig = createDefaultUiConfig()
        val meta = createDefaultMeta()

        val response = MovieListResponse(
            success = true,
            data = movieListData,
            uiConfig = uiConfig,
            meta = meta
        )

        return Result.Success(response)
    }

    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsResponse> {
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
                val sentimentReviewsJson = dataJson?.optJSONObject(StringConstants.FIELD_SENTIMENT_REVIEWS)
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
            val inputStream = context.assets.open(fileName)
            inputStream.bufferedReader().use { it.readText() }
        }.getOrElse { null }
    }

    /**
     * Parses movie details from JSON
     */
    private fun parseMovieDetailsFromJson(jsonObject: JSONObject): MovieDetails {
        val genresJson = jsonObject.optJSONArray("genres")
        val genres = if (genresJson != null) {
            (StringConstants.DEFAULT_INT_VALUE until genresJson.length()).map { i ->
                val genreJson = genresJson.getJSONObject(i)
                org.studioapp.cinemy.data.model.Genre(
                    id = genreJson.optInt(
                        StringConstants.SERIALIZED_ID,
                        StringConstants.DEFAULT_INT_VALUE
                    ),
                    name = genreJson.optString(
                        StringConstants.SERIALIZED_NAME,
                        StringConstants.EMPTY_STRING
                    )
                )
            }
        } else {
            emptyList()
        }

        val productionCompaniesJson = jsonObject.optJSONArray("productionCompanies")
        val productionCompanies = if (productionCompaniesJson != null) {
            (StringConstants.DEFAULT_INT_VALUE until productionCompaniesJson.length()).map { i ->
                val companyJson = productionCompaniesJson.getJSONObject(i)
                org.studioapp.cinemy.data.model.ProductionCompany(
                    id = companyJson.optInt(
                        StringConstants.SERIALIZED_ID,
                        StringConstants.DEFAULT_INT_VALUE
                    ),
                    logoPath = companyJson.optString(StringConstants.SERIALIZED_LOGO_PATH),
                    name = companyJson.optString(
                        StringConstants.SERIALIZED_NAME,
                        StringConstants.EMPTY_STRING
                    ),
                    originCountry = companyJson.optString(
                        StringConstants.SERIALIZED_ORIGIN_COUNTRY,
                        StringConstants.EMPTY_STRING
                    )
                )
            }
        } else {
            emptyList()
        }

        return MovieDetails(
            id = jsonObject.optInt(
                StringConstants.SERIALIZED_ID,
                StringConstants.DEFAULT_INT_VALUE
            ),
            title = jsonObject.optString(
                StringConstants.SERIALIZED_TITLE,
                StringConstants.EMPTY_STRING
            ),
            description = jsonObject.optString(
                StringConstants.SERIALIZED_OVERVIEW,
                StringConstants.EMPTY_STRING
            ),
            posterPath = jsonObject.optString(StringConstants.SERIALIZED_POSTER_PATH),
            backdropPath = jsonObject.optString(StringConstants.SERIALIZED_BACKDROP_PATH),
            rating = jsonObject.optDouble(
                StringConstants.SERIALIZED_VOTE_AVERAGE,
                StringConstants.DEFAULT_DOUBLE_VALUE
            ),
            voteCount = jsonObject.optInt(
                StringConstants.SERIALIZED_VOTE_COUNT,
                StringConstants.DEFAULT_INT_VALUE
            ),
            releaseDate = jsonObject.optString(
                StringConstants.SERIALIZED_RELEASE_DATE,
                StringConstants.EMPTY_STRING
            ),
            runtime = jsonObject.optInt(
                StringConstants.SERIALIZED_RUNTIME,
                StringConstants.DEFAULT_INT_VALUE
            ),
            genres = genres,
            productionCompanies = productionCompanies,
            budget = jsonObject.optLong(
                StringConstants.SERIALIZED_BUDGET,
                StringConstants.DEFAULT_LONG_VALUE
            ),
            revenue = jsonObject.optLong(
                StringConstants.SERIALIZED_REVENUE,
                StringConstants.DEFAULT_LONG_VALUE
            ),
            status = jsonObject.optString(
                StringConstants.SERIALIZED_STATUS,
                StringConstants.EMPTY_STRING
            )
        )
    }

    /**
     * Creates default movie details as fallback
     */
    private fun createDefaultMovieDetails(): MovieDetails {
        return MovieDetails(
            id = StringConstants.DEFAULT_INT_VALUE,
            title = StringConstants.EMPTY_STRING,
            description = StringConstants.EMPTY_STRING,
            posterPath = null,
            backdropPath = null,
            rating = StringConstants.DEFAULT_DOUBLE_VALUE,
            voteCount = StringConstants.DEFAULT_INT_VALUE,
            releaseDate = StringConstants.EMPTY_STRING,
            runtime = StringConstants.DEFAULT_INT_VALUE,
            genres = emptyList(),
            productionCompanies = emptyList(),
            budget = StringConstants.DEFAULT_LONG_VALUE,
            revenue = StringConstants.DEFAULT_LONG_VALUE,
            status = StringConstants.EMPTY_STRING
        )
    }
    
    /**
     * Parses sentiment reviews from JSON
     */
    private fun parseSentimentReviewsFromJson(jsonObject: JSONObject): org.studioapp.cinemy.data.model.SentimentReviews {
        val positiveJson = jsonObject.optJSONArray(StringConstants.FIELD_POSITIVE)
        val positive = if (positiveJson != null) {
            (0 until positiveJson.length()).map { i ->
                positiveJson.getString(i)
            }
        } else {
            emptyList()
        }
        
        val negativeJson = jsonObject.optJSONArray(StringConstants.FIELD_NEGATIVE)
        val negative = if (negativeJson != null) {
            (0 until negativeJson.length()).map { i ->
                negativeJson.getString(i)
            }
        } else {
            emptyList()
        }
        
        return org.studioapp.cinemy.data.model.SentimentReviews(
            positive = positive,
            negative = negative
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
                errorMessage = "An error occurred",
                noMoviesFound = "No movies found",
                retryButton = "Retry",
                backButton = "Back",
                playButton = "Play"
            ),
            buttons = org.studioapp.cinemy.data.model.ButtonConfiguration(
                primaryButtonColor = Color(0xFF6200EE),
                secondaryButtonColor = Color(0xFF03DAC6),
                buttonTextColor = Color(0xFFFFFFFF),
                buttonCornerRadius = StringConstants.BUTTON_CORNER_RADIUS
            )
        )
    }

    /**
     * Creates default meta information
     */
    private fun createDefaultMeta(): Meta {
        return Meta(
            timestamp = System.currentTimeMillis().toString(),
            method = StringConstants.META_METHOD_UNKNOWN,
            resultsCount = StringConstants.META_RESULTS_COUNT_ZERO,
            aiGenerated = StringConstants.DEFAULT_BOOLEAN_VALUE,
            geminiColors = org.studioapp.cinemy.data.model.GeminiColors(
                primary = "#6200EE",
                secondary = "#03DAC6",
                accent = "#FF6B6B"
            ),
            version = "1.0.0"
        )
    }
}
