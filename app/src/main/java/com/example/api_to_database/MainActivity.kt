package com.example.api_to_database


import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.api_to_database.WeatherInfo.*
import com.example.api_to_database.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import kotlin.reflect.typeOf

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView

    private var cache: CacheMemory = CacheMemory(this@MainActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        lifecycleScope.launch {
            // Start getWeather() concurrently
            val weatherDeferred = getWeather()

            // Now that getWeather() is completed, proceed with the next steps
            val mainDataList = getAllMainData()
            recyclerView.adapter = MyAdapter(this, mainDataList, getAllData())
        }

    }


    private suspend fun getAllData(): List<WeatherData> {
        var weatherDB: WeatherDatabase = WeatherDatabase.getDatabase(this@MainActivity)

        return withContext(Dispatchers.IO) {
            weatherDB.weatherDao().getAllWeatherData()
        }
    }


    private suspend fun getAllMainData(): List<Main> {
        var weatherDB: WeatherDatabase = WeatherDatabase.getDatabase(this@MainActivity)

        return withContext(Dispatchers.IO) {
            weatherDB.weatherDao().getAllMain()
        }

    }

    private suspend fun getWeather() {
        val weather: Call<WeatherHeader> =
            WeatherService.weatherInstance.getWeatherForecast("Kathmandu", "metric")

        // Use withContext to switch to the IO dispatcher for network operations
        val response = withContext(Dispatchers.IO) {
            weather.execute() // Execute the first network request

            //synchronous code i.e blocks the thread until it is completed
        }

        if (response.isSuccessful) {
            val weatherData = response.body()

            weatherData?.let {
                val weatherDB = WeatherDatabase.getDatabase(this)
                withContext(Dispatchers.IO) {
                    weatherDB.weatherDao().insertAll(it)
                    for (item in it.list) {
                        weatherDB.weatherDao().insertWeatherData(item)
                        weatherDB.weatherDao().insertMain(item.main)
                        weatherDB.weatherDao().insertWeather(item.weather[0])
                        weatherDB.weatherDao().insertClouds(item.clouds)
                        weatherDB.weatherDao().insertWinds(item.wind)
                        weatherDB.weatherDao().insertSys(item.sys)
                    }
                }
            }
        } else {
            // Handle the case when the network request is not successful
            Log.d("FAILURE", "Error in fetching weather data")
        }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        val weatherCurrent: Call<WeatherResponse> =
            WeatherService.weatherInstance.getCurrentWeather("Kathmandu", "metric")

        if (cache.get("Current Weather") == null) {
            // Execute the second network request and handle the response
            val currentWeatherResponse = withContext(Dispatchers.IO) {
                weatherCurrent.execute()
            }

            if (currentWeatherResponse.isSuccessful) {
                val weather: WeatherResponse? = currentWeatherResponse.body()

                weather?.let {
                    binding.apply {
                        weatherIn.text = it.name.toString()
                        temperature.text = "${it.main.temp.toInt()}°"
                        description.text = it.weather[0].main.toString()
                        high.text = "Max: ${it.main.temp_max}°"
                        low.text = "Min: ${it.main.temp_min}°"

                        val icon = it.weather[0].icon
                        val imageUrl = "http://openweathermap.org/img/wn/$icon@2x.png"
                        Glide.with(this@MainActivity).load(imageUrl).into(iconImage)
                    }
                    cache.put("Current Weather", Gson().toJson(it)) //cpnverts the object into Json before saving it in cache
                }
            } else {
                // Handle the case when the network request is not successful
                Log.d("FAILURE", "Error in fetching current weather data")
            }
        } else {
            // Handle the case when data is retrieved from cache
            val gson = Gson()
            val cacheDataString = cache.get("Current Weather").toString()

            Log
                .d("S",cacheDataString.toString())

            val cacheData: WeatherResponse? =
                gson.fromJson(cacheDataString, WeatherResponse::class.java)

            cacheData?.let {
                // Update UI or perform other tasks with cached data
                binding.weatherIn.text = cacheData.name.toString()
                binding.temperature.text = cacheData.main.temp.toInt().toString() + "° Cache"
                binding.description.text = cacheData.weather[0].main.toString()
                binding.high.text = "Max: " + cacheData.main.temp_max.toString() + "°"
                binding.low.text = "Min: " + cacheData.main.temp_min.toString() + "°"

                val icon = cacheData.weather[0].icon
                val imageUrl = "http://openweathermap.org/img/wn/$icon@2x.png"

                Glide.with(this@MainActivity).load(imageUrl).into(binding.iconImage)
            }
        }
    }

}

