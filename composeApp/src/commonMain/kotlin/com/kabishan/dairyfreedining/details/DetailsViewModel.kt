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
): ViewModel() {

    var detailsState: MutableState<DetailsState> = mutableStateOf(DetailsState.ShowLoading)
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
                    DetailsState.ShowSuccess(details = it)
                } ?: DetailsState.ShowError
                Status.FAILURE -> DetailsState.ShowError
            }
        }
    }
}