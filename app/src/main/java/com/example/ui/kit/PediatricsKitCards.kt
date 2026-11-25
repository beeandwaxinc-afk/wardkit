package com.example.ui.kit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.VerifiedUser
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChoiceOption
import com.example.data.kit.PediatricsKitData
import com.example.ui.components.ConnectedPillRow
import com.example.ui.theme.LocalMedWheelExtras
import com.example.ui.theme.PlusJakartaFontFamily

// ============================================================================
// BLOCK 1 · CARD 1: Age Weight, Height, OFC & Vitals Lookup
// ============================================================================
@Composable
fun PedsGrowthVitalsCard() {
    var selectedId by rememberSaveable { mutableStateOf(PediatricsKitData.growthVitalsRows[3].id) } // Default 1 yr
    val row = PediatricsKitData.growthVitalsRows.firstOrNull { it.id == selectedId }
        ?: PediatricsKitData.growthVitalsRows.first()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_peds_growth_vitals"),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "age_chips") {
            KitScrollableChipRow(
                items = PediatricsKitData.growthVitalsRows.map { it.id to it.ageLabel },
                selectedId = selectedId,
                onSelect = { selectedId = it },
                tagPrefix = "peds_age"
            )
        }

        item(key = "anthro_grid") {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "GROWTH · ${row.ageLabel.uppercase()}",
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
                        label = "Weight",
                        value = row.weightKg,
                        emphasized = true,
                        modifier = Modifier.weight(1f)
                    )
                    KitMetricTile(
                        label = "Height",
                        value = row.heightCm,
                        modifier = Modifier.weight(1f)
                    )
                }
                KitMetricTile(
                    label = "Head Circumference (OFC)",
                    value = row.ofcCm,
                    unit = if (row.ofcCm == "—") "Routine OFC tracked up to 5 yr" else "Expected median OFC",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item(key = "vitals_grid") {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "NORMAL VITALS · ${row.ageLabel.uppercase()}",
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
                        label = "Heart Rate",
                        value = row.hrBpm,
                        unit = "bpm",
                        modifier = Modifier.weight(1f)
                    )
                    KitMetricTile(
                        label = "Resp Rate",
                        value = row.rrPerMin,
                        unit = "/min",
                        modifier = Modifier.weight(1f)
                    )
                }
                KitMetricTile(
                    label = "Systolic BP (SBP)",
                    value = row.sbpMmHg,
                    unit = "mmHg (awake normal range)",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item(key = "bedside_formulas") {
            KitSectionCard(
                title = "Bedside Estimation Formulas",
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                PediatricsKitData.bedsideFormulas.forEachIndexed { idx, rule ->
                    KitKeyValueRow(
                        label = rule.label,
                        value = rule.formula,
                        valueColor = MaterialTheme.colorScheme.primary
                    )
                    if (idx < PediatricsKitData.bedsideFormulas.lastIndex) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    }
                }
            }
        }
    }
}

// ============================================================================
// BLOCK 1 · CARD 2: Developmental Milestones & Red Flags
// ============================================================================
@Composable
fun PedsMilestonesCard() {
    var selectedId by rememberSaveable { mutableStateOf(PediatricsKitData.milestoneAges[2].id) } // Default 6 mo
    val milestone = PediatricsKitData.milestoneAges.firstOrNull { it.id == selectedId }
        ?: PediatricsKitData.milestoneAges.first()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_peds_milestones"),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "milestone_chips") {
            KitScrollableChipRow(
                items = PediatricsKitData.milestoneAges.map { it.id to it.ageLabel },
                selectedId = selectedId,
                onSelect = { selectedId = it },
                tagPrefix = "milestone_age"
            )
        }

        item(key = "milestone_domains") {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MilestoneDomainRow("Gross Motor", milestone.grossMotor)
                MilestoneDomainRow("Fine Motor", milestone.fineMotor)
                MilestoneDomainRow("Language", milestone.language)
                MilestoneDomainRow("Social / Adaptive", milestone.socialAdaptive)
            }
        }

        item(key = "milestone_red_flag") {
            KitRedFlagStrip(
                title = "Red Flags · ${milestone.ageLabel}",
                bullets = listOf(
                    milestone.redFlag,
                    "Loss of ANY previously acquired skill (regression) at ANY age = urgent referral."
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun MilestoneDomainRow(domain: String, description: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = domain.uppercase(),
                style = MaterialTheme.typography.labelMedium.copy(
                    letterSpacing = 0.7.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    lineHeight = 22.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// ============================================================================
// BLOCK 1 · CARD 3: WHO Dehydration Plans A, B & C
// ============================================================================
@Composable
fun PedsWhoDehydrationCard() {
    var selectedPlanId by rememberSaveable { mutableStateOf("plan_a") }
    val plan = PediatricsKitData.whoPlans.firstOrNull { it.id == selectedPlanId }
        ?: PediatricsKitData.whoPlans.first()
    val extras = LocalMedWheelExtras.current

    val severityColors = when (plan.id) {
        "plan_a" -> extras.tierGreen
        "plan_b" -> extras.tierAmber
        else -> extras.tierRed
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_peds_who_dehydration"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "who_plan_selector") {
            ConnectedPillRow(
                options = PediatricsKitData.whoPlans.map { ChoiceOption(it.id, it.shortTitle) },
                selectedId = selectedPlanId,
                onSelect = { selectedPlanId = it },
                tagPrefix = "who_plan"
            )
        }

        item(key = "who_banner") {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = severityColors.container,
                border = BorderStroke(1.5.dp, severityColors.badge.copy(alpha = 0.55f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = plan.shortTitle,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                            color = severityColors.onContainer
                        )
                        Text(
                            text = plan.classification,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = severityColors.onContainer.copy(alpha = 0.85f)
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = severityColors.badge
                    ) {
                        Text(
                            text = plan.severityTag,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.surfaceContainerLowest,
                            modifier = Modifier.padding(horizontal = 11.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }

        item(key = "who_signs") {
            KitSectionCard(title = "Key Clinical Signs") {
                plan.signs.forEachIndexed { idx, sign ->
                    KitKeyValueRow(
                        label = sign.feature,
                        value = sign.finding,
                        valueColor = severityColors.badge
                    )
                    if (idx < plan.signs.lastIndex) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    }
                }
            }
        }

        if (plan.ivProtocolRows.isNotEmpty()) {
            item(key = "who_iv_split") {
                KitSectionCard(
                    title = "IV Ringer's Lactate (100 mL/kg Total)",
                    badge = "PLAN C",
                    containerColor = severityColors.container.copy(alpha = 0.55f)
                ) {
                    plan.ivProtocolRows.forEachIndexed { idx, (age, schedule) ->
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = age,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                                color = severityColors.badge
                            )
                            Text(
                                text = schedule,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontFamily = PlusJakartaFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontFeatureSettings = "tnum"
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (idx < plan.ivProtocolRows.lastIndex) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
                        }
                    }
                }
            }
        }

        item(key = "who_management") {
            KitSectionCard(title = "Management Protocol") {
                plan.managementSteps.forEach { step ->
                    Text(
                        text = if (step.startsWith("•")) step else "• $step",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            lineHeight = 20.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                plan.zincNote?.let { zinc ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = zinc,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

        if (plan.showOrsAgeTable) {
            item(key = "who_plan_b_table") {
                KitSectionCard(
                    title = "WHO Plan B · 4-Hour ORS by Age",
                    badge = "IF WT UNKNOWN"
                ) {
                    PediatricsKitData.whoPlanBAgeTable.forEachIndexed { idx, r ->
                        KitKeyValueRow(
                            label = "${r.age} (${r.weight})",
                            value = r.volumeMl,
                            valueColor = MaterialTheme.colorScheme.primary
                        )
                        if (idx < PediatricsKitData.whoPlanBAgeTable.lastIndex) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                        }
                    }
                }
            }
        }

        item(key = "who_danger") {
            KitRedFlagStrip(
                title = "Clinical Caution / Danger Signs",
                bullets = listOf(plan.dangerNote)
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

// ============================================================================
// BLOCK 1 · CARD 4: Neonatal Quick Card
// ============================================================================
@Composable
fun PedsNeonatalCard() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_peds_neonatal"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "neo_fluids") {
            KitSectionCard(
                title = "Daily Maintenance Fluids",
                badge = "mL/kg/day"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PediatricsKitData.neonatalDailyFluids.forEach { item ->
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = item.day,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = item.mlPerKgDay,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontFamily = PlusJakartaFontFamily,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 19.sp,
                                        fontFeatureSettings = "tnum"
                                    ),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
                Text(
                    text = "• Add +10–20 mL/kg/day under phototherapy or radiant warmer.\n• Preterm neonates start higher (~80 mL/kg/day Day 1).",
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item(key = "neo_hypoglycemia") {
            KitSectionCard(
                title = "Neonatal Hypoglycemia",
                badge = "<45 mg/dL · <2.5 mmol/L"
            ) {
                KitKeyValueRow(
                    label = "Threshold",
                    value = "< 45 mg/dL (< 2.5 mmol/L)",
                    valueColor = LocalMedWheelExtras.current.tierRed.badge
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                KitKeyValueRow(
                    label = "Symptomatic / Severe Bolus",
                    value = "2 mL/kg IV 10% Dextrose",
                    valueColor = MaterialTheme.colorScheme.primary
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                KitKeyValueRow(
                    label = "Maintenance Infusion",
                    value = "GIR 6–8 mg/kg/min (recheck 30 min)"
                )
            }
        }

        item(key = "neo_resus_spo2") {
            KitSectionCard(
                title = "Newborn Resuscitation · Pre-Ductal SpO₂",
                badge = "PPV: 21% FiO₂ (≥35w)"
            ) {
                val chunks = PediatricsKitData.neonatalSpo2Targets.chunked(3)
                chunks.forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowItems.forEach { target ->
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.surfaceContainerHigh
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = target.minute,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = target.target,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontFamily = PlusJakartaFontFamily,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontFeatureSettings = "tnum"
                                        ),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
                Text(
                    text = "• Measure pre-ductal SpO₂ on right hand/wrist; start PPV at 21% FiO₂ (≥35 wks).",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item(key = "neo_jaundice") {
            KitRedFlagStrip(
                title = "Jaundice Red Flags",
                bullets = PediatricsKitData.jaundiceRedFlags
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

// ============================================================================
// BLOCK 1 · CARD 5: Immunization Schedule
// ============================================================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PedsImmunizationCard() {
    var scheduleId by rememberSaveable { mutableStateOf("epi_who") }
    val rows = if (scheduleId == "epi_who") {
        PediatricsKitData.epiWhoSchedule
    } else {
        PediatricsKitData.ukAusNipSchedule
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("card_peds_immunization"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "vaccine_protocol_chip") {
            Surface(
                shape = RoundedCornerShape(50),
                color = LocalMedWheelExtras.current.tierAmber.container,
                border = BorderStroke(1.dp, LocalMedWheelExtras.current.tierAmber.badge.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.VerifiedUser,
                        contentDescription = null,
                        tint = LocalMedWheelExtras.current.tierAmber.badge,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Protocol Reference — Verify Local Guideline",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = LocalMedWheelExtras.current.tierAmber.onContainer
                    )
                }
            }
        }

        item(key = "vaccine_selector") {
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption("epi_who", "EPI / WHO"),
                    ChoiceOption("uk_aus", "UK / Aus NIP")
                ),
                selectedId = scheduleId,
                onSelect = { scheduleId = it },
                tagPrefix = "vaccine_schedule"
            )
        }

        item(key = "vaccine_table") {
            KitSectionCard(
                title = if (scheduleId == "epi_who") "EPI / WHO Routine Schedule" else "UK / Aus NIP Summary"
            ) {
                rows.forEachIndexed { idx, row ->
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = row.age,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            row.vaccines.forEach { vax ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surfaceContainerHigh
                                ) {
                                    Box(modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp)) {
                                        Text(
                                            text = vax,
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (idx < rows.lastIndex) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}
