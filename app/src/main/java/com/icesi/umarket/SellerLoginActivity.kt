package com.icesi.umarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.icesi.umarket.databinding.ActivitySellerLoginBinding

class SellerLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySellerLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySellerLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpHyperLink.setOnClickListener {
            var userType = intent.extras?.getString("userType","")
            if(userType=="consumer"){
                startActivity(Intent(this,ConsumerSignupActivity::class.java))
            }else{
                startActivity(Intent(this,SellerSignupActivity::class.java))
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
                            val user = it.toObject(Seller::class.java)

                            saveUser(user!!)
                            startActivity(Intent(this,SellerHomeActivity::class.java))
                            finish()
                        }.addOnFailureListener{
                            Toast.makeText(this.baseContext,it.message, Toast.LENGTH_LONG).show()
                        }
                }.addOnFailureListener{
                Toast.makeText(this.baseContext,it.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveUser(user: Seller){
        val sp = getSharedPreferences("u-market", MODE_PRIVATE)
        val json = Gson().toJson(user)
        sp.edit().putString("user",json).apply()

    }
}