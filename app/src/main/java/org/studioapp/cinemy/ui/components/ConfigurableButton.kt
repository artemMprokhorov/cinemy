package org.studioapp.cinemy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.studioapp.cinemy.BuildConfig
import org.studioapp.cinemy.data.model.UiConfiguration
import org.studioapp.cinemy.ui.theme.Dimens16
import org.studioapp.cinemy.ui.theme.Dimens8
import org.studioapp.cinemy.ui.theme.PrimaryBlue
import org.studioapp.cinemy.ui.theme.SecondaryGreen
import org.studioapp.cinemy.ui.theme.Float12
import org.studioapp.cinemy.ui.theme.Float38

/**
 * Configurable button component that supports server-driven styling
 *
 * This component accepts UiConfig parameters for dynamic theming while
 * falling back to Material3 defaults when no configuration is provided.
 *
 * @param text The button text to display
 * @param onClick The action to perform when the button is clicked
 * @param uiConfig Optional UI configuration for dynamic styling
 * @param modifier Modifier to apply to the button
 * @param isSecondary Whether this is a secondary button (affects color scheme)
 * @param enabled Whether the button is enabled
 * @param contentPadding Padding for the button content
 */
@Composable
fun ConfigurableButton(
    text: String,
    onClick: () -> Unit,
    uiConfig: UiConfiguration? = null,
    modifier: Modifier = Modifier,
    isSecondary: Boolean = false,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = Dimens16, vertical = Dimens8)
) {
    // Extract color configuration from UiConfig
    val colorScheme = uiConfig?.colors

    // Determine button colors with PRIORITY to uiConfig colors
    val buttonColor = when {
        uiConfig?.colors != null -> {
            if (isSecondary) colorScheme?.secondary ?: SecondaryGreen // Force green
            else colorScheme?.primary ?: PrimaryBlue // Force blue
        }
        else -> {
            if (isSecondary) MaterialTheme.colorScheme.secondary
            else MaterialTheme.colorScheme.primary
        }
    }

    val textColor = if (uiConfig?.colors != null) {
        colorScheme?.onPrimary ?: Color.White // Force white text
    } else {
        MaterialTheme.colorScheme.onPrimary
    }
    val cornerRadius = uiConfig?.buttons?.buttonCornerRadius?.dp ?: Dimens8

    // Debug logging for color application (removed to prevent infinite loop)
    // if (BuildConfig.DEBUG) {
    //     Log.d("ConfigurableButton", "Button colors - Primary: ${colorScheme?.primary}, Secondary: ${colorScheme?.secondary}, Text: $textColor, Using AI colors: ${uiConfig?.colors != null}")
    //     Log.d("ConfigurableButton", "FORCED COLORS - Button: $buttonColor, Text: $textColor, IsSecondary: $isSecondary")
    // }

    Button(
        onClick = onClick,
        modifier = modifier.background(buttonColor), // FORCE background color with absolute priority
        enabled = enabled,
        contentPadding = contentPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = textColor,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = Float12),
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = Float38)
        ),
        shape = RoundedCornerShape(cornerRadius)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
