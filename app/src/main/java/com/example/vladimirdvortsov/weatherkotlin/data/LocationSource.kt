package com.example.vladimirdvortsov.weatherkotlin.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class LocationSource(
    private val context: Context
) {

    val manager: LocationManager =
        context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

    private val locationSubject = PublishSubject.create<Location>()


    fun observeLocation(): Observable<Location> {
        return locationSubject.doOnSubscribe {
            requestUpdates()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestUpdates() {
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
                        locationSubject.onNext(location)
                    }
                }

                override fun onProviderDisabled(p0: String?) {
                    locationSubject.onError(LocationException())
                }
            },
            Looper.getMainLooper()
        )
    }

}