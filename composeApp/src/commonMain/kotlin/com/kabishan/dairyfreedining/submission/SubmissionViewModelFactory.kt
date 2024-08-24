package com.kabishan.dairyfreedining.submission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

class SubmissionViewModelFactory(
    private val repository: SubmissionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return SubmissionViewModel(repository) as T
    }
}