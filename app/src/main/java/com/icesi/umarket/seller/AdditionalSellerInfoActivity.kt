package com.icesi.umarket.seller

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.icesi.umarket.databinding.ActivityAdditionalSellerInfoBinding
import com.icesi.umarket.model.Market
import com.icesi.umarket.model.Seller
import com.icesi.umarket.util.UtilDomi
import java.util.*

class AdditionalSellerInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdditionalSellerInfoBinding
    private lateinit var imageUri: Uri
    private lateinit var idImg: String
    private lateinit var idMarket: String

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
            val i = Intent(this, SellerLoginActivity::class.java)

            val marketID = UUID.randomUUID().toString()
            idMarket = marketID
            val marketName = binding.signUpMarketNameTextField.text.toString()
            val marketDescription = binding.sellerInfoDescriptionTextField.text.toString()
            //val fileName = UUID.randomUUID().toString()

            if(verifyBlankAdditionalInfoFields(marketName,marketDescription)){
                val seller = Seller(id!!,sellerName!!,email!!,password!!,phone!!,"Seller",marketID)

                var market = Market(marketID,id,marketName,marketDescription,"",phone)

                Firebase.firestore.collection("users").document(id).set(seller).addOnSuccessListener {
                    Firebase.firestore.collection("markets").document(marketID).set(market).addOnSuccessListener {
                        loadImgToMarket()
                        //Firebase.storage.getReference().child("market-image-profile").child("fileName").putFile(imageUri)
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
        ////binding.newMarketImage.setOnClickListener { val intent = Intent(Intent.ACTION_GET_CONTENT) intent.type = "image/*" galleryLauncher.launch(intent) }
        binding.sellerAdditionalInfoBackBtn.setOnClickListener {
            finish()
        }

        binding.cardView2.setOnClickListener{
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.type = "image/*"
            galleryLauncher.launch(i)
        }
    }

    private fun onGalleryResult(activityResult: ActivityResult){
        if(activityResult.resultCode == RESULT_OK) {
            val uri = activityResult.data?.data!!
            val pathI = UtilDomi.getPath(applicationContext, uri!!)
            val bitmap = BitmapFactory.decodeFile(pathI)
            binding.cardView2.setContentPadding(10, 10, 10, 10)
            binding.MarketImageProfile.setImageURI(uri)
            idImg = UUID.randomUUID().toString()
            Log.e("id Img:", idImg)
            Firebase.storage.reference.child("market-image-profile").child(idImg)
                .putFile(uri!!)
                .addOnSuccessListener {
                    Toast.makeText(this, "Imagen cargada", Toast.LENGTH_LONG).show()
                }
        }else{
            Toast.makeText(this, "No carga la imagen", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadImgToMarket(){
        Firebase.firestore.collection("markets").document(idMarket).update("imageID", idImg)
    }

    private fun verifyBlankAdditionalInfoFields(
        marketName: String,
        marketDescription: String,
    ): Boolean {
        return marketName.isNotBlank() && marketDescription.isNotBlank()
    }
}