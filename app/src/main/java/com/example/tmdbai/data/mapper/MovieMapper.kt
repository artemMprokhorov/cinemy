package com.example.tmdbai.data.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.tmdbai.data.model.Movie
import com.example.tmdbai.data.model.MovieDetails
import com.example.tmdbai.data.model.MovieListResponse
import com.example.tmdbai.data.model.MovieDetailsResponse
import com.example.tmdbai.data.model.MovieDetailsData
import com.example.tmdbai.data.model.MovieListData
import com.example.tmdbai.data.model.Pagination
import com.example.tmdbai.data.model.Meta
import com.example.tmdbai.data.model.UiConfiguration
import com.example.tmdbai.data.model.ColorScheme
import com.example.tmdbai.data.model.TextConfiguration
import com.example.tmdbai.data.model.ButtonConfiguration
import com.example.tmdbai.data.model.GeminiColors
import com.example.tmdbai.data.model.Genre
import com.example.tmdbai.data.model.ProductionCompany
import com.example.tmdbai.data.model.SearchInfo
import com.example.tmdbai.data.model.Result
import com.example.tmdbai.data.remote.dto.MovieDto
import com.example.tmdbai.data.remote.dto.MovieDetailsDto
import com.example.tmdbai.data.remote.dto.McpMovieListResponseDto
import com.example.tmdbai.data.remote.dto.PaginationDto
import com.example.tmdbai.data.remote.dto.MetaDto
import com.example.tmdbai.data.remote.dto.UiConfigurationDto
import com.example.tmdbai.data.remote.dto.ColorSchemeDto
import com.example.tmdbai.data.remote.dto.TextConfigurationDto
import com.example.tmdbai.data.remote.dto.ButtonConfigurationDto
import com.example.tmdbai.data.remote.dto.GeminiColorsDto
import com.example.tmdbai.data.remote.dto.GenreDto
import com.example.tmdbai.data.remote.dto.ProductionCompanyDto
import com.example.tmdbai.data.remote.dto.SearchInfoDto
import com.example.tmdbai.data.remote.dto.McpResponseDto

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
            productionCompanies = dto.productionCompanies.map { mapProductionCompanyDtoToProductionCompany(it) },
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
                    primary = androidx.compose.ui.graphics.Color.Blue,
                    secondary = androidx.compose.ui.graphics.Color.Gray,
                    background = androidx.compose.ui.graphics.Color.Black,
                    surface = androidx.compose.ui.graphics.Color.DarkGray,
                    onPrimary = androidx.compose.ui.graphics.Color.White,
                    onSecondary = androidx.compose.ui.graphics.Color.White,
                    onBackground = androidx.compose.ui.graphics.Color.White,
                    onSurface = androidx.compose.ui.graphics.Color.White,
                    moviePosterColors = emptyList()
                ),
                texts = TextConfiguration(
                    appTitle = "Movies",
                    loadingText = "Loading...",
                    errorMessage = "Error",
                    noMoviesFound = "No movies found",
                    retryButton = "Retry",
                    backButton = "Back",
                    playButton = "Play"
                ),
                buttons = ButtonConfiguration(
                    primaryButtonColor = androidx.compose.ui.graphics.Color.Blue,
                    secondaryButtonColor = androidx.compose.ui.graphics.Color.Gray,
                    buttonTextColor = androidx.compose.ui.graphics.Color.White,
                    buttonCornerRadius = 8
                )
            ),
            error = null,
            meta = Meta(
                timestamp = System.currentTimeMillis().toString(),
                method = "getPopularMovies",
                searchQuery = null,
                movieId = null,
                resultsCount = dto.movies.size,
                aiGenerated = true,
                geminiColors = GeminiColors(
                    primary = "#2C5F6F",
                    secondary = "#4A90A4",
                    accent = "#FFD700"
                ),
                avgRating = null,
                movieRating = null,
                version = "2.0.0"
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
            primary = Color(android.graphics.Color.parseColor(dto.primary)),
            secondary = Color(android.graphics.Color.parseColor(dto.secondary)),
            background = Color(android.graphics.Color.parseColor(dto.background)),
            surface = Color(android.graphics.Color.parseColor(dto.surface)),
            onPrimary = Color(android.graphics.Color.parseColor(dto.onPrimary)),
            onSecondary = Color(android.graphics.Color.parseColor(dto.onSecondary)),
            onBackground = Color(android.graphics.Color.parseColor(dto.onBackground)),
            onSurface = Color(android.graphics.Color.parseColor(dto.onSurface)),
            moviePosterColors = dto.moviePosterColors.map { Color(android.graphics.Color.parseColor(it)) }
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
            primaryButtonColor = Color(android.graphics.Color.parseColor(dto.primaryButtonColor)),
            secondaryButtonColor = Color(android.graphics.Color.parseColor(dto.secondaryButtonColor)),
            buttonTextColor = Color(android.graphics.Color.parseColor(dto.buttonTextColor)),
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
            val uiConfig = mcpResponse.uiConfig?.let { mapUiConfigurationDtoToUiConfiguration(it) }
            Result.Success(dataMapper(mcpResponse.data), uiConfig)
        } else {
            val uiConfig = mcpResponse.uiConfig?.let { mapUiConfigurationDtoToUiConfiguration(it) }
            Result.Error(mcpResponse.error ?: "Unknown error", uiConfig)
        }
    }
}
