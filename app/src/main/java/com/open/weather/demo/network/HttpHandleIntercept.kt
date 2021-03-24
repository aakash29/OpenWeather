package com.open.weather.demo.network

import okhttp3.Interceptor
import okhttp3.Response

class HttpHandleIntercept : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request().newBuilder().build()
        val response: Response?
        response = chain.proceed(request)
        return response
    }
}