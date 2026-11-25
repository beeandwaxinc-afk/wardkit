package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Functions
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocalHospital
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.CalculatorDef
import com.example.data.ChoiceOption
import com.example.data.Department
import com.example.data.DepartmentId
import com.example.data.ThemeMode
import com.example.ui.theme.LocalMedWheelExtras

/**
 * Lazily composed Side Drawer body rendered via LazyColumn with explicit keys.
 * All department and formula accordions start collapsed by default to ensure instant drawer open
 * and zero background composition overhead on 4GB RAM devices.
 */
@Composable
fun MedWheelDrawerContent(
    themeMode: ThemeMode,
    activeCalculator: CalculatorDef?,
    departments: List<Department>,
    calculatorsByDepartment: Map<DepartmentId, List<CalculatorDef>>,
    toolCounts: Map<DepartmentId, Int>,
    totalToolCount: Int,
    onSelectTheme: (ThemeMode) -> Unit,
    onJumpToDepartment: (DepartmentId) -> Unit,
    onJumpToCalculator: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val isDark = LocalMedWheelExtras.current.isDark

    // All accordions collapsed by default
    var departmentsExpanded by remember { mutableStateOf(false) }
    var formulasExpanded by remember { mutableStateOf(false) }
    var disclaimerExpanded by remember { mutableStateOf(false) }
    val expandedFormulaDepts = remember { mutableStateMapOf<DepartmentId, Boolean>() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("drawer_lazy_list"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ====================================================================
        // BRAND HEADER — Minimalist Stethoscope Emblem tinted via onSurface
        // ====================================================================
        item(key = "drawer_brand_header", contentType = "drawer_header") {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)
                    ),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_ward_kit_emblem),
                            contentDescription = stringResource(R.string.cd_ward_kit_logo),
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 26.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Offline Clinical Ward Toolkit",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // ====================================================================
        // SECTION 1: THEME TOGGLE (System | Light | AMOLED Dark)
        // ====================================================================
        item(key = "drawer_theme_section", contentType = "drawer_theme") {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = stringResource(R.string.drawer_appearance),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                ConnectedPillRow(
                    options = listOf(
                        ChoiceOption(ThemeMode.SYSTEM.key, stringResource(R.string.drawer_theme_system)),
                        ChoiceOption(ThemeMode.LIGHT.key, stringResource(R.string.drawer_theme_light)),
                        ChoiceOption(ThemeMode.AMOLED_DARK.key, stringResource(R.string.drawer_theme_amoled))
                    ),
                    selectedId = themeMode.key,
                    onSelect = { key -> onSelectTheme(ThemeMode.fromKey(key)) },
                    tagPrefix = "drawer_theme"
                )
            }
        }

        item(key = "drawer_div_1", contentType = "drawer_divider") {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        }

        // ====================================================================
        // SECTION 2: DEPARTMENTS & TOOL-COUNT BADGES ACCORDION
        // ====================================================================
        item(key = "drawer_departments_header", contentType = "drawer_accordion_header") {
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLow
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 56.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            departmentsExpanded = !departmentsExpanded
                        }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                        .testTag("drawer_departments_accordion"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.LocalHospital,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = stringResource(R.string.drawer_departments_header, totalToolCount),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = if (departmentsExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                        contentDescription = if (departmentsExpanded) "Collapse departments" else "Expand departments",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        if (departmentsExpanded) {
            items(
                items = departments,
                key = { "drawer_dept_${it.id.name}" },
                contentType = { "drawer_dept_row" }
            ) { dept ->
                val count = toolCounts[dept.id] ?: 0
                val accent = if (isDark) dept.palette.darkPrimary else dept.palette.lightPrimary
                val badgeBg = if (isDark) dept.palette.darkContainer else dept.palette.lightContainer
                val badgeFg = if (isDark) dept.palette.darkOnContainer else dept.palette.lightOnContainer

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLow
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onJumpToDepartment(dept.id)
                            }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                            .testTag("drawer_dept_${dept.id.name.lowercase()}"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(accent)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = dept.fullName,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = badgeBg
                        ) {
                            Text(
                                text = "$count",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = badgeFg,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }

        // ====================================================================
        // SECTION 3: FORMULAS & CLINICAL PEARLS ACCORDION (LAZY ITEMS)
        // ====================================================================
        item(key = "drawer_formulas_header", contentType = "drawer_accordion_header") {
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLow
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 56.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            formulasExpanded = !formulasExpanded
                        }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                        .testTag("drawer_formulas_accordion"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Functions,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = stringResource(R.string.drawer_formulas_header),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = if (formulasExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                        contentDescription = if (formulasExpanded) "Collapse formulas" else "Expand formulas",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        if (formulasExpanded) {
            if (activeCalculator != null) {
                item(key = "drawer_active_formula_${activeCalculator.id}", contentType = "drawer_active_formula") {
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                        modifier = Modifier.testTag("drawer_active_formula_card")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.drawer_active_calculator_badge),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = activeCalculator.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = activeCalculator.formulaSummary,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 18.sp
                                ),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = activeCalculator.clinicalPearl,
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }

            departments.forEach { dept ->
                val isDeptOpen = expandedFormulaDepts[dept.id] == true
                val accent = if (isDark) dept.palette.darkPrimary else dept.palette.lightPrimary

                item(
                    key = "drawer_formula_dept_hdr_${dept.id.name}",
                    contentType = "drawer_formula_dept_header"
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceContainer
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    expandedFormulaDepts[dept.id] = !isDeptOpen
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                                .testTag("drawer_formula_dept_${dept.id.name.lowercase()}"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(accent)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = dept.fullName,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = if (isDeptOpen) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                if (isDeptOpen) {
                    val deptCalcs = calculatorsByDepartment[dept.id].orEmpty()
                    items(
                        items = deptCalcs,
                        key = { "drawer_formula_calc_${it.id}" },
                        contentType = { "drawer_formula_card" }
                    ) { calc ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerLow,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { onJumpToCalculator(calc.id) }
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = calc.name,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = accent
                                )
                                Text(
                                    text = calc.formulaSummary,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Medium,
                                        lineHeight = 17.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = calc.clinicalPearl,
                                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 16.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        item(key = "drawer_div_2", contentType = "drawer_divider") {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        }

        // ====================================================================
        // SECTION 4: INFO & CLINICAL DISCLAIMER
        // ====================================================================
        item(key = "drawer_disclaimer", contentType = "drawer_disclaimer") {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = stringResource(R.string.drawer_version_line),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLow
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 52.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    disclaimerExpanded = !disclaimerExpanded
                                }
                                .padding(horizontal = 14.dp, vertical = 12.dp)
                                .testTag("drawer_disclaimer_accordion"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = stringResource(R.string.drawer_disclaimer_title),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = if (disclaimerExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (disclaimerExpanded) {
                            Text(
                                text = stringResource(R.string.drawer_disclaimer_body),
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(start = 14.dp, end = 14.dp, bottom = 14.dp)
                            )
                        }
                    }
                }
            }
        }

        item(key = "drawer_bottom_spacer", contentType = "drawer_spacer") {
            Spacer(Modifier.height(8.dp))
        }
    }
}
