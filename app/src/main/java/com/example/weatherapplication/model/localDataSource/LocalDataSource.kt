package com.example.weatherapp.model.localDataSource

import android.app.Application
import androidx.room.Room

object LocalDataSource {
   fun getInstance(application: Application) : WeatherDao{
       return Room.databaseBuilder(application, WeatherDatabase::class.java, "WeatherDatabase").build().weatherDao()
   }
}