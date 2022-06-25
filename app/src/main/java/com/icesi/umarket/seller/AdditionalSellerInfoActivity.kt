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
import com.google.gson.Gson
import com.icesi.umarket.databinding.ActivityAdditionalSellerInfoBinding
import com.icesi.umarket.model.Market
import com.icesi.umarket.model.Seller
import com.icesi.umarket.model.User
import com.icesi.umarket.util.Util
import com.icesi.umarket.util.UtilDomi
import java.util.*

class AdditionalSellerInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdditionalSellerInfoBinding
    private lateinit var currentUser: Seller
    private lateinit var idImg: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdditionalSellerInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),::onGalleryResult)
        currentUser = Gson().fromJson(intent.extras?.getString("currentUser",""), Seller::class.java)

        binding.sellerSignupBtn.setOnClickListener {
            sendSeller()
        }

        binding.sellerAdditionalInfoBackBtn.setOnClickListener {
            finish()
        }

        binding.cardView2.setOnClickListener{
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.type = "image/*"
            galleryLauncher.launch(i)
        }
    }

    private fun sendSeller(){
        val i = Intent(this, SellerLoginActivity::class.java)
        val marketName = binding.signUpMarketNameTextField.text.toString()
        val marketShortDescription = binding.marketShortDescriptionTextField.text.toString()
        val marketDescription = binding.sellerInfoDescriptionTextField.text.toString()
        currentUser.marketID = UUID.randomUUID().toString()

        if(verifyBlankAdditionalInfoFields(marketName,marketDescription)){
            var market = Market(currentUser.marketID,currentUser.id,marketName,marketDescription,marketShortDescription,"",currentUser.phone)

            Firebase.firestore.collection("users").document(currentUser.id).set(currentUser).addOnSuccessListener {
                Firebase.firestore.collection("markets").document(currentUser.marketID).set(market).addOnSuccessListener {
                    loadImgToMarket()
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

    private fun onGalleryResult(activityResult: ActivityResult){
        if(activityResult.resultCode == RESULT_OK) {
            val uri = activityResult.data?.data!!
            binding.cardView2.setContentPadding(10, 10, 10, 10)
            binding.marketImageProfile.setImageURI(uri)
            idImg = UUID.randomUUID().toString()

           if(Util.sendImg(idImg,"market-image-profile", uri)){
               Toast.makeText(this, "Imagen cargada", Toast.LENGTH_LONG).show()
           }

        }else{
            Toast.makeText(this, "No carga la imagen", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadImgToMarket(){
        Firebase.firestore.collection("markets").document(currentUser.marketID).update("imageID", idImg)
    }

    private fun verifyBlankAdditionalInfoFields(
        marketName: String,
        marketDescription: String,
    ): Boolean {
        return marketName.isNotBlank() && marketDescription.isNotBlank()
    }
}