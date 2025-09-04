package com.example.tmdbai.data.repository

import com.example.tmdbai.data.mcp.McpClient
import com.example.tmdbai.data.model.MovieListResponse
import com.example.tmdbai.data.model.MovieDetailsResponse
import com.example.tmdbai.data.model.Result

class MovieRepositoryImpl(
    private val mcpClient: McpClient
) : MovieRepository {
    
    override suspend fun getPopularMovies(page: Int): Result<MovieListResponse> {
        return runCatching {
            val response = mcpClient.getPopularMoviesViaMcp(page)
            when (response) {
                is Result.Success -> {
                    Result.Success(
                        data = response.data,
                        uiConfig = response.uiConfig
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
            val response = mcpClient.searchMoviesViaMcp(query, page)
            when (response) {
                is Result.Success -> {
                    Result.Success(
                        data = response.data,
                        uiConfig = response.uiConfig
                    )
                }
                is Result.Error -> Result.Error(response.message, response.uiConfig)
                is Result.Loading -> Result.Loading
            }
        }.getOrElse { exception ->
            Result.Error("Network error: ${exception.message}")
        }
    }
    
    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsResponse> {
        return runCatching {
            val response = mcpClient.getMovieDetailsViaMcp(movieId)
            when (response) {
                is Result.Success -> {
                    Result.Success(
                        data = response.data,
                        uiConfig = response.uiConfig
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
