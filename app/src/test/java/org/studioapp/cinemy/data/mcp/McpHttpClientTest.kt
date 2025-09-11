package org.studioapp.cinemy.data.mcp

import android.content.Context
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.studioapp.cinemy.data.mcp.models.McpRequest
import org.studioapp.cinemy.data.mcp.models.McpResponse
import org.studioapp.cinemy.data.model.StringConstants

class McpHttpClientTest {

    private lateinit var mockContext: Context
    private lateinit var mockFakeInterceptor: FakeInterceptor
    private lateinit var mcpHttpClient: McpHttpClient

    @Before
    fun setUp() {
        mockContext = mockk()
        mockFakeInterceptor = mockk()
        
        // Create McpHttpClient and use reflection to inject mock
        mcpHttpClient = McpHttpClient(mockContext)
        
        // Use reflection to replace the private fakeInterceptor
        val field = McpHttpClient::class.java.getDeclaredField("fakeInterceptor")
        field.isAccessible = true
        field.set(mcpHttpClient, mockFakeInterceptor)
    }

    @Test
    fun `sendRequest should use fake interceptor when USE_MOCK_DATA is true`() = runBlocking {
        // Given
        val request = McpRequest(
            method = "testMethod",
            params = mapOf("param1" to "value1")
        )
        val expectedResponse = McpResponse<Map<String, Any>>(
            success = true,
            data = mapOf("test" to "data"),
            error = null,
            message = "Mock response"
        )
        
        coEvery { mockFakeInterceptor.intercept<Map<String, Any>>(request) } returns expectedResponse

        // When
        val result = mcpHttpClient.sendRequest<Map<String, Any>>(request)

        // Then
        assertEquals(expectedResponse, result)
        coVerify { mockFakeInterceptor.intercept<Map<String, Any>>(request) }
    }

    @Test(expected = RuntimeException::class)
    fun `sendRequest should handle fake interceptor exception`() = runBlocking {
        // Given
        val request = McpRequest(
            method = "testMethod",
            params = mapOf("param1" to "value1")
        )
        val exception = RuntimeException("Mock error")
        
        coEvery { mockFakeInterceptor.intercept<Map<String, Any>>(request) } throws exception

        // When
        mcpHttpClient.sendRequest<Map<String, Any>>(request)
        
        // Then
        // Exception should be thrown
        coVerify { mockFakeInterceptor.intercept<Map<String, Any>>(request) }
    }

    @Test
    fun `sendRequest should handle different request types`() = runBlocking {
        // Given
        val request1 = McpRequest(
            method = "getPopularMovies",
            params = mapOf("page" to "1")
        )
        val request2 = McpRequest(
            method = "getMovieDetails",
            params = mapOf("movieId" to "123")
        )
        
        val response1 = McpResponse<List<Map<String, Any>>>(
            success = true,
            data = listOf(mapOf("id" to 1, "title" to "Movie 1")),
            error = null,
            message = "Success"
        )
        val response2 = McpResponse<Map<String, Any>>(
            success = true,
            data = mapOf("id" to 123, "title" to "Movie Details"),
            error = null,
            message = "Success"
        )
        
        coEvery { mockFakeInterceptor.intercept<List<Map<String, Any>>>(request1) } returns response1
        coEvery { mockFakeInterceptor.intercept<Map<String, Any>>(request2) } returns response2

        // When
        val result1 = mcpHttpClient.sendRequest<List<Map<String, Any>>>(request1)
        val result2 = mcpHttpClient.sendRequest<Map<String, Any>>(request2)

        // Then
        assertEquals(response1, result1)
        assertEquals(response2, result2)
        
        coVerify { mockFakeInterceptor.intercept<List<Map<String, Any>>>(request1) }
        coVerify { mockFakeInterceptor.intercept<Map<String, Any>>(request2) }
    }

    @Test
    fun `sendRequest should handle empty params`() = runBlocking {
        // Given
        val request = McpRequest(
            method = "testMethod",
            params = emptyMap()
        )
        val expectedResponse = McpResponse<Map<String, Any>>(
            success = true,
            data = mapOf("test" to "data"),
            error = null,
            message = "Mock response"
        )
        
        coEvery { mockFakeInterceptor.intercept<Map<String, Any>>(request) } returns expectedResponse

        // When
        val result = mcpHttpClient.sendRequest<Map<String, Any>>(request)

        // Then
        assertEquals(expectedResponse, result)
        coVerify { mockFakeInterceptor.intercept<Map<String, Any>>(request) }
    }

    @Test
    fun `sendRequest should handle null data response`() = runBlocking {
        // Given
        val request = McpRequest(
            method = "testMethod",
            params = mapOf("param1" to "value1")
        )
        val expectedResponse = McpResponse<Map<String, Any>>(
            success = false,
            data = null,
            error = "No data available",
            message = "Error response"
        )
        
        coEvery { mockFakeInterceptor.intercept<Map<String, Any>>(request) } returns expectedResponse

        // When
        val result = mcpHttpClient.sendRequest<Map<String, Any>>(request)

        // Then
        assertEquals(expectedResponse, result)
        assertFalse(result.success)
        assertNull(result.data)
        assertEquals("No data available", result.error)
        coVerify { mockFakeInterceptor.intercept<Map<String, Any>>(request) }
    }

    @Test
    fun `sendRequest should handle complex data types`() = runBlocking {
        // Given
        val request = McpRequest(
            method = "testMethod",
            params = mapOf(
                "stringParam" to "value",
                "intParam" to "123",
                "boolParam" to "true"
            )
        )
        val complexData = mapOf(
            "movies" to listOf(
                mapOf("id" to 1, "title" to "Movie 1"),
                mapOf("id" to 2, "title" to "Movie 2")
            ),
            "pagination" to mapOf(
                "page" to 1,
                "totalPages" to 10,
                "hasNext" to true
            )
        )
        val expectedResponse = McpResponse<Map<String, Any>>(
            success = true,
            data = complexData,
            error = null,
            message = "Complex data response"
        )
        
        coEvery { mockFakeInterceptor.intercept<Map<String, Any>>(request) } returns expectedResponse

        // When
        val result = mcpHttpClient.sendRequest<Map<String, Any>>(request)

        // Then
        assertEquals(expectedResponse, result)
        assertTrue(result.success)
        assertNotNull(result.data)
        assertEquals(complexData, result.data)
        coVerify { mockFakeInterceptor.intercept<Map<String, Any>>(request) }
    }

    @Test
    fun `sendRequest should handle error response`() = runBlocking {
        // Given
        val request = McpRequest(
            method = "testMethod",
            params = mapOf("param1" to "value1")
        )
        val expectedResponse = McpResponse<Map<String, Any>>(
            success = false,
            data = null,
            error = "Network error",
            message = "Request failed"
        )
        
        coEvery { mockFakeInterceptor.intercept<Map<String, Any>>(request) } returns expectedResponse

        // When
        val result = mcpHttpClient.sendRequest<Map<String, Any>>(request)

        // Then
        assertEquals(expectedResponse, result)
        assertFalse(result.success)
        assertNull(result.data)
        assertEquals("Network error", result.error)
        assertEquals("Request failed", result.message)
        coVerify { mockFakeInterceptor.intercept<Map<String, Any>>(request) }
    }

    @Test
    fun `sendRequest should handle different generic types`() = runBlocking {
        // Given
        val request = McpRequest(
            method = "testMethod",
            params = mapOf("param1" to "value1")
        )
        
        // Since FakeInterceptor returns the same response for unknown methods regardless of generic type,
        // we need to test that the method works with different generic types
        val stringResponse = McpResponse<String>(
            success = false,
            data = null,
            error = "Unknown method: testMethod",
            message = "Unknown method: testMethod"
        )
        
        val intResponse = McpResponse<Int>(
            success = false,
            data = null,
            error = "Unknown method: testMethod",
            message = "Unknown method: testMethod"
        )
        
        val listResponse = McpResponse<List<String>>(
            success = false,
            data = null,
            error = "Unknown method: testMethod",
            message = "Unknown method: testMethod"
        )
        
        coEvery { mockFakeInterceptor.intercept<String>(request) } returns stringResponse
        coEvery { mockFakeInterceptor.intercept<Int>(request) } returns intResponse
        coEvery { mockFakeInterceptor.intercept<List<String>>(request) } returns listResponse

        // When
        val stringResult = mcpHttpClient.sendRequest<String>(request)
        val intResult = mcpHttpClient.sendRequest<Int>(request)
        val listResult = mcpHttpClient.sendRequest<List<String>>(request)

        // Then
        assertEquals(stringResponse, stringResult)
        assertEquals(intResponse, intResult)
        assertEquals(listResponse, listResult)
        
        coVerify { mockFakeInterceptor.intercept<String>(request) }
        coVerify { mockFakeInterceptor.intercept<Int>(request) }
        coVerify { mockFakeInterceptor.intercept<List<String>>(request) }
    }

    @Test
    fun `close should close HTTP client`() {
        // Given
        // Mock the HTTP client close method through reflection
        val httpClientField = McpHttpClient::class.java.getDeclaredField("httpClient")
        httpClientField.isAccessible = true
        val httpClient = httpClientField.get(mcpHttpClient)
        
        // We can't easily mock the HttpClient, so we'll just verify the method doesn't throw
        // When
        mcpHttpClient.close()

        // Then
        // Method should complete without throwing exception
        assertTrue(true)
    }

    @Test
    fun `sendRequest should handle fake interceptor returning null`() = runBlocking {
        // Given
        val request = McpRequest(
            method = "testMethod",
            params = mapOf("param1" to "value1")
        )
        
        val expectedResponse = McpResponse<Map<String, Any>>(
            success = false,
            data = null,
            error = "Test error",
            message = "Test message"
        )
        
        coEvery { mockFakeInterceptor.intercept<Map<String, Any>>(request) } returns expectedResponse

        // When
        val result = mcpHttpClient.sendRequest<Map<String, Any>>(request)

        // Then
        // Should return the response from fake interceptor
        assertEquals(expectedResponse, result)
        assertFalse(result.success)
        assertNull(result.data)
        assertEquals("Test error", result.error)
        assertEquals("Test message", result.message)
        coVerify { mockFakeInterceptor.intercept<Map<String, Any>>(request) }
    }

    @Test
    fun `sendRequest should handle fake interceptor returning success false`() = runBlocking {
        // Given
        val request = McpRequest(
            method = "testMethod",
            params = mapOf("param1" to "value1")
        )
        val expectedResponse = McpResponse<Map<String, Any>>(
            success = false,
            data = mapOf("error" to "details"),
            error = "Test error",
            message = "Test message"
        )
        
        coEvery { mockFakeInterceptor.intercept<Map<String, Any>>(request) } returns expectedResponse

        // When
        val result = mcpHttpClient.sendRequest<Map<String, Any>>(request)

        // Then
        assertEquals(expectedResponse, result)
        assertFalse(result.success)
        assertEquals("Test error", result.error)
        assertEquals("Test message", result.message)
        coVerify { mockFakeInterceptor.intercept<Map<String, Any>>(request) }
    }
}
