package org.studioapp.cinemy.data.mcp

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import org.studioapp.cinemy.data.model.StringConstants
import org.studioapp.cinemy.data.remote.dto.ButtonConfigurationDto
import org.studioapp.cinemy.data.remote.dto.ColorSchemeDto
import org.studioapp.cinemy.data.remote.dto.GeminiColorsDto
import org.studioapp.cinemy.data.remote.dto.MetaDto
import org.studioapp.cinemy.data.remote.dto.MovieDto
import org.studioapp.cinemy.data.remote.dto.TextConfigurationDto
import org.studioapp.cinemy.data.remote.dto.UiConfigurationDto

/**
 * Loads UI configuration and meta data from asset files
 * instead of using hardcoded mock generation methods.
 */
class AssetDataLoader(private val context: Context) {

    /**
     * Loads UI configuration from asset files
     */
    fun loadUiConfig(): UiConfigurationDto {
        return runCatching {
            val jsonString = loadJsonFromAssets(StringConstants.ASSET_MOCK_MOVIES)
            if (jsonString != null) {
                val jsonObject = JSONObject(jsonString)
                val uiConfigJson = jsonObject.optJSONObject(StringConstants.FIELD_UI_CONFIG)


                if (uiConfigJson != null) {
                    parseUiConfigFromJson(uiConfigJson)
                } else {
                    createDefaultUiConfig()
                }
            } else {
                createDefaultUiConfig()
            }
        }.getOrElse { e ->
            createDefaultUiConfig()
        }
    }

    /**
     * Loads meta data from asset files
     */
    fun loadMetaData(method: String, resultsCount: Int = 0, movieId: Int? = null): MetaDto {
        return runCatching {
            val jsonString = loadJsonFromAssets(StringConstants.ASSET_MOCK_MOVIES)
            if (jsonString != null) {
                val jsonObject = JSONObject(jsonString)
                val metaJson = jsonObject.optJSONObject(StringConstants.FIELD_META)
                if (metaJson != null) {
                    parseMetaFromJson(metaJson, method, resultsCount, movieId)
                } else {
                    createDefaultMeta(method, resultsCount, movieId)
                }
            } else {
                createDefaultMeta(method, resultsCount, movieId)
            }
        }.getOrElse { e ->
            createDefaultMeta(method, resultsCount, movieId)
        }
    }

    /**
     * Loads mock movies from asset files
     */
    fun loadMockMovies(): List<MovieDto> {
        return runCatching {
            val jsonString = loadJsonFromAssets(StringConstants.ASSET_MOCK_MOVIES)
            if (jsonString != null) {
                val jsonObject = JSONObject(jsonString)
                val dataJson = jsonObject.optJSONObject(StringConstants.FIELD_DATA)
                val moviesJson = dataJson?.optJSONArray(StringConstants.FIELD_MOVIES)
                if (moviesJson != null) {
                    parseMoviesFromJson(moviesJson)
                } else {
                    emptyList()
                }
            } else {
                emptyList()
            }
        }.getOrElse { e ->
            emptyList()
        }
    }

    /**
     * Loads JSON content from assets
     */
    private fun loadJsonFromAssets(fileName: String): String? {
        return runCatching {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        }.getOrElse { e ->
            null
        }
    }

    /**
     * Parses UI configuration from JSON
     */
    private fun parseUiConfigFromJson(uiConfigJson: JSONObject): UiConfigurationDto {
        val colorsJson = uiConfigJson.optJSONObject(StringConstants.FIELD_COLORS)
        val textsJson = uiConfigJson.optJSONObject(StringConstants.FIELD_TEXTS)
        val buttonsJson = uiConfigJson.optJSONObject(StringConstants.FIELD_BUTTONS)


        return UiConfigurationDto(
            colors = if (colorsJson != null) {
                ColorSchemeDto(
                    primary = colorsJson.optString(
                        StringConstants.FIELD_PRIMARY,
                        StringConstants.COLOR_PRIMARY
                    ),
                    secondary = colorsJson.optString(
                        StringConstants.FIELD_SECONDARY,
                        StringConstants.COLOR_SECONDARY
                    ),
                    background = colorsJson.optString(
                        StringConstants.FIELD_BACKGROUND,
                        StringConstants.COLOR_BACKGROUND
                    ),
                    surface = colorsJson.optString(
                        StringConstants.FIELD_SURFACE,
                        StringConstants.COLOR_SURFACE
                    ),
                    onPrimary = colorsJson.optString(
                        StringConstants.FIELD_ON_PRIMARY,
                        StringConstants.COLOR_ON_PRIMARY
                    ),
                    onSecondary = colorsJson.optString(
                        StringConstants.FIELD_ON_SECONDARY,
                        StringConstants.COLOR_ON_SECONDARY
                    ),
                    onBackground = colorsJson.optString(
                        StringConstants.FIELD_ON_BACKGROUND,
                        StringConstants.COLOR_ON_BACKGROUND
                    ),
                    onSurface = colorsJson.optString(
                        StringConstants.FIELD_ON_SURFACE,
                        StringConstants.COLOR_ON_SURFACE
                    ),
                    moviePosterColors = parseMoviePosterColors(
                        colorsJson.optJSONArray(
                            StringConstants.FIELD_MOVIE_POSTER_COLORS
                        )
                    )
                )
            } else {
                createDefaultColorScheme()
            },
            texts = if (textsJson != null) {
                TextConfigurationDto(
                    appTitle = textsJson.optString(
                        StringConstants.FIELD_APP_TITLE,
                        StringConstants.MOVIES_TITLE
                    ),
                    loadingText = textsJson.optString(
                        StringConstants.FIELD_LOADING_TEXT,
                        StringConstants.LOADING_MOVIES_TEXT
                    ),
                    errorMessage = textsJson.optString(
                        StringConstants.FIELD_ERROR_MESSAGE,
                        StringConstants.ERROR_UNKNOWN
                    ),
                    noMoviesFound = textsJson.optString(
                        StringConstants.FIELD_NO_MOVIES_FOUND,
                        StringConstants.NO_MOVIES_FOUND
                    ),
                    retryButton = textsJson.optString(
                        StringConstants.FIELD_RETRY_BUTTON,
                        StringConstants.RETRY_BUTTON
                    ),
                    backButton = textsJson.optString(
                        StringConstants.FIELD_BACK_BUTTON,
                        StringConstants.BACK_BUTTON
                    ),
                    playButton = textsJson.optString(
                        StringConstants.FIELD_PLAY_BUTTON,
                        StringConstants.PLAY_BUTTON
                    )
                )
            } else {
                createDefaultTextConfiguration()
            },
            buttons = if (buttonsJson != null) {
                ButtonConfigurationDto(
                    primaryButtonColor = buttonsJson.optString(
                        StringConstants.FIELD_PRIMARY_BUTTON_COLOR,
                        StringConstants.COLOR_PRIMARY
                    ),
                    secondaryButtonColor = buttonsJson.optString(
                        StringConstants.FIELD_SECONDARY_BUTTON_COLOR,
                        StringConstants.COLOR_SECONDARY
                    ),
                    buttonTextColor = buttonsJson.optString(
                        StringConstants.FIELD_BUTTON_TEXT_COLOR,
                        StringConstants.COLOR_BUTTON_TEXT
                    ),
                    buttonCornerRadius = buttonsJson.optInt(
                        StringConstants.FIELD_BUTTON_CORNER_RADIUS,
                        8
                    )
                )
            } else {
                createDefaultButtonConfiguration()
            }
        )
    }

    /**
     * Parses meta data from JSON
     */
    private fun parseMetaFromJson(
        metaJson: JSONObject,
        method: String,
        resultsCount: Int,
        movieId: Int?
    ): MetaDto {
        val geminiColorsJson = metaJson.optJSONObject(StringConstants.FIELD_GEMINI_COLORS)

        return MetaDto(
            timestamp = metaJson.optString(
                StringConstants.FIELD_TIMESTAMP,
                System.currentTimeMillis().toString()
            ),
            method = method,
            searchQuery = metaJson.optString(StringConstants.FIELD_SEARCH_QUERY, null),
            movieId = movieId,
            resultsCount = resultsCount,
            aiGenerated = metaJson.optBoolean(StringConstants.FIELD_AI_GENERATED, true),
            geminiColors = if (geminiColorsJson != null) {
                GeminiColorsDto(
                    primary = geminiColorsJson.optString(
                        StringConstants.FIELD_PRIMARY,
                        StringConstants.COLOR_PRIMARY
                    ),
                    secondary = geminiColorsJson.optString(
                        StringConstants.FIELD_SECONDARY,
                        StringConstants.COLOR_SECONDARY
                    ),
                    accent = geminiColorsJson.optString(
                        StringConstants.FIELD_ACCENT,
                        StringConstants.COLOR_ACCENT
                    )
                )
            } else {
                createDefaultGeminiColors()
            },
            avgRating = metaJson.optDouble(StringConstants.FIELD_AVG_RATING, 0.0).takeIf { it > 0 },
            movieRating = metaJson.optDouble(StringConstants.FIELD_MOVIE_RATING, 0.0)
                .takeIf { it > 0 },
            version = metaJson.optString(
                StringConstants.FIELD_VERSION,
                StringConstants.VERSION_2_0_0
            )
        )
    }

    /**
     * Parses movies from JSON array
     */
    private fun parseMoviesFromJson(moviesJson: JSONArray): List<MovieDto> {
        val movies = mutableListOf<MovieDto>()
        for (i in 0 until moviesJson.length()) {
            val movieJson = moviesJson.getJSONObject(i)
            movies.add(
                MovieDto(
                    id = movieJson.optInt(StringConstants.FIELD_ID, 0),
                    title = movieJson.optString(
                        StringConstants.FIELD_TITLE,
                        StringConstants.UNKNOWN_MOVIE_TITLE
                    ),
                    description = movieJson.optString(
                        StringConstants.FIELD_DESCRIPTION,
                        StringConstants.NO_DESCRIPTION_AVAILABLE
                    ),
                    posterPath = movieJson.optString(StringConstants.FIELD_POSTER_PATH, null),
                    backdropPath = movieJson.optString(StringConstants.FIELD_BACKDROP_PATH, null),
                    rating = movieJson.optDouble(StringConstants.FIELD_RATING, 0.0),
                    voteCount = movieJson.optInt(StringConstants.FIELD_VOTE_COUNT, 0),
                    releaseDate = movieJson.optString(StringConstants.FIELD_RELEASE_DATE, null),
                    genreIds = parseGenreIds(movieJson.optJSONArray(StringConstants.FIELD_GENRE_IDS)),
                    popularity = movieJson.optDouble(StringConstants.FIELD_POPULARITY, 0.0),
                    adult = movieJson.optBoolean(StringConstants.FIELD_ADULT, false)
                )
            )
        }
        return movies
    }

    /**
     * Parses movie poster colors from JSON array
     */
    private fun parseMoviePosterColors(colorsJson: JSONArray?): List<String> {
        if (colorsJson == null) return emptyList()

        val colors = mutableListOf<String>()
        for (i in 0 until colorsJson.length()) {
            val colorJson = colorsJson.getJSONObject(i)
            colors.add(
                colorJson.optString(
                    StringConstants.FIELD_VALUE,
                    StringConstants.COLOR_PRIMARY
                )
            )
        }
        return colors
    }

    /**
     * Parses genre IDs from JSON array
     */
    private fun parseGenreIds(genreIdsJson: JSONArray?): List<Int> {
        if (genreIdsJson == null) return emptyList()

        val genreIds = mutableListOf<Int>()
        for (i in 0 until genreIdsJson.length()) {
            genreIds.add(genreIdsJson.getInt(i))
        }
        return genreIds
    }

    // Default creation methods
    private fun createDefaultUiConfig(): UiConfigurationDto {
        return UiConfigurationDto(
            colors = createDefaultColorScheme(),
            texts = createDefaultTextConfiguration(),
            buttons = createDefaultButtonConfiguration()
        )
    }

    private fun createDefaultColorScheme(): ColorSchemeDto {
        return ColorSchemeDto(
            primary = StringConstants.COLOR_PRIMARY,
            secondary = StringConstants.COLOR_SECONDARY,
            background = StringConstants.COLOR_BACKGROUND,
            surface = StringConstants.COLOR_SURFACE,
            onPrimary = StringConstants.COLOR_ON_PRIMARY,
            onSecondary = StringConstants.COLOR_ON_SECONDARY,
            onBackground = StringConstants.COLOR_ON_BACKGROUND,
            onSurface = StringConstants.COLOR_ON_SURFACE,
            moviePosterColors = emptyList()
        )
    }

    private fun createDefaultTextConfiguration(): TextConfigurationDto {
        return TextConfigurationDto(
            appTitle = StringConstants.MOVIES_TITLE,
            loadingText = StringConstants.LOADING_MOVIES_TEXT,
            errorMessage = StringConstants.ERROR_UNKNOWN,
            noMoviesFound = StringConstants.NO_MOVIES_FOUND,
            retryButton = StringConstants.RETRY_BUTTON,
            backButton = StringConstants.BACK_BUTTON,
            playButton = StringConstants.PLAY_BUTTON
        )
    }

    private fun createDefaultButtonConfiguration(): ButtonConfigurationDto {
        return ButtonConfigurationDto(
            primaryButtonColor = StringConstants.COLOR_PRIMARY,
            secondaryButtonColor = StringConstants.COLOR_SECONDARY,
            buttonTextColor = StringConstants.COLOR_BUTTON_TEXT,
            buttonCornerRadius = StringConstants.BUTTON_CORNER_RADIUS
        )
    }

    private fun createDefaultMeta(method: String, resultsCount: Int, movieId: Int?): MetaDto {
        return MetaDto(
            timestamp = System.currentTimeMillis().toString(),
            method = method,
            searchQuery = null,
            movieId = movieId,
            resultsCount = resultsCount,
            aiGenerated = true,
            geminiColors = createDefaultGeminiColors(),
            avgRating = null,
            movieRating = null,
            version = StringConstants.VERSION_2_0_0
        )
    }

    private fun createDefaultGeminiColors(): GeminiColorsDto {
        return GeminiColorsDto(
            primary = StringConstants.COLOR_PRIMARY,
            secondary = StringConstants.COLOR_SECONDARY,
            accent = StringConstants.COLOR_ACCENT
        )
    }
}
