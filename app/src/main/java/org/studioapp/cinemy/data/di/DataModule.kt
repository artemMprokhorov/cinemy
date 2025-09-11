package org.studioapp.cinemy.data.di

import android.util.Log
import org.studioapp.cinemy.BuildConfig
import org.studioapp.cinemy.data.mcp.AssetDataLoader
import org.studioapp.cinemy.data.mcp.McpClient
import org.studioapp.cinemy.data.mcp.McpHttpClient
import org.studioapp.cinemy.data.repository.MovieRepository
import org.studioapp.cinemy.data.repository.MovieRepositoryImpl
import org.koin.dsl.module

val dataModule = module {

    // MCP HTTP Client
    single<McpHttpClient> {
        McpHttpClient(get()).also {
            if (BuildConfig.DEBUG) {
                Log.d("DI", "McpHttpClient created - USE_MOCK_DATA: ${BuildConfig.USE_MOCK_DATA}")
                Log.d("DI", "MCP_SERVER_URL: ${BuildConfig.MCP_SERVER_URL}")
                Log.d("DI", "FLAVOR_NAME: ${BuildConfig.FLAVOR_NAME}")
            }
        }
    }

    // Asset Data Loader
    single<AssetDataLoader> { AssetDataLoader(get()) }

    // MCP Client
    single<McpClient> { McpClient(get()) }

    // Repository - Uses McpClient which automatically switches between mock and real data
    single<MovieRepository> { MovieRepositoryImpl(get()) }
}
