package com.tandurteam.tandur.core.model.network.authentication.response

data class SignUpResponse(
    val data: SignUpData?,
    val status: Int?,
    val message: String?
)