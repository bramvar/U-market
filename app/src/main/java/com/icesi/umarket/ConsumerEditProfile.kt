package com.icesi.umarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.icesi.umarket.databinding.ActivityConsumerEditProfileBinding
import com.icesi.umarket.databinding.ActivityConsumerLoginBinding
import com.icesi.umarket.model.User

class ConsumerEditProfile : AppCompatActivity() {
    private lateinit var binding: ActivityConsumerEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsumerEditProfileBinding.inflate(layoutInflater)
        loadUSer()
        setContentView(binding.root)
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

    fun loadUSer(){
        var email: String = Firebase.auth.currentUser?.email.toString()

        Firebase.firestore.collection("users").whereEqualTo("email", email).limit(1).
        addSnapshotListener { value, error ->
            if (value != null) {
                for (doc in value.documents ){
                    var user = doc.toObject(User::class.java)!!
                    loadInformation(user)
                }
            }
        }
    }
}