package com.icesi.umarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.icesi.umarket.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),::onResult)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectConsumerBtn.setOnClickListener {
            changeActivity("consumer")
        }

        binding.selectSellerBtn.setOnClickListener {
            changeActivity("seller")
        }
    }

    private fun changeActivity(userType: String){
        val intent = Intent(this, SellerLoginActivity::class.java).apply{
            putExtra("userType", userType)
        }
        launcher.launch(intent)
    }

    fun onResult(activityResult: ActivityResult?) {}
}
