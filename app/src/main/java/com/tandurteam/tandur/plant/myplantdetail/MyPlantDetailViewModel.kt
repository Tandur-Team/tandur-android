package com.tandurteam.tandur.plant.myplantdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.myplantdetail.MyPlantDetailRepository
import com.tandurteam.tandur.core.model.network.myplantdetail.response.detailmyplant.MyPlantDetailResponse

class MyPlantDetailViewModel(
    private val myPlantDetailRepository: MyPlantDetailRepository
) : ViewModel() {
    fun getMyPlantDetail(
        plantName: String,
        plantId: String
    ): LiveData<ApiResponse<MyPlantDetailResponse>> =
        myPlantDetailRepository.getPlantDetail(plantName, plantId).asLiveData()

    fun harvestPlant(
        plantId: String,
        satisfactionRate: Int
    ): LiveData<ApiResponse<MyPlantDetailResponse>> =
        myPlantDetailRepository.harvestPlant(plantId, satisfactionRate).asLiveData()
}