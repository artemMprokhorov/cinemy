package com.example.tmdbai.data.di

import com.example.tmdbai.data.mcp.McpClient
import com.example.tmdbai.data.repository.MovieRepository
import com.example.tmdbai.data.repository.MovieRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    
    // MCP Client
    single<McpClient> { McpClient() }
    
    // Repository
    single<MovieRepository> { MovieRepositoryImpl(get()) }
}
