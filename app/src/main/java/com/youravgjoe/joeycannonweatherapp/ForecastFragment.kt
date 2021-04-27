package com.youravgjoe.joeycannonweatherapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.youravgjoe.joeycannonweatherapp.databinding.ForecastFragmentBinding
import com.youravgjoe.joeycannonweatherapp.models.WeatherData
import com.youravgjoe.joeycannonweatherapp.viewmodels.ForecastViewModel


const val FORECAST_TAG = "ForecastFragment"

class ForecastFragment(
    val city: String,
    private val list: List<WeatherData>
) : Fragment() {
    private lateinit var binding: ForecastFragmentBinding
    private lateinit var viewModel: ForecastViewModel
    private lateinit var forecastInterface: ForecastInterface

    companion object {
        fun newInstance(city: String, list: List<WeatherData>) = ForecastFragment(city, list)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        forecastInterface = context as ForecastInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ForecastFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ForecastViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@ForecastFragment
            viewModel = this@ForecastFragment.viewModel
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = ForecastAdapter(
            list,
            requireContext(),
            forecastInterface
        )
    }

    interface ForecastInterface {
        fun onRowClick(weatherData: WeatherData)
    }
}