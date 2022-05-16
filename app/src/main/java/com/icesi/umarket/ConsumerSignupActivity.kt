package com.icesi.umarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.icesi.umarket.databinding.ActivityConsumerRegistrationDataBinding
import com.icesi.umarket.model.User

class ConsumerSignupActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityConsumerRegistrationDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsumerRegistrationDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onResult)
        binding.consumerRegistButtom.setOnClickListener {
            if(validateData()){
                var gson = Gson()
                /*
                Create the user class and use it to send the data
                 */
                var name: String = binding.consumerNameText.text.toString()
                var email: String = binding.consumerEmailText.text.toString()
                var password: String= binding.consumerPasswordText.text.toString()

                Firebase.auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                    var id = Firebase.auth.currentUser?.uid
                    val intent = Intent(this, AdditionalConsumerInfoActivity::class.java)
                        .putExtra("userObj", gson.toJson(User(id.toString(), name, email, password,"","","consumer")))
                    startActivity(intent)
                }.addOnFailureListener {
                    Toast.makeText(this.baseContext,it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.consumerCreateAccount.setOnClickListener{
            val intent = Intent(this, ConsumerLoginActivity::class.java)
            launcher.launch(intent)
        }

        binding.consumerNameText.setOnClickListener{
            changeToNormalText(binding.consumerNameText)
        }
        binding.consumerEmailText.setOnClickListener{
            changeToNormalText(binding.consumerEmailText)
            binding.emailMsg.isVisible = false
        }

        binding.consumerPasswordText.setOnClickListener{
            changeToNormalText(binding.consumerPasswordText)
        }

        binding.consumerRepasswordText.setOnClickListener{
            binding.passwordMsg.isVisible = false
        }
    }

    private fun changeToNormalText(text: android.widget.EditText){
        text.setBackgroundResource(R.drawable.shaping_plain_text)
    }

    private fun changeToErrorText(text: android.widget.EditText){
        text.setBackgroundResource(R.drawable.shaping_plain_text_error)
    }

    private fun validatePassword(password: String, repassword:String): Boolean {
        var arePasswordEquals: Boolean = false
        if(password != repassword){
            binding.passwordMsg.isVisible = true
        }else{
            arePasswordEquals=true;
        }
        return arePasswordEquals
    }

    private fun validateData(): Boolean{
        var isDataCorrect: Boolean = false
        if(
            binding.consumerNameText.text.toString()!="" &&
            binding.consumerEmailText.text.toString()!="" &&
            binding.consumerPasswordText.text.toString()!=""
        ){
            var password = binding.consumerPasswordText.text.toString()
            var repassword =binding.consumerRepasswordText.text.toString()
            if(validatePassword(password, repassword )){
                isDataCorrect=true
            }
        }else{
            incorrectDataVisualChanges()
        }
        return isDataCorrect
    }

    private fun incorrectDataVisualChanges() {

        if(binding.consumerNameText.text.toString()==""){
            changeToErrorText(binding.consumerNameText)
        }
        if(binding.consumerEmailText.text.toString()=="" || !(binding.consumerEmailText.text.toString().contains("@"))){
            changeToErrorText(binding.consumerEmailText)
            binding.emailMsg.isVisible = true
        }
        if(binding.consumerPasswordText.text.toString()==""){
            changeToErrorText(binding.consumerPasswordText)
        }
        binding.passwordMsg.isVisible = true
    }

fun onResult(activityResult: ActivityResult?) {

}
}