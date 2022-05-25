package com.tandurteam.tandur.core.model.network.nearbyplant

import android.util.Log
import com.tandurteam.tandur.core.constant.DataStoreConstant
import com.tandurteam.tandur.core.constant.HttpConstant
import com.tandurteam.tandur.core.helper.SharedPreferences
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.ApiService
import com.tandurteam.tandur.core.model.network.nearbyplant.response.nearby.NearbyPlantResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class NearbyPlantRepository(
    private val apiService: ApiService,
    private val dataStore: SharedPreferences
) {
    fun getNearbyPlant(): Flow<ApiResponse<NearbyPlantResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading())

                // get token
                val token = withContext(Dispatchers.IO) {
                    dataStore.getStringData(DataStoreConstant.TOKEN).firstOrNull()
                }
                Log.d(TAG, "getNearbyPlant: $token")

                val response =
                    apiService.getNearbyPlant("Bearer $token", DUMMY_ZONE_LOCAL, DUMMY_ZONE_CITY)
                when (response.status) {
                    HttpConstant.STATUS_OK -> {
                        Log.d(TAG, "getNearbyPlant: Success")

                        emit(ApiResponse.Success(response))
                    }
                    else -> {
                        Log.d(TAG, "Nearby Plant: ${response.message}")
                        emit(ApiResponse.Error(response.message))
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "$e")
                emit(ApiResponse.Error(e.toString()))
            }
        }
    }

    companion object {
        private val TAG = NearbyPlantRepository::class.java.simpleName
        private const val DUMMY_ZONE_LOCAL = "Sekardangan"
        private const val DUMMY_ZONE_CITY = "Sidoarjo"
    }
}