package com.icesi.umarket

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.icesi.umarket.databinding.FragmentConsumerProfileBinding
import com.icesi.umarket.model.User


class ConsumerProfileFragment : Fragment() {

    private lateinit var binding: FragmentConsumerProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentConsumerProfileBinding.inflate(layoutInflater)
        var email: String = Firebase.auth.currentUser?.email.toString()
        lateinit var user: User
        Firebase.firestore.collection("users").whereEqualTo("email", email).limit(1).
        addSnapshotListener { value, error ->
            if (value != null) {
                for (doc in value.documents ){
                    user = doc.toObject(User::class.java)!!

                    binding.nameConsumer.text = user.name
                    binding.emailConsumer.text = user.email
                    binding.phoneConsumer.text = user.phone
                    loadProfileImg(user.img)
                }
            }
        }


        binding.settingsBtn.setOnClickListener {
            val intent = Intent(activity, ConsumerEditProfile::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    fun loadProfileImg(imageID: String){

        Firebase.storage.reference.child("profile").child(imageID).downloadUrl
            .addOnSuccessListener{
                Glide.with(binding.profilephoto).load(it).into(binding.profilephoto)
            }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ConsumerProfileFragment()
    }
}