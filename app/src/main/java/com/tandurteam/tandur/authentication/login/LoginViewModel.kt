package com.tandurteam.tandur.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.authentication.AuthRepository
import com.tandurteam.tandur.core.model.network.authentication.request.LoginRequest
import com.tandurteam.tandur.core.model.network.authentication.response.LoginResponse

class LoginViewModel(private val authRepository: AuthRepository): ViewModel() {
    fun loginUser(loginRequest: LoginRequest): LiveData<ApiResponse<LoginResponse>> =
        authRepository.loginUser(loginRequest).asLiveData()
}