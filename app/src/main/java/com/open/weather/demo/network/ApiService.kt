package com.open.weather.demo.network

import com.open.weather.demo.model.ForecastResponse
import com.open.weather.demo.model.WeatherResponse
import com.open.weather.demo.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("weather?")
    suspend fun getWeatherByLatLng(
        @Query("appid") appId: String = Constants.API_KEY,
        @Query("lat") latitude: String?,
        @Query("lon") longitude: String?,
        @Query("units") units: String
    ): Response<WeatherResponse>

    @GET("forecast?")
    suspend fun getForecastByLatLang(
        @Query("appid") appId: String = Constants.API_KEY,
        @Query("lat") latitude: String?,
        @Query("lon") longitude: String?,
        @Query("units") units: String
    ): Response<ForecastResponse>

}