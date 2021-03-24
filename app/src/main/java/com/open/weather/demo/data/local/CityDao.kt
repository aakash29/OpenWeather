package com.open.weather.demo.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CityDao {

    @Query("SELECT * FROM city")
    fun getAllCity(): LiveData<List<City>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: City?)

    @Delete
    suspend fun delete(city: City?)
}