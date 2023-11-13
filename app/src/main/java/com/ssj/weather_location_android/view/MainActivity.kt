package com.ssj.weather_location_android.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ssj.weather_location_android.R
import com.ssj.weather_location_android.databinding.ActivityMainBinding
import com.ssj.weather_location_android.network.WeatherRepository
import com.ssj.weather_location_android.network.WeatherResponse
import com.ssj.weather_location_android.viewmodel.WeatherViewModel
import com.ssj.weather_location_android.viewmodel.WeatherViewModelFactory
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Date
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val CITY = "last_city"
    private lateinit var city: String

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(!checkPermissions()) {
            requestPermission()
        }

        val repository = WeatherRepository()
        weatherViewModel = ViewModelProvider(this, WeatherViewModelFactory(repository)).get(WeatherViewModel::class.java)
        weatherViewModel.weatherData.observe(this, Observer { weatherResponse ->

            if(weatherResponse == null) {
                Toast.makeText(this, "Invalid City Name", Toast.LENGTH_SHORT).show()
            } else {
                city = weatherResponse.name
                setDataOnViews(weatherResponse)
            }
        })

        // EditBox event > trigger api call by city name
        binding.etGetCityName.setOnEditorActionListener { v, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                beginApiWeatherDataCall()

                val view = this.currentFocus
                if (view != null) {
                    val imm: InputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                    binding.etGetCityName.clearFocus()
                }
                true
            } else false
        }

        binding.refreshButton.setOnClickListener {
            beginApiWeatherDataCall()
        }

        // auto-load previous searched city, when open app
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        city = sharedPreferences.getString(CITY, "").toString()
        if (city.isNotEmpty()) {
            weatherViewModel.getCityWeatherData(city)
        }

    }

    private fun beginApiWeatherDataCall() {
        if (checkPermissions()) {

            if (isLocationEnabled()) {

                // final latitude and longitude code here
                if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
            } else {
                // open setting
                Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }

        } else {
            finishAffinity()
        }

            // api call by city_name
            weatherViewModel.getCityWeatherData(binding.etGetCityName.text.toString())

             // api call by latitude, longitude value
//            weatherViewModel.getWeather(35.156, -106.5392)
        }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    override fun onPause() {
        super.onPause()

//        Save latest searched city_name
        val editor = sharedPreferences.edit()
        editor.putString(CITY, city)
        editor.apply()
    }

    private fun setDataOnViews(body: WeatherResponse?) {

        val sdf = SimpleDateFormat("dd/MM/yyyy  hh:mm")
        val currentDate = sdf.format(Date())
        binding.tvDateAndTime.text = body!!.name + "\n" + currentDate

        binding.tvDayMaxTemp.text =
            "Day " + kelvinToCelsius(body!!.main.temp_max) + "°"
        binding.tvDayMinTemp.text =
            "Night " + kelvinToCelsius(body!!.main.temp_min) + "°"
        binding.tvTemp.text = "" + kelvinToCelsius(body!!.main.temp) + "°"
        binding.tvFeelsLike.text =
            "Feels Alike " + kelvinToCelsius(body!!.main.feels_like) + "°"
        binding.tvWeatherType.text = body.weather[0].main
        binding.tvSunrise.text = timeStampToLocalDate(body.sys.sunrise.toLong())
        binding.tvSunset.text = timeStampToLocalDate(body.sys.sunset.toLong())
        binding.tvPressure.text = body.main.pressure.toString()
        binding.tvHumidity.text = body.main.humidity.toString() + " %"
        binding.tvWindSpeed.text = body.wind.speed.toString() + " m/s"

        binding.tvTempFarenhite.text =
            "" + ((kelvinToCelsius(body.main.temp)).times(1.8).plus(32).roundToInt())

        binding.etGetCityName.setText(body.name)

        updateUI(body.weather[0].id)

    }

    private fun updateUI(id: Int) {


        if (id in 200..232) {
            //ThunderStorm
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.thunderstorm)
            binding.rlToolbar.setBackgroundColor(resources.getColor(R.color.thunderstorm))
            binding.rlSubLayout.background = ContextCompat.getDrawable(
                this@MainActivity, R.drawable.thunderstrom_bg
            )
            binding.llMainBgBelow.background = ContextCompat.getDrawable(
                this@MainActivity, R.drawable.thunderstrom_bg
            )
            binding.llMainBgAbove.background = ContextCompat.getDrawable(
                this@MainActivity, R.drawable.thunderstrom_bg
            )
            binding.ivWeatherBg.setImageResource(R.drawable.thunderstrom_bg)
            binding.ivWeatherIcon.setImageResource(R.drawable.thunderstrom)
        } else if (id in 300..321) {

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.drizzle)
            binding.rlToolbar.setBackgroundColor(resources.getColor(R.color.drizzle))
            binding.rlSubLayout.background = ContextCompat.getDrawable(
                this@MainActivity, R.drawable.drizzle_bg
            )
            binding.llMainBgBelow.background = ContextCompat.getDrawable(
                this@MainActivity,
                R.drawable.drizzle_bg
            )
            binding.llMainBgAbove.background = ContextCompat.getDrawable(
                this@MainActivity, R.drawable.drizzle_bg
            )
            binding.ivWeatherBg.setImageResource(R.drawable.drizzle_bg)
            binding.ivWeatherIcon.setImageResource(R.drawable.drizzle)

        } else if (id in 500..531) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.rain)
            binding.rlToolbar.setBackgroundColor(resources.getColor(R.color.rain))
            binding.rlSubLayout.background = ContextCompat.getDrawable(
                this@MainActivity, R.drawable.rainy_bg
            )
            binding.llMainBgBelow.background = ContextCompat.getDrawable(
                this@MainActivity, R.drawable.rainy_bg
            )
            binding.llMainBgAbove.background = ContextCompat.getDrawable(
                this@MainActivity, R.drawable.rainy_bg
            )
            binding.ivWeatherBg.setImageResource(R.drawable.rainy_bg)
            binding.ivWeatherIcon.setImageResource(R.drawable.rain)

        } else if (id in 600..620) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.snow)
            binding.rlToolbar.setBackgroundColor(resources.getColor(R.color.snow))
            binding.rlSubLayout.background = ContextCompat.getDrawable(
                this@MainActivity, R.drawable.snow_bg
            )
            binding.llMainBgBelow.background = ContextCompat.getDrawable(
                this@MainActivity, R.drawable.snow_bg
            )
            binding.llMainBgAbove.background = ContextCompat.getDrawable(
                this@MainActivity, R.drawable.snow_bg
            )
            binding.ivWeatherBg.setImageResource(R.drawable.snow_bg)
            binding.ivWeatherIcon.setImageResource(R.drawable.snow)


        } else if (id in 701..781) {

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.atmosphere)
            binding.rlToolbar.setBackgroundColor(resources.getColor(R.color.atmosphere))
            binding.rlSubLayout.background = ContextCompat.getDrawable(
                this@MainActivity, R.drawable.mist_bg
            )
            binding.llMainBgBelow.background = ContextCompat.getDrawable(
                this@MainActivity, R.drawable.mist_bg
            )
            binding.llMainBgAbove.background = ContextCompat.getDrawable(
                this@MainActivity, R.drawable.mist_bg
            )
            binding.ivWeatherBg.setImageResource(R.drawable.mist_bg)
            binding.ivWeatherIcon.setImageResource(R.drawable.mist)


        } else if (id == 800) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.clear)
            binding.rlToolbar.setBackgroundColor(resources.getColor(R.color.clear))
            binding.rlSubLayout.background = ContextCompat.getDrawable(
                this@MainActivity, R.drawable.clear_bg
            )
            binding.llMainBgBelow.background = ContextCompat.getDrawable(
                this@MainActivity, R.drawable.clear_bg
            )
            binding.llMainBgAbove.background = ContextCompat.getDrawable(
                this@MainActivity, R.drawable.clear_bg
            )

            binding.ivWeatherBg.setImageResource(R.drawable.clear_bg)
            binding.ivWeatherIcon.setImageResource(R.drawable.clear)


        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.clouds)
            binding.rlToolbar.setBackgroundColor(resources.getColor(R.color.clouds))
            binding.rlSubLayout.background = ContextCompat.getDrawable(
                this@MainActivity, R.drawable.clouds_bg
            )
            binding.llMainBgBelow.background = ContextCompat.getDrawable(
                this@MainActivity, R.drawable.clouds_bg
            )
            binding.llMainBgAbove.background = ContextCompat.getDrawable(
                this@MainActivity, R.drawable.clouds_bg
            )

            binding.ivWeatherBg.setImageResource(R.drawable.clouds_bg)
            binding.ivWeatherIcon.setImageResource(R.drawable.clouds)
        }

        binding.pdLoading.visibility = View.GONE
        binding.rlMainLayout.visibility = View.VISIBLE


    }

    private fun timeStampToLocalDate(timeStamp: Long): String {
        val localTime = timeStamp.let {
            Instant.ofEpochSecond(it).atZone(ZoneId.systemDefault()).toLocalTime()
        }
        return localTime.toString()
    }

    private fun kelvinToCelsius(temp: Double): Double {
        var intTemp = temp
        intTemp = intTemp.minus(273)
        return intTemp.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
    }

}
