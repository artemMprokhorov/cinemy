package com.example.tmdbai.data.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.tmdbai.data.model.*
import com.example.tmdbai.data.remote.dto.*

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
            popularity = dto.popularity
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
            runtime = dto.runtime,
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
            name = dto.name,
            logoPath = dto.logoPath
        )
    }
    
    fun mapMcpMovieListResponseDtoToMovieListResponse(dto: McpMovieListResponseDto): MovieListResponse {
        return MovieListResponse(
            movies = dto.movies.map { mapMovieDtoToMovie(it) },
            pagination = mapPaginationDtoToPagination(dto.pagination)
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
            buttons = mapButtonConfigurationDtoToButtonConfiguration(dto.buttons)
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
