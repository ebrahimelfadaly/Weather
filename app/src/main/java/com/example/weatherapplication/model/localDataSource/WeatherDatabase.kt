package com.example.weatherapp.model.localDataSource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.model.entities.WeatherResponse

@Database(entities = arrayOf(WeatherResponse::class), version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase(){
    abstract fun weatherDao(): WeatherDao
}