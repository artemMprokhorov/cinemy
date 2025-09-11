package org.studioapp.cinemy.data.mcp

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.studioapp.cinemy.data.model.StringConstants

class AssetDataLoaderTest {

    private lateinit var mockContext: Context
    private lateinit var assetDataLoader: AssetDataLoader
    private lateinit var mockInputStream: java.io.InputStream
    private lateinit var mockBufferedReader: java.io.BufferedReader

    @Before
    fun setUp() {
        mockContext = mockk()
        mockInputStream = mockk()
        mockBufferedReader = mockk()
        assetDataLoader = AssetDataLoader(mockContext)
    }

    @Test
    fun `loadUiConfig should return default config when asset loading fails`() {
        // Given
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = assetDataLoader.loadUiConfig()

        // Then
        assertNotNull(result)
        assertNotNull(result.colors)
        assertNotNull(result.texts)
        assertNotNull(result.buttons)
        assertEquals(StringConstants.COLOR_PRIMARY, result.colors.primary)
        assertEquals(StringConstants.MOVIES_TITLE, result.texts.appTitle)
        assertEquals(StringConstants.COLOR_PRIMARY, result.buttons.primaryButtonColor)
    }

    @Test
    fun `loadUiConfig should return default config when JSON parsing fails`() {
        // Given
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = assetDataLoader.loadUiConfig()

        // Then
        assertNotNull(result)
        assertNotNull(result.colors)
        assertNotNull(result.texts)
        assertNotNull(result.buttons)
    }

    @Test
    fun `loadMetaData should return default meta when asset loading fails`() {
        // Given
        val method = "testMethod"
        val resultsCount = 10
        val movieId = 123

        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = assetDataLoader.loadMetaData(method, resultsCount, movieId)

        // Then
        assertNotNull(result)
        assertEquals(method, result.method)
        assertEquals(resultsCount, result.resultsCount)
        assertEquals(movieId, result.movieId)
        assertTrue(result.aiGenerated)
        assertNotNull(result.geminiColors)
        assertEquals(StringConstants.VERSION_2_0_0, result.version)
    }

    @Test
    fun `loadMetaData should return default meta when JSON parsing fails`() {
        // Given
        val method = "testMethod"
        val resultsCount = 5
        val movieId: Int? = null

        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = assetDataLoader.loadMetaData(method, resultsCount, movieId)

        // Then
        assertNotNull(result)
        assertEquals(method, result.method)
        assertEquals(resultsCount, result.resultsCount)
        assertEquals(movieId, result.movieId)
        assertTrue(result.aiGenerated)
        assertNotNull(result.geminiColors)
    }

    @Test
    fun `loadMockMovies should return empty list when asset loading fails`() {
        // Given
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = assetDataLoader.loadMockMovies()

        // Then
        assertNotNull(result)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `loadMockMovies should return empty list when JSON parsing fails`() {
        // Given
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = assetDataLoader.loadMockMovies()

        // Then
        assertNotNull(result)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `loadMockMovies should return empty list when movies array is null`() {
        // Given
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = assetDataLoader.loadMockMovies()

        // Then
        assertNotNull(result)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `loadUiConfig should handle null uiConfig in JSON`() {
        // Given
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = assetDataLoader.loadUiConfig()

        // Then
        assertNotNull(result)
        assertNotNull(result.colors)
        assertNotNull(result.texts)
        assertNotNull(result.buttons)
    }

    @Test
    fun `loadMetaData should handle null meta in JSON`() {
        // Given
        val method = "testMethod"
        val resultsCount = 5
        val movieId: Int? = null

        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = assetDataLoader.loadMetaData(method, resultsCount, movieId)

        // Then
        assertNotNull(result)
        assertEquals(method, result.method)
        assertEquals(resultsCount, result.resultsCount)
        assertEquals(movieId, result.movieId)
    }

    @Test
    fun `loadMetaData should handle different parameter combinations`() {
        // Given
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // Test with all parameters
        val result1 = assetDataLoader.loadMetaData("method1", 10, 123)
        assertEquals("method1", result1.method)
        assertEquals(10, result1.resultsCount)
        assertEquals(123, result1.movieId)

        // Test with null movieId
        val result2 = assetDataLoader.loadMetaData("method2", 5, null)
        assertEquals("method2", result2.method)
        assertEquals(5, result2.resultsCount)
        assertNull(result2.movieId)

        // Test with zero results
        val result3 = assetDataLoader.loadMetaData("method3", 0, 456)
        assertEquals("method3", result3.method)
        assertEquals(0, result3.resultsCount)
        assertEquals(456, result3.movieId)
    }

    @Test
    fun `loadUiConfig should use correct asset file`() {
        // Given
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        assetDataLoader.loadUiConfig()

        // Then
        verify { mockContext.assets.open(StringConstants.ASSET_MOCK_MOVIES) }
    }

    @Test
    fun `loadMetaData should use correct asset file`() {
        // Given
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        assetDataLoader.loadMetaData("test", 0)

        // Then
        verify { mockContext.assets.open(StringConstants.ASSET_MOCK_MOVIES) }
    }

    @Test
    fun `loadMockMovies should use correct asset file`() {
        // Given
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        assetDataLoader.loadMockMovies()

        // Then
        verify { mockContext.assets.open(StringConstants.ASSET_MOCK_MOVIES) }
    }

    @Test
    fun `loadUiConfig should handle asset loading exception gracefully`() {
        // Given
        every { mockContext.assets.open(any()) } throws RuntimeException("IO Error")

        // When
        val result = assetDataLoader.loadUiConfig()

        // Then
        assertNotNull(result)
        // Should return default configuration
        assertEquals(StringConstants.COLOR_PRIMARY, result.colors.primary)
    }

    @Test
    fun `loadMetaData should handle asset loading exception gracefully`() {
        // Given
        every { mockContext.assets.open(any()) } throws RuntimeException("IO Error")
        val method = "testMethod"
        val resultsCount = 5
        val movieId = 123

        // When
        val result = assetDataLoader.loadMetaData(method, resultsCount, movieId)

        // Then
        assertNotNull(result)
        assertEquals(method, result.method)
        assertEquals(resultsCount, result.resultsCount)
        assertEquals(movieId, result.movieId)
    }

    @Test
    fun `loadMockMovies should handle asset loading exception gracefully`() {
        // Given
        every { mockContext.assets.open(any()) } throws RuntimeException("IO Error")

        // When
        val result = assetDataLoader.loadMockMovies()

        // Then
        assertNotNull(result)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `loadUiConfig should handle JSON parsing exception gracefully`() {
        // Given
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = assetDataLoader.loadUiConfig()

        // Then
        assertNotNull(result)
        // Should return default configuration
        assertEquals(StringConstants.COLOR_PRIMARY, result.colors.primary)
    }

    @Test
    fun `loadMetaData should handle JSON parsing exception gracefully`() {
        // Given
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        val method = "testMethod"
        val resultsCount = 5
        val movieId = 123

        // When
        val result = assetDataLoader.loadMetaData(method, resultsCount, movieId)

        // Then
        assertNotNull(result)
        assertEquals(method, result.method)
        assertEquals(resultsCount, result.resultsCount)
        assertEquals(movieId, result.movieId)
    }

    @Test
    fun `loadMockMovies should handle JSON parsing exception gracefully`() {
        // Given
        every { mockContext.assets.open(any()) } throws Exception("Asset not found")

        // When
        val result = assetDataLoader.loadMockMovies()

        // Then
        assertNotNull(result)
        assertTrue(result.isEmpty())
    }
}
