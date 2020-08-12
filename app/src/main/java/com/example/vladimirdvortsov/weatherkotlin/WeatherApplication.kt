package com.example.vladimirdvortsov.weatherkotlin

import android.app.Application
import com.example.vladimirdvortsov.weatherkotlin.di.weatherModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherApplication)
            modules(listOf(weatherModule))
        }
    }

}