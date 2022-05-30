package com.tandurteam.tandur.core.model.network.myplant.response.myplantlist

import com.google.gson.annotations.SerializedName

data class MyPlantListData(
    @SerializedName("_id")
    val id: String?,
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
    val userId: String?,
    @SerializedName("image_url")
    val imageUrl: String
)