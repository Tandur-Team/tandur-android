package com.tandurteam.tandur.core.model.network.detailuser.response.userdetail

import com.google.gson.annotations.SerializedName

data class DetailUserResponse(

    @field:SerializedName("data")
    val data: DetailUserData,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: Int
)