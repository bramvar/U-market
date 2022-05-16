package com.icesi.umarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.icesi.umarket.databinding.ActivityAdditionalSellerInfoBinding

class AdditionalSellerInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdditionalSellerInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdditionalSellerInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id =intent.getStringExtra("ID")
        val marketName = intent.getStringExtra("MARKET_NAME")
        val sellerName = intent.getStringExtra("SELLER_NAME")
        val email = intent.getStringExtra("EMAIL")
        val password = intent.getStringExtra("PASSWORD")

        binding.sellerSignupBtn.setOnClickListener {
            val i = Intent(this,SellerLoginActivity::class.java)

            val sellerPhone = binding.sellerInfoPhoneTextField.text.toString()
            val marketDescription = binding.sellerInfoDescriptionTextField.text.toString()

            if(verifyBlankAdditionalInfoFields(sellerPhone,marketDescription)){
                val seller = Seller(id!!,marketName!!,sellerName!!,email!!,password!!,sellerPhone,marketDescription,"Seller")
                Firebase.firestore.collection("users").document(id).set(seller).addOnSuccessListener {
                    Toast.makeText(this.baseContext,"Usuario registrado. Inicie sesión", Toast.LENGTH_LONG).show()
                    startActivity(i)
                }
            }else {
                Toast.makeText(this.baseContext,"Faltan campos por diligenciar", Toast.LENGTH_LONG).show()
            }
        }

        binding.sellerAdditionalInfoBackBtn.setOnClickListener {
            finish()
        }
    }

    private fun verifyBlankAdditionalInfoFields(
        sellerPhone: String,
        marketDescription: String,
    ): Boolean {
        return sellerPhone.isNotBlank() && marketDescription.isNotBlank()
    }
}