package com.example.vladimirdvortsov.weatherkotlin.api

import com.example.vladimirdvortsov.weatherkotlin.Constant
import com.example.vladimirdvortsov.weatherkotlin.model.Weather
import com.example.vladimirdvortsov.weatherkotlin.util.WeatherDeserializer
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherClient {

    private val gson = GsonBuilder().registerTypeAdapter(Weather::class.java, WeatherDeserializer())

    fun create(): WeatherApi {
        val retrofit =
            Retrofit.Builder()
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(Constant.weatherUrl)
                .addConverterFactory(GsonConverterFactory.create(gson.create()))
                .build()

        return retrofit.create(WeatherApi::class.java)
    }

}