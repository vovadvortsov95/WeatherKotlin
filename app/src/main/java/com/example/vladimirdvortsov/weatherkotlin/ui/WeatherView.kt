package com.example.vladimirdvortsov.weatherkotlin.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.PermissionChecker
import com.example.vladimirdvortsov.weatherkotlin.location.LocationRepository
import com.example.vladimirdvortsov.weatherkotlin.model.Weather

class WeatherView(application : Application) : AndroidViewModel(application)  {

    val locationRepository = LocationRepository(application.applicationContext)
    val weatherLD = MutableLiveData<Weather>()
    init {
        weatherLD.value = null
    }

    @SuppressLint("CheckResult")
    fun getWeatherByCity( city : String){
        locationRepository.getWeatherByName(city).subscribe{
            weatherLD.postValue(it)
        }
    }

    @SuppressLint("CheckResult")
    fun getWeatherByCoord(context: Context){
        if(isPermissionGranted(context)) {
            locationRepository.getWeatherByCoord(context)?.subscribe {
                weatherLD.postValue(it)
            }
        }
    }

    @SuppressLint("CheckResult")
    fun getWeatherById(id : Int){
        locationRepository.getWeatherById(id).subscribe {
            weatherLD.postValue(it)
        }
    }

    private fun isPermissionGranted(context: Context) : Boolean{
        return !(PermissionChecker.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && PermissionChecker.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    }
}