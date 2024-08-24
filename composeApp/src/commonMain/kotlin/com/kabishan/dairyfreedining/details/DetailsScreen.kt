package com.kabishan.dairyfreedining.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kabishan.dairyfreedining.ui.composables.ErrorMessage
import com.kabishan.dairyfreedining.ui.composables.LoadingMessage
import com.kabishan.dairyfreedining.model.RestaurantDetails
import com.kabishan.dairyfreedining.ui.composables.CategoryHeader
import com.kabishan.dairyfreedining.ui.composables.DairyFreeDiningSearchBar
import com.kabishan.dairyfreedining.ui.composables.FoodListItem
import com.kabishan.dairyfreedining.ui.composables.TopBar
import com.kabishan.dairyfreedining.ui.theme.DairyFreeDiningTheme
import dairyfreedining.composeapp.generated.resources.Res
import dairyfreedining.composeapp.generated.resources.details_search_bar_placeholder
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
    )
) {
    Scaffold(
        topBar = {
            TopBar(
                title = restaurantName,
                navController = navController
            )
        },
        modifier = Modifier.fillMaxSize()
    )
    { innerPadding ->
        DetailsScreenContent(
            innerPadding,
            viewModel.detailsState.value
        ) {
            viewModel.getRestaurantDetails(restaurantId)
        }
    }
}

@Composable
fun DetailsScreenContent(
    innerPadding: PaddingValues,
    detailsState: DetailsState,
    getRestaurantDetails: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        when (detailsState) {
            is DetailsState.ShowSuccess -> DetailsScreen(detailsState.details.categories)
            DetailsState.ShowLoading -> LoadingMessage()
            DetailsState.ShowError -> ErrorMessage(getRestaurantDetails)
        }
    }
}

@Composable
fun DetailsScreen(categories: Map<String, List<String>>) {
    val searchQuery: MutableState<String> = remember { mutableStateOf(String()) }
    val categoriesMap: MutableState<Map<String, List<String>>> =
        remember { mutableStateOf(categories) }

    DairyFreeDiningSearchBar(
        query = searchQuery.value,
        onQueryChange = { query ->
            searchQuery.value = query

            if (searchQuery.value.trim().isNotBlank()) {
                categoriesMap.value = categories.map { (category, foods) ->
                    Pair(
                        category,
                        foods.filter { it.contains(searchQuery.value.trim(), ignoreCase = true) })
                }.toMap()
            } else {
                categoriesMap.value = categories
            }
        },
        placeholderText = stringResource(resource = Res.string.details_search_bar_placeholder)
    )

    LazyColumn {
        categoriesMap.value.forEach { category ->
            if (category.value.isNotEmpty()) {
                item {
                    CategoryHeader(
                        text = category.key,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    )
                }

                items(category.value) { foodItem ->
                    FoodListItem(
                        foodItem = foodItem,
                        modifier = Modifier.fillMaxWidth()
                    )
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