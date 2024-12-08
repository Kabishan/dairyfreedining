package com.kabishan.dairyfreedining.analytics

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.analytics

object Analytics {

    private const val SCREEN_NAME = "screen_name"

    const val LANDING_SCREEN = "landing screen"
    const val DETAILS_SCREEN = "details screen"
    const val SUBMISSION_SCREEN = "submission screen"
    const val ABOUT_SCREEN = "about screen"

    // Screen Track
    private const val SCREEN_TRACK_EVENT = "screen_track"

    // Screen Error
    private const val SCREEN_ERROR_TRACK_EVENT = "screen_error_track"

    // Submission Track
    private const val SUBMISSION_TRACK_EVENT = "submission_track"
    private const val SUBMISSION_STATE = "submission_state"

    const val SUBMISSION_SUCCESS = "success"
    const val SUBMISSION_FAILURE = "failure"
    const val SUBMISSION_EMPTY_FIELDS = "empty fields"

    fun trackScreen(screenName: String) {
        Firebase.analytics.logEvent(
            SCREEN_TRACK_EVENT,
            mapOf(SCREEN_NAME to screenName)
        )
    }

    fun trackScreenError(screenName: String) {
        Firebase.analytics.logEvent(
            SCREEN_ERROR_TRACK_EVENT,
            mapOf(SCREEN_NAME to screenName)
        )
    }

    fun trackSubmission(submissionState: String) {
        Firebase.analytics.logEvent(
            SUBMISSION_TRACK_EVENT,
            mapOf(SUBMISSION_STATE to submissionState)
        )
    }
}