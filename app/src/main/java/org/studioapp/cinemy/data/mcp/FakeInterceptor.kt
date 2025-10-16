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
 *
 * This interceptor simulates network behavior by adding realistic delays
 * and returning mock data from asset files for development and testing purposes.
 * It supports pagination for movie lists and handles both successful responses
 * and error scenarios.
 *
 * @param context Android context for accessing asset files
 */
class FakeInterceptor(private val context: Context) {

    /**
     * Intercepts MCP requests and returns mock responses from assets.
     *
     * This method simulates network behavior by adding a random delay and
     * returning mock data based on the request method. It supports two main
     * operations: fetching popular movies with pagination and getting movie details.
     *
     * @param T The expected response data type
     * @param request The MCP request containing method and parameters
     * @return McpResponse<T> containing mock data or error information
     *
     * @throws No exceptions are thrown; errors are returned as failed McpResponse
     *
     * @see McpRequest for request structure
     * @see McpResponse for response structure
     * @see loadMockDataFromAssetsWithPagination for paginated movie data
     * @see loadMockDataFromAssets for single item data
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
     * Loads mock data from assets with pagination support.
     *
     * This method reads JSON data from assets, parses it, and creates a paginated
     * response by slicing the data based on the requested page. It handles pagination
     * metadata including total pages, hasNext, hasPrevious flags.
     *
     * @param fileName The asset file name to load data from
     * @param page The requested page number (1-based)
     * @return McpResponse<Any> containing paginated movie data or error response
     *
     * @throws No exceptions are thrown; errors are handled via runCatching
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
     * Loads mock data from assets for single item responses.
     *
     * This method reads JSON data from assets and returns it as-is without
     * pagination processing. Used for single item responses like movie details.
     *
     * @param fileName The asset file name to load data from
     * @return McpResponse<Any> containing the loaded data or error response
     *
     * @throws No exceptions are thrown; errors are handled via runCatching
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
     * Parses JSON string to Map.
     *
     * Converts a JSON string into a native Kotlin Map structure for easier
     * data manipulation and access.
     *
     * @param jsonString The JSON string to parse
     * @return Map<String, Any> representing the parsed JSON structure
     */
    private fun parseJsonResponse(jsonString: String): Map<String, Any> {
        val jsonObject = JSONObject(jsonString)
        return jsonObjectToMap(jsonObject)
    }

    /**
     * Converts JSONObject to Map<String, Any>.
     *
     * Recursively converts a JSONObject to a native Kotlin Map, handling
     * nested objects and arrays appropriately.
     *
     * @param jsonObject The JSONObject to convert
     * @return Map<String, Any> representing the JSONObject structure
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
     * Converts JSONArray to List<Any>.
     *
     * Recursively converts a JSONArray to a native Kotlin List, handling
     * nested objects and arrays appropriately.
     *
     * @param jsonArray The JSONArray to convert
     * @return List<Any> representing the JSONArray structure
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
     * Creates a fallback response when asset loading fails.
     *
     * Returns a standardized error response when asset files cannot be loaded
     * or parsed, providing consistent error handling across the interceptor.
     *
     * @return McpResponse<Any> containing error information
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
