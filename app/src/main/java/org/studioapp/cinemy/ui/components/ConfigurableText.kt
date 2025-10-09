package org.studioapp.cinemy.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import org.studioapp.cinemy.data.model.UiConfiguration

/**
 * Configurable text component that supports server-driven styling
 *
 * This component accepts UiConfig parameters for dynamic text theming while
 * falling back to Material3 defaults when no configuration is provided.
 *
 * @param text The text content to display
 * @param style The text style to apply (Material3 typography)
 * @param uiConfig Optional UI configuration for dynamic styling
 * @param modifier Modifier to apply to the text
 * @param color Override color for the text (takes precedence over UiConfig)
 * @param maxLines Maximum number of lines for the text
 * @param contentDescription Optional accessibility description for screen readers
 * @param testTag Optional test tag for QA automation
 * @param testId Optional test ID for QA automation
 * @param testData Optional test data attributes for QA automation
 */
@Composable
fun ConfigurableText(
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    uiConfig: UiConfiguration? = null,
    modifier: Modifier = Modifier,
    color: Color? = null,
    maxLines: Int = Int.MAX_VALUE,
    contentDescription: String? = null,
    testTag: String? = null,
    testId: String? = null,
    testData: Map<String, String> = emptyMap()
) {
    // Determine text color with priority: explicit color > UiConfig > Material3 default
    val textColor = when {
        color != null -> color
        uiConfig?.colors != null -> {
            // Use appropriate color from UiConfig based on context
            // For cards, use onSurface; for general text, use onBackground
            uiConfig.colors.onSurface
        }

        else -> MaterialTheme.colorScheme.onBackground
    }

    // Debug logging for color application (removed to prevent infinite loop)

    Text(
        text = text,
        style = style,
        modifier = modifier
            .let { baseModifier ->
                if (contentDescription != null) {
                    baseModifier.semantics {
                        this.contentDescription = contentDescription
                    }
                } else {
                    baseModifier
                }
            }
            .then(
                TestUtils.TestModifiers.testAttributes(
                    tag = testTag,
                    id = testId,
                    data = testData
                )
            ),
        color = textColor,
        maxLines = maxLines
    )
}
