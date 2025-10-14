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
import org.studioapp.cinemy.data.model.StringConstants.BACK_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.BUTTON_CORNER_RADIUS
import org.studioapp.cinemy.data.model.StringConstants.CATEGORY_MEDIUM
import org.studioapp.cinemy.data.model.StringConstants.COLOR_ACCENT
import org.studioapp.cinemy.data.model.StringConstants.COLOR_BACKGROUND
import org.studioapp.cinemy.data.model.StringConstants.COLOR_BUTTON_TEXT
import org.studioapp.cinemy.data.model.StringConstants.COLOR_ON_BACKGROUND
import org.studioapp.cinemy.data.model.StringConstants.COLOR_ON_PRIMARY
import org.studioapp.cinemy.data.model.StringConstants.COLOR_ON_SECONDARY
import org.studioapp.cinemy.data.model.StringConstants.COLOR_ON_SURFACE
import org.studioapp.cinemy.data.model.StringConstants.COLOR_PRIMARY
import org.studioapp.cinemy.data.model.StringConstants.COLOR_SECONDARY
import org.studioapp.cinemy.data.model.StringConstants.COLOR_SURFACE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BACKEND_APP_TITLE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BACKEND_BACKGROUND_COLOR
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BACKEND_BACK_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BACKEND_BUTTON_CORNER_RADIUS
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BACKEND_ERROR_MESSAGE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BACKEND_LOADING_TEXT
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BACKEND_NO_MOVIES_FOUND
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BACKEND_ON_BACKGROUND_COLOR
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BACKEND_ON_PRIMARY_COLOR
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BACKEND_ON_SECONDARY_COLOR
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BACKEND_ON_SURFACE_COLOR
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BACKEND_PLAY_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BACKEND_PRIMARY_COLOR
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BACKEND_RETRY_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BACKEND_SECONDARY_COLOR
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BACKEND_SURFACE_COLOR
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_BOOLEAN_VALUE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_DOUBLE_VALUE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_INT_VALUE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_LONG_VALUE
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_ACCENT_COLOR
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_PRIMARY_COLOR
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_MOVIE_SECONDARY_COLOR
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_TOTAL_PAGES
import org.studioapp.cinemy.data.model.StringConstants.EMPTY_STRING
import org.studioapp.cinemy.data.model.StringConstants.ERROR_UNKNOWN
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ACCENT
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ADULT
import org.studioapp.cinemy.data.model.StringConstants.FIELD_AI_GENERATED
import org.studioapp.cinemy.data.model.StringConstants.FIELD_API_SUCCESS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_APP_TITLE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_AVG_RATING
import org.studioapp.cinemy.data.model.StringConstants.FIELD_BACKDROP_PATH
import org.studioapp.cinemy.data.model.StringConstants.FIELD_BACKGROUND
import org.studioapp.cinemy.data.model.StringConstants.FIELD_BACK_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.FIELD_BUDGET
import org.studioapp.cinemy.data.model.StringConstants.FIELD_BUTTON_CORNER_RADIUS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_BUTTON_TEXT_COLOR
import org.studioapp.cinemy.data.model.StringConstants.FIELD_COLORS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_DESCRIPTION
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ERROR_MESSAGE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_GEMINI_COLORS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_GENRES
import org.studioapp.cinemy.data.model.StringConstants.FIELD_GENRE_IDS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ID
import org.studioapp.cinemy.data.model.StringConstants.FIELD_LOADING_TEXT
import org.studioapp.cinemy.data.model.StringConstants.FIELD_LOGO_PATH
import org.studioapp.cinemy.data.model.StringConstants.FIELD_MOVIE_DETAILS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_MOVIE_POSTER_COLORS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_MOVIE_RATING
import org.studioapp.cinemy.data.model.StringConstants.FIELD_NAME
import org.studioapp.cinemy.data.model.StringConstants.FIELD_NEGATIVE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_NEGATIVE_COUNT
import org.studioapp.cinemy.data.model.StringConstants.FIELD_NO_MOVIES_FOUND
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ON_BACKGROUND
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ON_PRIMARY
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ON_SECONDARY
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ON_SURFACE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ORIGINAL_LANGUAGE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ORIGINAL_TITLE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_ORIGIN_COUNTRY
import org.studioapp.cinemy.data.model.StringConstants.FIELD_PLAY_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.FIELD_POPULARITY
import org.studioapp.cinemy.data.model.StringConstants.FIELD_POSITIVE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_POSITIVE_COUNT
import org.studioapp.cinemy.data.model.StringConstants.FIELD_POSTER_PATH
import org.studioapp.cinemy.data.model.StringConstants.FIELD_PRIMARY
import org.studioapp.cinemy.data.model.StringConstants.FIELD_PRIMARY_BUTTON_COLOR
import org.studioapp.cinemy.data.model.StringConstants.FIELD_PRODUCTION_COMPANIES
import org.studioapp.cinemy.data.model.StringConstants.FIELD_RATING
import org.studioapp.cinemy.data.model.StringConstants.FIELD_RELEASE_DATE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_RETRY_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.FIELD_REVENUE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_RUNTIME
import org.studioapp.cinemy.data.model.StringConstants.FIELD_SEARCH_QUERY
import org.studioapp.cinemy.data.model.StringConstants.FIELD_SECONDARY
import org.studioapp.cinemy.data.model.StringConstants.FIELD_SECONDARY_BUTTON_COLOR
import org.studioapp.cinemy.data.model.StringConstants.FIELD_SENTIMENT_METADATA
import org.studioapp.cinemy.data.model.StringConstants.FIELD_SENTIMENT_REVIEWS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_SOURCE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_STATUS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_SURFACE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_TIMESTAMP
import org.studioapp.cinemy.data.model.StringConstants.FIELD_TITLE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_TOTAL_REVIEWS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_VERSION
import org.studioapp.cinemy.data.model.StringConstants.FIELD_VIDEO
import org.studioapp.cinemy.data.model.StringConstants.FIELD_VOTE_COUNT
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_APP_TITLE
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_BACKGROUND
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_BACK_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_BUTTONS
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_BUTTON_CORNER_RADIUS
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_BUTTON_TEXT_COLOR
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_COLORS
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_ERROR_MESSAGE
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_LOADING_TEXT
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_MOVIE_POSTER_COLORS
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_NO_MOVIES_FOUND
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_ON_BACKGROUND
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_ON_PRIMARY
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_ON_SECONDARY
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_ON_SURFACE
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_PLAY_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_PRIMARY
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_PRIMARY_BUTTON_COLOR
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_RETRY_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_SECONDARY
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_SECONDARY_BUTTON_COLOR
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_SURFACE
import org.studioapp.cinemy.data.model.StringConstants.JSON_FIELD_TEXTS
import org.studioapp.cinemy.data.model.StringConstants.LANGUAGE_EN
import org.studioapp.cinemy.data.model.StringConstants.LOADING_MOVIES_TEXT
import org.studioapp.cinemy.data.model.StringConstants.MOVIES_TITLE
import org.studioapp.cinemy.data.model.StringConstants.MOVIE_COLORS_CATEGORY
import org.studioapp.cinemy.data.model.StringConstants.MOVIE_COLORS_METADATA
import org.studioapp.cinemy.data.model.StringConstants.MOVIE_COLORS_MODEL_USED
import org.studioapp.cinemy.data.model.StringConstants.NO_DESCRIPTION_AVAILABLE
import org.studioapp.cinemy.data.model.StringConstants.NO_MOVIES_FOUND
import org.studioapp.cinemy.data.model.StringConstants.PLAY_BUTTON
import org.studioapp.cinemy.data.model.StringConstants.RETRY_BUTTON
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
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_PAGE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_POPULARITY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_POSTER_PATH
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_PRIMARY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RATING
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RELEASE_DATE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_RESULTS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_SECONDARY
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_TITLE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_TOTAL_PAGES
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_TOTAL_RESULTS
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_VIDEO
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_VOTE_AVERAGE
import org.studioapp.cinemy.data.model.StringConstants.SERIALIZED_VOTE_COUNT
import org.studioapp.cinemy.data.model.StringConstants.UNKNOWN_MOVIE_TITLE
import org.studioapp.cinemy.data.model.StringConstants.VERSION_2_0_0
import org.studioapp.cinemy.data.model.TextConfiguration
import org.studioapp.cinemy.data.model.UiConfiguration
import org.studioapp.cinemy.data.remote.dto.ButtonConfigurationDto
import org.studioapp.cinemy.data.remote.dto.ColorMetadataDto
import org.studioapp.cinemy.data.remote.dto.ColorSchemeDto
import org.studioapp.cinemy.data.remote.dto.GeminiColorsDto
import org.studioapp.cinemy.data.remote.dto.GenreDto
import org.studioapp.cinemy.data.remote.dto.MetaDto
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
            rating = (movieData[SERIALIZED_VOTE_AVERAGE] as? Number)?.toDouble()
                ?: DEFAULT_DOUBLE_VALUE,
            voteCount = (movieData[SERIALIZED_VOTE_COUNT] as? Number)?.toInt() ?: DEFAULT_INT_VALUE,
            releaseDate = movieData[SERIALIZED_RELEASE_DATE] as? String ?: EMPTY_STRING,
            genreIds = (movieData[SERIALIZED_GENRE_IDS] as? List<Number>)?.map { it.toInt() }
                ?: emptyList(),
            popularity = (movieData[SERIALIZED_POPULARITY] as? Number)?.toDouble()
                ?: DEFAULT_DOUBLE_VALUE,
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
                    modelUsed = metadataData?.get(SERIALIZED_MODEL_USED) as? Boolean
                        ?: DEFAULT_BOOLEAN_VALUE,
                    rating = (metadataData?.get(SERIALIZED_RATING) as? Number)?.toDouble()
                        ?: DEFAULT_DOUBLE_VALUE
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
    fun mapJsonToMovieDetailsDto(
        movieDetailsData: Map<String, Any>,
        movieId: Int
    ): MovieDetailsDto {
        return MovieDetailsDto(
            id = (movieDetailsData[FIELD_ID] as? Number)?.toInt() ?: movieId,
            title = movieDetailsData[FIELD_TITLE] as? String ?: EMPTY_STRING,
            description = movieDetailsData[FIELD_DESCRIPTION] as? String ?: EMPTY_STRING,
            posterPath = movieDetailsData[FIELD_POSTER_PATH] as? String,
            backdropPath = movieDetailsData[FIELD_BACKDROP_PATH] as? String,
            rating = (movieDetailsData[FIELD_RATING] as? Number)?.toDouble()
                ?: DEFAULT_DOUBLE_VALUE,
            voteCount = (movieDetailsData[FIELD_VOTE_COUNT] as? Number)?.toInt()
                ?: DEFAULT_INT_VALUE,
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

    /**
     * Maps JSON to UiConfigurationDto
     * @param colorsJson JSON object containing color configuration
     * @param textsJson JSON object containing text configuration
     * @param buttonsJson JSON object containing button configuration
     * @return UiConfigurationDto with parsed configuration
     */
    fun mapJsonToUiConfigurationDto(
        colorsJson: org.json.JSONObject?,
        textsJson: org.json.JSONObject?,
        buttonsJson: org.json.JSONObject?
    ): UiConfigurationDto {
        return UiConfigurationDto(
            colors = if (colorsJson != null) {
                mapJsonToColorSchemeDto(colorsJson)
            } else {
                createDefaultColorSchemeDto()
            },
            texts = if (textsJson != null) {
                mapJsonToTextConfigurationDto(textsJson)
            } else {
                createDefaultTextConfigurationDto()
            },
            buttons = if (buttonsJson != null) {
                mapJsonToButtonConfigurationDto(buttonsJson)
            } else {
                createDefaultButtonConfigurationDto()
            }
        )
    }

    /**
     * Maps JSON to ColorSchemeDto
     * @param colorsJson JSON object containing color configuration
     * @return ColorSchemeDto with parsed colors
     */
    private fun mapJsonToColorSchemeDto(colorsJson: org.json.JSONObject): ColorSchemeDto {
        return ColorSchemeDto(
            primary = colorsJson.optString(FIELD_PRIMARY, COLOR_PRIMARY),
            secondary = colorsJson.optString(FIELD_SECONDARY, COLOR_SECONDARY),
            background = colorsJson.optString(FIELD_BACKGROUND, COLOR_BACKGROUND),
            surface = colorsJson.optString(FIELD_SURFACE, COLOR_SURFACE),
            onPrimary = colorsJson.optString(FIELD_ON_PRIMARY, COLOR_ON_PRIMARY),
            onSecondary = colorsJson.optString(FIELD_ON_SECONDARY, COLOR_ON_SECONDARY),
            onBackground = colorsJson.optString(FIELD_ON_BACKGROUND, COLOR_ON_BACKGROUND),
            onSurface = colorsJson.optString(FIELD_ON_SURFACE, COLOR_ON_SURFACE),
            moviePosterColors = parseMoviePosterColorsFromJson(
                colorsJson.optJSONArray(
                    FIELD_MOVIE_POSTER_COLORS
                )
            )
        )
    }

    /**
     * Maps JSON to TextConfigurationDto
     * @param textsJson JSON object containing text configuration
     * @return TextConfigurationDto with parsed texts
     */
    private fun mapJsonToTextConfigurationDto(textsJson: org.json.JSONObject): TextConfigurationDto {
        return TextConfigurationDto(
            appTitle = textsJson.optString(FIELD_APP_TITLE, MOVIES_TITLE),
            loadingText = textsJson.optString(FIELD_LOADING_TEXT, LOADING_MOVIES_TEXT),
            errorMessage = textsJson.optString(FIELD_ERROR_MESSAGE, ERROR_UNKNOWN),
            noMoviesFound = textsJson.optString(FIELD_NO_MOVIES_FOUND, NO_MOVIES_FOUND),
            retryButton = textsJson.optString(FIELD_RETRY_BUTTON, RETRY_BUTTON),
            backButton = textsJson.optString(FIELD_BACK_BUTTON, BACK_BUTTON),
            playButton = textsJson.optString(FIELD_PLAY_BUTTON, PLAY_BUTTON)
        )
    }

    /**
     * Maps JSON to ButtonConfigurationDto
     * @param buttonsJson JSON object containing button configuration
     * @return ButtonConfigurationDto with parsed button settings
     */
    private fun mapJsonToButtonConfigurationDto(buttonsJson: org.json.JSONObject): ButtonConfigurationDto {
        return ButtonConfigurationDto(
            primaryButtonColor = buttonsJson.optString(FIELD_PRIMARY_BUTTON_COLOR, COLOR_PRIMARY),
            secondaryButtonColor = buttonsJson.optString(
                FIELD_SECONDARY_BUTTON_COLOR,
                COLOR_SECONDARY
            ),
            buttonTextColor = buttonsJson.optString(FIELD_BUTTON_TEXT_COLOR, COLOR_BUTTON_TEXT),
            buttonCornerRadius = buttonsJson.optInt(
                FIELD_BUTTON_CORNER_RADIUS,
                BUTTON_CORNER_RADIUS
            )
        )
    }

    /**
     * Parses movie poster colors from JSON array
     * @param colorsArray JSON array containing color strings
     * @return List of color strings
     */
    private fun parseMoviePosterColorsFromJson(colorsArray: org.json.JSONArray?): List<String> {
        if (colorsArray == null) return emptyList()

        val colors = mutableListOf<String>()
        for (i in 0 until colorsArray.length()) {
            val color = colorsArray.optString(i)
            if (color.isNotEmpty()) {
                colors.add(color)
            }
        }
        return colors
    }

    /**
     * Creates default ColorSchemeDto
     * @return ColorSchemeDto with default values
     */
    private fun createDefaultColorSchemeDto(): ColorSchemeDto {
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
     * Creates default TextConfigurationDto
     * @return TextConfigurationDto with default values
     */
    private fun createDefaultTextConfigurationDto(): TextConfigurationDto {
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
     * Creates default ButtonConfigurationDto
     * @return ButtonConfigurationDto with default values
     */
    private fun createDefaultButtonConfigurationDto(): ButtonConfigurationDto {
        return ButtonConfigurationDto(
            primaryButtonColor = COLOR_PRIMARY,
            secondaryButtonColor = COLOR_SECONDARY,
            buttonTextColor = COLOR_BUTTON_TEXT,
            buttonCornerRadius = BUTTON_CORNER_RADIUS
        )
    }

    /**
     * Maps JSON array to list of MovieDto objects
     * @param moviesJson JSON array containing movie data
     * @return List of MovieDto objects
     */
    fun mapJsonArrayToMovieDtoList(moviesJson: org.json.JSONArray): List<MovieDto> {
        val movies = mutableListOf<MovieDto>()
        for (i in 0 until moviesJson.length()) {
            val movieJson = moviesJson.getJSONObject(i)
            movies.add(mapJsonToMovieDtoFromAsset(movieJson))
        }
        return movies
    }

    /**
     * Maps JSON object to MovieDto (for asset data)
     * @param movieJson JSON object containing movie data
     * @return MovieDto with parsed movie information
     */
    private fun mapJsonToMovieDtoFromAsset(movieJson: org.json.JSONObject): MovieDto {
        return MovieDto(
            id = movieJson.optInt(FIELD_ID, 0),
            title = movieJson.optString(FIELD_TITLE, UNKNOWN_MOVIE_TITLE),
            description = movieJson.optString(FIELD_DESCRIPTION, NO_DESCRIPTION_AVAILABLE)
                .takeIf { it.isNotEmpty() } ?: movieJson.optString(
                SERIALIZED_OVERVIEW,
                NO_DESCRIPTION_AVAILABLE
            ),
            posterPath = movieJson.optString(FIELD_POSTER_PATH, null)
                ?: movieJson.optString(SERIALIZED_POSTER_PATH, null) ?: EMPTY_STRING,
            backdropPath = movieJson.optString(FIELD_BACKDROP_PATH, null)
                ?: movieJson.optString(SERIALIZED_BACKDROP_PATH, null) ?: EMPTY_STRING,
            rating = movieJson.optDouble(FIELD_RATING, DEFAULT_DOUBLE_VALUE)
                .takeIf { it > 0 } ?: movieJson.optDouble(
                SERIALIZED_VOTE_AVERAGE,
                DEFAULT_DOUBLE_VALUE
            ),
            voteCount = movieJson.optInt(FIELD_VOTE_COUNT, DEFAULT_INT_VALUE)
                .takeIf { it > 0 } ?: movieJson.optInt(SERIALIZED_VOTE_COUNT, DEFAULT_INT_VALUE),
            releaseDate = movieJson.optString(FIELD_RELEASE_DATE, null)
                ?: movieJson.optString(SERIALIZED_RELEASE_DATE, null) ?: EMPTY_STRING,
            genreIds = parseGenreIdsFromJson(movieJson.optJSONArray(FIELD_GENRE_IDS))
                .takeIf { it.isNotEmpty() } ?: parseGenreIdsFromJson(
                movieJson.optJSONArray(
                    SERIALIZED_GENRE_IDS
                )
            ),
            popularity = movieJson.optDouble(FIELD_POPULARITY, DEFAULT_DOUBLE_VALUE)
                .takeIf { it > 0 } ?: movieJson.optDouble(
                SERIALIZED_POPULARITY,
                DEFAULT_DOUBLE_VALUE
            ),
            adult = movieJson.optBoolean(FIELD_ADULT, DEFAULT_BOOLEAN_VALUE)
                .takeIf { movieJson.has(FIELD_ADULT) } ?: movieJson.optBoolean(
                SERIALIZED_ADULT,
                DEFAULT_BOOLEAN_VALUE
            ),
            originalLanguage = movieJson.optString(FIELD_ORIGINAL_LANGUAGE, null)
                ?: movieJson.optString(SERIALIZED_ORIGINAL_LANGUAGE, null) ?: LANGUAGE_EN,
            originalTitle = movieJson.optString(FIELD_ORIGINAL_TITLE, null)
                ?: movieJson.optString(SERIALIZED_ORIGINAL_TITLE, null)
                ?: movieJson.optString(FIELD_TITLE, UNKNOWN_MOVIE_TITLE),
            video = movieJson.optBoolean(FIELD_VIDEO, DEFAULT_BOOLEAN_VALUE)
                .takeIf { movieJson.has(FIELD_VIDEO) } ?: movieJson.optBoolean(
                SERIALIZED_VIDEO,
                DEFAULT_BOOLEAN_VALUE
            ),
            colors = mapJsonToMovieColorsDto(movieJson)
        )
    }

    /**
     * Maps JSON to MovieColorsDto
     * @param movieJson JSON object containing movie data
     * @return MovieColorsDto with parsed color information
     */
    private fun mapJsonToMovieColorsDto(movieJson: org.json.JSONObject): MovieColorsDto {
        val colorsJson = movieJson.optJSONObject(FIELD_COLORS)
        return if (colorsJson != null) {
            val metadataJson = colorsJson.optJSONObject(MOVIE_COLORS_METADATA)
            MovieColorsDto(
                accent = colorsJson.optString(FIELD_ACCENT, DEFAULT_MOVIE_ACCENT_COLOR),
                primary = colorsJson.optString(FIELD_PRIMARY, DEFAULT_MOVIE_PRIMARY_COLOR),
                secondary = colorsJson.optString(FIELD_SECONDARY, DEFAULT_MOVIE_SECONDARY_COLOR),
                metadata = ColorMetadataDto(
                    category = metadataJson?.optString(MOVIE_COLORS_CATEGORY, CATEGORY_MEDIUM)
                        ?: CATEGORY_MEDIUM,
                    modelUsed = metadataJson?.optBoolean(MOVIE_COLORS_MODEL_USED, true) ?: true,
                    rating = metadataJson?.optDouble(FIELD_RATING, DEFAULT_DOUBLE_VALUE)
                        ?: DEFAULT_DOUBLE_VALUE
                )
            )
        } else {
            // Default colors if not present
            MovieColorsDto(
                accent = DEFAULT_MOVIE_ACCENT_COLOR,
                primary = DEFAULT_MOVIE_PRIMARY_COLOR,
                secondary = DEFAULT_MOVIE_SECONDARY_COLOR,
                metadata = ColorMetadataDto(
                    category = MOVIE_COLORS_CATEGORY,
                    modelUsed = true,
                    rating = movieJson.optDouble(FIELD_RATING, DEFAULT_DOUBLE_VALUE)
                        .takeIf { it > 0 } ?: movieJson.optDouble(
                        SERIALIZED_VOTE_AVERAGE,
                        DEFAULT_DOUBLE_VALUE
                    )
                )
            )
        }
    }

    /**
     * Parses genre IDs from JSON array
     * @param genreIdsArray JSON array containing genre IDs
     * @return List of genre IDs
     */
    private fun parseGenreIdsFromJson(genreIdsArray: org.json.JSONArray?): List<Int> {
        if (genreIdsArray == null) return emptyList()

        val genreIds = mutableListOf<Int>()
        for (i in 0 until genreIdsArray.length()) {
            val genreId = genreIdsArray.optInt(i, -1)
            if (genreId > 0) {
                genreIds.add(genreId)
            }
        }
        return genreIds
    }

    /**
     * Maps JSON to MetaDto
     * @param metaJson JSON object containing metadata
     * @param method Method name
     * @param resultsCount Number of results
     * @param movieId Movie ID (optional)
     * @return MetaDto with parsed metadata
     */
    fun mapJsonToMetaDto(
        metaJson: org.json.JSONObject,
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
            searchQuery = metaJson.optString(FIELD_SEARCH_QUERY, null) ?: EMPTY_STRING,
            movieId = movieId,
            resultsCount = resultsCount,
            aiGenerated = metaJson.optBoolean(FIELD_AI_GENERATED, true),
            geminiColors = if (geminiColorsJson != null) {
                mapJsonToGeminiColorsDto(geminiColorsJson)
            } else {
                createDefaultGeminiColorsDto()
            },
            avgRating = metaJson.optDouble(FIELD_AVG_RATING, DEFAULT_DOUBLE_VALUE)
                .takeIf { it > 0 },
            movieRating = metaJson.optDouble(FIELD_MOVIE_RATING, DEFAULT_DOUBLE_VALUE)
                .takeIf { it > 0 },
            version = metaJson.optString(FIELD_VERSION, VERSION_2_0_0)
        )
    }

    /**
     * Maps JSON to GeminiColorsDto
     * @param geminiColorsJson JSON object containing Gemini colors
     * @return GeminiColorsDto with parsed color information
     */
    private fun mapJsonToGeminiColorsDto(geminiColorsJson: org.json.JSONObject): GeminiColorsDto {
        return GeminiColorsDto(
            primary = geminiColorsJson.optString(FIELD_PRIMARY, COLOR_PRIMARY),
            secondary = geminiColorsJson.optString(FIELD_SECONDARY, COLOR_SECONDARY),
            accent = geminiColorsJson.optString(FIELD_ACCENT, COLOR_ACCENT)
        )
    }

    /**
     * Creates default GeminiColorsDto
     * @return GeminiColorsDto with default values
     */
    private fun createDefaultGeminiColorsDto(): GeminiColorsDto {
        return GeminiColorsDto(
            primary = COLOR_PRIMARY,
            secondary = COLOR_SECONDARY,
            accent = COLOR_ACCENT
        )
    }

    /**
     * Creates default MetaDto
     * @param method API method name
     * @param resultsCount Number of results returned
     * @param movieId Movie ID (optional)
     * @return MetaDto with default metadata values
     */
    fun createDefaultMetaDto(method: String, resultsCount: Int, movieId: Int?): MetaDto {
        return MetaDto(
            timestamp = System.currentTimeMillis().toString(),
            method = method,
            searchQuery = null,
            movieId = movieId,
            resultsCount = resultsCount,
            aiGenerated = true,
            geminiColors = createDefaultGeminiColorsDto(),
            avgRating = null,
            movieRating = null,
            version = VERSION_2_0_0
        )
    }

    /**
     * Maps backend UI configuration JSON to UiConfigurationDto
     * @param backendUiConfig Map containing backend UI configuration
     * @return UiConfigurationDto with parsed UI configuration
     */
    fun mapBackendUiConfigToUiConfigurationDto(backendUiConfig: Map<String, Any>): UiConfigurationDto {
        val colors = backendUiConfig[JSON_FIELD_COLORS] as? Map<String, Any>
        val texts = backendUiConfig[JSON_FIELD_TEXTS] as? Map<String, Any>
        val buttons = backendUiConfig[JSON_FIELD_BUTTONS] as? Map<String, Any>

        return if (colors != null && texts != null && buttons != null) {
            UiConfigurationDto(
                colors = mapBackendColorsToColorSchemeDto(colors),
                texts = mapBackendTextsToTextConfigurationDto(texts),
                buttons = mapBackendButtonsToButtonConfigurationDto(buttons)
            )
        } else {
            createDefaultUiConfigurationDto()
        }
    }

    /**
     * Maps backend colors to ColorSchemeDto
     * @param colors Map containing color data
     * @return ColorSchemeDto with parsed color information
     */
    private fun mapBackendColorsToColorSchemeDto(colors: Map<String, Any>): ColorSchemeDto {
        return ColorSchemeDto(
            primary = colors[JSON_FIELD_PRIMARY] as? String ?: DEFAULT_BACKEND_PRIMARY_COLOR,
            secondary = colors[JSON_FIELD_SECONDARY] as? String ?: DEFAULT_BACKEND_SECONDARY_COLOR,
            background = colors[JSON_FIELD_BACKGROUND] as? String
                ?: DEFAULT_BACKEND_BACKGROUND_COLOR,
            surface = colors[JSON_FIELD_SURFACE] as? String ?: DEFAULT_BACKEND_SURFACE_COLOR,
            onPrimary = colors[JSON_FIELD_ON_PRIMARY] as? String
                ?: DEFAULT_BACKEND_ON_PRIMARY_COLOR,
            onSecondary = colors[JSON_FIELD_ON_SECONDARY] as? String
                ?: DEFAULT_BACKEND_ON_SECONDARY_COLOR,
            onBackground = colors[JSON_FIELD_ON_BACKGROUND] as? String
                ?: DEFAULT_BACKEND_ON_BACKGROUND_COLOR,
            onSurface = colors[JSON_FIELD_ON_SURFACE] as? String
                ?: DEFAULT_BACKEND_ON_SURFACE_COLOR,
            moviePosterColors = (colors[JSON_FIELD_MOVIE_POSTER_COLORS] as? List<*>)?.mapNotNull { it as? String }
                ?: emptyList()
        )
    }

    /**
     * Maps backend texts to TextConfigurationDto
     * @param texts Map containing text data
     * @return TextConfigurationDto with parsed text information
     */
    private fun mapBackendTextsToTextConfigurationDto(texts: Map<String, Any>): TextConfigurationDto {
        return TextConfigurationDto(
            appTitle = texts[JSON_FIELD_APP_TITLE] as? String ?: DEFAULT_BACKEND_APP_TITLE,
            loadingText = texts[JSON_FIELD_LOADING_TEXT] as? String ?: DEFAULT_BACKEND_LOADING_TEXT,
            errorMessage = texts[JSON_FIELD_ERROR_MESSAGE] as? String
                ?: DEFAULT_BACKEND_ERROR_MESSAGE,
            noMoviesFound = texts[JSON_FIELD_NO_MOVIES_FOUND] as? String
                ?: DEFAULT_BACKEND_NO_MOVIES_FOUND,
            retryButton = texts[JSON_FIELD_RETRY_BUTTON] as? String ?: DEFAULT_BACKEND_RETRY_BUTTON,
            backButton = texts[JSON_FIELD_BACK_BUTTON] as? String ?: DEFAULT_BACKEND_BACK_BUTTON,
            playButton = texts[JSON_FIELD_PLAY_BUTTON] as? String ?: DEFAULT_BACKEND_PLAY_BUTTON
        )
    }

    /**
     * Maps backend buttons to ButtonConfigurationDto
     * @param buttons Map containing button data
     * @return ButtonConfigurationDto with parsed button information
     */
    private fun mapBackendButtonsToButtonConfigurationDto(buttons: Map<String, Any>): ButtonConfigurationDto {
        return ButtonConfigurationDto(
            primaryButtonColor = buttons[JSON_FIELD_PRIMARY_BUTTON_COLOR] as? String
                ?: DEFAULT_BACKEND_PRIMARY_COLOR,
            secondaryButtonColor = buttons[JSON_FIELD_SECONDARY_BUTTON_COLOR] as? String
                ?: DEFAULT_BACKEND_SECONDARY_COLOR,
            buttonTextColor = buttons[JSON_FIELD_BUTTON_TEXT_COLOR] as? String
                ?: DEFAULT_BACKEND_ON_PRIMARY_COLOR,
            buttonCornerRadius = (buttons[JSON_FIELD_BUTTON_CORNER_RADIUS] as? Number)?.toInt()
                ?: DEFAULT_BACKEND_BUTTON_CORNER_RADIUS
        )
    }

    /**
     * Creates default UiConfigurationDto
     * @return UiConfigurationDto with default values
     */
    private fun createDefaultUiConfigurationDto(): UiConfigurationDto {
        return UiConfigurationDto(
            colors = ColorSchemeDto(
                primary = DEFAULT_BACKEND_PRIMARY_COLOR,
                secondary = DEFAULT_BACKEND_SECONDARY_COLOR,
                background = DEFAULT_BACKEND_BACKGROUND_COLOR,
                surface = DEFAULT_BACKEND_SURFACE_COLOR,
                onPrimary = DEFAULT_BACKEND_ON_PRIMARY_COLOR,
                onSecondary = DEFAULT_BACKEND_ON_SECONDARY_COLOR,
                onBackground = DEFAULT_BACKEND_ON_BACKGROUND_COLOR,
                onSurface = DEFAULT_BACKEND_ON_SURFACE_COLOR,
                moviePosterColors = emptyList()
            ),
            texts = TextConfigurationDto(
                appTitle = DEFAULT_BACKEND_APP_TITLE,
                loadingText = DEFAULT_BACKEND_LOADING_TEXT,
                errorMessage = DEFAULT_BACKEND_ERROR_MESSAGE,
                noMoviesFound = DEFAULT_BACKEND_NO_MOVIES_FOUND,
                retryButton = DEFAULT_BACKEND_RETRY_BUTTON,
                backButton = DEFAULT_BACKEND_BACK_BUTTON,
                playButton = DEFAULT_BACKEND_PLAY_BUTTON
            ),
            buttons = ButtonConfigurationDto(
                primaryButtonColor = DEFAULT_BACKEND_PRIMARY_COLOR,
                secondaryButtonColor = DEFAULT_BACKEND_SECONDARY_COLOR,
                buttonTextColor = DEFAULT_BACKEND_ON_PRIMARY_COLOR,
                buttonCornerRadius = DEFAULT_BACKEND_BUTTON_CORNER_RADIUS
            )
        )
    }

    /**
     * Maps backend movie details JSON to MovieDetailsDto
     * @param innerData Map containing movie details data
     * @param movieId Movie ID
     * @return MovieDetailsDto with parsed movie details
     */
    fun mapBackendMovieDetailsToMovieDetailsDto(
        innerData: Map<String, Any>,
        movieId: Int
    ): MovieDetailsDto {
        val movieDetailsData = innerData[FIELD_MOVIE_DETAILS] as? Map<String, Any>
        val sentimentReviewsData = innerData[FIELD_SENTIMENT_REVIEWS] as? Map<String, Any>
        val sentimentMetadataData = innerData[FIELD_SENTIMENT_METADATA] as? Map<String, Any>

        val sentimentReviews = sentimentReviewsData?.let { reviewsData ->
            mapBackendSentimentReviewsToSentimentReviewsDto(reviewsData)
        }

        val sentimentMetadata = sentimentMetadataData?.let { metadataData ->
            mapBackendSentimentMetadataToSentimentMetadataDto(metadataData)
        }

        return MovieDetailsDto(
            id = (movieDetailsData?.get(FIELD_ID) as? Number)?.toInt() ?: movieId,
            title = movieDetailsData?.get(FIELD_TITLE) as? String ?: EMPTY_STRING,
            description = movieDetailsData?.get(FIELD_DESCRIPTION) as? String ?: EMPTY_STRING,
            posterPath = movieDetailsData?.get(FIELD_POSTER_PATH) as? String,
            backdropPath = movieDetailsData?.get(FIELD_BACKDROP_PATH) as? String,
            rating = (movieDetailsData?.get(FIELD_RATING) as? Number)?.toDouble()
                ?: DEFAULT_DOUBLE_VALUE,
            voteCount = (movieDetailsData?.get(FIELD_VOTE_COUNT) as? Number)?.toInt()
                ?: DEFAULT_INT_VALUE,
            releaseDate = movieDetailsData?.get(FIELD_RELEASE_DATE) as? String ?: EMPTY_STRING,
            runtime = (movieDetailsData?.get(FIELD_RUNTIME) as? Number)?.toInt()
                ?: DEFAULT_INT_VALUE,
            genres = mapBackendGenresToGenreDtoList(movieDetailsData?.get(FIELD_GENRES) as? List<Map<String, Any>>),
            productionCompanies = mapBackendProductionCompaniesToProductionCompanyDtoList(
                movieDetailsData?.get(FIELD_PRODUCTION_COMPANIES) as? List<Map<String, Any>>
            ),
            budget = (movieDetailsData?.get(FIELD_BUDGET) as? Number)?.toLong()
                ?: DEFAULT_LONG_VALUE,
            revenue = (movieDetailsData?.get(FIELD_REVENUE) as? Number)?.toLong()
                ?: DEFAULT_LONG_VALUE,
            status = movieDetailsData?.get(FIELD_STATUS) as? String ?: EMPTY_STRING,
            sentimentReviews = sentimentReviews,
            sentimentMetadata = sentimentMetadata
        )
    }

    /**
     * Maps backend sentiment reviews to SentimentReviewsDto
     * @param reviewsData Map containing sentiment reviews data
     * @return SentimentReviewsDto with parsed sentiment reviews
     */
    private fun mapBackendSentimentReviewsToSentimentReviewsDto(reviewsData: Map<String, Any>): SentimentReviewsDto {
        return SentimentReviewsDto(
            positive = (reviewsData[FIELD_POSITIVE] as? List<String>) ?: emptyList(),
            negative = (reviewsData[FIELD_NEGATIVE] as? List<String>) ?: emptyList()
        )
    }

    /**
     * Maps backend sentiment metadata to SentimentMetadataDto
     * @param metadataData Map containing sentiment metadata
     * @return SentimentMetadataDto with parsed sentiment metadata
     */
    private fun mapBackendSentimentMetadataToSentimentMetadataDto(metadataData: Map<String, Any>): SentimentMetadataDto {
        return SentimentMetadataDto(
            totalReviews = (metadataData[FIELD_TOTAL_REVIEWS] as? Number)?.toInt() ?: 0,
            positiveCount = (metadataData[FIELD_POSITIVE_COUNT] as? Number)?.toInt() ?: 0,
            negativeCount = (metadataData[FIELD_NEGATIVE_COUNT] as? Number)?.toInt() ?: 0,
            source = metadataData[FIELD_SOURCE] as? String ?: EMPTY_STRING,
            timestamp = metadataData[FIELD_TIMESTAMP] as? String ?: EMPTY_STRING,
            apiSuccess = (metadataData[FIELD_API_SUCCESS] as? Map<String, Boolean>) ?: emptyMap()
        )
    }

    /**
     * Maps backend genres to GenreDto list
     * @param genresData List of genre data maps
     * @return List of GenreDto objects
     */
    private fun mapBackendGenresToGenreDtoList(genresData: List<Map<String, Any>>?): List<GenreDto> {
        return genresData?.map { genreData ->
            GenreDto(
                id = (genreData[FIELD_ID] as? Number)?.toInt() ?: DEFAULT_INT_VALUE,
                name = genreData[FIELD_NAME] as? String ?: EMPTY_STRING
            )
        } ?: emptyList()
    }

    /**
     * Maps backend production companies to ProductionCompanyDto list
     * @param companiesData List of production company data maps
     * @return List of ProductionCompanyDto objects
     */
    private fun mapBackendProductionCompaniesToProductionCompanyDtoList(
        companiesData: List<Map<String, Any>>?
    ): List<ProductionCompanyDto> {
        return companiesData?.map { companyData ->
            ProductionCompanyDto(
                id = (companyData[FIELD_ID] as? Number)?.toInt() ?: DEFAULT_INT_VALUE,
                logoPath = companyData[FIELD_LOGO_PATH] as? String,
                name = companyData[FIELD_NAME] as? String ?: EMPTY_STRING,
                originCountry = companyData[FIELD_ORIGIN_COUNTRY] as? String ?: EMPTY_STRING
            )
        } ?: emptyList()
    }

    /**
     * Maps backend movie list JSON to MovieDto list
     * @param moviesData List of movie data maps
     * @return List of MovieDto objects
     */
    fun mapBackendMovieListToMovieDtoList(moviesData: List<Map<String, Any>>?): List<MovieDto> {
        return moviesData?.map { movieData ->
            mapBackendMovieToMovieDto(movieData)
        } ?: emptyList()
    }

    /**
     * Maps backend movie JSON to MovieDto
     * @param movieData Map containing movie data
     * @return MovieDto with parsed movie information
     */
    private fun mapBackendMovieToMovieDto(movieData: Map<String, Any>): MovieDto {
        val colorsData = movieData[SERIALIZED_COLORS] as? Map<String, Any>
        val metadataData = colorsData?.get(SERIALIZED_METADATA) as? Map<String, Any>

        return MovieDto(
            id = (movieData[SERIALIZED_ID] as? Number)?.toInt() ?: DEFAULT_INT_VALUE,
            title = movieData[SERIALIZED_TITLE] as? String ?: EMPTY_STRING,
            description = movieData[SERIALIZED_OVERVIEW] as? String ?: EMPTY_STRING,
            posterPath = movieData[SERIALIZED_POSTER_PATH] as? String,
            backdropPath = movieData[SERIALIZED_BACKDROP_PATH] as? String,
            rating = (movieData[SERIALIZED_VOTE_AVERAGE] as? Number)?.toDouble()
                ?: DEFAULT_DOUBLE_VALUE,
            voteCount = (movieData[SERIALIZED_VOTE_COUNT] as? Number)?.toInt() ?: DEFAULT_INT_VALUE,
            releaseDate = movieData[SERIALIZED_RELEASE_DATE] as? String ?: EMPTY_STRING,
            genreIds = (movieData[SERIALIZED_GENRE_IDS] as? List<Number>)?.map { it.toInt() }
                ?: emptyList(),
            popularity = (movieData[SERIALIZED_POPULARITY] as? Number)?.toDouble()
                ?: DEFAULT_DOUBLE_VALUE,
            adult = movieData[SERIALIZED_ADULT] as? Boolean ?: DEFAULT_BOOLEAN_VALUE,
            originalLanguage = movieData[SERIALIZED_ORIGINAL_LANGUAGE] as? String ?: EMPTY_STRING,
            originalTitle = movieData[SERIALIZED_ORIGINAL_TITLE] as? String ?: EMPTY_STRING,
            video = movieData[SERIALIZED_VIDEO] as? Boolean ?: DEFAULT_BOOLEAN_VALUE,
            colors = mapBackendMovieColorsToMovieColorsDto(colorsData, metadataData)
        )
    }

    /**
     * Maps backend movie colors to MovieColorsDto
     * @param colorsData Map containing color data
     * @param metadataData Map containing metadata
     * @return MovieColorsDto with parsed color information
     */
    private fun mapBackendMovieColorsToMovieColorsDto(
        colorsData: Map<String, Any>?,
        metadataData: Map<String, Any>?
    ): MovieColorsDto {
        return MovieColorsDto(
            accent = colorsData?.get(SERIALIZED_ACCENT) as? String ?: EMPTY_STRING,
            primary = colorsData?.get(SERIALIZED_PRIMARY) as? String ?: EMPTY_STRING,
            secondary = colorsData?.get(SERIALIZED_SECONDARY) as? String ?: EMPTY_STRING,
            metadata = ColorMetadataDto(
                category = metadataData?.get(SERIALIZED_CATEGORY) as? String ?: EMPTY_STRING,
                modelUsed = metadataData?.get(SERIALIZED_MODEL_USED) as? Boolean
                    ?: DEFAULT_BOOLEAN_VALUE,
                rating = (metadataData?.get(SERIALIZED_RATING) as? Number)?.toDouble()
                    ?: DEFAULT_DOUBLE_VALUE
            )
        )
    }

    /**
     * Maps backend movie list response JSON to MovieListResponseDto
     * @param data Map containing response data
     * @param page Current page number
     * @return MovieListResponseDto with parsed response information
     */
    fun mapBackendMovieListResponseToMovieListResponseDto(
        data: Map<String, Any>,
        page: Int
    ): MovieListResponseDto {
        val movies =
            mapBackendMovieListToMovieDtoList(data[SERIALIZED_RESULTS] as? List<Map<String, Any>>)
        val backendPage = (data[SERIALIZED_PAGE] as? Number)?.toInt() ?: page
        val backendTotalPages =
            (data[SERIALIZED_TOTAL_PAGES] as? Number)?.toInt() ?: DEFAULT_TOTAL_PAGES
        val backendTotalResults =
            (data[SERIALIZED_TOTAL_RESULTS] as? Number)?.toInt() ?: DEFAULT_INT_VALUE

        return MovieListResponseDto(
            page = backendPage,
            results = movies,
            totalPages = backendTotalPages,
            totalResults = backendTotalResults
        )
    }

    /**
     * Maps backend movie details response to complete domain models
     * @param innerData Map containing movie details data
     * @param movieId Movie ID
     * @param backendUiConfig Optional backend UI configuration
     * @param assetDataLoader Asset data loader for fallback
     * @return Triple containing domain movie details, sentiment reviews, sentiment metadata, and UI config
     */
    fun mapBackendMovieDetailsResponseToDomainModels(
        innerData: Map<String, Any>,
        movieId: Int,
        backendUiConfig: Map<String, Any>?,
        assetDataLoader: org.studioapp.cinemy.data.mcp.AssetDataLoader
    ): kotlin.Triple<org.studioapp.cinemy.data.model.MovieDetails, org.studioapp.cinemy.data.model.SentimentReviews?, org.studioapp.cinemy.data.model.SentimentMetadata?> {
        val movieDetails = mapBackendMovieDetailsToMovieDetailsDto(innerData, movieId)
        val domainMovieDetails = mapMovieDetailsDtoToMovieDetails(movieDetails)

        val domainSentimentReviews = movieDetails.sentimentReviews?.let {
            mapSentimentReviewsDtoToSentimentReviews(it)
        }
        val domainSentimentMetadata = movieDetails.sentimentMetadata?.let {
            mapSentimentMetadataDtoToSentimentMetadata(it)
        }

        return kotlin.Triple(domainMovieDetails, domainSentimentReviews, domainSentimentMetadata)
    }

    /**
     * Maps backend UI configuration to domain UI configuration
     * @param backendUiConfig Backend UI configuration
     * @param assetDataLoader Asset data loader for fallback
     * @return Domain UI configuration
     */
    fun mapBackendUiConfigToDomainUiConfig(
        backendUiConfig: Map<String, Any>?,
        assetDataLoader: org.studioapp.cinemy.data.mcp.AssetDataLoader
    ): org.studioapp.cinemy.data.model.UiConfiguration {
        val uiConfig = if (backendUiConfig != null) {
            mapBackendUiConfigToUiConfigurationDto(backendUiConfig)
        } else {
            assetDataLoader.loadUiConfig()
        }
        return mapUiConfigurationDtoToUiConfiguration(uiConfig)
    }
}
