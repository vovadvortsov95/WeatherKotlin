package com.example.vladimirdvortsov.weatherkotlin

import android.content.Context
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.vladimirdvortsov.weatherkotlin.api.GetWeatherResponce


class MainActivity : AppCompatActivity(), android.arch.lifecycle.Observer<Location> {

    private lateinit var context : Context


    private lateinit var city : TextView
    private lateinit var humidity : TextView
    private lateinit var wind : TextView
    private lateinit var pressure : TextView
    private lateinit var temp : TextView
    private lateinit var weatherType : TextView



    override fun onChanged(t: Location?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = applicationContext
        observeLocationLD(context)
        var api = GetWeatherResponce.create()
    }

    private fun observeLocationLD(context: Context){
        val locationLiveData = LocationLiveData.getInstance(context)
        locationLiveData!!.observeForever(this)
    }


}
