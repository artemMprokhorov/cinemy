package com.example.tmdbai.data.mcp

import com.example.tmdbai.BuildConfig
import com.example.tmdbai.data.mcp.models.McpRequest
import com.example.tmdbai.data.mcp.models.McpResponse
import com.example.tmdbai.data.model.Result
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import timber.log.Timber

/**
 * HTTP client for MCP (Model Context Protocol) communication
 * 
 * This client handles HTTP POST requests to the MCP server using Ktor HttpClient.
 * It provides a clean interface for sending MCP requests and handling responses.
 */
class McpHttpClient {
    
    private val httpClient = HttpClient(Android) {
        // Content negotiation for JSON serialization
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        
        // Logging for debugging
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }
    
    /**
     * Sends an MCP request to the server
     * 
     * @param method The MCP method to call
     * @param params The parameters to send with the request
     * @return Result<T> containing the response data or error information
     */
    suspend fun <T> sendMcpRequest(
        method: String, 
        params: Map<String, Any>
    ): Result<T> {
        return runCatching {
            // Create MCP request
            val mcpRequest = McpRequest(
                method = method,
                params = params.mapValues { it.value.toString() }
            )
            
            Timber.d("Sending MCP request: method=$method, params=$params")
            
            // Send HTTP POST request
            val response: McpResponse<T> = httpClient.post("${BuildConfig.MCP_SERVER_URL}/mcp") {
                contentType(ContentType.Application.Json)
                setBody(mcpRequest)
            }.body()
            
            Timber.d("Received MCP response: success=${response.success}, message=${response.message}")
            
            // Handle response
            when {
                response.success -> {
                    response.data?.let { data ->
                        Result.Success(data = data)
                    } ?: Result.Error("Response data is null")
                }
                else -> {
                    Result.Error(response.error ?: "Unknown error occurred")
                }
            }
        }.getOrElse { exception ->
            Timber.e(exception, "Error sending MCP request: method=$method")
            Result.Error("Network error: ${exception.message ?: "Unknown error"}")
        }
    }
    
    /**
     * Closes the HTTP client and releases resources
     */
    fun close() {
        httpClient.close()
    }
}
