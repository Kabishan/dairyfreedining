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
import com.kabishan.dairyfreedining.DestinationScreen
import com.kabishan.dairyfreedining.coach_marks.Arrow
import com.kabishan.dairyfreedining.coach_marks.CoachMarkKeys
import com.kabishan.dairyfreedining.coach_marks.CoachMarkToolTip
import com.kabishan.dairyfreedining.model.Restaurant
import com.kabishan.dairyfreedining.navigateTo
import com.kabishan.dairyfreedining.preferences.DataStoreRepository
import com.kabishan.dairyfreedining.search.SearchBar
import com.kabishan.dairyfreedining.ui.composables.ErrorMessage
import com.kabishan.dairyfreedining.ui.composables.LoadingMessage
import com.kabishan.dairyfreedining.ui.composables.RestaurantTile
import com.kabishan.dairyfreedining.ui.composables.TopBar
import com.kabishan.dairyfreedining.ui.theme.DairyFreeDiningTheme
import com.pseudoankit.coachmark.LocalCoachMarkScope
import com.pseudoankit.coachmark.UnifyCoachmark
import com.pseudoankit.coachmark.model.HighlightedViewConfig
import com.pseudoankit.coachmark.model.OverlayClickEvent
import com.pseudoankit.coachmark.model.ToolTipPlacement
import com.pseudoankit.coachmark.scope.enableCoachMark
import dairyfreedining.composeapp.generated.resources.Res
import dairyfreedining.composeapp.generated.resources.app_name
import dairyfreedining.composeapp.generated.resources.landing_search_bar_placeholder
import dairyfreedining.composeapp.generated.resources.no_restaurants_found
import dairyfreedining.composeapp.generated.resources.submit_food_item_floating_action_button_accessibility_text
import dairyfreedining.composeapp.generated.resources.submit_food_item_floating_action_coach_mark_text
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

    UnifyCoachmark(
        onOverlayClicked = {
            coroutineScope.launch {
                dataStoreRepository.setBooleanPreference(
                    DataStoreRepository.SHOW_COACH_MARK_LANDING,
                    false
                )
            }
            OverlayClickEvent.DismissAll
        }
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
                viewModel.landingScreenStateHolder,
                { restaurantId: String, restaurantName: String ->
                    navigateTo(
                        navController,
                        DestinationScreen.Details.createRoute(restaurantId, restaurantName)
                    )
                },
                {
                    navigateTo(navController, DestinationScreen.Submission.route)
                },
                showCoachMark
            )
        }
    }
}

@Composable
private fun LandingScreenContent(
    innerPadding: PaddingValues,
    landingScreenStateHolder: LandingViewModel.LandingScreenStateHolder,
    navigateToDetails: (String, String) -> Unit,
    navigateToSubmission: () -> Unit,
    showCoachMark: Boolean?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        when (val landingState = landingScreenStateHolder.landingState.value) {
            is LandingState.ShowSuccess -> RestaurantsList(
                landingScreenStateHolder.searchQuery.value,
                landingScreenStateHolder.updateSearchQuery,
                landingState.restaurantList,
                navigateToDetails,
                navigateToSubmission,
                showCoachMark
            )

            LandingState.ShowLoading -> LoadingMessage()
            LandingState.ShowError -> ErrorMessage(landingScreenStateHolder.getRestaurants)
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
    showCoachMark: Boolean?
) {
    val localCoachMarkScope = LocalCoachMarkScope.current
    val coroutineScope = rememberCoroutineScope()

    val restaurantList: MutableState<List<Restaurant>> = remember { mutableStateOf(restaurants) }

    coroutineScope.launch {
        if (showCoachMark == true) {
            localCoachMarkScope.show(CoachMarkKeys.LANDING_SCREEN_SUBMIT_BUTTON)
        }
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
                .enableCoachMark(
                    key = CoachMarkKeys.LANDING_SCREEN_SUBMIT_BUTTON,
                    toolTipPlacement = ToolTipPlacement.Start,
                    highlightedViewConfig = HighlightedViewConfig(
                        shape = HighlightedViewConfig.Shape.Rect(12.dp),
                        padding = PaddingValues(8.dp)
                    ),
                    tooltip = {
                        CoachMarkToolTip(
                            text = stringResource(
                                Res.string.submit_food_item_floating_action_coach_mark_text
                            ),
                            arrow = Arrow.End()
                        )
                    },
                    coachMarkScope = localCoachMarkScope
                )
        ) {
            Icon(
                Icons.Default.Edit,
                contentDescription = stringResource(
                    Res.string.submit_food_item_floating_action_button_accessibility_text
                )
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
            landingScreenStateHolder = LandingViewModel.LandingScreenStateHolder(
                searchQuery = mutableStateOf(String()),
                updateSearchQuery = {},
                landingState = mutableStateOf(
                    LandingState.ShowSuccess(
                        listOf(Restaurant(id = "id", "imageUrl", name = "McDonald's"))
                    )
                ),
                getRestaurants = {}
            ),
            navigateToDetails = { _: String, _: String -> },
            navigateToSubmission = {},
            showCoachMark = false
        )
    }
}
