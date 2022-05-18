package com.tandurteam.tandur.core.model.network.authentication.request

data class LoginRequest(
    val email: String,
    val password: String
)