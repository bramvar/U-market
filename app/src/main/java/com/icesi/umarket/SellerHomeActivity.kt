package com.icesi.umarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.icesi.umarket.databinding.ActivitySellerHomeBinding

class SellerHomeActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySellerHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySellerHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}