package com.tandurteam.tandur.core.model.authentication.request

data class LoginRequest(
    val email: String,
    val password: String
)