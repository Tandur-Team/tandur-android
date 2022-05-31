package com.tandurteam.tandur.core.model.network.myplant.response.myplantlist

data class MyPlantListResponse(
    var data: List<MyPlantListData>?,
    val message: String?,
    val status: Int?
)