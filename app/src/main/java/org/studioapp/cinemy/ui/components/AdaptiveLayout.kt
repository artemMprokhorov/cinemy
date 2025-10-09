package org.studioapp.cinemy.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
    modifier: Modifier = Modifier
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
            modifier = modifier
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
 * Single pane layout for phones
 * Shows only one pane at a time
 */
@Composable
private fun SinglePaneLayout(
    leftPane: @Composable () -> Unit,
    rightPane: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    // For single pane, we show the right pane (movie details) by default
    // The left pane (movies list) is handled by navigation
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        rightPane()
    }
}

/**
 * Adaptive grid layout for movie cards
 * Adjusts column count based on device type
 */
@Composable
fun AdaptiveGridLayout(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val deviceType = getDeviceType()
    val spacing = getOptimalSpacing()

    when (deviceType) {
        DeviceUtils.DeviceType.FOLDABLE -> {
            // Foldable devices get optimized spacing
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(spacing)
            ) {
                content()
            }
        }

        DeviceUtils.DeviceType.TABLET, DeviceUtils.DeviceType.DESKTOP -> {
            // Tablets and desktop get standard spacing
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = spacing, vertical = spacing / 2)
            ) {
                content()
            }
        }

        DeviceUtils.DeviceType.PHONE -> {
            // Phones get minimal spacing
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                content()
            }
        }
    }
}

/**
 * Adaptive spacing modifier
 * Provides appropriate spacing based on device type
 */
@Composable
fun Modifier.adaptivePadding(): Modifier {
    val spacing = getOptimalSpacing()
    return this.padding(spacing)
}

/**
 * Adaptive content width modifier
 * Adjusts content width based on device type
 */
@Composable
fun Modifier.adaptiveContentWidth(): Modifier {
    val deviceType = getDeviceType()

    return when (deviceType) {
        DeviceUtils.DeviceType.FOLDABLE -> {
            // Foldable devices use full width
            this.fillMaxWidth()
        }

        DeviceUtils.DeviceType.TABLET, DeviceUtils.DeviceType.DESKTOP -> {
            // Tablets and desktop use constrained width
            this.width(600.dp)
        }

        DeviceUtils.DeviceType.PHONE -> {
            // Phones use full width
            this.fillMaxWidth()
        }
    }
}
