package com.tandurteam.tandur.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tandurteam.tandur.core.model.network.myplant.response.myplantlist.MyPlantListData
import com.tandurteam.tandur.databinding.ItemMyPlantListBinding

class MyPlantListAdapter : RecyclerView.Adapter<MyPlantListAdapter.ViewHolder>() {

    private var _binding: ItemMyPlantListBinding? = null
    private val binding get() = _binding!!
    private var listData = ArrayList<MyPlantListData>()
    var onItemClick: ((MyPlantListData) -> Unit)? = null

    fun setData(newListData: List<MyPlantListData>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        listData.forEachIndexed { index, _ -> notifyItemChanged(index) }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemMyPlantListBinding.bind(itemView)

        init {
            binding.cardMyPlant.setOnClickListener { onItemClick?.invoke(listData[adapterPosition]) }
        }

        fun bind(data: MyPlantListData) {
            with(binding) {
                // TODO
//                Glide.with(itemView.context)
//                    .asBitmap()
//                    .load(IMAGE_BASE_URL + data.posterPath)
//                    .transform(FitCenter(), RoundedCorners(16))
//                    .into(ivTanaman)

                tvNamaTanaman.text = data.plantName
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
}