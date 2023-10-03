package com.example.api_to_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [WeatherInfo.WeatherHeader::class, WeatherInfo.WeatherData::class, WeatherInfo.Main::class, WeatherInfo.Weather::class, WeatherInfo.Clouds::class, WeatherInfo.Sys::class, WeatherInfo.Rain::class, WeatherInfo.Wind::class],
//       , Clouds::class, Wind::class, Rain::class, Sys::class],
    version = 2
)
@TypeConverters(WeatherInfo.DataTypeConverter::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    companion object {

        @Volatile
        private var INSTANCE: WeatherDatabase? = null //hold the refenrence to the database

        /**
         * Gets the instance of the database*
         * @param context
         * @return
         */
        fun getDatabase(context: Context): WeatherDatabase {
            val tempInstance = INSTANCE

            //if instancce already exists then that instance is returned
            if (tempInstance != null) {
                return tempInstance
            }

            /**
             * else new instance is created using databaseBuilder() method
             *
             *
             * synchronized is used to prevent this code from being executed by multiple threads
             *
             * "this" = the curernt innstance of the class
             */
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, WeatherDatabase::class.java, "weather_database"
                ).build()

                //new database is assigned to instance
                INSTANCE = instance
                return instance
            }
        }


    }
}