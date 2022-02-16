package com.example.weatherapp.model.localDataSource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.model.entities.WeatherResponse

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDefault(weatherResponse: WeatherResponse?)

    @Query("SELECT * from WeatherResponse")
    fun getAllData():LiveData<List<WeatherResponse>>

    @Query("DELETE from WeatherResponse WHERE lon = :lon AND lat= :lat")
    fun deleteDefault(lat:String,lon:String)



    @Query("SELECT * from WeatherResponse WHERE lon != :lon AND lat != :lat")
    fun getFavList(lat:String,lon:String):LiveData<List<WeatherResponse>>

    @Query("SELECT * from WeatherResponse WHERE lon = :lon AND lat = :lat")
    fun getCurrentForBroadCast(lat:String,lon:String):WeatherResponse

    @Query("SELECT * from WeatherResponse")
    fun getFavDataForRefresh():List<WeatherResponse>


}