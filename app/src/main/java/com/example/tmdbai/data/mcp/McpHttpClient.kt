package com.example.tmdbai.data.mcp

import android.util.Log
import com.example.tmdbai.BuildConfig
import com.example.tmdbai.data.mcp.models.McpRequest
import com.example.tmdbai.data.mcp.models.McpResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import org.json.JSONObject
import org.json.JSONArray

class McpHttpClient {
    
    private val httpClient = HttpClient(Android) {
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
            connectTimeoutMillis = 10000
        }
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }

    suspend fun <T> sendRequest(request: McpRequest): McpResponse<T> {
        return if (BuildConfig.USE_MOCK_DATA) {
            Log.d("MCP", "Using mock data for method: ${request.method}")
            sendMockRequest(request)
        } else {
            Log.d("MCP", "Attempting real request to: ${BuildConfig.MCP_SERVER_URL}")
            sendRealRequest(request)
        }
    }

    private suspend fun <T> sendRealRequest(request: McpRequest): McpResponse<T> {
        return try {
            if (BuildConfig.MCP_SERVER_URL.isBlank()) {
                Log.w("MCP", "MCP_SERVER_URL is blank, falling back to mock")
                return sendMockRequest(request)
            }

            Log.d("MCP", "Sending real request: ${request.method} to ${BuildConfig.MCP_SERVER_URL}")
            
            // Manually create JSON request body to avoid serialization issues
            val requestBody = buildString {
                append("{")
                append("\"method\":\"${request.method}\",")
                append("\"params\":{")
                request.params.entries.forEachIndexed { index, entry ->
                    if (index > 0) append(",")
                    append("\"${entry.key}\":\"${entry.value}\"")
                }
                append("}")
                append("}")
            }
            
            // Use only the exact endpoint provided in the URL
            val endpoints = listOf("")
            
            var lastError: Exception? = null
            var successfulResponse: String? = null
            
            for (endpoint in endpoints) {
                try {
                    val url = "${BuildConfig.MCP_SERVER_URL}$endpoint"
                    Log.d("MCP", "Trying endpoint: $url")
                    
                    val response = httpClient.post(url) {
                        contentType(ContentType.Application.Json)
                        setBody(requestBody)
                    }

                    val responseText = response.body<String>()
                    Log.d("MCP", "Response from $url: $responseText")
                    
                    // Check if response looks like an error page or is not JSON
                    if (responseText.contains("<!DOCTYPE html>") || 
                        responseText.contains("Cannot POST") ||
                        responseText.contains("<html") ||
                        responseText.contains("<title>Error</title>") ||
                        !responseText.trimStart().startsWith("{") && !responseText.trimStart().startsWith("[")) {
                        Log.w("MCP", "Endpoint $url returned error page or non-JSON response, trying next...")
                        continue
                    }
                    
                    // If we get here, the response looks good
                    successfulResponse = responseText
                    Log.d("MCP", "Found working endpoint: $url")
                    break
                    
                } catch (e: Exception) {
                    Log.w("MCP", "Endpoint $endpoint failed: ${e.message}")
                    lastError = e
                    continue
                }
            }
            
            if (successfulResponse == null) {
                throw lastError ?: Exception("All endpoints failed")
            }
            
            // Parse the JSON response manually
            try {
                val jsonResponse = parseJsonResponse(successfulResponse)
                val mcpResponse = McpResponse<T>(
                    success = jsonResponse["success"] as? Boolean ?: true,
                    data = jsonResponse["data"] as? T,
                    error = jsonResponse["error"] as? String,
                    message = jsonResponse["message"] as? String ?: "Real request successful"
                )
                Log.d("MCP", "Real request successful: ${request.method}")
                mcpResponse
            } catch (e: Exception) {
                Log.w("MCP", "Failed to parse JSON response: ${e.message}")
                // Return a successful response with the raw data
                McpResponse<T>(
                    success = true,
                    data = successfulResponse as? T,
                    error = null,
                    message = "Real request successful (raw response)"
                )
            }

        } catch (e: Exception) {
            Log.w("MCP", "Real request failed: ${e.message}, falling back to mock")
            // Graceful fallback to mock data
            val mockResponse = sendMockRequest<T>(request)
            // Add indicator that this is fallback data
            mockResponse.copy(
                message = "Using mock data (backend unavailable)"
            )
        }
    }

    private suspend fun <T> sendMockRequest(request: McpRequest): McpResponse<T> {
        // Simulate realistic network delay only in mock mode
        delay(300 + (Math.random() * 500).toLong())
        
        // Return mock response based on provided contracts
        @Suppress("UNCHECKED_CAST")
        return when (request.method) {
            "getPopularMovies" -> createMockPopularResponse() as McpResponse<T>
            "searchMovies" -> createMockSearchResponse(request.params["query"] ?: "") as McpResponse<T>
            "getMovieDetails" -> createMockDetailsResponse(
                request.params["movieId"]?.toIntOrNull() ?: 1
            ) as McpResponse<T>
            else -> McpResponse(
                success = false,
                data = null,
                error = "Unknown method: ${request.method}",
                message = "Method not supported"
            )
        }
    }

    // Mock response creators using exact contract JSON structure
    private fun createMockPopularResponse(): McpResponse<Any> {
        // Use the exact JSON structure from Get Popular Movies.rtf
        return McpResponse(
            success = true,
            data = mapOf(
                "movies" to listOf(
                    mapOf(
                        "id" to 1061474,
                        "title" to "Superman",
                        "description" to "Superman, a journalist in Metropolis, embarks on a journey to reconcile his Kryptonian heritage with his human upbringing as Clark Kent.",
                        "posterPath" to "/ombsmhYUqR4qqOLOxAyr5V8hbyv.jpg",
                        "backdropPath" to "/eU7IfdWq8KQy0oNd4kKXS0QUR08.jpg",
                        "rating" to 7.5,
                        "voteCount" to 2896,
                        "releaseDate" to "2025-07-09",
                        "genreIds" to listOf(878, 12, 28),
                        "popularity" to 361.1663,
                        "adult" to false
                    )
                ),
                "pagination" to mapOf(
                    "page" to 1,
                    "totalPages" to 52280,
                    "totalResults" to 1045594,
                    "hasNext" to true,
                    "hasPrevious" to false
                )
            ),
            error = null,
            message = "Mock data loaded successfully"
        )
    }

    private fun createMockSearchResponse(query: String): McpResponse<Any> {
        // Use structure from searchMovies.rtf
        return McpResponse(
            success = true,
            data = mapOf(
                "movies" to listOf(
                    mapOf(
                        "id" to 1924,
                        "title" to "Superman",
                        "description" to "Mild-mannered Clark Kent works as a reporter at the Daily Planet alongside his crush, Lois Lane.",
                        "posterPath" to "/d7px1FQxW4tngdACVRsCSaZq0Xl.jpg",
                        "backdropPath" to "/5PfHGXosySGs0l1JfeREspy3v6G.jpg",
                        "rating" to 7.153,
                        "voteCount" to 4050,
                        "releaseDate" to "1978-12-14",
                        "genreIds" to listOf(878, 28, 12),
                        "popularity" to 10.2992,
                        "adult" to false
                    )
                ),
                "pagination" to mapOf(
                    "page" to 1,
                    "totalPages" to 11,
                    "totalResults" to 211,
                    "hasNext" to true,
                    "hasPrevious" to false
                ),
                "searchQuery" to query
            ),
            error = null,
            message = "Mock search results for: $query"
        )
    }

    private fun createMockDetailsResponse(movieId: Int): McpResponse<Any> {
        // Use structure from Get Movie Details.rtf
        return McpResponse(
            success = true,
            data = mapOf(
                "movieDetails" to mapOf(
                    "id" to movieId,
                    "title" to "Nobody 2",
                    "description" to "Former assassin Hutch Mansell takes his family on a nostalgic vacation to a small-town theme park.",
                    "posterPath" to "/svXVRoRSu6zzFtCzkRsjZS7Lqpd.jpg",
                    "backdropPath" to "/mEW9XMgYDO6U0MJcIRqRuSwjzN5.jpg",
                    "rating" to 7.085,
                    "voteCount" to 177,
                    "releaseDate" to "2025-08-13",
                    "runtime" to 89,
                    "genres" to listOf(
                        mapOf("id" to 28, "name" to "Action"),
                        mapOf("id" to 53, "name" to "Thriller")
                    ),
                    "productionCompanies" to listOf(
                        mapOf(
                            "id" to 33,
                            "name" to "Universal Pictures",
                            "logoPath" to "/8lvHyhjr8oUKOOy2dKXoALWKdp0.png",
                            "originCountry" to "US"
                        )
                    ),
                    "budget" to 25000000,
                    "revenue" to 28583560,
                    "status" to "Released"
                )
            ),
            error = null,
            message = "Mock movie details loaded"
        )
    }

    fun close() {
        httpClient.close()
    }
    
    private fun parseJsonResponse(jsonString: String): Map<String, Any> {
        val jsonObject = JSONObject(jsonString)
        val result = mutableMapOf<String, Any>()
        
        // Convert JSONObject to Map<String, Any>
        for (key in jsonObject.keys()) {
            val value = jsonObject.get(key)
            result[key] = when (value) {
                is JSONObject -> {
                    val nestedMap = mutableMapOf<String, Any>()
                    for (nestedKey in value.keys()) {
                        nestedMap[nestedKey] = parseValue(value.get(nestedKey))
                    }
                    nestedMap
                }
                is JSONArray -> {
                    val list = mutableListOf<Any>()
                    for (i in 0 until value.length()) {
                        list.add(parseValue(value.get(i)))
                    }
                    list
                }
                else -> value
            }
        }
        
        return result
    }
    
    private fun parseValue(value: Any): Any {
        return when (value) {
            is JSONObject -> {
                val nestedMap = mutableMapOf<String, Any>()
                for (nestedKey in value.keys()) {
                    nestedMap[nestedKey] = parseValue(value.get(nestedKey))
                }
                nestedMap
            }
            is JSONArray -> {
                val list = mutableListOf<Any>()
                for (i in 0 until value.length()) {
                    list.add(parseValue(value.get(i)))
                }
                list
            }
            else -> value
        }
    }
}