package com.example.tmdbai.data.repository

import com.example.tmdbai.data.model.MovieDetailsResponse
import com.example.tmdbai.data.model.MovieListResponse
import com.example.tmdbai.data.model.Result

interface MovieRepository {
    // Popular movies with enhanced pagination
    suspend fun getPopularMovies(page: Int = 1): Result<MovieListResponse>

    // Movie details with complete information
    suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsResponse>
}
