package com.example.user.weatherkotlin.api

import com.example.user.weatherkotlin.Constant
import com.example.user.weatherkotlin.model.Weather
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observer

interface WeatherApi {


    @GET("{city}")
    fun getWeather( @Path("city") city : String, apiKey : String = Constant.apiKey) : Observer<Weather>


}