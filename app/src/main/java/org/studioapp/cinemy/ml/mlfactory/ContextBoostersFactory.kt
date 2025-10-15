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
 * Factory for creating ContextBoosters for sentiment analysis.
 * 
 * This factory provides centralized context term management for enhanced sentiment analysis,
 * offering both standard movie-specific context boosters and dynamic context creation
 * from enhanced model data. It serves as the primary source for context terms used
 * across different sentiment analysis models in the ML layer.
 * 
 * The factory supports two main use cases:
 * - Standard movie context boosters with predefined terms for movie sentiment analysis
 * - Dynamic context creation from enhanced model JSON data with custom patterns
 * 
 * Context boosters enhance sentiment analysis by providing movie-specific terminology
 * and quality indicators that help identify positive and negative sentiment patterns
 * in movie reviews and discussions.
 */
object ContextBoostersFactory {

    /**
     * Creates ContextBoosters with movie-specific terms and quality indicators.
     * 
     * This method creates a comprehensive set of context boosters specifically designed
     * for movie sentiment analysis. It includes movie-specific terminology (cinematography,
     * acting, plot, etc.), positive quality indicators (masterpiece, brilliant, etc.),
     * and negative failure indicators (flop, disaster, etc.).
     * 
     * The returned ContextBoosters contains:
     * - Movie terms: 12 movie-specific context terms for enhanced analysis
     * - Positive context: 9 quality indicators for positive sentiment boosting
     * - Negative context: 9 failure indicators for negative sentiment boosting
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
     * Creates movie-specific terms for context boosting.
     * 
     * Returns a list of 12 movie-specific context terms that are commonly used
     * in movie reviews and discussions. These terms help the sentiment analyzer
     * identify movie-related content and apply appropriate context boosting.
     * 
     * @return List of 12 movie-specific context terms
     */
    private fun createMovieTerms(): List<String> {
        return listOf(
            CONTEXT_CINEMATOGRAPHY, CONTEXT_ACTING, CONTEXT_PLOT, CONTEXT_STORY, CONTEXT_DIRECTOR, CONTEXT_PERFORMANCE,
            CONTEXT_SCRIPT, CONTEXT_DIALOGUE, CONTEXT_VISUALS, CONTEXT_EFFECTS, CONTEXT_SOUNDTRACK, CONTEXT_EDITING
        )
    }

    /**
     * Creates positive quality context terms.
     * 
     * Returns a list of 9 positive quality indicators that are commonly used
     * to describe high-quality movies and positive aspects of films. These terms
     * help boost positive sentiment when found in movie reviews.
     * 
     * @return List of 9 positive quality context terms
     */
    private fun createPositiveContext(): List<String> {
        return listOf(
            QUALITY_MASTERPIECE, QUALITY_ARTISTRY, QUALITY_BRILLIANT, QUALITY_GENIUS, QUALITY_INNOVATIVE,
            QUALITY_GROUNDBREAKING, QUALITY_REVOLUTIONARY, QUALITY_TIMELESS, QUALITY_CLASSIC
        )
    }

    /**
     * Creates negative failure context terms.
     * 
     * Returns a list of 9 negative failure indicators that are commonly used
     * to describe poor-quality movies and negative aspects of films. These terms
     * help boost negative sentiment when found in movie reviews.
     * 
     * @return List of 9 negative failure context terms
     */
    private fun createNegativeContext(): List<String> {
        return listOf(
            FAILURE_FLOP, FAILURE_DISASTER, FAILURE_FAILURE, FAILURE_RUINED, FAILURE_DESTROYED, FAILURE_BUTCHERED,
            FAILURE_MANGLED, FAILURE_TORTURE, FAILURE_NIGHTMARE
        )
    }

    /**
     * Creates ContextBoosters from enhanced model data.
     * 
     * This method is used when loading enhanced model format with custom context patterns.
     * It extracts context patterns from enhanced model data and creates a ContextBoosters
     * instance with the custom patterns. This allows for dynamic context configuration
     * based on model-specific parameters.
     * 
     * The method maps enhanced model context patterns to the standard ContextBoosters format:
     * - movieTerms: Set to null (not available in v2 enhanced model format)
     * - positiveContext: Extracted from modelData.context_patterns.strong_positive
     * - negativeContext: Extracted from modelData.context_patterns.strong_negative
     * 
     * @param modelData Enhanced model data containing context patterns
     * @return ContextBoosters configured from the enhanced model data
     * @throws IllegalArgumentException if modelData is null
     */
    fun createFromEnhancedModel(modelData: EnhancedModelData): ContextBoosters {
        return ContextBoosters(
            movieTerms = null, // Not in v2 model
            positiveContext = modelData.context_patterns?.strong_positive,
            negativeContext = modelData.context_patterns?.strong_negative
        )
    }
}
