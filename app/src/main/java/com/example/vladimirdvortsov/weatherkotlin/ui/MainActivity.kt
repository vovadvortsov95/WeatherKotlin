package com.example.vladimirdvortsov.weatherkotlin.ui

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.example.vladimirdvortsov.weatherkotlin.R
import com.example.vladimirdvortsov.weatherkotlin.model.Weather



class MainActivity : AppCompatActivity() {

    // TODO : LiveData with Weather .
    // TODO : update view with bind . viewModel.bind(weatherLD)
    // mutable live data в ней сделать подписку на апи .  А при обновлении лайв даты апдейтить вью

    private lateinit var context: Context

    private lateinit var city: TextView
    private lateinit var humidity: TextView
    private lateinit var wind: TextView
    private lateinit var pressure: TextView
    private lateinit var temp: TextView
    private lateinit var weatherType: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = applicationContext
   //     val weather = Weather("main",0.0,0,0,"Description","04h",3,7,"Omsk",200)
   //    bindView(weather)
        WeatherView(application).weatherLD.observe(this, Observer<Weather>{
            if ( it != null) {
                bindView(it)
            }
        })

    }


    private fun bindView(weather: Weather) {
        initView()
        val windInfo = weather.deg.toString() + " , " + weather.speed.toString() + " m/s"
        city.text = weather.city
        humidity.text = weather.humidity.toString()
        wind.text = windInfo
        pressure.text = weather.pressure.toString()
        temp.text = weather.temp.toString()
        weatherType.text = weather.main
    }

    private fun initView() {
        city = findViewById(R.id.city)
        humidity = findViewById(R.id.humidity_percent)
        wind = findViewById(R.id.wind_value)
        pressure = findViewById(R.id.preccure_value)
        temp = findViewById(R.id.temp)
        weatherType = findViewById(R.id.weather_type)
    }
}
