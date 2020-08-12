package com.example.vladimirdvortsov.weatherkotlin.ui

import android.Manifest
import androidx.lifecycle.Observer
import android.content.Context
import android.content.pm.PackageManager
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.example.vladimirdvortsov.weatherkotlin.Constant
import com.example.vladimirdvortsov.weatherkotlin.R
import com.example.vladimirdvortsov.weatherkotlin.base.BaseActivity
import com.example.vladimirdvortsov.weatherkotlin.model.Weather
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity(R.layout.activity_main) {

    private val viewModel: WeatherViewModel by inject()
    private var isCelsia: Boolean = true

    private fun bindView(weather: Weather) {
            city.text = SpannableStringBuilder(weather.city)
            humidity_value.text = weather.humidity.toString()
            setDeg(weather)
            pressure_value.text = weather.pressure.toString()
            weather_type.text = weather.main
            setWeatherType(weather)
            setImage(weather)
    }

    private fun setImage(weather: Weather) {
        Picasso.get().load(Constant.imageUrl + weather.icon + ".png").resize(250, 250)
            .into(weather_icon)
    }

    private fun setDeg(weather: Weather) {
        wind_value.text = (getDegDirection(weather) + weather.speed.toString() + " m/s")
    }

    private fun getDegDirection(weather: Weather): String {
        return when (weather.deg) {
            in 0..23 -> "north ,"
            in 23..68 -> "north-east ,"
            in 68..113 -> "east ,"
            in 113..158 -> "south-east ,"
            in 158..203 -> "south ,"
            in 203..248 -> "south-west ,"
            in 248..293 -> "west "
            in 293..338 -> "north-west ,"
            else -> "north ,"
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

    override fun observeChanges() {
        viewModel.weatherLD.observe(this, Observer<Weather> {
            it?.let {
                bindView(it)
            }
        })
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, 0)
    }

    override fun initView() {
        setCityListener()
        setCelsiaListener()
        setFahrenheirListener()
        setLocationListener()
        setCityTextListener()
    }

    private fun setCityTextListener() {
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
                        hideKeyboard()
                    }
            }
        })
    }

    private fun setLocationListener() {
        geolocation.setOnClickListener {
            if (checkLocationPermission()) {
                viewModel.getWeatherByCoord()
                isCelsia = true
                celsia.setBackgroundResource(R.drawable.left_white)
                fahrenheit.setBackgroundResource(R.drawable.right_grey)
            }
        }
    }

    private fun setFahrenheirListener() {
        fahrenheit.setOnClickListener {
            if (!temp.text.isNullOrEmpty() && isCelsia) {

                temp.text = (temp.text.toString().toInt() - 273).toString()
                metric_type.text = " °F"
                isCelsia = false
                celsia.setBackgroundResource(R.drawable.left_grey)
                fahrenheit.setBackgroundResource(R.drawable.right_white)

            }
        }
    }

    private fun setCelsiaListener() {
        celsia.setOnClickListener {
            if (!temp.text.isNullOrEmpty() && !isCelsia) {
                metric_type.text = " °C"
                temp.text = (temp.text.toString().toInt() + 273).toString()
                isCelsia = true
                celsia.setBackgroundResource(R.drawable.left_white)
                fahrenheit.setBackgroundResource(R.drawable.right_grey)
            }
        }
    }

    private fun setCityListener() {
        city.isFocusable = false

        change_city.setOnClickListener {
            city.text = SpannableStringBuilder("")
            city.isFocusableInTouchMode = true
            city.requestFocus()
            hideKeyboard()
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