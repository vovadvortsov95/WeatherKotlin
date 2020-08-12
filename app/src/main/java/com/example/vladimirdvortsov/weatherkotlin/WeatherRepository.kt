package com.example.vladimirdvortsov.weatherkotlin

import android.location.Location
import com.example.vladimirdvortsov.weatherkotlin.api.WeatherClient
import com.example.vladimirdvortsov.weatherkotlin.data.LocationSource
import com.example.vladimirdvortsov.weatherkotlin.model.Weather
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject

class WeatherRepository : KoinComponent {

    private val weatherClient : WeatherClient by inject()

    private val locationSource: LocationSource by inject()

    fun getWeatherByCoord(): Observable<Weather> {
        return getLocation()
            .flatMapSingle { location ->
                return@flatMapSingle weatherClient.create()
                    .getWeatherByCoord(location.latitude, location.longitude)
                    .retry(5)
                    .subscribeOn(Schedulers.io())
            }
    }

    fun getWeatherByCity(city: String): Single<Weather> {
        return weatherClient.create()
            .getWeatherByCityName(city)
            .subscribeOn(Schedulers.io())
    }

    private fun getLocation(): Observable<Location> {
        return locationSource.observeLocation()
    }

}