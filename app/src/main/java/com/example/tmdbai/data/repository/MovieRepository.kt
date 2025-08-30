package com.example.tmdbai.data.repository

import com.example.tmdbai.data.model.Movie
import com.example.tmdbai.data.model.MovieDetails
import com.example.tmdbai.data.model.MovieListResponse
import com.example.tmdbai.data.model.Result

interface MovieRepository {
    suspend fun getPopularMovies(page: Int = 1): Result<MovieListResponse>
    suspend fun getTopRatedMovies(page: Int = 1): Result<MovieListResponse>
    suspend fun getNowPlayingMovies(page: Int = 1): Result<MovieListResponse>
    suspend fun searchMovies(query: String, page: Int = 1): Result<MovieListResponse>
    suspend fun getMovieDetails(movieId: Int): Result<MovieDetails>
    suspend fun getMovieRecommendations(movieId: Int, page: Int = 1): Result<MovieListResponse>
}
