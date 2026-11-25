package com.example.ui.kit

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
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChoiceOption
import com.example.data.kit.CkdRiskLevel
import com.example.data.kit.GastroKitData
import com.example.data.kit.HepBSeverity
import com.example.data.kit.NephrologyKitData
import com.example.data.kit.ObsGynaeKitData
import com.example.ui.components.ConnectedPillRow
import com.example.ui.theme.LocalMedWheelExtras
import com.example.ui.theme.PlusJakartaFontFamily

// ============================================================================
// GASTRO & HEPATOLOGY — CARD 1: Interactive Hepatitis B & C Serology Decoder
// ============================================================================
@Composable
fun GastroHepBDecoderCard() {
    var modeId by rememberSaveable { mutableStateOf("hepb") }
    var hbsAg by rememberSaveable { mutableStateOf(false) }
    var antiHbs by rememberSaveable { mutableStateOf(false) }
    var totalAntiHbc by rememberSaveable { mutableStateOf(false) }
    var igmAntiHbc by rememberSaveable { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    val extras = LocalMedWheelExtras.current

    val decoded = GastroKitData.decodeHepB(hbsAg, antiHbs, totalAntiHbc, igmAntiHbc)
    val (bannerBg, bannerFg, bannerBadge) = when (decoded.severity) {
        HepBSeverity.SAFE -> Triple(extras.tierGreen.container, extras.tierGreen.onContainer, extras.tierGreen.badge)
        HepBSeverity.INFO -> Triple(extras.tierBlue.container, extras.tierBlue.onContainer, extras.tierBlue.badge)
        HepBSeverity.WARNING -> Triple(extras.tierAmber.container, extras.tierAmber.onContainer, extras.tierAmber.badge)
        HepBSeverity.CRITICAL -> Triple(extras.tierRed.container, extras.tierRed.onContainer, extras.tierRed.badge)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_gastro_hepb_decoder")
    ) {
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("hepb", "Interactive Hep B"),
                    ChoiceOption("hepc", "Hep C Matrix")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "gastro_hep_mode"
            )
        }

        if (modeId == "hepb") {
            // Live pinned interpretation banner at the top
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("hepb_live_result"),
                shape = RoundedCornerShape(24.dp),
                color = bannerBg,
                border = BorderStroke(1.5.dp, bannerBadge.copy(alpha = 0.65f))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Phase7PillBadge(
                            text = decoded.badge,
                            container = bannerBadge,
                            content = Color.White
                        )
                        Box(
                            modifier = Modifier
                                .heightIn(min = 48.dp)
                                .clip(RoundedCornerShape(50))
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    hbsAg = false
                                    antiHbs = false
                                    totalAntiHbc = false
                                    igmAntiHbc = false
                                }
                                .testTag("hepb_reset_btn"),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.85f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.RestartAlt,
                                        contentDescription = "Reset markers",
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Reset",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                    Text(
                        text = decoded.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = PlusJakartaFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 19.sp
                        ),
                        color = bannerFg
                    )
                    Text(
                        text = decoded.detail,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            lineHeight = 19.sp
                        ),
                        color = bannerFg.copy(alpha = 0.92f)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item(key = "hepb_toggles") {
                    KitSectionCard(title = "Tap Markers to Decode Live", badge = "4 MARKERS") {
                        HepBMarkerToggleRow(
                            label = "HBsAg",
                            subtitle = "Surface Antigen (Active infection)",
                            isPositive = hbsAg,
                            onChange = { hbsAg = it },
                            tag = "hepb_hbsag"
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                        HepBMarkerToggleRow(
                            label = "Anti-HBs",
                            subtitle = "Surface Antibody (Protective immunity)",
                            isPositive = antiHbs,
                            onChange = { antiHbs = it },
                            tag = "hepb_antihbs"
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                        HepBMarkerToggleRow(
                            label = "Total Anti-HBc",
                            subtitle = "Core Antibody (Past or current infection)",
                            isPositive = totalAntiHbc,
                            onChange = { totalAntiHbc = it },
                            tag = "hepb_total_hbc"
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                        HepBMarkerToggleRow(
                            label = "IgM Anti-HBc",
                            subtitle = "Acute Core Antibody (Recent / window)",
                            isPositive = igmAntiHbc,
                            onChange = { igmAntiHbc = it },
                            tag = "hepb_igm_hbc"
                        )
                    }
                }
                item { Spacer(Modifier.height(10.dp)) }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(GastroKitData.hepCMatrix, key = { it.pattern }) { row ->
                    KitSectionCard(
                        title = row.meaning,
                        badge = row.pattern,
                        containerColor = if (row.isActive) extras.tierRed.container.copy(alpha = 0.48f)
                        else MaterialTheme.colorScheme.surfaceContainerLow
                    ) {
                        Text(
                            text = row.action,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                            color = if (row.isActive) extras.tierRed.onContainer else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                item { Spacer(Modifier.height(10.dp)) }
            }
        }
    }
}

@Composable
private fun HepBMarkerToggleRow(
    label: String,
    subtitle: String,
    isPositive: Boolean,
    onChange: (Boolean) -> Unit,
    tag: String
) {
    val haptic = LocalHapticFeedback.current
    val red = LocalMedWheelExtras.current.tierRed

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.width(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            val posShape = RoundedCornerShape(topStart = 18.dp, bottomStart = 18.dp, topEnd = 6.dp, bottomEnd = 6.dp)
            val negShape = RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp, topEnd = 18.dp, bottomEnd = 18.dp)

            Surface(
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    .clip(posShape)
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onChange(true)
                    }
                    .testTag("${tag}_pos"),
                shape = posShape,
                color = if (isPositive) red.badge
                else MaterialTheme.colorScheme.surfaceContainerHigh
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "(+)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                        color = if (isPositive) Color.White
                        else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    .clip(negShape)
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onChange(false)
                    }
                    .testTag("${tag}_neg"),
                shape = negShape,
                color = if (!isPositive) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceContainerHigh
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "(−)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                        color = if (!isPositive) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

// ============================================================================
// GASTRO & HEPATOLOGY — CARD 2: Acute GI Bleed Bundles
// ============================================================================
@Composable
fun GastroGiBleedCard() {
    var modeId by rememberSaveable { mutableStateOf("variceal") }
    val red = LocalMedWheelExtras.current.tierRed

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_gastro_gi_bleed"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "gi_bleed_resus") {
            KitRedFlagStrip(
                title = "Immediate Resuscitation · Target Hb 7–8 g/dL",
                bullets = listOf(GastroKitData.giBleedResusStrip)
            )
        }

        item(key = "gi_bleed_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("variceal", "Variceal Bleed"),
                    ChoiceOption("nonvariceal", "Non-Variceal UGIB")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "gastro_bleed_mode"
            )
        }

        if (modeId == "variceal") {
            items(GastroKitData.varicealBundleSteps, key = { it.first }) { (title, detail) ->
                KitSectionCard(title = title) {
                    Text(
                        text = detail,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 20.sp,
                            fontFeatureSettings = "tnum"
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        } else {
            items(GastroKitData.nonVaricealSteps, key = { it.first }) { (title, detail) ->
                KitSectionCard(title = title) {
                    Text(
                        text = detail,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            item(key = "forrest_table") {
                KitSectionCard(title = "Forrest Classification · Rebleed Risk", badge = "ENDOSCOPY") {
                    GastroKitData.forrestClasses.forEachIndexed { idx, row ->
                        if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                        KitKeyValueRow(
                            label = "Forrest ${row.code}",
                            sublabel = row.appearance,
                            value = row.rebleedRisk,
                            valueColor = if (row.isHighRisk) red.badge else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// GASTRO & HEPATOLOGY — CARD 3: LFT Patterns & Jaundice Matrix
// ============================================================================
@Composable
fun GastroLftJaundiceCard() {
    var modeId by rememberSaveable { mutableStateOf("lft") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_gastro_lft_jaundice"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "lft_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("lft", "LFT & R-Factor"),
                    ChoiceOption("jaundice", "Jaundice Workup")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "gastro_lft_mode"
            )
        }

        if (modeId == "lft") {
            item(key = "rfactor_banner") {
                KitInfoStrip(
                    title = "R-Factor Formula",
                    bullets = listOf(GastroKitData.rFactorFormula)
                )
            }
            items(GastroKitData.lftPatterns, key = { it.pattern }) { row ->
                KitSectionCard(title = row.pattern, badge = row.rFactor) {
                    row.bullets.forEach { bullet ->
                        Text(
                            text = "• $bullet",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                lineHeight = 20.sp,
                                fontFeatureSettings = "tnum"
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        } else {
            items(GastroKitData.jaundiceBuckets, key = { it.first }) { (title, causes) ->
                KitSectionCard(title = title) {
                    Text(
                        text = causes,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// GASTRO & HEPATOLOGY — CARD 4: Cirrhosis Complication Protocols
// ============================================================================
@Composable
fun GastroCirrhosisCard() {
    var modeId by rememberSaveable { mutableStateOf("he") }
    val red = LocalMedWheelExtras.current.tierRed

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_gastro_cirrhosis"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "cirrhosis_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("he", "Encephalopathy"),
                    ChoiceOption("sbp", "SBP"),
                    ChoiceOption("hrs", "HRS-AKI")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "gastro_cirrhosis_mode"
            )
        }

        when (modeId) {
            "he" -> {
                item(key = "west_haven") {
                    KitSectionCard(title = "West Haven Encephalopathy Grades", badge = "G1–G4") {
                        GastroKitData.westHavenGrades.forEachIndexed { idx, (grade, desc) ->
                            if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                            KitKeyValueRow(
                                label = grade,
                                value = desc,
                                valueColor = if (idx == 3) red.badge else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                item(key = "he_rx") {
                    KitSectionCard(title = "Management & Lactulose Target", badge = "2–3 STOOLS/D") {
                        GastroKitData.heManagementBullets.forEach { bullet ->
                            Text(
                                text = "• $bullet",
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
            "sbp" -> {
                item(key = "sbp_protocol") {
                    KitSectionCard(title = "Spontaneous Bacterial Peritonitis", badge = "PMN ≥250") {
                        GastroKitData.sbpProtocolBullets.forEach { bullet ->
                            Text(
                                text = "• $bullet",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    lineHeight = 20.sp,
                                    fontFeatureSettings = "tnum"
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
            else -> {
                item(key = "hrs_protocol") {
                    KitSectionCard(title = "Hepatorenal Syndrome (HRS-AKI)", badge = "ALBUMIN + PRESSOR") {
                        GastroKitData.hrsAkiBullets.forEach { bullet ->
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
// GASTRO & HEPATOLOGY — CARD 5: Stool, H. pylori & IBD / IBS Key
// ============================================================================
@Composable
fun GastroStoolIbdCard() {
    var modeId by rememberSaveable { mutableStateOf("bristol") }
    val green = LocalMedWheelExtras.current.tierGreen

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_gastro_stool_ibd"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "stool_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("bristol", "Bristol Stool"),
                    ChoiceOption("asuc", "Acute Severe UC"),
                    ChoiceOption("hpylori", "H. pylori & IBS")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "gastro_stool_mode"
            )
        }

        when (modeId) {
            "bristol" -> {
                item(key = "bristol_card") {
                    KitSectionCard(title = "Bristol Stool Form Scale (1–7)", badge = "TYPES 3–4 IDEAL") {
                        GastroKitData.bristolStoolRows.forEachIndexed { idx, row ->
                            if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                            KitKeyValueRow(
                                label = row.range,
                                sublabel = row.description,
                                value = row.clinicalLabel,
                                valueColor = if (row.isNormal) green.badge else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
            "asuc" -> {
                item(key = "truelove_red") {
                    KitRedFlagStrip(
                        title = "Truelove & Witts Acute Severe UC (ASUC)",
                        bullets = GastroKitData.trueloveWittsCriteria
                    )
                }
                item(key = "asuc_bundle") {
                    KitSectionCard(title = "ASUC Inpatient Ward Bundle", badge = "ADMIT STAT") {
                        GastroKitData.asucWardBundle.forEach { bullet ->
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
                item(key = "hpylori_warn") {
                    Phase7AmberBanner(
                        title = "Protocol Reference — Verify Local Resistance",
                        body = "Select Bismuth Quadruple vs Clarithromycin Triple based on regional clarithromycin resistance."
                    )
                }
                item(key = "hpylori_ibs_card") {
                    KitSectionCard(title = "H. pylori Eradication & Rome IV IBS", badge = "14 DAYS") {
                        GastroKitData.hPyloriAndIbsBullets.forEach { bullet ->
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
// NEPHROLOGY — CARD 1: KDIGO AKI Staging & Differentiation
// ============================================================================
@Composable
fun NephroAkiStagingCard() {
    var modeId by rememberSaveable { mutableStateOf("stages") }
    val red = LocalMedWheelExtras.current.tierRed

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_nephro_aki"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "aki_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("stages", "KDIGO AKI Stages"),
                    ChoiceOption("diff", "Pre / Intrinsic / Post")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "nephro_aki_mode"
            )
        }

        if (modeId == "stages") {
            items(NephrologyKitData.kdigoAkiStages, key = { it.stage }) { row ->
                KitSectionCard(
                    title = "KDIGO ${row.stage}",
                    badge = if (row.isSevere) "SEVERE" else "AKI",
                    containerColor = if (row.isSevere) red.container.copy(alpha = 0.52f)
                    else MaterialTheme.colorScheme.surfaceContainerLow
                ) {
                    Text(
                        text = "SCr: ${row.scrCriteria}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFeatureSettings = "tnum"
                        ),
                        color = if (row.isSevere) red.onContainer else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Urine Output: ${row.uoCriteria}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontFeatureSettings = "tnum"
                        ),
                        color = if (row.isSevere) red.badge else MaterialTheme.colorScheme.primary
                    )
                }
            }
        } else {
            item(key = "aki_diff_table") {
                KitSectionCard(title = "Prerenal vs. Intrinsic ATN", badge = "URINE INDICES") {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("METRIC", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold), color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1.1f))
                        Text("PRERENAL", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold), color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(0.95f))
                        Text("ATN", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold), color = red.badge, modifier = Modifier.weight(1.05f))
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    NephrologyKitData.akiDiffRows.forEachIndexed { idx, row ->
                        if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = row.parameter,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1.1f)
                            )
                            Text(
                                text = row.prerenal,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontFeatureSettings = "tnum"
                                ),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(0.95f)
                            )
                            Text(
                                text = row.intrinsicAtn,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontFeatureSettings = "tnum"
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1.05f)
                            )
                        }
                    }
                }
            }
            item(key = "postrenal_strip") {
                KitInfoStrip(
                    title = "Rule Out Postrenal Obstruction First",
                    bullets = listOf(NephrologyKitData.postrenalPearl)
                )
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// NEPHROLOGY — CARD 2: CKD G1–G5 / A1–A3 Risk Heatmap
// ============================================================================
@Composable
fun NephroCkdHeatmapCard() {
    val isDark = LocalMedWheelExtras.current.isDark

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_nephro_ckd_heatmap"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "ckd_heatmap_grid") {
            KitSectionCard(title = "KDIGO CKD Risk Heatmap (GFR × ACR)", badge = "G1–G5 / A1–A3") {
                // Header row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "GFR",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1.05f)
                    )
                    listOf("A1\n<30", "A2\n30–300", "A3\n>300").forEach { col ->
                        Text(
                            text = col,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontFeatureSettings = "tnum",
                                lineHeight = 13.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // 6 GFR rows
                NephrologyKitData.ckdHeatmapRows.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1.05f)) {
                            Text(
                                text = row.gfrLabel,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFeatureSettings = "tnum"
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = row.gfrRange,
                                style = MaterialTheme.typography.labelSmall.copy(fontFeatureSettings = "tnum"),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        HeatmapCell(level = row.a1Risk, isDark = isDark, modifier = Modifier.weight(1f))
                        HeatmapCell(level = row.a2Risk, isDark = isDark, modifier = Modifier.weight(1f))
                        HeatmapCell(level = row.a3Risk, isDark = isDark, modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        item(key = "nephro_referral") {
            KitRedFlagStrip(
                title = "Mandatory Nephrology Referral Triggers",
                bullets = NephrologyKitData.nephrologyReferralTriggers
            )
        }

        item(key = "ckd_targets") {
            KitSectionCard(title = "CKD Ward Complication Targets", badge = "GOALS") {
                NephrologyKitData.ckdWardTargets.forEach { target ->
                    Text(
                        text = "• $target",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 20.sp,
                            fontFeatureSettings = "tnum"
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

@Composable
private fun HeatmapCell(
    level: CkdRiskLevel,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    val (bg, fg) = when (level) {
        CkdRiskLevel.LOW -> if (isDark) Color(0xFF144527) to Color(0xFF86EFAC) else Color(0xFFCFF6DA) to Color(0xFF0A4D22)
        CkdRiskLevel.MODERATE -> if (isDark) Color(0xFF4A3B08) to Color(0xFFFDE047) else Color(0xFFFEF0B3) to Color(0xFF6B4700)
        CkdRiskLevel.HIGH -> if (isDark) Color(0xFF592A08) to Color(0xFFFDBA74) else Color(0xFFFFD8B0) to Color(0xFF7C2D12)
        CkdRiskLevel.VERY_HIGH -> if (isDark) Color(0xFF5C1519) to Color(0xFFFCA5A5) else Color(0xFFFFCDD2) to Color(0xFF7F1D1D)
    }

    Surface(
        modifier = modifier.height(36.dp),
        shape = RoundedCornerShape(10.dp),
        color = bg
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = level.shortLabel,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 11.sp
                ),
                color = fg
            )
        }
    }
}

// ============================================================================
// NEPHROLOGY — CARD 3: Urinalysis Casts & Crystals Key
// ============================================================================
@Composable
fun NephroUrinalysisCard() {
    var modeId by rememberSaveable { mutableStateOf("casts") }
    val red = LocalMedWheelExtras.current.tierRed
    val list = if (modeId == "casts") NephrologyKitData.urinaryCasts else NephrologyKitData.urinaryCrystals

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_nephro_urinalysis"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "ua_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("casts", "Urinary Casts"),
                    ChoiceOption("crystals", "Urinary Crystals")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "nephro_ua_mode"
            )
        }

        items(list, key = { it.name }) { item ->
            KitSectionCard(
                title = item.name,
                badge = item.badge,
                containerColor = if (item.isUrgent) red.container.copy(alpha = 0.45f)
                else MaterialTheme.colorScheme.surfaceContainerLow
            ) {
                Text(
                    text = item.significance,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        lineHeight = 20.sp
                    ),
                    color = if (item.isUrgent) red.onContainer else MaterialTheme.colorScheme.onSurface
                )
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// NEPHROLOGY — CARD 4: Dialysis Emergencies (AEIOU) & Sick-Day Rules (SADMANS)
// ============================================================================
@Composable
fun NephroDialysisSickDayCard() {
    var modeId by rememberSaveable { mutableStateOf("aeiou") }
    val red = LocalMedWheelExtras.current.tierRed

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_nephro_dialysis_sadmans"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "dialysis_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("aeiou", "Dialysis (AEIOU)"),
                    ChoiceOption("sadmans", "Sick-Day (SADMANS)")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "nephro_dialysis_mode"
            )
        }

        if (modeId == "aeiou") {
            items(NephrologyKitData.dialysisAeiou, key = { it.letter }) { item ->
                KitSectionCard(
                    title = item.title,
                    badge = item.letter,
                    containerColor = red.container.copy(alpha = 0.48f)
                ) {
                    Text(
                        text = item.detail,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 20.sp
                        ),
                        color = red.onContainer
                    )
                }
            }
        } else {
            item(key = "sadmans_banner") {
                Phase7AmberBanner(
                    title = "Acute Dehydration / Illness / AKI Rule",
                    body = "Temporarily HOLD these 7 SADMANS medication classes until volume status & kidney function recover."
                )
            }
            item(key = "sadmans_card") {
                KitSectionCard(title = "SADMANS — Hold 7 Drugs in AKI", badge = "7 CLASSES") {
                    NephrologyKitData.sadmansRules.forEachIndexed { idx, item ->
                        if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Phase7PillBadge(
                                text = item.letter,
                                container = MaterialTheme.colorScheme.primary,
                                content = MaterialTheme.colorScheme.onPrimary
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.drugClass,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = item.riskReason,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
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
// NEPHROLOGY — CARD 5: Renal Drug Dosing Adjustment Flags
// ============================================================================
@Composable
fun NephroRenalDrugsCard() {
    var query by rememberSaveable { mutableStateOf("") }
    val red = LocalMedWheelExtras.current.tierRed
    val filtered = if (query.isBlank()) {
        NephrologyKitData.renalDrugFlags
    } else {
        val q = query.trim().lowercase()
        NephrologyKitData.renalDrugFlags.filter {
            it.drug.lowercase().contains(q) ||
                it.rule.lowercase().contains(q) ||
                it.cutoffBadge.lowercase().contains(q)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_nephro_renal_drugs"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item(key = "renal_drug_filter") {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Filter drug (e.g., Metformin, Opioids, Cefepime)…") },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Rounded.Close, contentDescription = "Clear filter")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(50),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("nephro_drug_filter_input")
            )
        }

        if (filtered.isEmpty()) {
            item(key = "renal_drug_empty") {
                KitSectionCard(title = "No matching renal drug flags") {
                    Text(
                        text = "Try searching Metformin, Nitrofurantoin, Enoxaparin, DOACs, Opioids, Cefepime, Gabapentin, or Contrast.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(filtered, key = { it.id }) { row ->
                KitSectionCard(
                    title = row.drug,
                    badge = row.cutoffBadge,
                    containerColor = if (row.isStopAlert) red.container.copy(alpha = 0.45f)
                    else MaterialTheme.colorScheme.surfaceContainerLow
                ) {
                    Text(
                        text = row.rule,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            lineHeight = 20.sp,
                            fontFeatureSettings = "tnum"
                        ),
                        color = if (row.isStopAlert) red.onContainer else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// OBS & GYNAE — CARD 1: Antenatal Care & Screening Timeline
// ============================================================================
@Composable
fun ObsAntenatalCard() {
    var triId by rememberSaveable { mutableStateOf("t1") }
    val selected = ObsGynaeKitData.trimesters.firstOrNull { it.id == triId }
        ?: ObsGynaeKitData.trimesters.first()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_obs_antenatal"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "trimester_toggle") {
            ConnectedPillRow(
                options = ObsGynaeKitData.trimesters.map { ChoiceOption(it.id, it.label) },
                selectedId = triId,
                onSelect = { triId = it },
                tagPrefix = "obs_tri_mode"
            )
        }

        item(key = "trimester_card_${selected.id}") {
            KitSectionCard(title = selected.rangeSubtitle, badge = "ANTENATAL") {
                selected.items.forEachIndexed { idx, (heading, detail) ->
                    if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                    Column(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = heading,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = detail,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                lineHeight = 20.sp,
                                fontFeatureSettings = "tnum"
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// OBS & GYNAE — CARD 2: Preeclampsia & Eclampsia MgSO₄ Protocols
// ============================================================================
@Composable
fun ObsMgso4Card() {
    var modeId by rememberSaveable { mutableStateOf("zuspan") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_obs_mgso4"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "mgso4_indication") {
            KitInfoStrip(
                title = "MgSO₄ Indications & 24-Hr Rule",
                bullets = listOf(ObsGynaeKitData.mgso4IndicationStrip)
            )
        }

        item(key = "mgso4_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("zuspan", "Zuspan (IV)"),
                    ChoiceOption("pritchard", "Pritchard (IM)"),
                    ChoiceOption("tox", "Toxicity Gate")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "obs_mgso4_mode"
            )
        }

        when (modeId) {
            "zuspan" -> {
                items(ObsGynaeKitData.zuspanSteps, key = { it.first }) { (title, dose) ->
                    KitSectionCard(title = title, badge = "IV PUMP") {
                        Text(
                            text = dose,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 20.sp,
                                fontFeatureSettings = "tnum"
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            "pritchard" -> {
                items(ObsGynaeKitData.pritchardSteps, key = { it.first }) { (title, dose) ->
                    KitSectionCard(title = title, badge = "IV + IM") {
                        Text(
                            text = dose,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 20.sp,
                                fontFeatureSettings = "tnum"
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            else -> {
                item(key = "mgso4_gate") {
                    KitSectionCard(title = "Mandatory Pre-Dose Toxicity Gate (ALL 3)", badge = "BEFORE EVERY DOSE") {
                        ObsGynaeKitData.mgToxicityChecks.forEach { line ->
                            Text(
                                text = line,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 20.sp,
                                    fontFeatureSettings = "tnum"
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                item(key = "mgso4_antidote") {
                    KitRedFlagStrip(
                        title = "MgSO₄ Toxicity Emergency Antidote",
                        bullets = listOf(ObsGynaeKitData.mgAntidoteLine)
                    )
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// OBS & GYNAE — CARD 3: PPH (4 Ts) & Uterotonic Ladder
// ============================================================================
@Composable
fun ObsPphCard() {
    var modeId by rememberSaveable { mutableStateOf("ladder") }
    val red = LocalMedWheelExtras.current.tierRed

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_obs_pph"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "pph_def") {
            KitRedFlagStrip(
                title = "Postpartum Hemorrhage (PPH) Threshold",
                bullets = listOf(ObsGynaeKitData.pphDefinitionStrip)
            )
        }

        item(key = "pph_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("ladder", "Uterotonic Ladder"),
                    ChoiceOption("fourts", "4 Ts Etiology")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "obs_pph_mode"
            )
        }

        if (modeId == "ladder") {
            items(ObsGynaeKitData.uterotonicLadder, key = { it.line }) { step ->
                KitSectionCard(
                    title = step.line,
                    badge = if (step.isRedFlag) "CONTRAINDICATION" else "STEP",
                    containerColor = if (step.isRedFlag) red.container.copy(alpha = 0.48f)
                    else MaterialTheme.colorScheme.surfaceContainerLow
                ) {
                    Text(
                        text = step.drugAndDose,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontFeatureSettings = "tnum"
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = step.warning,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = if (step.isRedFlag) FontWeight.Bold else FontWeight.Medium,
                            lineHeight = 18.sp
                        ),
                        color = if (step.isRedFlag) red.badge else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            item(key = "pph_escalation") {
                Phase7AmberBanner(
                    title = "Refractory Mechanical & Surgical Escalation",
                    body = ObsGynaeKitData.pphSurgicalEscalation
                )
            }
        } else {
            item(key = "pph_4ts") {
                KitSectionCard(title = "The 4 Ts of Postpartum Hemorrhage", badge = "ETIOLOGY") {
                    ObsGynaeKitData.pphFourTs.forEachIndexed { idx, (tLabel, desc) ->
                        if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                        Column(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = tLabel,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = desc,
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
// OBS & GYNAE — CARD 4: CTG Interpretation Matrix (FIGO / NICE)
// ============================================================================
@Composable
fun ObsCtgCard() {
    var modeId by rememberSaveable { mutableStateOf("baseline") }
    val red = LocalMedWheelExtras.current.tierRed

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_obs_ctg"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "ctg_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("baseline", "Baseline & Variability"),
                    ChoiceOption("decels", "Decelerations & Rescue")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "obs_ctg_mode"
            )
        }

        if (modeId == "baseline") {
            item(key = "ctg_drcbravado") {
                KitInfoStrip(
                    title = "Systematic CTG Read · DR C BRAVADO",
                    bullets = listOf("Define Risk · Contractions · Baseline RAte · Variability · Accelerations · Decelerations · Overall impression.")
                )
            }
            items(ObsGynaeKitData.ctgCoreFeatures, key = { it.feature }) { row ->
                KitSectionCard(
                    title = row.feature,
                    badge = row.normalBadge,
                    containerColor = if (row.isUrgent) red.container.copy(alpha = 0.4f)
                    else MaterialTheme.colorScheme.surfaceContainerLow
                ) {
                    Text(
                        text = row.abnormalNote,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 20.sp,
                            fontFeatureSettings = "tnum"
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        } else {
            items(ObsGynaeKitData.ctgDecelerations, key = { it.feature }) { row ->
                KitSectionCard(
                    title = row.feature,
                    badge = row.normalBadge,
                    containerColor = if (row.isUrgent) red.container.copy(alpha = 0.5f)
                    else MaterialTheme.colorScheme.surfaceContainerLow
                ) {
                    Text(
                        text = row.abnormalNote,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (row.isUrgent) FontWeight.SemiBold else FontWeight.Normal,
                            lineHeight = 20.sp
                        ),
                        color = if (row.isUrgent) red.onContainer else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            item(key = "ctg_rescue") {
                KitRedFlagStrip(
                    title = "Immediate Intrauterine Resuscitation",
                    bullets = ObsGynaeKitData.intrauterineResusSteps
                )
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// OBS & GYNAE — CARD 5: Safe Drugs in Pregnancy & Lactation
// ============================================================================
@Composable
fun ObsSafeDrugsCard() {
    var modeId by rememberSaveable { mutableStateOf("safe") }
    val red = LocalMedWheelExtras.current.tierRed

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_obs_safe_drugs"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "preg_drugs_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("safe", "Safe Ward / OPD"),
                    ChoiceOption("redlist", "Teratogens Red List")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "obs_drugs_mode"
            )
        }

        if (modeId == "safe") {
            items(ObsGynaeKitData.safePregnancyDrugs, key = { it.title }) { row ->
                KitSectionCard(title = row.title, badge = row.badge) {
                    Text(
                        text = row.detail,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            item(key = "lactation_pearl") {
                KitInfoStrip(
                    title = "Breastfeeding & Lactation Safety",
                    bullets = listOf(ObsGynaeKitData.lactationPearl)
                )
            }
        } else {
            items(ObsGynaeKitData.teratogenRedList, key = { it.title }) { row ->
                KitSectionCard(
                    title = row.title,
                    badge = row.badge,
                    containerColor = red.container.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = row.detail,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 20.sp
                        ),
                        color = red.onContainer
                    )
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// SHARED PHASE 7 VISUAL HELPERS
// ============================================================================
@Composable
private fun Phase7PillBadge(
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
private fun Phase7AmberBanner(
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
