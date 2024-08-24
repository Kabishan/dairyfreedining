package com.kabishan.dairyfreedining.details

import com.kabishan.dairyfreedining.model.RestaurantDetails

sealed class DetailsState {
    data object ShowLoading: DetailsState()
    data class ShowSuccess(val details: RestaurantDetails): DetailsState()
    data object ShowError: DetailsState()
}