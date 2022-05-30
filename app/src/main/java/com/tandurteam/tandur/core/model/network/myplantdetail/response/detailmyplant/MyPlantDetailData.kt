package com.tandurteam.tandur.core.model.network.myplantdetail.response.detailmyplant

import com.google.gson.annotations.SerializedName
import com.tandurteam.tandur.core.model.network.plantdetail.response.MonthlyData

data class MyPlantDetailData(

    @field:SerializedName("plant_harvest_date")
    val plantHarvestDate: String,

    @field:SerializedName("monthly_data")
    val monthlyData: List<MonthlyData>,

    @field:SerializedName("probability")
    val probability: Int,

    @field:SerializedName("image_url")
    val imageUrl: String,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("long")
    val jsonMemberLong: Double,

    @field:SerializedName("zone_city")
    val zoneCity: String,

    @field:SerializedName("is_harvested")
    val isHarvested: Int,

    @field:SerializedName("satisfaction_rate")
    val satisfactionRate: Int,

    @field:SerializedName("user_id")
    val userId: String,

    @field:SerializedName("plant_start_date")
    val plantStartDate: String,

    @field:SerializedName("fixedData")
    val fixedData: FixedData,

    @field:SerializedName("_id")
    val id: String,

    @field:SerializedName("zone_local")
    val zoneLocal: String,

    @field:SerializedName("lat")
    val lat: Double,

    @field:SerializedName("plant_name")
    val plantName: String
)