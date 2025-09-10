package com.example.tmdbai.data.mapper

import androidx.compose.ui.graphics.Color
import com.example.tmdbai.data.model.ButtonConfiguration
import com.example.tmdbai.data.model.ColorScheme
import com.example.tmdbai.data.model.GeminiColors
import com.example.tmdbai.data.model.Genre
import com.example.tmdbai.data.model.Meta
import com.example.tmdbai.data.model.Movie
import com.example.tmdbai.data.model.MovieDetails
import com.example.tmdbai.data.model.MovieListData
import com.example.tmdbai.data.model.MovieListResponse
import com.example.tmdbai.data.model.Pagination
import com.example.tmdbai.data.model.ProductionCompany
import com.example.tmdbai.data.model.Result
import com.example.tmdbai.data.model.SearchInfo
import com.example.tmdbai.data.model.SentimentReviews
import com.example.tmdbai.data.model.StringConstants
import com.example.tmdbai.data.model.TextConfiguration
import com.example.tmdbai.data.model.UiConfiguration
import com.example.tmdbai.data.remote.dto.ButtonConfigurationDto
import com.example.tmdbai.data.remote.dto.ColorSchemeDto
import com.example.tmdbai.data.remote.dto.GeminiColorsDto
import com.example.tmdbai.data.remote.dto.GenreDto
import com.example.tmdbai.data.remote.dto.McpMovieListResponseDto
import com.example.tmdbai.data.remote.dto.McpResponseDto
import com.example.tmdbai.data.remote.dto.MetaDto
import com.example.tmdbai.data.remote.dto.MovieDetailsDto
import com.example.tmdbai.data.remote.dto.MovieDto
import com.example.tmdbai.data.remote.dto.PaginationDto
import com.example.tmdbai.data.remote.dto.ProductionCompanyDto
import com.example.tmdbai.data.remote.dto.SearchInfoDto
import com.example.tmdbai.data.remote.dto.SentimentReviewsDto
import com.example.tmdbai.data.remote.dto.TextConfigurationDto
import com.example.tmdbai.data.remote.dto.UiConfigurationDto
import android.graphics.Color as AndroidColor

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
            primary = Color(AndroidColor.parseColor(dto.primary)),
            secondary = Color(AndroidColor.parseColor(dto.secondary)),
            background = Color(AndroidColor.parseColor(dto.background)),
            surface = Color(AndroidColor.parseColor(dto.surface)),
            onPrimary = Color(AndroidColor.parseColor(dto.onPrimary)),
            onSecondary = Color(AndroidColor.parseColor(dto.onSecondary)),
            onBackground = Color(AndroidColor.parseColor(dto.onBackground)),
            onSurface = Color(AndroidColor.parseColor(dto.onSurface)),
            moviePosterColors = dto.moviePosterColors.map { Color(AndroidColor.parseColor(it)) }
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
            primaryButtonColor = Color(AndroidColor.parseColor(dto.primaryButtonColor)),
            secondaryButtonColor = Color(AndroidColor.parseColor(dto.secondaryButtonColor)),
            buttonTextColor = Color(AndroidColor.parseColor(dto.buttonTextColor)),
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
