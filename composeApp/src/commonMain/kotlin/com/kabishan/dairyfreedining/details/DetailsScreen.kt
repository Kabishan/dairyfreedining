package com.kabishan.dairyfreedining.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kabishan.dairyfreedining.coach_marks.CoachMarkKeys
import com.kabishan.dairyfreedining.coach_marks.CoachMarkToolTip
import com.kabishan.dairyfreedining.filter.FilterSection
import com.kabishan.dairyfreedining.filter.FilterTab
import com.kabishan.dairyfreedining.model.RestaurantDetails
import com.kabishan.dairyfreedining.preferences.DataStoreRepository
import com.kabishan.dairyfreedining.search.SearchBar
import com.kabishan.dairyfreedining.ui.composables.ErrorMessage
import com.kabishan.dairyfreedining.ui.composables.FoodListItem
import com.kabishan.dairyfreedining.ui.composables.LoadingMessage
import com.kabishan.dairyfreedining.ui.composables.Subheader
import com.kabishan.dairyfreedining.ui.composables.TopBar
import com.kabishan.dairyfreedining.ui.theme.DairyFreeDiningTheme
import com.pseudoankit.coachmark.LocalCoachMarkScope
import com.pseudoankit.coachmark.UnifyCoachmark
import com.pseudoankit.coachmark.model.HighlightedViewConfig
import com.pseudoankit.coachmark.model.OverlayClickEvent
import com.pseudoankit.coachmark.model.ToolTipPlacement
import com.pseudoankit.coachmark.overlay.DimOverlayEffect
import com.pseudoankit.coachmark.scope.enableCoachMark
import com.pseudoankit.coachmark.shape.Arrow
import dairyfreedining.composeapp.generated.resources.Res
import dairyfreedining.composeapp.generated.resources.details_filter_coach_mark_text
import dairyfreedining.composeapp.generated.resources.details_search_bar_placeholder
import dairyfreedining.composeapp.generated.resources.details_subheading
import dairyfreedining.composeapp.generated.resources.filter_categories
import dairyfreedining.composeapp.generated.resources.no_food_items_found
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DetailsScreen(
    restaurantId: String,
    restaurantName: String,
    dataStoreRepository: DataStoreRepository,
    navController: NavController,
    viewModel: DetailsViewModel = viewModel(
        factory = DetailsViewModelFactory(
            restaurantId = restaurantId,
            repository = DetailsRepository()
        )
    )
) {
    var showCoachMark: Boolean? by remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()

    coroutineScope.launch {
        dataStoreRepository.getBooleanPreference(DataStoreRepository.SHOW_COACH_MARK_DETAILS)
            .collect {
                showCoachMark = it
            }
    }

    UnifyCoachmark(
        overlayEffect = DimOverlayEffect(DairyFreeDiningTheme.color.scrim.copy(alpha = .5f)),
        onOverlayClicked = {
            coroutineScope.launch {
                dataStoreRepository.setBooleanPreference(
                    DataStoreRepository.SHOW_COACH_MARK_DETAILS,
                    false
                )
            }
            OverlayClickEvent.DismissAll
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = restaurantName,
                    subTitle = stringResource(Res.string.details_subheading),
                    navController = navController
                )
            },
            modifier = Modifier.fillMaxSize()
        )
        { innerPadding ->
            DetailsScreenContent(
                innerPadding,
                viewModel.searchQuery.value,
                viewModel::updateSearchQuery,
                viewModel.detailsState.value,
                { viewModel.getRestaurantDetails(restaurantId) },
                viewModel.selectedCategoryList.value,
                viewModel::updateSelectedCategoryList,
                viewModel::clearSelectedCategoryList,
                viewModel::resetSelectedCategoryList,
                showCoachMark
            )
        }
    }
}

@Composable
private fun DetailsScreenContent(
    innerPadding: PaddingValues,
    searchQuery: String,
    updateSearchQuery: (String) -> Unit,
    detailsState: DetailsState,
    getRestaurantDetails: () -> Unit,
    selectedCategoryList: List<String>,
    updateSelectedCategoryList: (String) -> Unit,
    clearSelectedCategoryList: () -> Unit,
    resetSelectedCategoryList: () -> Unit,
    showCoachMark: Boolean?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        when (detailsState) {
            is DetailsState.ShowSuccess -> DetailsScreen(
                detailsState.details.categories.filter { (_, foodList) -> foodList.isNotEmpty() },
                searchQuery,
                updateSearchQuery,
                selectedCategoryList,
                updateSelectedCategoryList,
                clearSelectedCategoryList,
                resetSelectedCategoryList,
                showCoachMark
            )

            DetailsState.ShowLoading -> LoadingMessage()
            DetailsState.ShowError -> ErrorMessage(getRestaurantDetails)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsScreen(
    categories: Map<String, List<String>>,
    searchQuery: String,
    updateSearchQuery: (String) -> Unit,
    selectedCategoryList: List<String>,
    updateSelectedCategoryList: (String) -> Unit,
    clearSelectedCategoryList: () -> Unit,
    resetSelectedCategoryList: () -> Unit,
    showCoachMark: Boolean?
) {
    val localCoachMarkScope = LocalCoachMarkScope.current
    val coroutineScope = rememberCoroutineScope()

    val displayCategories: MutableState<Map<String, List<String>>> =
        remember { mutableStateOf(categories) }

    val selectedCategories = categories.filterKeys { category ->
        selectedCategoryList.contains(category)
    }

    val isBottomSheetShown = remember { mutableStateOf(false) }

    coroutineScope.launch {
        if (showCoachMark == true) {
            localCoachMarkScope.show(CoachMarkKeys.DETAILS_SCREEN_FILTER)
        }
    }

    SearchBar(
        query = searchQuery,
        onQueryChange = { query -> updateSearchQuery(query) },
        placeholderText = stringResource(resource = Res.string.details_search_bar_placeholder)
    )

    FilterTab(
        onClick = { isBottomSheetShown.value = true },
        modifier = Modifier
            .fillMaxWidth()
            .enableCoachMark(
                key = CoachMarkKeys.DETAILS_SCREEN_FILTER,
                toolTipPlacement = ToolTipPlacement.Bottom,
                highlightedViewConfig = HighlightedViewConfig(
                    shape = HighlightedViewConfig.Shape.Rect(12.dp),
                    padding = PaddingValues(8.dp)
                ),
                tooltip = {
                    CoachMarkToolTip(
                        text = stringResource(Res.string.details_filter_coach_mark_text),
                        arrow = Arrow.Top()
                    )
                },
                coachMarkScope = localCoachMarkScope
            )
    )

    HorizontalDivider()

    if (isBottomSheetShown.value) {
        ModalBottomSheet(onDismissRequest = { isBottomSheetShown.value = false }) {
            FilterSection(
                sectionName = stringResource(Res.string.filter_categories),
                allItems = categories.keys.toList(),
                selectedItems = selectedCategoryList,
                onSelectionChanged = { category -> updateSelectedCategoryList(category) },
                onSelectionCleared = clearSelectedCategoryList,
                onSelectionReset = resetSelectedCategoryList
            )
        }
    }

    displayCategories.value = if (searchQuery.trim().isNotBlank()) {
        selectedCategories.map { (category, foods) ->
            Pair(
                category,
                foods.filter { it.contains(searchQuery.trim(), ignoreCase = true) })
        }.toMap()
    } else {
        selectedCategories
    }

    if (displayCategories.value.values.flatten().isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(text = stringResource(Res.string.no_food_items_found))
        }
    } else {
        LazyColumn {
            displayCategories.value.forEach { category ->
                if (category.value.isNotEmpty()) {
                    item {
                        Subheader(
                            text = category.key,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        )
                    }

                    itemsIndexed(category.value) { index, foodItem ->
                        FoodListItem(
                            foodItem = foodItem,
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (index < category.value.size - 1) {
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun DetailsScreenPreview() {
    DairyFreeDiningTheme {
        DetailsScreenContent(
            innerPadding = PaddingValues(),
            searchQuery = String(),
            updateSearchQuery = {},
            detailsState = DetailsState.ShowSuccess(
                RestaurantDetails(
                    mapOf(
                        Pair("Chicken", listOf("McChicken"))
                    ),
                    "id",
                    "McDonald's"
                )
            ),
            getRestaurantDetails = {},
            selectedCategoryList = listOf(),
            updateSelectedCategoryList = {},
            clearSelectedCategoryList = {},
            resetSelectedCategoryList = {},
            showCoachMark = false
        )
    }
}