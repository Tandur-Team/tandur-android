package com.tandurteam.tandur.core.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tandurteam.tandur.R
import com.tandurteam.tandur.core.model.network.myplant.response.myplantlist.MyPlantListData
import com.tandurteam.tandur.databinding.ItemMyPlantListBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

class MyPlantListAdapter : RecyclerView.Adapter<MyPlantListAdapter.ViewHolder>() {

    private var _binding: ItemMyPlantListBinding? = null
    private val binding get() = _binding!!
    private var listData = ArrayList<MyPlantListData>()
    var onItemClick: ((MyPlantListData) -> Unit)? = null

    fun setData(newListData: List<MyPlantListData>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        Log.d(TAG, "setData: $listData")
        listData.forEachIndexed { index, _ -> notifyItemChanged(index) }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemMyPlantListBinding.bind(itemView)

        init {
            binding.cardMyPlant.setOnClickListener { onItemClick?.invoke(listData[absoluteAdapterPosition]) }
        }

        fun bind(data: MyPlantListData) {
            with(binding) {
                Glide.with(itemView.context)
                    .asBitmap()
                    .load(data.imageUrl)
                    .transform(CenterCrop(), RoundedCorners(24))
                    .into(ivTanaman)

                tvNamaTanaman.text = data.plantName

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val nowDate = LocalDate.parse(
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                            System.currentTimeMillis()
                        ), pattern
                    )
                    val harvestDate = LocalDate.parse(data.plantHarvestDate, pattern)
                    val daysDifferent = Period.between(
                        nowDate.withDayOfMonth(1),
                        harvestDate.withDayOfMonth(1)
                    ).months
                    tvWaktu.text = itemView.context.getString(
                        R.string.different_date_time,
                        daysDifferent.toString()
                    )
                } else {
                    tvWaktu.text = data.plantHarvestDate
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = ItemMyPlantListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myPlant = listData[position]
        holder.bind(myPlant)
    }

    override fun getItemCount(): Int = listData.size

    companion object {
        private val TAG = MyPlantListAdapter::class.java.simpleName
    }
}