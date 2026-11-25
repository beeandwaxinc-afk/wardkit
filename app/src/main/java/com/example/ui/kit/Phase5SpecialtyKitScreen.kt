package com.example.ui.kit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.material.icons.rounded.CheckBoxOutlineBlank
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.TableRows
import androidx.compose.material.icons.rounded.ViewAgenda
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChoiceOption
import com.example.data.kit.GeneralSurgeryKitData
import com.example.data.kit.IcuKitData
import com.example.data.kit.InternalMedicineKitData
import com.example.data.kit.RassTone
import com.example.ui.components.ConnectedPillRow
import com.example.ui.theme.LocalMedWheelExtras
import com.example.ui.theme.PlusJakartaFontFamily

// ============================================================================
// GENERAL SURGERY — CARD 1: ATLS Hemorrhagic Shock (Classes I–IV)
// ============================================================================
@Composable
fun SurgAtlsShockCard() {
    var selectedId by rememberSaveable { mutableStateOf("c1") }
    var showFullTable by rememberSaveable { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    val selected = GeneralSurgeryKitData.atlsShockClasses.firstOrNull { it.id == selectedId }
        ?: GeneralSurgeryKitData.atlsShockClasses.first()
    val red = LocalMedWheelExtras.current.tierRed

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_surg_atls_shock"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "atls_selector") {
            ConnectedPillRow(
                options = GeneralSurgeryKitData.atlsShockClasses.map { ChoiceOption(it.id, it.label) },
                selectedId = selectedId,
                onSelect = {
                    selectedId = it
                    showFullTable = false
                },
                tagPrefix = "surg_atls_class"
            )
        }

        item(key = "atls_table_toggle") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    modifier = Modifier
                        .heightIn(min = 40.dp)
                        .clip(RoundedCornerShape(50))
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            showFullTable = !showFullTable
                        }
                        .testTag("surg_atls_table_toggle"),
                    shape = RoundedCornerShape(50),
                    color = if (showFullTable) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceContainerHigh
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = if (showFullTable) Icons.Rounded.ViewAgenda else Icons.Rounded.TableRows,
                            contentDescription = null,
                            tint = if (showFullTable) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (showFullTable) "Single Class View" else "Compare All I–IV",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (showFullTable) MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        if (!showFullTable) {
            item(key = "atls_hero_${selected.id}") {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = if (selected.isCritical) red.container.copy(alpha = 0.72f)
                    else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f),
                    border = BorderStroke(
                        1.5.dp,
                        if (selected.isCritical) red.badge.copy(alpha = 0.6f)
                        else MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${selected.label.uppercase()} HEMORRHAGE",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.8.sp
                                ),
                                color = if (selected.isCritical) red.badge else MaterialTheme.colorScheme.primary
                            )
                            Phase5PillBadge(
                                text = "${selected.bloodLossPct} · ${selected.bloodLossMl}",
                                container = if (selected.isCritical) red.badge else MaterialTheme.colorScheme.primary,
                                content = Color.White
                            )
                        }
                        Text(
                            text = selected.fluidAction,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp
                            ),
                            color = if (selected.isCritical) red.onContainer
                            else MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            item(key = "atls_metrics_${selected.id}") {
                val metricPairs = listOf(
                    "Heart Rate" to "${selected.heartRate} bpm",
                    "Blood Pressure" to selected.bloodPressure,
                    "Pulse Pressure" to selected.pulsePressure,
                    "Resp Rate" to "${selected.respRate} /min",
                    "Urine Output" to selected.urineOutput,
                    "Base Deficit" to selected.baseDeficit
                ).chunked(2)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    metricPairs.forEach { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            pair.forEach { (label, value) ->
                                KitMetricTile(
                                    label = label,
                                    value = value,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                    KitSectionCard(title = "CNS / Mental Status") {
                        Text(
                            text = selected.cnsStatus,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        } else {
            item(key = "atls_compact_table") {
                KitSectionCard(title = "ATLS 10th-Ed Comparison (I–IV)", badge = "ALL CLASSES") {
                    GeneralSurgeryKitData.atlsShockClasses.forEachIndexed { idx, row ->
                        if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${row.label} (${row.bloodLossPct} · ${row.bloodLossMl})",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        fontFeatureSettings = "tnum"
                                    ),
                                    color = if (row.isCritical) red.badge else MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "BD ${row.baseDeficit}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFeatureSettings = "tnum"
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "HR ${row.heartRate} · BP ${row.bloodPressure} · PP ${row.pulsePressure} · RR ${row.respRate}",
                                style = MaterialTheme.typography.bodySmall.copy(fontFeatureSettings = "tnum"),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "UO ${row.urineOutput} · CNS: ${row.cnsStatus}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Action: ${row.fluidAction}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        item(key = "atls_red_flag") {
            KitRedFlagStrip(
                title = "Occult Shock Red Flag",
                bullets = listOf(GeneralSurgeryKitData.atlsRedFlag)
            )
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// GENERAL SURGERY — CARD 2: Burn Surface Area Reference
// ============================================================================
@Composable
fun SurgBurnsTbsaCard() {
    var modeId by rememberSaveable { mutableStateOf("nines") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_surg_burns"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "burns_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("nines", "Rule of Nines"),
                    ChoiceOption("aba", "ABA Transfer Criteria")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "surg_burns_mode"
            )
        }

        if (modeId == "nines") {
            item(key = "burns_table") {
                KitSectionCard(title = "Rule of Nines · Adult vs. Infant/Child", badge = "% TBSA") {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "BODY REGION",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1.4f)
                        )
                        Text(
                            text = "ADULT",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(0.75f)
                        )
                        Text(
                            text = "PEADS",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(0.9f)
                        )
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    GeneralSurgeryKitData.burnRegions.forEachIndexed { idx, row ->
                        if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = row.region,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1.4f)
                            )
                            Text(
                                text = row.adultPct,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontFamily = PlusJakartaFontFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFeatureSettings = "tnum"
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(0.75f)
                            )
                            Text(
                                text = row.pedsPct,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontFamily = PlusJakartaFontFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFeatureSettings = "tnum"
                                ),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(0.9f)
                            )
                        }
                    }
                }
            }
            item(key = "burns_pearls") {
                KitInfoStrip(
                    title = "Bedside TBSA & Lund-Browder Pearls",
                    bullets = listOf(
                        GeneralSurgeryKitData.lundBrowderPearl,
                        GeneralSurgeryKitData.burnPalmPearl
                    )
                )
            }
        } else {
            item(key = "burns_aba") {
                KitRedFlagStrip(
                    title = "ABA Burn Center Transfer Criteria",
                    bullets = GeneralSurgeryKitData.abaTransferCriteria
                )
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// GENERAL SURGERY — CARD 3: Sutures & Removal Timeline
// ============================================================================
@Composable
fun SurgSuturesCard() {
    var modeId by rememberSaveable { mutableStateOf("removal") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_surg_sutures"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "suture_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("removal", "Removal & Gauge"),
                    ChoiceOption("materials", "Material Guide")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "surg_suture_mode"
            )
        }

        if (modeId == "removal") {
            item(key = "suture_removal_table") {
                KitSectionCard(title = "Suture Gauge & Removal by Site", badge = "TIMELINE") {
                    GeneralSurgeryKitData.sutureRemovalRows.forEachIndexed { idx, row ->
                        if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                        KitKeyValueRow(
                            label = row.site,
                            sublabel = "Gauge: ${row.gauge}",
                            value = row.removalDays,
                            valueColor = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            item(key = "suture_delay_note") {
                Phase5AmberBanner(
                    title = "Delayed Healing Rule",
                    body = GeneralSurgeryKitData.sutureDelayNote
                )
            }
        } else {
            item(key = "suture_absorbable") {
                KitSectionCard(title = "Absorbable Sutures", badge = "INTERNAL / SUBCUT") {
                    GeneralSurgeryKitData.absorbableSutures.forEachIndexed { idx, row ->
                        if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                        Column(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = row.name,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = row.type,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Text(
                                text = row.absorptionOrUse,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            item(key = "suture_non_absorbable") {
                KitSectionCard(title = "Non-Absorbable Sutures", badge = "SKIN / VASCULAR") {
                    GeneralSurgeryKitData.nonAbsorbableSutures.forEachIndexed { idx, row ->
                        if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                        Column(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = row.name,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = row.type,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Text(
                                text = row.absorptionOrUse,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// GENERAL SURGERY — CARD 4: Wounds, Tetanus & Surgical Prophylaxis
// ============================================================================
@Composable
fun SurgWoundsTetanusCard() {
    var modeId by rememberSaveable { mutableStateOf("tetanus") }
    val red = LocalMedWheelExtras.current.tierRed

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_surg_wounds_tetanus"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "wounds_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("tetanus", "Tetanus / TIG"),
                    ChoiceOption("classes", "Wound Classes"),
                    ChoiceOption("abx", "Pre-Op Abx")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "surg_wound_mode"
            )
        }

        when (modeId) {
            "tetanus" -> {
                item(key = "tetanus_clean") {
                    KitSectionCard(title = "Clean, Minor Wound", badge = "NO TIG") {
                        GeneralSurgeryKitData.tetanusCleanMinor.forEachIndexed { idx, row ->
                            if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                            KitKeyValueRow(
                                label = row.vaccineHistory,
                                value = row.action,
                                valueColor = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                item(key = "tetanus_dirty") {
                    KitSectionCard(title = "Dirty / Tetanus-Prone Wound", badge = "CHECK TIG") {
                        GeneralSurgeryKitData.tetanusDirtyProne.forEachIndexed { idx, row ->
                            if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                            KitKeyValueRow(
                                label = row.vaccineHistory,
                                value = row.action,
                                valueColor = if (row.needsTig) red.badge else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                item(key = "tetanus_dirty_note") {
                    KitInfoStrip(
                        title = "Tetanus-Prone Definition & Administration",
                        bullets = listOf(GeneralSurgeryKitData.tetanusDirtyExamples)
                    )
                }
            }
            "classes" -> {
                item(key = "cdc_wound_classes") {
                    KitSectionCard(title = "CDC Surgical Wound Classification", badge = "SSI RISK") {
                        GeneralSurgeryKitData.cdcWoundClasses.forEachIndexed { idx, row ->
                            if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                            Column(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = row.classLabel,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Phase5PillBadge(
                                        text = row.ssiRate,
                                        container = MaterialTheme.colorScheme.primaryContainer,
                                        content = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                Text(
                                    text = row.examples,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
            else -> {
                item(key = "preop_warn") {
                    Phase5AmberBanner(
                        title = "Protocol Reference — Verify Local Guideline",
                        body = "Confirm institutional antibiogram, MRSA status, and β-lactam allergy history."
                    )
                }
                item(key = "preop_timing") {
                    KitSectionCard(title = "Timing & Redosing Rules", badge = "≤60 MIN") {
                        GeneralSurgeryKitData.preOpTimingBullets.forEach { bullet ->
                            Text(
                                text = "• $bullet",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 20.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                item(key = "preop_agents") {
                    KitSectionCard(title = "Typical Prophylactic Agents") {
                        GeneralSurgeryKitData.preOpTypicalAgents.forEachIndexed { idx, row ->
                            if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                            KitKeyValueRow(
                                label = row.surgeryType,
                                value = row.typicalAgent,
                                valueColor = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// GENERAL SURGERY — CARD 5: Post-Op Fever (5 Ws) & Drains / Stomas
// ============================================================================
@Composable
fun SurgPostOpDrainsCard() {
    var modeId by rememberSaveable { mutableStateOf("fever") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_surg_postop_drains"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "postop_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("fever", "Post-Op Fever (5 Ws)"),
                    ChoiceOption("drains", "Drains & Stomas")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "surg_postop_mode"
            )
        }

        if (modeId == "fever") {
            items(GeneralSurgeryKitData.postOpFeverTimeline, key = { it.window }) { stage ->
                KitSectionCard(title = stage.wLabel, badge = stage.window) {
                    stage.causes.forEach { line ->
                        Text(
                            text = "• $line",
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            item(key = "postop_red_flag") {
                KitRedFlagStrip(
                    title = "Early Toxic Fever Red Flag",
                    bullets = listOf(GeneralSurgeryKitData.postOpRedFlag)
                )
            }
        } else {
            item(key = "drain_criteria") {
                KitSectionCard(title = "Surgical Drain Removal Criteria", badge = "<20–30 mL/24h") {
                    GeneralSurgeryKitData.drainRemovalCriteria.forEach { bullet ->
                        Text(
                            text = "• $bullet",
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            item(key = "stoma_norms") {
                KitSectionCard(title = "Stoma Output Norms", badge = "ILEO vs COLO") {
                    GeneralSurgeryKitData.stomaNorms.forEachIndexed { idx, row ->
                        if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                        Column(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = row.type,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Phase5PillBadge(
                                    text = row.dailyOutput,
                                    container = MaterialTheme.colorScheme.primaryContainer,
                                    content = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Text(
                                text = row.siteAndAppearance,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = row.alertNote,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            item(key = "stoma_red_flag") {
                KitRedFlagStrip(
                    title = "Stoma Ischemia Warning",
                    bullets = listOf(GeneralSurgeryKitData.stomaIschemiaWarning)
                )
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// ICU & CRITICAL CARE — CARD 1: Initial Mechanical Ventilation Presets
// ============================================================================
@Composable
fun IcuVentPresetsCard() {
    var presetId by rememberSaveable { mutableStateOf("ards") }
    val preset = IcuKitData.ventPresets.firstOrNull { it.id == presetId }
        ?: IcuKitData.ventPresets.first()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_icu_vent_presets"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "vent_toggle") {
            ConnectedPillRow(
                options = IcuKitData.ventPresets.map { ChoiceOption(it.id, it.label) },
                selectedId = presetId,
                onSelect = { presetId = it },
                tagPrefix = "icu_vent_preset"
            )
        }

        item(key = "vent_params_${preset.id}") {
            KitSectionCard(
                title = preset.mode,
                badge = "INITIAL",
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
            ) {
                preset.metrics.chunked(2).forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        pair.forEach { (label, value) ->
                            KitMetricTile(
                                label = label,
                                value = value,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        item(key = "vent_goal_${preset.id}") {
            if (preset.isWarningGoal) {
                KitRedFlagStrip(
                    title = "Mandatory Safety Limit",
                    bullets = listOf(preset.hardLimitOrGoal)
                )
            } else {
                KitInfoStrip(
                    title = "Neuro / Post-Op Target",
                    bullets = listOf(preset.hardLimitOrGoal)
                )
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// ICU & CRITICAL CARE — CARD 2: High Peak vs. Plateau Pressure Troubleshooter
// ============================================================================
@Composable
fun IcuPeakPlateauCard() {
    var pathwayId by rememberSaveable { mutableStateOf("resistance") }
    var dopesExpanded by rememberSaveable { mutableStateOf(true) }
    val haptic = LocalHapticFeedback.current
    val pathway = IcuKitData.pressurePathways.firstOrNull { it.id == pathwayId }
        ?: IcuKitData.pressurePathways.first()
    val red = LocalMedWheelExtras.current.tierRed

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_icu_peak_plateau"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "insp_hold_step0") {
            KitInfoStrip(
                title = "Step 0 — Measure Plateau Pressure",
                bullets = listOf(IcuKitData.inspiratoryHoldStep)
            )
        }

        item(key = "pressure_toggle") {
            ConnectedPillRow(
                options = IcuKitData.pressurePathways.map { ChoiceOption(it.id, it.toggleLabel) },
                selectedId = pathwayId,
                onSelect = { pathwayId = it },
                tagPrefix = "icu_pressure_mode"
            )
        }

        item(key = "pathway_detail_${pathway.id}") {
            KitSectionCard(
                title = pathway.headline,
                badge = pathway.gradientRule
            ) {
                Text(
                    text = "LIKELY CAUSES",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.7.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                pathway.causes.forEach { cause ->
                    Text(
                        text = "• $cause",
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                Text(
                    text = "IMMEDIATE BEDSIDE ACTIONS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.7.sp
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                pathway.actions.forEachIndexed { idx, action ->
                    Text(
                        text = "${idx + 1}. $action",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 20.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        item(key = "dopes_collapsible") {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = red.container,
                border = BorderStroke(1.5.dp, red.badge.copy(alpha = 0.65f))
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 52.dp)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                dopesExpanded = !dopesExpanded
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .testTag("icu_dopes_toggle"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.WarningAmber,
                                contentDescription = null,
                                tint = red.badge,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "DOPES — SUDDEN DESAT / CRASH",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.7.sp
                                ),
                                color = red.badge
                            )
                        }
                        Icon(
                            imageVector = if (dopesExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                            contentDescription = if (dopesExpanded) "Collapse DOPES" else "Expand DOPES",
                            tint = red.badge
                        )
                    }
                    AnimatedVisibility(visible = dopesExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, bottom = 14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IcuKitData.dopesChecklist.forEach { item ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Phase5PillBadge(
                                        text = item.letter,
                                        container = red.badge,
                                        content = Color.White
                                    )
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.title,
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                            color = red.onContainer
                                        )
                                        Text(
                                            text = item.detail,
                                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 17.sp),
                                            color = red.onContainer.copy(alpha = 0.92f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// ICU & CRITICAL CARE — CARD 3: Weaning & SBT Readiness Checklist
// ============================================================================
@Composable
fun IcuWeaningSbtCard() {
    var checkedCsv by rememberSaveable { mutableStateOf("") }
    val checkedIds = checkedCsv.split(",").filter { it.isNotEmpty() }.toSet()
    val haptic = LocalHapticFeedback.current
    val green = LocalMedWheelExtras.current.tierGreen
    val total = IcuKitData.sbtChecklist.size
    val allReady = checkedIds.size == total

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_icu_weaning_sbt"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "sbt_checklist_card") {
            KitSectionCard(
                title = "Tap to Verify Readiness Criteria",
                badge = "${checkedIds.size} / $total READY",
                containerColor = if (allReady) green.container.copy(alpha = 0.65f)
                else MaterialTheme.colorScheme.surfaceContainerLow
            ) {
                IcuKitData.sbtChecklist.forEachIndexed { idx, item ->
                    val isChecked = item.id in checkedIds
                    if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                val next = if (isChecked) checkedIds - item.id else checkedIds + item.id
                                checkedCsv = next.joinToString(",")
                            }
                            .padding(vertical = 6.dp, horizontal = 4.dp)
                            .testTag("sbt_check_${item.id}"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = if (isChecked) Icons.Rounded.CheckBox else Icons.Rounded.CheckBoxOutlineBlank,
                            contentDescription = item.label,
                            tint = if (isChecked) green.badge else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = item.detail,
                                style = MaterialTheme.typography.bodySmall.copy(fontFeatureSettings = "tnum"),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        item(key = "sbt_thresholds") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IcuKitData.sbtThresholdTiles.forEach { (label, value) ->
                    KitMetricTile(
                        label = label,
                        value = value,
                        emphasized = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        item(key = "sbt_cuff_leak") {
            KitInfoStrip(
                title = "Cuff Leak Test",
                bullets = listOf(IcuKitData.cuffLeakPearl)
            )
        }

        item(key = "sbt_failure_strip") {
            KitRedFlagStrip(
                title = "SBT Failure Criteria — Reconnect & Rest",
                bullets = IcuKitData.sbtFailureCriteria
            )
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// ICU & CRITICAL CARE — CARD 4: Sedation & Delirium Scales (RASS, CPOT, CAM-ICU)
// ============================================================================
@Composable
fun IcuSedationDeliriumCard() {
    var modeId by rememberSaveable { mutableStateOf("rass") }
    val extras = LocalMedWheelExtras.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_icu_sedation_delirium"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "sedation_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("rass", "RASS"),
                    ChoiceOption("cam", "CAM-ICU"),
                    ChoiceOption("abcdef", "ABCDEF Bundle")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "icu_sedation_mode"
            )
        }

        when (modeId) {
            "rass" -> {
                item(key = "rass_table") {
                    KitSectionCard(title = "Richmond Agitation-Sedation Scale", badge = "TARGET 0 TO -2") {
                        IcuKitData.rassRows.forEach { row ->
                            val (bg, fg, badgeBg) = when (row.tone) {
                                RassTone.AGITATED -> Triple(extras.tierRed.container, extras.tierRed.onContainer, extras.tierRed.badge)
                                RassTone.TARGET -> Triple(extras.tierGreen.container, extras.tierGreen.onContainer, extras.tierGreen.badge)
                                RassTone.MODERATE -> Triple(extras.tierAmber.container, extras.tierAmber.onContainer, extras.tierAmber.badge)
                                RassTone.DEEP -> Triple(extras.tierBlue.container, extras.tierBlue.onContainer, extras.tierBlue.badge)
                            }
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                color = bg.copy(alpha = 0.72f)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = badgeBg
                                    ) {
                                        Box(
                                            modifier = Modifier.width(38.dp).padding(vertical = 4.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = row.score,
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontFamily = PlusJakartaFontFamily,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontFeatureSettings = "tnum"
                                                ),
                                                color = Color.White
                                            )
                                        }
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = row.term,
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                            color = fg
                                        )
                                        Text(
                                            text = row.description,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = fg.copy(alpha = 0.9f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            "cam" -> {
                item(key = "cam_icu_flow") {
                    KitSectionCard(title = "CAM-ICU Positive Delirium Flow", badge = "F1 + F2 + (F3 OR F4)") {
                        IcuKitData.camIcuFeatures.forEach { feat ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surfaceContainerHigh
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Phase5PillBadge(
                                        text = feat.number,
                                        container = MaterialTheme.colorScheme.primary,
                                        content = MaterialTheme.colorScheme.onPrimary
                                    )
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = feat.title,
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = feat.rule,
                                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                            if (feat.connector != null) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Phase5PillBadge(
                                        text = feat.connector,
                                        container = MaterialTheme.colorScheme.primaryContainer,
                                        content = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }
                    }
                }
                item(key = "cam_uta_note") {
                    Phase5AmberBanner(
                        title = "Deep Sedation Rule",
                        body = IcuKitData.camIcuUtaNote
                    )
                }
            }
            else -> {
                item(key = "abcdef_bundle") {
                    KitSectionCard(title = "ABCDEF ICU Liberation Bundle", badge = "CPOT ≥3 = PAIN") {
                        IcuKitData.abcdefBundle.forEachIndexed { idx, item ->
                            if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Phase5PillBadge(
                                    text = item.letter,
                                    container = MaterialTheme.colorScheme.primary,
                                    content = MaterialTheme.colorScheme.onPrimary
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = item.detail,
                                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// ICU & CRITICAL CARE — CARD 5: FAST HUGS BID & Electrolyte Safe Caps
// ============================================================================
@Composable
fun IcuProphylaxisLytesCard() {
    var modeId by rememberSaveable { mutableStateOf("fasthugs") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_icu_prophylaxis_lytes"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "icu_proph_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("fasthugs", "FAST HUGS BID & VAP"),
                    ChoiceOption("lytes", "Electrolyte Safe Caps")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "icu_proph_mode"
            )
        }

        if (modeId == "fasthugs") {
            item(key = "fast_hugs_grid") {
                KitSectionCard(title = "FAST HUGS BID Daily Rounding", badge = "11 CHECKS") {
                    IcuKitData.fastHugsBid.chunked(2).forEach { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            pair.forEach { item ->
                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(16.dp),
                                    color = MaterialTheme.colorScheme.surfaceContainerHigh
                                ) {
                                    Column(
                                        modifier = Modifier.padding(10.dp),
                                        verticalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Phase5PillBadge(
                                                text = item.letter,
                                                container = MaterialTheme.colorScheme.primary,
                                                content = MaterialTheme.colorScheme.onPrimary
                                            )
                                            Text(
                                                text = item.title,
                                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                                                color = MaterialTheme.colorScheme.onSurface,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Text(
                                            text = item.detail,
                                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 16.sp),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                            if (pair.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
            item(key = "vap_strip") {
                KitInfoStrip(
                    title = "VAP Prevention Bundle",
                    bullets = listOf(IcuKitData.vapPreventionStrip)
                )
            }
        } else {
            item(key = "icu_lytes_table") {
                KitSectionCard(title = "ICU Electrolyte Replacement Limits", badge = "SAFETY CAPS") {
                    IcuKitData.icuElectrolyteCaps.forEachIndexed { idx, row ->
                        if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                        Column(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = row.electrolyte,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )
                                Phase5PillBadge(
                                    text = row.limitBadge,
                                    container = MaterialTheme.colorScheme.primaryContainer,
                                    content = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Text(
                                text = row.guidance,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    lineHeight = 18.sp,
                                    fontFeatureSettings = "tnum"
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// INTERNAL MEDICINE — CARD 1: Empiric Antibiotic Syndromic Guide
// ============================================================================
@Composable
fun MedEmpiricAbxCard() {
    var filterId by rememberSaveable { mutableStateOf("all") }
    val red = LocalMedWheelExtras.current.tierRed
    val visible = if (filterId == "all") {
        InternalMedicineKitData.empiricAbxSyndromes
    } else {
        InternalMedicineKitData.empiricAbxSyndromes.filter { it.id == filterId }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_med_empiric_abx"),
        contentPadding = PaddingValues(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "abx_warning_banner") {
            Phase5AmberBanner(
                title = "Antimicrobial Stewardship",
                body = InternalMedicineKitData.abxHeaderWarning,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item(key = "abx_filter_chips") {
            KitScrollableChipRow(
                items = listOf("all" to "All") + InternalMedicineKitData.empiricAbxSyndromes.map { it.id to it.chipLabel },
                selectedId = filterId,
                onSelect = { filterId = it },
                tagPrefix = "med_abx_filter"
            )
        }

        items(visible, key = { it.id }) { syn ->
            KitSectionCard(
                title = syn.title,
                badge = syn.badge,
                containerColor = if (syn.isUrgent) red.container.copy(alpha = 0.55f)
                else MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                syn.tiers.forEachIndexed { idx, tier ->
                    if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                    Column(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = tier.setting,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                            color = if (tier.isRedFlag) red.badge else MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = tier.regimen,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (tier.isRedFlag) FontWeight.SemiBold else FontWeight.Normal,
                                lineHeight = 20.sp
                            ),
                            color = if (tier.isRedFlag) red.onContainer else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// INTERNAL MEDICINE — CARD 2: Insulin Pharmacokinetics & Glycemic Regimens
// ============================================================================
@Composable
fun MedInsulinPkCard() {
    var modeId by rememberSaveable { mutableStateOf("pk") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_med_insulin_pk"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "insulin_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("pk", "Insulin PK Table"),
                    ChoiceOption("regimen", "Inpatient Regimen")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "med_insulin_mode"
            )
        }

        if (modeId == "pk") {
            items(InternalMedicineKitData.insulinPkRows, key = { it.category }) { row ->
                KitSectionCard(title = row.category, badge = row.examples) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        KitMetricTile(label = "Onset", value = row.onset, modifier = Modifier.weight(1f))
                        KitMetricTile(label = "Peak", value = row.peak, modifier = Modifier.weight(1f))
                        KitMetricTile(label = "Duration", value = row.duration, modifier = Modifier.weight(1f))
                    }
                    Text(
                        text = row.bedsideNote,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            item(key = "insulin_rules") {
                KitSectionCard(title = "Inpatient Basal-Bolus Targets", badge = "140–180 mg/dL") {
                    InternalMedicineKitData.basalBolusRules.forEach { rule ->
                        Text(
                            text = "• $rule",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                lineHeight = 20.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            item(key = "insulin_tdd") {
                KitSectionCard(title = "Starting Total Daily Dose (TDD)", badge = "50% BASAL / 50% BOLUS") {
                    InternalMedicineKitData.inpatientTddTiers.forEachIndexed { idx, (dose, who) ->
                        if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                        KitKeyValueRow(
                            label = who,
                            value = dose,
                            valueColor = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            item(key = "insulin_red_flag") {
                KitRedFlagStrip(
                    title = "Type 1 Diabetes & Sliding-Scale Red Flag",
                    bullets = listOf(InternalMedicineKitData.insulinRedFlag)
                )
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// INTERNAL MEDICINE — CARD 3: Hypertensive Emergency Matrix
// ============================================================================
@Composable
fun MedHtnEmergencyCard() {
    val red = LocalMedWheelExtras.current.tierRed

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_med_htn_emergency"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "htn_golden_rule") {
            KitRedFlagStrip(
                title = "Golden Rule — Target-Organ Damage",
                bullets = listOf(InternalMedicineKitData.htnGoldenRule)
            )
        }

        items(InternalMedicineKitData.htnEmergencies, key = { it.condition }) { row ->
            KitSectionCard(
                title = row.condition,
                badge = row.targetBadge,
                containerColor = if (row.isHighRisk) red.container.copy(alpha = 0.48f)
                else MaterialTheme.colorScheme.surfaceContainerLow
            ) {
                Text(
                    text = row.firstLineAgents,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 20.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = row.cautionNote,
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// INTERNAL MEDICINE — CARD 4: Electrolyte Workup Rapid Maps
// ============================================================================
@Composable
fun MedElectrolyteMapsCard() {
    var modeId by rememberSaveable { mutableStateOf("na") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_med_electrolyte_maps"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "lytes_map_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("na", "Hyponatremia"),
                    ChoiceOption("k", "Potassium (K⁺)"),
                    ChoiceOption("ca", "Calcium (Ca²⁺)")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "med_lyte_mode"
            )
        }

        when (modeId) {
            "na" -> {
                item(key = "na_step1") {
                    KitSectionCard(title = "Step 1 — Check Serum Osmolality (Na <135)", badge = "mOsm/kg") {
                        InternalMedicineKitData.hypoNaStep1Osm.forEachIndexed { idx, (osm, meaning) ->
                            if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                            KitKeyValueRow(
                                label = osm,
                                value = meaning,
                                valueColor = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                items(InternalMedicineKitData.hypoNaStep2Volume, key = { it.title }) { bucket ->
                    KitSectionCard(title = "Step 2 · ${bucket.title}", badge = bucket.badge) {
                        bucket.bullets.forEach { bullet ->
                            Text(
                                text = "• $bullet",
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                item(key = "na_safety_strip") {
                    KitRedFlagStrip(
                        title = "3% NaCl & ODS Safety Cap",
                        bullets = listOf(InternalMedicineKitData.hypoNaSafetyStrip)
                    )
                }
            }
            "k" -> {
                items(InternalMedicineKitData.potassiumBuckets, key = { it.title }) { bucket ->
                    KitSectionCard(title = bucket.title, badge = bucket.badge) {
                        bucket.bullets.forEach { bullet ->
                            Text(
                                text = "• $bullet",
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
            else -> {
                item(key = "ca_banner") {
                    KitInfoStrip(
                        title = "Verify True Calcium First",
                        bullets = listOf(InternalMedicineKitData.calciumCorrectionBanner)
                    )
                }
                items(InternalMedicineKitData.calciumBuckets, key = { it.title }) { bucket ->
                    KitSectionCard(title = bucket.title, badge = bucket.badge) {
                        bucket.bullets.forEach { bullet ->
                            Text(
                                text = "• $bullet",
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// INTERNAL MEDICINE — CARD 5: Fever / FUO & Medical VTE Prophylaxis
// ============================================================================
@Composable
fun MedVteFuoCard() {
    var modeId by rememberSaveable { mutableStateOf("vte") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_med_vte_fuo"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "vte_fuo_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("vte", "Medical VTE Prophylaxis"),
                    ChoiceOption("fuo", "Inpatient Fever / FUO")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "med_vte_fuo_mode"
            )
        }

        if (modeId == "vte") {
            item(key = "padua_triggers") {
                KitSectionCard(title = "High-Risk Triggers (Padua Score ≥ 4)", badge = "SCORE ≥4") {
                    InternalMedicineKitData.paduaTriggers.forEachIndexed { idx, (pts, items) ->
                        if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Phase5PillBadge(
                                text = pts,
                                container = MaterialTheme.colorScheme.primary,
                                content = MaterialTheme.colorScheme.onPrimary
                            )
                            Text(
                                text = items,
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
            item(key = "vte_pharm") {
                KitSectionCard(title = "Pharmacologic Prophylaxis", badge = "RENAL CHECK") {
                    InternalMedicineKitData.vtePharmacologicBullets.forEach { bullet ->
                        Text(
                            text = "• $bullet",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                lineHeight = 20.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            item(key = "vte_contraindications") {
                KitRedFlagStrip(
                    title = "Contraindications — Use Mechanical SCDs",
                    bullets = InternalMedicineKitData.vteContraindications
                )
            }
        } else {
            item(key = "fuo_def") {
                KitInfoStrip(
                    title = "Classic FUO Definition",
                    bullets = listOf(InternalMedicineKitData.fuoDefinition)
                )
            }
            item(key = "fuo_big4") {
                KitSectionCard(title = "Big 4 Etiology Buckets", badge = "DIFFERENTIAL") {
                    InternalMedicineKitData.fuoBigFour.forEachIndexed { idx, bucket ->
                        if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                        Column(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = bucket.category,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = bucket.examples,
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
            item(key = "fuo_workup") {
                Phase5AmberBanner(
                    title = "Minimum Core Fever / FUO Workup",
                    body = InternalMedicineKitData.fuoCoreWorkupStrip
                )
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// SHARED PHASE 5 VISUAL HELPERS (M3 EXPRESSIVE · HIGH-CONTRAST)
// ============================================================================
@Composable
private fun Phase5PillBadge(
    text: String,
    container: Color,
    content: Color
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = container
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                fontFeatureSettings = "tnum"
            ),
            color = content,
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun Phase5AmberBanner(
    title: String,
    body: String,
    modifier: Modifier = Modifier
) {
    val amber = LocalMedWheelExtras.current.tierAmber
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = amber.container,
        border = BorderStroke(1.dp, amber.badge.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.6.sp
                ),
                color = amber.badge
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 18.sp
                ),
                color = amber.onContainer
            )
        }
    }
}
