package com.tandurteam.tandur.core.model.network.createplant.request

import com.google.gson.annotations.SerializedName

data class CreatePlantRequest(
    @SerializedName("plant_name")
    val plantName: String,
    val zone: String
)