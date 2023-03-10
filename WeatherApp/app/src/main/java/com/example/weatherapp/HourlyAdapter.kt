package com.example.weatherapp

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weatherapp.data.Weather
import com.example.weatherapp.databinding.ItemHourlyWeatherBinding

class HourlyAdapter(
    private val list: List<Weather.Current>
) : RecyclerView.Adapter<HourlyAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(private val binding: ItemHourlyWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Weather.Current) {
            binding.temp.text = "${item.temp_c} ℃"
            binding.hour.text = "${item.time?.takeLast(5)}"
            val url = "https:" + item.condition.icon
            Log.d("TAG", "bind: $url")
            binding.image.load(url)
        }
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = list[position]
        holder.bind(currentItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            ItemHourlyWeatherBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }


}