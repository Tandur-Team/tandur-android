package com.tandurteam.tandur.dashboard.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tandurteam.tandur.MainActivity
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

            val visibility = if (isLoading) View.GONE else View.VISIBLE
            tvEmailUser.visibility = visibility
            tvNamaUser.visibility = visibility
            tvJumlahTanamanUser.visibility = visibility
            tvTingkatKepuasanUser.visibility = visibility
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
                            tvJumlahTanamanUser.text = "Masih Belom Ada"
                            tvTingkatKepuasanUser.text = dataDetail.avgSatisfactionRate.toString()
                        }
                    }
                    else -> {
                        setLoadingState(false)
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