package com.icesi.umarket

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.icesi.umarket.databinding.ActivitySellerLoginBinding
import com.icesi.umarket.model.User

class ConsumerLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellerLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.loginBtn.setOnClickListener {
            val email = binding.logInUserNameTextField.text.toString()
            val password = binding.logInPasswdTextField.text.toString()

            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val currentUser = Firebase.auth.currentUser

                    Firebase.firestore.collection("users").document(currentUser!!.uid).get()
                        .addOnSuccessListener {
                            val user = it.toObject(User::class.java)

                            //saveUser(user!!)
                            startActivity(Intent(this, ConsumerHomeActivity::class.java))
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
