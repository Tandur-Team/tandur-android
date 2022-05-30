package com.tandurteam.tandur.dashboard.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.detailuser.DetailUserRepository
import com.tandurteam.tandur.core.model.network.detailuser.response.userdetail.DetailUserResponse

class ProfileViewModel(private val detailUserRepository: DetailUserRepository) : ViewModel() {
    fun getDetailUser(): LiveData<ApiResponse<DetailUserResponse>> =
        detailUserRepository.getDetailUser().asLiveData()

    suspend fun clearUserToken() =
        detailUserRepository.clearUserToken()
}