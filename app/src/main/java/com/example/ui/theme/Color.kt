package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================================================
// BASE LIGHT & AMOLED DARK NEUTRAL TONAL SURFACES
// ============================================================================
val LightBackground = Color(0xFFF6F8FA)
val LightSurface = Color(0xFFF6F8FA)
val LightSurfaceContainerLowest = Color(0xFFFFFFFF)
val LightSurfaceContainerLow = Color(0xFFF0F3F6)
val LightSurfaceContainer = Color(0xFFE9EEF2)
val LightSurfaceContainerHigh = Color(0xFFE1E7EC)
val LightSurfaceContainerHighest = Color(0xFFD7DFE6)
val LightOnSurface = Color(0xFF101820)
val LightOnSurfaceVariant = Color(0xFF4B5A68)
val LightOutline = Color(0xFF7B8B9A)
val LightOutlineVariant = Color(0xFFCFD8E0)

// True AMOLED Dark (pure #000000 canvas + deep calibrated charcoal surfaces)
val AmoledBackground = Color(0xFF000000)
val AmoledSurface = Color(0xFF000000)
val AmoledSurfaceContainerLowest = Color(0xFF050608)
val AmoledSurfaceContainerLow = Color(0xFF0D1014)
val AmoledSurfaceContainer = Color(0xFF14181F)
val AmoledSurfaceContainerHigh = Color(0xFF1C222B)
val AmoledSurfaceContainerHighest = Color(0xFF252D38)
val AmoledOnSurface = Color(0xFFF2F6FA)
val AmoledOnSurfaceVariant = Color(0xFFA7B4C2)
val AmoledOutline = Color(0xFF5A6775)
val AmoledOutlineVariant = Color(0xFF28313D)

// Standard System Dark (deep slate dark)
val DarkBackground = Color(0xFF0E1217)
val DarkSurface = Color(0xFF0E1217)
val DarkSurfaceContainerLowest = Color(0xFF090C10)
val DarkSurfaceContainerLow = Color(0xFF151A21)
val DarkSurfaceContainer = Color(0xFF1B212A)
val DarkSurfaceContainerHigh = Color(0xFF232B36)
val DarkSurfaceContainerHighest = Color(0xFF2C3643)

// ============================================================================
// CLINICAL TIER SEMANTIC COLORS (GREEN / AMBER / RED / BLUE)
// ============================================================================
data class TierColors(
    val container: Color,
    val onContainer: Color,
    val badge: Color
)

val TierGreenLight = TierColors(Color(0xFFDDF5E4), Color(0xFF0B5326), Color(0xFF15803D))
val TierGreenDark = TierColors(Color(0xFF11351F), Color(0xFF86EFAC), Color(0xFF4ADE80))

val TierAmberLight = TierColors(Color(0xFFFFF0CE), Color(0xFF6B3E00), Color(0xFFB45309))
val TierAmberDark = TierColors(Color(0xFF3A2708), Color(0xFFFCD34D), Color(0xFFF59E0B))

val TierRedLight = TierColors(Color(0xFFFFE0E0), Color(0xFF7F1D1D), Color(0xFFDC2626))
val TierRedDark = TierColors(Color(0xFF3F1215), Color(0xFFFCA5A5), Color(0xFFF87171))

val TierBlueLight = TierColors(Color(0xFFDBEAFE), Color(0xFF1E3A8A), Color(0xFF2563EB))
val TierBlueDark = TierColors(Color(0xFF102446), Color(0xFF93C5FD), Color(0xFF60A5FA))

// ============================================================================
// PER-DEPARTMENT DYNAMIC MATERIAL 3 TONAL ACCENT PALETTES
// ============================================================================
data class DepartmentPalette(
    val lightPrimary: Color,
    val lightOnPrimary: Color,
    val lightContainer: Color,
    val lightOnContainer: Color,
    val darkPrimary: Color,
    val darkOnPrimary: Color,
    val darkContainer: Color,
    val darkOnContainer: Color
)

// 1. Pediatrics — Teal
val DeptTeal = DepartmentPalette(
    lightPrimary = Color(0xFF00796B),
    lightOnPrimary = Color(0xFFFFFFFF),
    lightContainer = Color(0xFFCFF4EE),
    lightOnContainer = Color(0xFF003831),
    darkPrimary = Color(0xFF4DD0C0),
    darkOnPrimary = Color(0xFF003730),
    darkContainer = Color(0xFF073A34),
    darkOnContainer = Color(0xFFB2F5EA)
)

// 2. Internal Medicine — Cobalt Blue
val DeptCobalt = DepartmentPalette(
    lightPrimary = Color(0xFF1565C0),
    lightOnPrimary = Color(0xFFFFFFFF),
    lightContainer = Color(0xFFD6E7FF),
    lightOnContainer = Color(0xFF072B59),
    darkPrimary = Color(0xFF6BB1FF),
    darkOnPrimary = Color(0xFF002E5C),
    darkContainer = Color(0xFF0B2E59),
    darkOnContainer = Color(0xFFCDE3FF)
)

// 3. Cardiology — Crimson
val DeptCrimson = DepartmentPalette(
    lightPrimary = Color(0xFFC62828),
    lightOnPrimary = Color(0xFFFFFFFF),
    lightContainer = Color(0xFFFFDAD9),
    lightOnContainer = Color(0xFF5E0B0B),
    darkPrimary = Color(0xFFFF6B6B),
    darkOnPrimary = Color(0xFF560008),
    darkContainer = Color(0xFF4A1115),
    darkOnContainer = Color(0xFFFFD1D1)
)

// 4. General Surgery — Amber
val DeptAmber = DepartmentPalette(
    lightPrimary = Color(0xFFB86B00),
    lightOnPrimary = Color(0xFFFFFFFF),
    lightContainer = Color(0xFFFFE9C4),
    lightOnContainer = Color(0xFF4C2900),
    darkPrimary = Color(0xFFFFB84D),
    darkOnPrimary = Color(0xFF472800),
    darkContainer = Color(0xFF422905),
    darkOnContainer = Color(0xFFFFE2B3)
)

// 5. Obs & Gynae — Magenta / Plum
val DeptMagenta = DepartmentPalette(
    lightPrimary = Color(0xFFAD1457),
    lightOnPrimary = Color(0xFFFFFFFF),
    lightContainer = Color(0xFFFFD8E6),
    lightOnContainer = Color(0xFF520026),
    darkPrimary = Color(0xFFF472B6),
    darkOnPrimary = Color(0xFF520029),
    darkContainer = Color(0xFF48102C),
    darkOnContainer = Color(0xFFFFCCE0)
)

// 6. Neuro & Neurosurgery — Indigo
val DeptIndigo = DepartmentPalette(
    lightPrimary = Color(0xFF3949AB),
    lightOnPrimary = Color(0xFFFFFFFF),
    lightContainer = Color(0xFFDDE1FF),
    lightOnContainer = Color(0xFF101C66),
    darkPrimary = Color(0xFF8C9EFF),
    darkOnPrimary = Color(0xFF121D66),
    darkContainer = Color(0xFF192259),
    darkOnContainer = Color(0xFFD8DEFF)
)

// 7. ICU & Critical Care — Coral Red
val DeptCoralRed = DepartmentPalette(
    lightPrimary = Color(0xFFD84315),
    lightOnPrimary = Color(0xFFFFFFFF),
    lightContainer = Color(0xFFFFDDD2),
    lightOnContainer = Color(0xFF5A1500),
    darkPrimary = Color(0xFFFF7A52),
    darkOnPrimary = Color(0xFF521200),
    darkContainer = Color(0xFF49180B),
    darkOnContainer = Color(0xFFFFD4C7)
)

// 8. Nephrology — Emerald
val DeptEmerald = DepartmentPalette(
    lightPrimary = Color(0xFF1B7A43),
    lightOnPrimary = Color(0xFFFFFFFF),
    lightContainer = Color(0xFFD2F5DF),
    lightOnContainer = Color(0xFF043B1B),
    darkPrimary = Color(0xFF52D68A),
    darkOnPrimary = Color(0xFF003919),
    darkContainer = Color(0xFF0A381F),
    darkOnContainer = Color(0xFFC2F7D6)
)

// 9. Gastro & Hepatology — Ochre / Gold
val DeptOchre = DepartmentPalette(
    lightPrimary = Color(0xFF9E6B00),
    lightOnPrimary = Color(0xFFFFFFFF),
    lightContainer = Color(0xFFFFECC2),
    lightOnContainer = Color(0xFF3E2800),
    darkPrimary = Color(0xFFF5C84C),
    darkOnPrimary = Color(0xFF3F2D00),
    darkContainer = Color(0xFF3B2C06),
    darkOnContainer = Color(0xFFFFE8AD)
)

// 10. Emergency & Anesthesia — Vermilion
val DeptVermilion = DepartmentPalette(
    lightPrimary = Color(0xFFE03E1A),
    lightOnPrimary = Color(0xFFFFFFFF),
    lightContainer = Color(0xFFFFDCD4),
    lightOnContainer = Color(0xFF5C1100),
    darkPrimary = Color(0xFFFF6D4D),
    darkOnPrimary = Color(0xFF540E00),
    darkContainer = Color(0xFF4A150A),
    darkOnContainer = Color(0xFFFFD3C9)
)
