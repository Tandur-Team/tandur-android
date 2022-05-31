package com.tandurteam.tandur.core.model.network.detailuser.response.userdetail

import com.google.gson.annotations.SerializedName

data class DetailUserData(

    @field:SerializedName("full_name")
    val fullName: String?,

    @field:SerializedName("avg_satisfaction_rate")
    val avgSatisfactionRate: Double?,

    @field:SerializedName("active_plants")
    val activePlants: Int?,

    @field:SerializedName("_id")
    val id: String?,

    @field:SerializedName("email")
    val email: String?
)