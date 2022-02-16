package com.example.weatherapp.model.entities

import androidx.room.Embedded
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.weatherapp.model.entities.dataConverters.DailyTypeConverter
import com.google.gson.annotations.SerializedName

 @TypeConverters(DailyTypeConverter::class)
data class Daily(

    @field:SerializedName("clouds")
    val clouds: Double?=null,

    @field:SerializedName("dew_point")
    val dew_point: Double?=null,

    @field:SerializedName("dt")
    val dt: Double?=null,

    @Embedded(prefix = "feelsLike_")
    @field:SerializedName("feels_like")
    val feels_like: FeelsLike?=null,

    @field:SerializedName("humidity")
    val humidity: Double?=null,

    @field:SerializedName("moon_phase")
    val moon_phase: Double?=null,

    @field:SerializedName("moonrise")
    val moonrise: Double?=null,

    @field:SerializedName("moonset")
    val moonset: Double?=null,

    @field:SerializedName("pop")
    val pop: Double?=null,

    @field:SerializedName("pressure")
    val pressure: Double?=null,

    @field:SerializedName("sunrise")
    val sunrise: Double?=null,

    @field:SerializedName("sunset")
    val sunset: Double?=null,

    @Embedded(prefix = "temp_")
    @field:SerializedName("temp")
    val temp: Temp?=null,

    @field:SerializedName("uvi")
    val uvi: Double?=null,

    @field:SerializedName("weather")
    val weather: List<Weather?>?=null,

    @field:SerializedName("wind_deg")
    val wind_deg: Double?=null,

    @field:SerializedName("snow")
    val snow: Double? = null,

    @field:SerializedName("wind_gust")
    val wind_gust: Double?=null,

    @field:SerializedName("rain")
    val rain: Double? = null,

    @field:SerializedName("wind_speed")
    val wind_speed: Double?=null
)