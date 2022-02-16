package com.example.weatherapp.model.remoteDataSource

import com.example.weatherapp.model.entities.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
  @GET("data/2.5/onecall")
  suspend fun getAllData(@Query("lat") lat:String,@Query("lon") lon:String,@Query("exclude") exclude:String,
                         @Query("units") units:String,@Query("lang") lang:String,@Query("appid") appid:String) : Response<WeatherResponse>


}