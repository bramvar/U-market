package com.icesi.umarket.seller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.icesi.umarket.databinding.ActivitySellerSignupBinding

class SellerSignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySellerSignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySellerSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sellerLogInHyperLink.setOnClickListener {
            finish()
        }
        binding.sellerSignupGoBackBtn.setOnClickListener {
            finish()
        }
        binding.nextSellerBtn.setOnClickListener(::registerSeller)
    }

    private fun registerSeller(view: View){
        val i = Intent(this, AdditionalSellerInfoActivity::class.java)
        val phone = binding.signUpSellerPhoneTextField.text.toString()
        val sellerName = binding.signUpSellerNameTextField.text.toString()
        val email = binding.signUpSellerEmailTextField.text.toString()
        val password = binding.signUpSellerPasswdTextField.text.toString()
        val rePassword = binding.signUpSellerRepPasswdTextField.text.toString()

        if(verifyBlankSignupFields(phone,sellerName,email,password,rePassword)){
            if(password == rePassword){
                Firebase.auth.createUserWithEmailAndPassword(email,password)
                    .addOnSuccessListener {
                        val id = Firebase.auth.currentUser?.uid

                        i.putExtra("ID",id)
                        i.putExtra("PHONE",phone)
                        i.putExtra("SELLER_NAME",sellerName)
                        i.putExtra("EMAIL",email)
                        i.putExtra("PASSWORD",password)

                        startActivity(i)
                    }.addOnFailureListener {
                        Toast.makeText(this.baseContext,it.message, Toast.LENGTH_LONG).show()
                    }
            }else{
                Toast.makeText(this.baseContext,"Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this.baseContext,"Faltan campos por diligenciar", Toast.LENGTH_LONG).show()

        }

    }

    private fun verifyBlankSignupFields(
        marketName: String,
        sellerName: String,
        email: String,
        password: String,
        rePassword: String
    ): Boolean {
        return marketName.isNotBlank() && sellerName.isNotBlank() && email.isNotBlank() && password.isNotBlank() && rePassword.isNotBlank()
    }

}