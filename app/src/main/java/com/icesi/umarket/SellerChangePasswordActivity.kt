package com.icesi.umarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.icesi.umarket.databinding.ActivitySellerChangePasswordBinding

class SellerChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySellerChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}