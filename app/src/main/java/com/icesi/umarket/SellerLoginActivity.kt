package com.icesi.umarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.icesi.umarket.databinding.ActivitySellerLoginBinding

class SellerLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySellerLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySellerLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpHyperLink.setOnClickListener {
            startActivity(Intent(this,SellerSignupActivity::class.java))
        }
        binding.sellerLoginGoBackBtn.setOnClickListener {
            finish()
        }
    }
}