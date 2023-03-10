package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.ApiService
import com.example.weatherapp.data.History
import com.example.weatherapp.data.HistoryDB
import com.example.weatherapp.data.HistoryDao
import com.example.weatherapp.databinding.ActivityWeatherBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding
    private lateinit var weatherApi: ApiService
    private lateinit var historyDao: HistoryDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        historyDao = HistoryDB.getInstance(this@WeatherActivity).dao()
        val cityName = intent.getStringExtra("cityName")?.capitalize()
        val id = intent.getLongExtra("id", -1)
        Log.d(TAG, "onCreate: cityName: $cityName, id: $id")

        binding.backBtn.setOnClickListener {
            finishAfterTransition()
        }

        if (id >= 0) {
            loadFromDB(id)
        } else {
            loadFromApi(cityName!!)
        }

    }

    private fun loadFromApi(cityName: String) {
        weatherApi = Retrofit.Builder().baseUrl(API_URL).client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiService::class.java)

        binding.progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = weatherApi.getCurrentWeather(cityName, API_KEY)
                Log.d(TAG, "onCreate: resp : $response")
                response.apply {
                    val history = History(
                        cityName = location.name,
                        currentTemp = current.temp_c,
                        windSpeed = current.wind_kph,
                        condition = current.condition.text,
                        feelsLike = current.feelslike_c.toString(),
                        max = forecast.forecastday.first().day.maxtemp_c,
                        min = forecast.forecastday.first().day.mintemp_c,
                        cloud = current.cloud,
                        pressure_in = current.pressure_in,
                        precip_mm = current.precip_mm,
                        humidity = current.humidity,
                        vis_km = current.vis_km,
                        uv = current.pressure_in,
                        list = forecast.forecastday.first().hour
                    )
                    historyDao.insertWeatherHistory(history)
                    updateUI(history)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showError()
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private suspend fun updateUI(history: History) = withContext(Dispatchers.Main) {
        binding.progressBar.visibility = View.GONE
        binding.cityName.text = history.cityName
        binding.conditionValue.text = history.condition
        binding.feelsLikeValue.text = "Feels like: ${history.feelsLike} ℃"
        binding.tempValue.text = "${history.currentTemp} ℃"

        binding.tempText.text = "${history.currentTemp} ℃"
        binding.minText.text = "${history.min} ℃"
        binding.maxText.text = "${history.max} ℃"
        binding.windText.text = "${history.windSpeed} km/hr"
        binding.pressureText.text = "${history.pressure_in} in"
        binding.precipText.text = "${history.precip_mm} mm"
        binding.cloudText.text = "${history.cloud}%"
        binding.humidityText.text = "${history.humidity}"
        binding.visibilityText.text = "${history.vis_km} km"
        binding.uvText.text = "${history.uv}"

        binding.hourlyWeatherList.apply {
            adapter =  HourlyAdapter(history.list)
        }
    }


    private fun loadFromDB(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val item = historyDao.getWeatherById(id)
                Log.d(TAG, "loadFromDB:  $item")
                updateUI(item!!)
            } catch (e: Exception) {
                e.printStackTrace()
                showError()
                finishAfterTransition()
            }
        }
    }

    private suspend fun showError() {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@WeatherActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }
}

private const val TAG = "WeatherActivity"
private const val API_URL = "https://api.weatherapi.com/"
private const val API_KEY = "5121c3e648f046d09df45242231002"
