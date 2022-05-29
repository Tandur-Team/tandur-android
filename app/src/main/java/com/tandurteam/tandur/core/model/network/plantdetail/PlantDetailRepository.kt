package com.tandurteam.tandur.core.model.network.plantdetail

import com.tandurteam.tandur.core.constant.DataStoreConstant
import com.tandurteam.tandur.core.constant.HttpConstant
import com.tandurteam.tandur.core.helper.SharedPreferences
import com.tandurteam.tandur.core.model.local.maps.UserLocation
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.ApiService
import com.tandurteam.tandur.core.model.network.plantdetail.response.PlantDetailResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class PlantDetailRepository(
    private val apiService: ApiService,
    private val dataStore: SharedPreferences
) {
    fun getUserLocation(): Flow<UserLocation> {
        return flow {
            try {
                // get city
                val city = withContext(Dispatchers.IO) {
                    dataStore.getStringData(DataStoreConstant.CITY).firstOrNull()
                }

                // get sub zone
                val subZone = withContext(Dispatchers.IO) {
                    dataStore.getStringData(DataStoreConstant.SUB_ZONE).firstOrNull()
                }

                // get latitude
                val latitude = withContext(Dispatchers.IO) {
                    dataStore.getDoubleData(DataStoreConstant.LATITUDE).firstOrNull()
                }

                // get longitude
                val longitude = withContext(Dispatchers.IO) {
                    dataStore.getDoubleData(DataStoreConstant.LONGITUDE).firstOrNull()
                }

                emit(UserLocation(latitude, longitude, subZone, city))
            } catch (e: Exception) {
                emit(UserLocation())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getPlantDetail(plantName: String): Flow<ApiResponse<PlantDetailResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading())

                // get city
                val city = withContext(Dispatchers.IO) {
                    dataStore.getStringData(DataStoreConstant.CITY).firstOrNull()
                }

                // get sub zone
                val subZone = withContext(Dispatchers.IO) {
                    dataStore.getStringData(DataStoreConstant.SUB_ZONE).firstOrNull()
                }

                // get latitude
                val latitude = withContext(Dispatchers.IO) {
                    dataStore.getDoubleData(DataStoreConstant.LATITUDE).firstOrNull()
                }

                // get longitude
                val longitude = withContext(Dispatchers.IO) {
                    dataStore.getDoubleData(DataStoreConstant.LONGITUDE).firstOrNull()
                }

                if (latitude != null && longitude != null && city != null && subZone != null) {
                    val response = apiService.getPlantDetail(
                        plantName,
                        subZone,
                        city,
                        latitude,
                        longitude
                    )

                    when (response.status) {
                        HttpConstant.STATUS_OK -> {
                            emit(ApiResponse.Success(response))
                        }

                        else -> {
                            emit(ApiResponse.Error(response.message.toString()))
                        }
                    }
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}