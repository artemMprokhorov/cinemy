package org.studioapp.cinemy.data.repository

import org.studioapp.cinemy.data.model.MovieDetailsResponse
import org.studioapp.cinemy.data.model.MovieListResponse
import org.studioapp.cinemy.data.model.Result

interface MovieRepository {
    // Popular movies with enhanced pagination
    suspend fun getPopularMovies(page: Int = 1): Result<MovieListResponse>

    // Movie details with complete information
    suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsResponse>
}
