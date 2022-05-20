package com.tandurteam.tandur

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tandurteam.tandur.authentication.AuthenticationActivity
import com.tandurteam.tandur.dashboard.DashboardActivity
import com.tandurteam.tandur.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        runBlocking {
            lifecycleScope.launch {
                delay(1000)
                checkUserToken()
            }
        }
    }

    private fun checkUserToken() {
        viewModel.getUserToken().observe(this) {
            it?.let {
                // go to dashboard
                Intent(this@MainActivity, DashboardActivity::class.java).apply {
                    startActivity(this)
                }.also { finish() }
            } ?: run {
                // go to authentication
                Intent(this@MainActivity, AuthenticationActivity::class.java).apply {
                    startActivity(this)
                }.also { finish() }
            }
        }
    }
}