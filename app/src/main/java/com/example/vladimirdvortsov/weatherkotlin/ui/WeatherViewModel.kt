package com.example.vladimirdvortsov.weatherkotlin.ui

import androidx.lifecycle.MutableLiveData
import com.example.vladimirdvortsov.weatherkotlin.WeatherRepository
import com.example.vladimirdvortsov.weatherkotlin.base.BaseViewModel
import com.example.vladimirdvortsov.weatherkotlin.model.Weather

class WeatherViewModel(
    private val weatherRepository: WeatherRepository
) : BaseViewModel() {

    val weatherLD = MutableLiveData<Weather>()

    val errorLD = MutableLiveData<Throwable>()

    init {
        weatherLD.value = null
    }

    fun getWeatherByCity(city: String) {
        disposable {
            weatherRepository.getWeatherByCity(city).doOnError {
                weatherLD.postValue(null)
            }.subscribe({
                weatherLD.postValue(it)
            }, {
                errorLD.postValue(it)
            })
        }
    }

    fun getWeatherByCoord() {
        disposable {
            weatherRepository.getWeatherByCoord()
                .subscribe({
                    weatherLD.postValue(it)
                }, {
                    errorLD.postValue(it)
                })
        }
    }

}