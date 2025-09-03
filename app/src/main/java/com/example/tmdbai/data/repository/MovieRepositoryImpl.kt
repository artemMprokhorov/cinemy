package com.example.tmdbai.data.repository

import com.example.tmdbai.data.mcp.McpClient
import com.example.tmdbai.data.model.MovieDetails
import com.example.tmdbai.data.model.MovieListResponse
import com.example.tmdbai.data.model.MoviesResponse
import com.example.tmdbai.data.model.MovieDetailsResponse
import com.example.tmdbai.data.model.Result

class MovieRepositoryImpl(
    private val mcpClient: McpClient
) : MovieRepository {
    
    override suspend fun getPopularMovies(page: Int): Result<MovieListResponse> {
        return runCatching {
            val response = mcpClient.getPopularMoviesViaMcp()
            when (response) {
                is Result.Success -> {
                    val moviesResponse = response.data
                    Result.Success(
                        data = MovieListResponse(
                            movies = moviesResponse.movies,
                            pagination = moviesResponse.pagination
                        ),
                        uiConfig = moviesResponse.uiConfig
                    )
                }
                is Result.Error -> Result.Error(response.message, response.uiConfig)
                is Result.Loading -> Result.Loading
            }
        }.getOrElse { exception ->
            Result.Error("Network error: ${exception.message}")
        }
    }
    
    override suspend fun getTopRatedMovies(page: Int): Result<MovieListResponse> {
        return runCatching {
            // For now, use the same as popular movies since we don't have a specific MCP method
            // This can be updated when the MCP server supports top rated movies
            val response = mcpClient.getPopularMoviesViaMcp()
            when (response) {
                is Result.Success -> {
                    val moviesResponse = response.data
                    Result.Success(
                        data = MovieListResponse(
                            movies = moviesResponse.movies,
                            pagination = moviesResponse.pagination
                        ),
                        uiConfig = moviesResponse.uiConfig
                    )
                }
                is Result.Error -> Result.Error(response.message, response.uiConfig)
                is Result.Loading -> Result.Loading
            }
        }.getOrElse { exception ->
            Result.Error("Network error: ${exception.message}")
        }
    }
    
    override suspend fun getNowPlayingMovies(page: Int): Result<MovieListResponse> {
        return runCatching {
            // For now, use the same as popular movies since we don't have a specific MCP method
            // This can be updated when the MCP server supports now playing movies
            val response = mcpClient.getPopularMoviesViaMcp()
            when (response) {
                is Result.Success -> {
                    val moviesResponse = response.data
                    Result.Success(
                        data = MovieListResponse(
                            movies = moviesResponse.movies,
                            pagination = moviesResponse.pagination
                        ),
                        uiConfig = moviesResponse.uiConfig
                    )
                }
                is Result.Error -> Result.Error(response.message, response.uiConfig)
                is Result.Loading -> Result.Loading
            }
        }.getOrElse { exception ->
            Result.Error("Network error: ${exception.message}")
        }
    }
    
    override suspend fun searchMovies(query: String, page: Int): Result<MovieListResponse> {
        return runCatching {
            val response = mcpClient.searchMoviesViaMcp(query)
            when (response) {
                is Result.Success -> {
                    val moviesResponse = response.data
                    Result.Success(
                        data = MovieListResponse(
                            movies = moviesResponse.movies,
                            pagination = moviesResponse.pagination
                        ),
                        uiConfig = moviesResponse.uiConfig
                    )
                }
                is Result.Error -> Result.Error(response.message, response.uiConfig)
                is Result.Loading -> Result.Loading
            }
        }.getOrElse { exception ->
            Result.Error("Network error: ${exception.message}")
        }
    }
    
    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetails> {
        return runCatching {
            val response = mcpClient.getMovieDetailsViaMcp(movieId)
            when (response) {
                is Result.Success -> {
                    val movieDetailsResponse = response.data
                    Result.Success(
                        data = movieDetailsResponse.movieDetails,
                        uiConfig = movieDetailsResponse.uiConfig
                    )
                }
                is Result.Error -> Result.Error(response.message, response.uiConfig)
                is Result.Loading -> Result.Loading
            }
        }.getOrElse { exception ->
            Result.Error("Network error: ${exception.message}")
        }
    }
    
    override suspend fun getMovieRecommendations(movieId: Int, page: Int): Result<MovieListResponse> {
        return runCatching {
            // For now, use search movies as a fallback since we don't have a specific recommendations method
            // This can be updated when the MCP server supports movie recommendations
            val response = mcpClient.searchMoviesViaMcp("")
            when (response) {
                is Result.Success -> {
                    val moviesResponse = response.data
                    Result.Success(
                        data = MovieListResponse(
                            movies = moviesResponse.movies,
                            pagination = moviesResponse.pagination
                        ),
                        uiConfig = moviesResponse.uiConfig
                    )
                }
                is Result.Error -> Result.Error(response.message, response.uiConfig)
                is Result.Loading -> Result.Loading
            }
        }.getOrElse { exception ->
            Result.Error("Network error: ${exception.message}")
        }
    }
}
