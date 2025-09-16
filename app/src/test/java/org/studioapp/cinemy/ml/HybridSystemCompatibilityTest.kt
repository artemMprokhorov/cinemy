package org.studioapp.cinemy.ml

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

/**
 * Compatibility test for hybrid sentiment analysis system
 * Ensures backward compatibility with existing SentimentAnalyzer functionality
 */
class HybridSystemCompatibilityTest {

    private lateinit var mockContext: android.content.Context
    private lateinit var sentimentAnalyzer: SentimentAnalyzer

    @Before
    fun setUp() {
        mockContext = TestUtils.createMockContext()
        sentimentAnalyzer = SentimentAnalyzer.getInstance(mockContext)
    }

    @Test
    fun `test backward compatibility with existing API`() = runBlocking {
        // Given
        sentimentAnalyzer.initialize()
        
        // When
        val result = sentimentAnalyzer.analyzeSentiment("This movie is amazing!")
        
        // Then
        assertTrue("Result should be successful", result.isSuccess)
        assertEquals("Should be positive sentiment", SentimentType.POSITIVE, result.sentiment)
        assertTrue("Should have reasonable confidence", result.confidence > 0.3)
        assertNotNull("Should have found keywords", result.foundKeywords)
    }

    @Test
    fun `test batch analysis compatibility`() = runBlocking {
        // Given
        sentimentAnalyzer.initialize()
        val texts = listOf(
            "Great movie!",
            "Terrible film.",
            "It's okay."
        )
        
        // When
        val results = sentimentAnalyzer.analyzeBatch(texts)
        
        // Then
        assertEquals("Should process all texts", texts.size, results.size)
        assertTrue("All results should be successful", results.all { it.isSuccess })
        
        // Verify different sentiment types
        val sentimentTypes = results.map { it.sentiment }.distinct()
        assertTrue("Should detect multiple sentiment types", sentimentTypes.size > 1)
    }

    @Test
    fun `test model info compatibility`() = runBlocking {
        // Given
        sentimentAnalyzer.initialize()
        
        // When
        val modelInfo = sentimentAnalyzer.getModelInfo()
        
        // Then
        assertNotNull("Model info should not be null", modelInfo)
        assertEquals("Should be keyword sentiment analysis", "keyword_sentiment_analysis", modelInfo?.type)
        assertTrue("Should have version", modelInfo?.version?.isNotEmpty() == true)
        assertTrue("Should have accuracy info", modelInfo?.accuracy?.isNotEmpty() == true)
    }

    @Test
    @Ignore("Requires real Android environment for TensorFlow Lite model loading")
    fun `test new TensorFlow model info methods`() = runBlocking {
        // Given
        sentimentAnalyzer.initialize()
        
        // When
        val tensorFlowModelInfo = sentimentAnalyzer.getTensorFlowModelInfo()
        val isTensorFlowAvailable = sentimentAnalyzer.isTensorFlowAvailable()
        
        // Then
        assertNotNull("TensorFlow model info should not be null", tensorFlowModelInfo)
        assertEquals("Should be tensorflow lite sentiment", "tensorflow_lite_sentiment", tensorFlowModelInfo?.type)
        assertTrue("TensorFlow should be available", isTensorFlowAvailable)
    }

    @Test
    fun `test error handling compatibility`() = runBlocking {
        // Given
        val failingContext = TestUtils.createFailingMockContext()
        val failingAnalyzer = SentimentAnalyzer.getInstance(failingContext)
        failingAnalyzer.initialize()
        
        // When
        val result = failingAnalyzer.analyzeSentiment("Test text")
        
        // Then
        // Should still work with keyword model fallback
        assertTrue("Should handle errors gracefully", result.isSuccess)
        assertNotNull("Should return valid sentiment", result.sentiment)
    }

    @Test
    fun `test performance characteristics`() = runBlocking {
        // Given
        sentimentAnalyzer.initialize()
        val testText = "This is a test movie review with some sentiment words."
        
        // When
        val (result, executionTime) = TestUtils.measureTime {
            sentimentAnalyzer.analyzeSentiment(testText)
        }
        
        // Then
        assertTrue("Should complete analysis successfully", result.isSuccess)
        assertTrue("Should complete within reasonable time", executionTime < 1000L)
        assertTrue("Should have reasonable confidence", result.confidence > 0.3)
    }

    @Test
    fun `test singleton pattern compatibility`() {
        // Given
        val context1 = TestUtils.createMockContext()
        val context2 = TestUtils.createMockContext()
        
        // When
        val analyzer1 = SentimentAnalyzer.getInstance(context1)
        val analyzer2 = SentimentAnalyzer.getInstance(context2)
        
        // Then
        assertEquals("Should return same instance", analyzer1, analyzer2)
    }

    @Test
    fun `test resource cleanup compatibility`() = runBlocking {
        // Given
        sentimentAnalyzer.initialize()
        
        // When
        sentimentAnalyzer.cleanup()
        
        // Then
        // Should not throw exceptions and should be safe to call multiple times
        sentimentAnalyzer.cleanup()
        sentimentAnalyzer.cleanup()
        
        // Should be able to reinitialize
        val reinitialized = sentimentAnalyzer.initialize()
        assertTrue("Should be able to reinitialize", reinitialized)
    }

    @Test
    fun `test edge cases compatibility`() = runBlocking {
        // Given
        sentimentAnalyzer.initialize()
        
        // When & Then
        val emptyResult = sentimentAnalyzer.analyzeSentiment("")
        assertTrue("Empty string should return neutral", emptyResult.sentiment == SentimentType.NEUTRAL)
        
        val whitespaceResult = sentimentAnalyzer.analyzeSentiment("   \n\t   ")
        assertTrue("Whitespace should return neutral", whitespaceResult.sentiment == SentimentType.NEUTRAL)
        
        val singleWordResult = sentimentAnalyzer.analyzeSentiment("amazing")
        assertTrue("Single word should work", singleWordResult.isSuccess)
        
        val veryLongText = "This is a very long movie review. ".repeat(100)
        val longTextResult = sentimentAnalyzer.analyzeSentiment(veryLongText)
        assertTrue("Very long text should work", longTextResult.isSuccess)
    }

    @Test
    fun `test sentiment type consistency`() = runBlocking {
        // Given
        sentimentAnalyzer.initialize()
        
        // When
        val positiveResult = sentimentAnalyzer.analyzeSentiment("This movie is amazing and fantastic!")
        val negativeResult = sentimentAnalyzer.analyzeSentiment("This movie is terrible and awful.")
        val neutralResult = sentimentAnalyzer.analyzeSentiment("This movie is okay and decent.")
        
        // Then
        assertEquals("Positive text should be positive", SentimentType.POSITIVE, positiveResult.sentiment)
        assertEquals("Negative text should be negative", SentimentType.NEGATIVE, negativeResult.sentiment)
        assertEquals("Neutral text should be neutral", SentimentType.NEUTRAL, neutralResult.sentiment)
        
        // All should be successful
        assertTrue("Positive result should be successful", positiveResult.isSuccess)
        assertTrue("Negative result should be successful", negativeResult.isSuccess)
        assertTrue("Neutral result should be successful", neutralResult.isSuccess)
    }

    @Test
    fun `test confidence score consistency`() = runBlocking {
        // Given
        sentimentAnalyzer.initialize()
        
        // When
        val strongPositiveResult = sentimentAnalyzer.analyzeSentiment("This movie is absolutely amazing, fantastic, and wonderful!")
        val weakPositiveResult = sentimentAnalyzer.analyzeSentiment("This movie is pretty good.")
        
        // Then
        assertTrue("Strong positive should have higher confidence", 
            strongPositiveResult.confidence >= weakPositiveResult.confidence)
        assertTrue("Strong positive confidence should be reasonable", 
            strongPositiveResult.confidence > 0.5)
        assertTrue("Weak positive confidence should be reasonable", 
            weakPositiveResult.confidence > 0.3)
    }
}
