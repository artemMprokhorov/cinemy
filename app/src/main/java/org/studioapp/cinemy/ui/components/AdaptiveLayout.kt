package org.studioapp.cinemy.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.studioapp.cinemy.utils.DeviceUtils
import org.studioapp.cinemy.utils.getDeviceType
import org.studioapp.cinemy.utils.getOptimalSpacing
import org.studioapp.cinemy.utils.supportsDualPane

/**
 * Adaptive layout component that automatically adjusts based on device type and capabilities.
 * Supports foldable devices with dual pane layout, tablets with standard dual pane, and phones with single pane.
 * 
 * @param leftPane Content composable for the left pane (typically movies list)
 * @param rightPane Content composable for the right pane (typically movie details)
 * @param modifier Modifier to be applied to the layout container
 * @param showRightPane Whether to show the right pane in single pane mode (ignored in dual pane modes)
 */
@Composable
fun AdaptiveLayout(
    leftPane: @Composable () -> Unit,
    rightPane: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    showRightPane: Boolean = false
) {
    val deviceType = getDeviceType()
    val supportsDual = supportsDualPane()
    val spacing = getOptimalSpacing()

    if (supportsDual && deviceType == DeviceUtils.DeviceType.FOLDABLE) {
        // Dual pane layout for unfolded foldable devices
        FoldableDualPaneLayout(
            leftPane = leftPane,
            rightPane = rightPane,
            modifier = modifier,
            spacing = spacing
        )
    } else if (supportsDual) {
        // Standard dual pane layout for tablets/desktop
        StandardDualPaneLayout(
            leftPane = leftPane,
            rightPane = rightPane,
            modifier = modifier,
            spacing = spacing
        )
    } else {
        // Single pane layout for phones and folded foldable devices
        SinglePaneLayout(
            leftPane = leftPane,
            rightPane = rightPane,
            modifier = modifier,
            showRightPane = showRightPane
        )
    }
}

/**
 * Foldable device specific dual pane layout with flexible sizing.
 * Optimized for unfolded foldable devices with 40/60 weight distribution.
 * 
 * @param leftPane Content composable for the left pane
 * @param rightPane Content composable for the right pane
 * @param modifier Modifier to be applied to the layout container
 * @param spacing Spacing between panes in dp
 */
@Composable
private fun FoldableDualPaneLayout(
    leftPane: @Composable () -> Unit,
    rightPane: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    spacing: androidx.compose.ui.unit.Dp
) {
    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        // Left pane - Movies list (flexible width)
        Box(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxSize()
        ) {
            leftPane()
        }

        // Divider for visual separation
        HorizontalDivider(
            modifier = Modifier
                .width(1.dp)
                .fillMaxSize(),
            color = colorScheme.outline.copy(alpha = 0.3f)
        )

        // Right pane - Movie details (flexible width)
        Box(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxSize()
        ) {
            rightPane()
        }
    }
}

/**
 * Standard dual pane layout for tablets and desktop devices.
 * Uses fixed width (320dp) for left pane and flexible width for right pane.
 * 
 * @param leftPane Content composable for the left pane
 * @param rightPane Content composable for the right pane
 * @param modifier Modifier to be applied to the layout container
 * @param spacing Spacing between panes in dp
 */
@Composable
private fun StandardDualPaneLayout(
    leftPane: @Composable () -> Unit,
    rightPane: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    spacing: androidx.compose.ui.unit.Dp
) {
    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        // Left pane - Movies list (fixed width)
        Box(
            modifier = Modifier
                .width(320.dp)
                .fillMaxSize()
        ) {
            leftPane()
        }

        // Divider for visual separation
        HorizontalDivider(
            modifier = Modifier
                .width(1.dp)
                .fillMaxSize(),
            color = colorScheme.outline.copy(alpha = 0.3f)
        )

        // Right pane - Movie details (flexible width)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            rightPane()
        }
    }
}

/**
 * Single pane layout for phones and folded foldable devices.
 * Shows either left or right pane based on the showRightPane parameter.
 * 
 * @param leftPane Content composable for the left pane
 * @param rightPane Content composable for the right pane
 * @param modifier Modifier to be applied to the layout container
 * @param showRightPane Whether to show the right pane (true) or left pane (false)
 */
@Composable
private fun SinglePaneLayout(
    leftPane: @Composable () -> Unit,
    rightPane: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    showRightPane: Boolean = false
) {
    // For single pane, show the appropriate pane based on showRightPane parameter
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (showRightPane) {
            rightPane()
        } else {
            leftPane()
        }
    }
}
