package com.tandurteam.tandur.core.model.network.myplantdetail.response.detailmyplant

import com.google.gson.annotations.SerializedName

data class MyPlantDetailResponse(

    @field:SerializedName("data")
    val data: MyPlantDetailData,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: Int
)