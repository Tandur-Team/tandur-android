package com.tandurteam.tandur.dashboard.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tandurteam.tandur.core.model.local.maps.UserLocation
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.fixedplant.FixedPlantRepository
import com.tandurteam.tandur.core.model.network.fixedplant.response.fixed.FixedPlantResponse
import com.tandurteam.tandur.core.model.network.nearbyplant.NearbyPlantRepository
import com.tandurteam.tandur.core.model.network.nearbyplant.response.nearby.NearbyPlantResponse

class HomeViewModel(
    private val fixedPlantRepository: FixedPlantRepository,
    private val nearbyPlantRepository: NearbyPlantRepository
) : ViewModel() {

    fun getUserFullName(): LiveData<String> =
        nearbyPlantRepository.getUserFullName().asLiveData()

    fun getAllFixedPlant(): LiveData<ApiResponse<FixedPlantResponse>> =
        fixedPlantRepository.getAllFixedPlants().asLiveData()

    fun getUserLocation(): LiveData<UserLocation> =
        nearbyPlantRepository.getUserLocation().asLiveData()

    fun getNearbyPlant(): LiveData<ApiResponse<NearbyPlantResponse>> =
        nearbyPlantRepository.getNearbyPlant().asLiveData()
}