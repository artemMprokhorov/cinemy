package org.studioapp.cinemy.data.mapper

import org.studioapp.cinemy.data.model.ButtonConfiguration
import org.studioapp.cinemy.data.model.ColorMetadata
import org.studioapp.cinemy.data.model.ColorScheme
import org.studioapp.cinemy.data.model.GeminiColors
import org.studioapp.cinemy.data.model.Genre
import org.studioapp.cinemy.data.model.Meta
import org.studioapp.cinemy.data.model.Movie
import org.studioapp.cinemy.data.model.MovieColors
import org.studioapp.cinemy.data.model.MovieDetails
import org.studioapp.cinemy.data.model.MovieListResponse
import org.studioapp.cinemy.data.model.Pagination
import org.studioapp.cinemy.data.model.ProductionCompany
import org.studioapp.cinemy.data.model.Result
import org.studioapp.cinemy.data.model.SearchInfo
import org.studioapp.cinemy.data.model.SentimentMetadata
import org.studioapp.cinemy.data.model.SentimentReviews
import org.studioapp.cinemy.data.model.StringConstants
import org.studioapp.cinemy.data.model.TextConfiguration
import org.studioapp.cinemy.data.model.UiConfiguration
import org.studioapp.cinemy.data.remote.dto.ButtonConfigurationDto
import org.studioapp.cinemy.data.remote.dto.ColorMetadataDto
import org.studioapp.cinemy.data.remote.dto.ColorSchemeDto
import org.studioapp.cinemy.data.remote.dto.GeminiColorsDto
import org.studioapp.cinemy.data.remote.dto.GenreDto
import org.studioapp.cinemy.data.remote.dto.McpMovieListResponseDto
import org.studioapp.cinemy.data.remote.dto.McpResponseDto
import org.studioapp.cinemy.data.remote.dto.MetaDto
import org.studioapp.cinemy.data.remote.dto.MovieColorsDto
import org.studioapp.cinemy.data.remote.dto.MovieDetailsDto
import org.studioapp.cinemy.data.remote.dto.MovieDto
import org.studioapp.cinemy.data.remote.dto.MovieListResponseDto
import org.studioapp.cinemy.data.remote.dto.PaginationDto
import org.studioapp.cinemy.data.remote.dto.ProductionCompanyDto
import org.studioapp.cinemy.data.remote.dto.SearchInfoDto
import org.studioapp.cinemy.data.remote.dto.SentimentMetadataDto
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
            adult = dto.adult,
            originalLanguage = dto.originalLanguage,
            originalTitle = dto.originalTitle,
            video = dto.video,
            colors = mapMovieColorsDtoToMovieColors(dto.colors)
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

    fun mapSentimentReviewsDtoToSentimentReviews(dto: SentimentReviewsDto?): SentimentReviews? {
        return dto?.let {
            SentimentReviews(
                positive = it.positive,
                negative = it.negative
            )
        }
    }

    fun mapSentimentMetadataDtoToSentimentMetadata(dto: SentimentMetadataDto?): SentimentMetadata? {
        return dto?.let {
            SentimentMetadata(
                totalReviews = it.totalReviews,
                positiveCount = it.positiveCount,
                negativeCount = it.negativeCount,
                source = it.source,
                timestamp = it.timestamp,
                apiSuccess = it.apiSuccess
            )
        }
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
            page = dto.pagination.page,
            results = dto.movies.map { mapMovieDtoToMovie(it) },
            totalPages = dto.pagination.totalPages,
            totalResults = dto.pagination.totalResults
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

    fun mapMovieColorsDtoToMovieColors(dto: MovieColorsDto): MovieColors {
        return MovieColors(
            accent = dto.accent,
            primary = dto.primary,
            secondary = dto.secondary,
            metadata = mapColorMetadataDtoToColorMetadata(dto.metadata)
        )
    }

    fun mapColorMetadataDtoToColorMetadata(dto: ColorMetadataDto): ColorMetadata {
        return ColorMetadata(
            category = dto.category,
            modelUsed = dto.modelUsed,
            rating = dto.rating
        )
    }

    fun mapMovieListResponseDtoToMovieListResponse(dto: MovieListResponseDto): MovieListResponse {
        return MovieListResponse(
            page = dto.page,
            results = dto.results.map { mapMovieDtoToMovie(it) },
            totalPages = dto.totalPages,
            totalResults = dto.totalResults
        )
    }
}
