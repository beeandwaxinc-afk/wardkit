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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChoiceOption
import com.example.data.kit.ErCrashProtocolsData
import com.example.data.kit.ProtocolStage
import com.example.ui.components.ConnectedPillRow
import com.example.ui.theme.LocalMedWheelExtras
import com.example.ui.theme.PlusJakartaFontFamily

// ============================================================================
// ER CRASH CARD 1: ACLS & PALS Arrest Algorithms
// ============================================================================
@Composable
fun ErAclsPalsCard() {
    var modeId by rememberSaveable { mutableStateOf("acls") }
    val algo = if (modeId == "acls") ErCrashProtocolsData.adultAcls else ErCrashProtocolsData.pedsPals
    val red = LocalMedWheelExtras.current.tierRed

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_er_acls_pals"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "acls_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("acls", "Adult ACLS"),
                    ChoiceOption("pals", "Pediatric PALS")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "er_arrest_mode"
            )
        }

        item(key = "cpr_strip") {
            KitSectionCard(
                title = "High-Quality CPR · ${algo.label}",
                badge = "CONTINUOUS",
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)
            ) {
                val rows = algo.cprMetrics.chunked(2)
                rows.forEach { pair ->
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

        item(key = "shockable_pathway") {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = red.container.copy(alpha = 0.6f),
                border = BorderStroke(1.5.dp, red.badge.copy(alpha = 0.55f))
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
                            text = "SHOCKABLE · VF / pVT",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.8.sp
                            ),
                            color = red.badge
                        )
                        Surface(shape = RoundedCornerShape(50), color = red.badge) {
                            Text(
                                text = "DEFIB STAT",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                            )
                        }
                    }
                    algo.shockableSteps.forEachIndexed { idx, step ->
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = step.title,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                color = red.onContainer
                            )
                            Text(
                                text = step.detail,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = PlusJakartaFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 19.sp
                                ),
                                color = red.onContainer.copy(alpha = 0.9f)
                            )
                        }
                        if (idx < algo.shockableSteps.lastIndex) {
                            HorizontalDivider(color = red.badge.copy(alpha = 0.22f))
                        }
                    }
                }
            }
        }

        item(key = "non_shockable_pathway") {
            KitSectionCard(
                title = "Non-Shockable · Asystole / PEA",
                badge = "ADRENALINE ASAP"
            ) {
                algo.nonShockableSteps.forEachIndexed { idx, step ->
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = step.title,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = step.detail,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 19.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    if (idx < algo.nonShockableSteps.lastIndex) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    }
                }
            }
        }

        item(key = "reversible_5h_5t") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                KitSectionCard(title = "5 H's", modifier = Modifier.weight(1f)) {
                    ErCrashProtocolsData.fiveHs.forEach { item ->
                        Text(
                            text = "• $item",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                KitSectionCard(title = "5 T's", modifier = Modifier.weight(1f)) {
                    ErCrashProtocolsData.fiveTs.forEach { item ->
                        Text(
                            text = "• $item",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

// ============================================================================
// ER CRASH CARD 2: Anaphylaxis & Status Epilepticus Ladders
// ============================================================================
@Composable
fun ErAnaphylaxisSeizureCard() {
    var modeId by rememberSaveable { mutableStateOf("anaphylaxis") }
    val red = LocalMedWheelExtras.current.tierRed

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_er_anaphylaxis_seizure"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "anaph_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("anaphylaxis", "Anaphylaxis"),
                    ChoiceOption("seizure", "Status Epilepticus")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "er_anaph_mode"
            )
        }

        if (modeId == "anaphylaxis") {
            items(ErCrashProtocolsData.anaphylaxisStages, key = { it.stepTitle }) { stage ->
                ProtocolStageCard(stage)
            }
        } else {
            items(ErCrashProtocolsData.statusEpilepticusLadder, key = { it.timeWindow }) { stage ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    color = if (stage.isUrgent) red.container.copy(alpha = 0.55f)
                    else MaterialTheme.colorScheme.surfaceContainerLow,
                    border = if (stage.isUrgent) BorderStroke(1.5.dp, red.badge.copy(alpha = 0.5f)) else null
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = if (stage.isUrgent) red.badge else MaterialTheme.colorScheme.primary
                            ) {
                                Text(
                                    text = stage.timeWindow,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 11.dp, vertical = 4.dp)
                                )
                            }
                            Text(
                                text = stage.title,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                color = if (stage.isUrgent) red.onContainer else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 10.dp)
                            )
                        }
                        stage.bullets.forEach { bullet ->
                            Text(
                                text = "• $bullet",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 20.sp
                                ),
                                color = if (stage.isUrgent) red.onContainer else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        item(key = "anaph_bottom_spacer") {
            Spacer(Modifier.height(12.dp))
        }
    }
}

// ============================================================================
// ER CRASH CARD 3: DKA / HHS & Hyperkalemia Cocktails
// ============================================================================
@Composable
fun ErDkaHyperkalemiaCard() {
    var modeId by rememberSaveable { mutableStateOf("dka") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_er_dka_hyperkalemia"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "dka_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("dka", "DKA / HHS"),
                    ChoiceOption("hyperk", "Hyperkalemia")
                ),
                selectedId = modeId,
                onSelect = { modeId = it },
                tagPrefix = "er_dka_mode"
            )
        }

        if (modeId == "dka") {
            item(key = "dka_diag") {
                KitInfoStrip(
                    title = "Diagnostic Criteria Strip",
                    bullets = ErCrashProtocolsData.dkaDiagnosticBullets
                )
            }
            items(ErCrashProtocolsData.dkaStages, key = { it.stepTitle }) { stage ->
                ProtocolStageCard(stage)
            }
        } else {
            item(key = "hyperk_banner") {
                KitRedFlagStrip(
                    title = "Trigger Threshold",
                    bullets = listOf("Treat emergently for K⁺ ≥ 6.0 mEq/L OR ANY ECG changes (peaked T, PR prolongation, flat P, wide QRS, sine wave).")
                )
            }
            items(ErCrashProtocolsData.hyperkalemiaStages, key = { it.stepTitle }) { stage ->
                ProtocolStageCard(stage)
            }
        }

        item(key = "dka_spacer") {
            Spacer(Modifier.height(12.dp))
        }
    }
}

// ============================================================================
// ER CRASH CARD 4: Sepsis 1-Hour & Acute Emergency Bundles
// ============================================================================
@Composable
fun ErSepsisBundlesCard() {
    var bundleId by rememberSaveable { mutableStateOf("sepsis") }
    val steps = when (bundleId) {
        "sepsis" -> ErCrashProtocolsData.sepsisBundleSteps
        "scape" -> ErCrashProtocolsData.scapeBundleSteps
        else -> ErCrashProtocolsData.massiveBleedSteps
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_er_sepsis_bundles"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "bundle_toggle") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("sepsis", "Sepsis 1-Hr"),
                    ChoiceOption("scape", "Acute Pulm Edema"),
                    ChoiceOption("bleed", "Massive Bleed")
                ),
                selectedId = bundleId,
                onSelect = { bundleId = it },
                tagPrefix = "er_bundle_mode"
            )
        }

        items(steps, key = { "${bundleId}_${it.stepTitle}" }) { stage ->
            ProtocolStageCard(stage)
        }

        item(key = "bundle_spacer") {
            Spacer(Modifier.height(12.dp))
        }
    }
}

// ============================================================================
// ER CRASH CARD 5: Toxicology Antidotes Table (14 Rows + Inline Blank Filter)
// ============================================================================
@Composable
fun ErToxicologyCard() {
    var filterQuery by rememberSaveable { mutableStateOf("") }
    val filtered = remember(filterQuery) {
        val q = filterQuery.trim().lowercase()
        if (q.isEmpty()) ErCrashProtocolsData.antidotes
        else ErCrashProtocolsData.antidotes.filter { row ->
            row.toxin.lowercase().contains(q) ||
                row.antidote.lowercase().contains(q) ||
                row.initialDoseAndPearl.lowercase().contains(q)
        }
    }
    val red = LocalMedWheelExtras.current.tierRed

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_er_toxicology"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item(key = "tox_filter_bar") {
            OutlinedTextField(
                value = filterQuery,
                onValueChange = { filterQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("tox_filter_input"),
                singleLine = true,
                placeholder = {
                    Text(
                        text = "Filter toxin or antidote…",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (filterQuery.isNotEmpty()) {
                        IconButton(onClick = { filterQuery = "" }) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Clear filter",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.65f),
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Transparent
                )
            )
        }

        items(filtered, key = { it.id }) { row ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = if (row.isRedFlag) red.container.copy(alpha = 0.5f)
                else MaterialTheme.colorScheme.surfaceContainerLow,
                border = if (row.isRedFlag) BorderStroke(1.2.dp, red.badge.copy(alpha = 0.45f)) else null
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = row.toxin,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (row.isRedFlag) red.badge else MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = row.antidote,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                                color = if (row.isRedFlag) Color.White else MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Text(
                        text = row.initialDoseAndPearl,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            lineHeight = 18.sp
                        ),
                        color = if (row.isRedFlag) red.onContainer else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item(key = "tox_spacer") {
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ProtocolStageCard(stage: ProtocolStage) {
    if (stage.isRedFlag) {
        KitRedFlagStrip(
            title = "${stage.stepTitle} · ${stage.badge}",
            bullets = stage.bullets
        )
    } else {
        KitSectionCard(title = stage.stepTitle, badge = stage.badge) {
            stage.bullets.forEach { bullet ->
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
}
