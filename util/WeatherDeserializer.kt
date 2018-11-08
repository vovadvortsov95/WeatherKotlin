package com.example.user.weatherkotlin.util

import com.example.user.weatherkotlin.model.Weather
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class WeatherDeserializer : JsonDeserializer<Weather>{

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Weather? {
        if (json != null){
           // return Weather(
                // params what we need
            json.asJsonObject.get("weather").asJsonObject.get("mist").asString
            json.asJsonObject.get("main").asJsonObject.get("temp").asString
            json.asJsonObject.get("main").asJsonObject.get("pressure").asString
            json.asJsonObject.get("main").asJsonObject.get("humidity").asString
            json.asJsonObject.get("main").asJsonObject.get("temp_min").asString
            json.asJsonObject.get("main").asJsonObject.get("temp_max").asString
            json.asJsonObject.get("wind").asJsonObject.get("speed").asString
            json.asJsonObject.get("wind").asJsonObject.get("deg").asString
            json.asJsonObject.get("name").asString
            json.asJsonObject.get("cod").asString // 200 is OK

         //   )
        }
        return null
    }

}