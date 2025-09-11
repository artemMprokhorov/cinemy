package org.studioapp.cinemy.data.di

import org.koin.dsl.module
import org.studioapp.cinemy.data.mcp.AssetDataLoader
import org.studioapp.cinemy.data.mcp.McpClient
import org.studioapp.cinemy.data.mcp.McpHttpClient
import org.studioapp.cinemy.data.repository.MovieRepository
import org.studioapp.cinemy.data.repository.MovieRepositoryImpl

val dataModule = module {

    // MCP HTTP Client
    single<McpHttpClient> {
        McpHttpClient(get())
    }

    // Asset Data Loader
    single<AssetDataLoader> { AssetDataLoader(get()) }

    // MCP Client
    single<McpClient> { McpClient(get()) }

    // Repository - Uses McpClient which automatically switches between mock and real data
    single<MovieRepository> { MovieRepositoryImpl(get()) }
}
