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

/**
 * HTTP transport for the MCP backend.
 *
 * Behavior:
 * - If `BuildConfig.USE_MOCK_DATA == true`, requests are handled by `FakeInterceptor` (assets-based mocks).
 * - Otherwise, real HTTP requests are sent to `BuildConfig.MCP_SERVER_URL` using Ktor.
 * - When `BuildConfig.MCP_SERVER_URL` is blank, real-mode transparently falls back to `FakeInterceptor`.
 * - Real responses are validated to be JSON and then parsed; array payloads are supported as first-class.
 *
 * This client never throws for public APIs; all failures are expressed via `McpResponse`.
 *
 * @param context Android context used by the mock interceptor for asset access.
 */
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
     * Sends an MCP request either to the real backend or to the mock interceptor depending on build flags.
     *
     * Routing rules:
     * - When `BuildConfig.USE_MOCK_DATA == true` → handled by `FakeInterceptor` (local assets, no network).
     * - Otherwise → delegates to the internal HTTP path.
     *
     * The method is non-throwing. Network and parsing failures are represented in the returned `McpResponse` with
     * `success=false`, `data=null`, and `error`/`message` populated.
     *
     * @param request the MCP request to send; see `McpRequest` for structure and parameters.
     * @return a typed `McpResponse<T>` containing the parsed payload on success, or an error contract on failure.
     */
    suspend fun <T> sendRequest(request: McpRequest): McpResponse<T> {
        return if (BuildConfig.USE_MOCK_DATA) {
            fakeInterceptor.intercept<T>(request)
        } else {
            sendRealRequest(request)
        }
    }

    /**
     * Sends a real HTTP request to the MCP backend.
     *
     * Details:
     * - Falls back to `FakeInterceptor` when `BuildConfig.MCP_SERVER_URL` is blank.
     * - Builds a JSON body via `HttpRequestMapper.buildJsonRequestBody` and posts it with `Content-Type: application/json`.
     * - Validates textual responses, rejecting HTML/error pages and non-JSON payloads.
     * - Parsing strategy: tries array-first (`parseJsonArrayResponse`), then falls back to string/object parsing (`parseJsonStringResponse`).
     * - On any failure, returns `McpResponse(success=false, data=null, error=..., message=HTTP_ERROR_UNABLE_TO_CONNECT)`.
     *
     * Note: This method is private; all exceptions are handled internally and converted to `McpResponse` by callers.
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
     * Closes the underlying Ktor `HttpClient` and releases resources.
     *
     * The method is idempotent and non-throwing under normal circumstances.
     */
    fun close() {
        httpClient.close()
    }


}