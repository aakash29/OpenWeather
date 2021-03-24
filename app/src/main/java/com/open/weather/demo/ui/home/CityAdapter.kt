package com.open.weather.demo.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.open.weather.demo.data.local.City
import com.open.weather.demo.databinding.ItemCityBinding
import java.util.*
import kotlin.collections.ArrayList

class CityAdapter(
    private val cities: List<City>,
    private val itemClickCallback: (City?, isForDelete: Boolean) -> (Unit)
) : RecyclerView.Adapter<CityAdapter.ViewHolder>(), Filterable {

    var searchCityList: ArrayList<City>? = arrayListOf()

    init {

        searchCityList?.addAll(cities)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemCityBinding = ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemCityBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.data = searchCityList?.get(position)

        holder.itemView.setOnClickListener {

            itemClickCallback.invoke(searchCityList?.get(position), false)
        }

        holder.binding.imgDelete.setOnClickListener {

            itemClickCallback.invoke(searchCityList?.get(position), true)
        }
    }

    override fun getItemCount(): Int = searchCityList?.size ?: 0

    class ViewHolder(val binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    searchCityList?.addAll(cities)
                } else {

                    val filteredList = ArrayList<City>()
                    cities.forEach { _city ->

                        if (_city.name?.toLowerCase(Locale.getDefault())?.contains(charString.toLowerCase(Locale.getDefault())) == true) {
                            filteredList.add(_city)
                        }
                    }
                    searchCityList = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = searchCityList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                @Suppress("UNCHECKED_CAST")
                searchCityList = filterResults.values as ArrayList<City>
                notifyDataSetChanged()
            }
        }
    }
}