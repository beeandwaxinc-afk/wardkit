package com.example.ui.kit

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.kit.IvBloodKitData
import com.example.ui.theme.PlusJakartaFontFamily

// ============================================================================
// BLOCK 3 · CARD 1: IV Fluid Composition Table
// ============================================================================
@Composable
fun IvFluidsTableCard() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_iv_fluids"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(IvBloodKitData.ivFluids, key = { it.name }) { fluid ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLow
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = fluid.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 17.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = fluid.tonicity,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "${fluid.osmolality} mOsm/L",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = PlusJakartaFontFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFeatureSettings = "tnum"
                                ),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ElectrolytePill("Na⁺", fluid.na, Modifier.weight(1f))
                        ElectrolytePill("K⁺", fluid.k, Modifier.weight(0.8f))
                        ElectrolytePill("Cl⁻", fluid.cl, Modifier.weight(1f))
                        ElectrolytePill("Buffer / Dex", fluid.bufferOrDex, Modifier.weight(1.45f))
                    }

                    Text(
                        text = fluid.wardUseAndCaution,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            lineHeight = 18.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        item(key = "iv_fluids_spacer") {
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ElectrolytePill(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontFamily = PlusJakartaFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontFeatureSettings = "tnum"
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// ============================================================================
// BLOCK 3 · CARD 2: IV Cannula Sizing Chart (Color-coded visual strips)
// ============================================================================
@Composable
fun IvCannulasCard() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_iv_cannulas"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(IvBloodKitData.ivCannulas, key = { it.gauge }) { cannula ->
            val swatch = Color(cannula.colorHex)
            val onSwatch = if (cannula.gauge == "24G") Color(0xFF1A1400) else Color.White

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLow
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Color-coded physical gauge pill
                    Box(
                        modifier = Modifier
                            .size(width = 66.dp, height = 58.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(swatch),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = cannula.gauge,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontFamily = PlusJakartaFontFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 20.sp
                                ),
                                color = onSwatch
                            )
                            Text(
                                text = cannula.colorName,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                ),
                                color = onSwatch.copy(alpha = 0.9f)
                            )
                        }
                    }

                    Spacer(Modifier.width(14.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = swatch.copy(alpha = 0.16f)
                        ) {
                            Text(
                                text = cannula.flowRate,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontFamily = PlusJakartaFontFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFeatureSettings = "tnum"
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                            )
                        }
                        Text(
                            text = cannula.indications,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium,
                                lineHeight = 18.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        item(key = "cannula_spacer") {
            Spacer(Modifier.height(12.dp))
        }
    }
}

// ============================================================================
// BLOCK 3 · CARD 3: Tubes & Catheters by Age
// ============================================================================
@Composable
fun TubesCathetersCard() {
    var selectedAgeId by rememberSaveable { mutableStateOf("adult") }
    val group = IvBloodKitData.tubeCatheterGroups.firstOrNull { it.id == selectedAgeId }
        ?: IvBloodKitData.tubeCatheterGroups.last()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_iv_tubes_catheters"),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "tube_age_chips") {
            KitScrollableChipRow(
                items = IvBloodKitData.tubeCatheterGroups.map { it.id to it.ageLabel },
                selectedId = selectedAgeId,
                onSelect = { selectedAgeId = it },
                tagPrefix = "tube_age"
            )
        }

        item(key = "tube_tiles") {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "${group.ageLabel.uppercase()} (${group.weightHint})",
                    style = MaterialTheme.typography.labelLarge.copy(
                        letterSpacing = 0.8.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 4.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KitMetricTile(
                        label = "Foley Catheter",
                        value = group.foleyFr,
                        emphasized = true,
                        modifier = Modifier.weight(1f)
                    )
                    KitMetricTile(
                        label = "NG Tube",
                        value = group.ngTubeFr,
                        modifier = Modifier.weight(1f)
                    )
                }

                KitMetricTile(
                    label = "Chest Drain (Thoracostomy)",
                    value = group.chestDrainFr,
                    modifier = Modifier.fillMaxWidth()
                )

                KitMetricTile(
                    label = "LMA Size (by Weight)",
                    value = group.lmaSize,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item(key = "foley_formula") {
            KitInfoStrip(
                title = "Quick Pediatric Sizing Formulas",
                bullets = listOf(
                    "Pediatric Foley / NG (Fr) ≈ 2 × uncuffed ETT size (or Age/2 + 8).",
                    "Pediatric Chest Drain (Fr) ≈ 4 × ETT size.",
                    "LMA weight bands: <5 kg = 1 · 5–10 kg = 1.5 · 10–20 kg = 2 · 20–30 kg = 2.5 · 30–50 kg = 3 · 50–70 kg = 4 · >70 kg = 5."
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

// ============================================================================
// BLOCK 3 · CARD 4: Blood Products & Transfusion Guide
// ============================================================================
@Composable
fun BloodProductsCard() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_iv_blood_products"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(IvBloodKitData.bloodProducts, key = { it.name }) { product ->
            KitSectionCard(title = product.name) {
                KitKeyValueRow(
                    label = "Volume / Dose",
                    value = product.volumeAndDose,
                    valueColor = MaterialTheme.colorScheme.primary
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                KitKeyValueRow(
                    label = "Expected Response",
                    value = product.expectedRise
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                KitKeyValueRow(
                    label = "Infusion Rule",
                    value = product.infusionRule
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                KitKeyValueRow(
                    label = "Key Indication",
                    value = product.keyIndication
                )
            }
        }

        item(key = "transfusion_reaction") {
            KitRedFlagStrip(
                title = "Acute Transfusion Reaction Protocol",
                bullets = IvBloodKitData.transfusionReactionSteps.map { (step, detail) ->
                    "$step — $detail"
                }
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}
