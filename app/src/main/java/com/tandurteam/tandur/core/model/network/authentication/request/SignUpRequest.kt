package com.tandurteam.tandur.core.model.network.authentication.request

data class SignUpRequest(
    val name: String,
    val email: String,
    val password: String
)
