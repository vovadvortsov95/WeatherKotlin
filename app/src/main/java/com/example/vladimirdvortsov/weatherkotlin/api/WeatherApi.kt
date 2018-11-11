package com.example.vladimirdvortsov.weatherkotlin.api

import com.example.vladimirdvortsov.weatherkotlin.Constant
import com.example.vladimirdvortsov.weatherkotlin.model.Weather
import io.reactivex.Observable
import io.reactivex.Observer
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//import rx.Observer

interface WeatherApi {

// what is Q
// http://api.openweathermap.org/data/2.5/      weather?q=Omsk&APPID=dff4ca3d35f3a61fef6f6e2df069347a

    @GET("weather?id={cityId}") // city id
    fun getWeatherByCityId(@Path("cityId") cityId : Int,@Query("&APPID=") apiKey : String = Constant.apiKey) : Observable<Weather>

    //api.openweathermap.org/data/2.5/weather?id=2172797
     @GET("weather?id={city}") // city id
     fun getWeatherByCityName(@Path("city") city : String,
                              @Query("APPID") apikey: String = Constant.apiKey) : Observable<Weather>

//http://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&APPID=dff4ca3d35f3a61fef6f6e2df069347a
     @GET("weather?{lat}{lon}") // city id
    fun getWeatherByCoord(@Query("lat=") @Path("lon") lat : Double,
                          @Query("&lon") @Path("lon") lon : Double,
                          @Query("&APPID=") apiKey : String = Constant.apiKey) : Observable<Weather>

}