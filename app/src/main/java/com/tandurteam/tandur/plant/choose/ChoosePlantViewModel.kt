package com.tandurteam.tandur.plant.choose

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.nearbyplant.NearbyPlantRepository
import com.tandurteam.tandur.core.model.network.nearbyplant.response.nearby.NearbyPlantResponse

class ChoosePlantViewModel(private val nearbyPlantRepository: NearbyPlantRepository): ViewModel() {
    fun getChoosePlant(): LiveData<ApiResponse<NearbyPlantResponse>> =
        nearbyPlantRepository.getNearbyPlant().asLiveData()
}