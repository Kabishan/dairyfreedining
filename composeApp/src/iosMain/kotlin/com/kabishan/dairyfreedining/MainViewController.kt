package com.kabishan.dairyfreedining

import androidx.compose.ui.window.ComposeUIViewController
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize

fun MainViewController() = ComposeUIViewController { DairyFreeDiningApp() }

fun initialize() {
    Firebase.initialize()
}