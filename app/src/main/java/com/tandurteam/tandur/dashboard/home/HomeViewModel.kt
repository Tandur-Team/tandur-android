package com.tandurteam.tandur.dashboard.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.fixedplant.FixedPlantRepository
import com.tandurteam.tandur.core.model.network.fixedplant.response.fixed.FixedPlantResponse

class HomeViewModel(private val  fixedPlantRepository: FixedPlantRepository): ViewModel() {
    fun getAllFixedPlant(): LiveData<ApiResponse<FixedPlantResponse>> =
        fixedPlantRepository.getAllFixedPlants().asLiveData()
}