package com.example.api_to_database

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.openweathermap.org/data/2.5/forecast?appid=1c6a79075cdea7f0378df4d969daed50&q=Kathmandu&units=metric

const val BASE_URL = "https://api.openweathermap.org/"
const val API_KEY = "1c6a79075cdea7f0378df4d969daed50"

//https://api.openweathermap.org/data/2.5/forecast?appid=1c6a79075cdea7f0378df4d969daed50&q=Kathmandu&units=metric

interface WeatherInterface{

    @GET("data/2.5/forecast?appid=$API_KEY")
    fun getWeatherForecast(@Query("q") q: String,@Query("units") units: String): Call<WeatherInfo.WeatherHeader>

    @GET("data/2.5/weather?appid=$API_KEY")
    fun getCurrentWeather(@Query("q") q: String,@Query("units") units: String): Call<WeatherResponse>

}

object WeatherService{
    val weatherInstance: WeatherInterface //singleton object

    init { //executed when the class is initiated
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        //Builder is inner class of retrofit

        //implements same
        weatherInstance = retrofit.create(WeatherInterface::class.java)
    }

}