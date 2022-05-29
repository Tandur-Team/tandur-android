package com.tandurteam.tandur.core.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tandurteam.tandur.R
import com.tandurteam.tandur.core.model.network.plantdetail.response.MonthlyData
import com.tandurteam.tandur.databinding.ItemConditionDetailBinding

class MonthlyLocationConditionAdapter :
    RecyclerView.Adapter<MonthlyLocationConditionAdapter.ViewHolder>() {

    private var _binding: ItemConditionDetailBinding? = null
    private val binding get() = _binding!!
    private var listData = ArrayList<MonthlyData>()

    fun setData(newListData: List<MonthlyData>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        Log.d(TAG, "setData: $newListData")
        listData.forEachIndexed { index, _ -> notifyItemChanged(index) }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemConditionDetailBinding.bind(itemView)

        fun bind(data: MonthlyData) {
            with(binding) {
                tvBulan.text = itemView.context.getString(
                    R.string.month,
                    (absoluteAdapterPosition + 1).toString()
                )

                // set geospatial data
                tvHujan.text = data.averageRain.toString()
                tvTemp.text = data.averageTemp.toString()
                tvHumidity.text = data.averageHumidity.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = ItemConditionDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val condition = listData[position]
        Log.d(TAG, "onBindViewHolder: $condition")
        holder.bind(condition)
    }

    override fun getItemCount(): Int = listData.size

    companion object {
        private val TAG = MonthlyLocationConditionAdapter::class.java.simpleName
    }
}