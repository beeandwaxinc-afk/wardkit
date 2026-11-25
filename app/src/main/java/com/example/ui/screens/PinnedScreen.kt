package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.CalculatorDef
import com.example.data.DepartmentRegistry
import com.example.data.kit.KitCardDef
import com.example.data.kit.KitRegistry
import com.example.ui.kit.ItemTypeBadge
import com.example.ui.theme.LocalMedWheelExtras

@Composable
fun PinnedScreen(
    pinnedCalculators: List<CalculatorDef>,
    pinnedKitCards: List<KitCardDef>,
    mostUsedCalculators: List<Pair<CalculatorDef, Int>>,
    onOpenCalculator: (String) -> Unit,
    onUnpinCalculator: (String) -> Unit,
    onOpenKitCard: (String) -> Unit,
    onUnpinKitCard: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalMedWheelExtras.current.isDark

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("pinned_screen_list"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ====================================================================
        // SECTION 1: UNIFIED PINNED (Calculators + Kit Reference Cards)
        // ====================================================================
        item(key = "header_pinned") {
            Text(
                text = stringResource(R.string.pinned_section_title),
                style = MaterialTheme.typography.labelLarge.copy(
                    letterSpacing = 1.sp,
                    fontSize = 13.sp
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 6.dp, top = 4.dp, bottom = 2.dp)
            )
        }

        if (pinnedCalculators.isEmpty() && pinnedKitCards.isEmpty()) {
            item(key = "pinned_empty") {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.pinned_empty_hint),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 20.dp)
                            .testTag("pinned_empty_hint")
                    )
                }
            }
        } else {
            items(pinnedKitCards, key = { "pinned_kit_${it.id}" }) { card ->
                val block = KitRegistry.getBlock(card.blockId)
                val accent = if (isDark) block.palette.darkPrimary else block.palette.lightPrimary
                UnifiedPinnedItemCard(
                    title = card.title,
                    subtitle = block.title,
                    accent = accent,
                    isKit = true,
                    usageCount = null,
                    showUnpin = true,
                    unpinContentDescription = stringResource(R.string.cd_unpin_kit_card),
                    onClick = { onOpenKitCard(card.id) },
                    onUnpin = { onUnpinKitCard(card.id) },
                    tag = "pinned_card_${card.id}",
                    unpinTag = "unpin_${card.id}"
                )
            }

            items(pinnedCalculators, key = { "pinned_calc_${it.id}" }) { calc ->
                val dept = DepartmentRegistry.get(calc.departmentId)
                val accent = if (isDark) dept.palette.darkPrimary else dept.palette.lightPrimary
                UnifiedPinnedItemCard(
                    title = calc.name,
                    subtitle = dept.fullName,
                    accent = accent,
                    isKit = false,
                    usageCount = null,
                    showUnpin = true,
                    unpinContentDescription = stringResource(R.string.cd_unpin_calculator),
                    onClick = { onOpenCalculator(calc.id) },
                    onUnpin = { onUnpinCalculator(calc.id) },
                    tag = "pinned_card_${calc.id}",
                    unpinTag = "unpin_${calc.id}"
                )
            }
        }

        // ====================================================================
        // SECTION 2: MOST USED (Top 6 auto-ranked calculators by usage count)
        // ====================================================================
        item(key = "header_most_used") {
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.most_used_section_title),
                style = MaterialTheme.typography.labelLarge.copy(
                    letterSpacing = 1.sp,
                    fontSize = 13.sp
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 6.dp, bottom = 2.dp)
            )
        }

        if (mostUsedCalculators.isEmpty()) {
            item(key = "most_used_empty") {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.most_used_empty_hint),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 20.dp)
                            .testTag("most_used_empty_hint")
                    )
                }
            }
        } else {
            items(mostUsedCalculators, key = { "used_${it.first.id}" }) { (calc, count) ->
                val dept = DepartmentRegistry.get(calc.departmentId)
                val accent = if (isDark) dept.palette.darkPrimary else dept.palette.lightPrimary
                UnifiedPinnedItemCard(
                    title = calc.name,
                    subtitle = dept.fullName,
                    accent = accent,
                    isKit = false,
                    usageCount = count,
                    showUnpin = false,
                    unpinContentDescription = stringResource(R.string.cd_unpin_calculator),
                    onClick = { onOpenCalculator(calc.id) },
                    onUnpin = {},
                    tag = "most_used_card_${calc.id}",
                    unpinTag = "unpin_${calc.id}"
                )
            }
        }

        item(key = "bottom_spacer") {
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun UnifiedPinnedItemCard(
    title: String,
    subtitle: String,
    accent: Color,
    isKit: Boolean,
    usageCount: Int?,
    showUnpin: Boolean,
    unpinContentDescription: String,
    onClick: () -> Unit,
    onUnpin: () -> Unit,
    tag: String,
    unpinTag: String
) {
    val haptic = LocalHapticFeedback.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick()
            }
            .testTag(tag),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 12.dp, top = 15.dp, bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        lineHeight = 25.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ItemTypeBadge(isKit = isKit)
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(accent)
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelMedium,
                        color = accent,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (usageCount != null) {
                Spacer(Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.surfaceContainerHighest
                ) {
                    Text(
                        text = "${usageCount}×",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            if (showUnpin) {
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onUnpin()
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .testTag(unpinTag)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PushPin,
                        contentDescription = unpinContentDescription,
                        tint = accent,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}
