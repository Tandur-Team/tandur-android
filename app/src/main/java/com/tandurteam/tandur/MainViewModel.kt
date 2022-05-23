package com.tandurteam.tandur

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tandurteam.tandur.core.constant.DataStoreConstant
import com.tandurteam.tandur.core.helper.SharedPreferences

class MainViewModel(private val dataStore: SharedPreferences) : ViewModel() {
    fun getUserToken() = dataStore.getStringData(DataStoreConstant.TOKEN).asLiveData()
}