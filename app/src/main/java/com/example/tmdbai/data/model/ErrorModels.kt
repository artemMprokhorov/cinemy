package com.example.tmdbai.data.model

/**
 * Error model classes for consistent error handling across the application
 */

/**
 * Base sealed class for all application errors
 */
sealed class AppError(
    val message: String,
    val code: String? = null,
    val cause: Throwable? = null
) {
    /**
     * Network-related errors
     */
    sealed class NetworkError(message: String, code: String? = null, cause: Throwable? = null) :
        AppError(message, code, cause) {
        object ConnectionError : NetworkError("Network connection failed")
        object TimeoutError : NetworkError("Request timeout")
        object ServerError : NetworkError("Server error occurred")
        object UnknownNetworkError : NetworkError("Unknown network error")

        data class HttpError(
            val statusCode: Int,
            val errorMessage: String,
            val errorCode: String? = null,
            val errorCause: Throwable? = null
        ) : NetworkError(errorMessage, errorCode, errorCause)
    }

    /**
     * Data-related errors
     */
    sealed class DataError(message: String, code: String? = null, cause: Throwable? = null) :
        AppError(message, code, cause) {
        object ParseError : DataError("Failed to parse data")
        object ValidationError : DataError("Data validation failed")
        object NotFoundError : DataError("Data not found")
        object UnknownDataError : DataError("Unknown data error")

        data class MappingError(
            val errorMessage: String,
            val errorCode: String? = null,
            val errorCause: Throwable? = null
        ) : DataError(errorMessage, errorCode, errorCause)
    }

    /**
     * MCP (Model Context Protocol) related errors
     */
    sealed class McpError(message: String, code: String? = null, cause: Throwable? = null) :
        AppError(message, code, cause) {
        object ConnectionError : McpError("MCP connection failed")
        object ResponseError : McpError("Invalid MCP response")
        object UnknownMcpError : McpError("Unknown MCP error")

        data class RequestError(
            val errorMessage: String,
            val errorCode: String? = null,
            val errorCause: Throwable? = null
        ) : McpError(errorMessage, errorCode, errorCause)
    }

    /**
     * Generic application errors
     */
    sealed class GenericError(message: String, code: String? = null, cause: Throwable? = null) :
        AppError(message, code, cause) {
        object UnknownError : GenericError("Unknown error occurred")
        object UnexpectedError : GenericError("Unexpected error occurred")

        data class CustomError(
            val errorMessage: String,
            val errorCode: String? = null,
            val errorCause: Throwable? = null
        ) : GenericError(errorMessage, errorCode, errorCause)
    }
}

/**
 * Extension function to convert AppError to user-friendly message
 */
fun AppError.toUserMessage(): String {
    return when (this) {
        is AppError.NetworkError.ConnectionError -> "Please check your internet connection"
        is AppError.NetworkError.TimeoutError -> "Request timed out. Please try again"
        is AppError.NetworkError.ServerError -> "Server is temporarily unavailable"
        is AppError.NetworkError.UnknownNetworkError -> "Network error occurred"
        is AppError.NetworkError.HttpError -> when (statusCode) {
            404 -> "Resource not found"
            500 -> "Server error occurred"
            else -> "Network error: $statusCode"
        }

        is AppError.DataError.ParseError -> "Failed to process data"
        is AppError.DataError.ValidationError -> "Invalid data received"
        is AppError.DataError.NotFoundError -> "No data found"
        is AppError.DataError.UnknownDataError -> "Data processing error"
        is AppError.DataError.MappingError -> errorMessage

        is AppError.McpError.ConnectionError -> "Service connection failed"
        is AppError.McpError.ResponseError -> "Invalid service response"
        is AppError.McpError.UnknownMcpError -> "Service error occurred"
        is AppError.McpError.RequestError -> errorMessage

        is AppError.GenericError.UnknownError -> "An unexpected error occurred"
        is AppError.GenericError.UnexpectedError -> "Something went wrong"
        is AppError.GenericError.CustomError -> errorMessage
    }
}

/**
 * Extension function to get error code for logging
 */
fun AppError.getErrorCode(): String {
    return code ?: when (this) {
        is AppError.NetworkError.ConnectionError -> "NETWORK_CONNECTION_ERROR"
        is AppError.NetworkError.TimeoutError -> "NETWORK_TIMEOUT_ERROR"
        is AppError.NetworkError.ServerError -> "NETWORK_SERVER_ERROR"
        is AppError.NetworkError.UnknownNetworkError -> "NETWORK_UNKNOWN_ERROR"
        is AppError.NetworkError.HttpError -> "NETWORK_HTTP_ERROR_${statusCode}"

        is AppError.DataError.ParseError -> "DATA_PARSE_ERROR"
        is AppError.DataError.ValidationError -> "DATA_VALIDATION_ERROR"
        is AppError.DataError.NotFoundError -> "DATA_NOT_FOUND_ERROR"
        is AppError.DataError.UnknownDataError -> "DATA_UNKNOWN_ERROR"
        is AppError.DataError.MappingError -> "DATA_MAPPING_ERROR"

        is AppError.McpError.ConnectionError -> "MCP_CONNECTION_ERROR"
        is AppError.McpError.ResponseError -> "MCP_RESPONSE_ERROR"
        is AppError.McpError.UnknownMcpError -> "MCP_UNKNOWN_ERROR"
        is AppError.McpError.RequestError -> "MCP_REQUEST_ERROR"

        is AppError.GenericError.UnknownError -> "GENERIC_UNKNOWN_ERROR"
        is AppError.GenericError.UnexpectedError -> "GENERIC_UNEXPECTED_ERROR"
        is AppError.GenericError.CustomError -> "GENERIC_CUSTOM_ERROR"
    }
}
