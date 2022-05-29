package com.tandurteam.tandur.plant.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tandurteam.tandur.core.model.local.maps.UserLocation
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.plantdetail.PlantDetailRepository
import com.tandurteam.tandur.core.model.network.plantdetail.response.PlantDetailResponse

class PlantDetailViewModel(private val plantDetailRepository: PlantDetailRepository) : ViewModel() {
    fun getPlantDetail(plantName: String): LiveData<ApiResponse<PlantDetailResponse>> =
        plantDetailRepository.getPlantDetail(plantName).asLiveData()

    fun getUserLocation(): LiveData<UserLocation> =
        plantDetailRepository.getUserLocation().asLiveData()
}