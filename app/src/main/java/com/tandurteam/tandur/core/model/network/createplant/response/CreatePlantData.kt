package com.tandurteam.tandur.core.model.network.createplant.response

import com.google.gson.annotations.SerializedName

data class CreatePlantData(
    @SerializedName("_id")
    val id: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("is_harvested")
    val isHarvested: Int?,
    @SerializedName("plant_harvest_date")
    val plantHarvestDate: String?,
    @SerializedName("plant_name")
    val plantName: String?,
    @SerializedName("plant_start_date")
    val plantStartDate: String?,
    @SerializedName("satisfaction_rate")
    val satisfactionRate: Int?,
    @SerializedName("user_id")
    val userId: String?
)