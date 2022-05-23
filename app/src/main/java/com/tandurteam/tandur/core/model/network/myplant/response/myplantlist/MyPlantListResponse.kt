package com.tandurteam.tandur.core.model.network.myplant.response.myplantlist

data class MyPlantListResponse(
    val data: List<MyPlantListData>?,
    val message: String?,
    val status: Int?
)