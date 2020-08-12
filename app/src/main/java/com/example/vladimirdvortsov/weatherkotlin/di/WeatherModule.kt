package com.example.vladimirdvortsov.weatherkotlin.di

import com.example.vladimirdvortsov.weatherkotlin.WeatherRepository
import com.example.vladimirdvortsov.weatherkotlin.api.WeatherClient
import com.example.vladimirdvortsov.weatherkotlin.ui.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val weatherModule = module {
    viewModel { WeatherViewModel(get()) }
    single { makeWeatherRepository() }
    single { makeClient() }
}

fun makeWeatherRepository(): WeatherRepository {
    return WeatherRepository()
}

fun makeClient() : WeatherClient {
    return WeatherClient()
}