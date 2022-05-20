package com.tandurteam.tandur.core.helper

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SharedPreferences(private val dataStore: DataStore<Preferences>) {
    private val token = stringPreferencesKey("token")

    fun getUserToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[token] ?: ""
        }
    }

    suspend fun saveUserToken(userToken: String) {
        dataStore.edit { preferences ->
            preferences[token] = userToken
        }
    }
}