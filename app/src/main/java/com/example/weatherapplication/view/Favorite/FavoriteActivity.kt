package com.example.weatherapplication.view.Favorite

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.Constant.Final
import com.example.weatherapplication.R
import com.example.weatherapplication.model.favorite.FavoriteEntity
import com.example.weatherapplication.view.Alert.AlertActivity
import com.example.weatherapplication.view.home.HomeActivity
import com.example.weatherapplication.view.setting.SettingsActivity
import com.example.weatherapplication.view.viewModel.FavViewModel
import com.example.weatherapplication.view.viewModel.FavouriteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceSelectionListener
import kotlinx.android.synthetic.main.activity_favorite.*
import java.math.BigDecimal
import java.math.RoundingMode

class FavoriteActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {

    lateinit var favoriteRecyclerView: RecyclerView

    private var favoriteRecyclerViewAdapter = FavoriteItemAdapter(arrayListOf())
    private lateinit var viewModel: FavViewModel

    var drawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

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



        favoriteRecyclerView = findViewById(R.id.FavoriteRecyclerView)
        val btn: FloatingActionButton = findViewById(R.id.addFavFloatingButton)
        btn.setOnClickListener {
            val intent = Intent(this, FavoriteMap::class.java)
            startActivity(intent)
        }

        viewModel = ViewModelProvider(this).get(FavViewModel::class.java)
        observeViewModel(viewModel)

        init()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAll()
    }

    private fun init(){
        favoriteRecyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = favoriteRecyclerViewAdapter
        }

    }

    private fun observeViewModel(viewModel: FavViewModel) {
        viewModel.getFavoriteInfo().observe(this, Observer { updateUI(it) })
        viewModel.getError().observe(this, Observer { showError(it) })
    }

    private fun updateUI(data: List<FavoriteEntity>) {
        favoriteRecyclerViewAdapter.updateList(data,viewModel)
        println(data)
    }

    private fun showError(msg: String) {
        Log.i("call", msg)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Sorry")
        builder.setMessage(msg)

        builder.setPositiveButton("OK") { _, _ ->
            Toast.makeText(
                applicationContext,
                "OK", Toast.LENGTH_SHORT
            ).show()
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
        return true    }
}