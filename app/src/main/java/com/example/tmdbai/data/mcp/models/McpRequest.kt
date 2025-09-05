package com.example.tmdbai.data.mcp.models

/**
 * Represents an MCP (Model Context Protocol) request
 *
 * @param method The method name to be called on the MCP server
 * @param params The parameters to be passed to the method
 */
data class McpRequest(
    val method: String,
    val params: Map<String, String> = emptyMap()
)
