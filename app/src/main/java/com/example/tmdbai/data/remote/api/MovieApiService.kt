package com.example.tmdbai.data.remote.api

import com.example.tmdbai.data.remote.dto.McpResponseDto
import com.example.tmdbai.data.remote.dto.MovieDetailsDto
import com.example.tmdbai.data.remote.dto.McpMovieListResponseDto

interface MovieApiService {
    suspend fun getPopularMovies(page: Int = 1): McpResponseDto<McpMovieListResponseDto>
    suspend fun getTopRatedMovies(page: Int = 1): McpResponseDto<McpMovieListResponseDto>
    suspend fun getNowPlayingMovies(page: Int = 1): McpResponseDto<McpMovieListResponseDto>
    suspend fun searchMovies(query: String, page: Int = 1): McpResponseDto<McpMovieListResponseDto>
    suspend fun getMovieDetails(movieId: Int): McpResponseDto<MovieDetailsDto>
    suspend fun getMovieRecommendations(movieId: Int, page: Int = 1): McpResponseDto<McpMovieListResponseDto>
}
