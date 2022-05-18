package com.tandurteam.tandur.core.di

import com.tandurteam.tandur.authentication.signup.SignUpViewModel
import com.tandurteam.tandur.core.model.network.ApiService
import com.tandurteam.tandur.core.model.network.authentication.AuthRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://tandur-backend-j4rjxcgo5a-et.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    factory { AuthRepository(get()) }
}

val viewModelModule = module {
    viewModel { SignUpViewModel(get()) }
}