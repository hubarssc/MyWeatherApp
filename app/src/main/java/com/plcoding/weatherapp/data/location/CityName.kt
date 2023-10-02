package com.plcoding.weatherapp.data.location

import android.content.Context
import android.location.Geocoder
import java.io.IOException
import java.util.Locale
import java.util.concurrent.Executors

class CityName(private val context: Context) {

    interface CityNameListener {
        fun onCityNameObtained(cityName: String?)
    }

    fun getCityName(latitude: Double, longitude: Double, listener: CityNameListener) {
        val geocoder = Geocoder(context, Locale.getDefault())

        Executors.newSingleThreadExecutor().execute {
            try {
                geocoder.run {
                    getFromLocation(
                        latitude,
                        longitude,
                        1
                    ) { addresses ->
                        if (addresses.isNotEmpty()) {
                            val address = addresses[0]
                            val city = address.locality
                            val state = address.adminArea
                            val country = address.countryName
                            val cityName = "City: $city\n"

                            listener.onCityNameObtained(cityName)
                        } else {
                            listener.onCityNameObtained(null)
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                listener.onCityNameObtained(null)
            }
        }
    }
}
