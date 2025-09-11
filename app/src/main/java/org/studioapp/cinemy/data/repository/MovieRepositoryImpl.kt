package org.studioapp.cinemy.data.repository

import org.studioapp.cinemy.BuildConfig
import org.studioapp.cinemy.data.mcp.McpClient
import org.studioapp.cinemy.data.model.MovieDetailsResponse
import org.studioapp.cinemy.data.model.MovieListResponse
import org.studioapp.cinemy.data.model.Result
import org.studioapp.cinemy.data.model.StringConstants

class MovieRepositoryImpl(
    private val mcpClient: McpClient
) : MovieRepository {

    override suspend fun getPopularMovies(page: Int): Result<MovieListResponse> {
        return runCatching {
            val response = mcpClient.getPopularMoviesViaMcp(page)
            
            // Debug logging for repository response
            if (BuildConfig.DEBUG) {
                val uiConfigInfo = when (response) {
                    is Result.Success -> response.uiConfig?.colors?.primary?.toString() ?: "null"
                    is Result.Error -> response.uiConfig?.colors?.primary?.toString() ?: "null"
                    is Result.Loading -> "loading"
                }
            }
            
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
            Result.Error("${StringConstants.ERROR_NETWORK_WITH_MESSAGE.format(exception.message ?: StringConstants.ERROR_UNKNOWN)}")
        }
    }


    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsResponse> {
        return runCatching {
            val response = mcpClient.getMovieDetailsViaMcp(movieId)
            
            // Debug logging for repository response
            if (BuildConfig.DEBUG) {
                val uiConfigInfo = when (response) {
                    is Result.Success -> response.uiConfig?.colors?.primary?.toString() ?: "null"
                    is Result.Error -> response.uiConfig?.colors?.primary?.toString() ?: "null"
                    is Result.Loading -> "loading"
                }
            }
            
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
            Result.Error("${StringConstants.ERROR_NETWORK_WITH_MESSAGE.format(exception.message ?: StringConstants.ERROR_UNKNOWN)}")
        }
    }
}
