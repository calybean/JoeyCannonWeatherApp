package com.youravgjoe.joeycannonweatherapp.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.youravgjoe.joeycannonweatherapp.models.WeatherData
import com.youravgjoe.joeycannonweatherapp.utils.Utils

class WeatherDetailsViewModel(
    private val weatherData: WeatherData,
    private val context: Context
) : ViewModel() {
    val iconUrl = MutableLiveData<String>()
    val temperature = MutableLiveData<String>()
    val feelsLike = MutableLiveData<String>()
    val weatherMain = MutableLiveData<String>()
    val weatherDescription = MutableLiveData<String>()

    init {
        getData()
    }

    fun getData() {
        iconUrl.postValue(weatherData.iconUrl)

        val temperatureString = Utils.getTempStringInCurrentUnits(
            weatherData.mainWeatherData.temp,
            context
        )
        temperature.postValue(temperatureString)

        val feelsLikeString = Utils.getTempFeelsLikeInCurrentUnits(
            weatherData.mainWeatherData.feels_like,
            context
        )
        feelsLike.postValue(feelsLikeString)

        weatherMain.postValue(weatherData.weather[0].main)

        weatherDescription.postValue(weatherData.weather[0].description)
    }
}