package com.example.vladimirdvortsov.weatherkotlin.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import com.example.vladimirdvortsov.weatherkotlin.Constant
import com.example.vladimirdvortsov.weatherkotlin.R
import com.example.vladimirdvortsov.weatherkotlin.model.Weather
import com.squareup.picasso.Picasso


@SuppressLint("WrongViewCast")
class MainActivity : AppCompatActivity() {

    // TODO : LiveData with Weather .
    // TODO : update view with bind . viewModel.bind(weatherLD)
    // mutable live data в ней сделать подписку на апи .  А при обновлении лайв даты апдейтить вью

    private val viewModel by lazy { ViewModelProviders.of(this).get(WeatherView::class.java) }

    private val city: TextView by lazy { return@lazy findViewById<TextView>(R.id.city) }

    private val cityEdit: TextView by lazy { return@lazy findViewById<TextView>(R.id.change_city) }
    private val humidity: TextView by lazy { return@lazy findViewById<TextView>(R.id.humidity_value) }
    private val wind: TextView by lazy { return@lazy findViewById<TextView>(R.id.wind_value) }
    private val pressure: TextView by lazy { return@lazy findViewById<TextView>(R.id.preccure_value) }
    private val temp: TextView by lazy { return@lazy findViewById<TextView>(R.id.temp) }
    private val weatherType: TextView by lazy { return@lazy findViewById<TextView>(R.id.weather_type) }
    private val locationImage: ImageView by lazy { return@lazy findViewById<ImageView>(R.id.geolocation) }


    private val weatherIcon: ImageView by lazy { return@lazy findViewById<ImageView>(R.id.weather_icon) }

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = applicationContext
        initView(context)

    }


    private fun bindView(weather: Weather?) {
        Log.d("weather", "bindView")
        if (weather != null) {
            val windInfo = weather.deg.toString() + " , " + weather.speed.toString() + " m/s"
            city.text = weather.city
            humidity.text = weather.humidity.toString()
            wind.text = windInfo
            pressure.text = weather.pressure.toString()
            temp.text = weather.temp.toString()
            weatherType.text = weather.main

            Picasso.get().load(Constant.imageUrl + weather.icon + ".png").resize(250, 250).into(weatherIcon)
        }
    }


    private fun initView(context: Context) {

        viewModel.weatherLD.observe(this, Observer<Weather> {
            if (it != null) {
                Log.d("it ", " != null")
                bindView(it)
            } else {
                Log.d("it", "= null")
            }

        })

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        fun showKeyboard() {
            viewModel.getWeatherByCity("1111hhhh111")
            imm.toggleSoftInput(0, 0)
        }
        city.isFocusable = false // onClick isFocusable = true // TODO : give focus for editText

        cityEdit.setOnClickListener {
            city.isFocusableInTouchMode = true
            city.requestFocus()
            showKeyboard()
        }

        city.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //never contains!
                if (s != null)
                    if (s.contains("\n")) {
                        viewModel.getWeatherByCity(s.toString())
                    }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //never contains!
                if (s != null)
                    if (s.contains("\n")) {
                        Log.d("FOCUS CHANGED", "true")
                        city.isFocusableInTouchMode = false
                        city.clearFocus()
                    }
            }
        })

        locationImage.setOnClickListener {
            viewModel.getWeatherByCoord(context)
        }
    }
}


