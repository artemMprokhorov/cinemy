package com.example.tmdbai.data.mcp.models

import com.google.gson.annotations.SerializedName

/**
 * Represents an MCP (Model Context Protocol) response
 * 
 * @param success Whether the request was successful
 * @param data The response data of type T
 * @param error Error message if the request failed
 * @param message Additional information about the response
 */
data class McpResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: T? = null,
    @SerializedName("error")
    val error: String? = null,
    @SerializedName("message")
    val message: String = ""
)
