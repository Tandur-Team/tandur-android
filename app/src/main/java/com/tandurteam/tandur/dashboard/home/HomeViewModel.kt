package com.tandurteam.tandur.dashboard.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tandurteam.tandur.core.model.local.maps.UserLocation
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.dailyweather.DailyWeatherRepository
import com.tandurteam.tandur.core.model.network.dailyweather.response.DailyWeatherResponse
import com.tandurteam.tandur.core.model.network.nearbyplant.NearbyPlantRepository
import com.tandurteam.tandur.core.model.network.nearbyplant.response.nearby.NearbyPlantResponse

class HomeViewModel(
    private val dailyWeatherRepository: DailyWeatherRepository,
    private val nearbyPlantRepository: NearbyPlantRepository
) : ViewModel() {

    fun getUserFullName(): LiveData<String> =
        nearbyPlantRepository.getUserFullName().asLiveData()

    fun getDailyWeather(): LiveData<ApiResponse<DailyWeatherResponse>> =
        dailyWeatherRepository.getDailyWeather().asLiveData()

    fun getUserLocation(): LiveData<UserLocation> =
        nearbyPlantRepository.getUserLocation().asLiveData()

    fun getNearbyPlant(query: String): LiveData<ApiResponse<NearbyPlantResponse>> =
        nearbyPlantRepository.getNearbyPlant(query).asLiveData()
}