package com.kabishan.dairyfreedining.about

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.kabishan.dairyfreedining.analytics.Analytics

class AboutViewModel : ViewModel(), DefaultLifecycleObserver {
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Analytics.trackScreen(Analytics.ABOUT_SCREEN)
    }
}