package com.example.vladimirdvortsov.weatherkotlin

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.support.v4.content.PermissionChecker
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.vladimirdvortsov.weatherkotlin.api.WeatherClient
import com.example.vladimirdvortsov.weatherkotlin.model.Weather
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class WeatherRepository(context: Context) {
    // weatherLD Here?
    //TODO : GET PROVIDERS WITH CRITERIA
    //getWeatherByCoordinates

    fun getWeatherByCoord(context: Context): Observable<Weather>? {

        return getLocation(context).flatMap {
            println("it.latitude ${it.latitude} , it.longitude ${it.longitude}")
            return@flatMap WeatherClient.create().getWeatherByCoord(it.latitude, it.longitude).subscribeOn(Schedulers.io())
        }
    }

    fun getWeatherByName(city: String): Observable<Weather> {
        return WeatherClient.create().getWeatherByCityName(city).subscribeOn(Schedulers.io())
    }

    fun getWeatherById(id: Int): Observable<Weather> {
        return WeatherClient.create().getWeatherByCityId(id).subscribeOn(Schedulers.io())
    }



    private fun getLocation(context: Context): Observable<Location> {
        val manager: LocationManager = context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
            return Observable.create<Location> { emitter ->
                if(checkPermission(context))
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, object : LocationListener {
                    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
                        Log.d("onStatusChanged", p0)
                    }

                    override fun onProviderEnabled(p0: String?) {
                        Log.d("onProviderEnabled", p0)
                    }

                    override fun onLocationChanged(location: Location?) {
                        Log.d("onLocationChanged!", location?.toString())
                        if (location != null && location.longitude != 0.0) {
                            manager.removeUpdates(this)
                            emitter.onNext(location)
                            emitter.onComplete()
                        }
                    }

                    override fun onProviderDisabled(p0: String?) {
                        emitter.onError(Throwable("onProviderDisabled"))
                    }
                }, Looper.getMainLooper())
            }
        }

    private fun checkPermission(context: Context): Boolean {
        return !(PermissionChecker.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && PermissionChecker.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    }
}