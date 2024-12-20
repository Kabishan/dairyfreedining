package com.kabishan.dairyfreedining.details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kabishan.dairyfreedining.analytics.Analytics
import com.kabishan.dairyfreedining.network.Status
import kotlinx.coroutines.launch

class DetailsViewModel(
    restaurantId: String,
    private val restaurantName: String,
    val repository: DetailsRepository
) : ViewModel(), DefaultLifecycleObserver {

    private val detailsState: MutableState<DetailsState> = mutableStateOf(DetailsState.ShowLoading)

    private val searchQuery: MutableState<String> = mutableStateOf(String())

    private val selectedCategoryList: MutableState<List<String>> = mutableStateOf(listOf())

    val detailsScreenStateHolder: DetailsScreenStateHolder = DetailsScreenStateHolder(
        detailsState,
        searchQuery,
        selectedCategoryList,
        ::getRestaurantDetails,
        ::updateSearchQuery,
        ::updateSelectedCategoryList,
        ::clearSelectedCategoryList
    )

    init {
        getRestaurantDetails(restaurantId)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Analytics.trackScreen("${Analytics.DETAILS_SCREEN} - $restaurantName")
    }

    fun getRestaurantDetails(restaurantId: String) {
        viewModelScope.launch {
            val result = repository.getRestaurantDetails(restaurantId)

            detailsState.value = when (result.status) {
                // Handle empty details
                Status.SUCCESS -> result.data?.let {
                    DetailsState.ShowSuccess(details = it)
                } ?: DetailsState.ShowError

                Status.FAILURE -> {
                    Analytics.trackScreenError(Analytics.DETAILS_SCREEN)
                    DetailsState.ShowError
                }
            }
        }
    }

    fun updateSearchQuery(updatedSearchQuery: String) {
        searchQuery.value = updatedSearchQuery
    }

    fun updateSelectedCategoryList(category: String) {
        val previousSelection = selectedCategoryList.value.toMutableList()

        if (previousSelection.contains(category)) {
            previousSelection.remove(category)
        } else {
            previousSelection.add(category)
        }

        selectedCategoryList.value = previousSelection
    }

    fun clearSelectedCategoryList() {
        selectedCategoryList.value = listOf()
    }

    data class DetailsScreenStateHolder(
        val detailsState: MutableState<DetailsState>,
        val searchQuery: MutableState<String>,
        val selectedCategoryList: MutableState<List<String>>,
        val getRestaurantDetails: (String) -> Unit,
        val updateSearchQuery: (String) -> Unit,
        val updateSelectedCategoryList: (String) -> Unit,
        val clearSelectedCategoryList: () -> Unit
    )
}