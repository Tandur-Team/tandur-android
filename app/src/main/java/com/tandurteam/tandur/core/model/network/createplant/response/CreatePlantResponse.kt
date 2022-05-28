package com.tandurteam.tandur.core.model.network.createplant.response

data class CreatePlantResponse(
    val data: CreatePlantData?,
    val message: String?,
    val status: Int?
)