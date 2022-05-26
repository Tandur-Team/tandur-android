package com.tandurteam.tandur.core.model.network.plantdetail.response

import com.google.gson.annotations.SerializedName

data class PlantDetailData(
    val duration: Int?,
    @SerializedName("image_url")
    val imageUrl: String?,
    val location: String?,
    @SerializedName("monthly_data")
    val monthlyData: MonthlyData?,
    val nearby: Int?,
    @SerializedName("plant_name")
    val plantName: String?,
    val probability: Int?
)