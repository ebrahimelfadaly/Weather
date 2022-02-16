package com.example.weatherapp.model.entities.dataConverters

import androidx.room.TypeConverter
import com.example.weatherapp.model.entities.Daily
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DailyTypeConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromDailyList(value: MutableList<Daily>): String {
            val gson = Gson()
            val type = object : TypeToken<MutableList<Daily>>() {}.type
            return gson.toJson(value, type)
        }

        @TypeConverter
        @JvmStatic
        fun toDailyList(value: String): MutableList<Daily> {
            val gson = Gson()
            val type = object : TypeToken<MutableList<Daily>>() {}.type
            return gson.fromJson(value, type)
        }
    }
}