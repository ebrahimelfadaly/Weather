package com.example.weatherapplication.view.Favorite

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.R
import com.example.weatherapplication.model.favorite.FavoriteEntity
import com.example.weatherapplication.view.home.HomeActivity
import com.example.weatherapplication.view.viewModel.FavViewModel
import kotlinx.android.synthetic.main.activity_home.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class FavoriteItemAdapter (var favoriteData: ArrayList<FavoriteEntity>): RecyclerView.Adapter<FavoriteItemAdapter.FavoriteViewHolder>() {
    lateinit var context: Context
    lateinit var viewModel: FavViewModel

    fun updateList(newList: List<FavoriteEntity>, viewModel: FavViewModel) {
        this.viewModel = viewModel

        favoriteData.clear()
        favoriteData.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        context = parent.context
        return FavoriteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.favorite_item_row, parent, false))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favoriteData[position], this)
    }

    override fun getItemCount(): Int {
        return favoriteData.size
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val favoriteTitleLbl = view.findViewById<TextView>(R.id.favoriteTitleLbl)
        private val deleteButton = view.findViewById<Button>(R.id.FavDeleteButton)
        private val image =view.findViewById<ImageView>(R.id.img_fav_row)


        @SuppressLint("SetTextI18n")
        fun bind(favorite: FavoriteEntity, adapter: FavoriteItemAdapter) {
            var citya=""
            var country=""
            val gcd= Geocoder(adapter.context, Locale.getDefault())
            var address:List<Address>?
            try {
                address=gcd.getFromLocation(favorite.lat,favorite.lon,1)
                citya= address[0].locality?.toString().toString()
                country=address[0].countryName

            }catch (e: IOException){
                e.printStackTrace()
            }







            favoriteTitleLbl.text = citya +"/n "+country

            deleteButton.setOnClickListener {
                val builder = AlertDialog.Builder(adapter.context)
                builder.setMessage("Are you sure you want to delete this location?")

                builder.setPositiveButton("Yes") { _, _ ->
                    adapter.viewModel.delete(favorite)
                    adapter.favoriteData.remove(favorite)
                    adapter.notifyDataSetChanged()
                }

                builder.setNegativeButton("No", null)
                builder.show()
            }

            itemView.setOnClickListener {
                val intent = Intent(adapter.context, HomeActivity::class.java)
                intent.putExtra("source", "favorite")
                intent.putExtra("lat", favorite.lat)
                intent.putExtra("lon", favorite.lon)
                adapter.context.startActivity(intent)
            }
        }
    }
}