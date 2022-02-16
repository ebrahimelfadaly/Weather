package com.example.weatherapp.model.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.TypeConverters
import com.example.weatherapp.model.entities.dataConverters.AlertItemTypeConverter
import com.example.weatherapp.model.entities.dataConverters.DailyTypeConverter
import com.example.weatherapp.model.entities.dataConverters.HourlyTypeConverter
import com.example.weatherapp.model.entities.dataConverters.WeatherTypeConverter
import com.google.gson.annotations.SerializedName
@Entity(primaryKeys = ["lon", "lat"])
@JvmSuppressWildcards
@TypeConverters(WeatherTypeConverter::class,DailyTypeConverter::class,
                HourlyTypeConverter::class,AlertItemTypeConverter::class)
data class WeatherResponse(
    @field:SerializedName("current")
    @Embedded(prefix = "current_")
    val current: Current?=null,

    @field:SerializedName("daily")
    val daily: List<Daily?>?=null,

    @field:SerializedName("alerts")
    val alerts: List<AlertsItem?>? = null,

    @field:SerializedName("hourly")
    val hourly: List<Hourly?>?=null,

    @field:SerializedName("lat")
    val lat: Double,


    @field:SerializedName("lon")
    val lon: Double,


    @field:SerializedName("timezone")
    val timezone: String?=null,


    @field:SerializedName("timezone_offset")
    val timezone_offset: Long?=null
)