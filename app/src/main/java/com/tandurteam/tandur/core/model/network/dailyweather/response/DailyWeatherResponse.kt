package com.tandurteam.tandur.core.model.network.dailyweather.response

data class DailyWeatherResponse(
    val data: DailyWeatherData?,
    val message: String?,
    val status: Int?
)