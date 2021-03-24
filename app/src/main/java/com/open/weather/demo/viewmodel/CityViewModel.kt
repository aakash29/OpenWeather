package com.open.weather.demo.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.open.weather.demo.data.local.City
import com.open.weather.demo.data.local.CityDao
import com.open.weather.demo.data.local.WeatherDatabase
import com.open.weather.demo.network.Resource
import com.open.weather.demo.data.repository.WeatherRepository
import com.open.weather.demo.model.ForecastResponse
import com.open.weather.demo.model.WeatherResponse
import com.open.weather.demo.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CityViewModel : ViewModel() {

    private var repository: WeatherRepository? = null
    var currentResponse: MutableLiveData<Resource<WeatherResponse?>>? = null
    var forecastDataResponse: MutableLiveData<Resource<ForecastResponse?>>? = null
    private var localDataSource: CityDao? = null
    var bookMarkClick: SingleLiveEvent<Boolean>? = null

    init {

        repository = WeatherRepository()
        currentResponse = MutableLiveData()
        forecastDataResponse = MutableLiveData()
        localDataSource = WeatherDatabase.getDatabase().cityDao()
        bookMarkClick = SingleLiveEvent()
    }

    fun onBookMarkClick() {

        bookMarkClick?.call()
    }

    fun bookmarkCity(){

        viewModelScope.launch(Dispatchers.IO){

            val city = City(
                currentResponse?.value?.data?.ıd,
                currentResponse?.value?.data?.name,
                currentResponse?.value?.data?.sys?.country,
                currentResponse?.value?.data?.coord?.lat.toString(),
                currentResponse?.value?.data?.coord?.lon.toString()
            )
            localDataSource?.insert(city)
            Log.e("@@@", "data inserted.")
        }
    }

    fun removeFromBookmark() {

        viewModelScope.launch(Dispatchers.IO){
            val city = City(
                currentResponse?.value?.data?.ıd,
                currentResponse?.value?.data?.name,
                currentResponse?.value?.data?.sys?.country,
                currentResponse?.value?.data?.coord?.lat.toString(),
                currentResponse?.value?.data?.coord?.lon.toString()
            )
            localDataSource?.delete(city)
        }
    }

    fun getWeather(latitude: String?, longitude: String?, units: String){

        viewModelScope.launch(Dispatchers.Main) {
            currentResponse?.value = Resource.loading()
            currentResponse?.value = repository?.getWeatherByLatLng(latitude, longitude, units)
        }
    }

    fun getForecast(latitude: String?, longitude: String?, units: String){

        viewModelScope.launch(Dispatchers.Main) {
            forecastDataResponse?.value = Resource.loading()
            forecastDataResponse?.value = repository?.getForecastByLatLang(latitude, longitude, units)
        }
    }
}