package com.example.tmdbai.data.mcp

import android.content.Context
import com.example.tmdbai.data.remote.api.MovieApiService
import com.example.tmdbai.data.remote.dto.McpResponseDto
import com.example.tmdbai.data.remote.dto.McpMovieListResponseDto
import com.example.tmdbai.data.remote.dto.MovieDetailsDto
import com.example.tmdbai.data.remote.dto.MovieDto
import com.example.tmdbai.data.remote.dto.PaginationDto
import com.example.tmdbai.data.remote.dto.GenreDto
import com.example.tmdbai.data.remote.dto.ProductionCompanyDto
import com.example.tmdbai.data.remote.dto.UiConfigurationDto
import com.example.tmdbai.data.remote.dto.ColorSchemeDto
import com.example.tmdbai.data.remote.dto.TextConfigurationDto
import com.example.tmdbai.data.remote.dto.ButtonConfigurationDto
import com.example.tmdbai.data.remote.dto.SearchInfoDto
import com.example.tmdbai.data.remote.dto.MetaDto
import com.example.tmdbai.data.remote.dto.GeminiColorsDto
import com.example.tmdbai.data.model.MovieListResponse
import com.example.tmdbai.data.model.MovieDetailsResponse
import com.example.tmdbai.data.model.MovieDetailsData
import com.example.tmdbai.data.model.MovieDetails
import com.example.tmdbai.data.model.Movie
import com.example.tmdbai.data.model.Result
import com.example.tmdbai.data.model.UiConfiguration
import com.example.tmdbai.data.model.ColorScheme
import com.example.tmdbai.data.model.TextConfiguration
import com.example.tmdbai.data.model.ButtonConfiguration
import com.example.tmdbai.data.model.Meta
import com.example.tmdbai.data.model.GeminiColors
import com.example.tmdbai.data.mapper.MovieMapper
import com.example.tmdbai.data.mcp.models.McpRequest
import com.google.gson.Gson
import kotlinx.coroutines.delay
import java.io.IOException

class McpClient(private val context: Context) : MovieApiService {
    
    private val gson = Gson()
    private val mcpHttpClient = McpHttpClient(context)
    
    override suspend fun getPopularMovies(page: Int): McpResponseDto<McpMovieListResponseDto> {
        val request = McpRequest(
            method = "getPopularMovies",
            params = mapOf("page" to page.toString())
        )
        
        val response = mcpHttpClient.sendRequest<Map<String, Any>>(request)
        
        return if (response.success && response.data != null) {
            // Convert the response data to DTO format
            val data = response.data
            val movies = (data["movies"] as? List<Map<String, Any>>)?.map { movieData ->
                MovieDto(
                    id = (movieData["id"] as? Number)?.toInt() ?: 0,
                    title = movieData["title"] as? String ?: "",
                    description = movieData["description"] as? String ?: "",
                    posterPath = movieData["posterPath"] as? String,
                    backdropPath = movieData["backdropPath"] as? String,
                    rating = (movieData["rating"] as? Number)?.toDouble() ?: 0.0,
                    voteCount = (movieData["voteCount"] as? Number)?.toInt() ?: 0,
                    releaseDate = movieData["releaseDate"] as? String ?: "",
                    genreIds = (movieData["genreIds"] as? List<Number>)?.map { it.toInt() } ?: emptyList(),
                    popularity = (movieData["popularity"] as? Number)?.toDouble() ?: 0.0,
                    adult = movieData["adult"] as? Boolean ?: false
                )
            } ?: emptyList()
            
            val paginationData = data["pagination"] as? Map<String, Any>
            val pagination = PaginationDto(
                page = (paginationData?.get("page") as? Number)?.toInt() ?: page,
                totalPages = (paginationData?.get("totalPages") as? Number)?.toInt() ?: 1,
                totalResults = (paginationData?.get("totalResults") as? Number)?.toInt() ?: 0,
                hasNext = paginationData?.get("hasNext") as? Boolean ?: false,
                hasPrevious = paginationData?.get("hasPrevious") as? Boolean ?: false
            )
            
            McpResponseDto(
                success = true,
                data = McpMovieListResponseDto(movies, pagination),
                uiConfig = generateMockUiConfig(),
                error = null,
                meta = generateMockMeta()
            )
        } else {
            McpResponseDto(
                success = false,
                data = null,
                uiConfig = generateMockUiConfig(),
                error = response.error ?: "Failed to fetch popular movies",
                meta = generateMockMeta()
            )
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
    
    
    override suspend fun getMovieDetails(movieId: Int): McpResponseDto<MovieDetailsDto> {
        val request = McpRequest(
            method = "getMovieDetails",
            params = mapOf("movieId" to movieId.toString())
        )
        
        val response = mcpHttpClient.sendRequest<Map<String, Any>>(request)
        
        return if (response.success && response.data != null) {
            // Convert the response data to DTO format
            val data = response.data
            val movieDetailsData = data["movieDetails"] as? Map<String, Any>
            
            if (movieDetailsData != null) {
                val movieDetails = MovieDetailsDto(
                    id = (movieDetailsData["id"] as? Number)?.toInt() ?: movieId,
                    title = movieDetailsData["title"] as? String ?: "",
                    description = movieDetailsData["description"] as? String ?: "",
                    posterPath = movieDetailsData["posterPath"] as? String,
                    backdropPath = movieDetailsData["backdropPath"] as? String,
                    rating = (movieDetailsData["rating"] as? Number)?.toDouble() ?: 0.0,
                    voteCount = (movieDetailsData["voteCount"] as? Number)?.toInt() ?: 0,
                    releaseDate = movieDetailsData["releaseDate"] as? String ?: "",
                    runtime = (movieDetailsData["runtime"] as? Number)?.toInt() ?: 0,
                    genres = (movieDetailsData["genres"] as? List<Map<String, Any>>)?.map { genreData ->
                        GenreDto(
                            id = (genreData["id"] as? Number)?.toInt() ?: 0,
                            name = genreData["name"] as? String ?: ""
                        )
                    } ?: emptyList(),
                    productionCompanies = (movieDetailsData["productionCompanies"] as? List<Map<String, Any>>)?.map { companyData ->
                        ProductionCompanyDto(
                            id = (companyData["id"] as? Number)?.toInt() ?: 0,
                            logoPath = companyData["logoPath"] as? String,
                            name = companyData["name"] as? String ?: "",
                            originCountry = companyData["originCountry"] as? String ?: ""
                        )
                    } ?: emptyList(),
                    budget = (movieDetailsData["budget"] as? Number)?.toLong() ?: 0,
                    revenue = (movieDetailsData["revenue"] as? Number)?.toLong() ?: 0,
                    status = movieDetailsData["status"] as? String ?: ""
                )
                
                McpResponseDto(
                    success = true,
                    data = movieDetails,
                    uiConfig = generateMockUiConfig(),
                    error = null,
                    meta = generateMockMeta()
                )
            } else {
                McpResponseDto(
                    success = false,
                    data = null,
                    uiConfig = generateMockUiConfig(),
                    error = "Invalid movie details data",
                    meta = generateMockMeta()
                )
            }
        } else {
            McpResponseDto(
                success = false,
                data = null,
                uiConfig = generateMockUiConfig(),
                error = response.error ?: "Failed to fetch movie details",
                meta = generateMockMeta()
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
            val request = McpRequest(
                method = "getPopularMovies",
                params = mapOf("page" to page.toString())
            )
            
            val response = mcpHttpClient.sendRequest<Map<String, Any>>(request)
            
            if (response.success && response.data != null) {
                val data = response.data
                val movies = (data["movies"] as? List<Map<String, Any>>)?.map { movieData ->
                    MovieDto(
                        id = (movieData["id"] as? Number)?.toInt() ?: 0,
                        title = movieData["title"] as? String ?: "",
                        description = movieData["description"] as? String ?: "",
                        posterPath = movieData["posterPath"] as? String,
                        backdropPath = movieData["backdropPath"] as? String,
                        rating = (movieData["rating"] as? Number)?.toDouble() ?: 0.0,
                        voteCount = (movieData["voteCount"] as? Number)?.toInt() ?: 0,
                        releaseDate = movieData["releaseDate"] as? String ?: "",
                        genreIds = (movieData["genreIds"] as? List<Number>)?.map { it.toInt() } ?: emptyList(),
                        popularity = (movieData["popularity"] as? Number)?.toDouble() ?: 0.0,
                        adult = movieData["adult"] as? Boolean ?: false
                    )
                } ?: emptyList()
                
                val paginationData = data["pagination"] as? Map<String, Any>
                val pagination = PaginationDto(
                    page = (paginationData?.get("page") as? Number)?.toInt() ?: page,
                    totalPages = (paginationData?.get("totalPages") as? Number)?.toInt() ?: 1,
                    totalResults = (paginationData?.get("totalResults") as? Number)?.toInt() ?: 0,
                    hasNext = paginationData?.get("hasNext") as? Boolean ?: false,
                    hasPrevious = paginationData?.get("hasPrevious") as? Boolean ?: false
                )
                
                val movieListResponse = MovieMapper.mapMcpMovieListResponseDtoToMovieListResponse(
                    McpMovieListResponseDto(movies, pagination)
                )
                
                Result.Success(
                    data = movieListResponse,
                    uiConfig = null
                )
            } else {
                Result.Error(response.error ?: "Failed to fetch popular movies")
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
            val request = McpRequest(
                method = "getMovieDetails",
                params = mapOf("movieId" to movieId.toString())
            )
            
            val response = mcpHttpClient.sendRequest<Map<String, Any>>(request)
            
            if (response.success && response.data != null) {
                val data = response.data
                val movieDetailsData = data["movieDetails"] as? Map<String, Any>
                
                if (movieDetailsData != null) {
                    val movieDetails = MovieDetailsDto(
                        id = (movieDetailsData["id"] as? Number)?.toInt() ?: movieId,
                        title = movieDetailsData["title"] as? String ?: "",
                        description = movieDetailsData["description"] as? String ?: "",
                        posterPath = movieDetailsData["posterPath"] as? String,
                        backdropPath = movieDetailsData["backdropPath"] as? String,
                        rating = (movieDetailsData["rating"] as? Number)?.toDouble() ?: 0.0,
                        voteCount = (movieDetailsData["voteCount"] as? Number)?.toInt() ?: 0,
                        releaseDate = movieDetailsData["releaseDate"] as? String ?: "",
                        runtime = (movieDetailsData["runtime"] as? Number)?.toInt() ?: 0,
                        genres = (movieDetailsData["genres"] as? List<Map<String, Any>>)?.map { genreData ->
                            GenreDto(
                                id = (genreData["id"] as? Number)?.toInt() ?: 0,
                                name = genreData["name"] as? String ?: ""
                            )
                        } ?: emptyList(),
                        productionCompanies = (movieDetailsData["productionCompanies"] as? List<Map<String, Any>>)?.map { companyData ->
                            ProductionCompanyDto(
                                id = (companyData["id"] as? Number)?.toInt() ?: 0,
                                logoPath = companyData["logoPath"] as? String,
                                name = companyData["name"] as? String ?: "",
                                originCountry = companyData["originCountry"] as? String ?: ""
                            )
                        } ?: emptyList(),
                        budget = (movieDetailsData["budget"] as? Number)?.toLong() ?: 0,
                        revenue = (movieDetailsData["revenue"] as? Number)?.toLong() ?: 0,
                        status = movieDetailsData["status"] as? String ?: ""
                    )
                    
                    val domainMovieDetails = MovieMapper.mapMovieDetailsDtoToMovieDetails(movieDetails)
                    
                    Result.Success(
                        data = MovieDetailsResponse(
                            success = true,
                            data = MovieDetailsData(movieDetails = domainMovieDetails),
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
                                movieRating = domainMovieDetails.rating,
                                version = "2.0.0"
                            )
                        ),
                        uiConfig = null
                    )
                } else {
                    Result.Error("Invalid movie details data")
                }
            } else {
                Result.Error(response.error ?: "Failed to fetch movie details")
            }
        }.getOrElse { exception ->
            Result.Error("Error fetching movie details: ${exception.message ?: "Unknown error"}")
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
