package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.CalcInputDef
import com.example.data.CalculationResult
import com.example.data.CalculatorDef
import com.example.data.CalculatorInputState
import com.example.data.DepartmentRegistry
import com.example.data.SecondaryOutput
import com.example.ui.components.ConnectedPillColumn
import com.example.ui.components.ConnectedPillRow
import com.example.ui.components.DateInputCard
import com.example.ui.components.LargeNumericInputCard
import com.example.ui.components.ScoreToggleCard
import com.example.ui.theme.LocalMedWheelExtras

@Composable
fun CalculatorCanvasScreen(
    calculator: CalculatorDef,
    inputState: CalculatorInputState,
    result: CalculationResult,
    isPinned: Boolean,
    onNumericChange: (String, String) -> Unit,
    onUnitSelected: (String, Int) -> Unit,
    onChoiceSelected: (String, String) -> Unit,
    onToggleCriterion: (String) -> Unit,
    onDateSelected: (String, Long) -> Unit,
    onTogglePin: () -> Unit,
    onClear: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    val department = DepartmentRegistry.get(calculator.departmentId)
    val haptic = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current

    val visibleInputs = calculator.inputs.filter { def ->
        val condKey = when (def) {
            is CalcInputDef.NumericField -> def.visibleWhenChoiceKey
            is CalcInputDef.SegmentedChoice -> def.visibleWhenChoiceKey
            is CalcInputDef.ToggleCriterion -> def.visibleWhenChoiceKey
            is CalcInputDef.SectionHeader -> def.visibleWhenChoiceKey
            is CalcInputDef.DateInput -> null
        }
        val condVal = when (def) {
            is CalcInputDef.NumericField -> def.visibleWhenChoiceValue
            is CalcInputDef.SegmentedChoice -> def.visibleWhenChoiceValue
            is CalcInputDef.ToggleCriterion -> def.visibleWhenChoiceValue
            is CalcInputDef.SectionHeader -> def.visibleWhenChoiceValue
            is CalcInputDef.DateInput -> null
        }
        if (condKey != null && condVal != null) {
            val defaultOpt = calculator.inputs
                .filterIsInstance<CalcInputDef.SegmentedChoice>()
                .firstOrNull { it.id == condKey }
                ?.defaultOptionId
            inputState.choice(condKey, defaultOpt) == condVal
        } else {
            true
        }
    }

    val lastNumericId = visibleInputs.filterIsInstance<CalcInputDef.NumericField>().lastOrNull()?.id

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        // ====================================================================
        // TOP PINNED LIVE ANSWER CARD (VIEWING ZONE)
        // ====================================================================
        PinnedLiveAnswerCard(
            departmentName = department.wheelName,
            calculatorName = calculator.name,
            result = result,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        )

        // ====================================================================
        // SCROLLABLE INPUT AREA (THUMB ZONE)
        // ====================================================================
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .testTag("calculator_inputs_list"),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (result.warnings.isNotEmpty()) {
                item(key = "sanity_warnings") {
                    PhysiologicalWarningBanner(warnings = result.warnings)
                }
            }

            itemsIndexed(
                items = visibleInputs,
                key = { _, def -> def.id }
            ) { _, def ->
                when (def) {
                    is CalcInputDef.SectionHeader -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 6.dp, top = 8.dp, bottom = 2.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = def.label.uppercase(),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    letterSpacing = 0.9.sp,
                                    fontSize = 13.sp
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                            if (def.subtitle.isNotEmpty()) {
                                Text(
                                    text = def.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    is CalcInputDef.NumericField -> {
                        LargeNumericInputCard(
                            fieldId = def.id,
                            label = def.label,
                            value = inputState.numericValues[def.id] ?: "",
                            hint = def.hint,
                            units = def.units,
                            selectedUnitIndex = inputState.unitIndex(def.id, def.defaultUnitIndex),
                            onValueChange = { onNumericChange(def.id, it) },
                            onUnitSelected = { onUnitSelected(def.id, it) },
                            imeAction = if (def.id == lastNumericId) ImeAction.Done else ImeAction.Next
                        )
                    }

                    is CalcInputDef.SegmentedChoice -> {
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerLow,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = def.label,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (def.subtitle.isNotEmpty()) {
                                    Text(
                                        text = def.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                val selected = inputState.choice(def.id, def.defaultOptionId)
                                if (def.compactRow && def.options.size <= 4) {
                                    ConnectedPillRow(
                                        options = def.options,
                                        selectedId = selected,
                                        onSelect = { onChoiceSelected(def.id, it) },
                                        tagPrefix = "choice_${def.id}"
                                    )
                                } else {
                                    ConnectedPillColumn(
                                        options = def.options,
                                        selectedId = selected,
                                        onSelect = { onChoiceSelected(def.id, it) },
                                        tagPrefix = "choice_${def.id}"
                                    )
                                }
                            }
                        }
                    }

                    is CalcInputDef.ToggleCriterion -> {
                        ScoreToggleCard(
                            fieldId = def.id,
                            label = def.label,
                            detail = def.detail,
                            pointsBadge = def.pointsBadge,
                            checked = inputState.toggle(def.id),
                            onToggle = { onToggleCriterion(def.id) }
                        )
                    }

                    is CalcInputDef.DateInput -> {
                        DateInputCard(
                            fieldId = def.id,
                            label = def.label,
                            hint = def.hint,
                            selectedMillis = inputState.dateMillis(def.id),
                            onDateSelected = { onDateSelected(def.id, it) }
                        )
                    }
                }
            }
        }

        // ====================================================================
        // BOTTOM ACTION DOCK: ← BACK | Pin / Pinned | CLEAR
        // ====================================================================
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        focusManager.clearFocus()
                        onBack()
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp)
                        .testTag("canvas_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = stringResource(R.string.action_back),
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1
                    )
                }

                FilledTonalButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onTogglePin()
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (isPinned) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceContainerHigh,
                        contentColor = if (isPinned) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSurface
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp)
                        .testTag("canvas_pin_button")
                ) {
                    Icon(
                        imageVector = if (isPinned) Icons.Rounded.PushPin else Icons.Outlined.PushPin,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = stringResource(if (isPinned) R.string.action_pinned else R.string.action_pin),
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1
                    )
                }

                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        focusManager.clearFocus()
                        onClear()
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSurface,
                        contentColor = MaterialTheme.colorScheme.surface
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp)
                        .testTag("canvas_clear_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = stringResource(R.string.action_clear),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun PinnedLiveAnswerCard(
    departmentName: String,
    calculatorName: String,
    result: CalculationResult,
    modifier: Modifier = Modifier
) {
    val extras = LocalMedWheelExtras.current
    val tierColors = extras.colorsForTier(result.tier)

    val cardBg by animateColorAsState(
        targetValue = if (result.isReady) MaterialTheme.colorScheme.surfaceContainerLow
        else MaterialTheme.colorScheme.surfaceContainerLow,
        animationSpec = tween(220),
        label = "answer_card_bg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (result.isReady) tierColors.badge.copy(alpha = 0.55f)
        else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f),
        animationSpec = tween(220),
        label = "answer_card_border"
    )

    Surface(
        modifier = modifier.testTag("live_answer_card"),
        shape = RoundedCornerShape(26.dp),
        color = cardBg,
        border = BorderStroke(1.5.dp, borderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 320.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = calculatorName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = departmentName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            if (!result.isReady) {
                Text(
                    text = result.emptyPrompt.ifEmpty { stringResource(R.string.empty_answer_prompt) },
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.72f),
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .testTag("empty_answer_text")
                )
            } else {
                // Giant live primary readout (44.sp–52.sp tabular numerals)
                val primaryFontSize = when {
                    result.primaryValue.length >= 10 -> 34.sp
                    result.primaryValue.length >= 7 -> 42.sp
                    else -> 50.sp
                }

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = result.primaryValue,
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = primaryFontSize,
                            lineHeight = primaryFontSize * 1.08f
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.testTag("live_primary_value")
                    )
                    if (result.primaryUnit.isNotEmpty()) {
                        Text(
                            text = result.primaryUnit,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }

                // Color-coded Clinical Tier Pill
                if (!result.tierLabel.isNullOrBlank()) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = tierColors.container,
                        modifier = Modifier.testTag("live_tier_pill")
                    ) {
                        Text(
                            text = result.tierLabel,
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp),
                            color = tierColors.onContainer,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }

                // Essential Secondary Outputs
                if (result.secondaryOutputs.isNotEmpty()) {
                    SecondaryOutputsGrid(outputs = result.secondaryOutputs)
                }

                // Optional brief clinical action note
                if (!result.clinicalNote.isNullOrBlank()) {
                    Text(
                        text = result.clinicalNote,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun SecondaryOutputsGrid(outputs: List<SecondaryOutput>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        val useTwoColumn = outputs.size >= 2 && outputs.all { it.value.length <= 24 }
        if (useTwoColumn) {
            outputs.chunked(2).forEach { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    pair.forEach { item ->
                        SecondaryOutputChip(
                            item = item,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (pair.size == 1) {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        } else {
            outputs.forEach { item ->
                SecondaryOutputChip(
                    item = item,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun SecondaryOutputChip(
    item: SecondaryOutput,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = if (item.highlight) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)
        else MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = item.label,
                style = MaterialTheme.typography.labelSmall,
                color = if (item.highlight) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                ),
                color = if (item.highlight) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun PhysiologicalWarningBanner(warnings: List<String>) {
    val amber = LocalMedWheelExtras.current.tierAmber
    AnimatedVisibility(visible = warnings.isNotEmpty()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("physiological_warning_banner"),
            shape = RoundedCornerShape(20.dp),
            color = amber.container,
            border = BorderStroke(1.dp, amber.badge.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Rounded.WarningAmber,
                    contentDescription = stringResource(R.string.sanity_warning_prefix),
                    tint = amber.onContainer,
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(20.dp)
                )
                Spacer(Modifier.width(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = stringResource(R.string.sanity_warning_prefix),
                        style = MaterialTheme.typography.labelLarge,
                        color = amber.onContainer
                    )
                    warnings.forEach { msg ->
                        Text(
                            text = msg,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            ),
                            color = amber.onContainer
                        )
                    }
                }
            }
        }
    }
}
