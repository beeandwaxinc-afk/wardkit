package com.example.ui.kit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.WarningAmber
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChoiceOption
import com.example.data.kit.AdultErAndInfusionData
import com.example.data.kit.DoseEngineMath
import com.example.data.kit.InfusionDrugDef
import com.example.data.kit.LaAndPeadsCoreData
import com.example.data.kit.LocalAnestheticDef
import com.example.ui.components.ConnectedPillRow
import com.example.ui.theme.LocalMedWheelExtras
import com.example.ui.theme.PlusJakartaFontFamily

// ============================================================================
// WARD & ICU INFUSION CARDS (#1–#10) & LOCAL ANESTHETICS / LAST CARDS
// ============================================================================

@Composable
fun ContinuousInfusionCard(
    infusion: InfusionDrugDef,
    weightKg: Double?,
    highlighted: Boolean = false
) {
    var selectedDilutionId by rememberSaveable(infusion.id) { mutableStateOf(infusion.dilutions.first().id) }
    var targetDoseText by rememberSaveable(infusion.id) { mutableStateOf("") }

    val dilution = infusion.dilutions.firstOrNull { it.id == selectedDilutionId }
        ?: infusion.dilutions.first()
    val doseVal = targetDoseText.trim().replace(',', '.').toDoubleOrNull()
    val mlHr = doseVal?.let {
        DoseEngineMath.computeInfusionMlHr(
            doseValue = it,
            weightKg = weightKg,
            dilution = dilution,
            weightIndependent = infusion.weightIndependent
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dose_card_${infusion.id}"),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = if (highlighted) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = infusion.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = if (infusion.weightIndependent) "FIXED · ${infusion.unitLabel}" else infusion.unitLabel,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            // Dilution pills
            KitScrollableChipRow(
                items = infusion.dilutions.map { it.id to it.label },
                selectedId = dilution.id,
                onSelect = { selectedDilutionId = it },
                tagPrefix = "dil_${infusion.id}",
                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
            )

            // Preset dose chips
            KitScrollableChipRow(
                items = infusion.presetDoses.map { d ->
                    val label = DoseEngineMath.fmt(d, if (d < 0.1) 2 else if (d < 10) 1 else 0)
                    label to "$label ${infusion.unitLabel}"
                },
                selectedId = doseVal?.let { d ->
                    DoseEngineMath.fmt(d, if (d < 0.1) 2 else if (d < 10) 1 else 0)
                } ?: "",
                onSelect = { targetDoseText = it },
                tagPrefix = "preset_${infusion.id}",
                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
            )

            // Custom blank Target Dose input + Live mL/hr readout side-by-side
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = targetDoseText,
                    onValueChange = { targetDoseText = sanitizeDecimalInput(it) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("inf_dose_${infusion.id}"),
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Target ${infusion.unitLabel}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    textStyle = TextStyle(
                        fontFamily = PlusJakartaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        fontFeatureSettings = "tnum",
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    trailingIcon = {
                        if (targetDoseText.isNotEmpty()) {
                            IconButton(onClick = { targetDoseText = "" }) {
                                Icon(Icons.Rounded.Close, contentDescription = "Clear dose", modifier = Modifier.size(18.dp))
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.7f),
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                Surface(
                    modifier = Modifier.weight(0.9f),
                    shape = RoundedCornerShape(16.dp),
                    color = if (mlHr != null) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceContainerHigh
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "PUMP RATE",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = if (mlHr != null) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = when {
                                mlHr != null -> "${DoseEngineMath.fmt(mlHr, 1)} mL/hr"
                                !infusion.weightIndependent && weightKg == null -> "Enter weight"
                                else -> "Pick dose"
                            },
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = PlusJakartaFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = if (mlHr != null) 21.sp else 14.sp,
                                fontFeatureSettings = "tnum"
                            ),
                            color = if (mlHr != null) MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Text(
                text = infusion.pearl,
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 17.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun InsulinDripCard(weightKg: Double?, highlighted: Boolean = false) {
    var rateMode by rememberSaveable { mutableStateOf("std") } // 0.1 vs 0.05
    val uPerKgHr = if (rateMode == "std") 0.1 else 0.05
    val unitsHr = weightKg?.let { it * uPerKgHr }
    val amber = LocalMedWheelExtras.current.tierAmber

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dose_card_icu_insulin_drip"),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = if (highlighted) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "IV Regular Insulin Infusion",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, fontSize = 17.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.primaryContainer) {
                    Text(
                        text = "1 U/mL (50U/50mL)",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("std", "DKA 0.1 U/kg/hr"),
                    ChoiceOption("red", "Reduced 0.05 U/kg/hr")
                ),
                selectedId = rateMode,
                onSelect = { rateMode = it },
                tagPrefix = "insulin_rate"
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LIVE RATE (1 U/mL)",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                    Text(
                        text = if (unitsHr != null) {
                            "${DoseEngineMath.fmt(unitsHr, 1)} U/hr  ·  ${DoseEngineMath.fmt(unitsHr, 1)} mL/hr"
                        } else {
                            "Enter weight"
                        },
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = PlusJakartaFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = if (unitsHr != null) 20.sp else 15.sp,
                            fontFeatureSettings = "tnum"
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            if (unitsHr != null && unitsHr > 15.0) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = amber.container,
                    border = BorderStroke(1.dp, amber.badge.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⚠ Sanity Check: >15 U/hr — verify patient weight & local DKA max cap.",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = amber.onContainer,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            Text(
                text = "Standard 50 U/50 mL or 100 U/100 mL NS · Confirm K⁺ ≥3.3 mEq/L before starting; check hourly CBG.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun MagSulfateCard(highlighted: Boolean = false) {
    var regId by rememberSaveable { mutableStateOf(AdultErAndInfusionData.magSulfateRegimens.first().id) }
    var concId by rememberSaveable { mutableStateOf("50") }
    val reg = AdultErAndInfusionData.magSulfateRegimens.firstOrNull { it.id == regId }
        ?: AdultErAndInfusionData.magSulfateRegimens.first()
    val concMgPerMl = if (concId == "50") 500.0 else 200.0
    val drawUpMl = (reg.doseGrams * 1000.0) / concMgPerMl

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dose_card_icu_mag_sulfate"),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = if (highlighted) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Magnesium Sulfate (IV Regimens)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, fontSize = 17.sp),
                color = MaterialTheme.colorScheme.onSurface
            )

            KitScrollableChipRow(
                items = AdultErAndInfusionData.magSulfateRegimens.map { it.id to it.label },
                selectedId = reg.id,
                onSelect = { regId = it },
                tagPrefix = "mgso4_reg",
                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
            )

            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("50", "50% (500 mg/mL)"),
                    ChoiceOption("20", "20% (200 mg/mL)")
                ),
                selectedId = concId,
                onSelect = { concId = it },
                tagPrefix = "mgso4_conc"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                KitMetricTile(
                    label = "Draw-Up Volume",
                    value = "${DoseEngineMath.fmt(drawUpMl, 1)} mL",
                    unit = "of ${concId}% MgSO₄",
                    emphasized = true,
                    modifier = Modifier.weight(1f)
                )
                KitMetricTile(
                    label = "Order",
                    value = "${DoseEngineMath.fmt(reg.doseGrams, 0)} g",
                    unit = reg.timingNote,
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = "Hold if patellar reflex absent, RR <12/min, or UO <30 mL/hr · Antidote = IV 10% Calcium Gluconate.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SteroidEquivalenceCard(highlighted: Boolean = false) {
    var sourceId by rememberSaveable { mutableStateOf("pred") }
    var amountText by rememberSaveable { mutableStateOf("") }
    val amountMg = amountText.trim().replace(',', '.').toDoubleOrNull()
    val equivalents = amountMg?.takeIf { it > 0.0 }?.let {
        DoseEngineMath.computeSteroidEquivalents(sourceId, it)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dose_card_icu_steroid_equiv"),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = if (highlighted) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quick Ward Steroid Equivalence",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, fontSize = 17.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "20 : 5 : 4 : 0.75",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            ConnectedPillRow(
                options = DoseEngineMath.steroidEquivalenceTable.map { ChoiceOption(it.id, it.shortName) },
                selectedId = sourceId,
                onSelect = { sourceId = it },
                tagPrefix = "steroid_src"
            )

            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = sanitizeDecimalInput(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("steroid_mg_input"),
                singleLine = true,
                placeholder = { Text("Enter dose in mg") },
                trailingIcon = {
                    if (amountText.isNotEmpty()) {
                        IconButton(onClick = { amountText = "" }) {
                            Icon(Icons.Rounded.Close, contentDescription = "Clear steroid dose")
                        }
                    } else {
                        Text("mg", modifier = Modifier.padding(end = 12.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                textStyle = TextStyle(
                    fontFamily = PlusJakartaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    fontFeatureSettings = "tnum",
                    color = MaterialTheme.colorScheme.onSurface
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.7f),
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                DoseEngineMath.steroidEquivalenceTable.forEach { row ->
                    val eqVal = equivalents?.firstOrNull { it.first.id == row.id }?.second
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        color = if (row.id == sourceId) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceContainerHigh
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = row.shortName,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = if (eqVal != null) "${DoseEngineMath.fmt(eqVal, if (eqVal < 10) 2 else 1)}" else "—",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontFamily = PlusJakartaFontFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFeatureSettings = "tnum"
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "mg",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LocalAnestheticCard(
    la: LocalAnestheticDef,
    weightKg: Double?,
    highlighted: Boolean = false
) {
    var selectedRegId by rememberSaveable(la.id) { mutableStateOf(la.regimens.first().id) }
    var selectedPctId by rememberSaveable(la.id) { mutableStateOf(la.percentOptions.first().id) }

    val regimen = la.regimens.firstOrNull { it.id == selectedRegId } ?: la.regimens.first()
    val pct = la.percentOptions.firstOrNull { it.id == selectedPctId } ?: la.percentOptions.first()
    val res = weightKg?.let { DoseEngineMath.computeLocalAnesthetic(it, regimen, pct) }
    val amber = LocalMedWheelExtras.current.tierAmber
    val red = LocalMedWheelExtras.current.tierRed

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dose_card_${la.id}"),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = if (highlighted) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = la.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, fontSize = 17.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${DoseEngineMath.fmt(regimen.mgPerKg, 1)} mg/kg · Max ${DoseEngineMath.fmt(regimen.maxMg, 0)} mg",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (la.regimens.size > 1) {
                ConnectedPillRow(
                    options = la.regimens.map { ChoiceOption(it.id, it.label) },
                    selectedId = regimen.id,
                    onSelect = { selectedRegId = it },
                    tagPrefix = "la_reg_${la.id}"
                )
            }

            ConnectedPillRow(
                options = la.percentOptions.map { ChoiceOption(it.id, it.label) },
                selectedId = pct.id,
                onSelect = { selectedPctId = it },
                tagPrefix = "la_pct_${la.id}"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                KitMetricTile(
                    label = "Max Safe Volume",
                    value = if (res != null) "${DoseEngineMath.fmtVol(res.maxSafeMl)} mL" else "Enter weight",
                    unit = pct.label,
                    emphasized = res != null,
                    modifier = Modifier.weight(1f)
                )
                KitMetricTile(
                    label = "Max Safe Dose",
                    value = if (res != null) "${DoseEngineMath.fmtDose(res.maxSafeMg)} mg" else "—",
                    unit = "Hard cap ${DoseEngineMath.fmt(regimen.maxMg, 0)} mg",
                    modifier = Modifier.weight(1f)
                )
            }

            if (res?.isCapped == true) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = amber.container,
                    border = BorderStroke(1.dp, amber.badge.copy(alpha = 0.55f))
                ) {
                    Text(
                        text = "⚠ CAPPED AT ADULT MAX (${DoseEngineMath.fmt(res.hardCapMg, 0)} mg)",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = amber.onContainer,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Text(
                text = la.pearl,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = if (la.isRedFlagPearl) FontWeight.Bold else FontWeight.Medium
                ),
                color = if (la.isRedFlagPearl) red.badge else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun IntralipidLastCard(weightKg: Double?, highlighted: Boolean = false) {
    val red = LocalMedWheelExtras.current.tierRed
    val res = weightKg?.let { DoseEngineMath.computeIntralipidLast(it) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dose_card_la_intralipid_last"),
        shape = RoundedCornerShape(24.dp),
        color = red.container,
        border = BorderStroke(if (highlighted) 2.5.dp else 1.5.dp, red.badge)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Rounded.WarningAmber, contentDescription = null, tint = red.badge, modifier = Modifier.size(22.dp))
                Column {
                    Text(
                        text = "20% INTRALIPID · LAST RESCUE",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = red.badge
                    )
                    Text(
                        text = "Local Anesthetic Systemic Toxicity",
                        style = MaterialTheme.typography.labelSmall,
                        color = red.onContainer.copy(alpha = 0.85f)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                KitMetricTile(
                    label = "Initial Bolus (2–3m)",
                    value = if (res != null) "${DoseEngineMath.fmt(res.bolusMl, 1)} mL" else "1.5 mL/kg",
                    unit = if (res?.isBolusCapped == true) "⚠ Capped at 100 mL (>70 kg)" else "Max 100 mL (>70 kg)",
                    modifier = Modifier.weight(1f)
                )
                KitMetricTile(
                    label = "Absolute Max Total",
                    value = if (res != null) "${DoseEngineMath.fmt(res.absoluteMaxMl, 0)} mL" else "12 mL/kg",
                    unit = "Total 20% lipid emulsion",
                    modifier = Modifier.weight(1f)
                )
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.82f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "CONTINUOUS INFUSION (0.25 mL/kg/min)",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = red.badge
                    )
                    Text(
                        text = if (res != null) {
                            "${DoseEngineMath.fmt(res.infusionMlPerMin, 1)} mL/min  ·  ${DoseEngineMath.fmt(res.infusionMlPerHr, 0)} mL/hr"
                        } else {
                            "Enter weight for mL/min & mL/hr (250 mL / 15–20m if >70 kg)"
                        },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = PlusJakartaFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontFeatureSettings = "tnum"
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (weightKg != null && weightKg > 70.0) {
                        Text(
                            text = "Note (>70 kg adult): May run 250 mL bag over 15–20 min.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            HorizontalDivider(color = red.badge.copy(alpha = 0.25f))

            LaAndPeadsCoreData.lastFiveStepChecklist.forEach { step ->
                Text(
                    text = step,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = 18.sp
                    ),
                    color = red.onContainer
                )
            }
        }
    }
}

internal fun sanitizeDecimalInput(input: String): String {
    val normalized = input.replace(',', '.')
    val sb = StringBuilder()
    var hasDot = false
    for (ch in normalized) {
        when {
            ch.isDigit() -> sb.append(ch)
            ch == '.' && !hasDot -> {
                hasDot = true
                sb.append(ch)
            }
        }
    }
    return sb.toString().take(8)
}
