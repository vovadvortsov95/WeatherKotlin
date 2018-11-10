package com.example.vladimirdvortsov.weatherkotlin.util

import com.example.vladimirdvortsov.weatherkotlin.model.Weather
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class WeatherDeserializer : JsonDeserializer<Weather>{

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Weather? {

        if (json != null){
            try {
                // return Weather(
                // params what we need
                return Weather(
                json.asJsonObject.get("weather").asJsonObject.get("mist").asString,
                json.asJsonObject.get("main").asJsonObject.get("temp").asDouble,
                json.asJsonObject.get("main").asJsonObject.get("pressure").asInt,
                json.asJsonObject.get("main").asJsonObject.get("humidity").asInt,
                json.asJsonObject.get("main").asJsonObject.get("temp_min").asDouble,
                json.asJsonObject.get("main").asJsonObject.get("temp_max").asDouble,
                json.asJsonObject.get("wind").asJsonObject.get("speed").asInt,
                json.asJsonObject.get("wind").asJsonObject.get("deg").asInt,
                json.asJsonObject.get("name").asString,
                json.asJsonObject.get("cod").asInt ) // 200 is OK
            }
            catch (e : Throwable){
                return null
            }
        }
        return null
    }

}