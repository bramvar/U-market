package com.icesi.umarket

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.icesi.umarket.databinding.ActivityConsumerRegistrationPhotoBinding
import com.icesi.umarket.util.UtilDomi

class ConsumerRegistrationActivityPhoto : AppCompatActivity() {
    private lateinit var binding: ActivityConsumerRegistrationPhotoBinding
    private var changeImage: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsumerRegistrationPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onResult)

        binding.consumerFinishRegistButtom.setOnClickListener {
            if (validateData()) {
                val intent = Intent(this, ConsumerHomeActivity::class.java)
                launcher.launch(intent)
            }
        }

        binding.ConsumerBackPhotoButtom.setOnClickListener {
            val intent = Intent(this, ConsumerRegistrationActivityData::class.java)
            launcher.launch(intent)
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

    private fun validateData(): Boolean {
        var isCorrect: Boolean = true;
        if (binding.ConsumerPhoneText.text.toString() == "" || binding.ConsumerPhoneText.text.toString().length < 10) {
            changeToErrorText(binding.ConsumerPhoneText)
            binding.phoneMsg.isVisible = true
            isCorrect = false
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
        val bitmap = BitmapFactory.decodeFile(pathI)
        binding.cardView.setContentPadding(10, 10, 10, 10)
        binding.consumerProfileImage.setImageURI(uri)
        changeImage = true;
    }

    fun onResult(activityResult: ActivityResult?) {

    }
}
