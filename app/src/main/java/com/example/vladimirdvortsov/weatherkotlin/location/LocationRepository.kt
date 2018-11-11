package com.example.vladimirdvortsov.weatherkotlin.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.vladimirdvortsov.weatherkotlin.api.GetWeatherResponce
import com.example.vladimirdvortsov.weatherkotlin.model.Weather
import io.reactivex.Observable

class LocationRepository constructor(context : Context) {

    var locationManager: LocationManager =
        context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
    lateinit var locationListener: LocationListener

    @SuppressLint("MissingPermission")
    fun getWeather() : Observable<Weather>? {

        return Observable.create<Location>{
            initLocationListener(locationManager)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                0L,
                0f,
                locationListener,
                Looper.getMainLooper())}.flatMap {
            return@flatMap GetWeatherResponce.create().getWeatherByCoord(it.longitude,it.latitude) }//getWeatherByCoords }


    }

  private  fun initLocationListener(locationManager : LocationManager) : Location? {
        val tag = "initLocationListener"
        var location : Location? = null
        locationListener = object : LocationListener {
            override fun onLocationChanged(p0: Location?) {
                Log.d(tag, "onLocationChanged")
                if (p0 != null) {
                    location = p0
                    locationManager.removeUpdates(this)
                } else {
                    Log.d(tag, "p0 null")
                }
            }

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
                Log.d(tag, "onStatusChanged")
            }

            override fun onProviderEnabled(p0: String?) {
                Log.d(tag, "onProviderEnabled")
            }

            // if remove updates we never get update . After turn on loc !
            override fun onProviderDisabled(p0: String?) {
                Log.d(tag, "onProviderDisabled")
            }

        }
        return location
    }

}