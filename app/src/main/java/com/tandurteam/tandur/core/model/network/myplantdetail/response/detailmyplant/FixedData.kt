package com.tandurteam.tandur.core.model.network.myplantdetail.response.detailmyplant

import com.google.gson.annotations.SerializedName

data class FixedData(

    @field:SerializedName("min_rain")
    val minRain: Double,

    @field:SerializedName("min_temp")
    val minTemp: Double,

    @field:SerializedName("max_humidity")
    val maxHumidity: Double,

    @field:SerializedName("max_temp")
    val maxTemp: Double,

    @field:SerializedName("max_rain")
    val maxRain: Double,

    @field:SerializedName("min_humidity")
    val minHumidity: Double
)