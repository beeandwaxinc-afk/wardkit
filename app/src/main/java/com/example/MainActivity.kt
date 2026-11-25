package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.CalculatorRegistry
import com.example.data.MainTab
import com.example.data.WheelStep
import com.example.ui.MedWheelViewModel
import com.example.ui.components.MedWheelDrawerContent
import com.example.ui.components.MedWheelTopBar
import com.example.ui.components.SearchOverlayResults
import com.example.ui.kit.KitBlockScreen
import com.example.ui.kit.KitHomeScreen
import com.example.ui.screens.CalculatorCanvasScreen
import com.example.ui.screens.CalculatorWheelScreen
import com.example.ui.screens.DepartmentWheelScreen
import com.example.ui.screens.PinnedScreen
import com.example.ui.theme.MedWheelTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedWheelApp()
        }
    }
}

@Composable
fun MedWheelApp(
    viewModel: MedWheelViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    // Hoisted scroll states above the top-level tab switch so tab transitions are instant
    // and preserve scroll positions without cold-measuring layouts from scratch.
    val departmentWheelState = rememberLazyListState(initialFirstVisibleItemIndex = uiState.focusedDepartmentIndex)
    val kitHomeListState = rememberLazyListState()
    val pinnedListState = rememberLazyListState()

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    BackHandler(enabled = !drawerState.isOpen && uiState.isSearchActive) {
        viewModel.setSearchActive(false)
    }

    MedWheelTheme(
        themeMode = uiState.themeMode,
        activePalette = uiState.activePalette
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.fillMaxWidth(0.86f),
                    drawerContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                ) {
                    // Lazy Drawer Gate: never compose heavy drawer items / 81 formula accordions while closed
                    if (drawerState.isOpen || drawerState.isAnimationRunning) {
                        MedWheelDrawerContent(
                            themeMode = uiState.themeMode,
                            activeCalculator = uiState.activeCalculator,
                            departments = viewModel.sortedDepartments,
                            calculatorsByDepartment = viewModel.calculatorsByDepartment,
                            toolCounts = viewModel.departmentToolCounts,
                            totalToolCount = CalculatorRegistry.all.size,
                            onSelectTheme = { mode -> viewModel.setThemeMode(mode) },
                            onJumpToDepartment = { deptId ->
                                viewModel.openDepartment(deptId)
                                scope.launch { drawerState.close() }
                            },
                            onJumpToCalculator = { calcId ->
                                viewModel.openCalculator(calcId, fromPinnedOrSearch = false)
                                scope.launch { drawerState.close() }
                            }
                        )
                    }
                }
            }
        ) {
            val showBottomBar = !uiState.isInsideCanvas && !uiState.isInsideKitCard && !uiState.isSearchActive

            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                topBar = {
                    MedWheelTopBar(
                        searchQuery = uiState.searchQuery,
                        isSearchActive = uiState.isSearchActive,
                        onMenuClick = { scope.launch { drawerState.open() } },
                        onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                        onSearchFocusChange = { viewModel.setSearchActive(it) },
                        onClearSearch = { viewModel.setSearchActive(false) }
                    )
                },
                bottomBar = {
                    if (showBottomBar) {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            tonalElevation = 0.dp,
                            modifier = Modifier.testTag("bottom_navigation_bar")
                        ) {
                            NavigationBarItem(
                                selected = uiState.activeTab == MainTab.CALCULATORS,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    viewModel.selectTab(MainTab.CALCULATORS)
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (uiState.activeTab == MainTab.CALCULATORS) {
                                            Icons.Rounded.Calculate
                                        } else {
                                            Icons.Outlined.Calculate
                                        },
                                        contentDescription = stringResource(R.string.tab_calculators)
                                    )
                                },
                                label = {
                                    Text(
                                        text = stringResource(R.string.tab_calculators),
                                        fontWeight = if (uiState.activeTab == MainTab.CALCULATORS) {
                                            FontWeight.Bold
                                        } else {
                                            FontWeight.Medium
                                        }
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                modifier = Modifier.testTag("nav_tab_calculators")
                            )

                            NavigationBarItem(
                                selected = uiState.activeTab == MainTab.KIT,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    viewModel.selectTab(MainTab.KIT)
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (uiState.activeTab == MainTab.KIT) {
                                            Icons.Rounded.MedicalServices
                                        } else {
                                            Icons.Outlined.MedicalServices
                                        },
                                        contentDescription = stringResource(R.string.tab_kit)
                                    )
                                },
                                label = {
                                    Text(
                                        text = stringResource(R.string.tab_kit),
                                        fontWeight = if (uiState.activeTab == MainTab.KIT) {
                                            FontWeight.Bold
                                        } else {
                                            FontWeight.Medium
                                        }
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                modifier = Modifier.testTag("nav_tab_kit")
                            )

                            NavigationBarItem(
                                selected = uiState.activeTab == MainTab.PINNED,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    viewModel.selectTab(MainTab.PINNED)
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (uiState.activeTab == MainTab.PINNED) {
                                            Icons.Rounded.PushPin
                                        } else {
                                            Icons.Outlined.PushPin
                                        },
                                        contentDescription = stringResource(R.string.tab_pinned)
                                    )
                                },
                                label = {
                                    Text(
                                        text = stringResource(R.string.tab_pinned),
                                        fontWeight = if (uiState.activeTab == MainTab.PINNED) {
                                            FontWeight.Bold
                                        } else {
                                            FontWeight.Medium
                                        }
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                modifier = Modifier.testTag("nav_tab_pinned")
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .widthIn(max = 680.dp)
                    ) {
                        if (uiState.isSearchActive) {
                            SearchOverlayResults(
                                query = uiState.searchQuery,
                                searchFilter = uiState.searchFilter,
                                calculatorResults = uiState.searchResults,
                                kitHits = uiState.kitSearchHits,
                                onSelectFilter = { viewModel.setSearchFilter(it) },
                                onSelectCalculator = { calcId ->
                                    viewModel.openCalculator(calcId, fromPinnedOrSearch = true)
                                },
                                onSelectKitCard = { cardId ->
                                    viewModel.openKitCard(cardId, fromPinnedOrSearch = true)
                                },
                                onSelectDoseDrug = { tabCardId, drugId ->
                                    viewModel.openDoseDrug(tabCardId, drugId)
                                }
                            )
                        } else {
                            // Instant crisp switch (zero dual-screen Crossfade/AnimatedContent jank)
                            when (uiState.activeTab) {
                                MainTab.KIT -> {
                                    val blockId = uiState.activeKitBlockId
                                    val cardId = uiState.activeKitCardId
                                    if (blockId != null && cardId != null) {
                                        KitBlockScreen(
                                            blockId = blockId,
                                            activeCardId = cardId,
                                            focusedDoseDrugId = uiState.focusedDoseDrugId,
                                            isCardPinned = cardId in uiState.pinnedKitIds,
                                            labUnitSystem = uiState.labUnitSystem,
                                            onSelectCard = { viewModel.selectKitCard(it) },
                                            onClearFocusedDoseDrug = { viewModel.clearFocusedDoseDrug() },
                                            onSelectLabUnitSystem = { viewModel.setLabUnitSystem(it) },
                                            onTogglePinCard = { viewModel.togglePinKitCard(it) },
                                            onBack = { viewModel.navigateBack() }
                                        )
                                    } else {
                                        KitHomeScreen(
                                            onOpenBlock = { viewModel.openKitBlock(it) },
                                            listState = kitHomeListState,
                                            tier1Rows = viewModel.kitTier1Rows,
                                            tier2Rows = viewModel.kitTier2Rows
                                        )
                                    }
                                }

                                MainTab.PINNED -> {
                                    PinnedScreen(
                                        pinnedCalculators = uiState.pinnedCalculators,
                                        pinnedKitCards = uiState.pinnedKitCards,
                                        mostUsedCalculators = uiState.mostUsedCalculators,
                                        onOpenCalculator = { id ->
                                            viewModel.openCalculator(id, fromPinnedOrSearch = true)
                                        },
                                        onUnpinCalculator = { id -> viewModel.togglePin(id) },
                                        onOpenKitCard = { id ->
                                            viewModel.openKitCard(id, fromPinnedOrSearch = true)
                                        },
                                        onUnpinKitCard = { id -> viewModel.togglePinKitCard(id) },
                                        listState = pinnedListState
                                    )
                                }

                                MainTab.CALCULATORS -> {
                                    when (uiState.wheelStep) {
                                        WheelStep.DEPARTMENTS -> {
                                            DepartmentWheelScreen(
                                                departments = viewModel.sortedDepartments,
                                                toolCounts = viewModel.departmentToolCounts,
                                                focusedIndex = uiState.focusedDepartmentIndex,
                                                wheelState = departmentWheelState,
                                                onFocusedIndexChanged = { viewModel.focusDepartment(it) },
                                                onOpenDepartment = { viewModel.openDepartment(it) }
                                            )
                                        }

                                        WheelStep.CALCULATORS -> {
                                            val deptCalcs = viewModel.calculatorsByDepartment[uiState.selectedDepartmentId]
                                                .orEmpty()
                                            CalculatorWheelScreen(
                                                departmentId = uiState.selectedDepartmentId,
                                                calculators = deptCalcs,
                                                focusedCalculatorIndex = uiState.focusedCalculatorIndex,
                                                onFocusedCalculatorChanged = { viewModel.focusCalculator(it) },
                                                onOpenCalculator = { id ->
                                                    viewModel.openCalculator(id, fromPinnedOrSearch = false)
                                                },
                                                onBack = { viewModel.navigateBack() }
                                            )
                                        }

                                        WheelStep.CANVAS -> {
                                            val calc = uiState.activeCalculator
                                            if (calc != null) {
                                                CalculatorCanvasScreen(
                                                    calculator = calc,
                                                    inputState = uiState.inputState,
                                                    result = uiState.calculationResult,
                                                    isPinned = calc.id in uiState.pinnedIds,
                                                    onNumericChange = { id, v -> viewModel.updateNumericInput(id, v) },
                                                    onUnitSelected = { id, idx -> viewModel.updateUnit(id, idx) },
                                                    onChoiceSelected = { id, opt -> viewModel.updateChoice(id, opt) },
                                                    onToggleCriterion = { id -> viewModel.toggleCriterion(id) },
                                                    onDateSelected = { id, millis -> viewModel.updateDate(id, millis) },
                                                    onTogglePin = { viewModel.togglePin(calc.id) },
                                                    onClear = { viewModel.clearInputs() },
                                                    onBack = { viewModel.navigateBack() }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
