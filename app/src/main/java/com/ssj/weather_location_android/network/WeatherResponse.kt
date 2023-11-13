package com.ssj.weather_location_android.network

import com.google.gson.annotations.SerializedName
import com.ssj.weather_location_android.model.Coord
import com.ssj.weather_location_android.model.Main
import com.ssj.weather_location_android.model.Sys
import com.ssj.weather_location_android.model.Weather
import com.ssj.weather_location_android.model.Wind

data class WeatherResponse(
    @SerializedName("coord") val coord:Coord,
    @SerializedName("weather") val weather:List<Weather>,
    @SerializedName("main") val main: Main,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("sys") val sys: Sys,
    @SerializedName("id") val id:Int,
    @SerializedName("name") val name:String,
)

/*

https://api.openweathermap.org/data/2.5/weather?lat=44.34&lon=10.99&appid={API key}
https://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}

{
    "coord": {
    "lon": -106.5392,
    "lat": 35.1056
},
    "weather": [
    {
        "id": 804,
        "main": "Clouds",
        "description": "overcast clouds",
        "icon": "04n"
    }
    ],
    "base": "stations",
    "main": {
    "temp": 277.92,
    "feels_like": 276.78,
    "temp_min": 276.23,
    "temp_max": 278.9,
    "pressure": 1025,
    "humidity": 81
},
    "visibility": 10000,
    "wind": {
    "speed": 1.54,
    "deg": 300
},
    "clouds": {
    "all": 100
},
    "dt": 1699699259,
    "sys": {
    "type": 1,
    "id": 3205,
    "country": "US",
    "sunrise": 1699709810,
    "sunset": 1699747418
},
    "timezone": -25200,
    "id": 5474813,
    "name": "La Cuesta",
    "cod": 200
}
*/

