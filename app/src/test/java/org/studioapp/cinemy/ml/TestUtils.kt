package org.studioapp.cinemy.ml

import android.content.Context
import android.content.res.AssetManager
import io.mockk.every
import io.mockk.mockk
import java.io.ByteArrayInputStream
import org.studioapp.cinemy.ml.model.SentimentResult
import org.studioapp.cinemy.ml.model.SentimentType

/**
 * Test utilities for ML testing in Cinemy
 * Provides mock contexts and helper methods for testing TensorFlow Lite integration
 */
object TestUtils {

    /**
     * Create a mock context with asset manager for testing
     * Uses real asset files when available, falls back to mocks
     */
    fun createMockContext(): Context {
        val mockContext = mockk<Context>(relaxed = true)
        val mockAssetManager = createMockAssetManager()

        every { mockContext.assets } returns mockAssetManager
        every { mockContext.applicationContext } returns mockContext

        return mockContext
    }

    /**
     * Create a real context for integration testing with actual model files
     */
    fun createRealContext(): Context {
        // For unit tests, we'll use a mock that simulates real file access
        val mockContext = mockk<Context>(relaxed = true)
        val mockAssetManager = createRealAssetManager()

        every { mockContext.assets } returns mockAssetManager
        every { mockContext.applicationContext } returns mockContext

        return mockContext
    }

    /**
     * Create a real asset manager that simulates actual model files
     */
    private fun createRealAssetManager(): AssetManager {
        val mockAssetManager = mockk<AssetManager>(relaxed = true)

        // Mock TensorFlow Lite model file (simulate real model)
        val tfliteModelContent = ByteArray(1024 * 1024) // Mock 1MB model file
        val tfliteInputStream = ByteArrayInputStream(tfliteModelContent)

        // Mock multilingual sentiment model
        val sentimentModelContent = """
        {
          "model_info": {
            "type": "multilingual_sentiment_analysis",
            "version": "2.0.0",
            "languages": ["en", "es", "ru"],
            "training_samples": 100000,
            "accuracy": 0.95,
            "features": 1000,
            "vocabulary_size": 50000,
            "created": "2024-01-01",
            "performance": {
              "training_time_seconds": 3600.0,
              "prediction_time_ms": "50ms",
              "supported_complexity": ["simple", "medium", "complex"]
            }
          },
          "model_data": {
            "weights": [0.1, 0.2, 0.3, 0.4, 0.5]
          }
        }
        """.trimIndent().toByteArray()
        val sentimentInputStream = ByteArrayInputStream(sentimentModelContent)

        // Mock configuration file
        val configContent = """
        {
          "tensorflow_lite": {
            "model_file": "production_sentiment_full_manual.tflite",
            "model_type": "sentiment_analysis",
            "version": "2.0.0",
            "input_config": {
              "input_tensor_name": "input_text",
              "input_shape": [1, 512],
              "input_type": "string",
              "preprocessing": {
                "max_length": 512,
                "padding": "max_length",
                "truncation": true,
                "lowercase": true,
                "remove_punctuation": false
              }
            },
            "output_config": {
              "output_tensor_name": "output_sentiment",
              "output_shape": [1, 3],
              "output_type": "float",
              "class_labels": ["negative", "neutral", "positive"],
              "confidence_threshold": 0.6
            },
            "performance": {
              "use_gpu": true,
              "use_nnapi": true,
              "use_xnnpack": true,
              "num_threads": 4
            },
            "fallback": {
              "use_keyword_model": true,
              "fallback_threshold": 0.3
            }
          },
          "hybrid_system": {
            "model_selection": {
              "use_tensorflow_for": ["complex", "medium"],
              "use_keyword_for": ["simple"],
              "complexity_threshold": 10,
              "confidence_threshold": 0.7
            },
            "integration": {
              "seamless_fallback": true,
              "performance_monitoring": true,
              "cache_results": true,
              "cache_duration_minutes": 30
            }
          }
        }
        """.trimIndent().toByteArray()
        val configInputStream = ByteArrayInputStream(configContent)

        // Mock vocabulary file
        val vocabContent = """
        {
          "[PAD]": 0,
          "[UNK]": 100,
          "[CLS]": 101,
          "[SEP]": 102,
          "[MASK]": 103,
          "the": 104,
          "is": 105,
          "amazing": 106,
          "terrible": 107,
          "good": 108,
          "bad": 109
        }
        """.trimIndent().toByteArray()
        val vocabInputStream = ByteArrayInputStream(vocabContent)

        // Mock file descriptors for assets
        every { 
            mockAssetManager.openFd("ml_models/production_sentiment_full_manual.tflite") 
        } returns mockk<android.content.res.AssetFileDescriptor> {
            every { fileDescriptor } returns mockk()
            every { startOffset } returns 0L
            every { declaredLength } returns tfliteModelContent.size.toLong()
        }

        every { 
            mockAssetManager.open("ml_models/multilingual_sentiment_production.json") 
        } returns sentimentInputStream

        every { 
            mockAssetManager.open("ml_models/android_integration_config.json") 
        } returns configInputStream

        every { 
            mockAssetManager.open("ml_models/vocab.json") 
        } returns vocabInputStream

        return mockAssetManager
    }

    /**
     * Create a mock asset manager with predefined assets
     */
    private fun createMockAssetManager(): AssetManager {
        val mockAssetManager = mockk<AssetManager>(relaxed = true)

        // Mock TensorFlow Lite model file (empty but valid)
        val tfliteModelContent = ByteArray(1024) // Mock 1KB model file
        val tfliteInputStream = ByteArrayInputStream(tfliteModelContent)

        // Mock configuration file
        val configContent = """
        {
          "tensorflow_lite": {
            "model_file": "production_sentiment_full_manual.tflite",
            "model_type": "sentiment_analysis",
            "version": "1.0.0",
            "input_config": {
              "input_tensor_name": "input_text",
              "input_shape": [1, 512],
              "input_type": "string",
              "preprocessing": {
                "max_length": 512,
                "padding": "post",
                "truncation": true,
                "lowercase": true,
                "remove_punctuation": false
              }
            },
            "output_config": {
              "output_tensor_name": "output_sentiment",
              "output_shape": [1, 3],
              "output_type": "float",
              "class_labels": ["negative", "neutral", "positive"],
              "confidence_threshold": 0.6
            },
            "performance": {
              "use_gpu": false,
              "use_nnapi": true,
              "num_threads": 4,
              "enable_xnnpack": true
            },
            "fallback": {
              "use_keyword_model": true,
              "fallback_threshold": 0.3
            }
          },
          "hybrid_system": {
            "model_selection": {
              "use_tensorflow_for": [
                "complex_text",
                "long_reviews",
                "ambiguous_sentiment"
              ],
              "use_keyword_for": [
                "short_text",
                "simple_sentiment",
                "fast_processing"
              ],
              "complexity_threshold": 50,
              "confidence_threshold": 0.7
            },
            "integration": {
              "seamless_fallback": true,
              "performance_monitoring": true,
              "cache_results": true,
              "cache_duration_minutes": 30
            }
          }
        }
        """.trimIndent()
        val configInputStream = ByteArrayInputStream(configContent.toByteArray())

        // Mock keyword model file
        val keywordModelContent = """
        {
          "model_info": {
            "type": "keyword_sentiment_analysis",
            "version": "2.0.0",
            "language": "en",
            "accuracy": "85%+",
            "speed": "very_fast"
          },
          "positive_keywords": ["amazing", "fantastic", "great", "excellent", "wonderful"],
          "negative_keywords": ["terrible", "awful", "horrible", "bad", "worst"],
          "neutral_indicators": ["okay", "decent", "average", "fine"],
          "intensity_modifiers": {
            "very": 1.2,
            "really": 1.1,
            "not": -1.0,
            "never": -1.0
          },
          "algorithm": {
            "base_confidence": 0.6,
            "neutral_threshold": 0.5,
            "min_confidence": 0.3,
            "max_confidence": 0.9
          }
        }
        """.trimIndent()
        val keywordModelInputStream = ByteArrayInputStream(keywordModelContent.toByteArray())

        // Setup mock responses
        every {
            mockAssetManager.open("ml_models/production_sentiment_full_manual.tflite")
        } returns tfliteInputStream

        every {
            mockAssetManager.open("ml_models/android_integration_config.json")
        } returns configInputStream

        every {
            mockAssetManager.open("ml_models/multilingual_sentiment_production.json")
        } returns keywordModelInputStream

        every {
            mockAssetManager.open("ml_models/multilingual_sentiment_production.json")
        } returns keywordModelInputStream

        return mockAssetManager
    }

    /**
     * Create a mock context that fails to load assets (for error testing)
     */
    fun createFailingMockContext(): Context {
        val mockContext = mockk<Context>(relaxed = true)
        val mockAssetManager = mockk<AssetManager>(relaxed = true)

        every { mockContext.assets } returns mockAssetManager
        every { mockContext.applicationContext } returns mockContext

        // Make asset loading fail
        every { mockAssetManager.open(any()) } throws Exception("Asset not found")

        return mockContext
    }

    /**
     * Test texts for different complexity levels
     */
    object TestTexts {
        // Simple texts (should use keyword model)
        val SIMPLE_POSITIVE = "This movie is amazing and fantastic!"
        val SIMPLE_NEGATIVE = "This movie is terrible and awful."
        val SIMPLE_NEUTRAL = "This movie is okay and decent."
        val SHORT_TEXT = "Great movie!"

        // Complex texts (should use TensorFlow model)
        val COMPLEX_POSITIVE = """
            This film is absolutely phenomenal and represents a masterful achievement in 
            cinematography. The director's vision is brilliantly executed, creating an 
            immersive experience that captivates from beginning to end. The performances 
            are outstanding, particularly the lead actor's nuanced portrayal of complex 
            emotions. The screenplay is intelligent and thought-provoking, weaving together 
            multiple narrative threads with remarkable skill. The visual effects are 
            stunning and serve the story rather than overwhelming it. This is truly a 
            masterpiece that will be remembered for years to come.
        """.trimIndent()

        val COMPLEX_NEGATIVE = """
            Unfortunately, this film fails to deliver on its promising premise. Despite 
            having a talented cast and substantial budget, the execution is fundamentally 
            flawed. The pacing is inconsistent, with long stretches of tedium punctuated 
            by moments of confusion. The script suffers from poor dialogue and contrived 
            plot developments that strain credibility. The director's choices seem 
            misguided, resulting in a disjointed narrative that never finds its rhythm. 
            While there are occasional glimpses of potential, they are quickly overshadowed 
            by the film's numerous shortcomings. This is a disappointing effort that 
            squanders its resources and fails to engage the audience.
        """.trimIndent()

        val COMPLEX_NEUTRAL = """
            This film presents an interesting but ultimately mixed experience. The 
            cinematography is competent and the acting is generally solid, though 
            nothing particularly remarkable. The story has some intriguing elements, 
            but the execution is somewhat uneven. There are moments of genuine 
            entertainment, but they are balanced by periods of mediocrity. The film 
            doesn't quite succeed in being either a compelling drama or an engaging 
            thriller, falling somewhere in between. It's the kind of movie that might 
            work for a casual viewing, but doesn't leave a lasting impression.
        """.trimIndent()

        // Ambiguous texts (should use TensorFlow model)
        val AMBIGUOUS_TEXT = """
            This movie is interesting and different, but I'm not sure if I liked it 
            or not. The story was unique and unusual, which made it complex to follow. 
            The acting was mixed - some performances were good while others were 
            disappointing. Overall, it's a varied experience that's hard to categorize.
        """.trimIndent()

        // Texts with complex indicators
        val COMPLEX_INDICATORS = """
            The film has excellent cinematography and outstanding performances. However, 
            the script is problematic and the pacing is inconsistent. On the other hand, 
            the visual effects are impressive. Nevertheless, the overall experience is 
            mixed due to these conflicting elements.
        """.trimIndent()
    }

    /**
     * Performance test data
     */
    object PerformanceTestData {
        val BATCH_TEXTS = listOf(
            TestTexts.SIMPLE_POSITIVE,
            TestTexts.SIMPLE_NEGATIVE,
            TestTexts.SIMPLE_NEUTRAL,
            TestTexts.COMPLEX_POSITIVE,
            TestTexts.COMPLEX_NEGATIVE,
            TestTexts.AMBIGUOUS_TEXT
        )

        const val PERFORMANCE_THRESHOLD_MS = 1000L // 1 second threshold
    }

    /**
     * Expected results for validation
     */
    object ExpectedResults {
        val SIMPLE_POSITIVE_SENTIMENT = SentimentType.POSITIVE
        val SIMPLE_NEGATIVE_SENTIMENT = SentimentType.NEGATIVE
        val SIMPLE_NEUTRAL_SENTIMENT = SentimentType.NEUTRAL

        val MIN_CONFIDENCE_THRESHOLD = 0.3
        val MAX_CONFIDENCE_THRESHOLD = 0.9
    }

    /**
     * Helper method to measure execution time
     */
    inline fun <T> measureTime(block: () -> T): Pair<T, Long> {
        val startTime = System.currentTimeMillis()
        val result = block()
        val endTime = System.currentTimeMillis()
        return Pair(result, endTime - startTime)
    }

    /**
     * Helper method to validate sentiment result
     */
    fun validateSentimentResult(
        result: SentimentResult,
        expectedSentiment: SentimentType? = null,
        minConfidence: Double = ExpectedResults.MIN_CONFIDENCE_THRESHOLD,
        maxConfidence: Double = ExpectedResults.MAX_CONFIDENCE_THRESHOLD
    ): Boolean {
        return result.isSuccess &&
                (expectedSentiment == null || result.sentiment == expectedSentiment) &&
                result.confidence >= minConfidence &&
                result.confidence <= maxConfidence &&
                result.errorMessage == null
    }

    /**
     * Helper method to check if text should use TensorFlow model
     */
    fun shouldUseTensorFlowForText(text: String): Boolean {
        val complexityThreshold = 50
        val complexIndicators = listOf(
            "however", "although", "but", "yet", "despite", "nevertheless",
            "on the other hand", "in contrast", "meanwhile", "furthermore",
            "moreover", "additionally", "consequently", "therefore", "thus"
        )
        val ambiguousWords = listOf(
            "interesting", "different", "unique", "unusual", "strange",
            "complex", "complicated", "mixed", "varied", "diverse"
        )

        val textLower = text.lowercase()

        return text.length > complexityThreshold ||
                complexIndicators.any { textLower.contains(it) } ||
                ambiguousWords.any { textLower.contains(it) }
    }
}
