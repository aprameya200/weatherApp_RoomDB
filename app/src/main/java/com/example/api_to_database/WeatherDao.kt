package com.example.api_to_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WeatherDao {

    @Insert
    fun insertAll(weather: WeatherInfo.WeatherHeader)

    @Insert
    fun insertWeatherData(weatherData: WeatherInfo.WeatherData)

    @Insert
    fun insertMain(weatherMain: WeatherInfo.Main)

    @Insert
    fun insertWeather(weatherWeather: WeatherInfo.Weather)

    @Insert
    fun insertClouds(weatherClouds: WeatherInfo.Clouds)

    @Insert
    fun insertWinds(weatherWinds: WeatherInfo.Wind)

    @Insert
    fun insertRain(weatherRain: WeatherInfo.Rain)

    @Insert
    fun insertSys(weatherSys: WeatherInfo.Sys)

    @Query("SELECT * FROM main")
    fun getAllMain() : List<WeatherInfo.Main>

    @Query("SELECT dt_txt FROM weather_data")
    fun getDailyForecast() : List<String>

    @Query("SELECT * FROM weather_data")
    fun getAllWeatherData() : List<WeatherInfo.WeatherData>



}