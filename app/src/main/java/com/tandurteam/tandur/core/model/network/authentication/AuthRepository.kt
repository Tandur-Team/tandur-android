package com.tandurteam.tandur.core.model.network.authentication

import android.util.Log
import com.tandurteam.tandur.core.constant.DataStoreConstant
import com.tandurteam.tandur.core.constant.HttpConstant
import com.tandurteam.tandur.core.helper.SharedPreferences
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.ApiService
import com.tandurteam.tandur.core.model.network.authentication.request.LoginRequest
import com.tandurteam.tandur.core.model.network.authentication.request.SignUpRequest
import com.tandurteam.tandur.core.model.network.authentication.response.LoginResponse
import com.tandurteam.tandur.core.model.network.authentication.response.SignUpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepository(
    private val apiService: ApiService,
    private val dataStore: SharedPreferences
) {
    fun signUpUser(signUpRequest: SignUpRequest): Flow<ApiResponse<SignUpResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading())

                val response = apiService.signUpUser(signUpRequest)

                when (response.status) {
                    HttpConstant.STATUS_CREATED -> {
                        Log.d(TAG, "signUpUser: Success")
                        emit(ApiResponse.Success(response))
                    }
                    else -> {
                        Log.d(TAG, "signUpUser: Fail ${response.message}")
                        response.message?.let {
                            emit(ApiResponse.Error(it))
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "signUpUser: $e")
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun loginUser(loginRequest: LoginRequest): Flow<ApiResponse<LoginResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading())

                val response = apiService.loginUser(loginRequest)

                when (response.status) {
                    HttpConstant.STATUS_OK -> {
                        // save token to datastore
                        response.token?.let {
                            dataStore.saveStringData(it, DataStoreConstant.TOKEN)
                        }

                        // save full name to datastore
                        response.fullName?.let {
                            dataStore.saveStringData(it, DataStoreConstant.FULL_NAME)
                        }

                        // save userId to datastore
                        response.userId?.let {
                            dataStore.saveStringData(it, DataStoreConstant.USER_ID)
                        }

                        // emit success
                        emit(ApiResponse.Success(response))
                    }
                    else -> {
                        response.message?.let {
                            emit(ApiResponse.Error(it))
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
        private val TAG = AuthRepository::class.java.simpleName
    }
}