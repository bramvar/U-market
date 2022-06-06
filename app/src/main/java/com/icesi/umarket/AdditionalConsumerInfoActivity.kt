package com.icesi.umarket

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.icesi.umarket.databinding.ActivityAdditionalConsumerInfoBinding
import com.icesi.umarket.model.User
import com.icesi.umarket.util.UtilDomi
import com.squareup.okhttp.Dispatcher
import java.util.*

class AdditionalConsumerInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdditionalConsumerInfoBinding
    private var changeImage: Boolean = false
    private lateinit var userObj: User
    private lateinit var idImg: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdditionalConsumerInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onResult)

        binding.consumerFinishRegistButtom.setOnClickListener {
            if (validateData()) {
                saveUser()
                val intent = Intent(this, ConsumerHomeActivity::class.java)
                launcher.launch(intent)
            }
        }

        binding.ConsumerBackPhotoButtom.setOnClickListener {
            finish()
        }

        val galLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ::onGalleryResult
        )

        binding.cardView.setOnClickListener {
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.type = "image/*"
            galLauncher.launch(i)
        }
        requestPermissions(arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ),1)

        binding.ConsumerPhoneText.setOnClickListener {
            changeToNormalText(binding.ConsumerPhoneText)
            binding.phoneMsg.isVisible = false
        }
    }

    private fun validateData(): Boolean {
        var isCorrect: Boolean = true;
        var phone = binding.ConsumerPhoneText.text.toString()
        if ( phone == "" || phone.length < 10) {
            changeToErrorText(binding.ConsumerPhoneText)
            binding.phoneMsg.isVisible = true
            isCorrect = false
        }else{
            var gson = Gson()
            ///Create method to save img in the Storage
            userObj = gson.fromJson<User>(intent.extras?.getString("userObj","").toString(),User::class.java)
            userObj.phone = phone
        }
        return isCorrect
    }

    private fun changeToErrorText(text: android.widget.EditText) {
        text.setBackgroundResource(R.drawable.shaping_plain_text_error)
    }

    private fun changeToNormalText(text: android.widget.EditText) {
        text.setBackgroundResource(R.drawable.shaping_plain_text)
    }

    private fun onGalleryResult(activityResult: ActivityResult) {
        if(activityResult.resultCode == RESULT_OK) {
            val uri = activityResult.data?.data!!
            val pathI = UtilDomi.getPath(applicationContext, uri!!)
            val bitmap = BitmapFactory.decodeFile(pathI)
            binding.cardView.setContentPadding(10, 10, 10, 10)
            binding.consumerProfileImage.setImageURI(uri)
            changeImage = true;
            idImg = UUID.randomUUID().toString()

            Firebase.storage.reference.child("profile").child(idImg.toString())
                .putFile(uri!!)
                .addOnSuccessListener {
                    Toast.makeText(this, "Imagen cargada", Toast.LENGTH_LONG).show()
                }
        }else{
            Toast.makeText(this, "No carga la imagen", Toast.LENGTH_LONG).show()
        }
    }
    private fun saveUser(){
        val sp = getSharedPreferences("u-market", MODE_PRIVATE)
        val json = Gson().toJson(userObj)
        sp.edit().putString("user",json).apply()
        Firebase.firestore.collection("users").document(userObj.id).update("img", idImg)
    }

    fun onResult(activityResult: ActivityResult?) {

    }
}
