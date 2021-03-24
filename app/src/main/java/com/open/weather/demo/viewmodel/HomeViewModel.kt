package com.open.weather.demo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.open.weather.demo.data.local.City
import com.open.weather.demo.data.local.CityDao
import com.open.weather.demo.data.local.WeatherDatabase
import com.open.weather.demo.data.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private var repository: WeatherRepository? = null
    private var localDataSource: CityDao? = null
    var cities: MutableLiveData<List<City>>? = null

    init {

        repository = WeatherRepository()
        localDataSource = WeatherDatabase.getDatabase().cityDao()
        cities = MutableLiveData()
    }

    fun getBookmarkedCities(): LiveData<List<City>>? = localDataSource?.getAllCity()

    fun removeFromBookmark(city: City?) {

        viewModelScope.launch(Dispatchers.IO){
            localDataSource?.delete(city)
        }
    }
}