package com.tandurteam.tandur.authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tandurteam.tandur.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {

    private var _binding: ActivityAuthenticationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}