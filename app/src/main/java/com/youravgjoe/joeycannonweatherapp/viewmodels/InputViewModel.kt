package com.youravgjoe.joeycannonweatherapp.viewmodels

import android.text.Editable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.youravgjoe.joeycannonweatherapp.InputFragment
import com.youravgjoe.joeycannonweatherapp.models.ListWeatherData
import com.youravgjoe.joeycannonweatherapp.repositories.CacheInterceptor
import com.youravgjoe.joeycannonweatherapp.services.OpenWeatherService
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


private const val BASE_URL = "https://openweathermap.org/"
private const val APP_ID = "6e63e7b24d32d9acc94492e4625ed13c"
private const val TIMEOUT: Long = 60

class InputViewModel(
    cache: Cache,
    private val inputInterface: InputFragment.InputFragmentInterface
) : ViewModel() {
    private val retrofit: Retrofit
    private val openWeatherService: OpenWeatherService
    private val compositeDisposable = CompositeDisposable()

    val getWeatherButtonEnabled = MutableLiveData<Boolean>()
    val showProgressBar = MutableLiveData<Boolean>()
    val showError = MutableLiveData<Boolean>()

    init {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addNetworkInterceptor(CacheInterceptor())
            .cache(cache)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        openWeatherService = retrofit.create(OpenWeatherService::class.java)
    }

    fun onGetWeatherButtonClick(city: String) {
        getWeatherButtonEnabled.postValue(false)
        showProgressBar.postValue(true)

        openWeatherService.getWeatherForCity(city, APP_ID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ListWeatherData> {
                override fun onComplete() {}

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(list: ListWeatherData) {
                    list.list.forEach { weatherData ->
                        val icon = weatherData.weather[0].icon
                        weatherData.iconUrl = "https://openweathermap.org/img/wn/${icon}@4x.png"
                    }
                    inputInterface.onGetWeather(city, list.list)
                    showProgressBar.postValue(false)
                    showError.postValue(false)
                }

                override fun onError(e: Throwable) {
                    showProgressBar.postValue(false)
                    showError.postValue(true)
                    e.printStackTrace()
                }
            })
    }

    fun dispose() = compositeDisposable.dispose()

    fun onCityInputTextChanged(text: Editable?) {
        if (text.isNullOrBlank()) {
            // only disable if it's not currently disabled
            if (getWeatherButtonEnabled.value != false) {
                getWeatherButtonEnabled.postValue(false)
            }
        } else {
            // only enable if it's not currently enabled
            if (getWeatherButtonEnabled.value != true) {
                getWeatherButtonEnabled.postValue(true)
            }
        }
    }
}