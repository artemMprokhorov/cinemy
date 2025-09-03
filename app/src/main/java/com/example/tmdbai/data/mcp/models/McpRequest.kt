package com.example.tmdbai.data.mcp.models

import com.google.gson.annotations.SerializedName

/**
 * Represents an MCP (Model Context Protocol) request
 * 
 * @param method The method name to be called on the MCP server
 * @param params The parameters to be passed to the method
 */
data class McpRequest(
    @SerializedName("method")
    val method: String,
    @SerializedName("params")
    val params: Map<String, String> = emptyMap()
)
