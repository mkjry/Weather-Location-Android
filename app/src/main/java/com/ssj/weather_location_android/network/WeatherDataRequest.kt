package com.ssj.weather_location_android.network

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherDataRequest {
    @GET("weather")
    suspend fun getWeather(
        @Query("lat")lat: Double,
        @Query("lon")lon: Double,
        @Query("appid")apiKey: String
    ): WeatherResponse

    @GET("weather")
    suspend fun getCityWeatherData(
        @Query("q") cityName: String,
        @Query("appid")apiKey: String
    ): Response<WeatherResponse>

}