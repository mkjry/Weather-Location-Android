package com.ssj.weather_location_android.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssj.weather_location_android.network.ApiEndpoint.APPID
import com.ssj.weather_location_android.network.RetrofitClient
import com.ssj.weather_location_android.network.WeatherDataRequest
import com.ssj.weather_location_android.network.WeatherRepository
import com.ssj.weather_location_android.network.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherResponse?>()
    val weatherData: MutableLiveData<WeatherResponse?> get() = _weatherData

    fun getWeather(lat: Double, lon: Double) {

        Log.i("last", lat.toString() + " " + lon.toString())
        viewModelScope.launch {
            Dispatchers.IO
            val response: WeatherResponse =
                RetrofitClient.createService(WeatherDataRequest::class.java)
                    .getWeather(lat, lon, APPID)

            if (response.weather.isNotEmpty()) {
//                _weatherData.value = response
                _weatherData.value = customizeValues(response)
                _weatherData.postValue(weatherData.value)
            } else {
                _weatherData.postValue(null)
            }
            return@launch
        }
    }

    private fun customizeValues(response: WeatherResponse): WeatherResponse? {

        // TODO: adjust data to databind on layout xml
        // Kelvin to Celsious ..
        return response
    }

    fun getCityWeatherData(cityName: String) {
        viewModelScope.launch {
            try {
                val response: Response<WeatherResponse> =
                    RetrofitClient.createService(WeatherDataRequest::class.java)
                        .getCityWeatherData(cityName, APPID)

                if (response.isSuccessful) {
//                    _weatherData.value = response.body()
                    _weatherData.value = response.body()?.let { customizeValues(it) }
                    _weatherData.postValue(weatherData.value)
                    Log.i("API",response.body().toString())
                } else {
                    _weatherData.postValue(null)
                    // Handle API error
                    Log.e("API Error", "Error: ${response.code()}")
                }

            } catch (e: Exception) {
                // Handle network error or other exceptions
                Log.e("Network Error", "Error: ${e.message}")
            }
        }
    }
}