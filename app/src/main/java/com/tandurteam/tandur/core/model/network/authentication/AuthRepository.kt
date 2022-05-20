package com.tandurteam.tandur.core.model.network.authentication

import android.util.Log
import androidx.lifecycle.asLiveData
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

                when (response.message) {
                    USER_CREATED -> {
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

                when (response.message) {
                    AUTH_SUCCESS -> {
                        // save token to datastore
                        response.token?.let {
                            dataStore.saveUserToken(it)
                        }
                        Log.d(TAG, "loginUser: ${dataStore.getUserToken().asLiveData().value}")

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
        private const val USER_CREATED = "User created"
        private const val AUTH_SUCCESS = "Auth Success"
    }
}