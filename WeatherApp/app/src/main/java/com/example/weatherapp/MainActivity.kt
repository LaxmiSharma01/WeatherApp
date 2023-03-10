package com.example.weatherapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.data.HistoryDB
import com.example.weatherapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.citySearch.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val cityName = textView.text?.toString()
                if (cityName.isNullOrEmpty()) {
                    Toast.makeText(this, "CityName should not empty", Toast.LENGTH_SHORT).show()
                    return@setOnEditorActionListener false
                }
                openWeatherScreenFromSearch(cityName)
                return@setOnEditorActionListener true
            }
            false
        }


    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val history = HistoryDB.getInstance(this@MainActivity).dao().getWeatherHistory()
                Log.d(TAG, "onResume: history : $history")
                withContext(Dispatchers.Main) {
                    binding.weatherList.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = HistoryAdapter(history){ id ->
                            openWeatherScreenFromId(id)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openWeatherScreenFromSearch(cityName: String) {
        val intent = Intent(this, WeatherActivity::class.java)
        intent.putExtra("cityName", cityName)
        startActivity(intent)
    }

    private fun openWeatherScreenFromId(id: Long) {
        val intent = Intent(this, WeatherActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }
}

private const val TAG = "MainActivity"