package com.kabishan.dairyfreedining.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

class DataStoreRepository(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun setBooleanPreference(key: Preferences.Key<Boolean>, value: Boolean): Boolean {
        return try {
            dataStore.edit { preferences ->
                preferences.set(key = key, value = value)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getBooleanPreference(key: Preferences.Key<Boolean>): Flow<Boolean> {
        return dataStore.data
            .catch { emptyFlow<Boolean>() }
            .map { preferences -> preferences[key] ?: true }
    }

    companion object {
        val SHOW_COACH_MARK_LANDING = booleanPreferencesKey(name = "show_coach_mark_landing")
        val SHOW_COACH_MARK_DETAILS = booleanPreferencesKey(name = "show_coach_mark_details")
    }
}