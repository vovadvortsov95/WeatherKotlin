package com.example.vladimirdvortsov.weatherkotlin.util

import android.util.Log
import com.example.vladimirdvortsov.weatherkotlin.model.Weather
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class WeatherDeserializer : JsonDeserializer<Weather>{

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Weather? {
Log.d("WeatherDeserialize","start")
        if (json != null){

            try {
            Log.d("JSON :",json.toString())
                 return Weather(
                json.asJsonObject.get("weather").asJsonArray.get(0).asJsonObject.get("main").asString,
                json.asJsonObject.get("main").asJsonObject.get("temp").asDouble,
                json.asJsonObject.get("main").asJsonObject.get("pressure").asInt,
                json.asJsonObject.get("main").asJsonObject.get("humidity").asInt,
                json.asJsonObject.get("weather").asJsonArray.get(0).asJsonObject.get("main").asString,//description,
                json.asJsonObject.get("weather").asJsonArray.get(0).asJsonObject.get("icon").asString,//icon,
                json.asJsonObject.get("wind").asJsonObject.get("speed").asInt,
                json.asJsonObject.get("wind").asJsonObject.get("deg").asInt,
                json.asJsonObject.get("name").asString,
                json.asJsonObject.get("cod").asInt )// 200 is OK,
            }
           catch (e : Throwable){
                return null
           }
        }
        return null
    }

}