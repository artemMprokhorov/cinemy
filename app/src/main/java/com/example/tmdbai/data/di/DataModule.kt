package com.example.tmdbai.data.di

import android.util.Log
import com.example.tmdbai.BuildConfig
import com.example.tmdbai.data.mcp.McpClient
import com.example.tmdbai.data.mcp.McpHttpClient
import com.example.tmdbai.data.repository.MovieRepository
import com.example.tmdbai.data.repository.MovieRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    
    // MCP HTTP Client
    single<McpHttpClient> { 
        McpHttpClient(get()).also {
            Log.d("DI", "McpHttpClient created - USE_MOCK_DATA: ${BuildConfig.USE_MOCK_DATA}")
            Log.d("DI", "MCP_SERVER_URL: ${BuildConfig.MCP_SERVER_URL}")
            Log.d("DI", "FLAVOR_NAME: ${BuildConfig.FLAVOR_NAME}")
        }
    }
    
    // MCP Client
    single<McpClient> { McpClient(get()) }
    
    // Repository
    single<MovieRepository> { MovieRepositoryImpl(get()) }
}
