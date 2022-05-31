package com.tandurteam.tandur.plant.myplantdetail

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.tandurteam.tandur.R
import com.tandurteam.tandur.core.adapter.MonthlyLocationConditionAdapter
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.dashboard.DashboardActivity
import com.tandurteam.tandur.databinding.DialogHarvestingBinding
import com.tandurteam.tandur.databinding.FragmentMyPlantDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyPlantDetailFragment : Fragment() {

    private val viewModel: MyPlantDetailViewModel by viewModel()
    private val navArg: MyPlantDetailFragmentArgs by navArgs()
    private var _binding: FragmentMyPlantDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MonthlyLocationConditionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMyPlantDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // hide bottom nav
        (requireActivity() as DashboardActivity).setBottomNavVisibility(false)

        // init adapter
        adapter = MonthlyLocationConditionAdapter()

        // set plant name by nav args
        binding.tvNamaTanamanDetail.text = navArg.plantName

        // on harvest clicked
        binding.btnHarvest.setOnClickListener {
            showHarvestDialog()
        }

        binding.swipeRefresh.setOnRefreshListener {
            observeLiveData(isHarvesting = false)
        }

        // observe
        observeLiveData(isHarvesting = false)

        // on back pressed
        binding.ivBack.setOnClickListener { requireActivity().onBackPressed() }
    }

    private fun setLoadingState(isLoading: Boolean) {
        with(binding) {
            swipeRefresh.isRefreshing = isLoading

            val visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
            tvStatusDetail.visibility = visibility
            tvStatusPeringatan.visibility = visibility
            rvStatus.visibility = visibility
            icDate.visibility = visibility
            tvDate.visibility = visibility
            tvUserDate.visibility = visibility
            icTime.visibility = visibility
            tvDurasi.visibility = visibility
            tvUserDurasi.visibility = visibility
            icLocation.visibility = visibility
            tvLocation.visibility = visibility
            tvUserLocation.visibility = visibility
            vLine4.visibility = visibility
            vLine5.visibility = visibility
            vLine6.visibility = visibility

        }
    }

    private fun observeLiveData(satisfactionRate: Int = -1, isHarvesting: Boolean) {
        val viewModelFunction = if (isHarvesting) {
            viewModel.harvestPlant(navArg.plantId, satisfactionRate)
        } else {
            viewModel.getMyPlantDetail(navArg.plantName, navArg.plantId)
        }

        viewModelFunction.observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result) {
                    is ApiResponse.Loading -> {
                        setLoadingState(true)
                    }

                    is ApiResponse.Success -> {
                        with(binding) {
                            val resultData = result.data.data
                            setLoadingState(false)

                            // set view
                            tvStatusDetail.text = resultData.probability.toString()
                            tvUserDate.text = resultData.plantStartDate
                            tvUserDurasi.text = resultData.plantHarvestDate
                            tvUserLocation.text = requireContext().getString(
                                R.string.location_info,
                                resultData.zoneLocal,
                                resultData.zoneCity
                            )
                            Glide.with(requireContext())
                                .asBitmap()
                                .load(resultData.imageUrl)
                                .into(ivTanamanDetail)

                            // check is harvested
                            if (resultData.isHarvested == 1) {
                                btnHarvest.visibility = View.GONE
                                ivSatisfactionFace.visibility = View.VISIBLE

                                // check satisfaction rate
                                if (resultData.satisfactionRate == 0) {
                                    ivSatisfactionFace.setImageResource(R.drawable.ic_sad)
                                    ivSatisfactionFace.imageTintList = ColorStateList.valueOf(
                                        ContextCompat.getColor(requireContext(), R.color.red_accent)
                                    )
                                } else if (resultData.satisfactionRate in (1..99)) {
                                    ivSatisfactionFace.setImageResource(R.drawable.ic_neutral)
                                    ivSatisfactionFace.imageTintList = ColorStateList.valueOf(
                                        ContextCompat.getColor(requireContext(), R.color.red_accent)
                                    )
                                }
                            }

                            // set adapter
                            Log.d(TAG, "getMyPlantPlantDetail: ${resultData.monthlyData}")
                            adapter.setData(resultData.monthlyData)
                            rvStatus.adapter = adapter
                        }
                    }
                    is ApiResponse.Error -> {
                        setLoadingState(false)
                        Toast.makeText(
                            requireContext(),
                            result.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(TAG, "observeLiveData: ${result.errorMessage}")
                    }
                    else -> {
                        setLoadingState(false)
                        Toast.makeText(
                            requireContext(),
                            "Terdapat kesalahan saat menghubungkan ke server.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun showHarvestDialog() {
        val dialog = Dialog(requireContext())
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(true)
            val dialogBinding = DialogHarvestingBinding.inflate(
                LayoutInflater.from(requireContext())
            )
            setContentView(dialogBinding.root)

            // init satisfaction rate
            var satisfactionRate = -1

            // set view
            with(dialogBinding) {
                ivSadFace.setOnClickListener {
                    satisfactionRate = 0
                    setEmotionColor(ivSadFace, ivHappyFace, ivNeutralFace)
                    setTextEmotionColor(tvSad, tvHappy, tvNeutral)
                }

                ivNeutralFace.setOnClickListener {
                    satisfactionRate = 50
                    setEmotionColor(ivNeutralFace, ivSadFace, ivHappyFace)
                    setTextEmotionColor(tvNeutral, tvSad, tvHappy)
                }

                ivHappyFace.setOnClickListener {
                    satisfactionRate = 100
                    setEmotionColor(ivHappyFace, ivSadFace, ivNeutralFace)
                    setTextEmotionColor(tvHappy, tvSad, tvNeutral)
                }

                tvBack.setOnClickListener { dismiss() }

                btnConfirmHarvest.setOnClickListener {
                    if (satisfactionRate >= 0) {
                        dismiss()
                        observeLiveData(satisfactionRate, true)
                    }
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

    companion object {
        private val TAG = MyPlantDetailFragment::class.java.simpleName
    }
}