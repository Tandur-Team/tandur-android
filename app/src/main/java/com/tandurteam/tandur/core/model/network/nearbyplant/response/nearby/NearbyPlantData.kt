package com.tandurteam.tandur.core.model.network.nearbyplant.response.nearby

import com.google.gson.annotations.SerializedName

data class NearbyPlantData(

	@field:SerializedName("zone_city")
	val zoneCity: String,

	@field:SerializedName("plant_harvest_date")
	val plantHarvestDate: String,

	@field:SerializedName("is_harvested")
	val isHarvested: Int,

	@field:SerializedName("satisfaction_rate")
	val satisfactionRate: Int,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("plant_start_date")
	val plantStartDate: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("zone_local")
	val zoneLocal: String,

	@field:SerializedName("plant_name")
	val plantName: String
)