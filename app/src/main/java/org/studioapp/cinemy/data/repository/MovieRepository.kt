package org.studioapp.cinemy.data.repository

import org.studioapp.cinemy.data.model.MovieDetailsResponse
import org.studioapp.cinemy.data.model.MovieListResponse
import org.studioapp.cinemy.data.model.Result

interface MovieRepository {
    /**
     * Fetches popular movies with pagination support
     * @param page Page number for pagination (default: 1)
     * @return Result containing MovieListResponse with movies, pagination, and UI config
     */
    suspend fun getPopularMovies(page: Int = 1): Result<MovieListResponse>

    /**
     * Fetches detailed movie information by ID
     * @param movieId Unique identifier of the movie
     * @return Result containing MovieDetailsResponse with complete movie details and UI config
     */
    suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsResponse>
}
