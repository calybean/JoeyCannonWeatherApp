package com.youravgjoe.joeycannonweatherapp.models

import android.icu.text.SimpleDateFormat
import android.text.format.DateFormat
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.util.*

data class ListWeatherData(
    @SerializedName("cod")
    val cod: String,
    @SerializedName("message")
    val message: Int,
    @SerializedName("cnt")
    val count: Int,
    @SerializedName("list")
    val list: List<WeatherData>
)

data class WeatherData(
    @SerializedName("dt")
    val dt: Long,
    @SerializedName("main")
    val mainWeatherData: MainWeatherData,
    @SerializedName("weather")
    val weather: List<WeatherDescData>,
    @SerializedName("clouds")
    val clouds: JSONObject,
    @SerializedName("wind")
    val wind: JSONObject,
    @SerializedName("visibility")
    val visibility: Float,
    @SerializedName("pop")
    val pop: Float,
    @SerializedName("rain")
    val rain: JSONObject,
    @SerializedName("sys")
    val sys: JSONObject,
    @SerializedName("dt_txt")
    val dt_txt: String,
    var iconUrl: String
) {
    fun getDateTimeString(): String {
        return DateFormat.format("EEE, MMM d, h:mm aa", Date(dt * 1000)).toString()
    }
}

data class WeatherDescData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("main")
    val main: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String
)

data class MainWeatherData(
    @SerializedName("temp")
    val temp: Float,
    @SerializedName("feels_like")
    val feels_like: Float,
    @SerializedName("temp_min")
    val temp_min: Float,
    @SerializedName("temp_max")
    val temp_max: Float,
    @SerializedName("pressure")
    val pressure: Float,
    @SerializedName("sea_level")
    val sea_level: Float,
    @SerializedName("grnd_level")
    val grnd_level: Float,
    @SerializedName("humidity")
    val humidity: Float,
    @SerializedName("temp_kf")
    val temp_kf: Float
)
