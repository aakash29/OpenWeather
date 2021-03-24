package com.open.weather.demo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.open.weather.demo.AppBase

@Database(entities = [City::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao

    companion object {

        @Volatile
        private var instance: WeatherDatabase? = null

        fun getDatabase(): WeatherDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(AppBase.applicationContext()).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, WeatherDatabase::class.java, "cities")
                .fallbackToDestructiveMigration()
                .build()
    }
}