package com.tandurteam.tandur.core.model.authentication.request

data class SignUpRequest(
    val name: String,
    val email: String,
    val password: String
)
