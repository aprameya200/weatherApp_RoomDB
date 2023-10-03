package com.example.api_to_database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WeatherInfo {

    @Entity(tableName = "weather_header")
    @TypeConverters(DataTypeConverter::class) // Specify the TypeConverter class
    data class WeatherHeader(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val cod: String? = null,
        val message: Int,
        val cnt: Int,
        val list: List<WeatherData> = emptyList(),
    )


    @Entity(tableName = "weather_data")
    @TypeConverters(DataTypeConverter::class) // Specify the TypeConverter class
    data class WeatherData(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val dt: Long,
        val main: Main,
        val weather: List<Weather>,
        val clouds: Clouds,
        val wind: Wind,
        val visibility: Int,
        val pop: Double,
        val rain: Rain?,
        val sys: Sys,
        val dt_txt: String
    ) {}

    @Entity(tableName = "main")
    @TypeConverters(DataTypeConverter::class) // Specify the TypeConverter class
    data class Main(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val temp: Double,
        val feels_like: Double,
        val temp_min: Double,
        val temp_max: Double,
        val pressure: Int,
        val sea_level: Int,
        val grnd_level: Int,
        val humidity: Int,
        val temp_kf: Double
    ) {}

    @Entity(tableName = "weather")
    @TypeConverters(DataTypeConverter::class) // Specify the TypeConverter class
    data class Weather(
        @PrimaryKey(autoGenerate = true)
        val key: Int,
        val id: Int,
        val main: String,
        val description: String,
        val icon: String
    ) {}

    @Entity(tableName = "clouds")
    @TypeConverters(DataTypeConverter::class) // Specify the TypeConverter class
    data class Clouds(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val all: Int
    ) {}

    @Entity(tableName = "wind")
    @TypeConverters(DataTypeConverter::class) // Specify the TypeConverter class
    data class Wind(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val speed: Double,
        val deg: Int,
        val gust: Double
    ) {}

    @Entity(tableName = "rain")
    @TypeConverters(DataTypeConverter::class) // Specify the TypeConverter class
    data class Rain(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        val three_hours: Double // Note the backticks around `3h` to match the JSON key
    ) {

    }


    @Entity(tableName = "sys")
    @TypeConverters(DataTypeConverter::class) // Specify the TypeConverter class
    data class Sys(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val pod: String
    ) {}


    class DataTypeConverter {
        @TypeConverter
        fun fromWeatherDataList(weatherDataList: List<WeatherData>): String {
            val gson = Gson()
            return gson.toJson(weatherDataList)
        }

        @TypeConverter
        fun toWeatherDataList(json: String): List<WeatherData> {
            val gson = Gson()
            val type = object : TypeToken<List<WeatherData>>() {}.type
            return gson.fromJson(json, type)
        }

        @TypeConverter
        fun fromMain(main: Main?): String? {
            val gson = Gson()
            return gson.toJson(main)
        }

        @TypeConverter
        fun toMain(json: String?): Main? {
            val gson = Gson()
            return gson.fromJson(json, Main::class.java)
        }

        @TypeConverter
        fun fromWeatherList(weatherList: List<Weather>?): String? {
            val gson = Gson()
            return gson.toJson(weatherList)
        }

        @TypeConverter
        fun toWeatherList(json: String?): List<Weather>? {
            val gson = Gson()
            val listType = object : TypeToken<List<Weather>>() {}.type
            return gson.fromJson(json, listType)
        }

        @TypeConverter
        fun fromClouds(clouds: Clouds?): String? {
            val gson = Gson()
            return gson.toJson(clouds)
        }

        @TypeConverter
        fun toClouds(json: String?): Clouds? {
            val gson = Gson()
            return gson.fromJson(json, Clouds::class.java)
        }

        @TypeConverter
        fun fromWind(wind: Wind?): String? {
            val gson = Gson()
            return gson.toJson(wind)
        }

        @TypeConverter
        fun toWind(json: String?): Wind? {
            val gson = Gson()
            return gson.fromJson(json, Wind::class.java)
        }

        @TypeConverter
        fun fromRain(rain: Rain?): String? {
            val gson = Gson()
            return gson.toJson(rain)
        }

        @TypeConverter
        fun toRain(json: String?): Rain? {
            val gson = Gson()
            return gson.fromJson(json, Rain::class.java)
        }

        @TypeConverter
        fun fromSys(sys: Sys?): String? {
            val gson = Gson()
            return gson.toJson(sys)
        }

        @TypeConverter
        fun toSys(json: String?): Sys? {
            val gson = Gson()
            return gson.fromJson(json, Sys::class.java)
        }
    }


}
