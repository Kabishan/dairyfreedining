package com.kabishan.dairyfreedining.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

class DetailsViewModelFactory(
    private val restaurantId: String,
    private val restaurantName: String,
    private val repository: DetailsRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return DetailsViewModel(restaurantId, restaurantName, repository = repository) as T
    }
}