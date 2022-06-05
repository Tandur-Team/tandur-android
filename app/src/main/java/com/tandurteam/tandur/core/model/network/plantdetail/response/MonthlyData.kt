package com.tandurteam.tandur.core.model.network.plantdetail.response

import com.google.gson.annotations.SerializedName

data class MonthlyData(
    @SerializedName("average_humidity")
    val averageHumidity: Double?,
    @SerializedName("rain")
    val rain: Double?,
    @SerializedName("average_temp")
    val averageTemp: Double?
)