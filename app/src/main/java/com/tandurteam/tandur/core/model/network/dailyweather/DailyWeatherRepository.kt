package com.tandurteam.tandur.core.model.network.dailyweather

import android.util.Log
import com.tandurteam.tandur.core.constant.DataStoreConstant
import com.tandurteam.tandur.core.constant.HttpConstant
import com.tandurteam.tandur.core.helper.SharedPreferences
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.ApiService
import com.tandurteam.tandur.core.model.network.dailyweather.response.DailyWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class DailyWeatherRepository(
    private val apiService: ApiService,
    private val dataStore: SharedPreferences
) {
    fun getDailyWeather(): Flow<ApiResponse<DailyWeatherResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading())

                // get latitude
                val latitude = withContext(Dispatchers.IO) {
                    dataStore.getDoubleData(DataStoreConstant.LATITUDE).firstOrNull()
                }

                // get longitude
                val longitude = withContext(Dispatchers.IO) {
                    dataStore.getDoubleData(DataStoreConstant.LONGITUDE).firstOrNull()
                }

                // get token
                val token = withContext(Dispatchers.IO) {
                    dataStore.getStringData(DataStoreConstant.TOKEN).firstOrNull()
                }

                if (token != null && latitude != null && longitude != null) {
                    val response = apiService.getDailyWeather("Bearer $token", latitude, longitude)

                    when (response.status) {
                        HttpConstant.STATUS_OK -> {
                            emit(ApiResponse.Success(response))
                        }

                        else -> {
                            response.message?.let { errorMessage ->
                                emit(ApiResponse.Error(errorMessage))
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "${e.message}")
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    companion object {
        private val TAG = DailyWeatherRepository::class.java.simpleName
    }
}