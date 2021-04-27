package com.youravgjoe.joeycannonweatherapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.youravgjoe.joeycannonweatherapp.models.WeatherData

class WeatherDetailsViewModelFactory(
   private val weatherData: WeatherData,
   private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WeatherDetailsViewModel(
            weatherData,
            context
        ) as T
    }
}