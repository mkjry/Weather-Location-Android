package com.ssj.weather_location_android.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ssj.weather_location_android.databinding.ActivityMainBinding
import com.ssj.weather_location_android.network.WeatherRepository
import com.ssj.weather_location_android.viewmodel.WeatherViewModel
import com.ssj.weather_location_android.viewmodel.WeatherViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = WeatherRepository()
        weatherViewModel = ViewModelProvider(this, WeatherViewModelFactory(repository))
            .get(WeatherViewModel::class.java)

        weatherViewModel.weatherData.observe(this, Observer { weatherResponse ->
            // update UI, also through databinding in xml
            binding.tvTextView.text = weatherResponse?.toString() ?: "null"
        })

//        weatherViewModel.getWeather(35.1056, -106.5392) // api call by location
        weatherViewModel.getCityWeatherData("albuquerque") // api call by city_name

    }
}