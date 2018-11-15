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

    private val viewModel by lazy { ViewModelProviders.of(this).get(WeatherViewModel::class.java) }

    private val city: TextView by lazy { return@lazy findViewById<TextView>(R.id.city) }

    private val cityEdit: TextView by lazy {  findViewById<TextView>(R.id.change_city) }
    private val humidity: TextView by lazy { return@lazy findViewById<TextView>(R.id.humidity_value) }
    private val wind: TextView by lazy { return@lazy findViewById<TextView>(R.id.wind_value) }
    private val pressure: TextView by lazy { return@lazy findViewById<TextView>(R.id.preccure_value) }
    private val temp: TextView by lazy { return@lazy findViewById<TextView>(R.id.temp) }
    private val weatherType: TextView by lazy { return@lazy findViewById<TextView>(R.id.weather_type) }
    private val locationImage: ImageView by lazy { return@lazy findViewById<ImageView>(R.id.geolocation) }
    private val celsia : TextView by lazy { return@lazy findViewById<TextView>(R.id.celsia)}
    private val fahrenheit : TextView by lazy { return@lazy findViewById<TextView>(R.id.fahrenheit)}
    private val tempType : TextView by lazy { return@lazy findViewById<TextView>(R.id.metric_type) }
    private var isCelsia : Boolean = true

    private val weatherIcon: ImageView by lazy { return@lazy findViewById<ImageView>(R.id.weather_icon) }

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = applicationContext
        initView(context)
    }

    private fun bindView(weather: Weather?) {
        if (weather != null) {
            val windInfo = weather.deg.toString() + " , " + weather.speed.toString() + " m/s"
            city.text = weather.city
            humidity.text = weather.humidity.toString()
            wind.text = windInfo
            pressure.text = weather.pressure.toString()
            weatherType.text = weather.main
            setWeatherType(weather)

            Picasso.get().load(Constant.imageUrl + weather.icon + ".png").resize(250, 250).into(weatherIcon)
        }
    }

    private fun setWeatherType(weather: Weather) {
        if (isCelsia) {
            val newTemp = (weather.temp - 273.15).toString()
            tempType.text = " °C"
            temp.text = newTemp
        } else {
            val newTemp = weather.temp.toString()
            tempType.text = " °F"
            temp.text = newTemp
        }
    }


    private fun initView(context: Context) {
        viewModel.weatherLD.observe(this, Observer<Weather> {
            if (it != null) {
                bindView(it)
            }
        })

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        fun showKeyboard() {
            imm.toggleSoftInput(0, 0)
        }
        city.isFocusable = false

        cityEdit.setOnClickListener {
            city.isFocusableInTouchMode = true
            city.requestFocus()
            showKeyboard()
        }

        celsia.setOnClickListener {
            if (!temp.text.isNullOrEmpty()){
                if (isCelsia) {
                    val newTemp = (temp.text.toString().toDouble() - 273.15).toString()
                    tempType.text = " °F"
                    temp.text = newTemp
                    isCelsia = false
                    celsia.setBackgroundColor(this.resources.getColor(R.color.colorGrey))
                    fahrenheit.setBackgroundColor(this.resources.getColor(R.color.cardBackground))
                }
                else{
                    val newTemp = (temp.text.toString().toDouble() + 273.15).toString()
                    tempType.text = " °C"
                    temp.text = newTemp
                    isCelsia = true
                    celsia.setBackgroundColor(this.resources.getColor(R.color.cardBackground))
                    fahrenheit.setBackgroundColor(this.resources.getColor(R.color.colorGrey))
                }

            }
        }

        fahrenheit.setOnClickListener {
            if (!temp.text.isNullOrEmpty()){
                if (isCelsia) {
                    temp.text = (temp.text.toString().toDouble() - 273.15).toString()
                    tempType.text = " °F"
                    isCelsia = false
                    celsia.setBackgroundColor(this.resources.getColor(R.color.colorGrey))
                    fahrenheit.setBackgroundColor(this.resources.getColor(R.color.cardBackground))
                }
                else{
                    temp.text = (temp.text.toString().toDouble() + 273.15).toString()
                    tempType.text = " °C"
                    isCelsia = true
                    celsia.setBackgroundColor(this.resources.getColor(R.color.cardBackground))
                    fahrenheit.setBackgroundColor(this.resources.getColor(R.color.colorGrey))
                }
            }
        }

        city.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null)
                    if (s.contains("\n")) {
                        city.isFocusableInTouchMode = false
                        city.clearFocus()
                        city.text = city.text.trim('\n',' ')
                        viewModel.getWeatherByCity(s.toString())
                        showKeyboard()
                    }
            }
        })

        locationImage.setOnClickListener {
            viewModel.getWeatherByCoord(context)
        }
    }
}