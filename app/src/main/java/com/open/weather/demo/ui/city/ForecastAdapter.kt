package com.open.weather.demo.ui.city

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.open.weather.demo.databinding.ItemForecastBinding
import com.open.weather.demo.model.ForecastResponse

class ForecastAdapter(private val foreCastList: List<ForecastResponse.Forecast>?): RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemForecastBinding = ItemForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemForecastBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.data = foreCastList?.get(position)
    }

    override fun getItemCount(): Int = foreCastList?.size ?: 0

    class ViewHolder(val binding: ItemForecastBinding) : RecyclerView.ViewHolder(binding.root)
}
