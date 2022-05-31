package com.tandurteam.tandur.core.model.network.plantdetail.response

import com.google.gson.annotations.SerializedName
import com.tandurteam.tandur.core.model.network.myplantdetail.response.detailmyplant.FixedData

data class PlantDetailData(
    val duration: Int?,
    @SerializedName("image_url")
    val imageUrl: String?,
    val location: String?,
    @SerializedName("monthly_data")
    val monthlyData: List<MonthlyData>?,
    val nearby: Int?,
    @SerializedName("plant_name")
    val plantName: String?,
    @field:SerializedName("fixed_data")
    val fixedData: FixedData,
    val probability: Int?
)