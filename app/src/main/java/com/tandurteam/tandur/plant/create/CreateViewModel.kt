package com.tandurteam.tandur.plant.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tandurteam.tandur.core.model.local.maps.UserLocation
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.createplant.CreatePlantRepository
import com.tandurteam.tandur.core.model.network.createplant.response.CreatePlantResponse

class CreateViewModel(private val createPlantRepository: CreatePlantRepository) : ViewModel() {
    fun createPlant(plantName: String): LiveData<ApiResponse<CreatePlantResponse>> =
        createPlantRepository.createPlant(plantName).asLiveData()

    fun getUserLocation(): LiveData<UserLocation> =
        createPlantRepository.getUserLocation().asLiveData()
}