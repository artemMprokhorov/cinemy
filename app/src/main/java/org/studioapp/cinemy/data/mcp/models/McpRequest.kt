package org.studioapp.cinemy.data.mcp.models

/**
 * Represents an MCP (Model Context Protocol) request structure for backend communication.
 * 
 * This data class encapsulates the essential components needed to make a request to the MCP server,
 * including the method name and associated parameters. It serves as the primary data structure
 * for all MCP communication within the Cinemy application.
 * 
 * The MCP request follows a standardized format where:
 * - The method name identifies the specific operation to be performed
 * - Parameters are provided as key-value pairs for method-specific data
 * 
 * @param method The method name to be called on the MCP server. Must be a non-empty string
 *               that corresponds to a valid MCP server method (e.g., "getPopularMovies", "getMovieDetails")
 * @param params The parameters to be passed to the method as a map of string key-value pairs.
 *               Defaults to an empty map if no parameters are required. All values must be
 *               convertible to strings for JSON serialization
 * 
 * @see McpResponse For the corresponding response structure
 * @see HttpRequestMapper For JSON serialization of MCP requests
 * @see McpClient For MCP request execution
 * 
 * @since 1.0.0
 * @author Cinemy Development Team
 */
data class McpRequest(
    val method: String,
    val params: Map<String, String> = emptyMap()
)
