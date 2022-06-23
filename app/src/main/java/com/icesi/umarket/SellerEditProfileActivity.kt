package com.icesi.umarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.icesi.umarket.databinding.ActivityConsumerEditProfileBinding
import com.icesi.umarket.databinding.ActivitySellerEditProfileBinding
import com.icesi.umarket.model.Market
import com.icesi.umarket.model.Seller
import com.icesi.umarket.seller.SellerHomeActivity

class SellerEditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySellerEditProfileBinding
    private lateinit var seller: Seller
    private lateinit var market: Market


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerEditProfileBinding.inflate(layoutInflater)

        seller = Gson().fromJson(intent.extras!!.getString("currentSeller", ""), Seller::class.java)

        Firebase.firestore.collection("markets").document(seller.marketID).get()
            .addOnSuccessListener {
                market = it.toObject(Market::class.java)!!
                loadInfo()
            }

        binding.editMarketDoneBtn.setOnClickListener {
            market.marketName = binding.nameMarketEdit.text.toString()
            market.phoneNumber = binding.phoneMarketEdit.text.toString()
            market.marketDescription = binding.descriptionMarketEdit.text.toString()

            Firebase.firestore.collection("markets").document(seller.marketID).set(market)

            /**
             * var password = binding.passwordSellerEdit.text.toString()
            Firebase.firestore.collection("users").document(seller.id).update("password",password)
            Firebase.auth.
             */

            startActivity(
                Intent(this, SellerHomeActivity::class.java).apply {
                putExtra("currentUser", Gson().toJson(seller))
            })
        }

        setContentView(binding.root)
    }

    private fun loadInfo() {
        binding.nameMarketEdit.setText(market.marketName)
        binding.descriptionMarketEdit.setText(market.marketDescription)
        binding.passwordSellerEdit.setText(seller.password)
        binding.phoneMarketEdit.setText(market.phoneNumber)
    }
}