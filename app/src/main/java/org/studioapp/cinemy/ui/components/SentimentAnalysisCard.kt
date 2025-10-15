package org.studioapp.cinemy.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import org.studioapp.cinemy.R
import org.studioapp.cinemy.data.model.SentimentReviews
import org.studioapp.cinemy.ui.constants.UiConstants.SENTIMENT_ANALYSIS_CARD
import org.studioapp.cinemy.ui.constants.UiConstants.SENTIMENT_TITLE
import org.studioapp.cinemy.ui.theme.Dimens12
import org.studioapp.cinemy.ui.theme.Dimens16
import org.studioapp.cinemy.ui.theme.Dimens200
import org.studioapp.cinemy.ui.theme.Dimens8
import org.studioapp.cinemy.ui.theme.Float01
import org.studioapp.cinemy.ui.theme.Float07
import org.studioapp.cinemy.ui.theme.SentimentNegative
import org.studioapp.cinemy.ui.theme.SentimentPositive

/**
 * Card component for displaying sentiment analysis results with positive and negative review categorization.
 * Shows loading states, error messages, and organized review content with visual indicators.
 *
 * @param sentimentReviews Sentiment reviews data to display, can be null if not loaded
 * @param error Error message to show if sentiment analysis failed
 * @param modifier Modifier for the composable
 */
@Composable
fun SentimentAnalysisCard(
    sentimentReviews: SentimentReviews? = null,
    error: String? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag(SENTIMENT_ANALYSIS_CARD),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface
        ),
        shape = RoundedCornerShape(Dimens16)
    ) {
        Column(
            modifier = Modifier.padding(Dimens16),
            verticalArrangement = Arrangement.spacedBy(Dimens12)
        ) {
            Text(
                text = stringResource(R.string.sentiment_analysis_title),
                style = typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurface,
                modifier = Modifier.testTag(SENTIMENT_TITLE)
            )

            Text(
                text = stringResource(R.string.sentiment_analysis_subtitle),
                style = typography.bodySmall,
                color = colorScheme.onSurface.copy(alpha = Float07)
            )

            // Error display
            error?.let { errorText ->
                Text(
                    text = errorText,
                    color = colorScheme.error,
                    style = typography.bodySmall
                )
            }

            // Show real reviews if available
            sentimentReviews?.let { reviews ->
                if (reviews.hasAnyReviews) {
                    SentimentReviewsContent(reviews = reviews)
                } else {
                    Text(
                        text = stringResource(R.string.sentiment_reviews_not_loaded),
                        style = typography.bodyMedium,
                        color = colorScheme.onSurface.copy(alpha = Float07)
                    )
                }
            } ?: run {
                // Show message if no reviews
                if (error == null) {
                    Text(
                        text = stringResource(R.string.sentiment_reviews_loading),
                        style = typography.bodyMedium,
                        color = colorScheme.onSurface.copy(alpha = Float07)
                    )
                }
            }
        }
    }
}

/**
 * Internal composable for displaying sentiment reviews content with positive and negative sections.
 * Shows review counts and scrollable lists of individual reviews with color-coded styling.
 *
 * @param reviews Sentiment reviews data containing positive and negative review lists
 * @param modifier Modifier for the composable
 */
@Composable
private fun SentimentReviewsContent(
    reviews: SentimentReviews,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens12)
    ) {
        // Positive reviews
        if (reviews.hasPositiveReviews) {
            Text(
                text = stringResource(R.string.sentiment_positive_reviews, reviews.positive.size),
                style = typography.titleSmall,
                fontWeight = FontWeight.Medium,
                color = SentimentPositive
            )

            LazyColumn(
                modifier = Modifier.heightIn(max = Dimens200),
                verticalArrangement = Arrangement.spacedBy(Dimens8)
            ) {
                items(reviews.positive) { review ->
                    ReviewItem(
                        text = review,
                        backgroundColor = SentimentPositive.copy(alpha = Float01),
                        textColor = SentimentPositive
                    )
                }
            }
        }

        // Negative reviews
        if (reviews.hasNegativeReviews) {
            Text(
                text = stringResource(R.string.sentiment_negative_reviews, reviews.negative.size),
                style = typography.titleSmall,
                fontWeight = FontWeight.Medium,
                color = SentimentNegative
            )

            LazyColumn(
                modifier = Modifier.heightIn(max = Dimens200),
                verticalArrangement = Arrangement.spacedBy(Dimens8)
            ) {
                items(reviews.negative) { review ->
                    ReviewItem(
                        text = review,
                        backgroundColor = SentimentNegative.copy(alpha = Float01),
                        textColor = SentimentNegative
                    )
                }
            }
        }
    }
}

/**
 * Internal composable for displaying individual review items with custom styling.
 * Creates a card with background color and text color for sentiment visualization.
 *
 * @param text Review text content to display
 * @param backgroundColor Background color for the review card
 * @param textColor Text color for the review content
 * @param modifier Modifier for the composable
 */
@Composable
private fun ReviewItem(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(Dimens8)
    ) {
        Text(
            text = text,
            style = typography.bodySmall,
            color = textColor,
            modifier = Modifier.padding(Dimens12)
        )
    }
}


/**
 * Preview composable for SentimentAnalysisCard component.
 * Shows the card in its default state for design system documentation.
 */
@Preview
@Composable
private fun SentimentAnalysisCardPreview() {
    MaterialTheme {
        SentimentAnalysisCard()
    }
}
