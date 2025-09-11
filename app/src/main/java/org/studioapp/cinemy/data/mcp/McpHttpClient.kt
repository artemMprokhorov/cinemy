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
import org.json.JSONObject
import org.studioapp.cinemy.BuildConfig
import org.studioapp.cinemy.data.mcp.models.McpRequest
import org.studioapp.cinemy.data.mcp.models.McpResponse
import org.studioapp.cinemy.data.model.StringConstants
import java.io.InputStream

class McpHttpClient(private val context: Context) {
    private val fakeInterceptor = FakeInterceptor(context)

    private val httpClient = HttpClient(Android) {
        install(HttpTimeout) {
            requestTimeoutMillis = StringConstants.HTTP_REQUEST_TIMEOUT_MS
            connectTimeoutMillis = StringConstants.HTTP_CONNECT_TIMEOUT_MS
        }
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }

    suspend fun <T> sendRequest(request: McpRequest): McpResponse<T> {
        return if (BuildConfig.USE_MOCK_DATA) {
            fakeInterceptor.intercept<T>(request)
        } else {
            sendRealRequest(request)
        }
    }

    private suspend fun <T> sendRealRequest(request: McpRequest): McpResponse<T> {
        return runCatching {
            if (BuildConfig.MCP_SERVER_URL.isBlank()) {
                return fakeInterceptor.intercept<T>(request)
            }


            // Manually create JSON request body to avoid serialization issues
            val requestBody = buildString {
                append(StringConstants.JSON_OPEN_BRACE)
                append(StringConstants.JSON_METHOD_FIELD.format(request.method))
                append(StringConstants.JSON_PARAMS_FIELD)
                request.params.entries.forEachIndexed { index, entry ->
                    if (index > 0) append(StringConstants.JSON_COMMA)
                    append(StringConstants.JSON_PARAM_ENTRY.format(entry.key, entry.value))
                }
                append(StringConstants.JSON_CLOSE_BRACE)
                append(StringConstants.JSON_CLOSE_BRACE)
            }

            // Use only the exact endpoint provided in the URL
            val endpoints = listOf(StringConstants.EMPTY_STRING)

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
                    if (responseText.contains(StringConstants.HTML_DOCTYPE) ||
                        responseText.contains(StringConstants.HTML_CANNOT_POST) ||
                        responseText.contains(StringConstants.HTML_TAG) ||
                        responseText.contains(StringConstants.HTML_ERROR_TITLE) ||
                        !responseText.trimStart()
                            .startsWith(StringConstants.JSON_OPEN_BRACE) && !responseText.trimStart()
                            .startsWith(StringConstants.JSON_ARRAY_START)
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
                throw lastError ?: Exception(StringConstants.MCP_MESSAGE_ALL_ENDPOINTS_FAILED)
            }

            // Parse the JSON response manually
            val responseText = successfulResponse!!
            runCatching {
                val jsonResponse = parseJsonResponse(responseText)
                val mcpResponse = McpResponse<T>(
                    success = jsonResponse[StringConstants.FIELD_SUCCESS] as? Boolean ?: true,
                    data = jsonResponse[StringConstants.FIELD_DATA] as? T,
                    error = jsonResponse[StringConstants.FIELD_ERROR] as? String,
                    message = jsonResponse[StringConstants.FIELD_MESSAGE] as? String
                        ?: StringConstants.MCP_MESSAGE_REAL_REQUEST_SUCCESSFUL
                )
                mcpResponse
            }.getOrElse { e ->
                // Return a successful response with the raw data
                McpResponse<T>(
                    success = true,
                    data = responseText as? T,
                    error = null,
                    message = StringConstants.MCP_MESSAGE_REAL_REQUEST_RAW_RESPONSE
                )
            }

        }.getOrElse { e ->
            // Graceful fallback to mock data
            val mockResponse = fakeInterceptor.intercept<T>(request)
            // Add indicator that this is fallback data
            mockResponse.copy(
                message = StringConstants.MCP_MESSAGE_USING_MOCK_BACKEND_UNAVAILABLE
            )
        }
    }


    // Helper method to load JSON from assets
    private fun loadJsonFromAssets(fileName: String): String? {
        return runCatching {
            val inputStream: InputStream = context.assets.open(fileName)
            inputStream.bufferedReader().use { it.readText() }
        }.getOrElse { e ->
            null
        }
    }

    // Helper method to convert JSONObject to Map
    private fun jsonObjectToMap(jsonObject: JSONObject): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        val keys = jsonObject.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = jsonObject.get(key)
            map[key] = when (value) {
                is JSONObject -> jsonObjectToMap(value)
                is JSONArray -> jsonArrayToList(value)
                else -> value
            }
        }
        return map
    }

    // Helper method to convert JSONArray to List
    private fun jsonArrayToList(jsonArray: JSONArray): List<Any> {
        val list = mutableListOf<Any>()
        for (i in 0 until jsonArray.length()) {
            val value = jsonArray.get(i)
            list.add(
                when (value) {
                    is JSONObject -> jsonObjectToMap(value)
                    is JSONArray -> jsonArrayToList(value)
                    else -> value
                }
            )
        }
        return list
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