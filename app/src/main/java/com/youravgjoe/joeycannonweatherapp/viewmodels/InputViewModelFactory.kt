package com.youravgjoe.joeycannonweatherapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.youravgjoe.joeycannonweatherapp.InputFragment
import okhttp3.Cache

class InputViewModelFactory(
    private val cache: Cache,
    private val inputInterface: InputFragment.InputFragmentInterface
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return InputViewModel(
            cache,
            inputInterface
        ) as T
    }
}