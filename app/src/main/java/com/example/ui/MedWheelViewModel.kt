package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
import com.example.data.kit.KitBlockDef
import com.example.data.kit.KitBlockId
import com.example.data.kit.KitCardDef
import com.example.data.kit.KitRegistry
import com.example.data.kit.KitSearchHit
import com.example.data.kit.LaAndPeadsCoreData
import com.example.data.kit.LabUnitSystem
import com.example.data.kit.SearchFilter
import com.example.domain.FormulaEngine
import com.example.ui.theme.DepartmentPalette
import com.example.ui.theme.DeptTeal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Immutable UI state holding precomputed lists so Compose reads 100% from memory
 * with zero per-frame sorting, filtering, or SharedPreferences disk I/O.
 */
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
    val pinnedCalculators: List<CalculatorDef> = emptyList(),
    val pinnedKitCards: List<KitCardDef> = emptyList(),
    val mostUsedCalculators: List<Pair<CalculatorDef, Int>> = emptyList(),
    val searchResults: List<CalculatorDef> = emptyList(),
    val kitSearchHits: List<KitSearchHit> = emptyList(),
    val inputState: CalculatorInputState = CalculatorInputState(),
    val calculationResult: CalculationResult = CalculationResult()
) {
    /**
     * Keeps root MaterialTheme stable (DeptTeal) during Department Wheel flings so scrolling
     * never triggers full-tree root theme recomposition. Local spotlight & CTA tinting handle
     * per-department accent colors inside DepartmentWheelScreen.
     */
    val activePalette: DepartmentPalette
        get() = when {
            activeTab == MainTab.KIT && activeKitBlockId != null ->
                KitRegistry.getBlock(activeKitBlockId).palette
            activeTab == MainTab.KIT -> DeptTeal
            activeTab == MainTab.PINNED -> DeptTeal
            wheelStep == WheelStep.DEPARTMENTS -> DeptTeal
            activeCalculatorId != null -> {
                val calc = CalculatorRegistry.get(activeCalculatorId)
                DepartmentRegistry.get(calc?.departmentId ?: selectedDepartmentId).palette
            }
            else -> DepartmentRegistry.get(selectedDepartmentId).palette
        }

    val activeCalculator: CalculatorDef?
        get() = activeCalculatorId?.let { CalculatorRegistry.get(it) }

    val isInsideCanvas: Boolean
        get() = activeTab == MainTab.CALCULATORS && wheelStep == WheelStep.CANVAS && activeCalculatorId != null

    val isInsideKitCard: Boolean
        get() = activeTab == MainTab.KIT && activeKitBlockId != null && activeKitCardId != null
}

private data class IndexedCalcEntry(
    val calc: CalculatorDef,
    val nameLower: String,
    val subtitleLower: String,
    val keywordsLower: List<String>
)

private data class IndexedKitCardEntry(
    val hit: KitSearchHit.CardHit,
    val titleLower: String,
    val tabLabelLower: String,
    val subtitleLower: String,
    val blockTitleLower: String,
    val keywordsLower: List<String>
)

private data class IndexedDoseDrugEntry(
    val hit: KitSearchHit.DrugHit,
    val titleLower: String,
    val subtitleLower: String,
    val keywordsLower: List<String>
)

class MedWheelViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = MedWheelPreferences(application)
    private var usageCountedForSession = false
    private var searchJob: Job? = null

    // Pre-sorted immutable datasets built once at initialization (zero sorting inside Composables)
    val sortedDepartments: List<Department> = DepartmentRegistry.all
    val calculatorsByDepartment: Map<DepartmentId, List<CalculatorDef>> =
        DepartmentRegistry.all.associate { dept ->
            dept.id to CalculatorRegistry.forDepartment(dept.id)
        }
    val departmentToolCounts: Map<DepartmentId, Int> =
        calculatorsByDepartment.mapValues { it.value.size }
    val kitTier1Rows: List<List<KitBlockDef>> = KitRegistry.hospitalEssentialBlocks.chunked(2)
    val kitTier2Rows: List<List<KitBlockDef>> = KitRegistry.specialtyBlocks.chunked(2)

    // Pre-built flat search index (populated once on Dispatchers.Default)
    @Volatile
    private var indexedCalculators: List<IndexedCalcEntry> = emptyList()
    @Volatile
    private var indexedKitCards: List<IndexedKitCardEntry> = emptyList()
    @Volatile
    private var indexedDoseDrugs: List<IndexedDoseDrugEntry> = emptyList()

    // Dedicated in-memory StateFlows hydrated once off-main-thread
    private val _pinnedCalculatorIds = MutableStateFlow<List<String>>(emptyList())
    val pinnedCalculatorIds: StateFlow<List<String>> = _pinnedCalculatorIds.asStateFlow()

    private val _pinnedKitIds = MutableStateFlow<List<String>>(emptyList())
    val pinnedKitIds: StateFlow<List<String>> = _pinnedKitIds.asStateFlow()

    private val _calculatorUsageCounts = MutableStateFlow<Map<String, Int>>(emptyMap())
    val calculatorUsageCounts: StateFlow<Map<String, Int>> = _calculatorUsageCounts.asStateFlow()

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _labUnitSystem = MutableStateFlow(LabUnitSystem.CONV)
    val labUnitSystem: StateFlow<LabUnitSystem> = _labUnitSystem.asStateFlow()

    private val _uiState = MutableStateFlow(MedWheelUiState())
    val uiState: StateFlow<MedWheelUiState> = _uiState.asStateFlow()

    init {
        // Hydrate all SharedPreferences once on Dispatchers.IO (0ms main-thread blocking)
        viewModelScope.launch(Dispatchers.IO) {
            val loadedTheme = prefs.loadThemeMode()
            val loadedLabUnit = prefs.loadLabUnitSystem()
            val loadedPinned = prefs.loadPinnedIds()
            val loadedPinnedKit = prefs.loadPinnedKitIds()
            val loadedUsage = prefs.loadUsageCounts()

            val pinnedCalcs = resolvePinnedCalculators(loadedPinned)
            val pinnedCards = resolvePinnedKitCards(loadedPinnedKit)
            val mostUsed = resolveMostUsedCalculators(loadedUsage)

            _themeMode.value = loadedTheme
            _labUnitSystem.value = loadedLabUnit
            _pinnedCalculatorIds.value = loadedPinned
            _pinnedKitIds.value = loadedPinnedKit
            _calculatorUsageCounts.value = loadedUsage

            _uiState.update { state ->
                state.copy(
                    themeMode = loadedTheme,
                    labUnitSystem = loadedLabUnit,
                    pinnedIds = loadedPinned,
                    pinnedKitIds = loadedPinnedKit,
                    usageCounts = loadedUsage,
                    pinnedCalculators = pinnedCalcs,
                    pinnedKitCards = pinnedCards,
                    mostUsedCalculators = mostUsed
                )
            }
        }

        // Pre-build lightweight lowercased search index once off the main thread
        viewModelScope.launch(Dispatchers.Default) {
            ensureSearchIndexBuilt()
        }
    }

    private fun ensureSearchIndexBuilt() {
        if (indexedCalculators.isNotEmpty()) return
        indexedCalculators = CalculatorRegistry.all.map { calc ->
            IndexedCalcEntry(
                calc = calc,
                nameLower = calc.name.lowercase(),
                subtitleLower = calc.shortSubtitle.lowercase(),
                keywordsLower = calc.synonyms.map { it.lowercase() }
            )
        }
        indexedKitCards = KitRegistry.allCards.map { card ->
            val block = KitRegistry.getBlock(card.blockId)
            IndexedKitCardEntry(
                hit = KitSearchHit.CardHit(card),
                titleLower = card.title.lowercase(),
                tabLabelLower = card.tabLabel.lowercase(),
                subtitleLower = card.shortSubtitle.lowercase(),
                blockTitleLower = block.title.lowercase(),
                keywordsLower = card.keywords.map { it.lowercase() }
            )
        }
        indexedDoseDrugs = LaAndPeadsCoreData.searchableDrugs.map { entry ->
            IndexedDoseDrugEntry(
                hit = KitSearchHit.DrugHit(entry),
                titleLower = entry.title.lowercase(),
                subtitleLower = entry.subtitle.lowercase(),
                keywordsLower = entry.keywords.map { it.lowercase() }
            )
        }
    }

    private fun resolvePinnedCalculators(ids: List<String>): List<CalculatorDef> =
        ids.mapNotNull { CalculatorRegistry.get(it) }

    private fun resolvePinnedKitCards(ids: List<String>): List<KitCardDef> =
        ids.mapNotNull { KitRegistry.getCard(it) }

    private fun resolveMostUsedCalculators(usage: Map<String, Int>): List<Pair<CalculatorDef, Int>> =
        usage.entries
            .filter { it.value > 0 }
            .sortedWith(compareByDescending<Map.Entry<String, Int>> { it.value }.thenBy { it.key })
            .mapNotNull { (id, count) -> CalculatorRegistry.get(id)?.let { it to count } }
            .take(6)

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
                    searchQuery = "",
                    searchResults = emptyList(),
                    kitSearchHits = emptyList()
                )
                MainTab.PINNED -> state.copy(
                    activeTab = MainTab.PINNED,
                    isSearchActive = false,
                    searchQuery = "",
                    searchResults = emptyList(),
                    kitSearchHits = emptyList()
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
                searchQuery = "",
                searchResults = emptyList(),
                kitSearchHits = emptyList()
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
                searchQuery = "",
                searchResults = emptyList(),
                kitSearchHits = emptyList()
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
                searchQuery = "",
                searchResults = emptyList(),
                kitSearchHits = emptyList()
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
        val next = if (cardId in _pinnedKitIds.value) {
            _pinnedKitIds.value.filterNot { it == cardId }
        } else {
            listOf(cardId) + _pinnedKitIds.value
        }
        val resolved = resolvePinnedKitCards(next)
        _pinnedKitIds.value = next
        _uiState.update { state ->
            state.copy(
                pinnedKitIds = next,
                pinnedKitCards = resolved
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            prefs.savePinnedKitIds(next)
        }
    }

    fun setLabUnitSystem(system: LabUnitSystem) {
        _labUnitSystem.value = system
        _uiState.update { it.copy(labUnitSystem = system) }
        viewModelScope.launch(Dispatchers.IO) {
            prefs.saveLabUnitSystem(system)
        }
    }

    fun setSearchFilter(filter: SearchFilter) {
        _uiState.update { it.copy(searchFilter = filter) }
    }

    fun focusDepartment(index: Int) {
        val clamped = index.coerceIn(0, sortedDepartments.lastIndex)
        val dept = sortedDepartments[clamped]
        _uiState.update { state ->
            if (state.focusedDepartmentIndex == clamped && state.selectedDepartmentId == dept.id) state
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
                searchQuery = "",
                searchResults = emptyList(),
                kitSearchHits = emptyList()
            )
        }
    }

    fun focusCalculator(index: Int) {
        _uiState.update { state ->
            val calcs = calculatorsByDepartment[state.selectedDepartmentId].orEmpty()
            val clamped = index.coerceIn(0, calcs.lastIndex.coerceAtLeast(0))
            if (state.focusedCalculatorIndex == clamped) state
            else state.copy(focusedCalculatorIndex = clamped)
        }
    }

    fun openCalculator(calculatorId: String, fromPinnedOrSearch: Boolean = false) {
        val calc = CalculatorRegistry.get(calculatorId) ?: return
        val deptIdx = DepartmentRegistry.indexOf(calc.departmentId)
        val deptCalcs = calculatorsByDepartment[calc.departmentId].orEmpty()
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
                searchResults = emptyList(),
                kitSearchHits = emptyList(),
                inputState = freshInput,
                calculationResult = initialResult
            )
        }
    }

    fun navigateBack() {
        _uiState.update { state ->
            if (state.isSearchActive) {
                return@update state.copy(
                    isSearchActive = false,
                    searchQuery = "",
                    searchResults = emptyList(),
                    kitSearchHits = emptyList()
                )
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
        runAsyncSearch(query)
    }

    fun setSearchActive(active: Boolean) {
        if (!active) {
            searchJob?.cancel()
            _uiState.update { state ->
                state.copy(
                    isSearchActive = false,
                    searchQuery = "",
                    searchResults = emptyList(),
                    kitSearchHits = emptyList()
                )
            }
        } else {
            _uiState.update { it.copy(isSearchActive = true) }
        }
    }

    private fun runAsyncSearch(rawQuery: String) {
        searchJob?.cancel()
        val q = rawQuery.trim().lowercase()
        if (q.isEmpty()) {
            _uiState.update { it.copy(searchResults = emptyList(), kitSearchHits = emptyList()) }
            return
        }
        searchJob = viewModelScope.launch(Dispatchers.Default) {
            ensureSearchIndexBuilt()
            val calcMatches = indexedCalculators.asSequence()
                .filter { entry ->
                    entry.nameLower.contains(q) ||
                        entry.subtitleLower.contains(q) ||
                        entry.keywordsLower.any { it.contains(q) }
                }
                .map { it.calc }
                .toList()

            val cardMatches = indexedKitCards.asSequence()
                .filter { entry ->
                    entry.titleLower.contains(q) ||
                        entry.tabLabelLower.contains(q) ||
                        entry.subtitleLower.contains(q) ||
                        entry.blockTitleLower.contains(q) ||
                        entry.keywordsLower.any { it.contains(q) }
                }
                .map { it.hit }
                .toList()

            val drugMatches = indexedDoseDrugs.asSequence()
                .filter { entry ->
                    entry.titleLower.contains(q) ||
                        entry.subtitleLower.contains(q) ||
                        entry.keywordsLower.any { it.contains(q) }
                }
                .map { it.hit }
                .toList()

            val combinedKit: List<KitSearchHit> = cardMatches + drugMatches

            _uiState.update { state ->
                if (state.searchQuery.trim().lowercase() != q) state
                else state.copy(
                    searchResults = calcMatches,
                    kitSearchHits = combinedKit
                )
            }
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
        _uiState.update { it.copy(themeMode = mode) }
        viewModelScope.launch(Dispatchers.IO) {
            prefs.saveThemeMode(mode)
        }
    }

    fun togglePin(calculatorId: String) {
        val next = if (calculatorId in _pinnedCalculatorIds.value) {
            _pinnedCalculatorIds.value.filterNot { it == calculatorId }
        } else {
            listOf(calculatorId) + _pinnedCalculatorIds.value
        }
        val resolved = resolvePinnedCalculators(next)
        _pinnedCalculatorIds.value = next
        _uiState.update { state ->
            state.copy(
                pinnedIds = next,
                pinnedCalculators = resolved
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            prefs.savePinnedIds(next)
        }
    }

    fun updateNumericInput(fieldId: String, rawValue: String) {
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
        var persistCount: Int? = null
        _uiState.update { state ->
            val nextInput = transform(state.inputState)
            val nextResult = FormulaEngine.evaluate(calcId, nextInput)
            var nextUsage = state.usageCounts
            var nextMostUsed = state.mostUsedCalculators
            if (nextResult.isReady && !usageCountedForSession) {
                usageCountedForSession = true
                val updatedCount = (state.usageCounts[calcId] ?: 0) + 1
                nextUsage = state.usageCounts + (calcId to updatedCount)
                nextMostUsed = resolveMostUsedCalculators(nextUsage)
                _calculatorUsageCounts.value = nextUsage
                persistCount = updatedCount
            }
            state.copy(
                inputState = nextInput,
                calculationResult = nextResult,
                usageCounts = nextUsage,
                mostUsedCalculators = nextMostUsed
            )
        }
        val countToSave = persistCount
        if (countToSave != null) {
            viewModelScope.launch(Dispatchers.IO) {
                prefs.saveUsageCount(calcId, countToSave)
            }
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
