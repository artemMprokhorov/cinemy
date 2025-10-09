package org.studioapp.cinemy.data.mcp

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import org.studioapp.cinemy.data.model.StringConstants
import org.studioapp.cinemy.data.remote.dto.ButtonConfigurationDto
import org.studioapp.cinemy.data.remote.dto.ColorMetadataDto
import org.studioapp.cinemy.data.remote.dto.ColorSchemeDto
import org.studioapp.cinemy.data.remote.dto.GeminiColorsDto
import org.studioapp.cinemy.data.remote.dto.MetaDto
import org.studioapp.cinemy.data.remote.dto.MovieColorsDto
import org.studioapp.cinemy.data.remote.dto.MovieDto
import org.studioapp.cinemy.data.remote.dto.TextConfigurationDto
import org.studioapp.cinemy.data.remote.dto.UiConfigurationDto
import org.studioapp.cinemy.data.util.AssetUtils

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
                AssetUtils.loadJsonFromAssets(context, StringConstants.ASSET_MOCK_MOVIES)
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
     * @param method API method name
     * @param resultsCount Number of results returned
     * @param movieId Movie ID (optional)
     * @return MetaDto with metadata information
     */
    fun loadMetaData(method: String, resultsCount: Int = 0, movieId: Int? = null): MetaDto {
        return runCatching {
            val jsonString =
                AssetUtils.loadJsonFromAssets(context, StringConstants.ASSET_MOCK_MOVIES)
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
     * @return List of MovieDto objects from assets
     */
    fun loadMockMovies(): List<MovieDto> {
        return runCatching {
            val jsonString =
                AssetUtils.loadJsonFromAssets(context, StringConstants.ASSET_MOCK_MOVIES)
            if (jsonString != null) {
                // Try to parse as array first (new contract structure)
                try {
                    val jsonArray = JSONArray(jsonString)
                    if (jsonArray.length() > 0) {
                        val firstObject = jsonArray.getJSONObject(0)
                        val moviesJson =
                            firstObject.optJSONArray(StringConstants.SERIALIZED_RESULTS)
                        if (moviesJson != null) {
                            return parseMoviesFromJson(moviesJson)
                        }
                    }
                } catch (e: Exception) {
                    // Fall back to old structure
                }

                // Fall back to old structure
                val jsonObject = JSONObject(jsonString)
                val dataJson = jsonObject.optJSONObject(StringConstants.FIELD_DATA)
                val moviesJson = dataJson?.optJSONArray(StringConstants.FIELD_MOVIES)
                    ?: dataJson?.optJSONArray(StringConstants.SERIALIZED_RESULTS)
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
        val geminiColorsJson = metaJson.optJSONObject(StringConstants.FIELD_GEMINI_COLORS)

        return MetaDto(
            timestamp = metaJson.optString(
                StringConstants.FIELD_TIMESTAMP,
                System.currentTimeMillis().toString()
            ),
            method = method,
            searchQuery = metaJson.optString(StringConstants.FIELD_SEARCH_QUERY, null) ?: "",
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
     * @param moviesJson JSON array containing movie data
     * @return List of MovieDto objects
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
                    ).takeIf { it.isNotEmpty() } ?: movieJson.optString(
                        StringConstants.SERIALIZED_OVERVIEW,
                        StringConstants.NO_DESCRIPTION_AVAILABLE
                    ),
                    posterPath = movieJson.optString(StringConstants.FIELD_POSTER_PATH, null)
                        ?: movieJson.optString(StringConstants.SERIALIZED_POSTER_PATH, null) ?: "",
                    backdropPath = movieJson.optString(StringConstants.FIELD_BACKDROP_PATH, null)
                        ?: movieJson.optString(StringConstants.SERIALIZED_BACKDROP_PATH, null)
                        ?: "",
                    rating = movieJson.optDouble(StringConstants.FIELD_RATING, 0.0)
                        .takeIf { it > 0 }
                        ?: movieJson.optDouble(StringConstants.SERIALIZED_VOTE_AVERAGE, 0.0),
                    voteCount = movieJson.optInt(StringConstants.FIELD_VOTE_COUNT, 0)
                        .takeIf { it > 0 }
                        ?: movieJson.optInt(StringConstants.SERIALIZED_VOTE_COUNT, 0),
                    releaseDate = movieJson.optString(StringConstants.FIELD_RELEASE_DATE, null)
                        ?: movieJson.optString(StringConstants.SERIALIZED_RELEASE_DATE, null) ?: "",
                    genreIds = parseGenreIds(movieJson.optJSONArray(StringConstants.FIELD_GENRE_IDS))
                        .takeIf { it.isNotEmpty() } ?: parseGenreIds(
                        movieJson.optJSONArray(
                            StringConstants.SERIALIZED_GENRE_IDS
                        )
                    ),
                    popularity = movieJson.optDouble(StringConstants.FIELD_POPULARITY, 0.0)
                        .takeIf { it > 0 }
                        ?: movieJson.optDouble(StringConstants.SERIALIZED_POPULARITY, 0.0),
                    adult = movieJson.optBoolean(StringConstants.FIELD_ADULT, false)
                        .takeIf { movieJson.has(StringConstants.FIELD_ADULT) }
                        ?: movieJson.optBoolean(StringConstants.SERIALIZED_ADULT, false),
                    originalLanguage = movieJson.optString(
                        StringConstants.FIELD_ORIGINAL_LANGUAGE,
                        null
                    )
                        ?: movieJson.optString(StringConstants.SERIALIZED_ORIGINAL_LANGUAGE, null)
                        ?: "en",
                    originalTitle = movieJson.optString(StringConstants.FIELD_ORIGINAL_TITLE, null)
                        ?: movieJson.optString(StringConstants.SERIALIZED_ORIGINAL_TITLE, null)
                        ?: movieJson.optString(
                            StringConstants.FIELD_TITLE,
                            StringConstants.UNKNOWN_MOVIE_TITLE
                        ),
                    video = movieJson.optBoolean(StringConstants.FIELD_VIDEO, false)
                        .takeIf { movieJson.has(StringConstants.FIELD_VIDEO) }
                        ?: movieJson.optBoolean(StringConstants.SERIALIZED_VIDEO, false),
                    colors = parseMovieColors(movieJson)
                )
            )
        }
        return movies
    }

    /**
     * Parses movie colors from JSON object
     * @param movieJson JSON object containing movie data
     * @return MovieColorsDto with parsed color information
     */
    private fun parseMovieColors(movieJson: JSONObject): MovieColorsDto {
        val colorsJson = movieJson.optJSONObject("colors")
        return if (colorsJson != null) {
            val metadataJson = colorsJson.optJSONObject("metadata")
            MovieColorsDto(
                accent = colorsJson.optString("accent", "#3AA1EF"),
                primary = colorsJson.optString("primary", "#1278D4"),
                secondary = colorsJson.optString("secondary", "#238EE5"),
                metadata = ColorMetadataDto(
                    category = metadataJson?.optString("category", "MEDIUM") ?: "MEDIUM",
                    modelUsed = metadataJson?.optBoolean("model_used", true) ?: true,
                    rating = metadataJson?.optDouble("rating", 0.0) ?: 0.0
                )
            )
        } else {
            // Default colors if not present
            MovieColorsDto(
                accent = "#3AA1EF",
                primary = "#1278D4",
                secondary = "#238EE5",
                metadata = ColorMetadataDto(
                    category = "MEDIUM",
                    modelUsed = true,
                    rating = movieJson.optDouble(StringConstants.FIELD_RATING, 0.0)
                        .takeIf { it > 0 }
                        ?: movieJson.optDouble(StringConstants.SERIALIZED_VOTE_AVERAGE, 0.0)
                )
            )
        }
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
                    StringConstants.FIELD_VALUE,
                    StringConstants.COLOR_PRIMARY
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
