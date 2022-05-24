package com.tandurteam.tandur.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
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
            binding.cardNearbyPlant.setOnClickListener { onItemClick?.invoke(listData[adapterPosition]) }
        }

        fun bind(data: NearbyPlantData){
            with(binding){
                Glide.with(itemView.context)
                    .asBitmap()
                    .load(DUMMY_IMAGE_URL) // TODO
                    .transform(CenterCrop(), RoundedCorners(24))
                    .into(ivTanaman)

                tvNamaTanaman.text = data.plantName
                tvJumlahUser.text = data.isHarvested.toString()
                tvStatus.text = data.satisfactionRate.toString()
                tvWaktu.text = data.plantHarvestDate
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

    companion object {
        private const val DUMMY_IMAGE_URL =
            "https://images.unsplash.com/photo-1592997572594-34be01bc36c7?crop=entropy&cs=tinysrgb&fm=jpg&ixlib=rb-1.2.1&q=80&raw_url=true&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170"
    }
}