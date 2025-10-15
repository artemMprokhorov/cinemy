package org.studioapp.cinemy.data.model

import androidx.compose.ui.graphics.Color
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_ACCENT_COLOR_HEX
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_APP_TITLE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BACK_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BLACK_COLOR_HEX_VALUE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BUTTON_CORNER_RADIUS
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_ERROR_MESSAGE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_GENRE_ACTION
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_GENRE_ACTION_ID
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_GENRE_DRAMA
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_GENRE_DRAMA_ID
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_LOADING_TEXT
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_METHOD
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_BACKDROP_PATH
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_BUDGET
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_DESCRIPTION
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_ID
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_POSTER_PATH
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_RATING_VALUE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_RELEASE_DATE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_REVENUE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_RUNTIME
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_STATUS
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_TITLE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_VOTE_COUNT
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_NO_MOVIES_FOUND
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_PLAY_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_PRIMARY_COLOR_HEX
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_PRIMARY_COLOR_HEX_VALUE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_PRODUCTION_COMPANY_ID
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_RESULTS_COUNT
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_RETRY_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_SECONDARY_COLOR_HEX
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_SECONDARY_COLOR_HEX_VALUE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_STUDIO_LOGO_PATH
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_STUDIO_NAME
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_STUDIO_ORIGIN_COUNTRY
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_VERSION
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_WHITE_COLOR_HEX_VALUE

/**
 * Data class containing default movie details for mock data
 */
data class DefaultMovieDetails(
    val id: Int = DEFAULT_MOVIE_ID,
    val title: String = DEFAULT_MOVIE_TITLE,
    val description: String = DEFAULT_MOVIE_DESCRIPTION,
    val posterPath: String = DEFAULT_MOVIE_POSTER_PATH,
    val backdropPath: String = DEFAULT_MOVIE_BACKDROP_PATH,
    val rating: Double = DEFAULT_MOVIE_RATING_VALUE,
    val voteCount: Int = DEFAULT_MOVIE_VOTE_COUNT,
    val releaseDate: String = DEFAULT_MOVIE_RELEASE_DATE,
    val runtime: Int = DEFAULT_MOVIE_RUNTIME,
    val genres: List<Genre> = listOf(
        Genre(DEFAULT_GENRE_ACTION_ID, DEFAULT_GENRE_ACTION),
        Genre(DEFAULT_GENRE_DRAMA_ID, DEFAULT_GENRE_DRAMA)
    ),
    val productionCompanies: List<ProductionCompany> = listOf(
        ProductionCompany(
            id = DEFAULT_PRODUCTION_COMPANY_ID,
            logoPath = DEFAULT_STUDIO_LOGO_PATH,
            name = DEFAULT_STUDIO_NAME,
            originCountry = DEFAULT_STUDIO_ORIGIN_COUNTRY
        )
    ),
    val budget: Long = DEFAULT_MOVIE_BUDGET,
    val revenue: Long = DEFAULT_MOVIE_REVENUE,
    val status: String = DEFAULT_MOVIE_STATUS
) {
    /**
     * Converts this default seed into the domain `MovieDetails` model.
     *
     * Purpose:
     * - Provides a straightforward way to obtain a fully-populated domain model for mock/demo flows
     *   using sensible defaults from `StringConstants`.
     *
     * Return:
     * - `MovieDetails` — a domain object whose fields mirror this default container: identifiers,
     *   textual attributes, media paths, rating metrics, runtime, genres, production companies,
     *   and financial/status fields.
     *
     * Exceptions:
     * - None. This method is pure and non-throwing.
     */
    fun toMovieDetails(): MovieDetails {
        return MovieDetails(
            id = id,
            title = title,
            description = description,
            posterPath = posterPath,
            backdropPath = backdropPath,
            rating = rating,
            voteCount = voteCount,
            releaseDate = releaseDate,
            runtime = runtime,
            genres = genres,
            productionCompanies = productionCompanies,
            budget = budget,
            revenue = revenue,
            status = status
        )
    }
}

/**
 * Data class containing default UI configuration for mock data
 */
data class DefaultUiConfiguration(
    val primaryColor: Color = Color(DEFAULT_PRIMARY_COLOR_HEX_VALUE),
    val secondaryColor: Color = Color(DEFAULT_SECONDARY_COLOR_HEX_VALUE),
    val backgroundColor: Color = Color(DEFAULT_WHITE_COLOR_HEX_VALUE),
    val surfaceColor: Color = Color(DEFAULT_WHITE_COLOR_HEX_VALUE),
    val onPrimaryColor: Color = Color(DEFAULT_WHITE_COLOR_HEX_VALUE),
    val onSecondaryColor: Color = Color(DEFAULT_BLACK_COLOR_HEX_VALUE),
    val onBackgroundColor: Color = Color(DEFAULT_BLACK_COLOR_HEX_VALUE),
    val onSurfaceColor: Color = Color(DEFAULT_BLACK_COLOR_HEX_VALUE),
    val moviePosterColors: List<Color> = listOf(
        Color(DEFAULT_PRIMARY_COLOR_HEX_VALUE),
        Color(DEFAULT_SECONDARY_COLOR_HEX_VALUE)
    ),
    val appTitle: String = DEFAULT_APP_TITLE,
    val loadingText: String = DEFAULT_LOADING_TEXT,
    val errorMessage: String = DEFAULT_ERROR_MESSAGE,
    val noMoviesFound: String = DEFAULT_NO_MOVIES_FOUND,
    val retryButton: String = DEFAULT_RETRY_BUTTON,
    val backButton: String = DEFAULT_BACK_BUTTON,
    val playButton: String = DEFAULT_PLAY_BUTTON,
    val primaryButtonColor: Color = Color(DEFAULT_PRIMARY_COLOR_HEX_VALUE),
    val secondaryButtonColor: Color = Color(DEFAULT_SECONDARY_COLOR_HEX_VALUE),
    val buttonTextColor: Color = Color(DEFAULT_WHITE_COLOR_HEX_VALUE),
    val buttonCornerRadius: Int = DEFAULT_BUTTON_CORNER_RADIUS
) {
    /**
     * Converts this default UI seed into the domain `UiConfiguration` model.
     *
     * Purpose:
     * - Supplies a complete, Compose-ready UI configuration for mock/demo screens, including
     *   color palette, text resources, and button presentation.
     *
     * Return:
     * - `UiConfiguration` — encapsulates `ColorScheme`, `TextConfiguration`, and
     *   `ButtonConfiguration` constructed from the default values provided here.
     *
     * Exceptions:
     * - None. This method is pure and non-throwing.
     */
    fun toUiConfiguration(): UiConfiguration {
        return UiConfiguration(
            colors = ColorScheme(
                primary = primaryColor,
                secondary = secondaryColor,
                background = backgroundColor,
                surface = surfaceColor,
                onPrimary = onPrimaryColor,
                onSecondary = onSecondaryColor,
                onBackground = onBackgroundColor,
                onSurface = onSurfaceColor,
                moviePosterColors = moviePosterColors
            ),
            texts = TextConfiguration(
                appTitle = appTitle,
                loadingText = loadingText,
                errorMessage = errorMessage,
                noMoviesFound = noMoviesFound,
                retryButton = retryButton,
                backButton = backButton,
                playButton = playButton
            ),
            buttons = ButtonConfiguration(
                primaryButtonColor = primaryButtonColor,
                secondaryButtonColor = secondaryButtonColor,
                buttonTextColor = buttonTextColor,
                buttonCornerRadius = buttonCornerRadius
            )
        )
    }
}

/**
 * Data class containing default meta information for mock data
 */
data class DefaultMeta(
    val timestamp: String = System.currentTimeMillis().toString(),
    val method: String = DEFAULT_METHOD,
    val resultsCount: Int = DEFAULT_RESULTS_COUNT,
    val aiGenerated: Boolean = false,
    val primaryColor: String = DEFAULT_PRIMARY_COLOR_HEX,
    val secondaryColor: String = DEFAULT_SECONDARY_COLOR_HEX,
    val accentColor: String = DEFAULT_ACCENT_COLOR_HEX,
    val version: String = DEFAULT_VERSION
) {
    /**
     * Converts this default metadata seed into the domain `Meta` model.
     *
     * Purpose:
     * - Produces a standard metadata envelope for responses, including timestamps,
     *   MCP method name, results count, AI-generation flag, version, and color hints.
     *
     * Return:
     * - `Meta` — domain metadata with `GeminiColors` composed from `primaryColor`, `secondaryColor`,
     *   and `accentColor`.
     *
     * Exceptions:
     * - None. This method is pure and non-throwing.
     */
    fun toMeta(): Meta {
        return Meta(
            timestamp = timestamp,
            method = method,
            resultsCount = resultsCount,
            aiGenerated = aiGenerated,
            geminiColors = GeminiColors(
                primary = primaryColor,
                secondary = secondaryColor,
                accent = accentColor
            ),
            version = version
        )
    }
}
