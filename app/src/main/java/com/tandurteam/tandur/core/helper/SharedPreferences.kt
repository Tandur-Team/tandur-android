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

    private val token = stringPreferencesKey("token")

    fun getUserToken(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[token] ?: ""
        }
    }

    suspend fun saveUserToken(userToken: String) {
        context.dataStore.edit { preferences ->
            preferences[token] = userToken
        }
    }

    suspend fun clearUserToken() {
        context.dataStore.edit { preferences ->
            preferences[token] = ""
        }
    }
}