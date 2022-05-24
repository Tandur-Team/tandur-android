package com.tandurteam.tandur.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tandurteam.tandur.core.model.network.fixedplant.response.fixed.FixedPlantData
import com.tandurteam.tandur.databinding.ItemHomeFirstBinding


class FixedPlantAdapter : RecyclerView.Adapter<FixedPlantAdapter.ViewHolder>() {

    private var _binding: ItemHomeFirstBinding? = null
    private val binding get() = _binding!!
    private val listData = ArrayList<FixedPlantData>()
    var onItemClick: ((FixedPlantData) -> Unit)? = null

    fun setData(newListData: List<FixedPlantData>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        listData.forEachIndexed { index, _ -> notifyItemChanged(index) }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemHomeFirstBinding.bind(itemView)

        init {
            binding.cardFixedPlant.setOnClickListener { onItemClick?.invoke(listData[adapterPosition]) }
        }

        fun bind(data: FixedPlantData) {
            with(binding) {
                Glide.with(itemView.context)
                    .asBitmap()
                    .load(data.imageUrl)
                    .transform(CenterCrop(), RoundedCorners(24))
                    .into(ivTanaman)

                tvNamaTanaman.text = data.plantName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = ItemHomeFirstBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fixedPlant = listData[position]
        holder.bind(fixedPlant)
    }

    override fun getItemCount(): Int = listData.size

}