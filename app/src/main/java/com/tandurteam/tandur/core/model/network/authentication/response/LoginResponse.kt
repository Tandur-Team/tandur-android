package com.tandurteam.tandur.core.model.network.authentication.response

data class LoginResponse(
    val message: String?,
    val token: String?,
    val userId: String?
)