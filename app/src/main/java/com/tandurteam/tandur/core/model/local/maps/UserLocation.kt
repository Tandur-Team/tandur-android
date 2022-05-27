package com.tandurteam.tandur.core.model.local.maps

data class UserLocation(
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0,
    val subZone: String? = "",
    val city: String? = ""
)
