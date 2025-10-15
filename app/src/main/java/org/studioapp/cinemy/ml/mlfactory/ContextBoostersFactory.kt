package org.studioapp.cinemy.ml.mlfactory

import org.studioapp.cinemy.ml.model.ContextBoosters
import org.studioapp.cinemy.ml.model.EnhancedModelData
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_CINEMATOGRAPHY
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_ACTING
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_PLOT
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_STORY
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_DIRECTOR
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_PERFORMANCE
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_SCRIPT
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_DIALOGUE
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_VISUALS
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_EFFECTS
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_SOUNDTRACK
import org.studioapp.cinemy.ml.MLConstants.CONTEXT_EDITING
import org.studioapp.cinemy.ml.MLConstants.QUALITY_MASTERPIECE
import org.studioapp.cinemy.ml.MLConstants.QUALITY_ARTISTRY
import org.studioapp.cinemy.ml.MLConstants.QUALITY_BRILLIANT
import org.studioapp.cinemy.ml.MLConstants.QUALITY_GENIUS
import org.studioapp.cinemy.ml.MLConstants.QUALITY_INNOVATIVE
import org.studioapp.cinemy.ml.MLConstants.QUALITY_GROUNDBREAKING
import org.studioapp.cinemy.ml.MLConstants.QUALITY_REVOLUTIONARY
import org.studioapp.cinemy.ml.MLConstants.QUALITY_TIMELESS
import org.studioapp.cinemy.ml.MLConstants.QUALITY_CLASSIC
import org.studioapp.cinemy.ml.MLConstants.FAILURE_FLOP
import org.studioapp.cinemy.ml.MLConstants.FAILURE_DISASTER
import org.studioapp.cinemy.ml.MLConstants.FAILURE_FAILURE
import org.studioapp.cinemy.ml.MLConstants.FAILURE_RUINED
import org.studioapp.cinemy.ml.MLConstants.FAILURE_DESTROYED
import org.studioapp.cinemy.ml.MLConstants.FAILURE_BUTCHERED
import org.studioapp.cinemy.ml.MLConstants.FAILURE_MANGLED
import org.studioapp.cinemy.ml.MLConstants.FAILURE_TORTURE
import org.studioapp.cinemy.ml.MLConstants.FAILURE_NIGHTMARE

/**
 * Factory for creating ContextBoosters for sentiment analysis
 * Provides centralized context term management for enhanced sentiment analysis
 */
object ContextBoostersFactory {

    /**
     * Creates ContextBoosters with movie-specific terms and quality indicators
     * 
     * @return ContextBoosters instance with movie terms, positive context, and negative context
     */
    fun createMovieContextBoosters(): ContextBoosters {
        return ContextBoosters(
            movieTerms = createMovieTerms(),
            positiveContext = createPositiveContext(),
            negativeContext = createNegativeContext()
        )
    }

    /**
     * Creates movie-specific terms for context boosting
     */
    private fun createMovieTerms(): List<String> {
        return listOf(
            CONTEXT_CINEMATOGRAPHY, CONTEXT_ACTING, CONTEXT_PLOT, CONTEXT_STORY, CONTEXT_DIRECTOR, CONTEXT_PERFORMANCE,
            CONTEXT_SCRIPT, CONTEXT_DIALOGUE, CONTEXT_VISUALS, CONTEXT_EFFECTS, CONTEXT_SOUNDTRACK, CONTEXT_EDITING
        )
    }

    /**
     * Creates positive quality context terms
     */
    private fun createPositiveContext(): List<String> {
        return listOf(
            QUALITY_MASTERPIECE, QUALITY_ARTISTRY, QUALITY_BRILLIANT, QUALITY_GENIUS, QUALITY_INNOVATIVE,
            QUALITY_GROUNDBREAKING, QUALITY_REVOLUTIONARY, QUALITY_TIMELESS, QUALITY_CLASSIC
        )
    }

    /**
     * Creates negative failure context terms
     */
    private fun createNegativeContext(): List<String> {
        return listOf(
            FAILURE_FLOP, FAILURE_DISASTER, FAILURE_FAILURE, FAILURE_RUINED, FAILURE_DESTROYED, FAILURE_BUTCHERED,
            FAILURE_MANGLED, FAILURE_TORTURE, FAILURE_NIGHTMARE
        )
    }

    /**
     * Creates ContextBoosters from enhanced model data
     * Used when loading enhanced model format with custom context patterns
     *
     * @param modelData Enhanced model data containing context patterns
     * @return ContextBoosters configured from the enhanced model data
     */
    fun createFromEnhancedModel(modelData: EnhancedModelData): ContextBoosters {
        return ContextBoosters(
            movieTerms = null, // Not in v2 model
            positiveContext = modelData.context_patterns?.strong_positive,
            negativeContext = modelData.context_patterns?.strong_negative
        )
    }
}
