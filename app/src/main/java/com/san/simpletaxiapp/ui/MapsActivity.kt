package com.san.simpletaxiapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.san.simpletaxiapp.R
import com.san.simpletaxiapp.databinding.ActivityMapsBinding
import com.san.simpletaxiapp.model.NearByDrivers
import com.san.simpletaxiapp.utils.PERMISSION_REQUEST
import com.san.simpletaxiapp.utils.USER
import com.san.simpletaxiapp.utils.Utils
import com.san.simpletaxiapp.utils.showToast
import com.san.simpletaxiapp.viewmodels.MyViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val viewModel: MyViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        viewModel.listOfDrivers()

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        var listOfNearByDrivers: ArrayList<NearByDrivers> = ArrayList()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // requesting permission if permission not granted
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ), PERMISSION_REQUEST
            )

            return
        }

        fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                val mLastLocation = task.result

                // add marker for current user location
                mMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            mLastLocation.latitude,
                            mLastLocation.longitude
                        )
                    ).title(USER).icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker))
                )
                animateToCurrentLocation(mMap, LatLng(
                    mLastLocation.latitude,
                    mLastLocation.longitude
                ))


                // observing list of driver arraylist from the viewModel
                viewModel.getListOfDrivers.observe(
                    this, { listOfDriversData ->

                        for (driver in listOfDriversData) {
                            val res: Double = Utils.distanceInKm(
                                mLastLocation.latitude, mLastLocation.longitude, driver.lat, driver.lng
                            )

                            listOfNearByDrivers.add(NearByDrivers(res, driver))

                            val driverLatLng = LatLng(driver.lat, driver.lng)

                            // add marker for list of each drivers
                            mMap.addMarker(MarkerOptions().position(driverLatLng).title(driver.driverName).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_top)))
                        }

                    })

                // filter nearest driver from list of nearByDrivers
                val nearByDriver: NearByDrivers? = listOfNearByDrivers.minByOrNull { it.distance }

                // hide book now when the near by driver location
                if(nearByDriver!!.distance >= 1) {
                    binding.bookNowBtn.visibility = View.GONE
                }

                // book now button action
                binding.bookNowBtn.setOnClickListener {
                    if(Utils.checkPermission(this)) {
                        if (nearByDriver != null) {
                            doBookNow(nearByDriver)
                        } else {
                            showToast(this, "NearByDriver Not Found")
                        }
                    } else {
                        ActivityCompat.requestPermissions(
                            this, arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                            ), PERMISSION_REQUEST
                        )
                    }
                }

            } else {

                if(Utils.isLocationEnabled(this)) {
                    //  if current location null or empty, no current location found
                    showToast(this, "No current location found")
                } else {
                    showToast(this, "Location Service Not Enabled, Turn On and Re-Try")
                }

            }
        }

    }

    // start activity to booking form activity with driver name and distance
    private fun doBookNow(nearByDriver: NearByDrivers) {
        startActivity(Intent(this, BookNowActivity::class.java).apply {
            putExtra("DRIVER", nearByDriver.driver.driverName)
            putExtra("DISTANCE", nearByDriver.distance.toString())
        })

        Log.d("TAG", "doBookNow: ${nearByDriver.distance} $nearByDriver")
    }

    // animate map camera to current location
    private fun animateToCurrentLocation(mGoogleMap: GoogleMap, mPosition: LatLng) {
        try {
            mGoogleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        mPosition
                    , 16.0f
                )
            )
        }catch (e: IOException) {
            // handle error
        }
    }

}