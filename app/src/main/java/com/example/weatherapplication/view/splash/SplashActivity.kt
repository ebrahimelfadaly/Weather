package com.example.weatherapplication.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.example.weatherapplication.R
import com.example.weatherapplication.view.home.HomeActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splashTimeOut = 2000
        val intent = Intent(this@SplashActivity, HomeActivity::class.java)
        Handler().postDelayed({
            startActivity(intent)
            finish()
        }, splashTimeOut.toLong())
    }
}