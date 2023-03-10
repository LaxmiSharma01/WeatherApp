package com.example.weatherapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Entity
data class History(
    val cityName: String,
    val currentTemp: Double,
    val windSpeed: Double,
    val condition: String,
    val feelsLike: String = "",
    val cloud: Int,
    val pressure_in: Double,
    val precip_mm: Double,
    val humidity: Double,
    val vis_km: Double,
    val uv: Double,
    val max: Double,
    val min: Double,
    val list: List<Weather.Current>
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = System.currentTimeMillis()

}

class Converters {
    @TypeConverter
    fun fromListCurrent(value: List<Weather.Current>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toListCurrent(value: String): List<Weather.Current> {
        val listType = object : TypeToken<List<Weather.Current>>() {}.type
        return try {
            Gson().fromJson(value, listType)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}

