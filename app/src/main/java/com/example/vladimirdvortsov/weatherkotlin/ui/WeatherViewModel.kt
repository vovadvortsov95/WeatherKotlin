package com.example.vladimirdvortsov.weatherkotlin.ui

import android.Manifest
import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.PermissionChecker
import androidx.lifecycle.ViewModel
import com.example.vladimirdvortsov.weatherkotlin.WeatherRepository
import com.example.vladimirdvortsov.weatherkotlin.model.Weather

class WeatherViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    val weatherLD = MutableLiveData<Weather>()
    init {
        weatherLD.value = null
    }

    fun getWeatherByCity(city: String) {
        weatherRepository.getWeatherByCity(city).doOnError {
            weatherLD.postValue(null)
        }.subscribe {
            weatherLD.postValue(it)
        }
    }

    fun getWeatherByCoord() {
            weatherRepository.getWeatherByCoord().doOnError {
                weatherLD.postValue(null)
            }?.subscribe {
                weatherLD.postValue(it)
            }
    }

}