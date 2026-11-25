package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.data.CalcInputDef
import com.example.data.CalculationResult
import com.example.data.CalculatorDef
import com.example.data.CalculatorInputState
import com.example.data.CalculatorRegistry
import com.example.data.Department
import com.example.data.DepartmentId
import com.example.data.DepartmentRegistry
import com.example.data.MainTab
import com.example.data.MedWheelPreferences
import com.example.data.ThemeMode
import com.example.data.WheelStep
import com.example.data.kit.KitBlockId
import com.example.data.kit.KitCardDef
import com.example.data.kit.KitRegistry
import com.example.data.kit.KitSearchHit
import com.example.data.kit.LabUnitSystem
import com.example.data.kit.SearchFilter
import com.example.domain.FormulaEngine
import com.example.ui.theme.DepartmentPalette
import com.example.ui.theme.DeptTeal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MedWheelUiState(
    val activeTab: MainTab = MainTab.CALCULATORS,
    val wheelStep: WheelStep = WheelStep.DEPARTMENTS,
    val focusedDepartmentIndex: Int = 0,
    val selectedDepartmentId: DepartmentId = DepartmentRegistry.all.first().id,
    val focusedCalculatorIndex: Int = 0,
    val activeCalculatorId: String? = null,
    val activeKitBlockId: KitBlockId? = null,
    val activeKitCardId: String? = null,
    val focusedDoseDrugId: String? = null,
    val returnToTabOnBack: MainTab? = null,
    val searchQuery: String = "",
    val searchFilter: SearchFilter = SearchFilter.ALL,
    val isSearchActive: Boolean = false,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val labUnitSystem: LabUnitSystem = LabUnitSystem.CONV,
    val pinnedIds: List<String> = emptyList(),
    val pinnedKitIds: List<String> = emptyList(),
    val usageCounts: Map<String, Int> = emptyMap(),
    val inputState: CalculatorInputState = CalculatorInputState(),
    val calculationResult: CalculationResult = CalculationResult()
) {
    val activeDepartment: Department
        get() = when {
            activeCalculatorId != null -> {
                val calc = CalculatorRegistry.get(activeCalculatorId)
                DepartmentRegistry.get(calc?.departmentId ?: selectedDepartmentId)
            }
            activeTab == MainTab.CALCULATORS && wheelStep == WheelStep.DEPARTMENTS -> {
                DepartmentRegistry.all.getOrElse(focusedDepartmentIndex) { DepartmentRegistry.all.first() }
            }
            else -> DepartmentRegistry.get(selectedDepartmentId)
        }

    val activePalette: DepartmentPalette
        get() = when {
            activeTab == MainTab.KIT && activeKitBlockId != null ->
                KitRegistry.getBlock(activeKitBlockId).palette
            activeTab == MainTab.KIT -> DeptTeal
            else -> activeDepartment.palette
        }

    val activeCalculator: CalculatorDef?
        get() = activeCalculatorId?.let { CalculatorRegistry.get(it) }

    val searchResults: List<CalculatorDef>
        get() = CalculatorRegistry.search(searchQuery)

    val kitSearchHits: List<KitSearchHit>
        get() = KitRegistry.searchUnified(searchQuery)

    val mostUsedCalculators: List<Pair<CalculatorDef, Int>>
        get() = usageCounts.entries
            .filter { it.value > 0 }
            .sortedWith(compareByDescending<Map.Entry<String, Int>> { it.value }.thenBy { it.key })
            .mapNotNull { (id, count) -> CalculatorRegistry.get(id)?.let { it to count } }
            .take(6)

    val pinnedCalculators: List<CalculatorDef>
        get() = pinnedIds.mapNotNull { CalculatorRegistry.get(it) }

    val pinnedKitCards: List<KitCardDef>
        get() = pinnedKitIds.mapNotNull { KitRegistry.getCard(it) }

    val isInsideCanvas: Boolean
        get() = activeTab == MainTab.CALCULATORS && wheelStep == WheelStep.CANVAS && activeCalculatorId != null

    val isInsideKitCard: Boolean
        get() = activeTab == MainTab.KIT && activeKitBlockId != null && activeKitCardId != null
}

class MedWheelViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = MedWheelPreferences(application)
    private var usageCountedForSession = false

    private val _uiState = MutableStateFlow(
        MedWheelUiState(
            themeMode = prefs.loadThemeMode(),
            labUnitSystem = prefs.loadLabUnitSystem(),
            pinnedIds = prefs.loadPinnedIds(),
            pinnedKitIds = prefs.loadPinnedKitIds(),
            usageCounts = prefs.loadUsageCounts()
        )
    )
    val uiState: StateFlow<MedWheelUiState> = _uiState.asStateFlow()

    fun selectTab(tab: MainTab) {
        _uiState.update { state ->
            when (tab) {
                MainTab.CALCULATORS -> state.copy(
                    activeTab = MainTab.CALCULATORS,
                    wheelStep = if (state.wheelStep == WheelStep.CANVAS) WheelStep.DEPARTMENTS else state.wheelStep,
                    activeCalculatorId = if (state.wheelStep == WheelStep.CANVAS) null else state.activeCalculatorId,
                    returnToTabOnBack = null
                )
                MainTab.KIT -> state.copy(
                    activeTab = MainTab.KIT,
                    activeKitBlockId = null,
                    activeKitCardId = null,
                    focusedDoseDrugId = null,
                    returnToTabOnBack = null,
                    isSearchActive = false,
                    searchQuery = ""
                )
                MainTab.PINNED -> state.copy(
                    activeTab = MainTab.PINNED,
                    isSearchActive = false,
                    searchQuery = ""
                )
            }
        }
    }

    fun openKitBlock(blockId: KitBlockId) {
        val firstCard = KitRegistry.cardsForBlock(blockId).firstOrNull() ?: return
        _uiState.update { state ->
            state.copy(
                activeTab = MainTab.KIT,
                activeKitBlockId = blockId,
                activeKitCardId = firstCard.id,
                focusedDoseDrugId = null,
                returnToTabOnBack = null,
                isSearchActive = false,
                searchQuery = ""
            )
        }
    }

    fun openKitCard(cardId: String, fromPinnedOrSearch: Boolean = false) {
        val card = KitRegistry.getCard(cardId) ?: return
        _uiState.update { state ->
            val returnTab = if (fromPinnedOrSearch && state.activeTab == MainTab.PINNED) {
                MainTab.PINNED
            } else {
                null
            }
            state.copy(
                activeTab = MainTab.KIT,
                activeKitBlockId = card.blockId,
                activeKitCardId = card.id,
                focusedDoseDrugId = null,
                returnToTabOnBack = returnTab,
                isSearchActive = false,
                searchQuery = ""
            )
        }
    }

    fun openDoseDrug(tabCardId: String, drugId: String) {
        val card = KitRegistry.getCard(tabCardId) ?: return
        _uiState.update { state ->
            state.copy(
                activeTab = MainTab.KIT,
                activeKitBlockId = KitBlockId.DOSE_ENGINE,
                activeKitCardId = card.id,
                focusedDoseDrugId = drugId,
                returnToTabOnBack = null,
                isSearchActive = false,
                searchQuery = ""
            )
        }
    }

    fun clearFocusedDoseDrug() {
        _uiState.update { it.copy(focusedDoseDrugId = null) }
    }

    fun selectKitCard(cardId: String) {
        val card = KitRegistry.getCard(cardId) ?: return
        _uiState.update { state ->
            state.copy(
                activeKitBlockId = card.blockId,
                activeKitCardId = card.id
            )
        }
    }

    fun togglePinKitCard(cardId: String) {
        _uiState.update { state ->
            val next = if (cardId in state.pinnedKitIds) {
                state.pinnedKitIds.filterNot { it == cardId }
            } else {
                listOf(cardId) + state.pinnedKitIds
            }
            prefs.savePinnedKitIds(next)
            state.copy(pinnedKitIds = next)
        }
    }

    fun setLabUnitSystem(system: LabUnitSystem) {
        prefs.saveLabUnitSystem(system)
        _uiState.update { it.copy(labUnitSystem = system) }
    }

    fun setSearchFilter(filter: SearchFilter) {
        _uiState.update { it.copy(searchFilter = filter) }
    }

    fun focusDepartment(index: Int) {
        val clamped = index.coerceIn(0, DepartmentRegistry.all.lastIndex)
        val dept = DepartmentRegistry.all[clamped]
        _uiState.update { state ->
            if (state.focusedDepartmentIndex == clamped) state
            else state.copy(
                focusedDepartmentIndex = clamped,
                selectedDepartmentId = dept.id
            )
        }
    }

    fun openDepartment(departmentId: DepartmentId) {
        val deptIdx = DepartmentRegistry.indexOf(departmentId)
        _uiState.update { state ->
            state.copy(
                activeTab = MainTab.CALCULATORS,
                wheelStep = WheelStep.CALCULATORS,
                focusedDepartmentIndex = deptIdx,
                selectedDepartmentId = departmentId,
                focusedCalculatorIndex = 0,
                activeCalculatorId = null,
                returnToTabOnBack = null,
                isSearchActive = false,
                searchQuery = ""
            )
        }
    }

    fun focusCalculator(index: Int) {
        _uiState.update { state ->
            val calcs = CalculatorRegistry.forDepartment(state.selectedDepartmentId)
            val clamped = index.coerceIn(0, (calcs.lastIndex).coerceAtLeast(0))
            if (state.focusedCalculatorIndex == clamped) state
            else state.copy(focusedCalculatorIndex = clamped)
        }
    }

    fun openCalculator(calculatorId: String, fromPinnedOrSearch: Boolean = false) {
        val calc = CalculatorRegistry.get(calculatorId) ?: return
        val deptIdx = DepartmentRegistry.indexOf(calc.departmentId)
        val deptCalcs = CalculatorRegistry.forDepartment(calc.departmentId)
        val calcIdx = deptCalcs.indexOfFirst { it.id == calculatorId }.coerceAtLeast(0)
        usageCountedForSession = false

        val freshInput = CalculatorInputState()
        val initialResult = FormulaEngine.evaluate(calculatorId, freshInput)

        _uiState.update { state ->
            val returnTab = if (fromPinnedOrSearch && state.activeTab == MainTab.PINNED) {
                MainTab.PINNED
            } else {
                null
            }
            state.copy(
                activeTab = MainTab.CALCULATORS,
                wheelStep = WheelStep.CANVAS,
                focusedDepartmentIndex = deptIdx,
                selectedDepartmentId = calc.departmentId,
                focusedCalculatorIndex = calcIdx,
                activeCalculatorId = calculatorId,
                returnToTabOnBack = returnTab,
                isSearchActive = false,
                searchQuery = "",
                inputState = freshInput,
                calculationResult = initialResult
            )
        }
    }

    fun navigateBack() {
        _uiState.update { state ->
            if (state.isSearchActive) {
                return@update state.copy(isSearchActive = false, searchQuery = "")
            }
            if (state.activeTab == MainTab.KIT) {
                return@update if (state.activeKitBlockId != null) {
                    if (state.returnToTabOnBack == MainTab.PINNED) {
                        state.copy(
                            activeTab = MainTab.PINNED,
                            activeKitBlockId = null,
                            activeKitCardId = null,
                            focusedDoseDrugId = null,
                            returnToTabOnBack = null
                        )
                    } else {
                        state.copy(
                            activeKitBlockId = null,
                            activeKitCardId = null,
                            focusedDoseDrugId = null,
                            returnToTabOnBack = null
                        )
                    }
                } else {
                    state.copy(activeTab = MainTab.CALCULATORS)
                }
            }
            if (state.activeTab == MainTab.PINNED) {
                return@update state.copy(activeTab = MainTab.CALCULATORS)
            }
            when (state.wheelStep) {
                WheelStep.CANVAS -> {
                    usageCountedForSession = false
                    if (state.returnToTabOnBack == MainTab.PINNED) {
                        state.copy(
                            activeTab = MainTab.PINNED,
                            wheelStep = WheelStep.DEPARTMENTS,
                            activeCalculatorId = null,
                            returnToTabOnBack = null,
                            inputState = CalculatorInputState(),
                            calculationResult = CalculationResult()
                        )
                    } else {
                        state.copy(
                            wheelStep = WheelStep.CALCULATORS,
                            activeCalculatorId = null,
                            returnToTabOnBack = null,
                            inputState = CalculatorInputState(),
                            calculationResult = CalculationResult()
                        )
                    }
                }
                WheelStep.CALCULATORS -> state.copy(
                    wheelStep = WheelStep.DEPARTMENTS,
                    activeCalculatorId = null
                )
                WheelStep.DEPARTMENTS -> state
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { state ->
            state.copy(
                searchQuery = query,
                isSearchActive = query.isNotEmpty() || state.isSearchActive
            )
        }
    }

    fun setSearchActive(active: Boolean) {
        _uiState.update { state ->
            state.copy(
                isSearchActive = active,
                searchQuery = if (!active) "" else state.searchQuery
            )
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        prefs.saveThemeMode(mode)
        _uiState.update { it.copy(themeMode = mode) }
    }

    fun togglePin(calculatorId: String) {
        _uiState.update { state ->
            val next = if (calculatorId in state.pinnedIds) {
                state.pinnedIds.filterNot { it == calculatorId }
            } else {
                listOf(calculatorId) + state.pinnedIds
            }
            prefs.savePinnedIds(next)
            state.copy(pinnedIds = next)
        }
    }

    fun updateNumericInput(fieldId: String, rawValue: String) {
        // Allow only valid decimal numeric characters (digits and at most one decimal point)
        val filtered = sanitizeDecimal(rawValue)
        mutateInputState { prev ->
            prev.copy(numericValues = prev.numericValues + (fieldId to filtered))
        }
    }

    fun updateUnit(fieldId: String, unitIndex: Int) {
        mutateInputState { prev ->
            prev.copy(selectedUnits = prev.selectedUnits + (fieldId to unitIndex))
        }
    }

    fun updateChoice(fieldId: String, optionId: String) {
        mutateInputState { prev ->
            prev.copy(selectedChoices = prev.selectedChoices + (fieldId to optionId))
        }
    }

    fun toggleCriterion(fieldId: String) {
        val calc = _uiState.value.activeCalculator ?: return
        val toggles = calc.inputs.filterIsInstance<CalcInputDef.ToggleCriterion>()
        val def = toggles.firstOrNull { it.id == fieldId }
        mutateInputState { prev ->
            val nextVal = !prev.toggle(fieldId)
            val updated = prev.toggleValues.toMutableMap()
            updated[fieldId] = nextVal
            if (nextVal) {
                if (def?.exclusiveGroupWithId != null) {
                    updated[def.exclusiveGroupWithId] = false
                }
                val groupKey = def?.exclusiveGroupKey
                if (groupKey != null) {
                    toggles.filter { it.exclusiveGroupKey == groupKey && it.id != fieldId }
                        .forEach { updated[it.id] = false }
                }
            }
            prev.copy(toggleValues = updated)
        }
    }

    fun updateDate(fieldId: String, utcMillis: Long) {
        mutateInputState { prev ->
            prev.copy(selectedDates = prev.selectedDates + (fieldId to utcMillis))
        }
    }

    fun clearInputs() {
        val calcId = _uiState.value.activeCalculatorId ?: return
        usageCountedForSession = false
        val empty = CalculatorInputState()
        val result = FormulaEngine.evaluate(calcId, empty)
        _uiState.update { it.copy(inputState = empty, calculationResult = result) }
    }

    private fun mutateInputState(transform: (CalculatorInputState) -> CalculatorInputState) {
        val calcId = _uiState.value.activeCalculatorId ?: return
        _uiState.update { state ->
            val nextInput = transform(state.inputState)
            val nextResult = FormulaEngine.evaluate(calcId, nextInput)
            var nextUsage = state.usageCounts
            if (nextResult.isReady && !usageCountedForSession) {
                usageCountedForSession = true
                nextUsage = prefs.incrementUsageCount(calcId)
            }
            state.copy(
                inputState = nextInput,
                calculationResult = nextResult,
                usageCounts = nextUsage
            )
        }
    }

    private fun sanitizeDecimal(input: String): String {
        val normalized = input.replace(',', '.')
        val sb = StringBuilder()
        var hasDot = false
        for (ch in normalized) {
            when {
                ch.isDigit() -> sb.append(ch)
                ch == '.' && !hasDot -> {
                    hasDot = true
                    sb.append(ch)
                }
            }
        }
        return sb.toString().take(10)
    }
}
