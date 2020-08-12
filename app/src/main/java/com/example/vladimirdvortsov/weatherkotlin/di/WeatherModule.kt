package com.example.vladimirdvortsov.weatherkotlin.di

import android.content.Context
import com.example.vladimirdvortsov.weatherkotlin.WeatherRepository
import com.example.vladimirdvortsov.weatherkotlin.ui.WeatherViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val weatherModule = module {
    viewModel { WeatherViewModel(get()) }
    single { makeWeatherRepository(androidContext()) }
}

fun makeWeatherRepository(context: Context): WeatherRepository {
    return WeatherRepository(context)
}