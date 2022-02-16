package com.example.weatherapp.model.remoteDataSource

import com.example.weatherapp.Constant.Final

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RemoteDataSource {
  fun getWeatherService():WeatherApi{
   return Retrofit.Builder().baseUrl(Final.BASE_URL)
       .addConverterFactory(GsonConverterFactory.create()).build()
       .create(WeatherApi::class.java)
  }


}