package com.tandurteam.tandur.core.model.network.nearbyplant.response.nearby

import com.google.gson.annotations.SerializedName

data class NearbyPlantResponse(

    @field:SerializedName("data")
    val data: List<NearbyPlantData>,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: Int
)