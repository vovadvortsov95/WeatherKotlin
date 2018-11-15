package com.example.vladimirdvortsov.weatherkotlin.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import com.example.vladimirdvortsov.weatherkotlin.Constant
import com.example.vladimirdvortsov.weatherkotlin.R
import com.example.vladimirdvortsov.weatherkotlin.model.Weather
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*


@SuppressLint("WrongViewCast")
class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(WeatherViewModel::class.java) }
    private val city: TextView by lazy { return@lazy findViewById<TextView>(R.id.city) }
    private var isCelsia : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView(applicationContext)
    }

    private fun bindView(weather: Weather?) {
        if (weather != null) {
            val windInfo = weather.deg.toString() + " , " + weather.speed.toString() + " m/s"
            city.text = weather.city
            humidity.text = weather.humidity.toString()
            wind.text = windInfo
            preccure.text = weather.pressure.toString()
            weather_type.text = weather.main
            setWeatherType(weather)

            Picasso.get().load(Constant.imageUrl + weather.icon + ".png").resize(250, 250).into(weather_icon)
        }
    }

    private fun setWeatherType(weather: Weather) {
        if (isCelsia) {
            val newTemp = (weather.temp - 273.15).toString()
            metric_type.text = " °C"
            temp.text = newTemp
        } else {
            val newTemp = weather.temp.toString()
            metric_type.text = " °F"
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

        change_city.setOnClickListener {
            city.isFocusableInTouchMode = true
            city.requestFocus()
            showKeyboard()
        }

        celsia.setOnClickListener {
            if (!temp.text.isNullOrEmpty()){
                if (isCelsia) {
                    val newTemp = (temp.text.toString().toDouble() - 273.15).toString()
                    metric_type.text = " °F"
                    temp.text = newTemp
                    isCelsia = false
                    celsia.setBackgroundColor(this.resources.getColor(R.color.colorGrey))
                    fahrenheit.setBackgroundColor(this.resources.getColor(R.color.cardBackground))
                }
                else{
                    val newTemp = (temp.text.toString().toDouble() + 273.15).toString()
                    metric_type.text = " °C"
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
                    metric_type.text = " °F"
                    isCelsia = false
                    celsia.setBackgroundColor(this.resources.getColor(R.color.colorGrey))
                    fahrenheit.setBackgroundColor(this.resources.getColor(R.color.cardBackground))
                }
                else{
                    temp.text = (temp.text.toString().toDouble() + 273.15).toString()
                    metric_type.text = " °C"
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

        geolocation.setOnClickListener {
            viewModel.getWeatherByCoord(context)
        }
    }
}