package com.san.simpletaxiapp.model

import java.io.Serializable

data class NearByDrivers (
    var distance: Double,
    var driver: ListOfDrivers
) : Serializable