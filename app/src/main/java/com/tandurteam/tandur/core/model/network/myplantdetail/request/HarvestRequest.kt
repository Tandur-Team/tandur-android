package com.tandurteam.tandur.core.model.network.myplantdetail.request

import com.google.gson.annotations.SerializedName

data class HarvestRequest(
    @SerializedName("satisfaction_rate")
    val satisfactionRate: Int
)