package com.example.tmdbai.data.mcp

import android.content.Context
import android.util.Log
import com.example.tmdbai.BuildConfig
import com.example.tmdbai.data.mcp.models.McpRequest
import com.example.tmdbai.data.mcp.models.McpResponse
import com.example.tmdbai.data.model.StringConstants
import kotlinx.coroutines.delay
import org.json.JSONObject

/**
 * Fake interceptor for dummy configuration that provides mock responses
 * based on asset files instead of hardcoded data generation.
 */
class FakeInterceptor(private val context: Context) {

    /**
     * Intercepts MCP requests and returns mock responses from assets
     */
    suspend fun <T> intercept(request: McpRequest): McpResponse<T> {
        // Simulate realistic network delay
        delay(StringConstants.FAKE_INTERCEPTOR_DELAY_BASE_MS + (Math.random() * StringConstants.FAKE_INTERCEPTOR_DELAY_RANDOM_MAX_MS).toLong())

        return when (request.method) {
            StringConstants.MCP_METHOD_GET_POPULAR_MOVIES -> {
                loadMockDataFromAssetsWithPagination(
                    StringConstants.ASSET_MOCK_MOVIES,
                    request.params[StringConstants.FIELD_PAGE]?.toIntOrNull()
                        ?: StringConstants.DEFAULT_PAGE_NUMBER
                ) as McpResponse<T>
            }

            StringConstants.MCP_METHOD_GET_MOVIE_DETAILS -> {
                loadMockDataFromAssets(StringConstants.ASSET_MOCK_MOVIE_DETAILS) as McpResponse<T>
            }

            else -> {
                McpResponse(
                    success = false,
                    data = null,
                    error = StringConstants.ERROR_UNKNOWN_METHOD.format(request.method),
                    message = StringConstants.ERROR_UNKNOWN_METHOD.format(request.method)
                )
            }
        }
    }

    /**
     * Loads mock data from assets with pagination support
     */
    private fun loadMockDataFromAssetsWithPagination(
        fileName: String,
        page: Int
    ): McpResponse<Any> {
        return runCatching {
            val jsonString = loadJsonFromAssets(fileName)
            if (jsonString != null) {
                val jsonResponse = parseJsonResponse(jsonString)

                // Create paginated response based on page
                val allMovies =
                    (jsonResponse[StringConstants.FIELD_DATA] as? Map<String, Any>)?.get(
                        StringConstants.FIELD_MOVIES
                    ) as? List<Map<String, Any>> ?: emptyList()
                val moviesPerPage = StringConstants.FAKE_INTERCEPTOR_MOVIES_PER_PAGE
                val startIndex = (page - 1) * moviesPerPage
                val endIndex = minOf(startIndex + moviesPerPage, allMovies.size)
                val pageMovies = allMovies.subList(startIndex, endIndex)

                val totalPages =
                    StringConstants.FAKE_INTERCEPTOR_TOTAL_PAGES // We have 3 pages of mock data
                val hasNext = page < totalPages
                val hasPrevious = page > StringConstants.DEFAULT_PAGE_NUMBER

                val paginatedData = mapOf(
                    StringConstants.FIELD_MOVIES to pageMovies,
                    StringConstants.FIELD_PAGINATION to mapOf(
                        StringConstants.FIELD_PAGE to page,
                        StringConstants.FIELD_TOTAL_PAGES to totalPages,
                        StringConstants.FIELD_TOTAL_RESULTS to allMovies.size,
                        StringConstants.FIELD_HAS_NEXT to hasNext,
                        StringConstants.FIELD_HAS_PREVIOUS to hasPrevious
                    )
                )

                McpResponse<Any>(
                    success = jsonResponse[StringConstants.FIELD_SUCCESS] as? Boolean ?: true,
                    data = paginatedData,
                    error = null,
                    message = jsonResponse[StringConstants.FIELD_MESSAGE] as? String
                        ?: StringConstants.MCP_MESSAGE_MOCK_DATA_LOADED_SUCCESSFULLY
                )
            } else {
                createFallbackResponse()
            }
        }.getOrElse { e ->
            if (BuildConfig.DEBUG) {
                Log.e("FakeInterceptor", "Error loading mock data from assets", e)
            }
            createFallbackResponse()
        }
    }

    /**
     * Loads mock data from assets for single item responses
     */
    private fun loadMockDataFromAssets(fileName: String): McpResponse<Any> {
        return runCatching {
            val jsonString = loadJsonFromAssets(fileName)
            if (jsonString != null) {
                val jsonResponse = parseJsonResponse(jsonString)
                McpResponse<Any>(
                    success = jsonResponse[StringConstants.FIELD_SUCCESS] as? Boolean ?: true,
                    data = jsonResponse[StringConstants.FIELD_DATA],
                    error = null,
                    message = jsonResponse[StringConstants.FIELD_MESSAGE] as? String
                        ?: StringConstants.MCP_MESSAGE_MOCK_DATA_LOADED_SUCCESSFULLY
                )
            } else {
                createFallbackResponse()
            }
        }.getOrElse { e ->
            if (BuildConfig.DEBUG) {
                Log.e("FakeInterceptor", "Error loading mock data from assets", e)
            }
            createFallbackResponse()
        }
    }

    /**
     * Loads JSON content from assets
     */
    private fun loadJsonFromAssets(fileName: String): String? {
        return runCatching {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        }.getOrElse { e ->
            if (BuildConfig.DEBUG) {
                Log.e("FakeInterceptor", "Error loading asset: $fileName", e)
            }
            null
        }
    }

    /**
     * Parses JSON string to Map
     */
    private fun parseJsonResponse(jsonString: String): Map<String, Any> {
        val jsonObject = JSONObject(jsonString)
        return jsonObjectToMap(jsonObject)
    }

    /**
     * Converts JSONObject to Map<String, Any>
     */
    private fun jsonObjectToMap(jsonObject: JSONObject): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        val keys = jsonObject.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = jsonObject.get(key)
            map[key] = when (value) {
                is JSONObject -> jsonObjectToMap(value)
                is org.json.JSONArray -> jsonArrayToList(value)
                else -> value
            }
        }
        return map
    }

    /**
     * Converts JSONArray to List<Any>
     */
    private fun jsonArrayToList(jsonArray: org.json.JSONArray): List<Any> {
        val list = mutableListOf<Any>()
        for (i in 0 until jsonArray.length()) {
            val value = jsonArray.get(i)
            list.add(
                when (value) {
                    is JSONObject -> jsonObjectToMap(value)
                    is org.json.JSONArray -> jsonArrayToList(value)
                    else -> value
                }
            )
        }
        return list
    }

    /**
     * Creates a fallback response when asset loading fails
     */
    private fun createFallbackResponse(): McpResponse<Any> {
        return McpResponse(
            success = false,
            data = null,
            error = StringConstants.ERROR_LOADING_MOCK_DATA,
            message = StringConstants.ERROR_LOADING_MOCK_DATA
        )
    }
}
