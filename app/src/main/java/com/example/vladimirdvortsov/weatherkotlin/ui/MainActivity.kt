package com.example.vladimirdvortsov.weatherkotlin.ui

import android.Manifest
import androidx.lifecycle.Observer
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.example.vladimirdvortsov.weatherkotlin.Constant
import com.example.vladimirdvortsov.weatherkotlin.R
import com.example.vladimirdvortsov.weatherkotlin.model.Weather
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by inject()
    private var isCelsia: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView(applicationContext)
    }

    private fun bindView(weather: Weather?) {
        if (weather != null) {
            city.text = SpannableStringBuilder(weather.city)
            humidity_value.text = weather.humidity.toString()
            when (weather.deg) {
                in 0..23 -> wind_value.text = ("north ," + weather.speed.toString() + " m/s")
                in 23..68 -> wind_value.text = ("north-east ," + weather.speed.toString() + " m/s")
                in 68..113 -> wind_value.text = ("east ," + weather.speed.toString() + " m/s")
                in 113..158 -> wind_value.text =
                    ("south-east ," + weather.speed.toString() + " m/s")
                in 158..203 -> wind_value.text = ("south ," + weather.speed.toString() + " m/s")
                in 203..248 -> wind_value.text =
                    ("south-west ," + weather.speed.toString() + " m/s")
                in 248..293 -> wind_value.text = ("west " + weather.speed.toString() + " m/s")
                in 293..338 -> wind_value.text =
                    ("north-west ," + weather.speed.toString() + " m/s")
                in 338..360 -> wind_value.text = ("north ," + weather.speed.toString() + " m/s")
            }
            pressure_value.text = weather.pressure.toString()
            weather_type.text = weather.main
            setWeatherType(weather)

            Picasso.get().load(Constant.imageUrl + weather.icon + ".png").resize(250, 250)
                .into(weather_icon)
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
            city.text = SpannableStringBuilder("")
            city.isFocusableInTouchMode = true
            city.requestFocus()
            showKeyboard()
        }

        celsia.setOnClickListener {
            if (!temp.text.isNullOrEmpty() && !isCelsia) {
                metric_type.text = " °C"
                temp.text = (temp.text.toString().toInt() + 273).toString()
                isCelsia = true
                celsia.setBackgroundResource(R.drawable.left_white)
                fahrenheit.setBackgroundResource(R.drawable.right_grey)


            }
        }

        fahrenheit.setOnClickListener {
            if (!temp.text.isNullOrEmpty() && isCelsia) {

                temp.text = (temp.text.toString().toInt() - 273).toString()
                metric_type.text = " °F"
                isCelsia = false
                celsia.setBackgroundResource(R.drawable.left_grey)
                fahrenheit.setBackgroundResource(R.drawable.right_white)

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
                        isCelsia = true
                        city.text = SpannableStringBuilder(city.text.trim('\n', ' '))
                        viewModel.getWeatherByCity(s.toString())
                        celsia.setBackgroundResource(R.drawable.left_white)
                        fahrenheit.setBackgroundResource(R.drawable.right_grey)
                        showKeyboard()
                    }
            }
        })

        geolocation.setOnClickListener {
            onLocationIconClick()
        }
    }

    private fun onLocationIconClick() {
        if (checkLocationPermission()) {
            viewModel.getWeatherByCoord()
            isCelsia = true
            celsia.setBackgroundResource(R.drawable.left_white)
            fahrenheit.setBackgroundResource(R.drawable.right_grey)
        }
    }

    private fun checkLocationPermission(): Boolean {
        return checkPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)
                && checkPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun checkPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

}