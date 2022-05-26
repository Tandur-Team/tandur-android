package com.tandurteam.tandur.core.model.network.plantdetail.response

import com.google.gson.annotations.SerializedName

data class MonthlyData(
    @SerializedName("average_humidity")
    val averageHumidity: List<Double>?,
    @SerializedName("average_rain")
    val averageRain: List<Double>?,
    @SerializedName("average_temp")
    val averageTemp: List<Double>?
)