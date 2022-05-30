package com.tandurteam.tandur.core.model.network.nearbyplant.response.nearby

import com.google.gson.annotations.SerializedName

data class NearbyPlantData(

    @field:SerializedName("image_url")
    val imageUrl: String,

    @field:SerializedName("avg_satisfaction_rate")
    val avgSatisfactionRate: Int,

    @field:SerializedName("harvest_duration")
    val harvestDuration: Int,

    @field:SerializedName("nearby")
    val nearby: Int,

    @field:SerializedName("plant_name")
    val plantName: String
)