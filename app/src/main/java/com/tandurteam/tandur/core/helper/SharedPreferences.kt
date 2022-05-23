package com.tandurteam.tandur.core.helper

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SharedPreferences(private val context: Context) {

    fun getStringData(key: String): Flow<String> {
        val prefKey = stringPreferencesKey(key)
        return context.dataStore.data.map { preferences ->
            preferences[prefKey] ?: ""
        }
    }

    suspend fun saveStringData(userToken: String, key: String) {
        val prefKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[prefKey] = userToken
        }
    }

    suspend fun clearStringData(key: String) {
        val prefKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[prefKey] = ""
        }
    }
}