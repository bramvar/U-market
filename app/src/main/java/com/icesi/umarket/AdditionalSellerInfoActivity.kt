package com.icesi.umarket

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.icesi.umarket.databinding.ActivityAdditionalSellerInfoBinding
import com.icesi.umarket.model.Market
import com.icesi.umarket.model.Product
import java.util.*

class AdditionalSellerInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdditionalSellerInfoBinding
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdditionalSellerInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),::onGalleryResult)

        val id =intent.getStringExtra("ID")
        val phone = intent.getStringExtra("PHONE")
        val sellerName = intent.getStringExtra("SELLER_NAME")
        val email = intent.getStringExtra("EMAIL")
        val password = intent.getStringExtra("PASSWORD")

        binding.sellerSignupBtn.setOnClickListener {
            val i = Intent(this,SellerLoginActivity::class.java)

            val marketID = UUID.randomUUID().toString()
            val marketName = binding.signUpMarketNameTextField.text.toString()
            val marketDescription = binding.sellerInfoDescriptionTextField.text.toString()
            val fileName = UUID.randomUUID().toString()

            if(verifyBlankAdditionalInfoFields(marketName,marketDescription)){
                val seller = Seller(id!!,sellerName!!,email!!,password!!,phone!!,"Seller",marketID)

                val market = Market(marketID,id,marketName,marketDescription,fileName)

                Firebase.firestore.collection("users").document(id).set(seller).addOnSuccessListener {
                    Firebase.firestore.collection("markets").document(marketID).set(market).addOnSuccessListener {
                        Firebase.storage.getReference().child("market-image-profile").child(fileName).putFile(imageUri)
                        Toast.makeText(this.baseContext,"Usuario registrado. Inicie sesión", Toast.LENGTH_LONG).show()
                        startActivity(i)
                    }.addOnFailureListener {
                        Toast.makeText(this.baseContext,it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }else {
                Toast.makeText(this.baseContext,"Faltan campos por diligenciar", Toast.LENGTH_LONG).show()
            }
        }

        binding.newMarketImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            galleryLauncher.launch(intent)
        }

        binding.sellerAdditionalInfoBackBtn.setOnClickListener {
            finish()
        }
    }

    private fun onGalleryResult(activityResult: ActivityResult){
        if(activityResult.resultCode == RESULT_OK){
            imageUri= activityResult.data?.data!!
            binding.newMarketImage.setImageURI(imageUri)

            //image upload
        }
    }

    private fun verifyBlankAdditionalInfoFields(
        marketName: String,
        marketDescription: String,
    ): Boolean {
        return marketName.isNotBlank() && marketDescription.isNotBlank()
    }
}