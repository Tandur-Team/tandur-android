package com.tandurteam.tandur.core.model.network.myplantdetail

import android.util.Log
import com.tandurteam.tandur.core.constant.DataStoreConstant
import com.tandurteam.tandur.core.constant.HttpConstant
import com.tandurteam.tandur.core.helper.SharedPreferences
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.ApiService
import com.tandurteam.tandur.core.model.network.myplantdetail.request.HarvestRequest
import com.tandurteam.tandur.core.model.network.myplantdetail.response.detailmyplant.MyPlantDetailResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class MyPlantDetailRepository(
    private val apiService: ApiService,
    private val dataStore: SharedPreferences
) {
    fun getPlantDetail(
        plantName: String,
        plantId: String
    ): Flow<ApiResponse<MyPlantDetailResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading())

                // get token
                val token = withContext(Dispatchers.IO) {
                    dataStore.getStringData(DataStoreConstant.TOKEN).firstOrNull()
                }
                Log.d(TAG, "getMyPlantDetail: $token")

                val response = apiService.getMyPlantDetail(
                    "Bearer $token",
                    plantName,
                    plantId
                )

                when (response.status) {
                    HttpConstant.STATUS_OK -> {
                        emit(ApiResponse.Success(response))
                    }

                    else -> {
                        emit(ApiResponse.Error(response.message))
                    }
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun harvestPlant(
        plantId: String,
        satisfactionRate: Int
    ): Flow<ApiResponse<MyPlantDetailResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading())

                // get token
                val token = withContext(Dispatchers.IO) {
                    dataStore.getStringData(DataStoreConstant.TOKEN).firstOrNull()
                }

                // get userId
                val userId = withContext(Dispatchers.IO) {
                    dataStore.getStringData(DataStoreConstant.USER_ID).firstOrNull()
                }

                // set harvest request body
                val requestBody = HarvestRequest(satisfactionRate)

                Log.d(TAG, "getMyPlantDetail: $token")

                val response = apiService.harvestPlant(
                    "Bearer $token",
                    requestBody,
                    userId.toString(),
                    plantId
                )

                when (response.status) {
                    HttpConstant.STATUS_OK -> {
                        emit(ApiResponse.Success(response))
                    }

                    else -> {
                        emit(ApiResponse.Error(response.message))
                    }
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    companion object {
        private val TAG = MyPlantDetailRepository::class.java.simpleName
    }
}