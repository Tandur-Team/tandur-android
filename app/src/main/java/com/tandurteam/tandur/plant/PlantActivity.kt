package com.tandurteam.tandur.plant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tandurteam.tandur.databinding.ActivityPlantBinding

class PlantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}