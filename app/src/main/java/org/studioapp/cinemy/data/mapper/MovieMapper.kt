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
import org.studioapp.cinemy.utils.ColorUtils.parseColor
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BOOLEAN_VALUE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_DOUBLE_VALUE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_INT_VALUE
import org.studioapp.cinemy.data.model.StringConstants.EMPTY_STRING
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ACCENT
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ADULT
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_BACKDROP_PATH
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_CATEGORY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_COLORS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_GENRE_IDS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ID
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_METADATA
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_MODEL_USED
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ORIGINAL_LANGUAGE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_ORIGINAL_TITLE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_OVERVIEW
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_POPULARITY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_POSTER_PATH
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_PRIMARY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RATING
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RELEASE_DATE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_SECONDARY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_TITLE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_VIDEO
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_VOTE_AVERAGE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_VOTE_COUNT
import org.studioapp.cinemy.data.model.StringConstants.FIELD_BACKDROP_PATH
import org.studioapp.cinemy.data.model.StringConstants.FIELD_BUDGET
import org.studioapp.cinemy.data.model.StringConstants.FIELD_DESCRIPTION
import org.studioapp.cinemy.data.model.StringConstants.FIELD_GENRES
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ID
import org.studioapp.cinemy.data.model.StringConstants.FIELD_LOGO_PATH
import org.studioapp.cinemy.data.model.StringConstants.FIELD_MOVIE_DETAILS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_NAME
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ORIGIN_COUNTRY
import org.studioapp.cinemy.data.model.StringConstants.FIELD_POSTER_PATH
import org.studioapp.cinemy.data.model.StringConstants.FIELD_PRODUCTION_COMPANIES
import org.studioapp.cinemy.data.model.StringConstants.FIELD_RATING
import org.studioapp.cinemy.data.model.StringConstants.FIELD_RELEASE_DATE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_REVENUE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_RUNTIME
import org.studioapp.cinemy.data.model.StringConstants.FIELD_STATUS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_TITLE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_VOTE_COUNT
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_LONG_VALUE

object MovieMapper {

    /**
     * Maps raw JSON movie data to MovieDto
     * @param movieData Raw JSON map from MCP response
     * @return MovieDto
     */
    fun mapJsonToMovieDto(movieData: Map<String, Any>): MovieDto {
        val colorsData = movieData[SERIALIZED_COLORS] as? Map<String, Any>
        val metadataData = colorsData?.get(SERIALIZED_METADATA) as? Map<String, Any>

        return MovieDto(
            id = (movieData[SERIALIZED_ID] as? Number)?.toInt() ?: DEFAULT_INT_VALUE,
            title = movieData[SERIALIZED_TITLE] as? String ?: EMPTY_STRING,
            description = movieData[SERIALIZED_OVERVIEW] as? String ?: EMPTY_STRING,
            posterPath = movieData[SERIALIZED_POSTER_PATH] as? String,
            backdropPath = movieData[SERIALIZED_BACKDROP_PATH] as? String,
            rating = (movieData[SERIALIZED_VOTE_AVERAGE] as? Number)?.toDouble() ?: DEFAULT_DOUBLE_VALUE,
            voteCount = (movieData[SERIALIZED_VOTE_COUNT] as? Number)?.toInt() ?: DEFAULT_INT_VALUE,
            releaseDate = movieData[SERIALIZED_RELEASE_DATE] as? String ?: EMPTY_STRING,
            genreIds = (movieData[SERIALIZED_GENRE_IDS] as? List<Number>)?.map { it.toInt() } ?: emptyList(),
            popularity = (movieData[SERIALIZED_POPULARITY] as? Number)?.toDouble() ?: DEFAULT_DOUBLE_VALUE,
            adult = movieData[SERIALIZED_ADULT] as? Boolean ?: DEFAULT_BOOLEAN_VALUE,
            originalLanguage = movieData[SERIALIZED_ORIGINAL_LANGUAGE] as? String ?: EMPTY_STRING,
            originalTitle = movieData[SERIALIZED_ORIGINAL_TITLE] as? String ?: EMPTY_STRING,
            video = movieData[SERIALIZED_VIDEO] as? Boolean ?: DEFAULT_BOOLEAN_VALUE,
            colors = MovieColorsDto(
                accent = colorsData?.get(SERIALIZED_ACCENT) as? String ?: EMPTY_STRING,
                primary = colorsData?.get(SERIALIZED_PRIMARY) as? String ?: EMPTY_STRING,
                secondary = colorsData?.get(SERIALIZED_SECONDARY) as? String ?: EMPTY_STRING,
                metadata = ColorMetadataDto(
                    category = metadataData?.get(SERIALIZED_CATEGORY) as? String ?: EMPTY_STRING,
                    modelUsed = metadataData?.get(SERIALIZED_MODEL_USED) as? Boolean ?: DEFAULT_BOOLEAN_VALUE,
                    rating = (metadataData?.get(SERIALIZED_RATING) as? Number)?.toDouble() ?: DEFAULT_DOUBLE_VALUE
                )
            )
        )
    }

    /**
     * Maps raw JSON movie details data to MovieDetailsDto
     * @param movieDetailsData Raw JSON map from MCP response
     * @param movieId Movie ID as fallback
     * @return MovieDetailsDto
     */
    fun mapJsonToMovieDetailsDto(movieDetailsData: Map<String, Any>, movieId: Int): MovieDetailsDto {
        return MovieDetailsDto(
            id = (movieDetailsData[FIELD_ID] as? Number)?.toInt() ?: movieId,
            title = movieDetailsData[FIELD_TITLE] as? String ?: EMPTY_STRING,
            description = movieDetailsData[FIELD_DESCRIPTION] as? String ?: EMPTY_STRING,
            posterPath = movieDetailsData[FIELD_POSTER_PATH] as? String,
            backdropPath = movieDetailsData[FIELD_BACKDROP_PATH] as? String,
            rating = (movieDetailsData[FIELD_RATING] as? Number)?.toDouble() ?: DEFAULT_DOUBLE_VALUE,
            voteCount = (movieDetailsData[FIELD_VOTE_COUNT] as? Number)?.toInt() ?: DEFAULT_INT_VALUE,
            releaseDate = movieDetailsData[FIELD_RELEASE_DATE] as? String ?: EMPTY_STRING,
            runtime = (movieDetailsData[FIELD_RUNTIME] as? Number)?.toInt() ?: DEFAULT_INT_VALUE,
            genres = (movieDetailsData[FIELD_GENRES] as? List<Map<String, Any>>)?.map { genreData ->
                GenreDto(
                    id = (genreData[FIELD_ID] as? Number)?.toInt() ?: DEFAULT_INT_VALUE,
                    name = genreData[FIELD_NAME] as? String ?: EMPTY_STRING
                )
            } ?: emptyList(),
            productionCompanies = (movieDetailsData[FIELD_PRODUCTION_COMPANIES] as? List<Map<String, Any>>)?.map { companyData ->
                ProductionCompanyDto(
                    id = (companyData[FIELD_ID] as? Number)?.toInt() ?: DEFAULT_INT_VALUE,
                    logoPath = companyData[FIELD_LOGO_PATH] as? String,
                    name = companyData[FIELD_NAME] as? String ?: EMPTY_STRING,
                    originCountry = companyData[FIELD_ORIGIN_COUNTRY] as? String ?: EMPTY_STRING
                )
            } ?: emptyList(),
            budget = (movieDetailsData[FIELD_BUDGET] as? Number)?.toLong() ?: DEFAULT_LONG_VALUE,
            revenue = (movieDetailsData[FIELD_REVENUE] as? Number)?.toLong() ?: DEFAULT_LONG_VALUE,
            status = movieDetailsData[FIELD_STATUS] as? String ?: EMPTY_STRING
        )
    }

    /**
     * Maps MovieDto to domain Movie model
     * @param dto MovieDto from API
     * @return Movie domain model
     */
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

    /**
     * Maps MovieDetailsDto to domain MovieDetails model
     * @param dto MovieDetailsDto from API
     * @return MovieDetails domain model
     */
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

    /**
     * Maps SentimentReviewsDto to domain SentimentReviews model
     * @param dto SentimentReviewsDto from API (nullable)
     * @return SentimentReviews domain model or null
     */
    fun mapSentimentReviewsDtoToSentimentReviews(dto: SentimentReviewsDto?): SentimentReviews? {
        return dto?.let {
            SentimentReviews(
                positive = it.positive,
                negative = it.negative
            )
        }
    }

    /**
     * Maps SentimentMetadataDto to domain SentimentMetadata model
     * @param dto SentimentMetadataDto from API (nullable)
     * @return SentimentMetadata domain model or null
     */
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

    /**
     * Maps GenreDto to domain Genre model
     * @param dto GenreDto from API
     * @return Genre domain model
     */
    fun mapGenreDtoToGenre(dto: GenreDto): Genre {
        return Genre(
            id = dto.id,
            name = dto.name
        )
    }

    /**
     * Maps ProductionCompanyDto to domain ProductionCompany model
     * @param dto ProductionCompanyDto from API
     * @return ProductionCompany domain model
     */
    fun mapProductionCompanyDtoToProductionCompany(dto: ProductionCompanyDto): ProductionCompany {
        return ProductionCompany(
            id = dto.id,
            logoPath = dto.logoPath,
            name = dto.name,
            originCountry = dto.originCountry
        )
    }


    /**
     * Maps UiConfigurationDto to domain UiConfiguration model
     * @param dto UiConfigurationDto from API
     * @return UiConfiguration domain model
     */
    fun mapUiConfigurationDtoToUiConfiguration(dto: UiConfigurationDto): UiConfiguration {
        return UiConfiguration(
            colors = mapColorSchemeDtoToColorScheme(dto.colors),
            texts = mapTextConfigurationDtoToTextConfiguration(dto.texts),
            buttons = mapButtonConfigurationDtoToButtonConfiguration(dto.buttons),
            searchInfo = dto.searchInfo?.let { mapSearchInfoDtoToSearchInfo(it) }
        )
    }

    /**
     * Maps ColorSchemeDto to domain ColorScheme model
     * @param dto ColorSchemeDto from API
     * @return ColorScheme domain model
     */
    fun mapColorSchemeDtoToColorScheme(dto: ColorSchemeDto): ColorScheme {
        return ColorScheme(
            primary = parseColor(dto.primary),
            secondary = parseColor(dto.secondary),
            background = parseColor(dto.background),
            surface = parseColor(dto.surface),
            onPrimary = parseColor(dto.onPrimary),
            onSecondary = parseColor(dto.onSecondary),
            onBackground = parseColor(dto.onBackground),
            onSurface = parseColor(dto.onSurface),
            moviePosterColors = dto.moviePosterColors.map { parseColor(it) }
        )
    }

    /**
     * Maps TextConfigurationDto to domain TextConfiguration model
     * @param dto TextConfigurationDto from API
     * @return TextConfiguration domain model
     */
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

    /**
     * Maps ButtonConfigurationDto to domain ButtonConfiguration model
     * @param dto ButtonConfigurationDto from API
     * @return ButtonConfiguration domain model
     */
    fun mapButtonConfigurationDtoToButtonConfiguration(dto: ButtonConfigurationDto): ButtonConfiguration {
        return ButtonConfiguration(
            primaryButtonColor = parseColor(dto.primaryButtonColor),
            secondaryButtonColor = parseColor(dto.secondaryButtonColor),
            buttonTextColor = parseColor(dto.buttonTextColor),
            buttonCornerRadius = dto.buttonCornerRadius
        )
    }

    /**
     * Maps SearchInfoDto to domain SearchInfo model
     * @param dto SearchInfoDto from API
     * @return SearchInfo domain model
     */
    fun mapSearchInfoDtoToSearchInfo(dto: SearchInfoDto): SearchInfo {
        return SearchInfo(
            query = dto.query,
            resultCount = dto.resultCount,
            avgRating = dto.avgRating,
            ratingType = dto.ratingType,
            colorBased = dto.colorBased
        )
    }


    /**
     * Maps MovieColorsDto to domain MovieColors model
     * @param dto MovieColorsDto from API
     * @return MovieColors domain model
     */
    fun mapMovieColorsDtoToMovieColors(dto: MovieColorsDto): MovieColors {
        return MovieColors(
            accent = dto.accent,
            primary = dto.primary,
            secondary = dto.secondary,
            metadata = mapColorMetadataDtoToColorMetadata(dto.metadata)
        )
    }

    /**
     * Maps ColorMetadataDto to domain ColorMetadata model
     * @param dto ColorMetadataDto from API
     * @return ColorMetadata domain model
     */
    fun mapColorMetadataDtoToColorMetadata(dto: ColorMetadataDto): ColorMetadata {
        return ColorMetadata(
            category = dto.category,
            modelUsed = dto.modelUsed,
            rating = dto.rating
        )
    }

    /**
     * Maps MovieListResponseDto to domain MovieListResponse model
     * @param dto MovieListResponseDto from API
     * @return MovieListResponse domain model
     */
    fun mapMovieListResponseDtoToMovieListResponse(dto: MovieListResponseDto): MovieListResponse {
        return MovieListResponse(
            page = dto.page,
            results = dto.results.map { mapMovieDtoToMovie(it) },
            totalPages = dto.totalPages,
            totalResults = dto.totalResults
        )
    }
}
