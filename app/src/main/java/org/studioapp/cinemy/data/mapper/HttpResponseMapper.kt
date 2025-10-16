package org.studioapp.cinemy.data.mapper

import org.json.JSONArray
import org.json.JSONObject
import org.studioapp.cinemy.data.mapper.HttpResponseMapper.jsonArrayToList
import org.studioapp.cinemy.data.mapper.HttpResponseMapper.jsonObjectToMap
import org.studioapp.cinemy.data.mapper.HttpResponseMapper.parseJsonObject
import org.studioapp.cinemy.data.mapper.HttpResponseMapper.parseJsonResponse
import org.studioapp.cinemy.data.mcp.models.McpResponse
import org.studioapp.cinemy.data.model.StringConstants.MCP_MESSAGE_REAL_REQUEST_RAW_RESPONSE
import org.studioapp.cinemy.data.model.StringConstants.MCP_MESSAGE_REAL_REQUEST_SUCCESSFUL

/**
 * Mapper for HTTP response parsing and JSON conversion.
 *
 * Provides utilities to convert `JSONObject`/`JSONArray` instances and raw JSON strings
 * into native Kotlin structures (`Map<String, Any>`, `List<Any>`) and to wrap parsed
 * payloads into `McpResponse<T>` for higher layers.
 */
object HttpResponseMapper {

    /**
     * Parses a JSON array response and maps the first element into an `McpResponse<T>`.
     *
     * The first element of the `jsonArray` must be a `JSONObject`. It is converted to a
     * `Map<String, Any>` via [parseJsonObject] and assigned to the `data` field (cast to `T`).
     *
     * @param jsonArray JSON array response where the first element represents the payload.
     * @return `McpResponse<T>` with `success=true`, `message=MCP_MESSAGE_REAL_REQUEST_SUCCESSFUL`, and parsed `data` when the array is not empty.
     * @throws Exception if the array is empty or its first element is not a valid `JSONObject`.
     */
    fun <T> parseJsonArrayResponse(jsonArray: JSONArray): McpResponse<T> {
        return if (jsonArray.length() > 0) {
            val firstObject = jsonArray.getJSONObject(0)
            val jsonResponse = parseJsonObject(firstObject)
            McpResponse<T>(
                success = true,
                data = jsonResponse as? T,
                error = null,
                message = MCP_MESSAGE_REAL_REQUEST_SUCCESSFUL
            )
        } else {
            throw Exception("Empty response array")
        }
    }

    /**
     * Parses a JSON string response and maps it into an `McpResponse<T>`.
     *
     * The method attempts to parse the string via [parseJsonResponse]. If parsing fails,
     * it returns a successful response containing the raw `jsonString` as `data` to aid
     * troubleshooting while maintaining a consistent response contract.
     *
     * @param jsonString Raw JSON string response.
     * @return `McpResponse<T>` with `success=true`. When parsing succeeds, `data` contains the parsed map (cast to `T`) and `message=MCP_MESSAGE_REAL_REQUEST_SUCCESSFUL`. When parsing fails, `data` contains the original `jsonString` (cast to `T`) and `message=MCP_MESSAGE_REAL_REQUEST_RAW_RESPONSE`.
     */
    fun <T> parseJsonStringResponse(jsonString: String): McpResponse<T> {
        return runCatching {
            val jsonResponse = parseJsonResponse(jsonString)
            McpResponse<T>(
                success = true,
                data = jsonResponse as? T,
                error = null,
                message = MCP_MESSAGE_REAL_REQUEST_SUCCESSFUL
            )
        }.getOrElse { e ->
            // Return a successful response with the raw data
            McpResponse<T>(
                success = true,
                data = jsonString as? T,
                error = null,
                message = MCP_MESSAGE_REAL_REQUEST_RAW_RESPONSE
            )
        }
    }

    /**
     * Converts a `JSONObject` to a `Map<String, Any>` recursively.
     *
     * Nested `JSONObject` and `JSONArray` instances are converted via [jsonObjectToMap]
     * and [jsonArrayToList] respectively.
     *
     * @param jsonObject JSON object to convert.
     * @return Map representation of the given JSON object.
     * @throws org.json.JSONException if a key lookup fails inside the JSON object.
     */
    fun jsonObjectToMap(jsonObject: JSONObject): Map<String, Any> {
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

    /**
     * Converts a `JSONArray` to a `List<Any>` recursively.
     *
     * Nested `JSONObject` and `JSONArray` elements are converted via [jsonObjectToMap]
     * and [jsonArrayToList] respectively.
     *
     * @param jsonArray JSON array to convert.
     * @return List representation of the given JSON array.
     * @throws org.json.JSONException if an index access fails inside the JSON array.
     */
    fun jsonArrayToList(jsonArray: JSONArray): List<Any> {
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

    /**
     * Parses a `JSONObject` into a `Map<String, Any>` recursively.
     *
     * This function iterates keys in the provided object and converts nested values
     * using [jsonObjectToMap] and [jsonArrayToList].
     *
     * @param jsonObject JSON object to parse.
     * @return Map representation of the provided JSON object.
     * @throws org.json.JSONException if a key lookup fails.
     */
    fun parseJsonObject(jsonObject: JSONObject): Map<String, Any> {
        val result = mutableMapOf<String, Any>()

        // Convert JSONObject to Map<String, Any>
        for (key in jsonObject.keys()) {
            val value = jsonObject.get(key)
            result[key] = when (value) {
                is JSONObject -> {
                    val nestedMap = mutableMapOf<String, Any>()
                    for (nestedKey in value.keys()) {
                        val nestedValue = value.get(nestedKey)
                        nestedMap[nestedKey] = when (nestedValue) {
                            is JSONObject -> jsonObjectToMap(nestedValue)
                            is JSONArray -> jsonArrayToList(nestedValue)
                            else -> nestedValue
                        }
                    }
                    nestedMap
                }

                is JSONArray -> jsonArrayToList(value)
                else -> value
            }
        }
        return result
    }

    /**
     * Parses a JSON string into a `Map<String, Any>` recursively.
     *
     * @param jsonString JSON string to parse.
     * @return Map representation of the JSON payload.
     * @throws org.json.JSONException if the string is not valid JSON or lookups fail.
     */
    fun parseJsonResponse(jsonString: String): Map<String, Any> {
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

    /**
     * Recursively parses a JSON value to native Kotlin types.
     *
     * - `JSONObject` → `Map<String, Any>`
     * - `JSONArray` → `List<Any>`
     * - Other values are returned unchanged.
     *
     * @param value JSON value to parse.
     * @return Parsed native representation of the input value.
     */
    fun parseValue(value: Any): Any {
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
