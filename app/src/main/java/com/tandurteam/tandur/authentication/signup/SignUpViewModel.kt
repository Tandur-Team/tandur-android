package com.tandurteam.tandur.authentication.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.authentication.AuthRepository
import com.tandurteam.tandur.core.model.network.authentication.request.EmailCheckRequest
import com.tandurteam.tandur.core.model.network.authentication.request.SignUpRequest
import com.tandurteam.tandur.core.model.network.authentication.response.EmailCheckResponse
import com.tandurteam.tandur.core.model.network.authentication.response.SignUpResponse

class SignUpViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun checkUserEmail(emailCheckRequest: EmailCheckRequest): LiveData<ApiResponse<EmailCheckResponse>> =
        authRepository.checkUserEmail(emailCheckRequest).asLiveData()

    fun signUpUser(signUpRequest: SignUpRequest): LiveData<ApiResponse<SignUpResponse>> =
        authRepository.signUpUser(signUpRequest).asLiveData()
}