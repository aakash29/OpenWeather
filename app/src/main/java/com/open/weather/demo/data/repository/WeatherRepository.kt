package com.open.weather.demo.data.repository

import com.open.weather.demo.base.BaseRepository
import com.open.weather.demo.network.Resource
import com.open.weather.demo.network.RetrofitBuilder
import com.open.weather.demo.model.ForecastResponse
import com.open.weather.demo.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository : BaseRepository() {

    suspend fun getWeatherByLatLng(latitude: String?, longitude: String?, units: String): Resource<WeatherResponse?> {

        return withContext(Dispatchers.IO) {
            return@withContext networkCall(
                call = {
                    RetrofitBuilder.apiService.getWeatherByLatLng(latitude = latitude, longitude = longitude, units = units)
                })
        }
    }

    suspend fun getForecastByLatLang(latitude: String?, longitude: String?, units: String): Resource<ForecastResponse?> {

        return withContext(Dispatchers.IO) {
            return@withContext networkCall(
                call = {
                    RetrofitBuilder.apiService.getForecastByLatLang(latitude = latitude, longitude = longitude, units = units)
                })
        }
    }

}