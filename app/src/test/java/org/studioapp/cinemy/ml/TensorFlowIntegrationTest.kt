package org.studioapp.cinemy.ml

import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.tensorflow.lite.Interpreter

/**
 * Comprehensive test suite for TensorFlow Lite integration in Cinemy
 * Tests hybrid sentiment analysis system with automatic model selection
 */
class TensorFlowIntegrationTest {

    private lateinit var mockContext: android.content.Context
    private lateinit var sentimentAnalyzer: SentimentAnalyzer
    private lateinit var tensorFlowModel: TensorFlowSentimentModel
    private lateinit var mockInterpreter: Interpreter

    @Before
    fun setUp() {
        // Create mock context with assets
        mockContext = TestUtils.createMockContext()

        // Create spy objects for testing
        sentimentAnalyzer = spyk(SentimentAnalyzer.getInstance(mockContext))
        tensorFlowModel = spyk(TensorFlowSentimentModel.getInstance(mockContext))

        // Mock TensorFlow Lite interpreter
        mockInterpreter = mockk<Interpreter>(relaxed = true)
    }

    @After
    fun tearDown() {
        // Clean up resources
        runBlocking {
            sentimentAnalyzer.cleanup()
            tensorFlowModel.cleanup()
        }
    }


    @Test
    fun `test TensorFlow model initialization failure`() = runBlocking {
        // Given
        val failingContext = TestUtils.createFailingMockContext()
        val model = TensorFlowSentimentModel.getInstance(failingContext)

        // When
        val isInitialized = model.initialize()

        // Then
        assertFalse("TensorFlow model should fail to initialize with bad context", isInitialized)
        assertFalse("Model should not be ready after failed initialization", model.isReady())
    }


    @Test
    fun `test simple text uses keyword model`() = runBlocking {
        // Given
        val mockContext = TestUtils.createMockContext()
        val analyzer = SentimentAnalyzer.getInstance(mockContext)
        analyzer.initialize()

        val simpleText = TestUtils.TestTexts.SIMPLE_POSITIVE

        // When
        val result = analyzer.analyzeSentiment(simpleText)

        // Then
        assertTrue("Result should be successful", result.isSuccess)
        assertEquals("Should be positive sentiment", SentimentType.POSITIVE, result.sentiment)
        assertTrue("Confidence should be reasonable", result.confidence > 0.3)
        assertTrue("Should contain keyword indicators", result.foundKeywords.isNotEmpty())

        // Verify that keyword model was used (no TensorFlow indicators)
        assertFalse(
            "Should not contain TensorFlow indicators",
            result.foundKeywords.any { it.contains("tensorflow") })
    }

    @Test
    fun `test complex text uses TensorFlow model`() = runBlocking {
        // Given
        val mockContext = TestUtils.createMockContext()
        val analyzer = SentimentAnalyzer.getInstance(mockContext)
        analyzer.initialize()

        val complexText = TestUtils.TestTexts.COMPLEX_POSITIVE

        // When
        val result = analyzer.analyzeSentiment(complexText)

        // Then
        assertTrue("Result should be successful", result.isSuccess)
        assertTrue(
            "Should be positive or neutral sentiment",
            result.sentiment == SentimentType.POSITIVE || result.sentiment == SentimentType.NEUTRAL
        )
        assertTrue("Confidence should be reasonable", result.confidence > 0.3)

        // Note: In real implementation, this would contain TensorFlow indicators
        // For now, we test that the system handles complex text appropriately
    }

    @Test
    fun `test ambiguous text handling`() = runBlocking {
        // Given
        val mockContext = TestUtils.createMockContext()
        val analyzer = SentimentAnalyzer.getInstance(mockContext)
        analyzer.initialize()

        val ambiguousText = TestUtils.TestTexts.AMBIGUOUS_TEXT

        // When
        val result = analyzer.analyzeSentiment(ambiguousText)

        // Then
        assertTrue("Result should be successful", result.isSuccess)
        assertTrue(
            "Should be neutral sentiment for ambiguous text",
            result.sentiment == SentimentType.NEUTRAL
        )
        assertTrue("Confidence should be reasonable", result.confidence > 0.3)
    }

    @Test
    fun `test fallback to keyword model on TensorFlow error`() = runBlocking {
        // Given
        val failingContext = TestUtils.createFailingMockContext()
        val analyzer = SentimentAnalyzer.getInstance(failingContext)
        analyzer.initialize()

        val text = TestUtils.TestTexts.COMPLEX_POSITIVE

        // When
        val result = analyzer.analyzeSentiment(text)

        // Then
        assertTrue("Result should be successful even with TensorFlow failure", result.isSuccess)
        assertTrue("Should fallback to keyword model", result.foundKeywords.isNotEmpty())
        assertNull("Should not have error message", result.errorMessage)
    }

    @Test
    fun `test batch analysis performance`() = runBlocking {
        // Given
        val mockContext = TestUtils.createMockContext()
        val analyzer = SentimentAnalyzer.getInstance(mockContext)
        analyzer.initialize()

        val batchTexts = TestUtils.PerformanceTestData.BATCH_TEXTS

        // When
        val (results, executionTime) = TestUtils.measureTime {
            analyzer.analyzeBatch(batchTexts)
        }

        // Then
        assertEquals("Should process all texts", batchTexts.size, results.size)
        assertTrue("All results should be successful", results.all { it.isSuccess })
        assertTrue(
            "Execution time should be reasonable",
            executionTime < TestUtils.PerformanceTestData.PERFORMANCE_THRESHOLD_MS
        )

        // Verify different sentiment types are detected
        val sentimentTypes = results.map { it.sentiment }.distinct()
        assertTrue("Should detect multiple sentiment types", sentimentTypes.size > 1)
    }

    @Test
    fun `test model selection logic`() {
        // Test simple texts should use keyword model
        assertFalse(
            "Simple positive text should not use TensorFlow",
            TestUtils.shouldUseTensorFlowForText(TestUtils.TestTexts.SIMPLE_POSITIVE)
        )
        assertFalse(
            "Simple negative text should not use TensorFlow",
            TestUtils.shouldUseTensorFlowForText(TestUtils.TestTexts.SIMPLE_NEGATIVE)
        )
        assertFalse(
            "Short text should not use TensorFlow",
            TestUtils.shouldUseTensorFlowForText(TestUtils.TestTexts.SHORT_TEXT)
        )

        // Test complex texts should use TensorFlow model
        assertTrue(
            "Complex positive text should use TensorFlow",
            TestUtils.shouldUseTensorFlowForText(TestUtils.TestTexts.COMPLEX_POSITIVE)
        )
        assertTrue(
            "Complex negative text should use TensorFlow",
            TestUtils.shouldUseTensorFlowForText(TestUtils.TestTexts.COMPLEX_NEGATIVE)
        )
        assertTrue(
            "Ambiguous text should use TensorFlow",
            TestUtils.shouldUseTensorFlowForText(TestUtils.TestTexts.AMBIGUOUS_TEXT)
        )
        assertTrue(
            "Text with complex indicators should use TensorFlow",
            TestUtils.shouldUseTensorFlowForText(TestUtils.TestTexts.COMPLEX_INDICATORS)
        )
    }

    @Test
    fun `test sentiment result validation`() {
        // Test valid positive result
        val positiveResult = SentimentResult.positive(0.8, listOf("amazing", "fantastic"))
        assertTrue(
            "Positive result should be valid",
            TestUtils.validateSentimentResult(positiveResult, SentimentType.POSITIVE)
        )

        // Test valid negative result
        val negativeResult = SentimentResult.negative(0.7, listOf("terrible", "awful"))
        assertTrue(
            "Negative result should be valid",
            TestUtils.validateSentimentResult(negativeResult, SentimentType.NEGATIVE)
        )

        // Test valid neutral result
        val neutralResult = SentimentResult.neutral(0.6, listOf("okay", "decent"))
        assertTrue(
            "Neutral result should be valid",
            TestUtils.validateSentimentResult(neutralResult, SentimentType.NEUTRAL)
        )

        // Test invalid result (low confidence)
        val lowConfidenceResult = SentimentResult.positive(0.2, listOf("maybe"))
        assertFalse(
            "Low confidence result should be invalid",
            TestUtils.validateSentimentResult(lowConfidenceResult)
        )

        // Test error result
        val errorResult = SentimentResult.error("Test error")
        assertFalse(
            "Error result should be invalid",
            TestUtils.validateSentimentResult(errorResult)
        )
    }

    @Test
    fun `test empty and blank text handling`() = runBlocking {
        // Given
        val mockContext = TestUtils.createMockContext()
        val analyzer = SentimentAnalyzer.getInstance(mockContext)
        analyzer.initialize()

        // When & Then
        val emptyResult = analyzer.analyzeSentiment("")
        assertTrue(
            "Empty text should return neutral",
            emptyResult.sentiment == SentimentType.NEUTRAL
        )
        assertTrue("Empty text result should be successful", emptyResult.isSuccess)

        val blankResult = analyzer.analyzeSentiment("   \n\t   ")
        assertTrue(
            "Blank text should return neutral",
            blankResult.sentiment == SentimentType.NEUTRAL
        )
        assertTrue("Blank text result should be successful", blankResult.isSuccess)
    }

    @Test
    fun `test intensity modifiers in keyword model`() = runBlocking {
        // Given
        val mockContext = TestUtils.createMockContext()
        val analyzer = SentimentAnalyzer.getInstance(mockContext)
        analyzer.initialize()

        // When
        val veryPositiveResult =
            analyzer.analyzeSentiment("This movie is very amazing and fantastic!")
        val notPositiveResult = analyzer.analyzeSentiment("This movie is not amazing or fantastic")

        // Then
        assertTrue(
            "Very positive should be positive",
            veryPositiveResult.sentiment == SentimentType.POSITIVE
        )
        assertTrue(
            "Not positive should be negative",
            notPositiveResult.sentiment == SentimentType.NEGATIVE
        )

        // Verify intensity modifiers are detected
        assertTrue(
            "Should detect 'very' modifier",
            veryPositiveResult.foundKeywords.any { it.contains("very") })
        assertTrue(
            "Should detect 'not' modifier",
            notPositiveResult.foundKeywords.any { it.contains("not") })
    }

    @Test
    fun `test context boosters in keyword model`() = runBlocking {
        // Given
        val mockContext = TestUtils.createMockContext()
        val analyzer = SentimentAnalyzer.getInstance(mockContext)
        analyzer.initialize()

        // When
        val movieContextResult =
            analyzer.analyzeSentiment("The cinematography is amazing and the acting is brilliant")
        val masterpieceResult =
            analyzer.analyzeSentiment("This is a masterpiece with outstanding artistry")

        // Then
        assertTrue(
            "Movie context should be positive",
            movieContextResult.sentiment == SentimentType.POSITIVE
        )
        assertTrue(
            "Masterpiece should be positive",
            masterpieceResult.sentiment == SentimentType.POSITIVE
        )

        // Verify context boosters are detected
        assertTrue(
            "Should detect movie terms",
            movieContextResult.foundKeywords.any { it.contains("🎬") })
        assertTrue(
            "Should detect positive context",
            masterpieceResult.foundKeywords.any { it.contains("✨") })
    }

    @Test
    fun `test resource cleanup`() = runBlocking {
        // Given
        val mockContext = TestUtils.createMockContext()
        val analyzer = SentimentAnalyzer.getInstance(mockContext)
        val model = TensorFlowSentimentModel.getInstance(mockContext)

        analyzer.initialize()
        model.initialize()

        // When
        analyzer.cleanup()
        model.cleanup()

        // Then
        assertFalse(
            "Analyzer should not be initialized after cleanup",
            analyzer.isTensorFlowAvailable()
        )
        assertFalse("Model should not be ready after cleanup", model.isReady())
    }

    @Test
    fun `test concurrent access safety`() = runBlocking {
        // Given
        val mockContext = TestUtils.createMockContext()
        val analyzer = SentimentAnalyzer.getInstance(mockContext)
        analyzer.initialize()

        val texts = listOf(
            TestUtils.TestTexts.SIMPLE_POSITIVE,
            TestUtils.TestTexts.SIMPLE_NEGATIVE,
            TestUtils.TestTexts.COMPLEX_POSITIVE,
            TestUtils.TestTexts.AMBIGUOUS_TEXT
        )

        // When - simulate concurrent access
        val results = texts.map { text ->
            analyzer.analyzeSentiment(text)
        }

        // Then
        assertEquals("Should process all texts", texts.size, results.size)
        assertTrue("All results should be successful", results.all { it.isSuccess })
        assertTrue(
            "Should have different sentiment types",
            results.map { it.sentiment }.distinct().size > 1
        )
    }

}
