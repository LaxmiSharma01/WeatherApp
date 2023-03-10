package com.example.weatherapp.data

data class Weather(
    val current: Current,
    val location: Location,
    val forecast: Forecast
) {
    data class Current(
        val cloud: Int,
        val condition: Condition,
        val feelslike_c: Double,
        val temp_c: Double,
        val wind_kph: Double,
        val pressure_in: Double,
        val precip_mm: Double,
        val humidity: Double,
        val vis_km: Double,
        val uv: Double,
        val time: String?
    ) {
        data class Condition(
            val code: Int,
            val icon: String,
            val text: String
        )
    }

    data class Forecast(
        val forecastday: List<Forecastday>
    ) {
        data class Forecastday(
            val hour: List<Current>,
            val day: Day
        )

        data class Day(
            val maxtemp_c: Double,
            val mintemp_c: Double,
            val avgtemp_c: Double,
            val maxwind_kph: Double,
            val totalprecip_mm: Double
        )
    }

    data class Location(
        val country: String,
        val localtime: String,
        val name: String,
    )
}