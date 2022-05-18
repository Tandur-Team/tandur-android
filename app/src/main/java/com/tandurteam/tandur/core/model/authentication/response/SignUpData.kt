package com.tandurteam.tandur.core.model.authentication.response

data class SignUpData(
    val _id: String?,
    val email: String?,
    val name: String?,
    val password: String?,
    val satisfaction_rate: Int?
)