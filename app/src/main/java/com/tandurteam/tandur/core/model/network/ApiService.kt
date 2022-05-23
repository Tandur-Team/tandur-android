package com.tandurteam.tandur.core.model.network

import com.tandurteam.tandur.core.model.network.authentication.request.LoginRequest
import com.tandurteam.tandur.core.model.network.authentication.request.SignUpRequest
import com.tandurteam.tandur.core.model.network.authentication.response.LoginResponse
import com.tandurteam.tandur.core.model.network.authentication.response.SignUpResponse
import com.tandurteam.tandur.core.model.network.myplant.response.myplantlist.MyPlantListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("user/signup")
    suspend fun signUpUser(
        @Body signUpRequest: SignUpRequest
    ): SignUpResponse

    @POST("user/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @GET("user/{userId}/plant")
    suspend fun getAllMyPlant(
        @Path("userId") userId: String
    ): MyPlantListResponse
}