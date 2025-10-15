package org.studioapp.cinemy.data.mcp.models

/**
 * Generic wrapper for MCP (Model Context Protocol) responses.
 *
 * This immutable data structure is returned by components parsing MCP/HTTP responses
 * (see `HttpResponseMapper`) and by data sources integrating with the MCP backend.
 * It conveys the operation outcome alongside optional payload and diagnostic context.
 *
 * Type parameter [T] denotes the expected payload type when a request succeeds.
 *
 * Contract:
 * - When [success] is `true`, [data] SHOULD be non-null for payload-bearing operations and
 *   [error] SHOULD be `null`.
 * - When [success] is `false`, [data] SHOULD be `null` and [error] SHOULD contain a
 *   human-readable reason.
 * - [message] can carry auxiliary information (e.g., normalized success markers or
 *   notes such as raw-response fallbacks), and is safe to display for debugging.
 *
 * Note: No exceptions are thrown by this class. Error semantics are expressed via fields.
 *
 * @param T The type of the successful payload contained in [data].
 * @property success Indicates whether the MCP operation completed successfully.
 * @property data Optional payload of type [T] when the operation succeeds; `null` otherwise.
 * @property error Optional error description when the operation fails; `null` on success.
 * @property message Optional, additional diagnostic or informational message accompanying the response.
 */
data class McpResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: String? = null,
    val message: String = ""
)
