package com.tandurteam.tandur.core.model.network

import com.tandurteam.tandur.core.model.network.authentication.request.LoginRequest
import com.tandurteam.tandur.core.model.network.authentication.request.SignUpRequest
import com.tandurteam.tandur.core.model.network.authentication.response.LoginResponse
import com.tandurteam.tandur.core.model.network.authentication.response.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("user/signup")
    suspend fun signUpUser(
        @Body signUpRequest: SignUpRequest
    ): SignUpResponse

    @POST("user/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): LoginResponse
}