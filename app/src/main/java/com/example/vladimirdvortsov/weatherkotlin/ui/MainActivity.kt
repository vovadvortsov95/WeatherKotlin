package com.example.vladimirdvortsov.weatherkotlin.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.vladimirdvortsov.weatherkotlin.R
import com.example.vladimirdvortsov.weatherkotlin.model.Weather
import com.squareup.picasso.Picasso


@SuppressLint("WrongViewCast")
class MainActivity : AppCompatActivity() {

    // TODO : LiveData with Weather .
    // TODO : update view with bind . viewModel.bind(weatherLD)
    // mutable live data в ней сделать подписку на апи .  А при обновлении лайв даты апдейтить вью

    private val viewModel by lazy { ViewModelProviders.of(this).get(WeatherView::class.java) }

    private lateinit var city: TextView

    private val cityEdit: TextView by lazy { return@lazy findViewById<TextView>(R.id.change_city) }
    private val humidity: TextView by lazy { return@lazy findViewById<TextView>(R.id.humidity_percent)}
    private val wind: TextView by lazy { return@lazy findViewById<TextView>(R.id.wind_value)}
    private val pressure: TextView by lazy {return@lazy findViewById<TextView>(R.id.preccure_value)}
    private val temp: TextView by lazy { return@lazy findViewById<TextView>(R.id.temp) }
    private val weatherType: TextView by lazy { return@lazy findViewById<TextView>(R.id.weather_type)}
    private val locationImage: ImageView by lazy { return@lazy findViewById<ImageView>(R.id.geolocation) }


    private lateinit var weatherIcon:ImageView

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //     val weather = Weather("main",0.0,0,0,"Description","04h",3,7,"Omsk",200)
        //    bindView(weather)
        context = applicationContext

        initView(context)
        //viewModel.getWeatherByCity("Omsk")
        //viewModel.getWeatherByCoord(context)
        viewModel.weatherLD.observe(this, Observer<Weather> {
            if (it != null) {
                Log.d("it ", " != null")
                bindView(it)
            } else {
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
 //       weatherIcon.setImageURI(Uri.parse("http://openweathermap.org/img/w/" + weather.icon + ".png"))

        val iconURL = "http://openweathermap.org/img/w/" + weather.icon + ".png"
        Picasso.get().load(iconURL).resize(250,250).into(weatherIcon)
    }

    private fun initView(context: Context) {
        weatherIcon = findViewById(R.id.weather_icon)

        city = findViewById(R.id.city)


        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        fun showKeyboard() {
            imm.toggleSoftInput(0, 0)

        }
        city.isFocusable = false // onClick isFocusable = true // TODO : give focus for editText


        cityEdit.setOnClickListener {
            city.isFocusableInTouchMode = true
            city.requestFocus()
            Log.d("FOCUS : ", city.hasFocus().toString())
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
                Toast.makeText(this@MainActivity, "onTextChanged", Toast.LENGTH_SHORT).show()
                Log.d("CharSequence", s.toString())
                //never contains!
                if (s != null)
                    if (s.contains("\n")) {

                        Log.d("FOCUS CHANGED","true")
                        city.isFocusableInTouchMode = false
                        city.clearFocus()

                    }

            }

        })

//        city.setOnKeyListener(View.OnKeyListener{
//
//                fun onKey(v :View, keyCode : Int, event : KeyEvent) : Boolean{
//
//                    return false;
//
//            }
//
//        }
//
//        );
//        private class CustomTextWather : TextWa


        locationImage.setOnClickListener {
            viewModel.getWeatherByCoord(context)
        }
    }
}


