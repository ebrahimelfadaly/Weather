package com.example.weatherapp.model.entities

import androidx.room.Embedded
import androidx.room.TypeConverters
import com.example.weatherapp.model.entities.dataConverters.HourlyTypeConverter
import com.google.gson.annotations.SerializedName
@TypeConverters(HourlyTypeConverter::class)
data class Hourly(
    @field:SerializedName("clouds")
    val clouds: Double?=null,

    @field:SerializedName("dew_point")
    val dew_point: Double?=null,

    @field:SerializedName("dt")
    val dt: Double?=null,

    @field:SerializedName("feels_like")
    val feels_like: Double?=null,

    @field:SerializedName("humidity")
    val humidity: Double?=null,

    @field:SerializedName("pop")
    val pop: Double?=null,

    @field:SerializedName("pressure")
    val pressure: Double,

    @field:SerializedName("temp")
    val temp: Double?=null,

    @field:SerializedName("uvi")
    val uvi: Double,

    @field:SerializedName("snow")
    @Embedded(prefix = "snow_")
    val snow: Snow? = null,

    @field:SerializedName("rain")
    @Embedded(prefix = "rain_")
    val rain: Rain? = null,

    @field:SerializedName("visibility")
    val visibility: Double?=null,

    @field:SerializedName("weather")
    val weather: List<Weather?>?=null,

    @field:SerializedName("wind_deg")
    val wind_deg: Double?=null,

    @field:SerializedName("wind_gust")
    val wind_gust: Double?=null,

    @field:SerializedName("wind_speed")
    val wind_speed: Double?=null
)