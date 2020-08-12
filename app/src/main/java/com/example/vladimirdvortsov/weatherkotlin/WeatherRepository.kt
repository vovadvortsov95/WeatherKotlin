package com.example.vladimirdvortsov.weatherkotlin

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.vladimirdvortsov.weatherkotlin.api.WeatherClient
import com.example.vladimirdvortsov.weatherkotlin.model.Weather
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class WeatherRepository(
    private val context: Context
) {

    fun getWeatherByCoord(): Single<Weather> {
        return getLocation()
            .flatMap { location ->
            return@flatMap WeatherClient.create()
                .getWeatherByCoord(location.latitude, location.longitude)
                .retry(5)
                .subscribeOn(Schedulers.io())
        }
    }

    fun getWeatherByCity(city: String): Single<Weather> {
        return WeatherClient.create()
            .getWeatherByCityName(city)
            .subscribeOn(Schedulers.io())

    }

    @SuppressLint("MissingPermission")
    private fun getLocation(): Single<Location> {
        val manager: LocationManager =
            context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        return Single.create<Location> { emitter ->
                manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0L,
                    0f,
                    object : LocationListener {
                        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
                        }

                        override fun onProviderEnabled(p0: String?) {
                        }

                        override fun onLocationChanged(location: Location?) {
                            if (location != null && location.longitude != 0.0) {
                                manager.removeUpdates(this)
                                emitter.onSuccess(location)
                            }
                        }

                        override fun onProviderDisabled(p0: String?) {
                            emitter.onError(Throwable("onProviderDisabled"))
                        }
                    },
                    Looper.getMainLooper()
                )
        }
    }

}