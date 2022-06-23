package com.icesi.umarket.consumer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.icesi.umarket.databinding.ActivityConsumerEditProfileBinding
import com.icesi.umarket.model.User
import com.icesi.umarket.util.Util

class ConsumerEditProfile : AppCompatActivity() {
    private lateinit var binding: ActivityConsumerEditProfileBinding
    lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsumerEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUser = Gson().fromJson(intent.extras?.getString("currentUser",""), User::class.java)
        loadInformation(currentUser)

        binding.editDoneBtn.setOnClickListener{
            val idtoCompare = currentUser.id
            updateInformation(currentUser)
            Firebase.firestore.collection("users").document(idtoCompare).set(currentUser)
            Toast.makeText(this, "Actualizacion exitosa", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, ConsumerHomeActivity::class.java).apply{
                putExtra("currentUser", Gson().toJson(currentUser))
            }

            startActivity(intent)
        }
    }

    fun loadInformation(user: User){
        binding.nameEdit.setText(user.name)
        binding.passEdit.setText(user.password)
        binding.emailEdit.setText(user.email)
        binding.phoneEdit.setText(user.phone)
        Util.loadImage(user.img,binding.profilephotoedit,"profile")
    }

    fun updateInformation(user: User){
        user.name = binding.nameEdit.text.toString()
        user.password = binding.passEdit.text.toString()
        user.email = binding.emailEdit.text.toString()
        user.phone = binding.phoneEdit.text.toString()
    }
}