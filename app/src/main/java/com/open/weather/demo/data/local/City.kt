package com.open.weather.demo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city")
data class City(
    @PrimaryKey
    val id: Int?,
    val name: String?,
    val country: String?,
    val latitude: String?,
    val longitude: String?
)