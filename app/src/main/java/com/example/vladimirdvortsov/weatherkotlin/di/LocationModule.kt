package com.example.vladimirdvortsov.weatherkotlin.di

import android.content.Context
import com.example.vladimirdvortsov.weatherkotlin.data.LocationSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val locationModule = module {
    single { makeLocationSource(androidContext()) }
}

private fun makeLocationSource(context: Context): LocationSource {
    return LocationSource(context)
}