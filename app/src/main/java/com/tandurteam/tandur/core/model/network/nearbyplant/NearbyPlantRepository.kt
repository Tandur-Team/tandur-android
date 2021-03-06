package com.tandurteam.tandur.core.model.network.nearbyplant

import android.util.Log
import com.tandurteam.tandur.core.constant.DataStoreConstant
import com.tandurteam.tandur.core.constant.HttpConstant
import com.tandurteam.tandur.core.helper.SharedPreferences
import com.tandurteam.tandur.core.model.local.maps.UserLocation
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.ApiService
import com.tandurteam.tandur.core.model.network.nearbyplant.response.nearby.NearbyPlantResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class NearbyPlantRepository(
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

    fun getUserFullName(): Flow<String> {
        return flow {
            try {
                // get city
                val fullName = withContext(Dispatchers.IO) {
                    dataStore.getStringData(DataStoreConstant.FULL_NAME).firstOrNull()
                }

                fullName?.let {
                    emit(it)
                }
            } catch (e: Exception) {
                e.message?.let {
                    emit(it)
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getNearbyPlant(query: String = ""): Flow<ApiResponse<NearbyPlantResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading())

                // get token
                val token = withContext(Dispatchers.IO) {
                    dataStore.getStringData(DataStoreConstant.TOKEN).firstOrNull()
                }

                // get city
                val city = withContext(Dispatchers.IO) {
                    dataStore.getStringData(DataStoreConstant.CITY).firstOrNull()
                }

                // get sub zone
                val subZone = withContext(Dispatchers.IO) {
                    dataStore.getStringData(DataStoreConstant.SUB_ZONE).firstOrNull()
                }
                Log.d(TAG, "getNearbyPlant: $token")
                Log.d(TAG, "getNearbyPlant: $city")
                Log.d(TAG, "getNearbyPlant: $subZone")

                if (subZone != null && city != null) {
                    Log.d(TAG, "query: $query")
                    val response = if (query.isEmpty()) {
                        apiService.getNearbyPlant(
                            "Bearer $token",
                            subZone,
                            city
                        )
                    } else apiService.searchNearbyPlant("Bearer $token", query, subZone, city)
                    when (response.status) {
                        HttpConstant.STATUS_OK -> {
                            Log.d(TAG, "getNearbyPlant: Success")
                            emit(ApiResponse.Success(response))
                        }
                        HttpConstant.STATUS_NOT_FOUND -> {
                            Log.d(TAG, "getNearbyPlant: Empty")
                            emit(ApiResponse.Empty)
                        }
                        else -> {
                            Log.d(TAG, "Nearby Plant: ${response.message}")
                            emit(ApiResponse.Error(response.message.toString()))
                        }
                    }
                }


            } catch (e: Exception) {
                Log.e(TAG, "$e")
                if (e.message.toString().contains(HttpConstant.STATUS_NOT_FOUND.toString())) {
                    emit(ApiResponse.Empty)
                } else {
                    emit(ApiResponse.Error(e.toString()))
                }
            }
        }
    }

    companion object {
        private val TAG = NearbyPlantRepository::class.java.simpleName
    }
}