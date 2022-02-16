package com.example.weatherapp.model.repository

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.Constant.Final
import com.example.weatherapp.model.entities.WeatherResponse
import com.example.weatherapp.model.localDataSource.LocalDataSource
import com.example.weatherapp.model.remoteDataSource.RemoteDataSource
import kotlinx.coroutines.*

class Repository(application: Application) {

 private val remoteDataSource = RemoteDataSource
    private val localDataSource = LocalDataSource.getInstance(application)
    val weatherLiveData = MutableLiveData<WeatherResponse>()


 private val lat = application.getSharedPreferences(Final.SHARED_PREF_CURRENT_LOCATION, Context.MODE_PRIVATE).getString(Final.CURRENT_LATITUDE,"null").toString()
    private val long = application.getSharedPreferences(Final.SHARED_PREF_CURRENT_LOCATION, Context.MODE_PRIVATE).getString(Final.CURRENT_LONGITUDE,"null").toString()
    private val oldLat = application.getSharedPreferences(Final.SHARED_PREF_CURRENT_LOCATION, Context.MODE_PRIVATE).getString(Final.OLD_LATITUDE,"null").toString()
    private val oldLong = application.getSharedPreferences(Final.SHARED_PREF_CURRENT_LOCATION, Context.MODE_PRIVATE).getString(Final.OLD_LONGITUDE,"null").toString()
    private val language = application.getSharedPreferences(Final.SHARED_PREF_SETTINGS, Context.MODE_PRIVATE).getString(Final.LANGUAGE,"en").toString()
    private val units = application.getSharedPreferences(Final.SHARED_PREF_SETTINGS, Context.MODE_PRIVATE).getString(Final.UNITS,"metric").toString()








//BASE_URL=https://api.openweathermap.org/data/2.5/onecall?lat=33.44&lon=-94.04&exclude=minutely&units=metric&lang=en&appid=078283b8f268fabdb678b867bdd5f5d0
//    const val API_KEY="078283b8f268fabdb678b867bdd5f5d0"




    fun loadAllData():LiveData< List<WeatherResponse>> {
        val exceptionHandlerException=CoroutineExceptionHandler { _, t:Throwable ->
            Log.i(Final.LOG_TAG,t.message.toString())
        }
        CoroutineScope(Dispatchers.IO+exceptionHandlerException).launch {

                val response=remoteDataSource.getWeatherService().getAllData( lat,long,Final.EXCLUDE_MINUTELY,units,language,Final.API_KEY)
                Log.i("getdata", "loadAllData: ${response}")
                if (response.isSuccessful){
                    localDataSource.insertDefault(response.body())
                    //localDataSource.deleteDefault(oldLat,oldLong)
                    Log.i(Final.LOG_TAG, "success")
                }

        }
        Log.i(Final.LOG_TAG, "outhere")
        return localDataSource.getAllData()
    }
















    fun fetchFavouriteList(latitude: String, longitude: String): LiveData<List<WeatherResponse>> {
        val exceptionHandlerException = CoroutineExceptionHandler { _, t:Throwable ->
            Log.i(Final.LOG_TAG,t.message.toString()) }
        CoroutineScope(Dispatchers.IO+exceptionHandlerException).launch {

                val response = remoteDataSource.getWeatherService().getAllData(latitude, longitude, Final.EXCLUDE_MINUTELY, units, language,Final.API_KEY)
                if (response.isSuccessful) {
                    localDataSource.insertDefault(response.body())
                    Log.i(Final.LOG_TAG, "success fav")
                }

        }
        Log.i(Final.LOG_TAG, "outhere fav")
        return localDataSource.getFavList(lat,long)
    }

    fun deleteFromFavourite(lat: String, lon: String) {
        CoroutineScope(Dispatchers.IO).launch {
            localDataSource.deleteDefault(lat,lon)
        }
    }


//    fun getAllAlerts(): LiveData<List<AlertModel>> {
//        return localAlarmDatabase.getAllAlarms()
//    }

    fun refreshCurrentLocation(latitude:String=lat,longitude:String=long){
        runBlocking(Dispatchers.IO) {
            launch {
                try{
                    if (lat != null) {
                        val response = remoteDataSource.getWeatherService().getAllData(latitude, longitude, Final.EXCLUDE_MINUTELY, units, language, Final.API_KEY)
                        if (response.isSuccessful) {
                            localDataSource.insertDefault(response.body())
                            localDataSource.deleteDefault(oldLat,oldLong)
                            Log.i(Final.LOG_TAG, "success22")
                        }
                    }
                }catch(e:Exception){
                    Log.i(Final.LOG_TAG,e.message.toString())
                }
            }
        }
        Log.i(Final.LOG_TAG, "outhere22")
    }

    fun deleteAlarmById(id:String) {
        CoroutineScope(Dispatchers.IO).launch {
           // localAlarmDatabase.deleteAlarmById(id)
        }
    }

    fun refreshFavData(lat:String,long:String){
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = remoteDataSource.getWeatherService().getAllData(lat, long, Final.EXCLUDE_MINUTELY, units, language, Final.API_KEY)
                if (response.isSuccessful) {
                    localDataSource.insertDefault(response.body())
                }
            }catch(e:Exception){
                Log.i(Final.LOG_TAG,e.message.toString())
            }
        }
    }

    fun getUnrefreshedData():List<WeatherResponse>?{
        var unrefreshedList:List<WeatherResponse>? = null
        runBlocking(Dispatchers.IO){
            launch {
                unrefreshedList = localDataSource.getFavDataForRefresh()
            }
        }
        return unrefreshedList
    }







}
