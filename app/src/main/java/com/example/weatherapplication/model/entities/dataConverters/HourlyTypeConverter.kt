package com.example.weatherapp.model.entities.dataConverters

import androidx.room.TypeConverter
import com.example.weatherapp.model.entities.Hourly
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HourlyTypeConverter {
    companion object{
        @TypeConverter
        @JvmStatic
        fun fromHourlyList(value: MutableList<Hourly>):String{
         val gson=Gson()
          val type=object :TypeToken<MutableList<Hourly>>(){}.type
            return gson.toJson(value,type)
        }
        @TypeConverter
        @JvmStatic
        fun toHourlyList(value: String):MutableList<Hourly>{
        val gson=Gson()
        val type=object :TypeToken<MutableList<Hourly>>(){}.type
            return gson.fromJson(value,type)
        }
    }
}