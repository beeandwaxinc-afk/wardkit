package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import com.example.data.CalculatorRegistry
import com.example.data.Department
import com.example.data.DepartmentId
import com.example.data.DepartmentRegistry
import com.example.ui.components.RotaryWheel

@Composable
fun DepartmentWheelScreen(
    focusedIndex: Int,
    onFocusedIndexChanged: (Int) -> Unit,
    onOpenDepartment: (DepartmentId) -> Unit,
    modifier: Modifier = Modifier
) {
    val departments = DepartmentRegistry.all
    val focusedDept = departments.getOrElse(focusedIndex) { departments.first() }
    val toolCount = CalculatorRegistry.countForDepartment(focusedDept.id)
    val haptic = LocalHapticFeedback.current

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
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(
                        imageVector = focusedDept.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
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
                        text = "${focusedIndex + 1} / ${departments.size} · $toolCount TOOLS",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            AnimatedContent(
                targetState = focusedDept,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "dept_header_anim"
            ) { dept ->
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = dept.fullName,
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
                        text = dept.subtitle,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
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
                initialIndex = focusedIndex,
                itemHeight = 84.dp,
                wheelTag = "department_wheel",
                keySelector = { it.id.name },
                onFocusedIndexChanged = onFocusedIndexChanged,
                onCenteredItemTapped = { idx ->
                    onOpenDepartment(departments[idx].id)
                },
                modifier = Modifier.weight(1f)
            ) { dept: Department, isCentered: Boolean ->
                Text(
                    text = dept.wheelName,
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 34.sp,
                        lineHeight = 38.sp
                    ),
                    color = if (isCentered) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface,
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
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
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
