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

    private fun getUserDetail() {
        viewModel.getDetailUser().observe(viewLifecycleOwner) {
            it?.let { detailUser ->
                when (detailUser) {
                    is ApiResponse.Success -> {
                        with(binding) {
                            val dataDetail = detailUser.data.data

                            // set views
                            tvEmailUser.text = dataDetail.email
                            tvNamaUser.text = dataDetail.fullName
                            tvJumlahTanamanUser.text = "Masih Belom Ada"
                            tvTingkatKepuasanUser.text = dataDetail.avgSatisfactionRate.toString()
                        }
                    }
                    else -> {
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

    companion object {
        private val TAG = ProfileFragment::class.java.simpleName
    }
}