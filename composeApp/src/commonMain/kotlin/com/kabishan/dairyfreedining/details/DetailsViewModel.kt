package com.kabishan.dairyfreedining.details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kabishan.dairyfreedining.network.Status
import kotlinx.coroutines.launch

class DetailsViewModel(
    restaurantId: String,
    val repository: DetailsRepository
) : ViewModel() {

    var detailsState: MutableState<DetailsState> = mutableStateOf(DetailsState.ShowLoading)
        private set

    val searchQuery: MutableState<String> = mutableStateOf(String())

    var selectedCategoryList: MutableState<List<String>> = mutableStateOf(listOf())
        private set

    init {
        getRestaurantDetails(restaurantId)
    }

    fun getRestaurantDetails(restaurantId: String) {
        viewModelScope.launch {
            val result = repository.getRestaurantDetails(restaurantId)

            detailsState.value = when (result.status) {
                // Handle empty details
                Status.SUCCESS -> result.data?.let {
                    selectedCategoryList = mutableStateOf(it.categories.keys.toList())
                    DetailsState.ShowSuccess(details = it)
                } ?: DetailsState.ShowError

                Status.FAILURE -> DetailsState.ShowError
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

    fun resetSelectedCategoryList() {
        if (detailsState.value is DetailsState.ShowSuccess) {
            selectedCategoryList.value =
                (detailsState.value as DetailsState.ShowSuccess).details.categories.keys.toList()
        }
    }
}