package com.example.vladimirdvortsov.weatherkotlin.api

import com.example.vladimirdvortsov.weatherkotlin.Constant
import com.example.vladimirdvortsov.weatherkotlin.model.Weather
import com.example.vladimirdvortsov.weatherkotlin.util.WeatherDeserializer
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherClient {

    companion object {
        val gson = GsonBuilder().registerTypeAdapter(Weather::class.java, WeatherDeserializer())

        fun create(): WeatherApi {
            val retrofit =
                Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(Constant.weatherUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson.create()))
                    .build()

            return retrofit.create(WeatherApi::class.java)
        }
    }

}