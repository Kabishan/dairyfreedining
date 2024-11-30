package com.kabishan.dairyfreedining.landing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.kabishan.dairyfreedining.DataStoreRepository
import com.kabishan.dairyfreedining.DestinationScreen
import com.kabishan.dairyfreedining.model.Restaurant
import com.kabishan.dairyfreedining.navigateTo
import com.kabishan.dairyfreedining.search.SearchBar
import com.kabishan.dairyfreedining.ui.composables.ErrorMessage
import com.kabishan.dairyfreedining.ui.composables.LoadingMessage
import com.kabishan.dairyfreedining.ui.composables.RestaurantTile
import com.kabishan.dairyfreedining.ui.composables.TopBar
import com.kabishan.dairyfreedining.ui.theme.DairyFreeDiningTheme
import dairyfreedining.composeapp.generated.resources.Res
import dairyfreedining.composeapp.generated.resources.app_name
import dairyfreedining.composeapp.generated.resources.landing_search_bar_placeholder
import dairyfreedining.composeapp.generated.resources.no_restaurants_found
import dairyfreedining.composeapp.generated.resources.submit_food_item_floating_action_button_accessibility_text
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LandingScreen(
    dataStoreRepository: DataStoreRepository,
    navController: NavController,
    viewModel: LandingViewModel = viewModel(
        factory = LandingViewModelFactory(
            LandingRepository()
        )
    )
) {
    var showCoachMark: Boolean? by remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()

    coroutineScope.launch {
        dataStoreRepository.getBooleanPreference(DataStoreRepository.SHOW_COACH_MARK_LANDING)
            .collect {
                showCoachMark = it
            }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(resource = Res.string.app_name),
                navController = navController,
                showBackIcon = false
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        LandingScreenContent(
            innerPadding,
            viewModel.searchQuery.value,
            viewModel::updateSearchQuery,
            viewModel.landingState.value,
            viewModel::getRestaurants,
            { restaurantId: String, restaurantName: String ->
                navigateTo(
                    navController,
                    DestinationScreen.Details.createRoute(restaurantId, restaurantName)
                )
            },
            {
                navigateTo(navController, DestinationScreen.Submission.route)
            },
            showCoachMark,
            {
                coroutineScope.launch {
                    dataStoreRepository.setBooleanPreference(
                        DataStoreRepository.SHOW_COACH_MARK_LANDING,
                        false
                    )
                }
            }
        )
    }
}

@Composable
private fun LandingScreenContent(
    innerPadding: PaddingValues,
    searchQuery: String,
    updateSearchQuery: (String) -> Unit,
    landingState: LandingState,
    getRestaurants: () -> Unit,
    navigateToDetails: (String, String) -> Unit,
    navigateToSubmission: () -> Unit,
    showCoachMark: Boolean?,
    setDoNotShowCoachMark: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        when (landingState) {
            is LandingState.ShowSuccess -> RestaurantsList(
                searchQuery,
                updateSearchQuery,
                landingState.restaurantList,
                navigateToDetails,
                navigateToSubmission,
                showCoachMark,
                setDoNotShowCoachMark
            )

            LandingState.ShowLoading -> LoadingMessage()
            LandingState.ShowError -> ErrorMessage(getRestaurants)
        }
    }
}

@Composable
private fun RestaurantsList(
    searchQuery: String,
    updateSearchQuery: (String) -> Unit,
    restaurants: List<Restaurant>,
    navigateToDetails: (String, String) -> Unit,
    navigateToSubmission: () -> Unit,
    showCoachMark: Boolean?,
    setShowCoachMark: () -> Unit
) {
    val restaurantList: MutableState<List<Restaurant>> = remember { mutableStateOf(restaurants) }

    println("Landing Screen: $showCoachMark")

    if (showCoachMark == true) {
        setShowCoachMark()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            SearchBar(
                query = searchQuery,
                onQueryChange = { query -> updateSearchQuery(query) },
                placeholderText = stringResource(resource = Res.string.landing_search_bar_placeholder)
            )

            if (searchQuery.trim().isNotBlank()) {
                restaurantList.value = restaurants.filter { restaurant ->
                    restaurant.name.contains(searchQuery.trim(), ignoreCase = true)
                }
            } else {
                restaurantList.value = restaurants
            }

            if (restaurantList.value.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    contentPadding = PaddingValues(2.dp)
                ) {
                    items(restaurantList.value) {
                        RestaurantTile(
                            text = it.name,
                            imageUrl = it.imageUrl,
                            onClick = { navigateToDetails(it.id, it.name) }
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(text = stringResource(Res.string.no_restaurants_found))
                }
            }
        }
        FloatingActionButton(
            onClick = navigateToSubmission,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(64.dp)
        ) {
            Icon(
                Icons.Default.Edit,
                contentDescription = stringResource(Res.string.submit_food_item_floating_action_button_accessibility_text)
            )
        }
    }
}

@Preview
@Composable
private fun LandingScreenContentPreview() {
    DairyFreeDiningTheme {
        LandingScreenContent(
            innerPadding = PaddingValues(),
            searchQuery = String(),
            updateSearchQuery = {},
            landingState = LandingState.ShowSuccess(
                listOf(Restaurant(id = "id", "imageUrl", name = "McDonald's"))
            ),
            getRestaurants = {},
            navigateToDetails = { _: String, _: String -> },
            navigateToSubmission = {},
            showCoachMark = false,
            setDoNotShowCoachMark = {}
        )
    }
}
