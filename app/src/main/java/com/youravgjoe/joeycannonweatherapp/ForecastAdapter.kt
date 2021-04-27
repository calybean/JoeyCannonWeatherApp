package com.youravgjoe.joeycannonweatherapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.textview.MaterialTextView
import com.youravgjoe.joeycannonweatherapp.models.WeatherData
import com.youravgjoe.joeycannonweatherapp.utils.Utils.getTempStringInCurrentUnits

class ForecastAdapter(
    private val list: List<WeatherData>,
    private val context: Context,
    private val forecastInterface: ForecastFragment.ForecastInterface
): RecyclerView.Adapter<ForecastAdapter.WeatherRowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherRowViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_data_row, parent, false)
        return WeatherRowViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: WeatherRowViewHolder, position: Int) {
        return holder.bind(list[position])
    }

    inner class WeatherRowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.findViewById(R.id.icon)
        private val weather: MaterialTextView = itemView.findViewById(R.id.weatherMain)
        private val dateTime: MaterialTextView = itemView.findViewById(R.id.dateTime)
        private val temperature: MaterialTextView = itemView.findViewById(R.id.temperature)

        fun bind(weatherData: WeatherData) {
            Glide.with(itemView)
                .load(weatherData.iconUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .timeout(6000) // 60 second timeout
                .into(icon)

            weather.text = weatherData.weather[0].main
            dateTime.text = weatherData.getDateTimeString()
            temperature.text = getTempStringInCurrentUnits(
                weatherData.mainWeatherData.temp,
                context
            )

            itemView.setOnClickListener {
                forecastInterface.onRowClick(weatherData)
            }
        }
    }
}