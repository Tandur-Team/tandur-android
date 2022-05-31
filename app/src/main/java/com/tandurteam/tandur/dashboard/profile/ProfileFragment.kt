package com.tandurteam.tandur.dashboard.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tandurteam.tandur.MainActivity
import com.tandurteam.tandur.R
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.dashboard.DashboardActivity
import com.tandurteam.tandur.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModel()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // on refresh swiped
        binding.swipeRefresh.setOnRefreshListener {
            getUserDetail()
        }

        // set user detail
        getUserDetail()

        with(binding) {
            // logout user
            btnLogout.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.clearUserToken()
                    Intent(requireActivity(), MainActivity::class.java).apply {
                        requireActivity().startActivity(this)
                        requireActivity().finish()
                    }
                }
            }
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        with(binding) {
            swipeRefresh.isRefreshing = isLoading

            tvEmailUser.text = requireContext().getString(R.string.please_wait)
            tvNamaUser.text = requireContext().getString(R.string.please_wait)
            tvJumlahTanamanUser.text = requireContext().getString(R.string.please_wait)
            tvTingkatKepuasanUser.text = requireContext().getString(R.string.please_wait)
        }
    }

    private fun getUserDetail() {
        viewModel.getDetailUser().observe(viewLifecycleOwner) {
            it?.let { detailUser ->
                when (detailUser) {
                    is ApiResponse.Loading -> {
                        setLoadingState(true)
                    }

                    is ApiResponse.Success -> {
                        with(binding) {
                            val dataDetail = detailUser.data.data
                            setLoadingState(false)

                            // set views
                            tvEmailUser.text = dataDetail.email
                            tvNamaUser.text = dataDetail.fullName
                            tvJumlahTanamanUser.text = dataDetail.activePlants.toString()
                            tvTingkatKepuasanUser.text = dataDetail.avgSatisfactionRate.toString()
                        }
                    }
                    is ApiResponse.Error -> {
                        setLoadingState(false)
                        Toast.makeText(
                            requireContext(),
                            detailUser.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(TAG, "$detailUser")
                    }
                    else -> {
                        setLoadingState(false)
                        Toast.makeText(
                            requireContext(),
                            "Terdapat kesalahan saat menghubungkan ke server",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(TAG, "$detailUser")
                    }
                }
            }

        }
    }

    override fun onPause() {
        super.onPause()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        // show bottom nav
        (requireActivity() as DashboardActivity).setBottomNavVisibility(true)

        // get user location info
        getUserDetail()
    }

    companion object {
        private val TAG = ProfileFragment::class.java.simpleName
    }
}