package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.CalculatorDef
import com.example.data.ChoiceOption
import com.example.data.DepartmentRegistry
import com.example.data.kit.KitRegistry
import com.example.data.kit.KitSearchHit
import com.example.data.kit.SearchFilter
import com.example.ui.kit.ItemTypeBadge
import com.example.ui.theme.LocalMedWheelExtras

@Composable
fun MedWheelTopBar(
    searchQuery: String,
    isSearchActive: Boolean,
    onMenuClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearchFocusChange: (Boolean) -> Unit,
    onClearSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(
            onClick = {
                focusManager.clearFocus()
                keyboard?.hide()
                onMenuClick()
            },
            modifier = Modifier
                .size(48.dp)
                .testTag("drawer_menu_button")
        ) {
            Icon(
                imageVector = Icons.Rounded.Menu,
                contentDescription = stringResource(R.string.cd_open_drawer),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(26.dp)
            )
        }

        Surface(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 50.dp),
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.surfaceContainerHigh
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(R.string.cd_search_icon),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search_placeholder),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.72f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = { keyboard?.hide() }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { state ->
                                if (state.isFocused) onSearchFocusChange(true)
                            }
                            .testTag("top_search_input")
                    )
                }

                if (searchQuery.isNotEmpty() || isSearchActive) {
                    IconButton(
                        onClick = {
                            focusManager.clearFocus()
                            keyboard?.hide()
                            onClearSearch()
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .testTag("clear_search_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(R.string.cd_clear_search),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchOverlayResults(
    query: String,
    searchFilter: SearchFilter,
    calculatorResults: List<CalculatorDef>,
    kitHits: List<KitSearchHit>,
    onSelectFilter: (SearchFilter) -> Unit,
    onSelectCalculator: (String) -> Unit,
    onSelectKitCard: (String) -> Unit,
    onSelectDoseDrug: (tabCardId: String, drugId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val isDark = LocalMedWheelExtras.current.isDark

    val shownKit = if (searchFilter == SearchFilter.ALL || searchFilter == SearchFilter.KIT) {
        kitHits
    } else {
        emptyList()
    }
    val shownCalc = if (searchFilter == SearchFilter.ALL || searchFilter == SearchFilter.CALC) {
        calculatorResults
    } else {
        emptyList()
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Compact connected filter pills: All | Calc | Kit
            ConnectedPillRow(
                options = listOf(
                    ChoiceOption(SearchFilter.ALL.id, stringResource(R.string.search_filter_all)),
                    ChoiceOption(SearchFilter.CALC.id, stringResource(R.string.search_filter_calc)),
                    ChoiceOption(SearchFilter.KIT.id, stringResource(R.string.search_filter_kit))
                ),
                selectedId = searchFilter.id,
                onSelect = { id ->
                    val next = SearchFilter.entries.firstOrNull { it.id == id } ?: SearchFilter.ALL
                    onSelectFilter(next)
                },
                tagPrefix = "search_filter",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )

            if (query.isBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 28.dp, vertical = 24.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = "Search 81 calculators, ER protocols, drugs & Kit cards (e.g., ACLS, Ketamine, Norepinephrine, Intralipid, DKA, CSF, Augmentin).",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else if (shownKit.isEmpty() && shownCalc.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = stringResource(R.string.search_no_results),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("search_results_list")
                ) {
                    items(
                        items = shownKit,
                        key = { it.key },
                        contentType = { "search_kit_hit" }
                    ) { hit ->
                        val block = KitRegistry.getBlock(hit.blockId)
                        val accent = if (isDark) block.palette.darkPrimary else block.palette.lightPrimary

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(22.dp))
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    focusManager.clearFocus()
                                    keyboard?.hide()
                                    when (hit) {
                                        is KitSearchHit.CardHit -> onSelectKitCard(hit.card.id)
                                        is KitSearchHit.DrugHit -> onSelectDoseDrug(hit.entry.tabId.cardId, hit.entry.drugId)
                                    }
                                }
                                .testTag("search_result_${hit.key}"),
                            shape = RoundedCornerShape(22.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerLow
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 18.dp, vertical = 15.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(accent)
                                )
                                Spacer(Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = hit.title,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = hit.subtitle,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Spacer(Modifier.width(10.dp))
                                ItemTypeBadge(isKit = true)
                            }
                        }
                    }

                    items(
                        items = shownCalc,
                        key = { "calc_${it.id}" },
                        contentType = { "search_calc_hit" }
                    ) { calc ->
                        val dept = DepartmentRegistry.get(calc.departmentId)
                        val accent = if (isDark) dept.palette.darkPrimary else dept.palette.lightPrimary

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(22.dp))
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    focusManager.clearFocus()
                                    keyboard?.hide()
                                    onSelectCalculator(calc.id)
                                }
                                .testTag("search_result_${calc.id}"),
                            shape = RoundedCornerShape(22.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerLow
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 18.dp, vertical = 15.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(accent)
                                )
                                Spacer(Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = calc.name,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = "${dept.wheelName} · ${calc.shortSubtitle}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Spacer(Modifier.width(10.dp))
                                ItemTypeBadge(isKit = false)
                            }
                        }
                    }
                }
            }
        }
    }
}
