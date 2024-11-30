package com.kabishan.dairyfreedining.submission

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kabishan.dairyfreedining.network.Status
import dairyfreedining.composeapp.generated.resources.Res
import dairyfreedining.composeapp.generated.resources.submission_screen_error_empty_fields
import dairyfreedining.composeapp.generated.resources.submission_screen_error_network
import kotlinx.coroutines.launch

class SubmissionViewModel(
    private val repository: SubmissionRepository
) : ViewModel() {

    private val submissionState: MutableState<SubmissionState> =
        mutableStateOf(SubmissionState.Initial)

    private val submissionTextFieldState: MutableState<SubmissionTextFieldState> =
        mutableStateOf(SubmissionTextFieldState())

    val submissionScreenStateHolder = SubmissionScreenStateHolder(
        submissionState,
        submissionTextFieldState,
        ::handleSubmissionTextFieldEvent,
        ::resetSubmissionState,
        ::submitFood
    )

    fun handleSubmissionTextFieldEvent(event: SubmissionTextFieldEvent) {
        when (event) {
            is SubmissionTextFieldEvent.RestaurantTextChanged -> {
                submissionTextFieldState.value =
                    submissionTextFieldState.value.copy(restaurant = event.restaurant)
            }

            is SubmissionTextFieldEvent.CategoryTextChanged -> {
                submissionTextFieldState.value =
                    submissionTextFieldState.value.copy(category = event.category)
            }

            is SubmissionTextFieldEvent.FoodTextChanged -> {
                submissionTextFieldState.value =
                    submissionTextFieldState.value.copy(food = event.food)
            }
        }
    }

    fun resetSubmissionState() {
        submissionState.value = SubmissionState.Initial
    }

    private fun resetSubmissionTextFieldState() {
        submissionTextFieldState.value = SubmissionTextFieldState()
    }

    fun submitFood() {
        viewModelScope.launch {
            submissionState.value = SubmissionState.ShowLoading

            with(submissionTextFieldState.value) {
                if (restaurant.isNotBlank() and category.isNotBlank() and food.isNotBlank()) {
                    val result = repository.submitFood(restaurant, category, food)

                    submissionState.value = when (result.status) {
                        Status.SUCCESS -> {
                            resetSubmissionTextFieldState()
                            SubmissionState.ShowSuccess
                        }

                        Status.FAILURE -> {
                            SubmissionState.ShowError(Res.string.submission_screen_error_network)
                        }
                    }
                } else {
                    submissionState.value =
                        SubmissionState.ShowError(Res.string.submission_screen_error_empty_fields)
                }
            }
        }
    }

    data class SubmissionScreenStateHolder(
        val submissionState: MutableState<SubmissionState>,
        val submissionTextFieldState: MutableState<SubmissionTextFieldState>,
        val handleSubmissionTextFieldEvent: (SubmissionTextFieldEvent) -> Unit,
        val resetSubmissionState: () -> Unit,
        val submitFood: () -> Unit
    )
}

data class SubmissionTextFieldState(
    val restaurant: String = String(),
    val category: String = String(),
    val food: String = String()
)

sealed class SubmissionTextFieldEvent {
    data class RestaurantTextChanged(val restaurant: String) : SubmissionTextFieldEvent()
    data class CategoryTextChanged(val category: String) : SubmissionTextFieldEvent()
    data class FoodTextChanged(val food: String) : SubmissionTextFieldEvent()
}