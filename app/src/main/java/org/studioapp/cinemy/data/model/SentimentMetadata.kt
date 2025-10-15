package org.studioapp.cinemy.data.model

/**
 * Sentiment analysis metadata associated with a movie.
 *
 * The values are provided by the backend (N8N workflow) and represent high-level
 * counters and provenance for the sentiment analysis. All counters are non-negative
 * integers and default to `0` when the backend omits values.
 *
 * - This model is optional in `MovieDetailsData`; callers should handle `null` safely.
 * - When using MCP mock assets, defaults may be returned.
 *
 * @property totalReviews Total number of reviews considered by the analysis.
 * @property positiveCount Number of reviews classified as positive.
 * @property negativeCount Number of reviews classified as negative.
 * @property source Free-form origin of the sentiment data (e.g., provider or pipeline name).
 * @property timestamp Backend-provided timestamp string indicating when analysis was produced.
 * @property apiSuccess Map of provider/system identifiers to success flags for the analysis run.
 */
data class SentimentMetadata(
    val totalReviews: Int = 0,
    val positiveCount: Int = 0,
    val negativeCount: Int = 0,
    val source: String = "",
    val timestamp: String = "",
    val apiSuccess: Map<String, Boolean> = emptyMap()
)
