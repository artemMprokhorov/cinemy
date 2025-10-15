package org.studioapp.cinemy.data.di

import org.koin.dsl.module
import org.studioapp.cinemy.data.mcp.AssetDataLoader
import org.studioapp.cinemy.data.mcp.McpClient
import org.studioapp.cinemy.data.mcp.McpHttpClient
import org.studioapp.cinemy.data.repository.MovieRepository
import org.studioapp.cinemy.data.repository.MovieRepositoryImpl

/**
 * Koin dependency injection module for the data layer
 * 
 * This module provides dependency injection configuration for all data layer components
 * including MCP (Model Context Protocol) clients, asset data loaders, and repositories.
 * It follows the singleton pattern to ensure consistent instances across the application
 * and supports both mock and production data sources based on build configuration.
 * 
 * The module is designed to work with the MVI architecture pattern, providing the
 * Model component dependencies for the presentation layer through repository interfaces.
 * 
 * @see org.koin.dsl.module for Koin module configuration
 * @see org.studioapp.cinemy.data.mcp.McpClient for MCP client implementation
 * @see org.studioapp.cinemy.data.repository.MovieRepository for repository interface
 * 
 * @sample
 * ```kotlin
 * // Module usage in Koin setup
 * startKoin {
 *     modules(dataModule)
 * }
 * ```
 */
val dataModule = module {

    /**
     * MCP HTTP Client dependency
     * 
     * Provides HTTP transport layer for MCP (Model Context Protocol) communication
     * with the backend server. Handles JSON request/response serialization and
     * network communication using Ktor HTTP client.
     * 
     * @return McpHttpClient singleton instance for HTTP transport
     * @see org.studioapp.cinemy.data.mcp.McpHttpClient for implementation details
     */
    single<McpHttpClient> {
        McpHttpClient(get())
    }

    /**
     * Asset Data Loader dependency
     * 
     * Provides asset loading functionality for mock data, UI configuration,
     * and metadata from Android assets directory. Used for development and
     * testing scenarios with local asset files.
     * 
     * @return AssetDataLoader singleton instance for asset operations
     * @see org.studioapp.cinemy.data.mcp.AssetDataLoader for implementation details
     */
    single<AssetDataLoader> { AssetDataLoader(get()) }

    /**
     * MCP Client dependency
     * 
     * Provides high-level MCP client for backend communication, combining
     * HTTP transport with asset loading capabilities. Handles request/response
     * mapping and provides unified interface for repository layer.
     * 
     * @return McpClient singleton instance for MCP operations
     * @see org.studioapp.cinemy.data.mcp.McpClient for implementation details
     */
    single<McpClient> { McpClient(get()) }

    /**
     * Movie Repository dependency
     * 
     * Provides unified repository implementation for movie data operations.
     * Supports both mock and production data sources based on build configuration.
     * Implements the repository pattern for clean architecture and serves as
     * the Model component in MVI architecture.
     * 
     * @return MovieRepository singleton instance for data operations
     * @see org.studioapp.cinemy.data.repository.MovieRepository for interface contract
     * @see org.studioapp.cinemy.data.repository.MovieRepositoryImpl for implementation details
     */
    single<MovieRepository> {
        MovieRepositoryImpl(get(), get())
    }
}
