package com.example.tmdbai.data.mcp

import com.example.tmdbai.data.remote.api.MovieApiService
import com.example.tmdbai.data.remote.dto.*
import com.example.tmdbai.data.model.*
import com.example.tmdbai.data.mapper.MovieMapper
import com.google.gson.Gson
import kotlinx.coroutines.delay
import java.io.IOException

class McpClient : MovieApiService {
    
    private val gson = Gson()
    private val mcpHttpClient = McpHttpClient()
    
    override suspend fun getPopularMovies(page: Int): McpResponseDto<McpMovieListResponseDto> {
        return simulateNetworkCall {
            val mockMovies = generateMockMovies()
            val pagination = PaginationDto(
                page = page,
                totalPages = 10,
                totalResults = 100,
                hasNext = page < 10,
                hasPrevious = page > 1
            )
            
            McpMovieListResponseDto(mockMovies, pagination)
        }
    }
    
    override suspend fun getTopRatedMovies(page: Int): McpResponseDto<McpMovieListResponseDto> {
        return simulateNetworkCall {
            val mockMovies = generateMockMovies().map { it.copy(title = "Top Rated: ${it.title}") }
            val pagination = PaginationDto(
                page = page,
                totalPages = 8,
                totalResults = 80,
                hasNext = page < 8,
                hasPrevious = page > 1
            )
            
            McpMovieListResponseDto(mockMovies, pagination)
        }
    }
    
    override suspend fun getNowPlayingMovies(page: Int): McpResponseDto<McpMovieListResponseDto> {
        return simulateNetworkCall {
            val mockMovies = generateMockMovies().map { it.copy(title = "Now Playing: ${it.title}") }
            val pagination = PaginationDto(
                page = page,
                totalPages = 5,
                totalResults = 50,
                hasNext = page < 5,
                hasPrevious = page > 1
            )
            
            McpMovieListResponseDto(mockMovies, pagination)
        }
    }
    
    override suspend fun searchMovies(query: String, page: Int): McpResponseDto<McpMovieListResponseDto> {
        return simulateNetworkCall {
            val mockMovies = generateMockMovies().filter { 
                it.title.contains(query, ignoreCase = true) || 
                it.description.contains(query, ignoreCase = true)
            }
            val pagination = PaginationDto(
                page = page,
                totalPages = 3,
                totalResults = mockMovies.size,
                hasNext = false,
                hasPrevious = page > 1
            )
            
            McpMovieListResponseDto(mockMovies, pagination)
        }
    }
    
    override suspend fun getMovieDetails(movieId: Int): McpResponseDto<MovieDetailsDto> {
        return simulateNetworkCall {
            MovieDetailsDto(
                id = movieId,
                title = "The Midnight Bloom",
                description = "A young botanist discovers a rare, bioluminescent flower with the power to heal any ailment, but must protect it from those who seek to exploit its magic.",
                posterPath = "/poster_path.jpg",
                backdropPath = "/backdrop_path.jpg",
                rating = 8.5,
                voteCount = 1234,
                releaseDate = "2024-03-15",
                runtime = 120,
                genres = listOf(
                    GenreDto(1, "Drama"),
                    GenreDto(2, "Fantasy"),
                    GenreDto(3, "Adventure")
                ),
                productionCompanies = listOf(
                    ProductionCompanyDto(1, "/logo1.png", "Fantasy Films", "US"),
                    ProductionCompanyDto(2, "/logo2.png", "Nature Studios", "US")
                ),
                budget = 50000000,
                revenue = 150000000,
                status = "Released"
            )
        }
    }
    
    override suspend fun getMovieRecommendations(movieId: Int, page: Int): McpResponseDto<McpMovieListResponseDto> {
        return simulateNetworkCall {
            val mockMovies = generateMockMovies().map { it.copy(title = "Recommended: ${it.title}") }
            val pagination = PaginationDto(
                page = page,
                totalPages = 4,
                totalResults = 40,
                hasNext = page < 4,
                hasPrevious = page > 1
            )
            
            McpMovieListResponseDto(mockMovies, pagination)
        }
    }
    
    /**
     * Business method: Get popular movies using MCP HTTP client
     */
    suspend fun getPopularMoviesViaMcp(page: Int): Result<MovieListResponse> {
        return runCatching {
            val response = mcpHttpClient.sendMcpRequest<McpResponseDto<McpMovieListResponseDto>>(
                method = "getPopularMovies",
                params = mapOf("page" to page)
            )
            
            when (response) {
                is Result.Success -> {
                    val mcpResponse = response.data
                    if (mcpResponse.success && mcpResponse.data != null) {
                        val movieListResponse = MovieMapper.mapMcpMovieListResponseDtoToMovieListResponse(mcpResponse.data)
                        val uiConfig = mcpResponse.uiConfig?.let { MovieMapper.mapUiConfigurationDtoToUiConfiguration(it) }
                        Result.Success(
                            data = movieListResponse,
                            uiConfig = uiConfig
                        )
                    } else {
                        Result.Error(mcpResponse.error ?: "Failed to fetch popular movies")
                    }
                }
                is Result.Error -> Result.Error(response.message)
                is Result.Loading -> Result.Loading
            }
        }.getOrElse { exception ->
            Result.Error("Error fetching popular movies: ${exception.message ?: "Unknown error"}")
        }
    }
    
    /**
     * Business method: Get movie details using MCP HTTP client
     */
    suspend fun getMovieDetailsViaMcp(movieId: Int): Result<MovieDetailsResponse> {
        return runCatching {
            val response = mcpHttpClient.sendMcpRequest<McpResponseDto<MovieDetailsDto>>(
                method = "getMovieDetails",
                params = mapOf("movieId" to movieId)
            )
            
            when (response) {
                is Result.Success -> {
                    val mcpResponse = response.data
                    if (mcpResponse.success && mcpResponse.data != null) {
                        val movieDetails = MovieMapper.mapMovieDetailsDtoToMovieDetails(mcpResponse.data)
                        val uiConfig = mcpResponse.uiConfig?.let { MovieMapper.mapUiConfigurationDtoToUiConfiguration(it) }
                        Result.Success(
                            data = MovieDetailsResponse(
                                success = true,
                                data = MovieDetailsData(movieDetails = movieDetails),
                                uiConfig = uiConfig ?: UiConfiguration(
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
                                    method = "getMovieDetails",
                                    searchQuery = null,
                                    movieId = movieId,
                                    resultsCount = null,
                                    aiGenerated = true,
                                    geminiColors = GeminiColors(
                                        primary = "#2C5F6F",
                                        secondary = "#4A90A4",
                                        accent = "#FFD700"
                                    ),
                                    avgRating = null,
                                    movieRating = movieDetails.rating,
                                    version = "2.0.0"
                                )
                            )
                        )
                    } else {
                        Result.Error(mcpResponse.error ?: "Failed to fetch movie details")
                    }
                }
                is Result.Error -> Result.Error(response.message)
                is Result.Loading -> Result.Loading
            }
        }.getOrElse { exception ->
            Result.Error("Error fetching movie details: ${exception.message ?: "Unknown error"}")
        }
    }
    
    /**
     * Business method: Search movies using MCP HTTP client
     */
    suspend fun searchMoviesViaMcp(query: String, page: Int): Result<MovieListResponse> {
        return runCatching {
            val response = mcpHttpClient.sendMcpRequest<McpResponseDto<McpMovieListResponseDto>>(
                method = "searchMovies",
                params = mapOf("query" to query, "page" to page)
            )
            
            when (response) {
                is Result.Success -> {
                    val mcpResponse = response.data
                    if (mcpResponse.success && mcpResponse.data != null) {
                        val movieListResponse = MovieMapper.mapMcpMovieListResponseDtoToMovieListResponse(mcpResponse.data)
                        val uiConfig = mcpResponse.uiConfig?.let { MovieMapper.mapUiConfigurationDtoToUiConfiguration(it) }
                        Result.Success(
                            data = movieListResponse,
                            uiConfig = uiConfig
                        )
                    } else {
                        Result.Error(mcpResponse.error ?: "Failed to search movies")
                    }
                }
                is Result.Error -> Result.Error(response.message)
                is Result.Loading -> Result.Loading
            }
        }.getOrElse { exception ->
            Result.Error("Error searching movies: ${exception.message ?: "Unknown error"}")
        }
    }
    
    /**
     * Clean up resources
     */
    fun close() {
        mcpHttpClient.close()
    }
    
    private suspend fun <T> simulateNetworkCall(block: () -> T): McpResponseDto<T> {
        return runCatching {
            // Simulate network delay
            delay(500 + (Math.random() * 1000).toLong())
            
            // Simulate occasional network errors (5% chance)
            if (Math.random() < 0.05) {
                throw IOException("Simulated network error")
            }
            
            val data = block()
            val uiConfig = generateMockUiConfig()
            val meta = generateMockMeta()
            
            McpResponseDto(
                success = true,
                data = data,
                uiConfig = uiConfig,
                error = null,
                meta = meta
            )
        }.getOrElse { exception ->
            McpResponseDto(
                success = false,
                data = null,
                uiConfig = generateMockUiConfig(),
                error = exception.message ?: "Unknown error",
                meta = MetaDto(
                    timestamp = System.currentTimeMillis().toString(),
                    method = "unknown",
                    searchQuery = null,
                    movieId = null,
                    resultsCount = null,
                    aiGenerated = false,
                    geminiColors = GeminiColorsDto(
                        primary = "#FF0000",
                        secondary = "#FF0000",
                        accent = "#FF0000"
                    ),
                    avgRating = null,
                    movieRating = null,
                    version = "2.0.0"
                )
            )
        }
    }
    
    private fun generateMockMovies(): List<MovieDto> {
        return listOf(
            MovieDto(
                id = 1,
                title = "The Midnight Bloom",
                description = "A young botanist discovers a rare, bioluminescent flower with the power to heal any ailment, but must protect it from those who seek to exploit its magic.",
                posterPath = "/poster1.jpg",
                rating = 8.5,
                voteCount = 1234,
                releaseDate = "2024-03-15",
                backdropPath = "/backdrop1.jpg",
                genreIds = listOf(1, 2, 3),
                popularity = 85.5,
                adult = false
            ),
            MovieDto(
                id = 2,
                title = "Echoes of the Past",
                description = "A historian uncovers a hidden diary revealing a forgotten chapter of the city's history, leading to a quest for a lost artifact.",
                posterPath = "/poster2.jpg",
                rating = 7.8,
                voteCount = 987,
                releaseDate = "2024-02-20",
                backdropPath = "/backdrop2.jpg",
                genreIds = listOf(4, 5),
                popularity = 72.3,
                adult = false
            ),
            MovieDto(
                id = 3,
                title = "Quantum Dreams",
                description = "A brilliant physicist develops a machine that can read dreams, but discovers that some dreams are actually memories from parallel universes.",
                posterPath = "/poster3.jpg",
                rating = 9.1,
                voteCount = 2156,
                releaseDate = "2024-01-10",
                backdropPath = "/backdrop3.jpg",
                genreIds = listOf(6, 7, 8),
                popularity = 95.2,
                adult = false
            ),
            MovieDto(
                id = 4,
                title = "The Last Lighthouse",
                description = "A lighthouse keeper on a remote island discovers mysterious signals that seem to be coming from the past, leading to an incredible journey through time.",
                posterPath = "/poster4.jpg",
                rating = 8.2,
                voteCount = 1456,
                releaseDate = "2024-04-05",
                backdropPath = "/backdrop4.jpg",
                genreIds = listOf(9, 10),
                popularity = 78.9,
                adult = false
            ),
            MovieDto(
                id = 5,
                title = "Neon Nights",
                description = "In a cyberpunk future, a street artist's graffiti comes to life, revealing hidden messages that could change the course of the city's future.",
                posterPath = "/poster5.jpg",
                rating = 7.6,
                voteCount = 892,
                releaseDate = "2024-05-12",
                backdropPath = "/backdrop5.jpg",
                genreIds = listOf(11, 12),
                popularity = 65.4,
                adult = false
            )
        )
    }
    
    private fun generateMockUiConfig(): UiConfigurationDto {
        return UiConfigurationDto(
            colors = ColorSchemeDto(
                primary = "#2C5F6F",
                secondary = "#4A90A4",
                background = "#1A1A1A",
                surface = "#2A2A2A",
                onPrimary = "#FFFFFF",
                onSecondary = "#FFFFFF",
                onBackground = "#FFFFFF",
                onSurface = "#FFFFFF",
                moviePosterColors = listOf(
                    "#4A90A4",
                    "#8B4513",
                    "#2E8B57",
                    "#4682B4",
                    "#6A5ACD",
                    "#FF6347",
                    "#32CD32",
                    "#FFD700"
                )
            ),
            texts = TextConfigurationDto(
                appTitle = "Movie App",
                loadingText = "Loading movies...",
                errorMessage = "Something went wrong. Please try again.",
                noMoviesFound = "No movies found",
                retryButton = "Retry",
                backButton = "Back",
                playButton = "Play"
            ),
            buttons = ButtonConfigurationDto(
                primaryButtonColor = "#2C5F6F",
                secondaryButtonColor = "#4A90A4",
                buttonTextColor = "#FFFFFF",
                buttonCornerRadius = 8
            ),
            searchInfo = SearchInfoDto(
                query = "popular movies",
                resultCount = 5,
                avgRating = 8.2,
                ratingType = "TMDB",
                colorBased = true
            )
        )
    }
    
    private fun generateMockMeta(): MetaDto {
        return MetaDto(
            timestamp = System.currentTimeMillis().toString(),
            method = "getPopularMovies",
            searchQuery = null,
            movieId = null,
            resultsCount = 5,
            aiGenerated = true,
            geminiColors = GeminiColorsDto(
                primary = "#2C5F6F",
                secondary = "#4A90A4",
                accent = "#FFD700"
            ),
            avgRating = 8.2,
            movieRating = null,
            version = "2.0.0"
        )
    }
}
