package com.kabishan.dairyfreedining.landing

import com.kabishan.dairyfreedining.model.Restaurant

sealed class LandingState {
    data object ShowLoading : LandingState()
    data class ShowSuccess(val restaurantList: List<Restaurant>) : LandingState()
    data object ShowError : LandingState()
}