package com.kabishan.dairyfreedining

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect fun createDataStore(context: Any? = null): DataStore<Preferences>

internal const val DATA_STORE_FILE_NAME = "prefs.preferences_pb"