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
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * Locked 60fps draw-phase RotaryWheel:
 * - Zero layoutInfo / scroll-offset reads during composition (strictly inside Modifier.graphicsLayer).
 * - centeredIndex computed strictly via derivedStateOf so recompositions never fire per-pixel.
 * - Haptic tick & focus callback fire inside LaunchedEffect(centeredIndex) only on integer index change.
 */
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
    listState: LazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialIndex.coerceIn(0, (items.lastIndex).coerceAtLeast(0))
    ),
    accentPrimary: Color = MaterialTheme.colorScheme.primary,
    accentContainer: Color = MaterialTheme.colorScheme.primaryContainer,
    itemContent: @Composable (item: T, isCentered: Boolean) -> Unit
) {
    if (items.isEmpty()) return

    val safeInitial = initialIndex.coerceIn(0, items.lastIndex)
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current
    val density = LocalDensity.current
    val itemHeightPx = with(density) { itemHeight.toPx() }
    val latestOnFocusedChanged by rememberUpdatedState(onFocusedIndexChanged)
    val latestOnCenteredTapped by rememberUpdatedState(onCenteredItemTapped)

    // Derived snapped integer index — emits ONLY when the centered item index actually changes
    val centeredIndexState = remember(listState, items.size) {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visible = layoutInfo.visibleItemsInfo
            if (visible.isEmpty()) {
                safeInitial
            } else {
                val viewportCenter =
                    (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) * 0.5f
                var bestIndex = safeInitial
                var bestDist = Float.MAX_VALUE
                for (i in visible.indices) {
                    val info = visible[i]
                    val itemCenter = info.offset + info.size * 0.5f
                    val dist = abs(itemCenter - viewportCenter)
                    if (dist < bestDist) {
                        bestDist = dist
                        bestIndex = info.index
                    }
                }
                bestIndex.coerceIn(0, items.lastIndex)
            }
        }
    }

    var initialized by remember(listState) { mutableStateOf(false) }

    LaunchedEffect(centeredIndexState.value) {
        val idx = centeredIndexState.value
        latestOnFocusedChanged(idx)
        if (initialized) {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        } else {
            initialized = true
        }
    }

    val surfaceBg = MaterialTheme.colorScheme.background
    val lensBg = accentContainer.copy(alpha = 0.42f)
    val lensBorder = accentPrimary.copy(alpha = 0.35f)
    val topFadeBrush = remember(surfaceBg) {
        Brush.verticalGradient(colors = listOf(surfaceBg, Color.Transparent))
    }
    val bottomFadeBrush = remember(surfaceBg) {
        Brush.verticalGradient(colors = listOf(Color.Transparent, surfaceBg))
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val verticalPad = ((maxHeight - itemHeight) / 2f).coerceAtLeast(0.dp)

        // Center Tonal Spotlight Lens behind focused slot (flat, zero GPU shadow overdraw)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(itemHeight + 8.dp),
            shape = RoundedCornerShape(28.dp),
            color = lensBg,
            border = BorderStroke(1.5.dp, lensBorder),
            shadowElevation = 0.dp
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
                key = { _, item -> keySelector(item) },
                contentType = { _, _ -> "wheel_item" }
            ) { index, item ->
                val interactionSource = remember { MutableInteractionSource() }
                val isCentered by remember(centeredIndexState, index) {
                    derivedStateOf { centeredIndexState.value == index }
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .padding(horizontal = 24.dp)
                        .graphicsLayer {
                            // Strictly draw-phase scroll math: zero composition reads during flings
                            val layoutInfo = listState.layoutInfo
                            val visible = layoutInfo.visibleItemsInfo
                            var itemOffset = 0
                            var itemSize = 0
                            var found = false
                            for (i in visible.indices) {
                                val info = visible[i]
                                if (info.index == index) {
                                    itemOffset = info.offset
                                    itemSize = info.size
                                    found = true
                                    break
                                }
                            }
                            val norm = if (found && itemHeightPx > 0f) {
                                val viewportCenter =
                                    (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) * 0.5f
                                val itemCenter = itemOffset + itemSize * 0.5f
                                ((itemCenter - viewportCenter) / itemHeightPx).coerceIn(-2.6f, 2.6f)
                            } else {
                                2.6f
                            }

                            val absNorm = abs(norm)
                            val scale = if (absNorm <= 1f) {
                                lerp(1.06f, 0.84f, absNorm)
                            } else {
                                lerp(0.84f, 0.70f, (absNorm - 1f).coerceIn(0f, 1f))
                            }
                            val alphaVal = if (absNorm <= 1f) {
                                lerp(1.0f, 0.44f, absNorm)
                            } else {
                                lerp(0.44f, 0.18f, (absNorm - 1f).coerceIn(0f, 1f))
                            }

                            scaleX = scale
                            scaleY = scale
                            alpha = alphaVal
                            rotationX = (-norm * 8f).coerceIn(-16f, 16f)
                            cameraDistance = 14f * density.density
                        }
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            if (centeredIndexState.value == index) {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                latestOnCenteredTapped(index)
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

        // Top soft fade mask for drum depth
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(44.dp)
                .background(topFadeBrush)
        )

        // Bottom soft fade mask
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(44.dp)
                .background(bottomFadeBrush)
        )
    }
}
