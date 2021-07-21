package com.san.simpletaxiapp.utils

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.LocationManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.san.simpletaxiapp.model.ListOfDrivers
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


var PERMISSION_REQUEST = 101

var USER = "USER"
var CASH_CAPS = "CASH"
var CASH = "Cash"
var CARD = "Card"

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

class Utils {

    companion object {

        fun listOfDriversData() : ArrayList<ListOfDrivers> {
            val driver: ArrayList<ListOfDrivers> = ArrayList()
            driver.add(ListOfDrivers("Cherry Hall", 11.023945,77.0018713))
            driver.add(ListOfDrivers("Audrey Robinson", 8.037244,80.0360853))
            driver.add(ListOfDrivers("Wilson Alexander", 8.038965,80.0362893))

            return driver
        }

        fun distanceInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val theta = lon1 - lon2
            var dist = sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * cos(
                deg2rad(theta)
            )
            dist = acos(dist)
            dist = rad2deg(dist)
            dist *= 60 * 1.1515
            dist *= 1.609344
            return dist
        }

        private fun deg2rad(deg: Double): Double {
            return deg * Math.PI / 180.0
        }

        private fun rad2deg(rad: Double): Double {
            return rad * 180.0 / Math.PI
        }

        fun checkPermission(context: Context) : Boolean {
            var checkFineLocationPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            var checkCoarsePermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
            var result = false

            if(checkFineLocationPermission == 0 && checkCoarsePermission == 0) {
                result = true
            }

            return result
        }

        fun isLocationEnabled(context: Context): Boolean {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return LocationManagerCompat.isLocationEnabled(locationManager)
        }

    }

}

