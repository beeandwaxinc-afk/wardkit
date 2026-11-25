package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun <T> RotaryWheel(
    items: List<T>,
    initialIndex: Int,
    itemHeight: Dp,
    wheelTag: String,
    keySelector: (T) -> Any,
    onFocusedIndexChanged: (Int) -> Unit,
    onCenteredItemTapped: (Int) -> Unit,
    modifier: Modifier = Modifier,
    itemContent: @Composable (item: T, isCentered: Boolean) -> Unit
) {
    if (items.isEmpty()) return

    val safeInitial = initialIndex.coerceIn(0, items.lastIndex)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = safeInitial)
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current
    val density = LocalDensity.current
    val itemHeightPx = with(density) { itemHeight.toPx() }

    val centeredIndex by remember(listState, items.size) {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visible = layoutInfo.visibleItemsInfo
            if (visible.isEmpty()) {
                safeInitial
            } else {
                val viewportCenter =
                    (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2f
                visible.minByOrNull { info ->
                    val itemCenter = info.offset + info.size / 2f
                    abs(itemCenter - viewportCenter)
                }?.index?.coerceIn(0, items.lastIndex) ?: safeInitial
            }
        }
    }

    var initialized by remember { mutableStateOf(false) }

    LaunchedEffect(listState) {
        snapshotFlow { centeredIndex }
            .distinctUntilChanged()
            .collect { idx ->
                onFocusedIndexChanged(idx)
                if (initialized) {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                } else {
                    initialized = true
                }
            }
    }

    val surfaceBg = MaterialTheme.colorScheme.background
    val lensBg = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.42f)
    val lensBorder = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val verticalPad = ((maxHeight - itemHeight) / 2f).coerceAtLeast(0.dp)

        // Center Tonal Spotlight Lens behind focused slot
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(itemHeight + 8.dp),
            shape = RoundedCornerShape(28.dp),
            color = lensBg,
            border = BorderStroke(1.5.dp, lensBorder)
        ) {}

        LazyColumn(
            state = listState,
            flingBehavior = snapBehavior,
            contentPadding = PaddingValues(vertical = verticalPad),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .testTag(wheelTag)
        ) {
            itemsIndexed(
                items = items,
                key = { _, item -> keySelector(item) }
            ) { index, item ->
                val isCentered = index == centeredIndex
                val interactionSource = remember { MutableInteractionSource() }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .padding(horizontal = 24.dp)
                        .graphicsLayer {
                            val layoutInfo = listState.layoutInfo
                            val info = layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
                            val viewportCenter =
                                (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2f
                            val norm = if (info != null && itemHeightPx > 0f) {
                                val itemCenter = info.offset + info.size / 2f
                                ((itemCenter - viewportCenter) / itemHeightPx).coerceIn(-2.6f, 2.6f)
                            } else {
                                (index - centeredIndex).toFloat().coerceIn(-2.6f, 2.6f)
                            }

                            val absNorm = abs(norm)
                            val scale = if (absNorm <= 1f) {
                                lerp(1.08f, 0.82f, absNorm)
                            } else {
                                lerp(0.82f, 0.66f, (absNorm - 1f).coerceIn(0f, 1f))
                            }
                            val alphaVal = if (absNorm <= 1f) {
                                lerp(1.0f, 0.42f, absNorm)
                            } else {
                                lerp(0.42f, 0.18f, (absNorm - 1f).coerceIn(0f, 1f))
                            }

                            scaleX = scale
                            scaleY = scale
                            alpha = alphaVal
                            rotationX = (-norm * 14f).coerceIn(-36f, 36f)
                            cameraDistance = 12f * density.density
                        }
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            if (index == centeredIndex) {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onCenteredItemTapped(index)
                            } else {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                coroutineScope.launch {
                                    listState.animateScrollToItem(index)
                                }
                            }
                        }
                        .testTag("${wheelTag}_item_$index")
                ) {
                    itemContent(item, isCentered)
                }
            }
        }

        // Top soft fade mask for physical drum depth
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(44.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(surfaceBg, Color.Transparent)
                    )
                )
        )

        // Bottom soft fade mask
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(44.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, surfaceBg)
                    )
                )
        )
    }
}
