package com.kabishan.dairyfreedining.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    val searchQuery: MutableState<String> = mutableStateOf(String())

    fun updateSearchQuery(updatedSearchQuery: String) {
        searchQuery.value = updatedSearchQuery
    }
}
