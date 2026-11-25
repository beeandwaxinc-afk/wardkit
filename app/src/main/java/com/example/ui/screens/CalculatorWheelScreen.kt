package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
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
import com.example.data.CalculatorDef
import com.example.data.DepartmentId
import com.example.data.DepartmentRegistry
import com.example.ui.components.RotaryWheel

@Composable
fun CalculatorWheelScreen(
    departmentId: DepartmentId,
    calculators: List<CalculatorDef>,
    focusedCalculatorIndex: Int,
    onFocusedCalculatorChanged: (Int) -> Unit,
    onOpenCalculator: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)
    if (calculators.isEmpty()) return

    val department = DepartmentRegistry.get(departmentId)
    val safeIndex = focusedCalculatorIndex.coerceIn(0, calculators.lastIndex)
    val focusedCalc = calculators[safeIndex]
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // ====================================================================
        // ONE UI UPPER ~28% VIEWING ZONE
        // ====================================================================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.28f)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center
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
                        imageVector = department.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(20.dp)
                    )
                }
                Text(
                    text = department.fullName.uppercase(),
                    style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 0.8.sp),
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = focusedCalc.shortSubtitle,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        // ====================================================================
        // ONE UI LOWER ~72% THUMB INTERACTION ZONE
        // ====================================================================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.72f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            key(departmentId) {
                RotaryWheel(
                    items = calculators,
                    initialIndex = safeIndex,
                    itemHeight = 96.dp,
                    wheelTag = "calculator_wheel",
                    keySelector = { it.id },
                    onFocusedIndexChanged = onFocusedCalculatorChanged,
                    onCenteredItemTapped = { idx ->
                        onOpenCalculator(calculators[idx].id)
                    },
                    modifier = Modifier.weight(1f)
                ) { calc: CalculatorDef, isCentered: Boolean ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = calc.name,
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 28.sp,
                                lineHeight = 32.sp
                            ),
                            color = if (isCentered) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (isCentered) {
                            Spacer(Modifier.height(3.dp))
                            Text(
                                text = calc.shortSubtitle,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            // Bottom Thumb Bar: ← BACK pill + OPEN → pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilledTonalButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onBack()
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .weight(0.42f)
                        .height(58.dp)
                        .testTag("calc_wheel_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.action_back),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onOpenCalculator(focusedCalc.id)
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .weight(0.58f)
                        .height(58.dp)
                        .testTag("open_calculator_button")
                ) {
                    Text(
                        text = stringResource(R.string.action_open),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp,
                            letterSpacing = 0.8.sp
                        )
                    )
                    Spacer(Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
