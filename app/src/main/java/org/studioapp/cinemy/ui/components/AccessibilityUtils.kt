package org.studioapp.cinemy.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/**
 * Accessibility utilities for UI components
 * Provides semantic descriptions and roles for screen readers
 */
object AccessibilityUtils {

    /**
     * Creates semantic description for movie cards
     */
    fun createMovieCardDescription(
        title: String,
        rating: Double,
        releaseDate: String,
        isClickable: Boolean = true
    ): String {
        return buildString {
            append("Movie: $title")
            append(", Rating: ${String.format("%.1f", rating)} out of 10")
            append(", Released: $releaseDate")
            if (isClickable) {
                append(", Double tap to view details")
            }
        }
    }

    /**
     * Creates semantic description for movie details
     */
    fun createMovieDetailsDescription(
        title: String,
        rating: Double,
        releaseDate: String,
        runtime: Int,
        genres: List<String>,
        description: String
    ): String {
        return buildString {
            append("Movie Details: $title")
            append(", Rating: ${String.format("%.1f", rating)} out of 10")
            append(", Released: $releaseDate")
            if (runtime > 0) {
                append(", Runtime: ${runtime} minutes")
            }
            if (genres.isNotEmpty()) {
                append(", Genres: ${genres.joinToString(", ")}")
            }
            if (description.isNotEmpty()) {
                append(", Description: $description")
            }
        }
    }

    /**
     * Creates semantic description for buttons
     */
    fun createButtonDescription(
        action: String,
        target: String? = null
    ): String {
        return if (target != null) {
            "$action $target"
        } else {
            action
        }
    }

    /**
     * Creates semantic description for pagination
     */
    fun createPaginationDescription(
        currentPage: Int,
        totalPages: Int,
        hasNext: Boolean,
        hasPrevious: Boolean
    ): String {
        return buildString {
            append("Page $currentPage of $totalPages")
            when {
                hasNext && hasPrevious -> append(", Swipe left or right to navigate")
                hasNext -> append(", Swipe left for next page")
                hasPrevious -> append(", Swipe right for previous page")
                else -> append(", No more pages available")
            }
        }
    }

    /**
     * Creates semantic description for loading states
     */
    fun createLoadingDescription(content: String): String {
        return "Loading $content, please wait"
    }

    /**
     * Creates semantic description for error states
     */
    fun createErrorDescription(error: String, retryAction: String = "retry"): String {
        return "Error: $error, $retryAction to try again"
    }

    /**
     * Creates semantic description for sentiment analysis
     */
    fun createSentimentDescription(
        positiveCount: Int,
        negativeCount: Int,
        totalCount: Int
    ): String {
        return buildString {
            append("Sentiment Analysis: ")
            append("$positiveCount positive reviews, ")
            append("$negativeCount negative reviews, ")
            append("out of $totalCount total reviews")
        }
    }

}

/**
 * Modifier for adding accessibility semantics to clickable elements
 */
@Composable
fun Modifier.accessibleClickable(
    description: String,
    role: Role = Role.Button,
    onClick: () -> Unit
): Modifier {
    return this
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
        .semantics {
            this.role = role
            this.contentDescription = description
        }
}

/**
 * Modifier for adding accessibility semantics to cards
 */
@Composable
fun Modifier.accessibleCard(
    description: String,
    isClickable: Boolean = false,
    onClick: (() -> Unit)? = null
): Modifier {
    val modifier = if (isClickable && onClick != null) {
        this.accessibleClickable(description, Role.Button, onClick)
    } else {
        this.semantics {
            contentDescription = description
        }
    }

    return modifier
}
