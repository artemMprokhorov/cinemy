package org.studioapp.cinemy.data.mcp

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import org.json.JSONArray
import org.studioapp.cinemy.BuildConfig
import org.studioapp.cinemy.data.mapper.HttpRequestMapper.buildJsonRequestBody
import org.studioapp.cinemy.data.mapper.HttpResponseMapper.parseJsonArrayResponse
import org.studioapp.cinemy.data.mapper.HttpResponseMapper.parseJsonStringResponse
import org.studioapp.cinemy.data.mcp.models.McpRequest
import org.studioapp.cinemy.data.mcp.models.McpResponse
import org.studioapp.cinemy.data.model.StringConstants.EMPTY_STRING
import org.studioapp.cinemy.data.model.StringConstants.HTML_CANNOT_POST
import org.studioapp.cinemy.data.model.StringConstants.HTML_DOCTYPE
import org.studioapp.cinemy.data.model.StringConstants.HTML_ERROR_TITLE
import org.studioapp.cinemy.data.model.StringConstants.HTML_TAG
import org.studioapp.cinemy.data.model.StringConstants.HTTP_CONNECT_TIMEOUT_MS
import org.studioapp.cinemy.data.model.StringConstants.HTTP_ERROR_NETWORK_ERROR
import org.studioapp.cinemy.data.model.StringConstants.HTTP_ERROR_UNABLE_TO_CONNECT
import org.studioapp.cinemy.data.model.StringConstants.HTTP_REQUEST_TIMEOUT_MS
import org.studioapp.cinemy.data.model.StringConstants.JSON_ARRAY_START
import org.studioapp.cinemy.data.model.StringConstants.JSON_OPEN_BRACE
import org.studioapp.cinemy.data.model.StringConstants.MCP_MESSAGE_ALL_ENDPOINTS_FAILED

class McpHttpClient(private val context: Context) {
    private val fakeInterceptor = FakeInterceptor(context)

    private val httpClient = HttpClient(Android) {
        install(HttpTimeout) {
            requestTimeoutMillis = HTTP_REQUEST_TIMEOUT_MS
            connectTimeoutMillis = HTTP_CONNECT_TIMEOUT_MS
        }
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }

    /**
     * Sends MCP request to backend or returns mock data
     * @param request MCP request object
     * @return McpResponse with data or error
     */
    suspend fun <T> sendRequest(request: McpRequest): McpResponse<T> {
        return if (BuildConfig.USE_MOCK_DATA) {
            fakeInterceptor.intercept<T>(request)
        } else {
            sendRealRequest(request)
        }
    }

    /**
     * Sends real HTTP request to MCP backend
     * @param request MCP request object
     * @return McpResponse with parsed data or error
     */
    private suspend fun <T> sendRealRequest(request: McpRequest): McpResponse<T> {
        return runCatching {
            if (BuildConfig.MCP_SERVER_URL.isBlank()) {
                return fakeInterceptor.intercept<T>(request)
            }


            // Build JSON request body using mapper
            val requestBody = buildJsonRequestBody(request)

            // Use only the exact endpoint provided in the URL
            val endpoints = listOf(EMPTY_STRING)

            var lastError: Exception? = null
            var successfulResponse: String? = null

            for (endpoint in endpoints) {
                runCatching {
                    val url = "${BuildConfig.MCP_SERVER_URL}$endpoint"

                    val response = httpClient.post(url) {
                        contentType(ContentType.Application.Json)
                        setBody(requestBody)
                    }

                    val responseText = response.body<String>()

                    // Check if response looks like an error page or is not JSON
                    if (responseText.contains(HTML_DOCTYPE) ||
                        responseText.contains(HTML_CANNOT_POST) ||
                        responseText.contains(HTML_TAG) ||
                        responseText.contains(HTML_ERROR_TITLE) ||
                        !responseText.trimStart()
                            .startsWith(JSON_OPEN_BRACE) && !responseText.trimStart()
                            .startsWith(JSON_ARRAY_START)
                    ) {
                        return@runCatching null
                    }

                    // If we get here, the response looks good
                    successfulResponse = responseText
                    responseText
                }.onFailure { e ->
                    lastError = e as? Exception ?: Exception(e.message)
                }
            }

            if (successfulResponse == null) {
                throw lastError ?: Exception(MCP_MESSAGE_ALL_ENDPOINTS_FAILED)
            }

            // Parse the JSON response using mapper
            val responseText = successfulResponse!!
            runCatching {
                // Backend returns a direct array [{...}], so we need to handle this format
                val jsonArray = JSONArray(responseText)
                parseJsonArrayResponse<T>(jsonArray)
            }.getOrElse { e ->
                // Fallback: try to parse as direct object or return raw data
                parseJsonStringResponse<T>(responseText)
            }

        }.getOrElse { e ->
            // For production version, don't fallback to mock data - show network error
            McpResponse<T>(
                success = false,
                data = null,
                error = HTTP_ERROR_NETWORK_ERROR.format(e.message),
                message = HTTP_ERROR_UNABLE_TO_CONNECT
            )
        }
    }


    /**
     * Closes HTTP client and cleans up resources
     */
    fun close() {
        httpClient.close()
    }


}