package com.tandurteam.tandur.core.model.network.fixedplant

import android.util.Log
import com.tandurteam.tandur.core.constant.HttpConstant
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.ApiService
import com.tandurteam.tandur.core.model.network.fixedplant.response.fixed.FixedPlantResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class FixedPlantRepository(
    private val apiService: ApiService
) {
    fun getAllFixedPlants(): Flow<ApiResponse<FixedPlantResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading())

                val response = apiService.getAllFixedPlant()
                when (response.status) {
                    HttpConstant.STATUS_OK -> {
                        Log.d(TAG, "getAllFixedPlants: ${response.data}")

                        emit(ApiResponse.Success(response))
                    }
                    else -> {
                        Log.d(TAG, response.message)
                        emit(ApiResponse.Error(response.message))
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "$e")
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    companion object {
        private val TAG = FixedPlantRepository::class.java.simpleName
    }
}