package com.example.vladimirdvortsov.weatherkotlin.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.vladimirdvortsov.weatherkotlin.R
import com.example.vladimirdvortsov.weatherkotlin.model.Weather


class MainActivity : AppCompatActivity() {

    // TODO : LiveData with Weather .
    // TODO : update view with bind . viewModel.bind(weatherLD)
    // mutable live data в ней сделать подписку на апи .  А при обновлении лайв даты апдейтить вью

    private val viewModel by lazy { ViewModelProviders.of(this).get(WeatherView::class.java)}

    private lateinit var city: TextView
    private lateinit var cityEdit : TextView
    private lateinit var humidity: TextView
    private lateinit var wind: TextView
    private lateinit var pressure: TextView
    private lateinit var temp: TextView
    private lateinit var weatherType: TextView
    private lateinit var locationImage : ImageView

    private lateinit var context : Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //     val weather = Weather("main",0.0,0,0,"Description","04h",3,7,"Omsk",200)
        //    bindView(weather)
        context = applicationContext

        initView(context)
        //viewModel.getWeatherByCity("Omsk")
        //viewModel.getWeatherByCoord(context)
        viewModel.weatherLD.observe( this, Observer<Weather>{
            if (it != null) {
                Log.d("it "," != null")
                bindView(it)
            }
            else {
                Log.d("it", "= null")
            }

        })
//        WeatherView(application).getWeatherByCity("Omsk")
    }


    private fun bindView(weather: Weather) {
        val windInfo = weather.deg.toString() + " , " + weather.speed.toString() + " m/s"
        city.text = weather.city
        humidity.text = weather.humidity.toString()
        wind.text = windInfo
        pressure.text = weather.pressure.toString()
        temp.text = weather.temp.toString()
        weatherType.text = weather.main
    }

    private fun initView(context: Context) {
        locationImage = findViewById(R.id.geolocation)
        city = findViewById(R.id.city)
        cityEdit = findViewById(R.id.change_city)
        humidity = findViewById(R.id.humidity_percent)
        wind = findViewById(R.id.wind_value)
        pressure = findViewById(R.id.preccure_value)
        temp = findViewById(R.id.temp)
        weatherType = findViewById(R.id.weather_type)
        city.isFocusable = false
        city.isClickable = true
//        cityEdit.setOnClickListener{
//            city.edita
//        }
        locationImage.setOnClickListener {
            viewModel.getWeatherByCoord(context)
        }
    }
}
