package org.studioapp.cinemy.data.mapper

import org.studioapp.cinemy.data.mcp.models.McpRequest
import org.studioapp.cinemy.data.model.StringConstants.JSON_CLOSE_BRACE
import org.studioapp.cinemy.data.model.StringConstants.JSON_COMMA
import org.studioapp.cinemy.data.model.StringConstants.JSON_METHOD_FIELD
import org.studioapp.cinemy.data.model.StringConstants.JSON_OPEN_BRACE
import org.studioapp.cinemy.data.model.StringConstants.JSON_PARAMS_FIELD
import org.studioapp.cinemy.data.model.StringConstants.JSON_PARAM_ENTRY

/**
 * Mapper for HTTP request building and JSON serialization
 * Handles conversion of MCP requests to JSON format
 */
object HttpRequestMapper {

    /**
     * Builds JSON request body from MCP request
     * Creates a properly formatted JSON string for MCP backend communication
     * @param request MCP request object containing method and parameters
     * @return JSON string representation of the request in format: {"method":"methodName","params":{"key":"value"}}
     * @throws IllegalArgumentException if request is invalid
     */
    fun buildJsonRequestBody(request: McpRequest): String {
        if (!validateRequest(request)) {
            throw IllegalArgumentException("Invalid MCP request: method and params must be provided")
        }

        return buildString {
            append(JSON_OPEN_BRACE)
            append(JSON_METHOD_FIELD.format(request.method))
            append(JSON_PARAMS_FIELD)
            request.params.entries.forEachIndexed { index, entry ->
                if (index > 0) append(JSON_COMMA)
                append(JSON_PARAM_ENTRY.format(entry.key, entry.value))
            }
            append(JSON_CLOSE_BRACE)
            append(JSON_CLOSE_BRACE)
        }
    }

    /**
     * Validates MCP request parameters
     * Checks if the request has a non-empty method and at least one parameter
     * @param request MCP request object to validate
     * @return true if request is valid (has method and params), false otherwise
     */
    fun validateRequest(request: McpRequest): Boolean {
        return request.method.isNotBlank() && request.params.isNotEmpty()
    }

    /**
     * Creates a formatted parameter entry for JSON
     * Formats a key-value pair into JSON parameter format: "key":"value"
     * @param key Parameter key (will be escaped if needed)
     * @param value Parameter value (will be converted to JSON-safe string)
     * @return Formatted JSON parameter entry as string
     */
    fun formatParameterEntry(key: String, value: Any): String {
        return JSON_PARAM_ENTRY.format(key, value)
    }

    /**
     * Escapes JSON string values to prevent injection
     * Applies JSON string escaping rules to prevent malformed JSON and security issues
     * @param value String value to escape
     * @return Escaped JSON string with proper escape sequences for backslash, quotes, and control characters
     */
    fun escapeJsonString(value: String): String {
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\b", "\\b")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
    }

    /**
     * Converts parameter value to JSON-safe string
     * Handles different data types and converts them to proper JSON format
     * @param value Parameter value of any type
     * @return JSON-safe string representation with proper formatting for strings, numbers, booleans, maps, and lists
     */
    fun parameterValueToJsonString(value: Any): String {
        return when (value) {
            is String -> "\"${escapeJsonString(value)}\""
            is Number -> value.toString()
            is Boolean -> value.toString()
            is Map<*, *> -> buildJsonFromMap(value as Map<String, Any>)
            is List<*> -> buildJsonFromList(value)
            else -> "\"${escapeJsonString(value.toString())}\""
        }
    }

    /**
     * Builds JSON from Map
     * Recursively converts a Map to JSON object string format
     * @param map Map to convert to JSON
     * @return JSON string representation of the map as {"key":"value",...}
     */
    private fun buildJsonFromMap(map: Map<String, Any>): String {
        return buildString {
            append(JSON_OPEN_BRACE)
            map.entries.forEachIndexed { index, entry ->
                if (index > 0) append(JSON_COMMA)
                append("\"${escapeJsonString(entry.key)}\":")
                append(parameterValueToJsonString(entry.value))
            }
            append(JSON_CLOSE_BRACE)
        }
    }

    /**
     * Builds JSON from List
     * Recursively converts a List to JSON array string format
     * @param list List to convert to JSON
     * @return JSON string representation of the list as [item1,item2,...]
     */
    private fun buildJsonFromList(list: List<*>): String {
        return buildString {
            append("[")
            list.forEachIndexed { index, item ->
                if (index > 0) append(JSON_COMMA)
                when (item) {
                    is String -> append("\"${escapeJsonString(item)}\"")
                    is Number -> append(item.toString())
                    is Boolean -> append(item.toString())
                    is Map<*, *> -> append(buildJsonFromMap(item as Map<String, Any>))
                    is List<*> -> append(buildJsonFromList(item))
                    else -> append("\"${escapeJsonString(item.toString())}\"")
                }
            }
            append("]")
        }
    }
}
