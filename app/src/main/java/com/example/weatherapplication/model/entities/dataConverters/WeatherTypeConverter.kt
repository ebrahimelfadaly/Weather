package com.example.weatherapp.model.entities.dataConverters

import androidx.room.TypeConverter
import com.example.weatherapp.model.entities.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WeatherTypeConverter {
    companion object{
        @TypeConverter
        @JvmStatic
        fun fromWeatherList(value: MutableList<Weather>): String {
            val gson = Gson()
            val type = object : TypeToken<MutableList<Weather>>() {}.type
            return gson.toJson(value, type)
        }

        @TypeConverter
        @JvmStatic
        fun toWeatherItemList(value: String): MutableList<Weather> {
            val gson = Gson()
            val type = object : TypeToken<MutableList<Weather>>() {}.type
            return gson.fromJson(value, type)
        }
    }
}