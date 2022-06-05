package com.tandurteam.tandur.authentication.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.authentication.request.EmailCheckRequest
import com.tandurteam.tandur.databinding.FragmentInputEmailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class InputEmailFragment : Fragment() {

    private val viewModel: SignUpViewModel by viewModel()
    private var _binding: FragmentInputEmailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInputEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            // Join button validation
            btnJoinNow.setOnClickListener {
                if (etEmail.isNotEmpty) {
                    if (etEmail.isEmailValid) {
                        checkEmail()
                    } else etEmail.onEmailInvalid()
                } else etEmail.onFormEmpty()
            }

            // Sign in on click
            tvSignIn.setOnClickListener {
                val action = InputEmailFragmentDirections.navigateToLoginFragment(null)
                Navigation.findNavController(binding.root).navigate(action)
            }
        }
    }

    private fun checkEmail() {
        viewModel.checkUserEmail(EmailCheckRequest(binding.etEmail.text.toString())).observe(
            viewLifecycleOwner
        ) {
            it?.let {
                when (it) {
                    is ApiResponse.Loading -> {
                        setLoadingState(true)
                    }

                    is ApiResponse.Success -> {
                        setLoadingState(false)
                        val action = InputEmailFragmentDirections.navigateToSignUpFragment(
                            binding.etEmail.text.toString()
                        )
                        Navigation.findNavController(binding.root).navigate(action)
                    }

                    is ApiResponse.Error -> {
                        setLoadingState(false)
                        Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                    }


                    else -> {
                        setLoadingState(false)
                    }
                }
            }
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        val loadingState = if (isLoading) View.VISIBLE else View.GONE
        val viewState = if (isLoading) View.GONE else View.VISIBLE

        with(binding) {
            progressLoading.visibility = loadingState
            btnJoinNow.visibility = viewState
            tvSignIn.visibility = viewState
        }
    }
}