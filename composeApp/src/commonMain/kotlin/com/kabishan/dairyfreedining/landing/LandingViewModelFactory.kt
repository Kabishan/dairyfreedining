package com.kabishan.dairyfreedining.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

class LandingViewModelFactory(
    private val repository: LandingRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return LandingViewModel(repository = repository) as T
    }
}