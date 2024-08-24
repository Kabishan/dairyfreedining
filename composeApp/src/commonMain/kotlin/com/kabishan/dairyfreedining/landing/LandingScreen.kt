package com.kabishan.dairyfreedining.landing

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kabishan.dairyfreedining.DestinationScreen
import com.kabishan.dairyfreedining.ui.composables.ErrorMessage
import com.kabishan.dairyfreedining.ui.composables.LoadingMessage
import com.kabishan.dairyfreedining.model.Restaurant
import com.kabishan.dairyfreedining.navigateTo
import com.kabishan.dairyfreedining.ui.composables.DairyFreeDiningSearchBar
import com.kabishan.dairyfreedining.ui.composables.RestaurantTile
import com.kabishan.dairyfreedining.ui.composables.TopBar
import com.kabishan.dairyfreedining.ui.theme.DairyFreeDiningTheme
import dairyfreedining.composeapp.generated.resources.Res
import dairyfreedining.composeapp.generated.resources.app_name
import dairyfreedining.composeapp.generated.resources.landing_search_bar_placeholder
import dairyfreedining.composeapp.generated.resources.no_restaurants_found
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LandingScreen(
    navController: NavController,
    viewModel: LandingViewModel = viewModel(
        factory = LandingViewModelFactory(
            LandingRepository()
        )
    )
) {
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
            }
        )
    }
}

@Composable
fun LandingScreenContent(
    innerPadding: PaddingValues,
    landingState: LandingState,
    getRestaurants: () -> Unit,
    navigateToDetails: (String, String) -> Unit,
    navigateToSubmission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        when (landingState) {
            is LandingState.ShowSuccess -> RestaurantsList(
                landingState.restaurantList,
                navigateToDetails,
                navigateToSubmission
            )
            LandingState.ShowLoading -> LoadingMessage()
            LandingState.ShowError -> ErrorMessage(getRestaurants)
        }
    }
}

@Composable
fun RestaurantsList(
    restaurants: List<Restaurant>,
    navigateToDetails: (String, String) -> Unit,
    navigateToSubmission: () -> Unit,
) {
    val restaurantList: MutableState<List<Restaurant>> = remember { mutableStateOf(restaurants) }
    val searchQuery: MutableState<String> = remember { mutableStateOf(String()) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            DairyFreeDiningSearchBar(
                query = searchQuery.value,
                onQueryChange = {
                    searchQuery.value = it
                    if (searchQuery.value.trim().isNotBlank()) {
                        restaurantList.value = restaurants.filter { restaurant ->
                            restaurant.name.contains(searchQuery.value.trim(), ignoreCase = true)
                        }
                    } else {
                        restaurantList.value = restaurants
                    }
                },
                placeholderText = stringResource(resource = Res.string.landing_search_bar_placeholder)
            )

            if (restaurantList.value.isNotEmpty()) {
                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    items(restaurantList.value) {
                        RestaurantTile(
                            text = it.name,
                            imageUrl = it.imageUrl,
                            modifier = Modifier.padding(2.dp),
                            onClick = { navigateToDetails(it.id, it.name) }
                        )
                    }
                }
            } else {
                Text(text = stringResource(Res.string.no_restaurants_found))
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
                contentDescription = null
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
            landingState = LandingState.ShowSuccess(
                listOf(Restaurant(id = "id", "imageUrl", name = "McDonald's"))
            ),
            {},
            { _: String, _: String -> },
            {}
        )
    }
}
