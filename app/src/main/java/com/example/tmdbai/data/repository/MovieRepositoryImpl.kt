package com.example.tmdbai.data.repository

import com.example.tmdbai.data.mapper.MovieMapper
import com.example.tmdbai.data.mcp.McpClient
import com.example.tmdbai.data.model.MovieDetails
import com.example.tmdbai.data.model.MovieListResponse
import com.example.tmdbai.data.model.Result

class MovieRepositoryImpl(
    private val mcpClient: McpClient
) : MovieRepository {
    
    override suspend fun getPopularMovies(page: Int): Result<MovieListResponse> {
        return runCatching {
            val response = mcpClient.getPopularMovies(page)
            if (response.success && response.data != null) {
                val movieListResponse = MovieMapper.mapMcpMovieListResponseDtoToMovieListResponse(response.data)
                val uiConfig = response.uiConfig?.let { MovieMapper.mapUiConfigurationDtoToUiConfiguration(it) }
                Result.Success(movieListResponse, uiConfig)
            } else {
                val uiConfig = response.uiConfig?.let { MovieMapper.mapUiConfigurationDtoToUiConfiguration(it) }
                Result.Error(response.error ?: "Failed to fetch popular movies", uiConfig)
            }
        }.getOrElse { exception ->
            Result.Error("Network error: ${exception.message}")
        }
    }
    
    override suspend fun getTopRatedMovies(page: Int): Result<MovieListResponse> {
        return runCatching {
            val response = mcpClient.getTopRatedMovies(page)
            if (response.success && response.data != null) {
                val movieListResponse = MovieMapper.mapMcpMovieListResponseDtoToMovieListResponse(response.data)
                val uiConfig = response.uiConfig?.let { MovieMapper.mapUiConfigurationDtoToUiConfiguration(it) }
                Result.Success(movieListResponse, uiConfig)
            } else {
                val uiConfig = response.uiConfig?.let { MovieMapper.mapUiConfigurationDtoToUiConfiguration(it) }
                Result.Error(response.error ?: "Failed to fetch top rated movies", uiConfig)
            }
        }.getOrElse { exception ->
            Result.Error("Network error: ${exception.message}")
        }
    }
    
    override suspend fun getNowPlayingMovies(page: Int): Result<MovieListResponse> {
        return runCatching {
            val response = mcpClient.getNowPlayingMovies(page)
            if (response.success && response.data != null) {
                val movieListResponse = MovieMapper.mapMcpMovieListResponseDtoToMovieListResponse(response.data)
                val uiConfig = response.uiConfig?.let { MovieMapper.mapUiConfigurationDtoToUiConfiguration(it) }
                Result.Success(movieListResponse, uiConfig)
            } else {
                val uiConfig = response.uiConfig?.let { MovieMapper.mapUiConfigurationDtoToUiConfiguration(it) }
                Result.Error(response.error ?: "Failed to fetch now playing movies", uiConfig)
            }
        }.getOrElse { exception ->
            Result.Error("Network error: ${exception.message}")
        }
    }
    
    override suspend fun searchMovies(query: String, page: Int): Result<MovieListResponse> {
        return runCatching {
            val response = mcpClient.searchMovies(query, page)
            if (response.success && response.data != null) {
                val movieListResponse = MovieMapper.mapMcpMovieListResponseDtoToMovieListResponse(response.data)
                val uiConfig = response.uiConfig?.let { MovieMapper.mapUiConfigurationDtoToUiConfiguration(it) }
                Result.Success(movieListResponse, uiConfig)
            } else {
                val uiConfig = response.uiConfig?.let { MovieMapper.mapUiConfigurationDtoToUiConfiguration(it) }
                Result.Error(response.error ?: "Failed to search movies", uiConfig)
            }
        }.getOrElse { exception ->
            Result.Error("Network error: ${exception.message}")
        }
    }
    
    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetails> {
        return runCatching {
            val response = mcpClient.getMovieDetails(movieId)
            if (response.success && response.data != null) {
                val movieDetails = MovieMapper.mapMovieDetailsDtoToMovieDetails(response.data)
                val uiConfig = response.uiConfig?.let { MovieMapper.mapUiConfigurationDtoToUiConfiguration(it) }
                Result.Success(movieDetails, uiConfig)
            } else {
                val uiConfig = response.uiConfig?.let { MovieMapper.mapUiConfigurationDtoToUiConfiguration(it) }
                Result.Error(response.error ?: "Failed to fetch movie details", uiConfig)
            }
        }.getOrElse { exception ->
            Result.Error("Network error: ${exception.message}")
        }
    }
    
    override suspend fun getMovieRecommendations(movieId: Int, page: Int): Result<MovieListResponse> {
        return runCatching {
            val response = mcpClient.getMovieRecommendations(movieId, page)
            if (response.success && response.data != null) {
                val movieListResponse = MovieMapper.mapMcpMovieListResponseDtoToMovieListResponse(response.data)
                val uiConfig = response.uiConfig?.let { MovieMapper.mapUiConfigurationDtoToUiConfiguration(it) }
                Result.Success(movieListResponse, uiConfig)
            } else {
                val uiConfig = response.uiConfig?.let { MovieMapper.mapUiConfigurationDtoToUiConfiguration(it) }
                Result.Error(response.error ?: "Failed to fetch movie recommendations", uiConfig)
            }
        }.getOrElse { exception ->
            Result.Error("Network error: ${exception.message}")
        }
    }
}
