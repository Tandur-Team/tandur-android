package com.tandurteam.tandur.core.model.network.detailuser.response.userdetail

import com.google.gson.annotations.SerializedName

data class DetailUserData(

    @field:SerializedName("full_name")
    val fullName: String,

    @field:SerializedName("my_plant_url")
    val myPlantUrl: String,

    @field:SerializedName("avg_satisfaction_rate")
    val avgSatisfactionRate: Int,

    @field:SerializedName("_id")
    val id: String,

    @field:SerializedName("email")
    val email: String
)