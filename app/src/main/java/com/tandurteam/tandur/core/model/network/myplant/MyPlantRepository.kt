package com.tandurteam.tandur.core.model.network.myplant

import android.util.Log
import androidx.lifecycle.asLiveData
import com.tandurteam.tandur.core.constant.DataStoreConstant
import com.tandurteam.tandur.core.constant.HttpConstant
import com.tandurteam.tandur.core.helper.SharedPreferences
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.ApiService
import com.tandurteam.tandur.core.model.network.myplant.response.myplantlist.MyPlantListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MyPlantRepository(
    private val apiService: ApiService,
    private val dataStore: SharedPreferences
) {
    fun getAllMyPlant(): Flow<ApiResponse<MyPlantListResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading())

                val userId = dataStore.getStringData(DataStoreConstant.USER_ID).asLiveData().value

                userId?.let { id ->
                    val response = apiService.getAllMyPlant(id)
                    when (response.status) {
                        HttpConstant.STATUS_OK -> {
                            Log.d(TAG, "getAllMyPlant: ${response.data}")

                            response.data?.let {
                                if (it.isNotEmpty()) emit(ApiResponse.Success(response))
                                else emit(ApiResponse.Empty)
                            }
                        }
                        else -> {
                            Log.d(TAG, "signUpUser: Fail ${response.message}")
                            response.message?.let {
                                emit(ApiResponse.Error(it))
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "signUpUser: $e")
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    companion object {
        private val TAG = MyPlantRepository::class.java.simpleName
    }
}