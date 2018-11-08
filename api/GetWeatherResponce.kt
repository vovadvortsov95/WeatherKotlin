package com.example.user.weatherkotlin.api

import com.example.user.weatherkotlin.Constant
import com.example.user.weatherkotlin.model.Weather
import com.example.user.weatherkotlin.util.WeatherDeserializer
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetWeatherResponce {


    companion object {
        val gson = GsonBuilder().registerTypeAdapter(Weather::class.java, WeatherDeserializer())

        fun create(): WeatherApi {
            val retrofit =
                Retrofit.Builder()
                    .baseUrl(Constant.weatherUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(WeatherApi::class.java)
        }
    }

}