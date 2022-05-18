package com.tandurteam.tandur.core.model.authentication.response

data class LoginResponse(
    val message: String?,
    val token: String?,
    val userId: String?
)