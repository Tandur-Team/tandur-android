package com.tandurteam.tandur.core.model.network.plantdetail

import com.tandurteam.tandur.core.constant.HttpConstant
import com.tandurteam.tandur.core.helper.SharedPreferences
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.ApiService
import com.tandurteam.tandur.core.model.network.plantdetail.response.PlantDetailResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PlantDetailRepository(
    private val apiService: ApiService,
    private val dataStore: SharedPreferences
) {
    fun getPlantDetail(plantName: String): Flow<ApiResponse<PlantDetailResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading())

                val response = apiService.getPlantDetail(
                    plantName,
                    "Sekardangan",
                    "Sidoarjo",
                    -7.800895,
                    112.009700
                )

                when (response.status) {
                    HttpConstant.STATUS_OK -> {
                        emit(ApiResponse.Success(response))
                    }

                    else -> {
                        emit(ApiResponse.Error(response.message.toString()))
                    }
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}