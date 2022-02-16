package com.example.kotlinapplication.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.example.weatherapp.model.entities.Hourly
import com.example.weatherapplication.R
import com.example.weatherapplication.view.viewModel.HomeViewModel

import kotlinx.android.synthetic.main.daily_row.view.*
import kotlinx.android.synthetic.main.hourly_row.view.*
import java.text.SimpleDateFormat
import java.util.*

class HourlyListAdapter(var hoursList: ArrayList<Hourly>,val viewModel: HomeViewModel) : RecyclerView.Adapter<HourlyListAdapter.HourlyViewHolder>(){


    fun updateHoursList(newCountries: List<Hourly>) {
        hoursList.clear()
        hoursList.addAll(newCountries)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = HourlyViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.hourly_row, parent, false)
    )


    override fun getItemCount(): Int {
        return hoursList.size
    }

    override fun onBindViewHolder(holder: HourlyListAdapter.HourlyViewHolder, position: Int) {
        holder.itemView.hourly_icon.setAnimation(viewModel.getIcon(hoursList[position]?.weather?.get(0)?.icon!!))
        holder.bind(hoursList[position])
    }

    class HourlyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val hourlyTime=itemView.hourly_time
        private val hourlyIcon=itemView.hourly_icon
        private val hourlyTemp=itemView.hourly_temp
//        private val hourlyPrec=itemView.hourly_prec

        fun bind(hourly:Hourly){
            val calendar = Calendar.getInstance()
            val tz = TimeZone.getDefault()
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.timeInMillis))
            val sdf = SimpleDateFormat("HH:mm a", Locale.getDefault())
            val currenTimeZone = (hourly.dt?.toLong())?.times(1000)?.let { it1 -> Date(it1) }
            hourlyTime.text= sdf.format(currenTimeZone)

            hourlyTemp.text=(hourly.temp)?.toInt().toString()+"°"
            //   hourlyPrec.text=hourly.
//            val icon=hourly.weather?.get(0)?.icon
//            val options = RequestOptions()
//                .error(R.mipmap.ic_launcher_round)
//            Glide.with(hourlyIcon.context)
//                .setDefaultRequestOptions(options)
//                .load(icon?.let { it2 -> getIcon(it2) })
//                .into(hourlyIcon)
        }
        fun getIcon(icon: String): String {
            // https://openweathermap.org/img/wn/${icon}.png
            return "https://openweathermap.org/img/wn/${icon}.png"

        }

    }
}