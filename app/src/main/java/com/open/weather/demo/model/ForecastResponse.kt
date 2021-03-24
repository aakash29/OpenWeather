package com.open.weather.demo.model

import android.graphics.Color
import com.google.gson.annotations.SerializedName
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.format.TextStyle
import java.text.SimpleDateFormat
import java.util.*


class ForecastResponse {

    @SerializedName("list")
    var list: List<Forecast>? = null

    class City {

        @SerializedName("sunset")
        var sunset = 0

        @SerializedName("sunrise")
        var sunrise = 0

        @SerializedName("timezone")
        var timezone = 0

        @SerializedName("population")
        var population = 0

        @SerializedName("country")
        var country: String? = null

        @SerializedName("coord")
        var coord: Coord? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("id")
        var id = 0

    }

    class Coord {
        @SerializedName("lon")
        var lon = 0.0

        @SerializedName("lat")
        var lat = 0.0

    }

    class Forecast {

        @SerializedName("dt_txt")
        var dtTxt: String? = null

        @SerializedName("wind")
        var wind: Wind? = null

        @SerializedName("weather")
        var weather: List<Weather>? = null

        @SerializedName("main")
        var main: Main? = null

        @SerializedName("dt")
        var dt: Long? = 0

        fun getDay(): String? {
            return dt?.let { getDateTime(it)?.getDisplayName(TextStyle.FULL, Locale.getDefault()) }
        }

        private fun getDateTime(s: Long): DayOfWeek? {
            return try {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val netDate = Date(s * 1000)
                val formattedDate = sdf.format(netDate)

                LocalDate.of(
                    formattedDate.substringAfterLast("/").toInt(),
                    formattedDate.substringAfter("/").take(2).toInt(),
                    formattedDate.substringBefore("/").toInt()
                ).dayOfWeek
            } catch (e: Exception) {
                e.printStackTrace()
                DayOfWeek.MONDAY
            }
        }

        fun getHourOfDay(): String {
            return dtTxt?.substringAfter(" ")?.substringBeforeLast(":") ?: "00:00"
        }

        fun getColor(): Int {
            return when (dt?.let { getDateTime(it) }) {
                DayOfWeek.MONDAY -> Color.parseColor("#28E0AE")
                DayOfWeek.TUESDAY -> Color.parseColor("#FF0090")
                DayOfWeek.WEDNESDAY -> Color.parseColor("#FFAE00")
                DayOfWeek.THURSDAY -> Color.parseColor("#0090FF")
                DayOfWeek.FRIDAY -> Color.parseColor("#DC0000")
                DayOfWeek.SATURDAY -> Color.parseColor("#0051FF")
                DayOfWeek.SUNDAY -> Color.parseColor("#3D28E0")
                else -> Color.parseColor("#28E0AE")
            }
        }
    }

    class Wind {

        @SerializedName("speed")
        var speed = 0.0
    }

    class Weather {
        @SerializedName("icon")
        var icon: String? = null

        @SerializedName("description")
        var description: String? = null

        @SerializedName("main")
        var main: String? = null

        @SerializedName("id")
        var id = 0

    }

    class Main {
        @SerializedName("temp_kf")
        var tempKf = 0.0

        @SerializedName("humidity")
        var humidity = 0

        @SerializedName("grnd_level")
        var grndLevel = 0

        @SerializedName("sea_level")
        var seaLevel = 0

        @SerializedName("pressure")
        var pressure = 0

        @SerializedName("temp_max")
        var tempMax = 0.0

        @SerializedName("temp_min")
        var tempMin = 0.0

        @SerializedName("feels_like")
        var feelsLike = 0.0

        @SerializedName("temp")
        var temp = 0.0

        fun getTempString(): String {
            return temp.toString().substringBefore(".") + "°"
        }

        fun getHumidityString(): String {
            return "$humidity°"
        }

        fun getTempMinString(): String {
            return tempMin.toString().substringBefore(".") + "°"
        }

        fun getTempMaxString(): String {
            return tempMax.toString().substringBefore(".") + "°"
        }
    }
}