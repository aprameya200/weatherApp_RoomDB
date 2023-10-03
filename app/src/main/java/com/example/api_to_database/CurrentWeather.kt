package com.example.api_to_database

import androidx.room.PrimaryKey


data class WeatherResponse(
    val coord: Coord,
    val weather: List<WeatherInfo.Weather>,
    val base: String,
    val main: WeatherInfo.Main,
    val visibility: Int,
    val wind: WeatherInfo.Wind,
    val clouds: WeatherInfo.Clouds,
    val dt: Long,
    val sys: WeatherInfo.Sys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)

data class Coord(
    val lon: Double,
    val lat: Double
)