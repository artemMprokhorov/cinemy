package org.studioapp.cinemy.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import org.studioapp.cinemy.BuildConfig
import org.studioapp.cinemy.R
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
 */
@Composable
fun ConfigurableText(
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    uiConfig: UiConfiguration? = null,
    modifier: Modifier = Modifier,
    color: Color? = null,
    maxLines: Int = Int.MAX_VALUE
) {
    // Determine text color with priority: explicit color > UiConfig > Material3 default
    val textColor = color ?: when {
        uiConfig?.colors != null -> {
            // Use appropriate color from UiConfig based on context
            // For cards, use onSurface; for general text, use onBackground
            uiConfig.colors.onSurface
        }

        else -> MaterialTheme.colorScheme.onBackground
    }

    // Debug logging for color application (removed to prevent infinite loop)
    // if (BuildConfig.DEBUG) {
    //     Log.d("ConfigurableText", "Text color: $textColor, Using AI colors: ${uiConfig?.colors != null}, Text: '$text'")
    //     Log.d("ConfigurableText", "FORCED COLOR - Text: $textColor, Using onSurface: ${uiConfig?.colors?.onSurface}")
    // }

    Text(
        text = text,
        style = style,
        modifier = modifier,
        color = textColor,
        maxLines = maxLines
    )
}

/**
 * Configurable text component for specific text types defined in UiConfig
 *
 * This component maps to specific text configurations from the server
 * such as app title, loading text, error messages, etc.
 *
 * @param textType The type of text to display (maps to UiConfig text configuration)
 * @param uiConfig UI configuration containing text definitions
 * @param modifier Modifier to apply to the text
 * @param style The text style to apply (Material3 typography)
 * @param color Override color for the text
 * @param maxLines Maximum number of lines for the text
 */
@Composable
fun ConfigurableTextByType(
    textType: TextType,
    uiConfig: UiConfiguration?,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color? = null,
    maxLines: Int = Int.MAX_VALUE
) {
    val textContent = when (textType) {
        TextType.APP_TITLE -> uiConfig?.texts?.appTitle
        TextType.LOADING_TEXT -> uiConfig?.texts?.loadingText
        TextType.ERROR_MESSAGE -> uiConfig?.texts?.errorMessage
        TextType.NO_MOVIES_FOUND -> uiConfig?.texts?.noMoviesFound
        TextType.RETRY_BUTTON -> uiConfig?.texts?.retryButton
        TextType.BACK_BUTTON -> uiConfig?.texts?.backButton
        TextType.PLAY_BUTTON -> uiConfig?.texts?.playButton
    } ?: stringResource(textType.defaultTextRes)

    ConfigurableText(
        text = textContent,
        style = style,
        uiConfig = uiConfig,
        modifier = modifier,
        color = color,
        maxLines = maxLines
    )
}

/**
 * Enum defining the types of text that can be configured via UiConfig
 */
enum class TextType(val defaultTextRes: Int) {
    APP_TITLE(R.string.app_title),
    LOADING_TEXT(R.string.loading_text),
    ERROR_MESSAGE(R.string.something_went_wrong),
    NO_MOVIES_FOUND(R.string.no_movies_found),
    RETRY_BUTTON(R.string.retry_button),
    BACK_BUTTON(R.string.back_button),
    PLAY_BUTTON(R.string.play_button)
}
