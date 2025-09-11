package org.studioapp.cinemy.data.mcp.models

/**
 * Represents an MCP (Model Context Protocol) response
 *
 * @param success Whether the request was successful
 * @param data The response data of type T
 * @param error Error message if the request failed
 * @param message Additional information about the response
 */
data class McpResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: String? = null,
    val message: String = ""
)
