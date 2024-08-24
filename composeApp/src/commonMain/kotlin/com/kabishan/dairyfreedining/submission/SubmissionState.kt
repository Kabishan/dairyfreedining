package com.kabishan.dairyfreedining.submission

import org.jetbrains.compose.resources.StringResource

sealed class SubmissionState {
    data object Initial: SubmissionState()
    data object ShowSuccess: SubmissionState()
    data object ShowLoading: SubmissionState()
    data class ShowError(val message: StringResource): SubmissionState()
}