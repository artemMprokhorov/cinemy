package org.studioapp.cinemy.data.remote.api

import org.studioapp.cinemy.data.remote.dto.McpResponseDto
import org.studioapp.cinemy.data.remote.dto.MovieDetailsDto
import org.studioapp.cinemy.data.remote.dto.MovieListResponseDto

/**
 * Interface defining the contract for movie-related API operations.
 *
 * This service provides methods for fetching movie data through the MCP (Model Context Protocol)
 * backend communication layer. All methods return MCP response DTOs that include both data
 * and UI configuration information.
 *
 * @see org.studioapp.cinemy.data.mcp.McpClient for the implementation
 * @see org.studioapp.cinemy.data.remote.dto.McpResponseDto for response structure
 */
interface MovieApiService {

    /**
     * Fetches a paginated list of popular movies from the backend.
     *
     * This method retrieves popular movies with pagination support, allowing clients to
     * request specific pages of results. The response includes movie data along with
     * UI configuration for dynamic theming.
     *
     * @param page The page number to fetch (1-based indexing). Defaults to 1 if not specified.
     * @return McpResponseDto<MovieListResponseDto> containing:
     *         - success: Boolean indicating operation success
     *         - data: MovieListResponseDto with paginated movie list and pagination metadata
     *         - error: String with error message if operation failed
     *         - message: String with additional information about the operation
     *
     * @see org.studioapp.cinemy.data.remote.dto.MovieListResponseDto for response structure
     * @see org.studioapp.cinemy.data.remote.dto.McpResponseDto for wrapper structure
     *
     * @throws No exceptions are thrown - errors are returned in the response structure
     */
    suspend fun getPopularMovies(page: Int = 1): McpResponseDto<MovieListResponseDto>

    /**
     * Fetches detailed information for a specific movie by its ID.
     *
     * This method retrieves comprehensive movie details including genres, production companies,
     * sentiment analysis data, and UI configuration for the specific movie.
     *
     * @param movieId The unique identifier of the movie to fetch details for.
     * @return McpResponseDto<MovieDetailsDto> containing:
     *         - success: Boolean indicating operation success
     *         - data: MovieDetailsDto with complete movie information including:
     *           - Basic movie details (title, description, runtime, etc.)
     *           - Genres and production companies
     *           - Budget and revenue information
     *           - Sentiment analysis reviews and metadata
     *         - error: String with error message if operation failed
     *         - message: String with additional information about the operation
     *
     * @see org.studioapp.cinemy.data.remote.dto.MovieDetailsDto for response structure
     * @see org.studioapp.cinemy.data.remote.dto.McpResponseDto for wrapper structure
     *
     * @throws No exceptions are thrown - errors are returned in the response structure
     */
    suspend fun getMovieDetails(movieId: Int): McpResponseDto<MovieDetailsDto>
}
