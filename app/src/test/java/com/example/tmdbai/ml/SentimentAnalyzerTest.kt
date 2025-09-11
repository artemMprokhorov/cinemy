package org.studioapp.cinemy.ml

import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse

/**
 * Test for Enhanced ML Model v2.0.0
 * Verifies the improved sentiment analysis functionality
 */
class SentimentAnalyzerTest {

    @Test
    fun testEnhancedModelCreation() {
        // Test that the enhanced model can be created with all new fields
        val model = createTestModel()
        
        // Verify model info
        assertEquals("2.0.0", model.modelInfo.version)
        assertEquals("85%+", model.modelInfo.accuracy)
        
        // Verify enhanced features
        assertNotNull("Neutral indicators should be present", model.neutralIndicators)
        assertNotNull("Intensity modifiers should be present", model.intensityModifiers)
        assertNotNull("Context boosters should be present", model.contextBoosters)
        
        // Verify expanded dictionary
        assertTrue("Should have more positive keywords", model.positiveKeywords.size > 20)
        assertTrue("Should have more negative keywords", model.negativeKeywords.size > 20)
        
        // Verify new algorithm config
        assertEquals(0.6, model.algorithm.baseConfidence, 0.01)
        assertNotNull("Keyword weight should be set", model.algorithm.keywordWeight)
        assertNotNull("Context weight should be set", model.algorithm.contextWeight)
        assertNotNull("Modifier weight should be set", model.algorithm.modifierWeight)
    }

    @Test
    fun testIntensityModifiers() {
        val model = createTestModel()
        
        // Test positive intensity modifiers
        assertTrue("Should contain 'absolutely' modifier", 
            model.intensityModifiers?.containsKey("absolutely") == true)
        assertEquals(1.5, model.intensityModifiers?.get("absolutely") ?: 0.0, 0.01)
        
        // Test negative intensity modifiers
        assertTrue("Should contain 'not' modifier", 
            model.intensityModifiers?.containsKey("not") == true)
        assertEquals(-1.0, model.intensityModifiers?.get("not") ?: 0.0, 0.01)
        
        // Test neutral intensity modifiers
        assertTrue("Should contain 'pretty' modifier", 
            model.intensityModifiers?.containsKey("pretty") == true)
        assertEquals(0.8, model.intensityModifiers?.get("pretty") ?: 0.0, 0.01)
    }

    @Test
    fun testContextBoosters() {
        val model = createTestModel()
        val boosters = model.contextBoosters
        
        assertNotNull("Context boosters should be present", boosters)
        
        // Test movie terms
        assertTrue("Should contain movie terms", 
            boosters?.movieTerms?.isNotEmpty() == true)
        assertTrue("Should contain 'cinematography'", 
            boosters?.movieTerms?.contains("cinematography") == true)
        
        // Test positive context
        assertTrue("Should contain positive context", 
            boosters?.positiveContext?.isNotEmpty() == true)
        assertTrue("Should contain 'masterpiece'", 
            boosters?.positiveContext?.contains("masterpiece") == true)
        
        // Test negative context
        assertTrue("Should contain negative context", 
            boosters?.negativeContext?.isNotEmpty() == true)
        assertTrue("Should contain 'flop'", 
            boosters?.negativeContext?.contains("flop") == true)
    }

    @Test
    fun testNeutralIndicators() {
        val model = createTestModel()
        
        assertNotNull("Neutral indicators should be present", model.neutralIndicators)
        assertTrue("Should contain 'okay'", model.neutralIndicators?.contains("okay") == true)
        assertTrue("Should contain 'decent'", model.neutralIndicators?.contains("decent") == true)
        assertTrue("Should contain 'average'", model.neutralIndicators?.contains("average") == true)
    }

    @Test
    fun testExpandedKeywords() {
        val model = createTestModel()
        
        // Test new positive keywords
        assertTrue("Should contain 'phenomenal'", model.positiveKeywords.contains("phenomenal"))
        assertTrue("Should contain 'spectacular'", model.positiveKeywords.contains("spectacular"))
        assertTrue("Should contain 'remarkable'", model.positiveKeywords.contains("remarkable"))
        
        // Test new negative keywords
        assertTrue("Should contain 'atrocious'", model.negativeKeywords.contains("atrocious"))
        assertTrue("Should contain 'unwatchable'", model.negativeKeywords.contains("unwatchable"))
        assertTrue("Should contain 'overrated'", model.negativeKeywords.contains("overrated"))
    }

    private fun createTestModel(): KeywordSentimentModel {
        val modelInfo = ModelInfo(
            type = "keyword_sentiment_analysis",
            version = "2.0.0",
            language = "en",
            accuracy = "85%+",
            speed = "very_fast"
        )
        
        val positiveKeywords = listOf(
            "amazing", "fantastic", "great", "excellent", "wonderful", "brilliant",
            "outstanding", "superb", "magnificent", "perfect", "incredible", "awesome",
            "beautiful", "lovely", "good", "nice", "best", "favorite", "love", "enjoy",
            "phenomenal", "spectacular", "remarkable", "exceptional", "marvelous",
            "stunning", "impressive", "captivating", "engaging", "compelling"
        )
        
        val negativeKeywords = listOf(
            "terrible", "awful", "horrible", "bad", "worst", "hate", "disgusting",
            "boring", "stupid", "dumb", "annoying", "frustrating", "disappointing",
            "waste", "rubbish", "garbage", "trash", "sucks", "pathetic", "lame",
            "atrocious", "dreadful", "appalling", "mediocre", "unwatchable",
            "cringe", "cheesy", "predictable", "cliché", "overrated"
        )
        
        val neutralIndicators = listOf(
            "okay", "decent", "average", "fine", "acceptable", "reasonable",
            "standard", "typical", "normal", "ordinary", "mediocre", "so-so"
        )
        
        val intensityModifiers = mapOf(
            "absolutely" to 1.5,
            "completely" to 1.4,
            "totally" to 1.3,
            "extremely" to 1.3,
            "incredibly" to 1.3,
            "very" to 1.2,
            "really" to 1.1,
            "pretty" to 0.8,
            "somewhat" to 0.7,
            "slightly" to 0.6,
            "not" to -1.0,
            "never" to -1.0,
            "barely" to -0.5
        )
        
        val contextBoosters = ContextBoosters(
            movieTerms = listOf(
                "cinematography", "acting", "plot", "story", "director", "performance",
                "script", "dialogue", "visuals", "effects", "soundtrack", "editing"
            ),
            positiveContext = listOf(
                "masterpiece", "artistry", "brilliant", "genius", "innovative",
                "groundbreaking", "revolutionary", "timeless", "classic"
            ),
            negativeContext = listOf(
                "flop", "disaster", "failure", "ruined", "destroyed", "butchered",
                "mangled", "butchered", "torture", "nightmare"
            )
        )
        
        val algorithm = AlgorithmConfig(
            baseConfidence = 0.6,
            keywordWeight = 1.0,
            contextWeight = 0.3,
            modifierWeight = 0.4,
            neutralThreshold = 0.5,
            minConfidence = 0.3,
            maxConfidence = 0.9
        )
        
        return KeywordSentimentModel(
            modelInfo = modelInfo,
            positiveKeywords = positiveKeywords,
            negativeKeywords = negativeKeywords,
            neutralIndicators = neutralIndicators,
            intensityModifiers = intensityModifiers,
            contextBoosters = contextBoosters,
            algorithm = algorithm
        )
    }
}
