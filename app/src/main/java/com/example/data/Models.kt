package com.example.data

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.DepartmentPalette

enum class ThemeMode(val key: String) {
    SYSTEM("SYSTEM"),
    LIGHT("LIGHT"),
    AMOLED_DARK("AMOLED_DARK");

    companion object {
        fun fromKey(key: String?): ThemeMode =
            entries.firstOrNull { it.key == key } ?: SYSTEM
    }
}

enum class MainTab {
    CALCULATORS,
    KIT,
    PINNED
}

enum class WheelStep {
    DEPARTMENTS,
    CALCULATORS,
    CANVAS
}

enum class DepartmentId {
    PEDIATRICS,
    INTERNAL_MEDICINE,
    CARDIOLOGY,
    GENERAL_SURGERY,
    OBS_GYNAE,
    NEURO,
    ICU,
    NEPHROLOGY,
    GASTRO,
    EMERGENCY
}

data class Department(
    val id: DepartmentId,
    val wheelName: String,
    val fullName: String,
    val subtitle: String,
    val palette: DepartmentPalette,
    val icon: ImageVector
)

enum class ClinicalTier {
    GREEN,
    AMBER,
    RED,
    BLUE
}

data class ChoiceOption(
    val id: String,
    val label: String,
    val detail: String = "",
    val pointsBadge: String? = null,
    val points: Int = 0
)

sealed interface CalcInputDef {
    val id: String
    val label: String

    data class SectionHeader(
        override val id: String,
        override val label: String,
        val subtitle: String = "",
        val visibleWhenChoiceKey: String? = null,
        val visibleWhenChoiceValue: String? = null
    ) : CalcInputDef

    data class NumericField(
        override val id: String,
        override val label: String,
        val hint: String = "",
        val units: List<String> = emptyList(),
        val defaultUnitIndex: Int = 0,
        val optional: Boolean = false,
        val allowZero: Boolean = false,
        val visibleWhenChoiceKey: String? = null,
        val visibleWhenChoiceValue: String? = null
    ) : CalcInputDef

    data class SegmentedChoice(
        override val id: String,
        override val label: String,
        val subtitle: String = "",
        val options: List<ChoiceOption>,
        val defaultOptionId: String? = null,
        val compactRow: Boolean = true,
        val visibleWhenChoiceKey: String? = null,
        val visibleWhenChoiceValue: String? = null
    ) : CalcInputDef

    data class ToggleCriterion(
        override val id: String,
        override val label: String,
        val detail: String = "",
        val pointsBadge: String,
        val points: Int,
        val exclusiveGroupWithId: String? = null,
        val exclusiveGroupKey: String? = null,
        val visibleWhenChoiceKey: String? = null,
        val visibleWhenChoiceValue: String? = null
    ) : CalcInputDef

    data class DateInput(
        override val id: String,
        override val label: String,
        val hint: String = "Tap to pick date"
    ) : CalcInputDef
}

data class CalculatorDef(
    val id: String,
    val departmentId: DepartmentId,
    val name: String,
    val shortSubtitle: String,
    val synonyms: List<String>,
    val formulaSummary: String,
    val clinicalPearl: String,
    val inputs: List<CalcInputDef>,
    val isChecklistZeroValid: Boolean = false
)

data class CalculatorInputState(
    val numericValues: Map<String, String> = emptyMap(),
    val selectedUnits: Map<String, Int> = emptyMap(),
    val selectedChoices: Map<String, String> = emptyMap(),
    val toggleValues: Map<String, Boolean> = emptyMap(),
    val selectedDates: Map<String, Long> = emptyMap()
) {
    fun number(id: String): Double? =
        numericValues[id]?.trim()?.replace(',', '.')?.toDoubleOrNull()

    fun unitIndex(id: String, defaultIndex: Int = 0): Int =
        selectedUnits[id] ?: defaultIndex

    fun choice(id: String, defaultId: String? = null): String? =
        selectedChoices[id] ?: defaultId

    fun toggle(id: String): Boolean =
        toggleValues[id] == true

    fun dateMillis(id: String): Long? =
        selectedDates[id]
}

data class SecondaryOutput(
    val label: String,
    val value: String,
    val highlight: Boolean = false
)

data class CalculationResult(
    val isReady: Boolean = false,
    val emptyPrompt: String = "Enter values below",
    val primaryValue: String = "",
    val primaryUnit: String = "",
    val tierLabel: String? = null,
    val tier: ClinicalTier = ClinicalTier.BLUE,
    val secondaryOutputs: List<SecondaryOutput> = emptyList(),
    val clinicalNote: String? = null,
    val warnings: List<String> = emptyList()
)
