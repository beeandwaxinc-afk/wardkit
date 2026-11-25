package com.example.ui.kit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.material.icons.rounded.CheckBoxOutlineBlank
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChoiceOption
import com.example.data.kit.CardiologyKitData
import com.example.data.kit.EmergencyAnesthesiaKitData
import com.example.data.kit.NeuroKitData
import com.example.ui.components.ConnectedPillRow
import com.example.ui.theme.LocalMedWheelExtras

// ============================================================================
// CARDIOLOGY — CARD 1: ECG Quick Sheet & STEMI Map
// ============================================================================
@Composable
fun CardioEcgStemiCard() {
    var modeId by rememberSaveable { mutableStateOf("stemi") }
    val red = LocalMedWheelExtras.current.tierRed

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_cardio_ecg_stemi"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "ecg_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("stemi", "STEMI Map"),
                    ChoiceOption("intervals", "Intervals"),
                    ChoiceOption("av", "AV Blocks")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "cardio_ecg_mode"
            )
        }

        when (modeId) {
            "stemi" -> {
                item(key = "stemi_table") {
                    KitSectionCard(title = "STEMI Territory → Coronary Artery", badge = "12-LEAD") {
                        CardiologyKitData.stemiTerritories.forEachIndexed { idx, row ->
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
                                        text = row.territory,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                        color = if (row.isRedFlag) red.badge else MaterialTheme.colorScheme.onSurface
                                    )
                                    Phase6PillBadge(
                                        text = row.leads,
                                        container = if (row.isRedFlag) red.badge else MaterialTheme.colorScheme.primaryContainer,
                                        content = if (row.isRedFlag) Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                Text(
                                    text = "Artery: ${row.artery}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                if (row.pearl.isNotEmpty()) {
                                    Text(
                                        text = row.pearl,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = if (row.isRedFlag) FontWeight.Bold else FontWeight.Normal,
                                            lineHeight = 18.sp
                                        ),
                                        color = if (row.isRedFlag) red.badge else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
            "intervals" -> {
                item(key = "normal_intervals") {
                    KitSectionCard(title = "Normal ECG Intervals & Axis", badge = "25 mm/s") {
                        CardiologyKitData.normalEcgIntervals.forEachIndexed { idx, (label, value) ->
                            if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                            KitKeyValueRow(
                                label = label,
                                value = value,
                                valueColor = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                item(key = "qtc_red_flag") {
                    KitRedFlagStrip(
                        title = "Prolonged QTc Alert",
                        bullets = listOf(CardiologyKitData.qtcRedFlag)
                    )
                }
            }
            else -> {
                items(CardiologyKitData.avBlocks, key = { it.degree }) { block ->
                    KitSectionCard(
                        title = block.degree,
                        badge = if (block.isHighRisk) "PACING RISK" else "MONITOR",
                        containerColor = if (block.isHighRisk) red.container.copy(alpha = 0.52f)
                        else MaterialTheme.colorScheme.surfaceContainerLow
                    ) {
                        Text(
                            text = block.ecgPattern,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = block.management,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = if (block.isHighRisk) FontWeight.Bold else FontWeight.Medium,
                                lineHeight = 18.sp
                            ),
                            color = if (block.isHighRisk) red.onContainer else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// CARDIOLOGY — CARD 2: Electrolyte & Drug ECG Changes
// ============================================================================
@Composable
fun CardioLytesDrugsEcgCard() {
    var modeId by rememberSaveable { mutableStateOf("lytes") }
    val red = LocalMedWheelExtras.current.tierRed
    val list = if (modeId == "lytes") CardiologyKitData.electrolyteEcgRows else CardiologyKitData.drugSyndromeEcgRows

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_cardio_lytes_drugs_ecg"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "lytes_drugs_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("lytes", "Electrolytes"),
                    ChoiceOption("drugs", "Drugs & Syndromes")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "cardio_ecg_cause_mode"
            )
        }

        items(list, key = { it.title }) { row ->
            KitSectionCard(
                title = row.title,
                badge = row.badge,
                containerColor = if (row.isRedFlag) red.container.copy(alpha = 0.52f)
                else MaterialTheme.colorScheme.surfaceContainerLow
            ) {
                Text(
                    text = row.findings,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        lineHeight = 20.sp,
                        fontFeatureSettings = "tnum"
                    ),
                    color = if (row.isRedFlag) red.onContainer else MaterialTheme.colorScheme.onSurface
                )
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// CARDIOLOGY — CARD 3: Murmurs & Dynamic Maneuvers
// ============================================================================
@Composable
fun CardioMurmursManeuversCard() {
    var modeId by rememberSaveable { mutableStateOf("murmurs") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_cardio_murmurs"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "murmurs_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("murmurs", "Murmurs Table"),
                    ChoiceOption("maneuvers", "Bedside Maneuvers")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "cardio_murmur_mode"
            )
        }

        if (modeId == "murmurs") {
            items(CardiologyKitData.murmurs, key = { it.name }) { m ->
                KitSectionCard(title = m.name, badge = m.timing) {
                    Text(
                        text = m.locationRadiation,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = m.keyFeatures,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        } else {
            items(CardiologyKitData.maneuvers, key = { it.maneuver }) { mv ->
                KitSectionCard(title = mv.maneuver, badge = mv.physiology) {
                    Text(
                        text = mv.effect,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 20.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// CARDIOLOGY — CARD 4: Cardiac Classifications
// ============================================================================
@Composable
fun CardioClassificationsCard() {
    var modeId by rememberSaveable { mutableStateOf("killip") }
    val red = LocalMedWheelExtras.current.tierRed

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_cardio_classifications"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "class_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("killip", "Killip & NYHA"),
                    ChoiceOption("hf", "HF (LVEF)"),
                    ChoiceOption("vw", "Vaughan-Williams")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "cardio_class_mode"
            )
        }

        when (modeId) {
            "killip" -> {
                item(key = "killip_card") {
                    KitSectionCard(title = "Killip Class (Post-MI Mortality)", badge = "BEDSIDE") {
                        CardiologyKitData.killipClasses.forEachIndexed { idx, row ->
                            if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                            KitKeyValueRow(
                                label = row.tier,
                                sublabel = row.description,
                                value = row.badge,
                                valueColor = if (idx >= 2) red.badge else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                item(key = "nyha_card") {
                    KitSectionCard(title = "NYHA Functional Classification", badge = "HEART FAILURE") {
                        CardiologyKitData.nyhaClasses.forEachIndexed { idx, row ->
                            if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                            KitKeyValueRow(
                                label = row.tier,
                                value = row.description,
                                valueColor = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
            "hf" -> {
                item(key = "hf_phenotypes") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CardiologyKitData.hfPhenotypes.forEach { (label, value) ->
                            KitMetricTile(
                                label = label,
                                value = value,
                                emphasized = label == "HFrEF",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                item(key = "hf_gdmt") {
                    KitSectionCard(title = "4 HFrEF GDMT Pillars", badge = "MORTALITY BENEFIT") {
                        CardiologyKitData.hfGdmtPillars.forEach { pillar ->
                            Text(
                                text = pillar,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 20.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
            else -> {
                items(CardiologyKitData.vaughanWilliams, key = { it.drugClass }) { vw ->
                    KitSectionCard(
                        title = "${vw.drugClass} · ${vw.mechanism}",
                        badge = vw.examples.substringBefore(","),
                        containerColor = if (vw.isRedFlag) red.container.copy(alpha = 0.45f)
                        else MaterialTheme.colorScheme.surfaceContainerLow
                    ) {
                        Text(
                            text = vw.examples,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = vw.note,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = if (vw.isRedFlag) FontWeight.Bold else FontWeight.Medium,
                                lineHeight = 18.sp
                            ),
                            color = if (vw.isRedFlag) red.onContainer else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// CARDIOLOGY — CARD 5: Cardiac Drugs & Anticoagulation
// ============================================================================
@Composable
fun CardioDrugsAnticoagCard() {
    var modeId by rememberSaveable { mutableStateOf("doac") }
    val red = LocalMedWheelExtras.current.tierRed

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_cardio_drugs_anticoag"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "anticoag_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("doac", "DOAC Cutoffs"),
                    ChoiceOption("dapt", "DAPT Rules"),
                    ChoiceOption("arrhythmia", "Acute Arrhythmia")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "cardio_drug_mode"
            )
        }

        when (modeId) {
            "doac" -> {
                items(CardiologyKitData.doacRenalMatrix, key = { it.drug }) { row ->
                    KitSectionCard(title = row.drug, badge = row.standardDose) {
                        Text(
                            text = "Reduced: ${row.reducedDoseRule}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontFeatureSettings = "tnum",
                                lineHeight = 20.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = row.avoidCutoff,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontFeatureSettings = "tnum"
                            ),
                            color = red.badge
                        )
                    }
                }
                item(key = "warfarin_only") {
                    KitRedFlagStrip(
                        title = "DOAC Absolute Contraindication",
                        bullets = listOf(CardiologyKitData.warfarinOnlyStrip)
                    )
                }
            }
            "dapt" -> {
                item(key = "dapt_card") {
                    KitSectionCard(title = "Dual Antiplatelet Therapy (DAPT)", badge = "ACS & STENTS") {
                        CardiologyKitData.daptRules.forEach { rule ->
                            Text(
                                text = "• $rule",
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
            else -> {
                items(CardiologyKitData.acuteArrhythmiaRows, key = { it.scenario }) { row ->
                    KitSectionCard(
                        title = row.scenario,
                        badge = row.badge,
                        containerColor = if (row.isUrgent) red.container.copy(alpha = 0.52f)
                        else MaterialTheme.colorScheme.surfaceContainerLow
                    ) {
                        Text(
                            text = row.action,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                lineHeight = 20.sp,
                                fontFeatureSettings = "tnum"
                            ),
                            color = if (row.isUrgent) red.onContainer else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// EMERGENCY & ANESTHESIA — CARD 1: Airway & RSI 7 Ps
// ============================================================================
@Composable
fun EmergAirwayRsiCard() {
    var modeId by rememberSaveable { mutableStateOf("rsi") }
    val red = LocalMedWheelExtras.current.tierRed

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_emerg_airway_rsi"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "airway_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("rsi", "RSI 7 Ps"),
                    ChoiceOption("difficult", "Difficult Airway"),
                    ChoiceOption("cico", "Failed Airway")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "emerg_airway_mode"
            )
        }

        when (modeId) {
            "rsi" -> {
                items(EmergencyAnesthesiaKitData.rsiSevenPs, key = { it.stepNumber }) { step ->
                    KitSectionCard(
                        title = "${step.stepNumber}. ${step.title}",
                        badge = step.timing,
                        containerColor = if (step.isCritical) red.container.copy(alpha = 0.48f)
                        else MaterialTheme.colorScheme.surfaceContainerLow
                    ) {
                        Text(
                            text = step.detail,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (step.isCritical) FontWeight.SemiBold else FontWeight.Normal,
                                lineHeight = 20.sp
                            ),
                            color = if (step.isCritical) red.onContainer else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            "difficult" -> {
                items(EmergencyAnesthesiaKitData.difficultAirwayMnemonics, key = { it.acronym }) { card ->
                    KitSectionCard(title = card.acronym, badge = card.purpose) {
                        card.letters.forEachIndexed { idx, (letter, desc) ->
                            if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                            Row(
                                modifier = Modifier.padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Phase6PillBadge(
                                    text = letter,
                                    container = MaterialTheme.colorScheme.primary,
                                    content = MaterialTheme.colorScheme.onPrimary
                                )
                                Text(
                                    text = desc,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
            else -> {
                item(key = "cico_red_card") {
                    KitRedFlagStrip(
                        title = "Failed Airway · CICO Rescue Algorithm",
                        bullets = EmergencyAnesthesiaKitData.cicoFailedAirwaySteps
                    )
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// EMERGENCY & ANESTHESIA — CARD 2: Oxygen Delivery & NIV Guide
// ============================================================================
@Composable
fun EmergOxygenNivCard() {
    var modeId by rememberSaveable { mutableStateOf("o2") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_emerg_o2_niv"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "o2_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("o2", "O₂ Devices Table"),
                    ChoiceOption("niv", "CPAP / BiPAP (NIV)")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "emerg_o2_mode"
            )
        }

        if (modeId == "o2") {
            items(EmergencyAnesthesiaKitData.oxygenDevices, key = { it.device }) { row ->
                KitSectionCard(title = row.device, badge = "${row.flowRate} · ${row.fio2}") {
                    Text(
                        text = row.note,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 20.sp,
                            fontFeatureSettings = "tnum"
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        } else {
            items(EmergencyAnesthesiaKitData.nivModes, key = { it.mode }) { niv ->
                KitSectionCard(title = niv.mode, badge = niv.initialSettings) {
                    Text(
                        text = "Best for: ${niv.bestFor}",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = niv.pearl,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            item(key = "niv_contraindications") {
                KitRedFlagStrip(
                    title = "Absolute NIV Contraindications & 30–60m Check",
                    bullets = EmergencyAnesthesiaKitData.nivContraindications
                )
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// EMERGENCY & ANESTHESIA — CARD 3: Sedation & Analgesia Ladder
// ============================================================================
@Composable
fun EmergSedationPainCard() {
    var modeId by rememberSaveable { mutableStateOf("sedation") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_emerg_sedation_pain"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "sed_pain_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("sedation", "Procedural Sedation"),
                    ChoiceOption("who", "WHO Pain & Reversals")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "emerg_sed_mode"
            )
        }

        if (modeId == "sedation") {
            item(key = "sed_monitor_strip") {
                KitInfoStrip(
                    title = "Mandatory Sedation Monitoring",
                    bullets = listOf(EmergencyAnesthesiaKitData.sedationMonitoringStrip)
                )
            }
            items(EmergencyAnesthesiaKitData.proceduralSedationDrugs, key = { it.drug }) { row ->
                KitSectionCard(title = row.drug, badge = row.onsetDur) {
                    Text(
                        text = row.ivDose,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontFeatureSettings = "tnum"
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Caution: ${row.caution}",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            item(key = "who_ladder") {
                KitSectionCard(title = "WHO Analgesia Ladder", badge = "STEPWISE") {
                    EmergencyAnesthesiaKitData.whoPainLadder.forEachIndexed { idx, (step, rx) ->
                        if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                        Column(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = step,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = rx,
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
            item(key = "reversals_strip") {
                KitRedFlagStrip(
                    title = "Reversal Agents",
                    bullets = EmergencyAnesthesiaKitData.reversalAgents
                )
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// EMERGENCY & ANESTHESIA — CARD 4: Trauma Survey & Clinical Imaging Rules
// ============================================================================
@Composable
fun EmergTraumaImagingCard() {
    var modeId by rememberSaveable { mutableStateOf("abcde") }
    val red = LocalMedWheelExtras.current.tierRed

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_emerg_trauma_imaging"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "trauma_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("abcde", "ATLS ABCDE"),
                    ChoiceOption("cspine", "C-Spine Rule"),
                    ChoiceOption("ottawa", "Ottawa Rules")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "emerg_trauma_mode"
            )
        }

        when (modeId) {
            "abcde" -> {
                items(EmergencyAnesthesiaKitData.atlsPrimarySurvey, key = { it.letter }) { row ->
                    KitSectionCard(
                        title = row.domain,
                        badge = row.letter,
                        containerColor = if (row.isUrgent) red.container.copy(alpha = 0.52f)
                        else MaterialTheme.colorScheme.surfaceContainerLow
                    ) {
                        Text(
                            text = row.actions,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                lineHeight = 20.sp
                            ),
                            color = if (row.isUrgent) red.onContainer else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            "cspine" -> {
                item(key = "cspine_card") {
                    KitSectionCard(title = "Canadian C-Spine Rule", badge = "GCS 15 · STABLE") {
                        EmergencyAnesthesiaKitData.canadianCSpineSteps.forEachIndexed { idx, (step, detail) ->
                            if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                            Column(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = step,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = detail,
                                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
            else -> {
                items(EmergencyAnesthesiaKitData.ottawaRules, key = { it.first }) { (title, criteria) ->
                    KitSectionCard(title = title, badge = "INDICATED IF") {
                        Text(
                            text = criteria,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
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
// EMERGENCY & ANESTHESIA — CARD 5: Pre-Op Fasting & Drug Hold Matrix
// ============================================================================
@Composable
fun EmergPreOpFastingCard() {
    var modeId by rememberSaveable { mutableStateOf("npo") }
    val red = LocalMedWheelExtras.current.tierRed

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_emerg_preop_fasting"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "fasting_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("npo", "NPO 2-4-6-8 Rules"),
                    ChoiceOption("holds", "Pre-Op Drug Hold")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "emerg_preop_mode"
            )
        }

        if (modeId == "npo") {
            items(EmergencyAnesthesiaKitData.fastingRules, key = { it.hours }) { rule ->
                KitSectionCard(title = rule.category, badge = rule.hours) {
                    Text(
                        text = rule.examples,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            item(key = "fasting_exception") {
                Phase6AmberBanner(
                    title = "Clinical Exceptions",
                    body = EmergencyAnesthesiaKitData.fastingExceptionNote
                )
            }
        } else {
            item(key = "continue_card") {
                KitInfoStrip(
                    title = "CONTINUE Morning of Surgery (Sip of Water)",
                    bullets = listOf(EmergencyAnesthesiaKitData.preOpContinueList)
                )
            }
            item(key = "hold_matrix") {
                KitSectionCard(title = "HOLD Pre-Op — Exact Windows", badge = "STOP TIMING") {
                    EmergencyAnesthesiaKitData.preOpHoldList.forEachIndexed { idx, row ->
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
                                    text = row.drug,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                    color = if (row.isHighRisk) red.badge else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )
                                Phase6PillBadge(
                                    text = row.holdWindow,
                                    container = if (row.isHighRisk) red.badge else MaterialTheme.colorScheme.primaryContainer,
                                    content = if (row.isHighRisk) Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Text(
                                text = row.rationale,
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
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
// NEURO & NEUROSURGERY — CARD 1: Cranial Nerves I–XII Exam & Lesions
// ============================================================================
@Composable
fun NeuroCranialNervesCard() {
    var groupId by rememberSaveable { mutableStateOf("all") }
    val red = LocalMedWheelExtras.current.tierRed
    val visible = when (groupId) {
        "1_4" -> NeuroKitData.cranialNerves.filter { it.number in 1..4 }
        "5_8" -> NeuroKitData.cranialNerves.filter { it.number in 5..8 }
        "9_12" -> NeuroKitData.cranialNerves.filter { it.number in 9..12 }
        else -> NeuroKitData.cranialNerves
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_neuro_cranial_nerves"),
        contentPadding = PaddingValues(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item(key = "cn_filter_row") {
            KitScrollableChipRow(
                items = listOf(
                    "all" to "All I–XII",
                    "1_4" to "I–IV",
                    "5_8" to "V–VIII",
                    "9_12" to "IX–XII"
                ),
                selectedId = groupId,
                onSelect = { groupId = it },
                tagPrefix = "neuro_cn_filter"
            )
        }

        items(visible, key = { it.number }) { cn ->
            KitSectionCard(
                title = "${cn.cnLabel} · ${cn.nameAndForamen}",
                badge = if (cn.isRedFlag) "RED FLAG" else null,
                containerColor = if (cn.isRedFlag) red.container.copy(alpha = 0.52f)
                else MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Test: ${cn.bedsideTest}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Lesion: ${cn.lesionDeficit}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (cn.isRedFlag) FontWeight.SemiBold else FontWeight.Normal,
                        lineHeight = 20.sp
                    ),
                    color = if (cn.isRedFlag) red.onContainer else MaterialTheme.colorScheme.onSurface
                )
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// NEURO & NEUROSURGERY — CARD 2: Interactive Dermatomes, Myotomes & Reflexes
// ============================================================================
@Composable
fun NeuroDermatomesCard() {
    var modeId by rememberSaveable { mutableStateOf("upper") }
    val rows = if (modeId == "upper") NeuroKitData.upperLimbDermatomes else NeuroKitData.lowerLimbDermatomes

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_neuro_dermatomes"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "dermatome_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("upper", "Upper Limb (C5–T1)"),
                    ChoiceOption("lower", "Lower Limb (L2–S2)")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "neuro_derm_mode"
            )
        }

        item(key = "dermatome_landmarks") {
            KitInfoStrip(
                title = "Rapid Sensory Landmarks",
                bullets = listOf(NeuroKitData.dermatomeLandmarks)
            )
        }

        items(rows, key = { it.level }) { row ->
            KitSectionCard(
                title = "Root ${row.level}",
                badge = if (row.reflex == "—") "NO DTR" else "DTR: ${row.reflex}"
            ) {
                Text(
                    text = "Dermatome: ${row.dermatome}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Myotome: ${row.myotome}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// NEURO & NEUROSURGERY — CARD 3: Brainstem & Spinal Cord Syndromes
// ============================================================================
@Composable
fun NeuroSyndromesCard() {
    var modeId by rememberSaveable { mutableStateOf("brainstem") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_neuro_syndromes"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "syndrome_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("brainstem", "Brainstem Syndromes"),
                    ChoiceOption("spinal", "Spinal Cord & Cauda")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "neuro_syndrome_mode"
            )
        }

        if (modeId == "brainstem") {
            item(key = "rule_of_4s") {
                KitInfoStrip(
                    title = "Brainstem Rule of 4s",
                    bullets = listOf(NeuroKitData.ruleOfFoursPearl)
                )
            }
            items(NeuroKitData.brainstemSyndromes, key = { it.title }) { syn ->
                KitSectionCard(title = syn.title, badge = syn.territoryBadge) {
                    Text(
                        text = syn.presentation,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        } else {
            items(NeuroKitData.spinalCordSyndromes, key = { it.title }) { syn ->
                KitSectionCard(title = syn.title, badge = syn.territoryBadge) {
                    Text(
                        text = syn.presentation,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            item(key = "cauda_conus_red") {
                KitRedFlagStrip(
                    title = "Surgical Emergency · Cauda Equina vs. Conus Medullaris",
                    bullets = NeuroKitData.caudaVsConusBullets
                )
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// NEURO & NEUROSURGERY — CARD 4: Acute Stroke Thrombolysis Checklist
// ============================================================================
@Composable
fun NeuroStrokeThrombolysisCard() {
    var checkedCsv by rememberSaveable { mutableStateOf("") }
    val checkedIds = checkedCsv.split(",").filter { it.isNotEmpty() }.toSet()
    val haptic = LocalHapticFeedback.current
    val green = LocalMedWheelExtras.current.tierGreen
    val total = NeuroKitData.strokeMandatoryGates.size
    val allCleared = checkedIds.size == total

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_neuro_stroke_tpa"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "stroke_window") {
            KitInfoStrip(
                title = "Thrombolysis & Thrombectomy Windows",
                bullets = listOf(NeuroKitData.strokeWindowSummary)
            )
        }

        item(key = "stroke_dosing") {
            KitSectionCard(title = "IV Thrombolytic Dosing", badge = "WEIGHT-BASED") {
                NeuroKitData.thrombolyticDoses.forEachIndexed { idx, (drug, dose) ->
                    if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                    KitKeyValueRow(
                        label = drug,
                        value = dose,
                        valueColor = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        item(key = "stroke_gates") {
            KitSectionCard(
                title = "Mandatory Pre-Drug Safety Gates",
                badge = "${checkedIds.size} / $total CLEARED",
                containerColor = if (allCleared) green.container.copy(alpha = 0.65f)
                else MaterialTheme.colorScheme.surfaceContainerLow
            ) {
                NeuroKitData.strokeMandatoryGates.forEachIndexed { idx, gate ->
                    val isChecked = gate.id in checkedIds
                    if (idx > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                val next = if (isChecked) checkedIds - gate.id else checkedIds + gate.id
                                checkedCsv = next.joinToString(",")
                            }
                            .padding(vertical = 6.dp, horizontal = 4.dp)
                            .testTag("stroke_gate_${gate.id}"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = if (isChecked) Icons.Rounded.CheckBox else Icons.Rounded.CheckBoxOutlineBlank,
                            contentDescription = gate.title,
                            tint = if (isChecked) green.badge else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = gate.title,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = gate.detail,
                                style = MaterialTheme.typography.bodySmall.copy(fontFeatureSettings = "tnum"),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        item(key = "stroke_contraindications") {
            KitRedFlagStrip(
                title = "Key Absolute Contraindications",
                bullets = NeuroKitData.strokeContraindications
            )
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// NEURO & NEUROSURGERY — CARD 5: Raised ICP Escalation Ladder & Herniation
// ============================================================================
@Composable
fun NeuroRaisedIcpCard() {
    var modeId by rememberSaveable { mutableStateOf("ladder") }
    val red = LocalMedWheelExtras.current.tierRed

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_neuro_raised_icp"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "icp_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("ladder", "ICP Ladder (0→3)"),
                    ChoiceOption("herniation", "Herniation Syndromes")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "neuro_icp_mode"
            )
        }

        item(key = "icp_targets") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NeuroKitData.icpTargets.forEach { (label, value) ->
                    KitMetricTile(
                        label = label,
                        value = value,
                        emphasized = label == "Target CPP",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        if (modeId == "ladder") {
            items(NeuroKitData.icpLadder, key = { it.tierBadge }) { tier ->
                KitSectionCard(
                    title = tier.title,
                    badge = tier.tierBadge,
                    containerColor = if (tier.isUrgent) red.container.copy(alpha = 0.52f)
                    else MaterialTheme.colorScheme.surfaceContainerLow
                ) {
                    tier.bullets.forEach { bullet ->
                        Text(
                            text = "• $bullet",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                lineHeight = 20.sp,
                                fontFeatureSettings = "tnum"
                            ),
                            color = if (tier.isUrgent) red.onContainer else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        } else {
            items(NeuroKitData.herniationSyndromes, key = { it.type }) { row ->
                KitSectionCard(
                    title = row.type,
                    badge = row.siteBadge,
                    containerColor = red.container.copy(alpha = 0.45f)
                ) {
                    Text(
                        text = row.signs,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            lineHeight = 20.sp
                        ),
                        color = red.onContainer
                    )
                }
            }
            item(key = "herniation_bridge") {
                KitRedFlagStrip(
                    title = "Immediate Herniation Emergency Bridge",
                    bullets = listOf(NeuroKitData.herniationRescueBridge)
                )
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

// ============================================================================
// SHARED PHASE 6 VISUAL HELPERS
// ============================================================================
@Composable
private fun Phase6PillBadge(
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
private fun Phase6AmberBanner(
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
