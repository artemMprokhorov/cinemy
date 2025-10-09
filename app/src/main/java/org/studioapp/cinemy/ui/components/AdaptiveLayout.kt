package org.studioapp.cinemy.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.studioapp.cinemy.utils.DeviceUtils
import org.studioapp.cinemy.utils.getDeviceType
import org.studioapp.cinemy.utils.getOptimalSpacing
import org.studioapp.cinemy.utils.supportsDualPane

/**
 * Adaptive layout component that adjusts based on device type
 * Supports foldable devices with dual pane layout
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
 * Foldable device specific dual pane layout
 * Optimized for foldable devices with flexible sizing
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
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
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
 * Standard dual pane layout for tablets and desktop
 * Fixed width for left pane, flexible for right pane
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
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
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
 * Single pane layout for phones and folded foldable devices
 * Shows the appropriate pane based on navigation state
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

// Unused methods removed: AdaptiveGridLayout, adaptivePadding, adaptiveContentWidth
