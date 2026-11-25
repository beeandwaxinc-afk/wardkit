package com.example.ui.kit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChoiceOption
import com.example.data.kit.LabUnitSystem
import com.example.data.kit.LabsFluidsKitData
import com.example.ui.components.ConnectedPillRow
import com.example.ui.theme.LocalMedWheelExtras
import com.example.ui.theme.PlusJakartaFontFamily

// ============================================================================
// BLOCK 2 · CARD 1: Core Adult Lab Panels (SI | Conv reactive)
// ============================================================================
@Composable
fun AdultCoreLabsCard(unitSystem: LabUnitSystem) {
    var selectedPanelId by rememberSaveable { mutableStateOf(LabsFluidsKitData.adultPanels.first().id) }
    val panel = LabsFluidsKitData.adultPanels.firstOrNull { it.id == selectedPanelId }
        ?: LabsFluidsKitData.adultPanels.first()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_labs_adult"),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "panel_chips") {
            KitScrollableChipRow(
                items = LabsFluidsKitData.adultPanels.map { it.id to it.label },
                selectedId = selectedPanelId,
                onSelect = { selectedPanelId = it },
                tagPrefix = "adult_lab_panel"
            )
        }

        item(key = "panel_table") {
            KitSectionCard(
                title = panel.label,
                badge = "${unitSystem.label} UNITS",
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                panel.rows.forEachIndexed { idx, row ->
                    KitKeyValueRow(
                        label = row.analyte,
                        sublabel = row.note,
                        value = row.rangeFor(unitSystem),
                        valueColor = MaterialTheme.colorScheme.primary
                    )
                    if (idx < panel.rows.lastIndex) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    }
                }
            }
        }
    }
}

// ============================================================================
// BLOCK 2 · CARD 2: Pediatric Age-Adjusted Labs (SI | Conv reactive)
// ============================================================================
@Composable
fun PediatricLabsCard(unitSystem: LabUnitSystem) {
    var selectedAgeId by rememberSaveable { mutableStateOf(LabsFluidsKitData.pedsLabGroups.first().id) }
    val group = LabsFluidsKitData.pedsLabGroups.firstOrNull { it.id == selectedAgeId }
        ?: LabsFluidsKitData.pedsLabGroups.first()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_labs_pediatric"),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "peds_lab_chips") {
            KitScrollableChipRow(
                items = LabsFluidsKitData.pedsLabGroups.map { it.id to it.ageLabel },
                selectedId = selectedAgeId,
                onSelect = { selectedAgeId = it },
                tagPrefix = "peds_lab_age"
            )
        }

        item(key = "peds_lab_table") {
            KitSectionCard(
                title = "Pediatric Labs · ${group.ageLabel}",
                badge = "${unitSystem.label} UNITS",
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                group.rows.forEachIndexed { idx, row ->
                    KitKeyValueRow(
                        label = row.analyte,
                        sublabel = row.note,
                        value = row.rangeFor(unitSystem),
                        valueColor = MaterialTheme.colorScheme.primary
                    )
                    if (idx < group.rows.lastIndex) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    }
                }
            }
        }

        item(key = "peds_lab_pearls") {
            KitInfoStrip(
                title = "Pediatric Physiological Shifts",
                bullets = listOf(
                    "WBC Shift (${group.ageLabel}): ${group.wbcShiftNote}",
                    "Alkaline Phosphatase (ALP) is physiologically 2–3× adult normal due to active bone growth — check GGT if liver etiology suspected."
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

// ============================================================================
// BLOCK 2 · CARD 3: ABG / VBG Rapid Matrix
// ============================================================================
@Composable
fun AbgVbgMatrixCard(unitSystem: LabUnitSystem) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_labs_abg_vbg"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "abg_normals") {
            KitSectionCard(
                title = "Normal Arterial Blood Gas (ABG)",
                badge = "${unitSystem.label} UNITS"
            ) {
                val normals = LabsFluidsKitData.abgNormals
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    normals.take(3).forEach { item ->
                        KitMetricTile(
                            label = item.parameter,
                            value = item.valueFor(unitSystem),
                            emphasized = item.parameter == "pH",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    normals.drop(3).forEach { item ->
                        KitMetricTile(
                            label = item.parameter,
                            value = item.valueFor(unitSystem),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        item(key = "abg_compensation") {
            KitSectionCard(title = "Primary Disorder & Compensation Matrix") {
                LabsFluidsKitData.abgDisorders.forEachIndexed { idx, row ->
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = row.disorder,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = row.primaryChange,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                )
                            }
                        }
                        Text(
                            text = row.compensationFormula,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = PlusJakartaFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontFeatureSettings = "tnum"
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (idx < LabsFluidsKitData.abgDisorders.lastIndex) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    }
                }
            }
        }

        item(key = "vbg_offsets") {
            KitSectionCard(title = "VBG-to-ABG Offset Pearls") {
                val offsets = LabsFluidsKitData.vbgOffsets(unitSystem)
                offsets.forEachIndexed { idx, (label, note) ->
                    KitKeyValueRow(
                        label = label,
                        value = note,
                        valueColor = MaterialTheme.colorScheme.onSurface
                    )
                    if (idx < offsets.lastIndex) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

// ============================================================================
// BLOCK 2 · CARD 4: Body Fluid Interpreters (CSF | Pleural | Ascitic | Synovial)
// ============================================================================
@Composable
fun BodyFluidsCard(unitSystem: LabUnitSystem) {
    var selectedFluid by rememberSaveable { mutableStateOf("csf") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_labs_body_fluids"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "fluid_tabs") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("csf", "CSF"),
                    ChoiceOption("pleural", "Pleural"),
                    ChoiceOption("ascitic", "Ascitic"),
                    ChoiceOption("synovial", "Synovial")
                ),
                selectedId = selectedFluid,
                onSelect = { selectedFluid = it },
                tagPrefix = "body_fluid"
            )
        }

        when (selectedFluid) {
            "csf" -> {
                item(key = "csf_profiles") {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        LabsFluidsKitData.csfProfiles.forEach { profile ->
                            val red = LocalMedWheelExtras.current.tierRed
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(22.dp),
                                color = if (profile.isUrgent) red.container.copy(alpha = 0.5f)
                                else MaterialTheme.colorScheme.surfaceContainerLow,
                                border = if (profile.isUrgent) BorderStroke(1.2.dp, red.badge.copy(alpha = 0.5f)) else null
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = profile.condition,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                            color = if (profile.isUrgent) red.badge else MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = "OP: ${profile.pressureCmH2O} cmH₂O",
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        FluidMiniCell("WBC & Diff", profile.wbcAndDiff, Modifier.weight(1f))
                                        FluidMiniCell(
                                            "Protein",
                                            if (unitSystem == LabUnitSystem.SI) profile.proteinSi else profile.proteinConv,
                                            Modifier.weight(1f)
                                        )
                                    }
                                    FluidMiniCell(
                                        "Glucose / CSF:Serum Ratio",
                                        if (unitSystem == LabUnitSystem.SI) profile.glucoseSi else profile.glucoseConv,
                                        Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }

            "pleural" -> {
                item(key = "pleural_lights") {
                    KitSectionCard(
                        title = "Light's Criteria (Exudate if ANY 1 Met)",
                        badge = "EXUDATE RULE"
                    ) {
                        LabsFluidsKitData.lightsCriteria.forEachIndexed { idx, criterion ->
                            Text(
                                text = "${idx + 1}. $criterion",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                item(key = "pleural_causes") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        KitSectionCard(title = "Transudate", modifier = Modifier.weight(1f)) {
                            LabsFluidsKitData.pleuralTransudateCauses.forEach { c ->
                                Text("• $c", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                        KitSectionCard(title = "Exudate", modifier = Modifier.weight(1f)) {
                            LabsFluidsKitData.pleuralExudateCauses.forEach { c ->
                                Text("• $c", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
                item(key = "pleural_empyema") {
                    KitRedFlagStrip(
                        title = "Empyema / Complicated Parapneumonic",
                        bullets = LabsFluidsKitData.empyemaMarkers(unitSystem)
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }

            "ascitic" -> {
                item(key = "ascitic_sbp") {
                    KitRedFlagStrip(
                        title = "SBP Diagnostic Cutoff",
                        bullets = listOf(
                            "Ascitic PMN ≥ 250 cells/mm³ = Spontaneous Bacterial Peritonitis (start empiric 3rd-gen cephalosporin).",
                            "If bloody tap (>10,000 RBCs): subtract 1 PMN per 250 RBCs."
                        )
                    )
                }
                item(key = "ascitic_saag_high") {
                    KitSectionCard(
                        title = LabsFluidsKitData.asciticSaagHighTitle(unitSystem),
                        badge = "PORTAL HTN"
                    ) {
                        LabsFluidsKitData.saagHighCauses.forEach { c ->
                            Text("• $c", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
                item(key = "ascitic_saag_low") {
                    KitSectionCard(
                        title = LabsFluidsKitData.asciticSaagLowTitle(unitSystem),
                        badge = "NON-PORTAL"
                    ) {
                        LabsFluidsKitData.saagLowCauses.forEach { c ->
                            Text("• $c", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }

            "synovial" -> {
                item(key = "synovial_table") {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        LabsFluidsKitData.synovialProfiles.forEach { row ->
                            val red = LocalMedWheelExtras.current.tierRed
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                color = if (row.isUrgent) red.container.copy(alpha = 0.65f)
                                else MaterialTheme.colorScheme.surfaceContainerLow,
                                border = if (row.isUrgent) BorderStroke(1.5.dp, red.badge.copy(alpha = 0.65f)) else null
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = row.category,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                        color = if (row.isUrgent) red.badge else MaterialTheme.colorScheme.primary
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        FluidMiniCell("WBC (/mm³)", row.wbcRange, Modifier.weight(1f))
                                        FluidMiniCell("PMN %", row.pmnPercent, Modifier.weight(0.75f))
                                        FluidMiniCell("Fluid", row.appearance, Modifier.weight(1.1f))
                                    }
                                }
                            }
                        }
                    }
                }
                item(key = "synovial_crystals") {
                    KitInfoStrip(
                        title = "Polarized Microscopy Crystal Key",
                        bullets = listOf(
                            "Gout (MSU): Needle-shaped, NEGATIVELY birefringent (yellow when parallel).",
                            "Pseudogout (CPPD): Rhomboid-shaped, POSITIVELY birefringent (blue when parallel).",
                            "Crystals do NOT rule out concurrent septic arthritis — always send Gram stain & culture."
                        )
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun FluidMiniCell(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.72f)
    ) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = PlusJakartaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontFeatureSettings = "tnum"
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
