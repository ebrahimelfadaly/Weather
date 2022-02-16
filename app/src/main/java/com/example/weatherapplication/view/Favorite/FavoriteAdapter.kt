package com.example.weatherapplication.view.Favorite

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.model.entities.WeatherResponse


import com.example.weatherapplication.R
import com.example.weatherapplication.databinding.FavoriteItemRowBinding
import com.example.weatherapplication.view.home.HomeActivity
import com.example.weatherapplication.view.viewModel.FavouriteViewModel
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class FavoriteAdapter(val list:ArrayList<WeatherResponse>,var viewModel: FavouriteViewModel):
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var Lang: String
    var address=""


    inner class ViewHolder(val binding: FavoriteItemRowBinding):RecyclerView.ViewHolder(binding.root)
    {
        @SuppressLint("SetTextI18n")
        fun bind(favorite: WeatherResponse, adapter: FavoriteAdapter) {



            itemView.setOnClickListener {
                val intent = Intent(adapter.context, HomeActivity::class.java)
                intent.putExtra("source", "favorite")
                intent.putExtra("lat", favorite.lat)
                intent.putExtra("lon", favorite.lon)
                adapter.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
     context=parent.context
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        Lang=pref.getString("language", "en")!!
      return  ViewHolder(FavoriteItemRowBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(list[position], this)
        holder.binding.FavDeleteButton.setOnClickListener{
            viewModel.showDeletionDialog(
                context,
                list[position].lat.toString(),
                list[position].lon.toString()
            )

        }




//        val gecoderAddress=Geocoder(context, Locale(Lang))
//        try {
//            if (Lang=="ar"){
//                 address=gecoderAddress.getFromLocation(list[0]?.lat,list[position]?.lon,1)[0].countryName?:list[position].timezone.toString()
//
//            }else{
//                address=gecoderAddress.getFromLocation(list[position]?.lat,list[position]?.lon,1)[0].adminArea?:list[position].timezone.toString()
//                address+=",${gecoderAddress.getFromLocation(list[position]?.lat,list[position]?.lon,1)[0].countryName?:list[position].timezone.toString()}"
//
//            }
//
//
//
//        }catch (e:IOException)
//        {
//            e.printStackTrace()
//        }
       holder.binding.favoriteTitleLbl.text = list[position].timezone





    }


    override fun getItemCount()= list.size
    fun UpdateList(incomingList: List<WeatherResponse>) {
        list.clear()
        list.addAll(incomingList)
        notifyDataSetChanged()
    }

}