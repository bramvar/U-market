package com.icesi.umarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.icesi.umarket.databinding.ActivitySellerSignupBinding

class SellerSignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySellerSignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySellerSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sellerLogInHyperLink.setOnClickListener {
            finish()
        }
        binding.sellerSignupGoBackBtn.setOnClickListener {
            finish()
        }
    }
}