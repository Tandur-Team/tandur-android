package com.tandurteam.tandur.core.model.network

import com.tandurteam.tandur.core.model.network.authentication.request.LoginRequest
import com.tandurteam.tandur.core.model.network.authentication.request.SignUpRequest
import com.tandurteam.tandur.core.model.network.authentication.response.LoginResponse
import com.tandurteam.tandur.core.model.network.authentication.response.SignUpResponse
import com.tandurteam.tandur.core.model.network.createplant.request.CreatePlantRequest
import com.tandurteam.tandur.core.model.network.createplant.response.CreatePlantResponse
import com.tandurteam.tandur.core.model.network.dailyweather.response.DailyWeatherResponse
import com.tandurteam.tandur.core.model.network.detailuser.response.userdetail.DetailUserResponse
import com.tandurteam.tandur.core.model.network.fixedplant.response.fixed.FixedPlantResponse
import com.tandurteam.tandur.core.model.network.myplant.response.myplantlist.MyPlantListResponse
import com.tandurteam.tandur.core.model.network.myplantdetail.request.HarvestRequest
import com.tandurteam.tandur.core.model.network.myplantdetail.response.detailmyplant.MyPlantDetailResponse
import com.tandurteam.tandur.core.model.network.nearbyplant.response.nearby.NearbyPlantResponse
import com.tandurteam.tandur.core.model.network.plantdetail.response.PlantDetailResponse
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

    @POST("user/{userId}/plant")
    suspend fun createPlant(
        @Header("Authorization") bearerToken: String,
        @Body createPlantRequest: CreatePlantRequest,
        @Path("userId") userId: String
    ): CreatePlantResponse

    @PATCH("user/{userId}/plant/{plantId}")
    suspend fun harvestPlant(
        @Header("Authorization") bearerToken: String,
        @Body harvestRequest: HarvestRequest,
        @Path("userId") userId: String,
        @Path("plantId") plantId: String
    ): MyPlantDetailResponse

    @GET("user/{userId}/plant")
    suspend fun getAllMyPlant(
        @Header("Authorization") bearerToken: String,
        @Path("userId") userId: String
    ): MyPlantListResponse

    @GET("user/{userId}/plant/search")
    suspend fun searchAllMyPlant(
        @Header("Authorization") bearerToken: String,
        @Path("userId") userId: String,
        @Query("search") query: String
    ): MyPlantListResponse

    @GET("fixedplant")
    suspend fun getAllFixedPlant(): FixedPlantResponse

    @GET("plant/")
    suspend fun getNearbyPlant(
        @Header("Authorization") bearerToken: String,
        @Query("zone_local") zoneLocal: String,
        @Query("zone_city") zoneCity: String
    ): NearbyPlantResponse

    @GET("plant/search")
    suspend fun searchNearbyPlant(
        @Header("Authorization") bearerToken: String,
        @Query("search") search: String,
        @Query("zone_local") zoneLocal: String,
        @Query("zone_city") zoneCity: String,
    ): NearbyPlantResponse

    @GET("plant/{plantName}")
    suspend fun getPlantDetail(
        @Path("plantName") plantName: String,
        @Query("zone_local") zoneLocal: String,
        @Query("zone_city") zoneCity: String,
        @Query("lat") lat: Double,
        @Query("long") lng: Double,
    ): PlantDetailResponse

    @GET("user/{userId}")
    suspend fun getDetailUser(
        @Header("Authorization") bearerToken: String,
        @Path("userId") userId: String
    ): DetailUserResponse

    @GET("user/{userId}/plant/{plantId}")
    suspend fun getMyPlantDetail(
        @Header("Authorization") bearerToken: String,
        @Path("userId") userId: String,
        @Path("plantId") plantId: String,
    ): MyPlantDetailResponse

    @GET("weather/")
    suspend fun getDailyWeather(
        @Header("Authorization") bearerToken: String,
        @Query("lat") latitude: Double,
        @Query("long") longitude: Double,
    ): DailyWeatherResponse
}