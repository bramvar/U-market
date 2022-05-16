package com.icesi.umarket

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.icesi.umarket.databinding.ActivityConsumerRegistrationPhotoBinding
import com.icesi.umarket.model.User
import com.icesi.umarket.util.UtilDomi

class AdditionalConsumerInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConsumerRegistrationPhotoBinding
    private var changeImage: Boolean = false
    private lateinit var userObj: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsumerRegistrationPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onResult)

        binding.consumerFinishRegistButtom.setOnClickListener {
            if (validateData()) {
                sendDataToDB()
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

        binding.ConsumerPhoneText.setOnClickListener {
            changeToNormalText(binding.ConsumerPhoneText)
            binding.phoneMsg.isVisible = false
        }
    }

    private fun sendDataToDB() {
        Firebase.firestore.collection("users")
            .document(userObj.id)
            .set(userObj)
        saveUser()
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
        val uri = activityResult.data?.data
        val pathI = UtilDomi.getPath(applicationContext, uri!!)
        //val bitmap = BitmapFactory.decodeFile(pathI)
        binding.cardView.setContentPadding(10, 10, 10, 10)
        binding.consumerProfileImage.setImageURI(uri)
        changeImage = true;
    }
    private fun saveUser(){
        val sp = getSharedPreferences("u-market", MODE_PRIVATE)
        val json = Gson().toJson(userObj)
        sp.edit().putString("user",json).apply()
    }

    fun onResult(activityResult: ActivityResult?) {

    }
}
