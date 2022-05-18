package com.tandurteam.tandur.core.model.network

import android.util.Log
import com.tandurteam.tandur.core.model.network.authentication.request.SignUpRequest
import com.tandurteam.tandur.core.model.network.authentication.response.SignUpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun signUpUser(signUpRequest: SignUpRequest): ApiResponse<SignUpResponse> {
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.signUpUser(signUpRequest)
            }

            when (response.message) {
                USER_CREATED -> {
                    ApiResponse.Success(response)
                }
                else -> {
                    response.message?.let {
                        ApiResponse.Error(it)
                    }
                }
            }
            ApiResponse.Success(response)
        } catch (e: Exception) {
            Log.e(TAG, "signUpUser: $e")
            ApiResponse.Error(e.toString())
        }
    }

    companion object {
        private val TAG = RemoteDataSource::class.java.simpleName
        private const val USER_CREATED = "User Created"
    }
}