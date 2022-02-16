package com.example.weatherapplication.view.setting

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.preference.*
import com.example.weatherapp.Constant.Final
import com.example.weatherapplication.R
import com.example.weatherapplication.view.Alert.AlertActivity
import com.example.weatherapplication.view.Favorite.FavoriteActivity
import com.example.weatherapplication.view.home.HomeActivity
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.navigation.NavigationView
import java.util.*

class SettingsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {


    var drawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        val navigationView: NavigationView = findViewById(R.id.nav_view)
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
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        private lateinit var locationListPreference: ListPreference
        private lateinit var locationEditTextPreference: EditTextPreference
        private lateinit var unitListPreference: ListPreference
        private lateinit var languageListPreference: ListPreference

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_setting, rootKey)
            Places.initialize(requireContext(), Final.MAP_API_KEY)
            val fieldList = listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(requireContext())
            val pref =  PreferenceManager.getDefaultSharedPreferences(requireContext())

            locationListPreference = findPreference("location")!!
            unitListPreference = findPreference("unit")!!
            languageListPreference = findPreference("language")!!
            locationEditTextPreference = findPreference("location_address")!!
            locationEditTextPreference.isVisible = !pref.getBoolean("currentLocation", true)

            locationListPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, value ->
                Log.i("call", value.toString())

                if(value.toString() == "other"){
                    startActivityForResult(intent, 100)
                }else{
                    pref.edit().putBoolean(Final.SHARED_PREF_CURRENT_LOCATION, true).apply()
                    locationEditTextPreference.isVisible = false
                }
                true
            }

            unitListPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, value ->
                Log.i("call", value.toString())
                pref.edit().putString(Final.UNITS, value.toString()).apply()
                true
            }

            languageListPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, value ->
                Log.i("call", value.toString())
                pref.edit().putString(Final.LANGUAGE, value.toString()).apply()
                setLang(value.toString())
                true
            }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == Final.LOCATION_PERMISSION_CODE) {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.let {
                            val place = Autocomplete.getPlaceFromIntent(data)
                            locationEditTextPreference.text = place.address
                            locationEditTextPreference.isVisible = true

                            val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
                            pref.edit().putFloat(Final.CURRENT_LATITUDE, place.latLng!!.latitude.toFloat()).apply()
                            pref.edit().putFloat(Final.CURRENT_LONGITUDE, place.latLng!!.longitude.toFloat()).apply()
                            pref.edit().putBoolean(Final.SHARED_PREF_CURRENT_LOCATION, false).apply()
                        }
                    }
                    AutocompleteActivity.RESULT_ERROR -> {
                        // TODO: Handle the error.
                        data?.let {
                            val status = Autocomplete.getStatusFromIntent(data)
                        }
                    }
                    Activity.RESULT_CANCELED -> {

                    }
                }
                return
            }
            super.onActivityResult(requestCode, resultCode, data)
        }

        private fun setLang(code: String) {
            val locale = Locale(code)
            Locale.setDefault(locale)
            val resources: Resources = requireContext().resources
            val config: Configuration = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)

            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }
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
}
