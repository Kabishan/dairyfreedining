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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kabishan.dairyfreedining.filter.FilterSection
import com.kabishan.dairyfreedining.filter.FilterTab
import com.kabishan.dairyfreedining.model.RestaurantDetails
import com.kabishan.dairyfreedining.search.SearchBar
import com.kabishan.dairyfreedining.search.SearchViewModel
import com.kabishan.dairyfreedining.search.SearchViewModelFactory
import com.kabishan.dairyfreedining.ui.composables.ErrorMessage
import com.kabishan.dairyfreedining.ui.composables.FoodListItem
import com.kabishan.dairyfreedining.ui.composables.LoadingMessage
import com.kabishan.dairyfreedining.ui.composables.Subheader
import com.kabishan.dairyfreedining.ui.composables.TopBar
import com.kabishan.dairyfreedining.ui.theme.DairyFreeDiningTheme
import dairyfreedining.composeapp.generated.resources.Res
import dairyfreedining.composeapp.generated.resources.filter_categories
import dairyfreedining.composeapp.generated.resources.details_search_bar_placeholder
import dairyfreedining.composeapp.generated.resources.details_subheading
import dairyfreedining.composeapp.generated.resources.no_food_items_found
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DetailsScreen(
    restaurantId: String,
    restaurantName: String,
    navController: NavController,
    viewModel: DetailsViewModel = viewModel(
        factory = DetailsViewModelFactory(
            restaurantId = restaurantId,
            repository = DetailsRepository()
        )
    ),
    searchViewModel: SearchViewModel = viewModel(
        factory = SearchViewModelFactory()
    )
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
            searchViewModel.searchQuery.value,
            searchViewModel::updateSearchQuery,
            viewModel.detailsState.value
        ) {
            viewModel.getRestaurantDetails(restaurantId)
        }
    }
}

@Composable
private fun DetailsScreenContent(
    innerPadding: PaddingValues,
    searchQuery: String,
    updateSearchQuery: (String) -> Unit,
    detailsState: DetailsState,
    getRestaurantDetails: () -> Unit
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
                updateSearchQuery
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
    updateSearchQuery: (String) -> Unit
) {
    val displayCategories: MutableState<Map<String, List<String>>> =
        remember { mutableStateOf(categories) }
    val selectedCategoryList: MutableState<List<String>> =
        remember { mutableStateOf(categories.keys.toList()) }

    val isBottomSheetShown = remember { mutableStateOf(false) }

    SearchBar(
        query = searchQuery,
        onQueryChange = { query -> updateSearchQuery(query) },
        placeholderText = stringResource(resource = Res.string.details_search_bar_placeholder)
    )

    FilterTab(
        onClick = { isBottomSheetShown.value = true },
        modifier = Modifier.fillMaxWidth()
    )

    HorizontalDivider()

    if (isBottomSheetShown.value) {
        ModalBottomSheet(onDismissRequest = { isBottomSheetShown.value = false }) {
            FilterSection(
                sectionName = stringResource(Res.string.filter_categories),
                allItems = categories.keys.toList(),
                selectedItems = selectedCategoryList.value,
                onSelectionChanged = { category ->
                    val previousSelection = selectedCategoryList.value.toMutableList()

                    if (previousSelection.contains(category)) {
                        previousSelection.remove(category)
                    } else {
                        previousSelection.add(category)
                    }

                    selectedCategoryList.value = previousSelection
                },
                onSelectionCleared = { selectedCategoryList.value = listOf() },
                onSelectionReset = { selectedCategoryList.value = categories.keys.toList() }
            )
        }
    }

    val selectedCategories = categories.filterKeys { category ->
        selectedCategoryList.value.contains(category)
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
            modifier = Modifier.fillMaxSize().padding(top = 8.dp),
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
            )
        ) {}
    }
}