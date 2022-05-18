package com.tandurteam.tandur.authentication.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.authentication.request.LoginRequest
import com.tandurteam.tandur.dashboard.DashboardActivity
import com.tandurteam.tandur.databinding.FragmentLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModel()
    private val navArgs: LoginFragmentArgs by navArgs()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            // On back pressed
            tvBack.setOnClickListener { requireActivity().onBackPressed() }

            // Set email from nav args
            etEmail.setText(navArgs.email)

            // On login clicked
            btnSignIn.setOnClickListener {
                // Set login request
                val loginRequest = LoginRequest(
                    etEmail.text.toString(),
                    etPassword.text.toString()
                )

                if (validateForm()) {
                    viewModel.loginUser(loginRequest).observe(viewLifecycleOwner) {
                        it?.let { response ->
                            when (response) {
                                is ApiResponse.Loading -> {
                                    progressLoading.visibility = View.VISIBLE
                                    tvBack.visibility = View.GONE
                                }

                                is ApiResponse.Success -> {
                                    // Toast message success
                                    Toast.makeText(
                                        requireContext(),
                                        "Berhasil Login",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    // Move to dashboard
                                    Intent(requireActivity(), DashboardActivity::class.java).apply {
                                        requireActivity().startActivity(this)
                                    }
                                }

                                else -> {
                                    // Toast message error
                                    Toast.makeText(
                                        requireContext(),
                                        "Gagal melakukan login, silakan ulangi kembali",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    // Hide loading and show button
                                    progressLoading.visibility = View.GONE
                                    tvBack.visibility = View.VISIBLE
                                }

                            }
                        }
                    }
                }

            }
        }
    }

    private fun validateForm(): Boolean {
        with(binding) {
            if (!etEmail.isNotEmpty) {
                etEmail.onFormEmpty()
                return false
            } else if (!etPassword.isNotEmpty) {
                etPassword.onFormEmpty()
                return false
            }
        }
        return true
    }
}