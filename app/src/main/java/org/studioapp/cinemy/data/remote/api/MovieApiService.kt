package org.studioapp.cinemy.data.remote.api

import org.studioapp.cinemy.data.remote.dto.McpResponseDto
import org.studioapp.cinemy.data.remote.dto.MovieDetailsDto
import org.studioapp.cinemy.data.remote.dto.MovieListResponseDto

interface MovieApiService {
    suspend fun getPopularMovies(page: Int = 1): McpResponseDto<MovieListResponseDto>
    suspend fun getMovieDetails(movieId: Int): McpResponseDto<MovieDetailsDto>
}
