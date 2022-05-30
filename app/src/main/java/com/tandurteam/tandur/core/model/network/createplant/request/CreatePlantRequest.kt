package com.tandurteam.tandur.core.model.network.createplant.request

import com.google.gson.annotations.SerializedName
import com.tandurteam.tandur.core.model.network.plantdetail.response.MonthlyData

data class CreatePlantRequest(
    @SerializedName("plant_name")
    val plantName: String,
    @SerializedName("zone_local")
    val zoneLocal: String,
    @SerializedName("zone_city")
    val zoneCity: String,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("long")
    val long: Double,
    @SerializedName("monthly_data")
    val monthlyData: List<MonthlyData>,
)