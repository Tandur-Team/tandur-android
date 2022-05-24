package com.tandurteam.tandur.core.model.network

import com.tandurteam.tandur.core.model.network.authentication.request.LoginRequest
import com.tandurteam.tandur.core.model.network.authentication.request.SignUpRequest
import com.tandurteam.tandur.core.model.network.authentication.response.LoginResponse
import com.tandurteam.tandur.core.model.network.authentication.response.SignUpResponse
import com.tandurteam.tandur.core.model.network.fixedplant.response.fixed.FixedPlantResponse
import com.tandurteam.tandur.core.model.network.myplant.response.myplantlist.MyPlantListResponse
import com.tandurteam.tandur.core.model.network.nearbyplant.response.nearby.NearbyPlantResponse
import retrofit2.http.*

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
        @Header("Authorization") bearerToken: String,
        @Path("userId") userId: String
    ): MyPlantListResponse

    @GET("fixedplant")
    suspend fun getAllFixedPlant(): FixedPlantResponse

    @GET("plant/")
    suspend fun getNearbyPlant(
        @Header("Authorization") bearerToken: String,
        @Query("zone_local") zoneLocal: String,
        @Query("zone_city") zoneCity: String
    ): NearbyPlantResponse
}