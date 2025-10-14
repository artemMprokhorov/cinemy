package org.studioapp.cinemy.data.mcp

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import org.studioapp.cinemy.data.model.StringConstants.ASSET_MOCK_MOVIES
import org.studioapp.cinemy.data.model.StringConstants.FIELD_UI_CONFIG
import org.studioapp.cinemy.data.model.StringConstants.FIELD_META
import org.studioapp.cinemy.data.model.StringConstants.FIELD_DATA
import org.studioapp.cinemy.data.model.StringConstants.FIELD_COLORS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_TEXTS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_BUTTONS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_MOVIE_POSTER_COLORS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_GEMINI_COLORS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_PRIMARY
import org.studioapp.cinemy.data.model.StringConstants.FIELD_SECONDARY
import org.studioapp.cinemy.data.model.StringConstants.FIELD_BACKGROUND
import org.studioapp.cinemy.data.model.StringConstants.FIELD_SURFACE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ON_PRIMARY
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ON_SECONDARY
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ON_BACKGROUND
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ON_SURFACE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_PRIMARY_BUTTON_COLOR
import org.studioapp.cinemy.data.model.StringConstants.FIELD_SECONDARY_BUTTON_COLOR
import org.studioapp.cinemy.data.model.StringConstants.FIELD_BUTTON_TEXT_COLOR
import org.studioapp.cinemy.data.model.StringConstants.FIELD_BUTTON_CORNER_RADIUS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_APP_TITLE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_LOADING_TEXT
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ERROR_MESSAGE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_NO_MOVIES_FOUND
import org.studioapp.cinemy.data.model.StringConstants.FIELD_RETRY_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.FIELD_BACK_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.FIELD_PLAY_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.FIELD_SEARCH_QUERY
import org.studioapp.cinemy.data.model.StringConstants.FIELD_AI_GENERATED
import org.studioapp.cinemy.data.model.StringConstants.FIELD_AVG_RATING
import org.studioapp.cinemy.data.model.StringConstants.FIELD_MOVIE_RATING
import org.studioapp.cinemy.data.model.StringConstants.FIELD_VERSION
import org.studioapp.cinemy.data.model.StringConstants.FIELD_TIMESTAMP
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ID
import org.studioapp.cinemy.data.model.StringConstants.FIELD_TITLE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_DESCRIPTION
import org.studioapp.cinemy.data.model.StringConstants.FIELD_POSTER_PATH
import org.studioapp.cinemy.data.model.StringConstants.FIELD_BACKDROP_PATH
import org.studioapp.cinemy.data.model.StringConstants.FIELD_RATING
import org.studioapp.cinemy.data.model.StringConstants.FIELD_VOTE_COUNT
import org.studioapp.cinemy.data.model.StringConstants.FIELD_RELEASE_DATE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_RUNTIME
import org.studioapp.cinemy.data.model.StringConstants.FIELD_GENRES
import org.studioapp.cinemy.data.model.StringConstants.FIELD_PRODUCTION_COMPANIES
import org.studioapp.cinemy.data.model.StringConstants.FIELD_BUDGET
import org.studioapp.cinemy.data.model.StringConstants.FIELD_REVENUE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_STATUS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_NAME
import org.studioapp.cinemy.data.model.StringConstants.FIELD_LOGO_PATH
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ORIGIN_COUNTRY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RELEASE_DATE
import org.studioapp.cinemy.data.model.StringConstants.COLOR_PRIMARY
import org.studioapp.cinemy.data.model.StringConstants.COLOR_SECONDARY
import org.studioapp.cinemy.data.model.StringConstants.COLOR_BACKGROUND
import org.studioapp.cinemy.data.model.StringConstants.COLOR_SURFACE
import org.studioapp.cinemy.data.model.StringConstants.COLOR_ON_PRIMARY
import org.studioapp.cinemy.data.model.StringConstants.COLOR_ON_SECONDARY
import org.studioapp.cinemy.data.model.StringConstants.COLOR_ON_BACKGROUND
import org.studioapp.cinemy.data.model.StringConstants.COLOR_ON_SURFACE
import org.studioapp.cinemy.data.model.StringConstants.COLOR_ACCENT
import org.studioapp.cinemy.data.model.StringConstants.COLOR_BUTTON_TEXT
import org.studioapp.cinemy.data.model.StringConstants.BUTTON_CORNER_RADIUS
import org.studioapp.cinemy.data.model.StringConstants.MOVIES_TITLE
import org.studioapp.cinemy.data.model.StringConstants.LOADING_MOVIES_TEXT
import org.studioapp.cinemy.data.model.StringConstants.ERROR_UNKNOWN
import org.studioapp.cinemy.data.model.StringConstants.NO_MOVIES_FOUND
import org.studioapp.cinemy.data.model.StringConstants.RETRY_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.BACK_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.PLAY_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.VERSION_2_0_0
import org.studioapp.cinemy.data.model.StringConstants.UNKNOWN_MOVIE_TITLE
import org.studioapp.cinemy.data.model.StringConstants.NO_DESCRIPTION_AVAILABLE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RESULTS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_MOVIES
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_OVERVIEW
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_POSTER_PATH
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_BACKDROP_PATH
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_VOTE_AVERAGE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_VOTE_COUNT
import org.studioapp.cinemy.data.model.StringConstants.FIELD_GENRE_IDS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_GENRE_IDS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_POPULARITY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_POPULARITY
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ADULT
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ADULT
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ORIGINAL_LANGUAGE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ORIGINAL_LANGUAGE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ORIGINAL_TITLE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ORIGINAL_TITLE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_VIDEO
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_VIDEO
import org.studioapp.cinemy.data.model.StringConstants.MOVIE_COLORS_METADATA
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ACCENT
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_ACCENT_COLOR
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_PRIMARY_COLOR
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_SECONDARY_COLOR
import org.studioapp.cinemy.data.model.StringConstants.MOVIE_COLORS_CATEGORY
import org.studioapp.cinemy.data.model.StringConstants.LANGUAGE_EN
import org.studioapp.cinemy.data.model.StringConstants.CATEGORY_MEDIUM
import org.studioapp.cinemy.data.model.StringConstants.MOVIE_COLORS_MODEL_USED
import org.studioapp.cinemy.data.model.StringConstants.FIELD_VALUE
import org.studioapp.cinemy.data.remote.dto.ButtonConfigurationDto
import org.studioapp.cinemy.data.remote.dto.ColorMetadataDto
import org.studioapp.cinemy.data.remote.dto.ColorSchemeDto
import org.studioapp.cinemy.data.mapper.MovieMapper
import org.studioapp.cinemy.data.remote.dto.GeminiColorsDto
import org.studioapp.cinemy.data.remote.dto.MetaDto
import org.studioapp.cinemy.data.remote.dto.MovieColorsDto
import org.studioapp.cinemy.data.remote.dto.MovieDto
import org.studioapp.cinemy.data.remote.dto.TextConfigurationDto
import org.studioapp.cinemy.data.remote.dto.UiConfigurationDto
import org.studioapp.cinemy.data.util.AssetUtils.loadJsonFromAssets

/**
 * Loads UI configuration and meta data from asset files
 */
class AssetDataLoader(private val context: Context) {

    /**
     * Loads UI configuration from asset files
     * @return UiConfigurationDto with theme configuration
     */
    fun loadUiConfig(): UiConfigurationDto {
        return runCatching {
            val jsonString =
                loadJsonFromAssets(context, ASSET_MOCK_MOVIES)
            if (jsonString != null) {
                val jsonObject = JSONObject(jsonString)
                val uiConfigJson = jsonObject.optJSONObject(FIELD_UI_CONFIG)


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
     * @param method API method name
     * @param resultsCount Number of results returned
     * @param movieId Movie ID (optional)
     * @return MetaDto with metadata information
     */
    fun loadMetaData(method: String, resultsCount: Int = 0, movieId: Int? = null): MetaDto {
        return runCatching {
            val jsonString =
                loadJsonFromAssets(context, ASSET_MOCK_MOVIES)
            if (jsonString != null) {
                val jsonObject = JSONObject(jsonString)
                val metaJson = jsonObject.optJSONObject(FIELD_META)
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
     * @return List of MovieDto objects from assets
     */
    fun loadMockMovies(): List<MovieDto> {
        return runCatching {
            val jsonString =
                loadJsonFromAssets(context, ASSET_MOCK_MOVIES)
            if (jsonString != null) {
                // Try to parse as array first (new contract structure)
                try {
                    val jsonArray = JSONArray(jsonString)
                    if (jsonArray.length() > 0) {
                        val firstObject = jsonArray.getJSONObject(0)
                        val moviesJson =
                            firstObject.optJSONArray(SERIALIZED_RESULTS)
                        if (moviesJson != null) {
                            return parseMoviesFromJson(moviesJson)
                        }
                    }
                } catch (e: Exception) {
                    // Fall back to old structure
                }

                // Fall back to old structure
                val jsonObject = JSONObject(jsonString)
                val dataJson = jsonObject.optJSONObject(FIELD_DATA)
                val moviesJson = dataJson?.optJSONArray(FIELD_MOVIES)
                    ?: dataJson?.optJSONArray(SERIALIZED_RESULTS)
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
     * Parses UI configuration from JSON object
     * @param uiConfigJson JSON object containing UI configuration
     * @return UiConfigurationDto with parsed configuration
     */
    private fun parseUiConfigFromJson(uiConfigJson: JSONObject): UiConfigurationDto {
        val colorsJson = uiConfigJson.optJSONObject(FIELD_COLORS)
        val textsJson = uiConfigJson.optJSONObject(FIELD_TEXTS)
        val buttonsJson = uiConfigJson.optJSONObject(FIELD_BUTTONS)


        return MovieMapper.mapJsonToUiConfigurationDto(colorsJson, textsJson, buttonsJson)
    }

    /**
     * Parses meta data from JSON object
     * @param metaJson JSON object containing metadata
     * @param method API method name
     * @param resultsCount Number of results returned
     * @param movieId Movie ID (optional)
     * @return MetaDto with parsed metadata
     */
    private fun parseMetaFromJson(
        metaJson: JSONObject,
        method: String,
        resultsCount: Int,
        movieId: Int?
    ): MetaDto {
        val geminiColorsJson = metaJson.optJSONObject(FIELD_GEMINI_COLORS)

        return MetaDto(
            timestamp = metaJson.optString(
                FIELD_TIMESTAMP,
                System.currentTimeMillis().toString()
            ),
            method = method,
            searchQuery = metaJson.optString(FIELD_SEARCH_QUERY, null) ?: "",
            movieId = movieId,
            resultsCount = resultsCount,
            aiGenerated = metaJson.optBoolean(FIELD_AI_GENERATED, true),
            geminiColors = if (geminiColorsJson != null) {
                GeminiColorsDto(
                    primary = geminiColorsJson.optString(
                        FIELD_PRIMARY,
                        COLOR_PRIMARY
                    ),
                    secondary = geminiColorsJson.optString(
                        FIELD_SECONDARY,
                        COLOR_SECONDARY
                    ),
                    accent = geminiColorsJson.optString(
                        FIELD_ACCENT,
                        COLOR_ACCENT
                    )
                )
            } else {
                createDefaultGeminiColors()
            },
            avgRating = metaJson.optDouble(FIELD_AVG_RATING, 0.0).takeIf { it > 0 },
            movieRating = metaJson.optDouble(FIELD_MOVIE_RATING, 0.0)
                .takeIf { it > 0 },
            version = metaJson.optString(
                FIELD_VERSION,
                VERSION_2_0_0
            )
        )
    }

    /**
     * Parses movies from JSON array
     * @param moviesJson JSON array containing movie data
     * @return List of MovieDto objects
     */
    private fun parseMoviesFromJson(moviesJson: JSONArray): List<MovieDto> {
        return MovieMapper.mapJsonArrayToMovieDtoList(moviesJson)
    }


    /**
     * Parses movie poster colors from JSON array
     * @param colorsJson JSON array containing color data
     * @return List of color strings
     */
    private fun parseMoviePosterColors(colorsJson: JSONArray?): List<String> {
        if (colorsJson == null) return emptyList()

        val colors = mutableListOf<String>()
        for (i in 0 until colorsJson.length()) {
            val colorJson = colorsJson.getJSONObject(i)
            colors.add(
                colorJson.optString(
                    FIELD_VALUE,
                    COLOR_PRIMARY
                )
            )
        }
        return colors
    }

    /**
     * Parses genre IDs from JSON array
     * @param genreIdsJson JSON array containing genre IDs
     * @return List of genre ID integers
     */
    private fun parseGenreIds(genreIdsJson: JSONArray?): List<Int> {
        if (genreIdsJson == null) return emptyList()

        val genreIds = mutableListOf<Int>()
        for (i in 0 until genreIdsJson.length()) {
            genreIds.add(genreIdsJson.getInt(i))
        }
        return genreIds
    }

    /**
     * Creates default UI configuration when asset loading fails
     * @return UiConfigurationDto with default values
     */
    private fun createDefaultUiConfig(): UiConfigurationDto {
        return UiConfigurationDto(
            colors = createDefaultColorScheme(),
            texts = createDefaultTextConfiguration(),
            buttons = createDefaultButtonConfiguration()
        )
    }

    /**
     * Creates default color scheme when asset loading fails
     * @return ColorSchemeDto with default color values
     */
    private fun createDefaultColorScheme(): ColorSchemeDto {
        return ColorSchemeDto(
            primary = COLOR_PRIMARY,
            secondary = COLOR_SECONDARY,
            background = COLOR_BACKGROUND,
            surface = COLOR_SURFACE,
            onPrimary = COLOR_ON_PRIMARY,
            onSecondary = COLOR_ON_SECONDARY,
            onBackground = COLOR_ON_BACKGROUND,
            onSurface = COLOR_ON_SURFACE,
            moviePosterColors = emptyList()
        )
    }

    /**
     * Creates default text configuration when asset loading fails
     * @return TextConfigurationDto with default text values
     */
    private fun createDefaultTextConfiguration(): TextConfigurationDto {
        return TextConfigurationDto(
            appTitle = MOVIES_TITLE,
            loadingText = LOADING_MOVIES_TEXT,
            errorMessage = ERROR_UNKNOWN,
            noMoviesFound = NO_MOVIES_FOUND,
            retryButton = RETRY_BUTTON,
            backButton = BACK_BUTTON,
            playButton = PLAY_BUTTON
        )
    }

    /**
     * Creates default button configuration when asset loading fails
     * @return ButtonConfigurationDto with default button values
     */
    private fun createDefaultButtonConfiguration(): ButtonConfigurationDto {
        return ButtonConfigurationDto(
            primaryButtonColor = COLOR_PRIMARY,
            secondaryButtonColor = COLOR_SECONDARY,
            buttonTextColor = COLOR_BUTTON_TEXT,
            buttonCornerRadius = BUTTON_CORNER_RADIUS
        )
    }

    /**
     * Creates default metadata when asset loading fails
     * @param method API method name
     * @param resultsCount Number of results returned
     * @param movieId Movie ID (optional)
     * @return MetaDto with default metadata values
     */
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
            version = VERSION_2_0_0
        )
    }

    /**
     * Creates default Gemini colors when asset loading fails
     * @return GeminiColorsDto with default color values
     */
    private fun createDefaultGeminiColors(): GeminiColorsDto {
        return GeminiColorsDto(
            primary = COLOR_PRIMARY,
            secondary = COLOR_SECONDARY,
            accent = COLOR_ACCENT
        )
    }
}
