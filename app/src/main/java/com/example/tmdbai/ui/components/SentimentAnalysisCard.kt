package com.example.tmdbai.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.tmdbai.data.model.SentimentReviews
import com.example.tmdbai.ml.SentimentResult
import com.example.tmdbai.ml.SentimentType
import com.example.tmdbai.ui.theme.*

@Composable
fun SentimentAnalysisCard(
    sentimentResult: SentimentResult?,
    sentimentReviews: SentimentReviews? = null,
    isLoading: Boolean = false,
    error: String? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(Dimens16)
    ) {
        Column(
            modifier = Modifier.padding(Dimens16),
            verticalArrangement = Arrangement.spacedBy(Dimens12)
        ) {
            Text(
                text = "🤖 Sentiment Analysis Reviews",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = "Ready reviews • AI-analysis",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = Float07)
            )
            
            // Error display
            error?.let { errorText ->
                Text(
                    text = errorText,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            // Show real reviews if available
            sentimentReviews?.let { reviews ->
                if (reviews.hasAnyReviews) {
                    SentimentReviewsContent(reviews = reviews)
                } else {
                    Text(
                        text = "Reviews not loaded yet...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = Float07)
                    )
                }
            } ?: run {
                // Show message if no reviews
                if (!isLoading && error == null) {
                    Text(
                        text = "Reviews will be loaded from backend...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = Float07)
                    )
                }
            }
            
            // DO NOT show technical SentimentResult information
            // sentimentResult?.let { result ->
            //     SentimentResultCard(result = result)
            // }
        }
    }
}

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
                text = "😊 Positive Reviews (${reviews.positive.size})",
                style = MaterialTheme.typography.titleSmall,
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
                text = "😞 Negative Reviews (${reviews.negative.size})",
                style = MaterialTheme.typography.titleSmall,
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
            style = MaterialTheme.typography.bodySmall,
            color = textColor,
            modifier = Modifier.padding(Dimens12)
        )
    }
}

@Composable
private fun SentimentResultCard(
    result: SentimentResult,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, icon) = when (result.sentiment) {
        SentimentType.POSITIVE -> Triple(
            SentimentPositive.copy(alpha = Float01),
            SentimentPositive,
            "😊"
        )
        SentimentType.NEGATIVE -> Triple(
            SentimentNegative.copy(alpha = Float01),
            SentimentNegative,
            "😞"
        )
        else -> Triple(
            SentimentNeutral.copy(alpha = Float01),
            SentimentNeutral,
            "😐"
        )
    }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(Dimens8)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens12),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = icon,
                    fontSize = Typography24
                )
                Spacer(Modifier.width(Dimens12))
                Column {
                    Text(
                        text = result.getDescription(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor,
                        fontWeight = FontWeight.Medium
                    )
                    if (result.foundKeywords.isNotEmpty()) {
                        Text(
                            text = "Keywords: ${result.foundKeywords.take(3).joinToString(", ")}",
                            style = MaterialTheme.typography.bodySmall,
                            color = textColor.copy(alpha = Float07)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SentimentAnalysisCardPreview() {
    MaterialTheme {
        SentimentAnalysisCard(
            sentimentResult = null,
            isLoading = false
        )
    }
}
