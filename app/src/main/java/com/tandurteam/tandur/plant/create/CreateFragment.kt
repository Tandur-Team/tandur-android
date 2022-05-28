package com.tandurteam.tandur.plant.create

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.tandurteam.tandur.R
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.dashboard.DashboardActivity
import com.tandurteam.tandur.databinding.FragmentCreateBinding
import com.tandurteam.tandur.maps.MapsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class CreateFragment : Fragment() {

    private val viewModel: CreateViewModel by viewModel()
    private val args: CreateFragmentArgs by navArgs()
    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!
    private lateinit var calendar: Calendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get user location
        getUserLocation()

        calendar = Calendar.getInstance()
        binding.etPlantDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, day)
                    updateLabel()
                    calendar.add(Calendar.MONTH, 7)
                    updateHarvestLabel()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        binding.etPlantingLocation.setOnClickListener {
            Intent(requireActivity(), MapsActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.tvPlantName.text = args.plantName

        binding.ivBack.setOnClickListener { requireActivity().onBackPressed() }

        binding.btnTanam.setOnClickListener { createPlant() }
    }

    private fun createPlant() {
        viewModel.createPlant(args.plantName).observe(viewLifecycleOwner) {
            it?.let { plantResponse ->
                when (plantResponse) {
                    is ApiResponse.Loading -> {
                        // set visibility
                        binding.progressLoading.visibility = View.VISIBLE
                        binding.btnTanam.visibility = View.GONE
                    }
                    is ApiResponse.Success -> {
                        // set visibility
                        binding.progressLoading.visibility = View.GONE
                        binding.btnTanam.visibility = View.VISIBLE

                        Log.d(TAG, "createPlant: ${plantResponse.data.data}")

                        // back to dashboard
                        Intent(requireActivity(), DashboardActivity::class.java).apply {
                            Toast.makeText(
                                requireContext(),
                                "${args.plantName} berhasil ditanam",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(this)
                            requireActivity().finish()
                        }
                    }
                    else -> {
                        // set visibility
                        binding.progressLoading.visibility = View.GONE
                        binding.btnTanam.visibility = View.VISIBLE

                        Toast.makeText(
                            requireContext(),
                            "Terdapat kesalahan saat menghubungkan ke server",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun getUserLocation() {
        viewModel.getUserLocation().observe(viewLifecycleOwner) {
            it?.let { userLocation ->
                binding.etPlantingLocation.setText(
                    requireContext().getString(
                        R.string.location_info,
                        userLocation.subZone,
                        userLocation.city
                    )
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateLabel() {
        val date = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(calendar.time)
        Log.d(TAG, "updateLabel: $calendar.time")
        binding.etPlantDate.setText(date)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateHarvestLabel() {
        val date = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(calendar.time)
        Log.d(TAG, "updateLabel: $calendar.time")
        binding.etEstimatedHarvestTime.setText(date)
    }

    companion object {
        private val TAG = CreateFragment::class.java.simpleName
    }
}