package com.icesi.umarket

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.icesi.umarket.databinding.ActivityConsumerRegistrationPhotoBinding

class ConsumerRegistrationActivityPhoto : AppCompatActivity() {
    private lateinit var  binding: ActivityConsumerRegistrationPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsumerRegistrationPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onResult)
        binding.consumerFinishRegistButtom.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            launcher.launch(intent)
        }

        binding.consumerProfileImage.setOnClickListener {

        }

        binding.ConsumerBackPhotoButtom.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            launcher.launch(intent)
        }

        val galLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onGalleryResult)
        binding.consumerProfileImage.setOnClickListener {
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.type = "image/*"
            galLauncher.launch(i)
        }
    }


    private fun onGalleryResult(activityResult: ActivityResult) {
        val uri = activityResult.data?.data
        //val pathI = UtilDomi.getPath(applicationContext, uri!!)
        //val bitmap = BitmapFactory.decodeFile(pathI)
        binding.consumerProfileImage.setImageURI(uri)
    }

}

fun onResult(activityResult: ActivityResult?) {

}
