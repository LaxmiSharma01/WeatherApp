package com.example.weatherapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.History
import com.example.weatherapp.databinding.ItemWeatherCardBinding

class HistoryAdapter(
    private val list: List<History>,
    private val onItemClick: (Long) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(private val binding: ItemWeatherCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: History) {
            binding.temp.text = "${item.currentTemp} ℃"
            binding.location.text = item.cityName
            binding.description.text = item.condition
            binding.minMax.text = "Min: ${item.windSpeed} ℃, Max: ${item.max} ℃"

            binding.root.setOnClickListener {
                item.id?.let{ id ->
                    onItemClick.invoke(id)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = list[position]
        holder.bind(currentItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            ItemWeatherCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }


}