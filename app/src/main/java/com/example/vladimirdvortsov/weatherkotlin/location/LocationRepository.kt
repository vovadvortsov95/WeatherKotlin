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

class LocationRepository(context: Context) {
    // weatherLD Here?
    //TODO : GET PROVIDERS WITH CRITERIA
    //getWeatherByCoordinates
    @SuppressLint("MissingPermission")
    fun getWeather(context : Context): Observable<Weather>? {

       return getLocation(context).flatMap {
            return@flatMap GetWeatherResponce.create().getWeatherByCoord(it.longitude, it.latitude)
        }
    }



    @SuppressLint("MissingPermission")
    private fun getLocation(context: Context): Observable<Location> {
        val manager: LocationManager = context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        return Observable.create<Location> { emitter ->

            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, object : LocationListener {
                override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
                    Log.d("onStatusChanged",p0)
                }

                override fun onProviderEnabled(p0: String?) {
                    Log.d("onProviderEnabled",p0)
                }

                override fun onLocationChanged(location: Location?) {
                    if( location != null && location.longitude != 0.0) {
                        manager.removeUpdates(this)
                        emitter.onNext(location)
                        emitter.onComplete()
                    }
                }

                override fun onProviderDisabled(p0: String?) {
                    emitter.onError(Throwable("xui"))
                }
            }, Looper.getMainLooper())
        }
    }

}