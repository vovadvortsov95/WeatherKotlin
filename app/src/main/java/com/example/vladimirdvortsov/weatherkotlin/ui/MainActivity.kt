package com.example.vladimirdvortsov.weatherkotlin.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import com.example.vladimirdvortsov.weatherkotlin.Constant
import com.example.vladimirdvortsov.weatherkotlin.R
import com.example.vladimirdvortsov.weatherkotlin.model.Weather
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*


@SuppressLint("WrongViewCast")
class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(WeatherViewModel::class.java) }
    private var isCelsia : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView(applicationContext)
    }

    private fun bindView(weather: Weather?) {
        if (weather != null) {
            city.text = SpannableStringBuilder(weather.city)
            humidity_value.text = weather.humidity.toString()
            wind_value.text = (weather.deg.toString() + " , " + weather.speed.toString() + " m/s")
            pressure_value.text = weather.pressure.toString()
            weather_type.text = weather.main
            setWeatherType(weather)

            Picasso.get().load(Constant.imageUrl + weather.icon + ".png").resize(250, 250).into(weather_icon)
        }
    }

    private fun setWeatherType(weather: Weather) {
        if (isCelsia) {
            metric_type.text = " °C"
            temp.text = (weather.temp - 273).toInt().toString()
        } else {
            metric_type.text = " °F"
            temp.text = weather.temp.toString()
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
                    metric_type.text = " °F"
                    temp.text = (temp.text.toString().toDouble() - 273).toString()
                    isCelsia = false
                    //celsia.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGrey))
                    //fahrenheit.setBackgroundColor(ContextCompat.getColor(context, R.color.cardBackground))
                    celsia.resources.getDrawable(R.drawable.left_grey)
                    fahrenheit.resources.getDrawable(R.drawable.right_white)
                }
                else{
                    metric_type.text = " °C"
                    temp.text = (temp.text.toString().toDouble() + 273).toString()
                    isCelsia = true
//                    celsia.setBackgroundColor(ContextCompat.getColor(context, R.color.cardBackground))
//                    fahrenheit.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGrey))
                    celsia.resources.getDrawable(R.drawable.left_white)
                    fahrenheit.resources.getDrawable(R.drawable.right_grey)
                }

            }
        }

        fahrenheit.setOnClickListener {
            if (!temp.text.isNullOrEmpty()){
                if (isCelsia) {
                    temp.text = (temp.text.toString().toDouble() - 273).toString()
                    metric_type.text = " °F"
                    isCelsia = false
//                    celsia.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGrey))
//                    fahrenheit.setBackgroundColor(ContextCompat.getColor(context, R.color.cardBackground))
                    celsia.resources.getDrawable(R.drawable.left_grey)
                    fahrenheit.resources.getDrawable(R.drawable.right_white)
                }
                else{
                    temp.text = (temp.text.toString().toDouble() + 273).toString()
                    metric_type.text = " °C"
                    isCelsia = true
//                    celsia.setBackgroundColor(ContextCompat.getColor(context, R.color.cardBackground))
//                    fahrenheit.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGrey))
                    celsia.resources.getDrawable(R.drawable.left_grey)
                    fahrenheit.resources.getDrawable(R.drawable.right_white)
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
                        city.text = SpannableStringBuilder(city.text.trim('\n',' '))
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