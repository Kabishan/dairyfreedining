package com.kabishan.dairyfreedining.landing

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kabishan.dairyfreedining.network.Status
import kotlinx.coroutines.launch

class LandingViewModel(private val repository: LandingRepository) : ViewModel() {

    var landingState: MutableState<LandingState> = mutableStateOf(LandingState.ShowLoading)
        private set

    init {
        getRestaurants()
    }

    fun getRestaurants() {
        viewModelScope.launch {
            val result = repository.getRestaurants()

            landingState.value = when (result.status) {
                Status.SUCCESS -> result.data?.let {
                    LandingState.ShowSuccess(it)
                } ?: LandingState.ShowSuccess(listOf())
                Status.FAILURE -> LandingState.ShowError
            }
        }
    }
}