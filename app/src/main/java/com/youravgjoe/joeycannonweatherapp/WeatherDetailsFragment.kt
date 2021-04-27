package com.youravgjoe.joeycannonweatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.youravgjoe.joeycannonweatherapp.databinding.WeatherDetailsFragmentBinding
import com.youravgjoe.joeycannonweatherapp.models.WeatherData
import com.youravgjoe.joeycannonweatherapp.viewmodels.WeatherDetailsViewModel
import com.youravgjoe.joeycannonweatherapp.viewmodels.WeatherDetailsViewModelFactory


const val WEATHER_DETAILS_TAG = "WeatherDetailsFragment"

class WeatherDetailsFragment(val weatherData: WeatherData) : Fragment() {
    private lateinit var binding: WeatherDetailsFragmentBinding
    private lateinit var viewModel: WeatherDetailsViewModel

    companion object {
        fun newInstance(weatherData: WeatherData) = WeatherDetailsFragment(weatherData)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WeatherDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, WeatherDetailsViewModelFactory(
            weatherData,
            requireContext())
        ).get(WeatherDetailsViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@WeatherDetailsFragment
            viewModel = this@WeatherDetailsFragment.viewModel
        }

        viewModel.getData()
        observeData()
    }

    private fun observeData() {
        viewModel.iconUrl.observe(this, Observer {
            Glide.with(binding.icon)
                .load(it)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .timeout(6000) // 60 second timeout
                .into(binding.icon)
        })

        viewModel.temperature.observe(this, Observer {
            binding.temperature.text = it
        })

        viewModel.feelsLike.observe(this, Observer {
            binding.feelsLike.text = it
        })

        viewModel.weatherMain.observe(this, Observer {
            binding.weatherMain.text = it
        })

        viewModel.weatherDescription.observe(this, Observer {
            binding.weatherDescription.text = it
        })
    }
}