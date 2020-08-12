package com.example.vladimirdvortsov.weatherkotlin.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.PermissionChecker
import com.example.vladimirdvortsov.weatherkotlin.WeatherRepository
import com.example.vladimirdvortsov.weatherkotlin.model.Weather

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val weatherRepository = WeatherRepository()
    val weatherLD = MutableLiveData<Weather>()
    init {
        weatherLD.value = null
    }

    @SuppressLint("CheckResult")
    fun getWeatherByCity(city: String) {
        weatherRepository.getWeatherByCity(city).doOnError {
            weatherLD.postValue(null)
        }.subscribe {
            weatherLD.postValue(it)
        }
    }

    @SuppressLint("CheckResult")
    fun getWeatherByCoord(context: Context) {
        if (isPermissionGranted()) {
            weatherRepository.getWeatherByCoord(context).doOnError {
                weatherLD.postValue(null)
            }?.subscribe {
                weatherLD.postValue(it)
            }
        }
    }

    private fun isPermissionGranted(): Boolean {
        return !(PermissionChecker.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && PermissionChecker.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    }
}