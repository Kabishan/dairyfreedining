package com.kabishan.dairyfreedining.landing

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kabishan.dairyfreedining.analytics.Analytics
import com.kabishan.dairyfreedining.network.Status
import kotlinx.coroutines.launch

class LandingViewModel(
    private val repository: LandingRepository
) : ViewModel(), DefaultLifecycleObserver {

    private val landingState: MutableState<LandingState> = mutableStateOf(LandingState.ShowLoading)

    private val searchQuery: MutableState<String> = mutableStateOf(String())

    val landingScreenStateHolder = LandingScreenStateHolder(
        landingState,
        searchQuery,
        ::getRestaurants,
        ::updateSearchQuery
    )

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Analytics.trackScreen(Analytics.LANDING_SCREEN)
    }

    init {
        getRestaurants()
    }

    fun getRestaurants() {
        viewModelScope.launch {
            val result = repository.getRestaurants()

            landingState.value = when (result.status) {
                Status.SUCCESS -> result.data?.let { list ->
                    LandingState.ShowSuccess(list.sortedBy { it.name })
                } ?: LandingState.ShowSuccess(listOf())

                Status.FAILURE -> {
                    Analytics.trackScreenError(Analytics.LANDING_SCREEN)
                    LandingState.ShowError
                }
            }
        }
    }

    fun updateSearchQuery(updatedSearchQuery: String) {
        searchQuery.value = updatedSearchQuery
    }

    data class LandingScreenStateHolder(
        val landingState: MutableState<LandingState>,
        val searchQuery: MutableState<String>,
        val getRestaurants: () -> Unit,
        val updateSearchQuery: (String) -> Unit
    )
}