package com.tandurteam.tandur.authentication.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.authentication.request.SignUpRequest
import com.tandurteam.tandur.databinding.FragmentSignUpBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment : Fragment() {

    private val viewModel: SignUpViewModel by viewModel()
    private val navArgs: SignUpFragmentArgs by navArgs()
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            // on back pressed
            tvBack.setOnClickListener { requireActivity().onBackPressed() }

            // set email from nav args
            etEmail.setText(navArgs.email)

            // on sign up pressed
            btnSignUp.setOnClickListener {
                // set sign up request
                val signUpRequest = SignUpRequest(
                    etFullName.text.toString(),
                    etEmail.text.toString(),
                    etPassword.text.toString()
                )

                // sign up user if all form are not empty
                if (validateForm()) {
                    viewModel.signUpUser(signUpRequest).observe(viewLifecycleOwner) {
                        it?.let { response ->
                            when (response) {
                                is ApiResponse.Loading -> {
                                    progressLoading.visibility = View.VISIBLE
                                    btnSignUp.visibility = View.GONE
                                    tvBack.visibility = View.GONE
                                }

                                is ApiResponse.Success -> {
                                    // show success message
                                    Toast.makeText(
                                        requireContext(),
                                        "${response.data.data?.name} Berhasil didaftarkan",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    // go to login
                                    val action =
                                        SignUpFragmentDirections.navigateSignUpFragmentToLoginFragment(
                                            etEmail.text.toString()
                                        )
                                    Navigation.findNavController(binding.root).navigate(action)
                                }

                                else -> {
                                    // show error message
                                    Toast.makeText(
                                        requireContext(),
                                        "Gagal melakukan registrasi, silakan ulangi lagi",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    // hide loading and show buttons
                                    progressLoading.visibility = View.GONE
                                    btnSignUp.visibility = View.VISIBLE
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
            } else if (!etFullName.isNotEmpty) {
                etFullName.onFormEmpty()
                return false
            } else if (!etPassword.isNotEmpty) {
                etPassword.onFormEmpty()
                return false
            } else if (!etEmail.isEmailValid) {
                etEmail.onEmailInvalid()
                return false
            } else if (!etPassword.isPasswordValid) {
                etPassword.onPasswordInvalid()
                return false
            }
        }

        return true
    }
}