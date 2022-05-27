package com.tandurteam.tandur.maps

import androidx.lifecycle.ViewModel
import com.tandurteam.tandur.core.helper.SharedPreferences

class MapsViewModel(private val dataStore: SharedPreferences) : ViewModel() {
    suspend fun setUserLocation(location: String, key: String) {
        dataStore.saveStringData(location, key)
    }

    suspend fun setUserLatLng(pointLocation: Double, key: String) {
        dataStore.saveDoubleData(pointLocation, key)
    }
}