package com.tandurteam.tandur.core.model.network.authentication.response

import com.google.gson.annotations.SerializedName

data class SignUpData(
    val _id: String?,
    val email: String?,
    @SerializedName("full_name")
    val name: String?,
    val password: String?,
    val satisfaction_rate: Int?
)