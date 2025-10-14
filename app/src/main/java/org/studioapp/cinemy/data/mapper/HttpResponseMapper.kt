package org.studioapp.cinemy.data.mapper

import org.json.JSONArray
import org.json.JSONObject
import org.studioapp.cinemy.data.mcp.models.McpResponse
import org.studioapp.cinemy.data.model.StringConstants.MCP_MESSAGE_REAL_REQUEST_RAW_RESPONSE
import org.studioapp.cinemy.data.model.StringConstants.MCP_MESSAGE_REAL_REQUEST_SUCCESSFUL

/**
 * Mapper for HTTP response parsing and JSON conversion
 * Handles conversion between JSON objects and native Kotlin types
 */
object HttpResponseMapper {

    /**
     * Parses JSON array response to McpResponse
     * @param jsonArray JSON array response
     * @return McpResponse with parsed data
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
     * Parses JSON string response to McpResponse
     * @param jsonString JSON string response
     * @return McpResponse with parsed data
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
     * Converts JSONObject to Map<String, Any>
     * @param jsonObject JSON object to convert
     * @return Map representation of JSON object
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
     * Converts JSONArray to List<Any>
     * @param jsonArray JSON array to convert
     * @return List representation of JSON array
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
     * Parses JSONObject to Map<String, Any>
     * @param jsonObject JSON object to parse
     * @return Map representation of JSON object
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
     * Parses JSON string to Map<String, Any>
     * @param jsonString JSON string to parse
     * @return Map representation of JSON string
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
     * Recursively parses JSON values to native types
     * @param value JSON value to parse
     * @return Parsed native value
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
