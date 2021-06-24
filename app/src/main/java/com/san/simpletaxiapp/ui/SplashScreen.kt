package com.san.simpletaxiapp.ui

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.san.simpletaxiapp.R
import com.san.simpletaxiapp.databinding.ActivitySplashScreenBinding
import com.san.simpletaxiapp.utils.PERMISSION_REQUEST
import com.san.simpletaxiapp.utils.Utils

class SplashScreen : AppCompatActivity() {

    private lateinit var splashScreenBinding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        splashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(splashScreenBinding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            checkPermissionInfo()
        }, 2000)

    }

    private fun checkPermissionInfo() {

        if (Utils.checkPermission(this)) {
            startMapActivity()
        } else {
            requestPermissions()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.size == 2 && permissions.size == 2 && requestCode == PERMISSION_REQUEST) {

            // re checking permission grant status is grant or not then start map activity else open request permission
            if(Utils.checkPermission(this)) {
                startMapActivity()
            } else {
                requestPermissions()
            }

        } else {
            requestPermissions()
        }

    }

    private fun requestPermissions() {
        // requesting permission if permission not granted
        ActivityCompat.requestPermissions(
            this, arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            ), PERMISSION_REQUEST
        )
    }

    private fun startMapActivity() {
        startActivity(Intent(this, MapsActivity::class.java))
        finish()
    }

}