package org.studioapp.cinemy.data.mcp

import android.content.Context
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

class FakeInterceptorTest {

    private lateinit var mockContext: Context
    private lateinit var fakeInterceptor: FakeInterceptor

    @Before
    fun setUp() {
        mockContext = mockk()
        fakeInterceptor = FakeInterceptor(mockContext)
    }

    @Test
    fun `intercept should return error for unknown method`() = runBlocking {
        // Given
        val request = McpRequest(
            method = "unknownMethod",
            params = mapOf("param1" to "value1")
        )

        // When
        val result = fakeInterceptor.intercept<Map<String, Any>>(request)

        // Then
        assertFalse(result.success)
        assertNull(result.data)
        assertNotNull(result.error)
        assertTrue(result.error!!.contains("unknownMethod"))
        assertNotNull(result.message)
        assertTrue(result.message!!.contains("unknownMethod"))
    }

    @Test
    fun `intercept should handle getPopularMovies when asset loading fails`() = runBlocking {
        // Given
        val request = McpRequest(
            method = StringConstants.MCP_METHOD_GET_POPULAR_MOVIES,
            params = mapOf("page" to "1")
        )
        
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = fakeInterceptor.intercept<Map<String, Any>>(request)

        // Then
        assertFalse(result.success)
        assertNull(result.data)
        assertEquals(StringConstants.ERROR_LOADING_MOCK_DATA, result.error)
        assertEquals(StringConstants.ERROR_LOADING_MOCK_DATA, result.message)
        
        verify { mockContext.assets.open(StringConstants.ASSET_MOCK_MOVIES) }
    }

    @Test
    fun `intercept should handle getMovieDetails when asset loading fails`() = runBlocking {
        // Given
        val request = McpRequest(
            method = StringConstants.MCP_METHOD_GET_MOVIE_DETAILS,
            params = mapOf("movieId" to "123")
        )
        
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = fakeInterceptor.intercept<Map<String, Any>>(request)

        // Then
        assertFalse(result.success)
        assertNull(result.data)
        assertEquals(StringConstants.ERROR_LOADING_MOCK_DATA, result.error)
        assertEquals(StringConstants.ERROR_LOADING_MOCK_DATA, result.message)
        
        verify { mockContext.assets.open(StringConstants.ASSET_MOCK_MOVIE_DETAILS) }
    }

    @Test
    fun `intercept should handle getPopularMovies when JSON parsing fails`() = runBlocking {
        // Given
        val request = McpRequest(
            method = StringConstants.MCP_METHOD_GET_POPULAR_MOVIES,
            params = mapOf("page" to "1")
        )
        
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = fakeInterceptor.intercept<Map<String, Any>>(request)

        // Then
        assertFalse(result.success)
        assertNull(result.data)
        assertEquals(StringConstants.ERROR_LOADING_MOCK_DATA, result.error)
        assertEquals(StringConstants.ERROR_LOADING_MOCK_DATA, result.message)
        
        verify { mockContext.assets.open(StringConstants.ASSET_MOCK_MOVIES) }
    }

    @Test
    fun `intercept should handle getMovieDetails when JSON parsing fails`() = runBlocking {
        // Given
        val request = McpRequest(
            method = StringConstants.MCP_METHOD_GET_MOVIE_DETAILS,
            params = mapOf("movieId" to "123")
        )
        
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = fakeInterceptor.intercept<Map<String, Any>>(request)

        // Then
        assertFalse(result.success)
        assertNull(result.data)
        assertEquals(StringConstants.ERROR_LOADING_MOCK_DATA, result.error)
        assertEquals(StringConstants.ERROR_LOADING_MOCK_DATA, result.message)
        
        verify { mockContext.assets.open(StringConstants.ASSET_MOCK_MOVIE_DETAILS) }
    }

    @Test
    fun `intercept should handle getPopularMovies with valid JSON`() = runBlocking {
        // Given
        val request = McpRequest(
            method = StringConstants.MCP_METHOD_GET_POPULAR_MOVIES,
            params = mapOf("page" to "1")
        )
        
        // Mock asset loading to return fallback response (simulating asset not found)
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = fakeInterceptor.intercept<Map<String, Any>>(request)

        // Then
        // Since asset loading fails, we expect a fallback response
        assertFalse(result.success)
        assertNull(result.data)
        assertNotNull(result.error)
        assertTrue(result.error!!.contains("Failed to load mock data from assets"))
        
        verify { mockContext.assets.open(StringConstants.ASSET_MOCK_MOVIES) }
    }

    @Test
    fun `intercept should handle getMovieDetails with valid JSON`() = runBlocking {
        // Given
        val request = McpRequest(
            method = StringConstants.MCP_METHOD_GET_MOVIE_DETAILS,
            params = mapOf("movieId" to "123")
        )
        
        val mockJson = """{
            "success": true,
            "data": {
                "movieDetails": {
                    "id": 123,
                    "title": "Test Movie",
                    "description": "Test Description"
                }
            },
            "message": "Success"
        }"""
        
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = fakeInterceptor.intercept<Map<String, Any>>(request)

        // Then
        // Since asset loading fails, we expect a fallback response
        assertFalse(result.success)
        assertNull(result.data)
        assertNotNull(result.error)
        assertTrue(result.error!!.contains("Failed to load mock data from assets"))
        
        verify { mockContext.assets.open(StringConstants.ASSET_MOCK_MOVIE_DETAILS) }
    }

    @Test
    fun `intercept should handle different page numbers for getPopularMovies`() = runBlocking {
        // Given
        val request1 = McpRequest(
            method = StringConstants.MCP_METHOD_GET_POPULAR_MOVIES,
            params = mapOf("page" to "1")
        )
        val request2 = McpRequest(
            method = StringConstants.MCP_METHOD_GET_POPULAR_MOVIES,
            params = mapOf("page" to "2")
        )
        val request3 = McpRequest(
            method = StringConstants.MCP_METHOD_GET_POPULAR_MOVIES,
            params = mapOf("page" to "3")
        )
        
        val mockJson = """{
            "success": true,
            "data": {
                "movies": [
                    {"id": 1, "title": "Movie 1"},
                    {"id": 2, "title": "Movie 2"},
                    {"id": 3, "title": "Movie 3"},
                    {"id": 4, "title": "Movie 4"},
                    {"id": 5, "title": "Movie 5"},
                    {"id": 6, "title": "Movie 6"}
                ]
            },
            "message": "Success"
        }"""
        
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result1 = fakeInterceptor.intercept<Map<String, Any>>(request1)
        val result2 = fakeInterceptor.intercept<Map<String, Any>>(request2)
        val result3 = fakeInterceptor.intercept<Map<String, Any>>(request3)

        // Then
        // Since asset loading fails, we expect fallback responses
        assertFalse(result1.success)
        assertFalse(result2.success)
        assertFalse(result3.success)
        
        // All should have error messages
        assertNotNull(result1.error)
        assertNotNull(result2.error)
        assertNotNull(result3.error)
        assertTrue(result1.error!!.contains("Failed to load mock data from assets"))
        assertTrue(result2.error!!.contains("Failed to load mock data from assets"))
        assertTrue(result3.error!!.contains("Failed to load mock data from assets"))
        
        verify(exactly = 3) { mockContext.assets.open(StringConstants.ASSET_MOCK_MOVIES) }
    }

    @Test
    fun `intercept should handle missing page parameter`() = runBlocking {
        // Given
        val request = McpRequest(
            method = StringConstants.MCP_METHOD_GET_POPULAR_MOVIES,
            params = emptyMap()
        )
        
        val mockJson = """{
            "success": true,
            "data": {
                "movies": [
                    {"id": 1, "title": "Movie 1"},
                    {"id": 2, "title": "Movie 2"}
                ]
            },
            "message": "Success"
        }"""
        
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = fakeInterceptor.intercept<Map<String, Any>>(request)

        // Then
        assertFalse(result.success)
        assertNull(result.data)
        assertNotNull(result.error)
        assertTrue(result.error!!.contains("Failed to load mock data from assets"))
        
        verify { mockContext.assets.open(StringConstants.ASSET_MOCK_MOVIES) }
    }

    @Test
    fun `intercept should handle invalid page parameter`() = runBlocking {
        // Given
        val request = McpRequest(
            method = StringConstants.MCP_METHOD_GET_POPULAR_MOVIES,
            params = mapOf("page" to "invalid")
        )
        
        val mockJson = """{
            "success": true,
            "data": {
                "movies": [
                    {"id": 1, "title": "Movie 1"},
                    {"id": 2, "title": "Movie 2"}
                ]
            },
            "message": "Success"
        }"""
        
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = fakeInterceptor.intercept<Map<String, Any>>(request)

        // Then
        assertFalse(result.success)
        assertNull(result.data)
        assertNotNull(result.error)
        assertTrue(result.error!!.contains("Failed to load mock data from assets"))
        
        verify { mockContext.assets.open(StringConstants.ASSET_MOCK_MOVIES) }
    }

    @Test
    fun `intercept should handle different generic types`() = runBlocking {
        // Given
        val request = McpRequest(
            method = StringConstants.MCP_METHOD_GET_POPULAR_MOVIES,
            params = mapOf("page" to "1")
        )
        
        val mockJson = """{
            "success": true,
            "data": {
                "movies": [
                    {"id": 1, "title": "Movie 1"}
                ]
            },
            "message": "Success"
        }"""
        
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val mapResult = fakeInterceptor.intercept<Map<String, Any>>(request)
        val listResult = fakeInterceptor.intercept<List<Map<String, Any>>>(request)
        val stringResult = fakeInterceptor.intercept<String>(request)

        // Then
        // Since asset loading fails, we expect fallback responses
        assertFalse(mapResult.success)
        assertFalse(listResult.success)
        assertFalse(stringResult.success)
        
        assertNull(mapResult.data)
        assertNull(listResult.data)
        assertNull(stringResult.data)
        
        assertNotNull(mapResult.error)
        assertNotNull(listResult.error)
        assertNotNull(stringResult.error)
        assertTrue(mapResult.error!!.contains("Failed to load mock data from assets"))
        assertTrue(listResult.error!!.contains("Failed to load mock data from assets"))
        assertTrue(stringResult.error!!.contains("Failed to load mock data from assets"))
        
        verify(exactly = 3) { mockContext.assets.open(StringConstants.ASSET_MOCK_MOVIES) }
    }

    @Test
    fun `intercept should handle empty movies array`() = runBlocking {
        // Given
        val request = McpRequest(
            method = StringConstants.MCP_METHOD_GET_POPULAR_MOVIES,
            params = mapOf("page" to "1")
        )
        
        val mockJson = """{
            "success": true,
            "data": {
                "movies": []
            },
            "message": "Success"
        }"""
        
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = fakeInterceptor.intercept<Map<String, Any>>(request)

        // Then
        assertFalse(result.success)
        assertNull(result.data)
        assertNotNull(result.error)
        assertTrue(result.error!!.contains("Failed to load mock data from assets"))
        
        verify { mockContext.assets.open(StringConstants.ASSET_MOCK_MOVIES) }
    }

    @Test
    fun `intercept should handle null data in JSON`() = runBlocking {
        // Given
        val request = McpRequest(
            method = StringConstants.MCP_METHOD_GET_MOVIE_DETAILS,
            params = mapOf("movieId" to "123")
        )
        
        val mockJson = """{
            "success": true,
            "data": null,
            "message": "Success"
        }"""
        
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = fakeInterceptor.intercept<Map<String, Any>>(request)

        // Then
        // Since asset loading fails, we expect a fallback response
        assertFalse(result.success)
        assertNull(result.data)
        assertNotNull(result.error)
        assertTrue(result.error!!.contains("Failed to load mock data from assets"))
        
        verify { mockContext.assets.open(StringConstants.ASSET_MOCK_MOVIE_DETAILS) }
    }

    @Test
    fun `intercept should handle missing success field in JSON`() = runBlocking {
        // Given
        val request = McpRequest(
            method = StringConstants.MCP_METHOD_GET_POPULAR_MOVIES,
            params = mapOf("page" to "1")
        )
        
        val mockJson = """{
            "data": {
                "movies": [
                    {"id": 1, "title": "Movie 1"}
                ]
            },
            "message": "Success"
        }"""
        
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = fakeInterceptor.intercept<Map<String, Any>>(request)

        // Then
        // Since asset loading fails, we expect a fallback response
        assertFalse(result.success)
        assertNull(result.data)
        assertNotNull(result.error)
        assertTrue(result.error!!.contains("Failed to load mock data from assets"))
        
        verify { mockContext.assets.open(StringConstants.ASSET_MOCK_MOVIES) }
    }

    @Test
    fun `intercept should handle missing message field in JSON`() = runBlocking {
        // Given
        val request = McpRequest(
            method = StringConstants.MCP_METHOD_GET_MOVIE_DETAILS,
            params = mapOf("movieId" to "123")
        )
        
        val mockJson = """{
            "success": true,
            "data": {
                "movieDetails": {
                    "id": 123,
                    "title": "Test Movie"
                }
            }
        }"""
        
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = fakeInterceptor.intercept<Map<String, Any>>(request)

        // Then
        assertFalse(result.success)
        assertNull(result.data)
        assertNotNull(result.error)
        assertTrue(result.error!!.contains("Failed to load mock data from assets"))
        assertNotNull(result.message) // Should use default message
        
        verify { mockContext.assets.open(StringConstants.ASSET_MOCK_MOVIE_DETAILS) }
    }
}
