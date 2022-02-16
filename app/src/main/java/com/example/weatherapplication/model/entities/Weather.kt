package com.example.weatherapp.model.entities

import com.google.gson.annotations.SerializedName

data class Weather(
    @field:SerializedName("description")
    val description: String?=null,

    @field:SerializedName("icon")
    val icon: String?=null,

    @field:SerializedName("id")
    val id: Int?=null,

    @field:SerializedName("main")
    val main: String?=null
)