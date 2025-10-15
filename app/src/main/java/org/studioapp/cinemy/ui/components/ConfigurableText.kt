package org.studioapp.cinemy.ui.components

import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import org.studioapp.cinemy.data.model.UiConfiguration

/**
 * Configurable text component that supports server-driven styling with dynamic theming
 *
 * This component provides server-driven text theming through UiConfiguration while
 * maintaining Material3 design system compatibility. It implements a color priority
 * system: explicit color parameter > UiConfig colors > Material3 default colors.
 * 
 * The component includes comprehensive accessibility support with content descriptions
 * and QA testing attributes for automated testing frameworks.
 *
 * @param text The text content to display
 * @param style The text style to apply (defaults to Material3 bodyMedium typography)
 * @param uiConfig Optional UI configuration for server-driven dynamic styling
 * @param modifier Modifier to apply to the text component
 * @param color Override color for the text (takes highest priority over UiConfig)
 * @param maxLines Maximum number of lines for text wrapping (defaults to unlimited)
 * @param contentDescription Optional accessibility description for screen readers
 * @param testTag Optional test tag for QA automation and testing frameworks
 * @param testId Optional test ID for QA automation and testing frameworks
 * @param testData Optional test data attributes for QA automation and testing frameworks
 */
@Composable
fun ConfigurableText(
    text: String,
    style: TextStyle = typography.bodyMedium,
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

        else -> colorScheme.onBackground
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
