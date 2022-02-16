package com.example.weatherapp.model.entities

import com.google.gson.annotations.SerializedName

data class AlertsItem(

	@field:SerializedName("start")
	val start: Double? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("sender_name")
	val senderName: String? = null,

	@field:SerializedName("end")
	val end: Double? = null,

	@field:SerializedName("event")
	val event: String? = null
)