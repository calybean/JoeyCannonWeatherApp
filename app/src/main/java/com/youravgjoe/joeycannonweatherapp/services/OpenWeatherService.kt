package com.youravgjoe.joeycannonweatherapp.services

import com.youravgjoe.joeycannonweatherapp.models.ListWeatherData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherService {
    @GET("//api.openweathermap.org/data/2.5/forecast")
    fun getWeatherForCity(
        @Query("q")
        city: String,
        @Query("appid")
        appId: String
    ): Observable<ListWeatherData>

//    @GET("img/wn/{icon}@2x.png")
//    fun getIcon(
//        @Path(value = "icon")
//        icon: String
//    ): Observable<JSONObject>
}