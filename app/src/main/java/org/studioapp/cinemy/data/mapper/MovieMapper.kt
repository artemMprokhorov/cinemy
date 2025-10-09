package org.studioapp.cinemy.data.mapper

import org.studioapp.cinemy.data.model.ButtonConfiguration
import org.studioapp.cinemy.data.model.ColorMetadata
import org.studioapp.cinemy.data.model.ColorScheme
import org.studioapp.cinemy.data.model.Genre
import org.studioapp.cinemy.data.model.Movie
import org.studioapp.cinemy.data.model.MovieColors
import org.studioapp.cinemy.data.model.MovieDetails
import org.studioapp.cinemy.data.model.MovieListResponse
import org.studioapp.cinemy.data.model.ProductionCompany
import org.studioapp.cinemy.data.model.SearchInfo
import org.studioapp.cinemy.data.model.SentimentMetadata
import org.studioapp.cinemy.data.model.SentimentReviews
import org.studioapp.cinemy.data.model.TextConfiguration
import org.studioapp.cinemy.data.model.UiConfiguration
import org.studioapp.cinemy.data.remote.dto.ButtonConfigurationDto
import org.studioapp.cinemy.data.remote.dto.ColorMetadataDto
import org.studioapp.cinemy.data.remote.dto.ColorSchemeDto
import org.studioapp.cinemy.data.remote.dto.GenreDto
import org.studioapp.cinemy.data.remote.dto.MovieColorsDto
import org.studioapp.cinemy.data.remote.dto.MovieDetailsDto
import org.studioapp.cinemy.data.remote.dto.MovieDto
import org.studioapp.cinemy.data.remote.dto.MovieListResponseDto
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
