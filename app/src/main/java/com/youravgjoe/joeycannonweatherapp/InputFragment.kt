package com.youravgjoe.joeycannonweatherapp

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.youravgjoe.joeycannonweatherapp.databinding.InputFragmentBinding
import com.youravgjoe.joeycannonweatherapp.models.WeatherData
import com.youravgjoe.joeycannonweatherapp.viewmodels.InputViewModel
import com.youravgjoe.joeycannonweatherapp.viewmodels.InputViewModelFactory
import okhttp3.Cache
import java.io.File

const val INPUT_TAG = "InputFragment"

class InputFragment : Fragment() {
    private lateinit var binding: InputFragmentBinding
    private lateinit var viewModel: InputViewModel
    private lateinit var inputInterface: InputFragmentInterface

    companion object {
        fun newInstance() = InputFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inputInterface = context as InputFragmentInterface
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.dispose()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = InputFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, InputViewModelFactory(getCache(), inputInterface))
            .get(InputViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@InputFragment
            viewModel = this@InputFragment.viewModel
        }

        setupViews()
        observeData()
    }

    private fun setupViews() {
        binding.getWeatherButton.setOnClickListener {
            onGetWeatherButtonClick()
        }

        binding.cityInput.requestFocus()

        binding.cityInput.addTextChangedListener { text: Editable? ->
            viewModel.onCityInputTextChanged(text)
        }

        binding.cityInput.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event.action == KeyEvent.ACTION_DOWN &&
                    event.keyCode == KeyEvent.KEYCODE_ENTER) {
                onGetWeatherButtonClick()
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }
    }

    private fun observeData() {
        viewModel.getWeatherButtonEnabled.observe(this, Observer { enabled ->
            binding.getWeatherButton.isEnabled = enabled
        })

        viewModel.showProgressBar.observe(this, Observer { show ->
            binding.progressBar.isVisible = show
        })

        viewModel.showError.observe(this, Observer { show ->
            binding.error.isVisible = show
        })
    }

    private fun onGetWeatherButtonClick() {
        if (binding.cityInput.text.isNullOrBlank()) return

        viewModel.onGetWeatherButtonClick(binding.cityInput.text.toString())
        hideKeyboard(binding.root)
        binding.cityInput.text?.clear()
    }

    private fun getCache(): Cache {
        val httpCacheDirectory = File(requireContext().cacheDir, "http-cache")
        val cacheSize: Long = 10 * 1024 * 1024 // 10 MiB
        return Cache(httpCacheDirectory, cacheSize)
    }

    private fun hideKeyboard(view: View) {
        view.clearFocus()
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    interface InputFragmentInterface {
         fun onGetWeather(city: String, list: List<WeatherData>)
    }
}