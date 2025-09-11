package org.studioapp.cinemy.data.mapper

import androidx.compose.ui.graphics.Color
import org.studioapp.cinemy.data.model.ButtonConfiguration
import org.studioapp.cinemy.data.model.ColorScheme
import org.studioapp.cinemy.data.model.GeminiColors
import org.studioapp.cinemy.data.model.Genre
import org.studioapp.cinemy.data.model.Meta
import org.studioapp.cinemy.data.model.Movie
import org.studioapp.cinemy.data.model.MovieDetails
import org.studioapp.cinemy.data.model.MovieListData
import org.studioapp.cinemy.data.model.MovieListResponse
import org.studioapp.cinemy.data.model.Pagination
import org.studioapp.cinemy.data.model.ProductionCompany
import org.studioapp.cinemy.data.model.Result
import org.studioapp.cinemy.data.model.SearchInfo
import org.studioapp.cinemy.data.model.SentimentReviews
import org.studioapp.cinemy.data.model.StringConstants
import org.studioapp.cinemy.data.model.TextConfiguration
import org.studioapp.cinemy.data.model.UiConfiguration
import org.studioapp.cinemy.data.remote.dto.ButtonConfigurationDto
import org.studioapp.cinemy.data.remote.dto.ColorSchemeDto
import org.studioapp.cinemy.data.remote.dto.GeminiColorsDto
import org.studioapp.cinemy.data.remote.dto.GenreDto
import org.studioapp.cinemy.data.remote.dto.McpMovieListResponseDto
import org.studioapp.cinemy.data.remote.dto.McpResponseDto
import org.studioapp.cinemy.data.remote.dto.MetaDto
import org.studioapp.cinemy.data.remote.dto.MovieDetailsDto
import org.studioapp.cinemy.data.remote.dto.MovieDto
import org.studioapp.cinemy.data.remote.dto.PaginationDto
import org.studioapp.cinemy.data.remote.dto.ProductionCompanyDto
import org.studioapp.cinemy.data.remote.dto.SearchInfoDto
import org.studioapp.cinemy.data.remote.dto.SentimentReviewsDto
import org.studioapp.cinemy.data.remote.dto.TextConfigurationDto
import org.studioapp.cinemy.data.remote.dto.UiConfigurationDto
import org.studioapp.cinemy.data.util.ColorUtils

object MovieMapper {

    fun mapMovieDtoToMovie(dto: MovieDto): Movie {
        return Movie(
            id = dto.id,
            title = dto.title,
            description = dto.description,
            posterPath = dto.posterPath,
            backdropPath = dto.backdropPath,
            rating = dto.rating,
            voteCount = dto.voteCount,
            releaseDate = dto.releaseDate,
            genreIds = dto.genreIds,
            popularity = dto.popularity,
            adult = dto.adult
        )
    }

    fun mapMovieDetailsDtoToMovieDetails(dto: MovieDetailsDto): MovieDetails {
        return MovieDetails(
            id = dto.id,
            title = dto.title,
            description = dto.description,
            posterPath = dto.posterPath,
            backdropPath = dto.backdropPath,
            rating = dto.rating,
            voteCount = dto.voteCount,
            releaseDate = dto.releaseDate,
            runtime = dto.runtime ?: 0,
            genres = dto.genres.map { mapGenreDtoToGenre(it) },
            productionCompanies = dto.productionCompanies.map {
                mapProductionCompanyDtoToProductionCompany(
                    it
                )
            },
            budget = dto.budget,
            revenue = dto.revenue,
            status = dto.status
        )
    }

    fun mapGenreDtoToGenre(dto: GenreDto): Genre {
        return Genre(
            id = dto.id,
            name = dto.name
        )
    }

    fun mapProductionCompanyDtoToProductionCompany(dto: ProductionCompanyDto): ProductionCompany {
        return ProductionCompany(
            id = dto.id,
            logoPath = dto.logoPath,
            name = dto.name,
            originCountry = dto.originCountry
        )
    }

    fun mapMcpMovieListResponseDtoToMovieListResponse(dto: McpMovieListResponseDto): MovieListResponse {
        return MovieListResponse(
            success = true,
            data = MovieListData(
                movies = dto.movies.map { mapMovieDtoToMovie(it) },
                pagination = mapPaginationDtoToPagination(dto.pagination),
                searchQuery = dto.searchQuery
            ),
            uiConfig = UiConfiguration(
                colors = ColorScheme(
                    primary = Color.Blue,
                    secondary = Color.Gray,
                    background = Color.Black,
                    surface = Color.DarkGray,
                    onPrimary = Color.White,
                    onSecondary = Color.White,
                    onBackground = Color.White,
                    onSurface = Color.White,
                    moviePosterColors = emptyList()
                ),
                texts = TextConfiguration(
                    appTitle = StringConstants.MOVIES_TITLE,
                    loadingText = StringConstants.LOADING_TEXT,
                    errorMessage = StringConstants.ERROR_GENERIC,
                    noMoviesFound = StringConstants.NO_MOVIES_FOUND,
                    retryButton = StringConstants.RETRY_BUTTON,
                    backButton = StringConstants.BACK_BUTTON,
                    playButton = StringConstants.PLAY_BUTTON
                ),
                buttons = ButtonConfiguration(
                    primaryButtonColor = Color.Blue,
                    secondaryButtonColor = Color.Gray,
                    buttonTextColor = Color.White,
                    buttonCornerRadius = StringConstants.BUTTON_CORNER_RADIUS
                )
            ),
            error = null,
            meta = Meta(
                timestamp = System.currentTimeMillis().toString(),
                method = StringConstants.MCP_METHOD_GET_POPULAR_MOVIES,
                searchQuery = null,
                movieId = null,
                resultsCount = dto.movies.size,
                aiGenerated = true,
                geminiColors = GeminiColors(
                    primary = StringConstants.COLOR_PRIMARY,
                    secondary = StringConstants.COLOR_SECONDARY,
                    accent = StringConstants.COLOR_ACCENT
                ),
                avgRating = null,
                movieRating = null,
                version = StringConstants.VERSION_2_0_0
            )
        )
    }

    fun mapPaginationDtoToPagination(dto: PaginationDto): Pagination {
        return Pagination(
            page = dto.page,
            totalPages = dto.totalPages,
            totalResults = dto.totalResults,
            hasNext = dto.hasNext,
            hasPrevious = dto.hasPrevious
        )
    }

    fun mapUiConfigurationDtoToUiConfiguration(dto: UiConfigurationDto): UiConfiguration {
        return UiConfiguration(
            colors = mapColorSchemeDtoToColorScheme(dto.colors),
            texts = mapTextConfigurationDtoToTextConfiguration(dto.texts),
            buttons = mapButtonConfigurationDtoToButtonConfiguration(dto.buttons),
            searchInfo = dto.searchInfo?.let { mapSearchInfoDtoToSearchInfo(it) }
        )
    }

    fun mapColorSchemeDtoToColorScheme(dto: ColorSchemeDto): ColorScheme {
        return ColorScheme(
            primary = ColorUtils.parseColor(dto.primary),
            secondary = ColorUtils.parseColor(dto.secondary),
            background = ColorUtils.parseColor(dto.background),
            surface = ColorUtils.parseColor(dto.surface),
            onPrimary = ColorUtils.parseColor(dto.onPrimary),
            onSecondary = ColorUtils.parseColor(dto.onSecondary),
            onBackground = ColorUtils.parseColor(dto.onBackground),
            onSurface = ColorUtils.parseColor(dto.onSurface),
            moviePosterColors = dto.moviePosterColors.map { ColorUtils.parseColor(it) }
        )
    }

    fun mapTextConfigurationDtoToTextConfiguration(dto: TextConfigurationDto): TextConfiguration {
        return TextConfiguration(
            appTitle = dto.appTitle,
            loadingText = dto.loadingText,
            errorMessage = dto.errorMessage,
            noMoviesFound = dto.noMoviesFound,
            retryButton = dto.retryButton,
            backButton = dto.backButton,
            playButton = dto.playButton
        )
    }

    fun mapButtonConfigurationDtoToButtonConfiguration(dto: ButtonConfigurationDto): ButtonConfiguration {
        return ButtonConfiguration(
            primaryButtonColor = ColorUtils.parseColor(dto.primaryButtonColor),
            secondaryButtonColor = ColorUtils.parseColor(dto.secondaryButtonColor),
            buttonTextColor = ColorUtils.parseColor(dto.buttonTextColor),
            buttonCornerRadius = dto.buttonCornerRadius
        )
    }

    fun mapSearchInfoDtoToSearchInfo(dto: SearchInfoDto): SearchInfo {
        return SearchInfo(
            query = dto.query,
            resultCount = dto.resultCount,
            avgRating = dto.avgRating,
            ratingType = dto.ratingType,
            colorBased = dto.colorBased
        )
    }

    fun mapMetaDtoToMeta(dto: MetaDto): Meta {
        return Meta(
            timestamp = dto.timestamp,
            method = dto.method,
            searchQuery = dto.searchQuery,
            movieId = dto.movieId,
            resultsCount = dto.resultsCount,
            aiGenerated = dto.aiGenerated,
            geminiColors = mapGeminiColorsDtoToGeminiColors(dto.geminiColors),
            avgRating = dto.avgRating,
            movieRating = dto.movieRating,
            version = dto.version
        )
    }

    fun mapGeminiColorsDtoToGeminiColors(dto: GeminiColorsDto): GeminiColors {
        return GeminiColors(
            primary = dto.primary,
            secondary = dto.secondary,
            accent = dto.accent
        )
    }

    fun <T> mapMcpResponseToResult(
        mcpResponse: McpResponseDto<T>,
        dataMapper: (T) -> Any
    ): Result<Any> {
        return if (mcpResponse.success && mcpResponse.data != null) {
            val uiConfig = mapUiConfigurationDtoToUiConfiguration(mcpResponse.uiConfig)
            Result.Success(dataMapper(mcpResponse.data), uiConfig)
        } else {
            val uiConfig = mapUiConfigurationDtoToUiConfiguration(mcpResponse.uiConfig)
            Result.Error(mcpResponse.error ?: StringConstants.ERROR_UNKNOWN, uiConfig)
        }
    }
    
    fun mapSentimentReviewsDtoToSentimentReviews(dto: SentimentReviewsDto?): SentimentReviews? {
        return dto?.let {
            SentimentReviews(
                positive = it.positive,
                negative = it.negative
            )
        }
    }
}
