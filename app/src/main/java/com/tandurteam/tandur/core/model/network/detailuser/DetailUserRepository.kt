package com.tandurteam.tandur.core.model.network.detailuser

import android.util.Log
import com.tandurteam.tandur.core.constant.DataStoreConstant
import com.tandurteam.tandur.core.constant.HttpConstant
import com.tandurteam.tandur.core.helper.SharedPreferences
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.ApiService
import com.tandurteam.tandur.core.model.network.detailuser.response.userdetail.DetailUserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class DetailUserRepository(
    private val apiService: ApiService,
    private val dataStore: SharedPreferences
) {
    fun getDetailUser(): Flow<ApiResponse<DetailUserResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading())

                // get token
                val token = withContext(Dispatchers.IO) {
                    dataStore.getStringData(DataStoreConstant.TOKEN).firstOrNull()
                }
                Log.d(TAG, "getDetailUser: $token")

                // get userId
                val userId = withContext(Dispatchers.IO) {
                    dataStore.getStringData(DataStoreConstant.USER_ID).firstOrNull()
                }
                Log.d(TAG, "getDetailUser: $userId")

                userId?.let { id ->
                    val response = apiService.getDetailUser("Bearer $token", id)
                    when (response.status) {
                        HttpConstant.STATUS_OK -> {
                            Log.d(TAG, "getDetailUser: ${response.data}")

                            emit(ApiResponse.Success(response))
                        }
                        else -> {
                            Log.d(TAG, "getDetailUser: fail ${response.data}")
                            emit(ApiResponse.Error(response.message))
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "signUpUser: $e")
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun clearUserToken() = dataStore.clearStringData(DataStoreConstant.TOKEN)

    companion object {
        private val TAG = DetailUserRepository::class.java.simpleName
    }
}