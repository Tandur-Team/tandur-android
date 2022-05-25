package com.tandurteam.tandur.plant.myplantdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tandurteam.tandur.dashboard.DashboardActivity
import com.tandurteam.tandur.databinding.FragmentMyPlantDetailBinding

class MyPlantDetailFragment : Fragment() {

    private var _binding: FragmentMyPlantDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMyPlantDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // on harvest clicked
        binding.btnHarvest.setOnClickListener {

        }

        // hide bottom nav
        (requireActivity() as DashboardActivity).setBottomNavVisibility(false)
    }
}