package com.icesi.umarket.consumer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.icesi.umarket.seller.SellerSignupActivity

import com.icesi.umarket.databinding.ActivityConsumerLoginBinding
import com.icesi.umarket.model.User


class ConsumerLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConsumerLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsumerLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpHyperLink.setOnClickListener {
            var userType = intent.extras?.getString("userType","")
            if(userType=="consumer"){
                startActivity(Intent(this, ConsumerSignupActivity::class.java))
            }else{
                startActivity(Intent(this, SellerSignupActivity::class.java))
            }
        }
        binding.sellerLoginGoBackBtn.setOnClickListener {
            finish()
        }


        binding.loginBtn.setOnClickListener {
            val email = binding.logInUserNameTextField.text.toString()
            val password = binding.logInPasswdTextField.text.toString()

            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val currentUser = Firebase.auth.currentUser

                    Firebase.firestore.collection("users").document(currentUser!!.uid).get()
                        .addOnSuccessListener {
                            val user = it.toObject(User::class.java)

                            startActivity(Intent(this, ConsumerHomeActivity::class.java).apply{
                                putExtra("currentUser", Gson().toJson(user))
                            })
                            finish()
                        }.addOnFailureListener {
                            Toast.makeText(this.baseContext, it.message, Toast.LENGTH_LONG).show()
                        }
                }.addOnFailureListener {
                    Toast.makeText(this.baseContext, it.message, Toast.LENGTH_LONG).show()
                }

        }
    }
}

