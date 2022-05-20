package com.tandurteam.tandur.dashboard.profile

import androidx.lifecycle.ViewModel
import com.tandurteam.tandur.core.helper.SharedPreferences

class ProfileViewModel(private val dataStore: SharedPreferences) : ViewModel() {
    suspend fun clearUserToken() = dataStore.clearUserToken()
}