package com.example.vladimirdvortsov.weatherkotlin.api

import com.example.vladimirdvortsov.weatherkotlin.Constant
import com.example.vladimirdvortsov.weatherkotlin.model.Weather
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {

    @GET("weather")
    fun getWeatherByCityName(
        @Query("q") city: String,
        @Query("APPID") apikey: String = Constant.apiKey
    ): Single<Weather>

    @GET("weather")
    fun getWeatherByCoord(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("APPID") apiKey: String = Constant.apiKey
    ): Single<Weather>

}