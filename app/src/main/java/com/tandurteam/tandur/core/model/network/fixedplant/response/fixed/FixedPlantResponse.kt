package com.tandurteam.tandur.core.model.network.fixedplant.response.fixed

data class FixedPlantResponse(
	val data: List<FixedPlantData>,

	val message: String,

	val status: Int
)