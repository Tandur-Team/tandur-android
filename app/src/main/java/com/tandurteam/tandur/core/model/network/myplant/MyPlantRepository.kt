package com.tandurteam.tandur.core.model.network.myplant

import android.util.Log
import com.tandurteam.tandur.core.constant.DataStoreConstant
import com.tandurteam.tandur.core.constant.HttpConstant
import com.tandurteam.tandur.core.helper.SharedPreferences
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.ApiService
import com.tandurteam.tandur.core.model.network.myplant.response.myplantlist.MyPlantListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class MyPlantRepository(
    private val apiService: ApiService,
    private val dataStore: SharedPreferences
) {
    fun getAllMyPlant(
        isHarvested: Int,
        query: String = ""
    ): Flow<ApiResponse<MyPlantListResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading())

                // get userId
                val userId = withContext(Dispatchers.IO) {
                    dataStore.getStringData(DataStoreConstant.USER_ID).firstOrNull()
                }
                Log.d(TAG, "getAllMyPlant: $userId")

                // get token
                val token = withContext(Dispatchers.IO) {
                    dataStore.getStringData(DataStoreConstant.TOKEN).firstOrNull()
                }
                Log.d(TAG, "getAllMyPlant: $token")

                userId?.let { id ->
                    val response = if (query.isEmpty()) {
                        apiService.getAllMyPlant("Bearer $token", id)
                    } else apiService.searchAllMyPlant("Bearer $token", id, query)
                    when (response.status) {
                        HttpConstant.STATUS_OK -> {
                            Log.d(TAG, "getAllMyPlant: ${response.data}")

                            response.data?.let {
                                val filteredList = it.filter { myPlants ->
                                    myPlants.isHarvested == isHarvested
                                }
                                response.data = filteredList
                                if (filteredList.isNotEmpty()) emit(ApiResponse.Success(response))
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