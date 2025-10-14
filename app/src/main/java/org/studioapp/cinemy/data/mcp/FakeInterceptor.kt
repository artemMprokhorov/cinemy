package org.studioapp.cinemy.data.mcp

import android.content.Context
import kotlinx.coroutines.delay
import org.json.JSONObject
import org.studioapp.cinemy.data.mcp.models.McpRequest
import org.studioapp.cinemy.data.mcp.models.McpResponse
import org.studioapp.cinemy.data.model.StringConstants.ASSET_MOCK_MOVIES
import org.studioapp.cinemy.data.model.StringConstants.ASSET_MOCK_MOVIE_DETAILS
import org.studioapp.cinemy.data.model.StringConstants.DEFAULT_PAGE_NUMBER
import org.studioapp.cinemy.data.model.StringConstants.ERROR_LOADING_MOCK_DATA
import org.studioapp.cinemy.data.model.StringConstants.ERROR_UNKNOWN_METHOD
import org.studioapp.cinemy.data.model.StringConstants.FAKE_INTERCEPTOR_DELAY_BASE_MS
import org.studioapp.cinemy.data.model.StringConstants.FAKE_INTERCEPTOR_DELAY_RANDOM_MAX_MS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_DATA
import org.studioapp.cinemy.data.model.StringConstants.FIELD_HAS_NEXT
import org.studioapp.cinemy.data.model.StringConstants.FIELD_HAS_PREVIOUS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_MESSAGE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_MOVIES
import org.studioapp.cinemy.data.model.StringConstants.FIELD_PAGE
import org.studioapp.cinemy.data.model.StringConstants.FIELD_PAGINATION
import org.studioapp.cinemy.data.model.StringConstants.FIELD_SUCCESS
import org.studioapp.cinemy.data.model.StringConstants.FIELD_TOTAL_PAGES
import org.studioapp.cinemy.data.model.StringConstants.FIELD_TOTAL_RESULTS
import org.studioapp.cinemy.data.model.StringConstants.MCP_MESSAGE_MOCK_DATA_LOADED_SUCCESSFULLY
import org.studioapp.cinemy.data.model.StringConstants.MCP_METHOD_GET_MOVIE_DETAILS
import org.studioapp.cinemy.data.model.StringConstants.MCP_METHOD_GET_POPULAR_MOVIES
import org.studioapp.cinemy.data.util.AssetUtils.loadJsonFromAssets

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
        delay(FAKE_INTERCEPTOR_DELAY_BASE_MS + (Math.random() * FAKE_INTERCEPTOR_DELAY_RANDOM_MAX_MS).toLong())

        return when (request.method) {
            MCP_METHOD_GET_POPULAR_MOVIES -> {
                loadMockDataFromAssetsWithPagination(
                    ASSET_MOCK_MOVIES,
                    request.params[FIELD_PAGE]?.toIntOrNull()
                        ?: DEFAULT_PAGE_NUMBER
                ) as McpResponse<T>
            }

            MCP_METHOD_GET_MOVIE_DETAILS -> {
                loadMockDataFromAssets(ASSET_MOCK_MOVIE_DETAILS) as McpResponse<T>
            }

            else -> {
                McpResponse(
                    success = false,
                    data = null,
                    error = ERROR_UNKNOWN_METHOD.format(request.method),
                    message = ERROR_UNKNOWN_METHOD.format(request.method)
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
            val jsonString = loadJsonFromAssets(context, fileName)
            if (jsonString != null) {
                val jsonResponse = parseJsonResponse(jsonString)

                // Create paginated response based on page
                val allMovies =
                    (jsonResponse[FIELD_DATA] as? Map<String, Any>)?.get(
                        FIELD_MOVIES
                    ) as? List<Map<String, Any>> ?: emptyList()
                val moviesPerPage = 15 // Default movies per page
                val startIndex = (page - 1) * moviesPerPage
                val endIndex = minOf(startIndex + moviesPerPage, allMovies.size)
                val pageMovies = allMovies.subList(startIndex, endIndex)

                val totalPages = 3 // We have 3 pages of mock data
                val hasNext = page < totalPages
                val hasPrevious = page > DEFAULT_PAGE_NUMBER

                val paginatedData = mapOf(
                    FIELD_MOVIES to pageMovies,
                    FIELD_PAGINATION to mapOf(
                        FIELD_PAGE to page,
                        FIELD_TOTAL_PAGES to totalPages,
                        FIELD_TOTAL_RESULTS to allMovies.size,
                        FIELD_HAS_NEXT to hasNext,
                        FIELD_HAS_PREVIOUS to hasPrevious
                    )
                )

                McpResponse<Any>(
                    success = jsonResponse[FIELD_SUCCESS] as? Boolean ?: true,
                    data = paginatedData,
                    error = null,
                    message = jsonResponse[FIELD_MESSAGE] as? String
                        ?: MCP_MESSAGE_MOCK_DATA_LOADED_SUCCESSFULLY
                )
            } else {
                createFallbackResponse()
            }
        }.getOrElse { e ->
            createFallbackResponse()
        }
    }

    /**
     * Loads mock data from assets for single item responses
     */
    private fun loadMockDataFromAssets(fileName: String): McpResponse<Any> {
        return runCatching {
            val jsonString = loadJsonFromAssets(context, fileName)
            if (jsonString != null) {
                val jsonResponse = parseJsonResponse(jsonString)
                McpResponse<Any>(
                    success = jsonResponse[FIELD_SUCCESS] as? Boolean ?: true,
                    data = jsonResponse[FIELD_DATA],
                    error = null,
                    message = jsonResponse[FIELD_MESSAGE] as? String
                        ?: MCP_MESSAGE_MOCK_DATA_LOADED_SUCCESSFULLY
                )
            } else {
                createFallbackResponse()
            }
        }.getOrElse { e ->
            createFallbackResponse()
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
            error = ERROR_LOADING_MOCK_DATA,
            message = ERROR_LOADING_MOCK_DATA
        )
    }
}
