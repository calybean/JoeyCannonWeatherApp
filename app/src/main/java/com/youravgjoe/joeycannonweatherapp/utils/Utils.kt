package com.youravgjoe.joeycannonweatherapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.youravgjoe.joeycannonweatherapp.R
import java.util.*


const val SHARED_PREFS_NAME = "prefs"
const val PREF_TEMPERATURE_UNITS = "prefTemperatureUnits"

object Utils {
    fun getTempStringInCurrentUnits(
        kelvin: Float,
        context: Context
    ): String {
        return when (getCurrentUnits(context)) {
            TemperatureUnits.FAHRENHEIT -> {
                context.resources.getString(R.string.degrees_F, kelvinToFahrenheit(kelvin))
            }
            TemperatureUnits.CELSIUS -> {
                context.resources.getString(R.string.degrees_C, kelvinToCelsius(kelvin))
            }
        }
    }

    fun getTempFeelsLikeInCurrentUnits(
        kelvin: Float,
        context: Context
    ): String {
        return context.resources.getString(
            R.string.feels_like,
            getTempInCurrentUnits(kelvin, context)
        )
    }

    private fun getTempInCurrentUnits(
        kelvin: Float,
        context: Context
    ): Float {
        return when (getCurrentUnits(context)) {
            TemperatureUnits.FAHRENHEIT -> kelvinToFahrenheit(kelvin)
            TemperatureUnits.CELSIUS -> kelvinToCelsius(kelvin)
        }
    }

    private fun kelvinToFahrenheit(kelvin: Float): Float {
        return (kelvin - 273.15F) * 9 / 5 + 32
    }

    private fun kelvinToCelsius(kelvin: Float): Float {
        return (kelvin - 273.15F)
    }

    fun getCurrentUnits(
        context: Context
    ): TemperatureUnits {
        val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE)
        return sharedPrefs.getString(PREF_TEMPERATURE_UNITS, null)?.let {
            if (it == TemperatureUnits.CELSIUS.value) {
                TemperatureUnits.CELSIUS
            } else {
                TemperatureUnits.FAHRENHEIT
            }
        } ?: TemperatureUnits.FAHRENHEIT
    }

    fun setCurrentUnits(context: Context, units: TemperatureUnits) {
        val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putString(PREF_TEMPERATURE_UNITS, units.value)
        editor.apply()
    }
}

enum class TemperatureUnits(val value: String) {
    FAHRENHEIT("fahrenheit"),
    CELSIUS("celsius")
}

@ExperimentalStdlibApi
fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") {
        it.toLowerCase(Locale.getDefault())
            .capitalize(Locale.getDefault())
    }