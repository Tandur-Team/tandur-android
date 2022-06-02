package com.tandurteam.tandur.core.model.network.createplant.request

import com.google.gson.annotations.SerializedName
import com.tandurteam.tandur.core.model.network.plantdetail.response.MonthlyData

data class CreatePlantRequest(
    @SerializedName("plant_name")
    var plantName: String,
    @SerializedName("zone_local")
    var zoneLocal: String,
    @SerializedName("zone_city")
    var zoneCity: String,
    @SerializedName("lat")
    var lat: Double,
    @SerializedName("long")
    var long: Double,
    @SerializedName("monthly_data")
    var monthlyData: List<MonthlyData>,
    @SerializedName("probability")
    var probability: Double,
    @SerializedName("start_date")
    var startDate: String,
    @SerializedName("harvest_date")
    var harvestDate: String,
)