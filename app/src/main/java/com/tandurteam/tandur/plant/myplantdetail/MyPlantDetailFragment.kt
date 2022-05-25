package com.tandurteam.tandur.plant.myplantdetail

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tandurteam.tandur.R
import com.tandurteam.tandur.dashboard.DashboardActivity
import com.tandurteam.tandur.databinding.DialogHarvestingBinding
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
            showHarvestDialog()
        }

        // hide bottom nav
        (requireActivity() as DashboardActivity).setBottomNavVisibility(false)
    }

    private fun showHarvestDialog() {
        val dialog = Dialog(requireContext())
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(true)
            val dialogBinding = DialogHarvestingBinding.inflate(
                LayoutInflater.from(requireContext())
            )
            setContentView(dialogBinding.root)

            // set view
            with(dialogBinding) {
                ivSadFace.setOnClickListener {
                    setEmotionColor(ivSadFace, ivHappyFace, ivNeutralFace)
                    setTextEmotionColor(tvSad, tvHappy, tvNeutral)
                }

                ivNeutralFace.setOnClickListener {
                    setEmotionColor(ivNeutralFace, ivSadFace, ivHappyFace)
                    setTextEmotionColor(tvNeutral, tvSad, tvHappy)
                }

                ivHappyFace.setOnClickListener {
                    setEmotionColor(ivHappyFace, ivSadFace, ivNeutralFace)
                    setTextEmotionColor(tvHappy, tvSad, tvNeutral)
                }

                tvBack.setOnClickListener { dismiss() }

                btnConfirmHarvest.setOnClickListener {
                    Toast.makeText(requireContext(), "Coming Soon!", Toast.LENGTH_SHORT).show()
                }
            }

            show()
        }
    }

    private fun setEmotionColor(
        clickedImage: ImageView,
        vararg otherImages: ImageView
    ) {
        clickedImage.imageTintList = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), R.color.green_accent)
        )

        otherImages.forEach { otherImage ->
            otherImage.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.dark_accent)
            )
        }
    }

    private fun setTextEmotionColor(
        clickedText: TextView,
        vararg otherTexts: TextView
    ) {
        clickedText.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_accent))

        otherTexts.forEach { otherText ->
            otherText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_accent))
        }
    }
}