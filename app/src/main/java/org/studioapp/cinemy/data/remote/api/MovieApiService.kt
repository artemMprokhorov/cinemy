package org.studioapp.cinemy.data.remote.api

import org.studioapp.cinemy.data.remote.dto.McpMovieListResponseDto
import org.studioapp.cinemy.data.remote.dto.McpResponseDto
import org.studioapp.cinemy.data.remote.dto.MovieDetailsDto
import org.studioapp.cinemy.data.remote.dto.MovieListResponseDto

interface MovieApiService {
    suspend fun getPopularMovies(page: Int = 1): McpResponseDto<MovieListResponseDto>
    suspend fun getTopRatedMovies(page: Int = 1): McpResponseDto<McpMovieListResponseDto>
    suspend fun getNowPlayingMovies(page: Int = 1): McpResponseDto<McpMovieListResponseDto>
    suspend fun getMovieDetails(movieId: Int): McpResponseDto<MovieDetailsDto>
    suspend fun getMovieRecommendations(
        movieId: Int,
        page: Int = 1
    ): McpResponseDto<McpMovieListResponseDto>
}
