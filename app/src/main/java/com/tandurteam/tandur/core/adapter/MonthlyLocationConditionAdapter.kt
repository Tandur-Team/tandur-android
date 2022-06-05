package com.tandurteam.tandur.core.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tandurteam.tandur.R
import com.tandurteam.tandur.core.model.network.myplantdetail.response.detailmyplant.FixedData
import com.tandurteam.tandur.core.model.network.plantdetail.response.MonthlyData
import com.tandurteam.tandur.databinding.ItemConditionDetailBinding

class MonthlyLocationConditionAdapter :
    RecyclerView.Adapter<MonthlyLocationConditionAdapter.ViewHolder>() {

    private var _binding: ItemConditionDetailBinding? = null
    private val binding get() = _binding!!
    private var listData = ArrayList<MonthlyData>()
    private var dataFix: FixedData? = null

    fun setData(newListData: List<MonthlyData>?, fixedData: FixedData) {
        if (newListData == null) return
        dataFix = fixedData
        listData.clear()
        listData.addAll(newListData)
        Log.d(TAG, "setData: $newListData")
        listData.forEachIndexed { index, _ -> notifyItemChanged(index) }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemConditionDetailBinding.bind(itemView)

        fun bind(data: MonthlyData) {
            with(binding) {
                dataFix?.let {
                    tvBulan.text = itemView.context.getString(
                        R.string.month,
                        (absoluteAdapterPosition + 1).toString()
                    )

                    // set geospatial data
                    tvHumidity.text = data.averageHumidity.toString()
                    tvHujan.text = data.rain.toString()
                    tvTemp.text = data.averageTemp.toString()

                    if (data.averageHumidity!! > it.maxHumidity || data.averageHumidity < it.minHumidity) {
                        tvBulan.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.red_accent
                            )
                        )

                        tvStatusKeseluruhan.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.red_accent
                            )
                        )
                        tvStatusKeseluruhan.text =
                            itemView.context.getString(R.string.terdapat_peringatan)

                        icHumidity.setColorFilter(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.red_accent
                            )
                        )
                        tvHumidity.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.red_accent
                            )
                        )

                    }

                    if (data.rain!! > it.maxRain || data.rain < it.minRain) {

                        tvBulan.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.red_accent
                            )
                        )

                        tvStatusKeseluruhan.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.red_accent
                            )
                        )
                        tvStatusKeseluruhan.text =
                            itemView.context.getString(R.string.terdapat_peringatan)

                        icRain.setColorFilter(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.red_accent
                            )
                        )
                        tvHujan.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.red_accent
                            )
                        )

                    }

                    if (data.averageTemp!! > it.maxTemp || data.averageTemp < it.minTemp) {
                        tvBulan.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.red_accent
                            )
                        )

                        tvStatusKeseluruhan.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.red_accent
                            )
                        )
                        tvStatusKeseluruhan.text =
                            itemView.context.getString(R.string.terdapat_peringatan)

                        icTemp.setColorFilter(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.red_accent
                            )
                        )
                        tvTemp.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.red_accent
                            )
                        )
                    }
                }
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