package com.youravgjoe.joeycannonweatherapp.repositories

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit


class CacheInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        val cacheControl = CacheControl.Builder()
            .maxAge(15, TimeUnit.MINUTES)
            .build()
        return response.newBuilder()
            .removeHeader("Pragma")
            .removeHeader("Cache-Control")
            .header("Cache-Control", cacheControl.toString())
            .build()
    }
}