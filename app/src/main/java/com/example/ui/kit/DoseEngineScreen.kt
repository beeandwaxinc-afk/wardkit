package com.example.ui.kit

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Medication
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChoiceOption
import com.example.data.kit.AdultErAndInfusionData
import com.example.data.kit.DoseEngineMath
import com.example.data.kit.DoseTabId
import com.example.data.kit.LaAndPeadsCoreData
import com.example.data.kit.SpecialDrugMode
import com.example.data.kit.WeightBasedDrugDef
import com.example.data.kit.WeightUnit
import com.example.ui.components.ConnectedPillRow
import com.example.ui.theme.LocalMedWheelExtras
import com.example.ui.theme.PlusJakartaFontFamily

/**
 * Balanced 4-Subtab Quick Drug & Infusion Engine with shared top pinned blank weight bar,
 * kg | lbs connected toggle, CLEAR pill, hard adult max caps, and unified search focus filter.
 */
@Composable
fun DoseEngineScreen(
    activeTabCardId: String,
    focusedDrugId: String?,
    isPinned: Boolean,
    onSelectTabCard: (String) -> Unit,
    onClearFocusedDrug: () -> Unit,
    onTogglePin: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    var weightText by rememberSaveable { mutableStateOf("") }
    var weightUnitKey by rememberSaveable { mutableStateOf(WeightUnit.KG.id) }
    val weightUnit = if (weightUnitKey == WeightUnit.LBS.id) WeightUnit.LBS else WeightUnit.KG
    val activeTab = DoseTabId.fromCardId(activeTabCardId)

    val parsedWeight = weightText.trim().replace(',', '.').toDoubleOrNull()?.takeIf { it > 0.0 }
    val weightKg = parsedWeight?.let { weightUnit.toKg(it) }

    val sanityWarning = when {
        weightKg == null -> null
        activeTab == DoseTabId.PEADS_CORE && weightKg > 120.0 ->
            "Physiological Check: ${DoseEngineMath.fmt(weightKg, 1)} kg exceeds typical pediatric weight (>120 kg) — adult caps enforced."
        activeTab != DoseTabId.PEADS_CORE && weightKg > 250.0 ->
            "Physiological Check: ${DoseEngineMath.fmt(weightKg, 1)} kg exceeds 250 kg — verify weight & use IBW/AdjBW where indicated."
        else -> null
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .testTag("dose_engine_screen")
    ) {
        // ====================================================================
        // TOP PINNED SHARED WEIGHT BAR (BLANK BY DEFAULT, 30.sp BOLD)
        // ====================================================================
        SharedPinnedWeightBar(
            weightText = weightText,
            weightUnit = weightUnit,
            weightKg = weightKg,
            onWeightChange = { weightText = sanitizeDecimalInput(it) },
            onUnitSelect = { weightUnitKey = it.id },
            onClear = { weightText = "" }
        )

        // ====================================================================
        // 4 BALANCED CONNECTED SUB-TABS
        // ====================================================================
        ConnectedPillRow(
            options = DoseTabId.entries.map { ChoiceOption(it.cardId, it.shortLabel) },
            selectedId = activeTab.cardId,
            onSelect = { nextCardId ->
                onClearFocusedDrug()
                onSelectTabCard(nextCardId)
            },
            tagPrefix = "dose_tab",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )

        // Non-blocking physiological amber warning if weight exceeds threshold
        if (sanityWarning != null) {
            val amber = LocalMedWheelExtras.current.tierAmber
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                color = amber.container,
                border = BorderStroke(1.dp, amber.badge.copy(alpha = 0.55f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Rounded.WarningAmber, contentDescription = null, tint = amber.badge, modifier = Modifier.size(18.dp))
                    Text(
                        text = sanityWarning,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = amber.onContainer
                    )
                }
            }
        }

        // Focused Drug Banner (when launched from Unified Search)
        if (focusedDrugId != null) {
            val haptic = LocalHapticFeedback.current
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filtered from Search",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onClearFocusedDrug()
                        }
                        .testTag("dose_show_all_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Show All",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Show all drugs",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        // ====================================================================
        // ACTIVE SUB-TAB CONTENT
        // ====================================================================
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (activeTab) {
                DoseTabId.ER_RSI -> {
                    val drugs = if (focusedDrugId != null) {
                        AdultErAndInfusionData.erRsiDrugs.filter { it.id == focusedDrugId }
                            .ifEmpty { AdultErAndInfusionData.erRsiDrugs }
                    } else {
                        AdultErAndInfusionData.erRsiDrugs
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("dose_list_er_rsi"),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(drugs, key = { it.id }) { drug ->
                            WeightBasedDrugCard(
                                drug = drug,
                                weightKg = weightKg,
                                highlighted = drug.id == focusedDrugId
                            )
                        }
                        item(key = "er_rsi_spacer") { Spacer(Modifier.height(10.dp)) }
                    }
                }

                DoseTabId.WARD_ICU -> {
                    val showAll = focusedDrugId == null
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("dose_list_ward_icu"),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val infusions = if (showAll) {
                            AdultErAndInfusionData.continuousInfusions
                        } else {
                            AdultErAndInfusionData.continuousInfusions.filter { it.id == focusedDrugId }
                        }
                        items(infusions, key = { it.id }) { inf ->
                            ContinuousInfusionCard(
                                infusion = inf,
                                weightKg = weightKg,
                                highlighted = inf.id == focusedDrugId
                            )
                        }

                        if (showAll || focusedDrugId == "icu_insulin_drip") {
                            item(key = "icu_insulin_drip") {
                                InsulinDripCard(weightKg = weightKg, highlighted = focusedDrugId == "icu_insulin_drip")
                            }
                        }

                        if (showAll || focusedDrugId == "icu_mag_sulfate") {
                            item(key = "icu_mag_sulfate") {
                                MagSulfateCard(highlighted = focusedDrugId == "icu_mag_sulfate")
                            }
                        }

                        if (showAll || focusedDrugId == "icu_electrolyte_caps") {
                            item(key = "icu_electrolyte_caps") {
                                KitSectionCard(title = "Electrolyte IV Replacement Safe Caps") {
                                    AdultErAndInfusionData.electrolyteCaps.forEachIndexed { idx, cap ->
                                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = cap.electrolyte,
                                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.primaryContainer) {
                                                    Text(
                                                        text = cap.routeBadge,
                                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp)
                                                    )
                                                }
                                            }
                                            Text(
                                                text = cap.safeCapRule,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        if (idx < AdultErAndInfusionData.electrolyteCaps.lastIndex) {
                                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                                        }
                                    }
                                }
                            }
                        }

                        if (showAll || focusedDrugId == "icu_steroid_equiv") {
                            item(key = "icu_steroid_equiv") {
                                SteroidEquivalenceCard(highlighted = focusedDrugId == "icu_steroid_equiv")
                            }
                        }

                        if (showAll || focusedDrugId == "icu_adult_analgesia") {
                            item(key = "icu_adult_analgesia") {
                                KitSectionCard(title = "Adult Analgesia & Antiemetic Ward Dosing") {
                                    AdultErAndInfusionData.adultWardOrders.forEachIndexed { idx, item ->
                                        KitKeyValueRow(
                                            label = item.drug,
                                            sublabel = item.maxOrCaution,
                                            value = item.standardOrder,
                                            valueColor = MaterialTheme.colorScheme.primary
                                        )
                                        if (idx < AdultErAndInfusionData.adultWardOrders.lastIndex) {
                                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                                        }
                                    }
                                }
                            }
                        }

                        item(key = "ward_icu_spacer") { Spacer(Modifier.height(10.dp)) }
                    }
                }

                DoseTabId.LOCAL_ANESTH -> {
                    val showAll = focusedDrugId == null
                    val agents = if (showAll) {
                        LaAndPeadsCoreData.localAnesthetics
                    } else {
                        LaAndPeadsCoreData.localAnesthetics.filter { it.id == focusedDrugId }
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("dose_list_local_anesth"),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item(key = "ibw_reminder") {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = MaterialTheme.colorScheme.surfaceContainerHigh
                            ) {
                                Text(
                                    text = "Use Ideal Body Weight (IBW) in obese patients",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                                )
                            }
                        }

                        items(agents, key = { it.id }) { la ->
                            LocalAnestheticCard(
                                la = la,
                                weightKg = weightKg,
                                highlighted = la.id == focusedDrugId
                            )
                        }

                        if (showAll || focusedDrugId == "la_intralipid_last") {
                            item(key = "la_intralipid_last") {
                                IntralipidLastCard(
                                    weightKg = weightKg,
                                    highlighted = focusedDrugId == "la_intralipid_last"
                                )
                            }
                        }

                        item(key = "la_spacer") { Spacer(Modifier.height(10.dp)) }
                    }
                }

                DoseTabId.PEADS_CORE -> {
                    val drugs = if (focusedDrugId != null) {
                        LaAndPeadsCoreData.peadsCoreDrugs.filter { it.id == focusedDrugId }
                            .ifEmpty { LaAndPeadsCoreData.peadsCoreDrugs }
                    } else {
                        LaAndPeadsCoreData.peadsCoreDrugs
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("dose_list_peads_core"),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(drugs, key = { it.id }) { drug ->
                            WeightBasedDrugCard(
                                drug = drug,
                                weightKg = weightKg,
                                highlighted = drug.id == focusedDrugId
                            )
                        }
                        item(key = "peads_core_spacer") { Spacer(Modifier.height(10.dp)) }
                    }
                }
            }
        }

        // ====================================================================
        // BOTTOM ACTION DOCK: ← BACK | Pin / Pinned
        // ====================================================================
        KitBottomActionBar(
            isPinned = isPinned,
            onBack = onBack,
            onTogglePin = onTogglePin
        )
    }
}

@Composable
private fun SharedPinnedWeightBar(
    weightText: String,
    weightUnit: WeightUnit,
    weightKg: Double?,
    onWeightChange: (String) -> Unit,
    onUnitSelect: (WeightUnit) -> Unit,
    onClear: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(
            width = 1.2.dp,
            color = if (weightKg != null) MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
            else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Medication,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "QUICK DRUG & INFUSION ENGINE",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.6.sp
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (weightUnit == WeightUnit.LBS && weightKg != null) {
                    Text(
                        text = "= ${DoseEngineMath.fmt(weightKg, 1)} kg",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontFamily = PlusJakartaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontFeatureSettings = "tnum"
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = weightText,
                    onValueChange = onWeightChange,
                    modifier = Modifier
                        .weight(1.25f)
                        .heightIn(min = 60.dp)
                        .testTag("dose_weight_input"),
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Enter weight",
                            style = TextStyle(
                                fontFamily = PlusJakartaFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 22.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    },
                    textStyle = TextStyle(
                        fontFamily = PlusJakartaFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 30.sp,
                        lineHeight = 34.sp,
                        fontFeatureSettings = "tnum",
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.7f),
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                // Connected kg | lbs toggle pill
                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    listOf(WeightUnit.KG, WeightUnit.LBS).forEachIndexed { idx, unit ->
                        val selected = unit == weightUnit
                        val shape = if (idx == 0) {
                            RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp, topEnd = 5.dp, bottomEnd = 5.dp)
                        } else {
                            RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp, topEnd = 16.dp, bottomEnd = 16.dp)
                        }
                        Surface(
                            modifier = Modifier
                                .heightIn(min = 52.dp)
                                .clip(shape)
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    onUnitSelect(unit)
                                }
                                .testTag("dose_unit_${unit.id}"),
                            shape = shape,
                            color = if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceContainerHigh
                        ) {
                            Box(
                                modifier = Modifier.padding(horizontal = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = unit.label,
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                                    color = if (selected) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                // CLEAR pill
                Surface(
                    modifier = Modifier
                        .heightIn(min = 52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onClear()
                        }
                        .testTag("dose_weight_clear"),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.onSurface
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "CLEAR",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.surface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeightBasedDrugCard(
    drug: WeightBasedDrugDef,
    weightKg: Double?,
    highlighted: Boolean = false
) {
    var selectedRegId by rememberSaveable(drug.id) { mutableStateOf(drug.regimens.first().id) }
    var selectedConcId by rememberSaveable(drug.id) { mutableStateOf(drug.concentrations.first().id) }

    val regimen = drug.regimens.firstOrNull { it.id == selectedRegId } ?: drug.regimens.first()
    val allowedConcs = if (regimen.allowedConcIds != null) {
        drug.concentrations.filter { it.id in regimen.allowedConcIds }
    } else {
        drug.concentrations
    }
    val conc = allowedConcs.firstOrNull { it.id == selectedConcId } ?: allowedConcs.first()

    val amber = LocalMedWheelExtras.current.tierAmber
    val red = LocalMedWheelExtras.current.tierRed

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dose_card_${drug.id}"),
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
                    text = drug.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = drug.routeTag,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            if (drug.regimens.size > 1) {
                KitScrollableChipRow(
                    items = drug.regimens.map { it.id to it.label },
                    selectedId = regimen.id,
                    onSelect = { nextId ->
                        selectedRegId = nextId
                        val nextReg = drug.regimens.firstOrNull { it.id == nextId }
                        if (nextReg?.allowedConcIds != null && selectedConcId !in nextReg.allowedConcIds) {
                            selectedConcId = nextReg.allowedConcIds.first()
                        }
                    },
                    tagPrefix = "reg_${drug.id}",
                    contentPadding = PaddingValues(0.dp)
                )
            } else {
                Text(
                    text = regimen.label,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (allowedConcs.size > 1 && drug.specialMode != SpecialDrugMode.ASTHMA_NEBS_TIER) {
                KitScrollableChipRow(
                    items = allowedConcs.map { it.id to it.label },
                    selectedId = conc.id,
                    onSelect = { selectedConcId = it },
                    tagPrefix = "conc_${drug.id}",
                    contentPadding = PaddingValues(0.dp)
                )
            } else if (drug.specialMode != SpecialDrugMode.ASTHMA_NEBS_TIER &&
                drug.specialMode != SpecialDrugMode.HEPARIN_BOLUS_DRIP
            ) {
                Text(
                    text = "Strength: ${conc.label}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // ================================================================
            // LIVE READOUT SLOT BY SPECIAL MODE
            // ================================================================
            when (drug.specialMode) {
                SpecialDrugMode.ADENOSINE_DUAL -> {
                    val res = weightKg?.let { DoseEngineMath.computeAdenosineDoses(it, conc.mgPerMl) }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        KitMetricTile(
                            label = "1st Dose (Max 6 mg)",
                            value = if (res != null) "${DoseEngineMath.fmtVol(res.firstDoseMl)} mL" else "Enter weight",
                            unit = if (res != null) "${DoseEngineMath.fmtDose(res.firstDoseMg)} mg" else "0.1 mg/kg",
                            emphasized = res != null,
                            modifier = Modifier.weight(1f)
                        )
                        KitMetricTile(
                            label = "2nd Dose (Max 12 mg)",
                            value = if (res != null) "${DoseEngineMath.fmtVol(res.secondDoseMl)} mL" else "—",
                            unit = if (res != null) "${DoseEngineMath.fmtDose(res.secondDoseMg)} mg" else "0.2 mg/kg",
                            emphasized = res != null,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (res != null && (res.isFirstCapped || res.isSecondCapped)) {
                        CapWarningPill("⚠ CAPPED AT ADULT MAX (6 mg / 12 mg)")
                    }
                }

                SpecialDrugMode.HEPARIN_BOLUS_DRIP -> {
                    val isAcs = regimen.id == "acs"
                    val res = weightKg?.let { DoseEngineMath.computeHeparin(it, isAcs) }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        KitMetricTile(
                            label = "IV Bolus",
                            value = if (res != null) "${DoseEngineMath.fmt(res.bolusUnits, 0)} U" else "Enter weight",
                            unit = if (isAcs) "60 U/kg (max 4,000 U)" else "80 U/kg (max 10,000 U)",
                            emphasized = res != null,
                            modifier = Modifier.weight(1f)
                        )
                        KitMetricTile(
                            label = "IV Drip",
                            value = if (res != null) "${DoseEngineMath.fmt(res.dripUnitsPerHr, 0)} U/hr" else "—",
                            unit = if (isAcs) "12 U/kg/hr (max 1,000)" else "18 U/kg/hr",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (res != null && (res.isBolusCapped || res.isDripCapped)) {
                        val capLabel = if (isAcs) "4,000 U / 1,000 U/hr" else "10,000 U"
                        CapWarningPill("⚠ CAPPED AT ADULT MAX ($capLabel)")
                    }
                }

                SpecialDrugMode.ASTHMA_NEBS_TIER -> {
                    val isHeavy = (weightKg ?: 0.0) >= 20.0
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        KitMetricTile(
                            label = "Salbutamol (5 mg/mL)",
                            value = when {
                                weightKg == null -> "Enter weight"
                                isHeavy -> "5.0 mg (1.0 mL)"
                                else -> "2.5 mg (0.5 mL)"
                            },
                            unit = if (weightKg == null) "<20 kg: 2.5 mg · ≥20 kg: 5 mg" else if (isHeavy) "Tier ≥20 kg" else "Tier <20 kg",
                            emphasized = weightKg != null,
                            modifier = Modifier.weight(1f)
                        )
                        KitMetricTile(
                            label = "Ipratropium",
                            value = when {
                                weightKg == null -> "—"
                                isHeavy -> "500 mcg (2 mL)"
                                else -> "250 mcg (1 mL)"
                            },
                            unit = "Neb q20m ×3",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                else -> {
                    val dynamicMax = if (drug.specialMode == SpecialDrugMode.ONDANSETRON_DYNAMIC_CAP && weightKg != null) {
                        if (weightKg >= 40.0) 8.0 else 4.0
                    } else {
                        null
                    }
                    val comp = weightKg?.let { DoseEngineMath.computeWeightDose(it, regimen, conc, dynamicMax) }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        KitMetricTile(
                            label = if (drug.tabId == DoseTabId.PEADS_CORE) "Volume / Dose" else "Draw-Up Volume",
                            value = if (comp != null) "${DoseEngineMath.fmtVol(comp.volumeMl)} mL" else "Enter weight",
                            unit = conc.label,
                            emphasized = comp != null,
                            modifier = Modifier.weight(1f)
                        )
                        KitMetricTile(
                            label = "Dose (${regimen.frequencyLabel})",
                            value = if (comp != null) "${DoseEngineMath.fmtDose(comp.finalSingleMg)} mg" else "—",
                            unit = if (comp?.finalDailyMg != null) {
                                "Total: ${DoseEngineMath.fmtDose(comp.finalDailyMg)} mg/day"
                            } else {
                                "Max ${DoseEngineMath.fmt(dynamicMax ?: regimen.maxSingleMg, 0)} mg"
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (comp != null && (comp.isSingleCapped || comp.isDailyCapped)) {
                        val maxText = if (comp.isSingleCapped) {
                            "${DoseEngineMath.fmt(comp.capSingleMg, 0)} mg"
                        } else {
                            "${DoseEngineMath.fmt(comp.capDailyMg ?: comp.capSingleMg, 0)} mg/day"
                        }
                        CapWarningPill("⚠ CAPPED AT ADULT MAX ($maxText)")
                    }
                }
            }

            Text(
                text = drug.pearl,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = if (drug.isRedFlagPearl) FontWeight.Bold else FontWeight.Medium,
                    lineHeight = 18.sp
                ),
                color = if (drug.isRedFlagPearl) red.badge else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CapWarningPill(text: String) {
    val amber = LocalMedWheelExtras.current.tierAmber
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = amber.container,
        border = BorderStroke(1.2.dp, amber.badge.copy(alpha = 0.6f))
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
            color = amber.onContainer,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
