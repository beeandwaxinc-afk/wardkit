package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.data.ClinicalTier
import com.example.data.ThemeMode

data class MedWheelThemeExtras(
    val isDark: Boolean,
    val isAmoled: Boolean,
    val tierGreen: TierColors,
    val tierAmber: TierColors,
    val tierRed: TierColors,
    val tierBlue: TierColors
) {
    fun colorsForTier(tier: ClinicalTier): TierColors = when (tier) {
        ClinicalTier.GREEN -> tierGreen
        ClinicalTier.AMBER -> tierAmber
        ClinicalTier.RED -> tierRed
        ClinicalTier.BLUE -> tierBlue
    }
}

val LocalMedWheelExtras = staticCompositionLocalOf {
    MedWheelThemeExtras(
        isDark = false,
        isAmoled = false,
        tierGreen = TierGreenLight,
        tierAmber = TierAmberLight,
        tierRed = TierRedLight,
        tierBlue = TierBlueLight
    )
}

@Composable
fun MedWheelTheme(
    themeMode: ThemeMode,
    activePalette: DepartmentPalette = DeptTeal,
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> systemDark
        ThemeMode.LIGHT -> false
        ThemeMode.AMOLED_DARK -> true
    }
    val isAmoled = themeMode == ThemeMode.AMOLED_DARK

    // Memoized crisp color scheme — avoids 20-frame root-tree animateColorAsState recompositions
    val colorScheme = remember(isDark, isAmoled, activePalette) {
        val primary = if (isDark) activePalette.darkPrimary else activePalette.lightPrimary
        val onPrimary = if (isDark) activePalette.darkOnPrimary else activePalette.lightOnPrimary
        val primaryContainer = if (isDark) activePalette.darkContainer else activePalette.lightContainer
        val onPrimaryContainer = if (isDark) activePalette.darkOnContainer else activePalette.lightOnContainer

        if (isDark) {
            darkColorScheme(
                primary = primary,
                onPrimary = onPrimary,
                primaryContainer = primaryContainer,
                onPrimaryContainer = onPrimaryContainer,
                secondary = primary,
                onSecondary = onPrimary,
                secondaryContainer = primaryContainer,
                onSecondaryContainer = onPrimaryContainer,
                tertiary = primary,
                background = if (isAmoled) AmoledBackground else DarkBackground,
                onBackground = AmoledOnSurface,
                surface = if (isAmoled) AmoledSurface else DarkSurface,
                onSurface = AmoledOnSurface,
                surfaceVariant = if (isAmoled) AmoledSurfaceContainerHigh else DarkSurfaceContainerHigh,
                onSurfaceVariant = AmoledOnSurfaceVariant,
                surfaceContainerLowest = if (isAmoled) AmoledSurfaceContainerLowest else DarkSurfaceContainerLowest,
                surfaceContainerLow = if (isAmoled) AmoledSurfaceContainerLow else DarkSurfaceContainerLow,
                surfaceContainer = if (isAmoled) AmoledSurfaceContainer else DarkSurfaceContainer,
                surfaceContainerHigh = if (isAmoled) AmoledSurfaceContainerHigh else DarkSurfaceContainerHigh,
                surfaceContainerHighest = if (isAmoled) AmoledSurfaceContainerHighest else DarkSurfaceContainerHighest,
                outline = AmoledOutline,
                outlineVariant = AmoledOutlineVariant,
                error = TierRedDark.badge,
                errorContainer = TierRedDark.container,
                onErrorContainer = TierRedDark.onContainer
            )
        } else {
            lightColorScheme(
                primary = primary,
                onPrimary = onPrimary,
                primaryContainer = primaryContainer,
                onPrimaryContainer = onPrimaryContainer,
                secondary = primary,
                onSecondary = onPrimary,
                secondaryContainer = primaryContainer,
                onSecondaryContainer = onPrimaryContainer,
                tertiary = primary,
                background = LightBackground,
                onBackground = LightOnSurface,
                surface = LightSurface,
                onSurface = LightOnSurface,
                surfaceVariant = LightSurfaceContainerHigh,
                onSurfaceVariant = LightOnSurfaceVariant,
                surfaceContainerLowest = LightSurfaceContainerLowest,
                surfaceContainerLow = LightSurfaceContainerLow,
                surfaceContainer = LightSurfaceContainer,
                surfaceContainerHigh = LightSurfaceContainerHigh,
                surfaceContainerHighest = LightSurfaceContainerHighest,
                outline = LightOutline,
                outlineVariant = LightOutlineVariant,
                error = TierRedLight.badge,
                errorContainer = TierRedLight.container,
                onErrorContainer = TierRedLight.onContainer
            )
        }
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = !isDark
            controller.isAppearanceLightNavigationBars = !isDark
        }
    }

    val extras = remember(isDark, isAmoled) {
        MedWheelThemeExtras(
            isDark = isDark,
            isAmoled = isAmoled,
            tierGreen = if (isDark) TierGreenDark else TierGreenLight,
            tierAmber = if (isDark) TierAmberDark else TierAmberLight,
            tierRed = if (isDark) TierRedDark else TierRedLight,
            tierBlue = if (isDark) TierBlueDark else TierBlueLight
        )
    }

    CompositionLocalProvider(LocalMedWheelExtras provides extras) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = MedWheelTypography,
            content = content
        )
    }
}
