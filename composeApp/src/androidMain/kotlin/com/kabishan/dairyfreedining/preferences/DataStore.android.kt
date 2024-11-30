package com.kabishan.dairyfreedining.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

actual fun createDataStore(context: Any?): DataStore<Preferences> {
    require(
        value = context is Context,
        lazyMessage = { "Context required" }
    )

    return PreferenceDataStoreFactory.createWithPath {
        context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath.toPath()
    }
}