package com.icesi.umarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.icesi.umarket.databinding.ActivityConsumerEditProfileBinding
import com.icesi.umarket.databinding.ActivityConsumerLoginBinding
import com.icesi.umarket.model.User

class ConsumerEditProfile : AppCompatActivity() {
    private lateinit var binding: ActivityConsumerEditProfileBinding
    lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsumerEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUser = Gson().fromJson(
            intent.extras?.getString("currentUser",""),
            User::class.java
        )
        loadInformation(currentUser)
    }

    fun loadInformation(user: User){

        binding.nameEdit.setText(user.name)
        binding.passEdit.setText(user.password)
        binding.emailEdit.setText(user.email)
        binding.phoneEdit.setText(user.phone)

        Firebase.storage.reference.child("profile").child(user.img).downloadUrl
            .addOnSuccessListener{
                Glide.with(binding.profilephotoedit).load(it).into(binding.profilephotoedit)
            }
    }
}