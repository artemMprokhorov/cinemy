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
 * Factory for creating intensity modifier mappings for sentiment analysis
 * Provides centralized management of modifier weights and intensities
 */
object IntensityModifiersFactory {

    /**
     * Creates a map of intensity modifiers with their corresponding weight values
     * Used for sentiment analysis to adjust confidence based on intensity words
     * 
     * @return Map of modifier strings to their intensity weights
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
