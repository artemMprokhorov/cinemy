package com.example.tmdbai.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tmdbai.data.model.UiConfiguration
import com.example.tmdbai.ui.theme.Float12
import com.example.tmdbai.ui.theme.Float38
import com.example.tmdbai.ui.theme.Dimens16
import com.example.tmdbai.ui.theme.Dimens8

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
    // Extract button configuration from UiConfig
    val buttonConfig = uiConfig?.buttons

    // Determine button colors with fallback to Material3 defaults
    val buttonColor = when {
        isSecondary -> buttonConfig?.secondaryButtonColor ?: MaterialTheme.colorScheme.secondary
        else -> buttonConfig?.primaryButtonColor ?: MaterialTheme.colorScheme.primary
    }

    val textColor = buttonConfig?.buttonTextColor ?: MaterialTheme.colorScheme.onPrimary
    val cornerRadius = buttonConfig?.buttonCornerRadius?.dp ?: Dimens8

    Button(
        onClick = onClick,
        modifier = modifier,
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
