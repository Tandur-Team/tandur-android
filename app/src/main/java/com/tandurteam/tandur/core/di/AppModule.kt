package com.tandurteam.tandur.core.di

import com.tandurteam.tandur.MainViewModel
import com.tandurteam.tandur.authentication.login.LoginViewModel
import com.tandurteam.tandur.authentication.signup.SignUpViewModel
import com.tandurteam.tandur.core.helper.SharedPreferences
import com.tandurteam.tandur.core.model.network.ApiService
import com.tandurteam.tandur.core.model.network.authentication.AuthRepository
import com.tandurteam.tandur.core.model.network.fixedplant.FixedPlantRepository
import com.tandurteam.tandur.core.model.network.myplant.MyPlantRepository
import com.tandurteam.tandur.core.model.network.nearbyplant.NearbyPlantRepository
import com.tandurteam.tandur.core.model.network.plantdetail.PlantDetailRepository
import com.tandurteam.tandur.dashboard.home.HomeViewModel
import com.tandurteam.tandur.dashboard.myplantlist.MyPlantViewModel
import com.tandurteam.tandur.dashboard.profile.ProfileViewModel
import com.tandurteam.tandur.plant.detail.PlantDetailViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
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

val dataStoreModule = module {
    single {
        SharedPreferences(androidContext())
    }
}

val repositoryModule = module {
    factory { AuthRepository(get(), get()) }
    factory { MyPlantRepository(get(), get()) }
    factory { FixedPlantRepository(get()) }
    factory { NearbyPlantRepository(get(), get()) }
    factory { PlantDetailRepository(get(), get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { MyPlantViewModel(get()) }
    viewModel { PlantDetailViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
}