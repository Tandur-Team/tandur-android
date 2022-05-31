package com.tandurteam.tandur.dashboard.myplantlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.myplant.MyPlantRepository
import com.tandurteam.tandur.core.model.network.myplant.response.myplantlist.MyPlantListResponse

class MyPlantViewModel(private val myPlantRepository: MyPlantRepository) : ViewModel() {
    fun getAllMyPlant(isHarvested: Int): LiveData<ApiResponse<MyPlantListResponse>> =
        myPlantRepository.getAllMyPlant(isHarvested).asLiveData()
}