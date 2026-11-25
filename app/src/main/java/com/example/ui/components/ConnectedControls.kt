package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChoiceOption
import com.example.ui.theme.PlusJakartaFontFamily
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Material 3 Expressive Connected Pill Button Group (Horizontal).
 * Outer corners rounded 20.dp, inner adjacent corners 6.dp, 4.dp gap, 48.dp+ touch target.
 */
@Composable
fun ConnectedPillRow(
    options: List<ChoiceOption>,
    selectedId: String?,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    tagPrefix: String = "pill"
) {
    val haptic = LocalHapticFeedback.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = option.id == selectedId
            val shape = when {
                options.size == 1 -> RoundedCornerShape(20.dp)
                index == 0 -> RoundedCornerShape(
                    topStart = 20.dp,
                    bottomStart = 20.dp,
                    topEnd = 6.dp,
                    bottomEnd = 6.dp
                )
                index == options.lastIndex -> RoundedCornerShape(
                    topStart = 6.dp,
                    bottomStart = 6.dp,
                    topEnd = 20.dp,
                    bottomEnd = 20.dp
                )
                else -> RoundedCornerShape(6.dp)
            }

            val bg by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceContainerHigh,
                animationSpec = tween(180),
                label = "pill_bg"
            )
            val fg by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurface,
                animationSpec = tween(180),
                label = "pill_fg"
            )

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 50.dp)
                    .clip(shape)
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onSelect(option.id)
                    }
                    .testTag("${tagPrefix}_${option.id}"),
                shape = shape,
                color = bg
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = option.label,
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp),
                        color = fg,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                    if (option.pointsBadge != null || option.detail.isNotEmpty()) {
                        Text(
                            text = option.pointsBadge ?: option.detail,
                            style = MaterialTheme.typography.labelSmall,
                            color = fg.copy(alpha = if (isSelected) 0.9f else 0.7f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

/**
 * Material 3 Expressive Connected Pill Button Group (Vertical Stack) for rich clinical score rows.
 */
@Composable
fun ConnectedPillColumn(
    options: List<ChoiceOption>,
    selectedId: String?,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    tagPrefix: String = "vpill"
) {
    val haptic = LocalHapticFeedback.current
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = option.id == selectedId
            val shape = when {
                options.size == 1 -> RoundedCornerShape(20.dp)
                index == 0 -> RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = 6.dp,
                    bottomEnd = 6.dp
                )
                index == options.lastIndex -> RoundedCornerShape(
                    topStart = 6.dp,
                    topEnd = 6.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                )
                else -> RoundedCornerShape(6.dp)
            }

            val bg by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceContainerLow,
                animationSpec = tween(180),
                label = "vpill_bg"
            )
            val borderColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary
                else Color.Transparent,
                animationSpec = tween(180),
                label = "vpill_border"
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 54.dp)
                    .clip(shape)
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onSelect(option.id)
                    }
                    .testTag("${tagPrefix}_${option.id}"),
                shape = shape,
                color = bg,
                border = if (isSelected) BorderStroke(1.5.dp, borderColor) else null
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = option.label,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                fontSize = 17.sp
                            ),
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.onSurface
                        )
                        if (option.detail.isNotEmpty()) {
                            Text(
                                text = option.detail,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.82f)
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    if (option.pointsBadge != null) {
                        Spacer(Modifier.width(12.dp))
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceContainerHighest
                        ) {
                            Text(
                                text = option.pointsBadge,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Large-typography numeric input container (68.dp+ tall, 30.sp bold tabular numerals)
 * with optional connected unit toggle pills. Strictly blank ("") default.
 */
@Composable
fun LargeNumericInputCard(
    fieldId: String,
    label: String,
    value: String,
    hint: String,
    units: List<String>,
    selectedUnitIndex: Int,
    onValueChange: (String) -> Unit,
    onUnitSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next
) {
    val activeUnit = units.getOrElse(selectedUnitIndex) { "" }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                if (units.size == 1 && activeUnit.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.surfaceContainerHighest
                    ) {
                        Text(
                            text = activeUnit,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            if (units.size > 1) {
                ConnectedPillRow(
                    options = units.mapIndexed { idx, u -> ChoiceOption(idx.toString(), u) },
                    selectedId = selectedUnitIndex.toString(),
                    onSelect = { id -> id.toIntOrNull()?.let(onUnitSelected) },
                    tagPrefix = "unit_$fieldId"
                )
            }

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 72.dp)
                    .testTag("input_$fieldId"),
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = PlusJakartaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    lineHeight = 38.sp,
                    fontFeatureSettings = "tnum",
                    color = MaterialTheme.colorScheme.onSurface
                ),
                placeholder = {
                    Text(
                        text = hint.ifEmpty { "—" },
                        style = TextStyle(
                            fontFamily = PlusJakartaFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 26.sp,
                            fontFeatureSettings = "tnum"
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f)
                    )
                },
                trailingIcon = {
                    if (value.isNotEmpty()) {
                        IconButton(
                            onClick = { onValueChange("") },
                            modifier = Modifier.testTag("clear_field_$fieldId")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Clear $label",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else if (activeUnit.isNotEmpty()) {
                        Text(
                            text = activeUnit,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier.padding(end = 14.dp)
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = imeAction
                ),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.7f),
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

/**
 * Full-width tactile Material 3 score toggle card with clear point badge (+1, +2, -2).
 */
@Composable
fun ScoreToggleCard(
    fieldId: String,
    label: String,
    detail: String,
    pointsBadge: String,
    checked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val bg by animateColorAsState(
        targetValue = if (checked) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceContainerLow,
        animationSpec = tween(180),
        label = "toggle_bg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (checked) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
        animationSpec = tween(180),
        label = "toggle_border"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 68.dp)
            .clip(RoundedCornerShape(22.dp))
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onToggle()
            }
            .testTag("toggle_$fieldId"),
        shape = RoundedCornerShape(22.dp),
        color = bg,
        border = BorderStroke(if (checked) 1.5.dp else 1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (checked) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                contentDescription = if (checked) "$label selected" else "$label not selected",
                tint = if (checked) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.size(26.dp)
            )
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    ),
                    color = if (checked) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurface
                )
                if (detail.isNotEmpty()) {
                    Text(
                        text = detail,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                        color = if (checked) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.width(10.dp))
            Surface(
                shape = RoundedCornerShape(50),
                color = if (checked) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceContainerHighest
            ) {
                Text(
                    text = pointsBadge,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (checked) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

/**
 * Material 3 DatePicker card for Naegele's Rule (LMP).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInputCard(
    fieldId: String,
    label: String,
    hint: String,
    selectedMillis: Long?,
    onDateSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    val formattedDate = remember(selectedMillis) {
        selectedMillis?.let {
            SimpleDateFormat("dd MMM yyyy", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }.format(Date(it))
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                showDialog = true
            }
            .testTag("date_$fieldId"),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(
            width = 1.5.dp,
            color = if (selectedMillis != null) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.size(6.dp))
                Text(
                    text = formattedDate ?: hint,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 26.sp
                    ),
                    color = if (formattedDate != null) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(
                    modifier = Modifier.padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CalendarMonth,
                        contentDescription = "Select date",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }

    if (showDialog) {
        val pickerState = rememberDatePickerState(initialSelectedDateMillis = selectedMillis)
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        pickerState.selectedDateMillis?.let(onDateSelected)
                        showDialog = false
                    },
                    modifier = Modifier.testTag("date_confirm_$fieldId")
                ) {
                    Text("Confirm", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }
}
