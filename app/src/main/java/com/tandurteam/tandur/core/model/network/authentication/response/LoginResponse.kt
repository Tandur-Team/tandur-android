package com.tandurteam.tandur.core.model.network.authentication.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val message: String?,
    val status: Int?,
    val token: String?,
    @SerializedName("user_id")
    val userId: String?
)