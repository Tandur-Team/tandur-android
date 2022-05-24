package com.tandurteam.tandur.core.model.network.fixedplant.response.fixed

import com.google.gson.annotations.SerializedName

data class FixedPlantData(

	@field:SerializedName("image_url")
	val imageUrl: String,

	@field:SerializedName("plant_name")
	val plantName: String
)