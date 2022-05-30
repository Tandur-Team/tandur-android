package com.tandurteam.tandur.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tandurteam.tandur.R
import com.tandurteam.tandur.core.model.network.nearbyplant.response.nearby.NearbyPlantData
import com.tandurteam.tandur.databinding.ItemHomeSuggestionBinding

class NearbyPlantAdapter : RecyclerView.Adapter<NearbyPlantAdapter.ViewHolder>() {

    private var _binding: ItemHomeSuggestionBinding? = null
    private val binding get() = _binding!!
    private val listData = ArrayList<NearbyPlantData>()
    var onItemClick: ((NearbyPlantData) -> Unit)? = null

    fun setData(newListData: List<NearbyPlantData>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        listData.forEachIndexed { index, _ -> notifyItemChanged(index) }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemHomeSuggestionBinding.bind(itemView)

        init {
            binding.cardNearbyPlant.setOnClickListener { onItemClick?.invoke(listData[absoluteAdapterPosition]) }
        }

        fun bind(data: NearbyPlantData){
            with(binding){
                Glide.with(itemView.context)
                    .asBitmap()
                    .load(data.imageUrl) // TODO
                    .transform(CenterCrop(), RoundedCorners(24))
                    .into(ivTanaman)

                tvNamaTanaman.text = data.plantName
                tvJumlahUser.text = data.nearby.toString()
                tvStatus.text = data.avgSatisfactionRate.toString()
                tvWaktu.text = itemView.context.getString(R.string.harvest_duration, data.harvestDuration.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = ItemHomeSuggestionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nearbyPlant = listData[position]
        holder.bind(nearbyPlant)
    }

    override fun getItemCount(): Int = listData.size


}