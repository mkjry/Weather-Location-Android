package com.ssj.weather_location_android.network

import retrofit2.Response

class WeatherRepository {

    private val apiService = RetrofitClient.createService(WeatherDataRequest::class.java)

    suspend fun getWeather(lat: Double, lon: Double, apiKey: String): WeatherResponse? = apiService.getWeather(lat, lon, apiKey)
    suspend fun getCityWeatherData(cityName: String, apiKey: String): Response<WeatherResponse> = apiService.getCityWeatherData(cityName, apiKey)
}