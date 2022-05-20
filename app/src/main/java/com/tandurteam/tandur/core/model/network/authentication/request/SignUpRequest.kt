package com.tandurteam.tandur.core.model.network.authentication.request

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("full_name")
    val name: String,
    val email: String,
    val password: String
)
