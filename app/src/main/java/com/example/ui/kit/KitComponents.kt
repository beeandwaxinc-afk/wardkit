package com.example.ui.kit

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.LocalMedWheelExtras
import com.example.ui.theme.PlusJakartaFontFamily

/**
 * Horizontally scrollable connected Material 3 pill chip selector for ages, panels, and sub-tabs.
 */
@Composable
fun KitScrollableChipRow(
    items: List<Pair<String, String>>,
    selectedId: String,
    onSelect: (String) -> Unit,
    tagPrefix: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp)
) {
    val haptic = LocalHapticFeedback.current
    val listState = rememberLazyListState()
    val selectedIndex = items.indexOfFirst { it.first == selectedId }.coerceAtLeast(0)

    LaunchedEffect(selectedIndex) {
        if (items.isNotEmpty()) {
            listState.animateScrollToItem((selectedIndex - 1).coerceAtLeast(0))
        }
    }

    LazyRow(
        state = listState,
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        itemsIndexed(items, key = { _, pair -> pair.first }) { index, (id, label) ->
            val isSelected = id == selectedId
            val shape = when {
                items.size == 1 -> RoundedCornerShape(20.dp)
                index == 0 -> RoundedCornerShape(
                    topStart = 20.dp, bottomStart = 20.dp, topEnd = 8.dp, bottomEnd = 8.dp
                )
                index == items.lastIndex -> RoundedCornerShape(
                    topStart = 8.dp, bottomStart = 8.dp, topEnd = 20.dp, bottomEnd = 20.dp
                )
                else -> RoundedCornerShape(8.dp)
            }

            val bg by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceContainerHigh,
                animationSpec = tween(160),
                label = "kit_chip_bg"
            )
            val fg by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurface,
                animationSpec = tween(160),
                label = "kit_chip_fg"
            )

            Surface(
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    .clip(shape)
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onSelect(id)
                    }
                    .testTag("${tagPrefix}_$id"),
                shape = shape,
                color = bg
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                        ),
                        color = fg,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun KitSectionCard(
    title: String,
    modifier: Modifier = Modifier,
    badge: String? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = containerColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.labelLarge.copy(
                        letterSpacing = 0.8.sp,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                if (badge != null) {
                    Spacer(Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = badge,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            content()
        }
    }
}

@Composable
fun KitMetricTile(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    unit: String = "",
    emphasized: Boolean = false
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = if (emphasized) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.72f)
        else MaterialTheme.colorScheme.surfaceContainerLow,
        border = if (emphasized) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
        else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 0.6.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = if (emphasized) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = PlusJakartaFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 23.sp,
                    lineHeight = 27.sp,
                    fontFeatureSettings = "tnum"
                ),
                color = if (emphasized) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (unit.isNotEmpty()) {
                Text(
                    text = unit,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (emphasized) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun KitRedFlagStrip(
    title: String,
    bullets: List<String>,
    modifier: Modifier = Modifier
) {
    val redColors = LocalMedWheelExtras.current.tierRed
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = redColors.container,
        border = BorderStroke(1.5.dp, redColors.badge.copy(alpha = 0.65f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.WarningAmber,
                    contentDescription = null,
                    tint = redColors.badge,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.labelLarge.copy(
                        letterSpacing = 0.8.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = redColors.badge
                )
            }
            bullets.forEach { line ->
                Text(
                    text = if (bullets.size > 1) "• $line" else line,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 20.sp
                    ),
                    color = redColors.onContainer
                )
            }
        }
    }
}

@Composable
fun KitInfoStrip(
    title: String,
    bullets: List<String>,
    modifier: Modifier = Modifier
) {
    val blueColors = LocalMedWheelExtras.current.tierBlue
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = blueColors.container,
        border = BorderStroke(1.dp, blueColors.badge.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = null,
                    tint = blueColors.badge,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.6.sp
                    ),
                    color = blueColors.badge
                )
            }
            bullets.forEach { line ->
                Text(
                    text = if (bullets.size > 1) "• $line" else line,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                        lineHeight = 18.sp
                    ),
                    color = blueColors.onContainer
                )
            }
        }
    }
}

@Composable
fun KitKeyValueRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    sublabel: String = "",
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1.1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            if (sublabel.isNotEmpty()) {
                Text(
                    text = sublabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall.copy(
                fontFamily = PlusJakartaFontFamily,
                fontWeight = FontWeight.Bold,
                fontFeatureSettings = "tnum"
            ),
            color = valueColor,
            modifier = Modifier.weight(0.95f)
        )
    }
}

/**
 * Compact tonal badge ("CALC" or "KIT") for Unified Search and Unified Pinned tab.
 */
@Composable
fun ItemTypeBadge(
    isKit: Boolean,
    modifier: Modifier = Modifier
) {
    val extras = LocalMedWheelExtras.current
    val bg = if (isKit) extras.tierGreen.container else MaterialTheme.colorScheme.primaryContainer
    val fg = if (isKit) extras.tierGreen.onContainer else MaterialTheme.colorScheme.onPrimaryContainer

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = bg
    ) {
        Text(
            text = stringResource(if (isKit) R.string.badge_kit else R.string.badge_calc),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.6.sp,
                fontSize = 10.sp
            ),
            color = fg,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

/**
 * Bottom action dock for every opened Kit Reference Card: ← BACK | Pin / Pinned.
 */
@Composable
fun KitBottomActionBar(
    isPinned: Boolean,
    onBack: () -> Unit,
    onTogglePin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        tonalElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledTonalButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onBack()
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
                    .testTag("kit_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.action_back),
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1
                )
            }

            FilledTonalButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onTogglePin()
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = if (isPinned) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (isPinned) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onPrimaryContainer
                ),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
                    .testTag("kit_pin_button")
            ) {
                Icon(
                    imageVector = if (isPinned) Icons.Rounded.PushPin else Icons.Outlined.PushPin,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = stringResource(if (isPinned) R.string.action_pinned else R.string.action_pin),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1
                )
            }
        }
    }
}
