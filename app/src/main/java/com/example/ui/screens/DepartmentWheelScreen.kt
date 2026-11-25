package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.Department
import com.example.data.DepartmentId
import com.example.ui.components.RotaryWheel
import com.example.ui.theme.LocalMedWheelExtras

@Composable
fun DepartmentWheelScreen(
    departments: List<Department>,
    toolCounts: Map<DepartmentId, Int>,
    focusedIndex: Int,
    wheelState: LazyListState = rememberLazyListState(initialFirstVisibleItemIndex = focusedIndex),
    onFocusedIndexChanged: (Int) -> Unit,
    onOpenDepartment: (DepartmentId) -> Unit,
    modifier: Modifier = Modifier
) {
    if (departments.isEmpty()) return

    val safeIndex = focusedIndex.coerceIn(0, departments.lastIndex)
    val focusedDept = departments[safeIndex]
    val toolCount = toolCounts[focusedDept.id] ?: 0
    val haptic = LocalHapticFeedback.current
    val isDark = LocalMedWheelExtras.current.isDark

    // Apply per-department accent locally so wheel flings never re-theme the root Activity
    val accentPrimary = if (isDark) focusedDept.palette.darkPrimary else focusedDept.palette.lightPrimary
    val accentOnPrimary = if (isDark) focusedDept.palette.darkOnPrimary else focusedDept.palette.lightOnPrimary
    val accentContainer = if (isDark) focusedDept.palette.darkContainer else focusedDept.palette.lightContainer
    val accentOnContainer = if (isDark) focusedDept.palette.darkOnContainer else focusedDept.palette.lightOnContainer

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // ====================================================================
        // ONE UI UPPER ~30% VIEWING ZONE
        // ====================================================================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.30f)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = accentContainer
                ) {
                    Icon(
                        imageVector = focusedDept.icon,
                        contentDescription = null,
                        tint = accentOnContainer,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(22.dp)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh
                ) {
                    Text(
                        text = "${safeIndex + 1} / ${departments.size} · $toolCount TOOLS",
                        style = MaterialTheme.typography.labelMedium,
                        color = accentPrimary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = focusedDept.fullName,
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 30.sp,
                        lineHeight = 34.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = focusedDept.subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // ====================================================================
        // ONE UI LOWER ~70% THUMB INTERACTION ZONE (WHEEL + OPEN PILL)
        // ====================================================================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.70f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RotaryWheel(
                items = departments,
                initialIndex = safeIndex,
                itemHeight = 84.dp,
                wheelTag = "department_wheel",
                keySelector = { it.id.name },
                onFocusedIndexChanged = onFocusedIndexChanged,
                onCenteredItemTapped = { idx ->
                    onOpenDepartment(departments[idx].id)
                },
                listState = wheelState,
                accentPrimary = accentPrimary,
                accentContainer = accentContainer,
                modifier = Modifier.weight(1f)
            ) { dept: Department, isCentered: Boolean ->
                val itemColor = if (isCentered) {
                    if (isDark) dept.palette.darkPrimary else dept.palette.lightPrimary
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
                Text(
                    text = dept.wheelName,
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 34.sp,
                        lineHeight = 38.sp
                    ),
                    color = itemColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Large Bottom Thumb Pill ("OPEN →")
            Button(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onOpenDepartment(focusedDept.id)
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentPrimary,
                    contentColor = accentOnPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 14.dp)
                    .height(58.dp)
                    .testTag("open_department_button")
            ) {
                Text(
                    text = stringResource(R.string.action_open),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
