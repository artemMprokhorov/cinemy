package org.studioapp.cinemy.ml.mlfactory

import org.studioapp.cinemy.ml.MLConstants.MODIFIER_ABSOLUTELY
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_BARELY
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_COMPLETELY
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_EXTREMELY
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_INCREDIBLY
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_NEVER
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_NOT
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_PRETTY
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_REALLY
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_SLIGHTLY
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_SOMEWHAT
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_TOTALLY
import org.studioapp.cinemy.ml.MLConstants.MODIFIER_VERY

/**
 * Factory for creating intensity modifier mappings for sentiment analysis.
 * 
 * This factory provides centralized management of intensity modifier weights and values
 * used in sentiment analysis algorithms. It serves as the primary source for intensity
 * modifiers that adjust sentiment confidence scores based on intensity words found in text.
 * 
 * The factory supports sentiment analysis by providing modifier weights that:
 * - Amplify positive sentiment with high-intensity positive modifiers (absolutely, extremely)
 * - Reduce sentiment intensity with moderate modifiers (pretty, somewhat, slightly)
 * - Negate sentiment with negative modifiers (not, never, barely)
 * 
 * Intensity modifiers are applied during sentiment analysis to adjust confidence scores
 * based on the strength of language used in the analyzed text.
 */
object IntensityModifiersFactory {

    /**
     * Creates a comprehensive map of intensity modifiers with their corresponding weight values.
     * 
     * This method creates a complete set of intensity modifiers specifically designed
     * for sentiment analysis. It includes positive intensity amplifiers (absolutely,
     * extremely, very), moderate intensity adjusters (pretty, somewhat, slightly),
     * and negative intensity modifiers (not, never, barely).
     * 
     * The returned map contains 13 intensity modifiers with weights ranging from -1.0 to 1.5:
     * - Positive amplifiers: 1.1 to 1.5 (absolutely=1.5, extremely=1.3, very=1.2)
     * - Moderate adjusters: 0.6 to 0.8 (pretty=0.8, somewhat=0.7, slightly=0.6)
     * - Negative modifiers: -1.0 to -0.5 (not=-1.0, never=-1.0, barely=-0.5)
     * 
     * These modifiers are used during sentiment analysis to adjust confidence scores
     * based on the intensity of language found in the analyzed text.
     * 
     * @return Map of modifier strings to their intensity weights, containing 13 modifiers
     *         with weights ranging from -1.0 to 1.5
     */
    fun createIntensityModifiers(): Map<String, Double> {
        return mapOf(
            MODIFIER_ABSOLUTELY to 1.5,
            MODIFIER_COMPLETELY to 1.4,
            MODIFIER_TOTALLY to 1.3,
            MODIFIER_EXTREMELY to 1.3,
            MODIFIER_INCREDIBLY to 1.3,
            MODIFIER_VERY to 1.2,
            MODIFIER_REALLY to 1.1,
            MODIFIER_PRETTY to 0.8,
            MODIFIER_SOMEWHAT to 0.7,
            MODIFIER_SLIGHTLY to 0.6,
            MODIFIER_NOT to -1.0,
            MODIFIER_NEVER to -1.0,
            MODIFIER_BARELY to -0.5
        )
    }
}
