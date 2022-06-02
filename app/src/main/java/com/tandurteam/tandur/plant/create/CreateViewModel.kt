package com.tandurteam.tandur.plant.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tandurteam.tandur.core.model.local.maps.UserLocation
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.createplant.CreatePlantRepository
import com.tandurteam.tandur.core.model.network.createplant.response.CreatePlantResponse
import com.tandurteam.tandur.core.model.network.plantdetail.response.MonthlyData
import com.tandurteam.tandur.core.model.network.plantdetail.response.PlantDetailResponse

class CreateViewModel(private val createPlantRepository: CreatePlantRepository) : ViewModel() {
    fun createPlant(
        plantName: String,
        monthlyData: List<MonthlyData>,
        probability: Double,
        startDate: String,
        harvestDate: String
    ): LiveData<ApiResponse<CreatePlantResponse>> =
        createPlantRepository.createPlant(
            plantName,
            monthlyData,
            probability,
            startDate,
            harvestDate
        ).asLiveData()

    fun getUserLocation(): LiveData<UserLocation> =
        createPlantRepository.getUserLocation().asLiveData()

    fun getPlantDetail(plantName: String): LiveData<ApiResponse<PlantDetailResponse>> =
        createPlantRepository.getPlantDetail(plantName).asLiveData()
}