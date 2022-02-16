package com.example.weatherapplication.view.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.kotlinapplication.view.HourlyListAdapter
import com.example.weatherapp.Constant.Final
import com.example.weatherapp.model.entities.Daily
import com.example.weatherapp.model.entities.Hourly
import com.example.weatherapp.model.entities.WeatherResponse
import com.example.weatherapplication.R
import com.example.weatherapplication.view.Alert.AlertActivity
import com.example.weatherapplication.view.Favorite.FavoriteActivity
import com.example.weatherapplication.view.setting.SettingsActivity
import com.example.weatherapplication.view.viewModel.HomeViewModel

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{
  lateinit var dailyListAdapter: DailyListAdapter
   lateinit var hourlyListAdapter :HourlyListAdapter
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: com.google.android.gms.location.LocationRequest
    lateinit var viewModel : HomeViewModel
    private lateinit var loading: ProgressBar
    private var source = "home"
    //private val myReceiver = AlertBroadcast()
    var data=null
    private var lat = 0.0F
    private var lon = 0.0F
    private var unit = String()
    private var language = String()


    var drawerLayout: DrawerLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
        loading = findViewById(R.id.loading_view)
        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        val navigationView: NavigationView = findViewById(R.id.
        nav_view)
        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        navigationView.bringToFront()
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout!!.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.nav_home)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)





       dailyListAdapter= DailyListAdapter(arrayListOf(),viewModel)
       hourlyListAdapter= HourlyListAdapter(arrayListOf(),viewModel)

       val recyclerViewForDaily: RecyclerView = findViewById(R.id.recycler_view_daily)
        recyclerViewForDaily.layoutManager = LinearLayoutManager(applicationContext,
            LinearLayoutManager.HORIZONTAL, true)
       recyclerViewForDaily.adapter = dailyListAdapter

      val recyclerViewForHourly: RecyclerView = findViewById(R.id.recycler_view_hourly)
       recyclerViewForHourly.layoutManager = LinearLayoutManager(applicationContext,
            LinearLayoutManager.HORIZONTAL, true)
        recyclerViewForHourly.adapter = hourlyListAdapter






           viewModel.loadAllData().observe(this, androidx.lifecycle.Observer  { data->
               if (data.isNotEmpty()) {
                   showLoading(false)
                   scrollView2.visibility=View.VISIBLE

                   data?.let {
                       data.let { updateMainDetails(it) }
                       data.get(0).daily?.let { it1 -> updateDailyListUI(it1 as List<Daily>) }
                       data.get(0).hourly?.let { list -> updateHourlyListUI(list as List<Hourly>) }
                       data.let { updateDetailsLayout(it) }
                   }
                   backgroundNoData.visibility = View.GONE
                   scrollView2.visibility=View.VISIBLE


               }else
               {
                   scrollView2.visibility=View.GONE
                   showLoading(true)
                   backgroundNoData.visibility = View.VISIBLE


               }
           })

        //viewModel.loadingLiveData.observe(this, androidx.lifecycle.Observer { showLoading(it) })



//        val intentFilter = IntentFilter(Intent.ACTION_TIME_TICK)
//        registerReceiver(myReceiver, intentFilter)
//
//
//        val intent = Intent(this, AlertService::class.java)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            //startForegroundService(intent)
//            // startService(intent)
//        }else{
//            startService(intent)
//        }
        homeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            refreshFav()
            homeRefreshLayout.isRefreshing = false
        })


    }

    private fun refreshFav() {
        var list = viewModel.getUnrefreshedData()
        if(list != null){
            for(element in list){
                viewModel.refreshFavData(element.lat.toString(),element.lon.toString())

            }
        }else{

        }
    }


   private fun updateDailyListUI(it: List<Daily>) {
        dailyListAdapter.updateHoursList(it)
   }
    private fun updateHourlyListUI(it: List<Hourly>) {
        hourlyListAdapter.updateHoursList(it)
   }

    private fun updateDetailsLayout(it: List<WeatherResponse>) {


        data_humidity.text=it.get(0).current?.humidity.toString()+"%"
        data_visibility.text=it.get(0).current?.visibility.toString()
        data_pressure.text=it.get(0).current?.pressure.toString()
        feels_like.text= it.get(0).current?.feels_like.toString()
    }
  //  holder.binding.mainCard.cityNameTV.text = viewModel.getCityName(context,savedLang,list[position].lat,list[position].lon,list[position].timezone!!)


    private fun updateMainDetails(it: List<WeatherResponse>) {
        var citya=""
        var country=""
        val gcd=Geocoder(applicationContext,Locale.getDefault())
        var address:List<Address>?
        try {
           address=gcd.getFromLocation(it.get(0)?.lat,it.get(0).lon,1)
            citya= address[0].locality?.toString().toString()
            country=address[0].countryName

        }catch (e:IOException){
            e.printStackTrace()
        }


        city.text=citya +"/ "+country
        val calendar = Calendar.getInstance()
        val tz = TimeZone.getDefault()
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.timeInMillis))
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm a", Locale.getDefault())
        val currenTimeZone = (it.get(0).current?.dt?.toLong())?.times(1000)?.let { it1 -> Date(it1) }
        date_time.text = sdf.format(currenTimeZone)



               imageView.setAnimation(viewModel.getIcon(it.get(0).current?.weather?.get(0)?.icon!!))

        // first layout
        cloud_desc.text = it.get(0).current?.weather?.get(0)?.description
        max.text = getText(R.string.max).toString() + (it.get(0).daily?.get(0)?.temp?.max)?.toInt().toString()+"°"
        min.text = getText(R.string.min).toString() + (it.get(0).daily?.get(0)?.temp?.min)?.toInt().toString()+"°"
        windSpeed.text = getText(R.string.wind).toString() + (it.get(0).current?.wind_speed?.toInt()).toString()
        clouds.text = getText(R.string.clouds).toString()+ it.get(0).current?.clouds.toString() + "%"
        when (unit) {
            "imperial" ->
            {
                temp.text = it.get(0).current?.temp?.toInt().toString() +"${getText(R.string.fahrenheit)}"
                windSpeed.text = getText(R.string.wind).toString() + (it.get(0).current?.wind_speed?.toInt()).toString() + getText(R.string.ms)
            }
            "metric" ->{
                temp.text = it.get(0).current?.temp?.toInt().toString() +"${getText(R.string.celsius)}"
                windSpeed.text = getText(R.string.wind).toString() + (it.get(0).current?.wind_speed?.toInt()).toString() + getText(R.string.ms)
            }
            else -> temp.text = it.get(0).current?.temp?.toInt().toString() +"${getText(R.string.kelvin)}"
        }

    }

    private fun askPermission() {
        AlertDialog.Builder(this)
            .setTitle("Permission")
            .setMessage("App need permission to display content overlay to ba able to use all its function")
            .setPositiveButton(
                android.R.string.yes
            ) { dialog, which ->
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + this.packageName)
                )
                startActivityForResult(intent, 0)
            }
            .setNegativeButton(android.R.string.no, null)
            .show()
    }

    private fun showLoading(flag: Boolean) {
        Log.i("call", flag.toString())
        if(flag){
            loading.visibility = View.VISIBLE
        }else{
            loading.visibility = View.GONE
        }
    }
    override fun onResume() {
        super.onResume()


        if (intent.getStringExtra("source") != null) {
            source = intent.getStringExtra("source")!!
        }

        val pref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        lat = pref.getFloat("lat", 0.0F)
        lon = pref.getFloat("lon", 0.0F)
        unit = pref.getString("unit", "metric")!!
        language = pref.getString("language", "en")!!
        val currentLocation = pref.getBoolean("currentLocation", true)
        if (source == "home") {
            if (currentLocation) {
               getLatestLocation()
            } else {
                saveCurrentLocationToSharedPref(lat.toString(),lon.toString())
            }
        }else{
            saveCurrentLocationToSharedPref(intent.getDoubleExtra("lat", 0.0).toString(), intent.getDoubleExtra("lon", 0.0).toString())
            viewModel.refreshCurrentLocation(intent.getDoubleExtra("lat", 0.0).toString(), intent.getDoubleExtra("lon", 0.0).toString())

        }

           refreshFav()


    }



    fun saveCurrentLocationToSharedPref(latitude: String,longitude: String){
        val sharedPref = this.getSharedPreferences(Final.SHARED_PREF_CURRENT_LOCATION, MODE_PRIVATE)
        val editor = sharedPref?.edit()

            editor?.putString(Final.OLD_LATITUDE,sharedPref.getString(Final.CURRENT_LATITUDE,"null"))?.apply()
            editor?.putString(Final.OLD_LONGITUDE,sharedPref.getString(Final.CURRENT_LONGITUDE,"null"))?.apply()
            editor?.putString(Final.CURRENT_LATITUDE,latitude)?.apply()
            editor?.putString(Final.CURRENT_LONGITUDE,longitude)?.apply()
            viewModel.refreshCurrentLocation(latitude,longitude)

    }

    override fun onDestroy() {
        super.onDestroy()
      //  unregisterReceiver(myReceiver)
    }


    // drawer
    override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun showErrorDialog(){
        val builder = androidx.appcompat.app.AlertDialog.Builder(applicationContext)
        builder.setTitle(R.string.errorLocation)
        builder.setMessage(R.string.locationMessage)

        builder.setPositiveButton(R.string.getPermission) { _, _ ->
            requsetPermission()
        }
        builder.setNegativeButton(R.string.ignore) { _, _ ->
        }
        builder.show()
    }




    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> startActivity(
                Intent(
                    this,
                    HomeActivity::class.java
                )
            )
            R.id.nav_setting -> startActivity(
                Intent(
                    this,
                    SettingsActivity::class.java
                )
            )
            R.id.nav_fav -> startActivity(
                Intent(
                    this,
                    FavoriteActivity::class.java
                )
            )
            R.id.nav_alert -> startActivity(
                Intent(
                    this,
                    AlertActivity::class.java
                )
            )

        }
        drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }


   private fun checkPermission():Boolean{

        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED)
        {
            return true
        }
        return false
    }

   private fun requsetPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),Final.LOCATION_PERMISSION_CODE)
    }
   private fun isLocationenable():Boolean{
        var locationManager:LocationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Final.LOCATION_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLatestLocation()
            }

        }else{
            showErrorDialog()
        }
    }
    @SuppressLint("MissingPermission")
    private fun getLatestLocation() {
      if (checkPermission()){
         if (isLocationenable())
         {
             val sharedPref = this.getSharedPreferences(Final.SHARED_PREF_CURRENT_LOCATION, MODE_PRIVATE)
             sharedPref.edit().putBoolean("currentLocation", true).apply()
             fusedLocationProviderClient.lastLocation.addOnCompleteListener { task->
                 var location:Location?=task.result
                 if (location==null){
                        getNewLocation()
                 }else{
                val latitude = location.latitude
                  val longitude=location.longitude
                     val pref = PreferenceManager.getDefaultSharedPreferences(applicationContext)

                     val currentLocation = pref.getBoolean(Final.SHARED_PREF_CURRENT_LOCATION,true)
                     if(currentLocation) {
                         saveCurrentLocationToSharedPref(latitude.toString(), longitude.toString())
                     }

                 }
             }

         }
      }else{
          requsetPermission()
      }
    }
   @SuppressLint("MissingPermission")
    private fun getNewLocation(){
        locationRequest=com.google.android.gms.location.LocationRequest()
       locationRequest.priority= com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval=0
       locationRequest.fastestInterval=0
       locationRequest.numUpdates=2

       fusedLocationProviderClient!!.requestLocationUpdates(locationRequest,locationCallback,Looper.getMainLooper())


    }
    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            val lonDecimal = BigDecimal(location.longitude).setScale(4, RoundingMode.HALF_DOWN)
            val latDecimal = BigDecimal(location.latitude).setScale(4, RoundingMode.HALF_DOWN)
            saveCurrentLocationToSharedPref(latDecimal.toString(),lonDecimal.toString())
        }
    }

}