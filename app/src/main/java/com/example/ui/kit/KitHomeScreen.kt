package com.example.ui.kit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Biotech
import androidx.compose.material.icons.rounded.Bloodtype
import androidx.compose.material.icons.rounded.ChildCare
import androidx.compose.material.icons.rounded.Emergency
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Healing
import androidx.compose.material.icons.rounded.HealthAndSafety
import androidx.compose.material.icons.rounded.LocalHospital
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.Medication
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material.icons.rounded.PregnantWoman
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.kit.KitBlockDef
import com.example.data.kit.KitBlockId
import com.example.data.kit.KitRegistry
import com.example.ui.theme.LocalMedWheelExtras

/**
 * Rebalanced 2-Tier Equal Bento Grid for the Ward Kit tab:
 * - Tier 1 ("HOSPITAL ESSENTIALS"): Symmetrical 2x2 Equal Bento Grid (4 Universal Blocks)
 * - Tier 2 ("SPECIALTIES"): Equal 2-Column A-Z Specialty Bento Grid (zero placeholder blocks)
 */
@Composable
fun KitHomeScreen(
    onOpenBlock: (KitBlockId) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    tier1Rows: List<List<KitBlockDef>> = KitRegistry.hospitalEssentialBlocks.chunked(2),
    tier2Rows: List<List<KitBlockDef>> = KitRegistry.specialtyBlocks.chunked(2)
) {
    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .testTag("kit_home_bento_grid"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "kit_header", contentType = "kit_header") {
            Column(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = stringResource(R.string.kit_header_title),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.kit_header_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // ====================================================================
        // TIER 1: HOSPITAL ESSENTIALS (2x2 SYMMETRICAL EQUAL BENTO GRID)
        // ====================================================================
        item(key = "tier1_header", contentType = "kit_section_header") {
            Text(
                text = "HOSPITAL ESSENTIALS",
                style = MaterialTheme.typography.labelLarge.copy(
                    letterSpacing = 1.sp,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 4.dp, top = 4.dp)
                    .testTag("kit_tier1_header")
            )
        }

        tier1Rows.forEachIndexed { rowIdx, rowBlocks ->
            item(key = "tier1_row_$rowIdx", contentType = "kit_bento_row") {
                EqualBentoPairRow(
                    blocks = rowBlocks,
                    onOpenBlock = onOpenBlock
                )
            }
        }

        // ====================================================================
        // TIER 2: SPECIALTIES (EQUAL 2-COLUMN A–Z BENTO GRID)
        // ====================================================================
        item(key = "tier2_header", contentType = "kit_section_header") {
            Spacer(Modifier.height(6.dp))
            Text(
                text = "SPECIALTIES",
                style = MaterialTheme.typography.labelLarge.copy(
                    letterSpacing = 1.sp,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 4.dp, top = 2.dp)
                    .testTag("kit_tier2_header")
            )
        }

        tier2Rows.forEachIndexed { rowIdx, rowBlocks ->
            item(key = "tier2_row_$rowIdx", contentType = "kit_bento_row") {
                EqualBentoPairRow(
                    blocks = rowBlocks,
                    onOpenBlock = onOpenBlock
                )
            }
        }

        item(key = "kit_bottom_spacer", contentType = "kit_spacer") {
            Spacer(Modifier.height(14.dp))
        }
    }
}

@Composable
private fun EqualBentoPairRow(
    blocks: List<KitBlockDef>,
    onOpenBlock: (KitBlockId) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        blocks.forEach { block ->
            EqualBentoBlockCard(
                block = block,
                icon = iconForBlock(block.id),
                onOpenBlock = { onOpenBlock(block.id) },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }
        // Keep strictly equal half-width card proportions on odd Specialty rows without placeholder cards
        if (blocks.size == 1) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

private fun iconForBlock(blockId: KitBlockId): ImageVector = when (blockId) {
    KitBlockId.ER_CRASH -> Icons.Rounded.Emergency
    KitBlockId.DOSE_ENGINE -> Icons.Rounded.Medication
    KitBlockId.LABS_FLUIDS -> Icons.Rounded.Biotech
    KitBlockId.IV_BLOOD -> Icons.Rounded.Bloodtype
    KitBlockId.CARDIOLOGY -> Icons.Rounded.Favorite
    KitBlockId.EMERGENCY_ANESTHESIA -> Icons.Rounded.LocalHospital
    KitBlockId.GASTRO_HEPATOLOGY -> Icons.Rounded.HealthAndSafety
    KitBlockId.GENERAL_SURGERY -> Icons.Rounded.Healing
    KitBlockId.ICU_CRITICAL_CARE -> Icons.Rounded.MonitorHeart
    KitBlockId.INTERNAL_MEDICINE -> Icons.Rounded.MedicalServices
    KitBlockId.NEPHROLOGY -> Icons.Rounded.WaterDrop
    KitBlockId.NEURO_NEUROSURGERY -> Icons.Rounded.Psychology
    KitBlockId.OBS_GYNAE -> Icons.Rounded.PregnantWoman
    KitBlockId.PEDIATRICS -> Icons.Rounded.ChildCare
}

@Composable
private fun EqualBentoBlockCard(
    block: KitBlockDef,
    icon: ImageVector,
    onOpenBlock: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalMedWheelExtras.current.isDark
    val haptic = LocalHapticFeedback.current
    val accent = if (isDark) block.palette.darkPrimary else block.palette.lightPrimary
    val container = if (isDark) block.palette.darkContainer else block.palette.lightContainer
    val onContainer = if (isDark) block.palette.darkOnContainer else block.palette.lightOnContainer

    Surface(
        modifier = modifier
            .heightIn(min = 158.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onOpenBlock()
            }
            .testTag("kit_block_${block.id.name.lowercase()}"),
        shape = RoundedCornerShape(24.dp),
        color = container.copy(alpha = if (isDark) 0.78f else 0.72f),
        border = BorderStroke(1.dp, accent.copy(alpha = if (isDark) 0.36f else 0.25f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                accent.copy(alpha = if (isDark) 0.22f else 0.14f),
                                accent.copy(alpha = 0f)
                            ),
                            center = Offset(size.width * 0.88f, size.height * 0.14f),
                            radius = size.width * 0.72f
                        )
                    )
                }
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = accent
                    ) {
                        Box(
                            modifier = Modifier.size(42.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isDark) block.palette.darkOnPrimary else block.palette.lightOnPrimary,
                                modifier = Modifier.size(23.dp)
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = if (isDark) 0.58f else 0.88f)
                    ) {
                        Text(
                            text = block.badgeText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 10.sp
                            ),
                            color = accent,
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = block.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            lineHeight = 22.sp
                        ),
                        color = onContainer,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = block.subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 16.sp),
                        color = onContainer.copy(alpha = 0.82f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
