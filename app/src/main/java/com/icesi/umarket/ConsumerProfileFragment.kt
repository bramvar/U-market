package com.icesi.umarket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.icesi.umarket.databinding.FragmentConsumerProfileBinding
import com.icesi.umarket.model.User

class ConsumerProfileFragment : Fragment() {

    private var _binding: FragmentConsumerProfileBinding? = null
    private val binding get() = _binding!!

    lateinit var currentUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentConsumerProfileBinding.inflate(inflater,container,false)

        loadUserData()
        binding.settingsBtn.setOnClickListener {
            val intent = Intent(activity, ConsumerEditProfile::class.java).apply{
                putExtra("currentUser", Gson().toJson(currentUser))
            }
            startActivity(intent)
        }

        binding.logoutBtn3.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    private fun loadUserData(){
        binding.nameConsumer.text = currentUser?.name
        binding.emailConsumer.text = currentUser?.email
        binding.phoneConsumer.text = currentUser?.phone
        loadProfileImg(currentUser!!.img)
    }

    private fun loadProfileImg(imageID: String){
        Firebase.storage.reference.child("profile").child(imageID).downloadUrl
            .addOnSuccessListener{
                Glide.with(binding.profilePhotoConsumer).load(it).into(binding.profilePhotoConsumer)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = ConsumerProfileFragment()
    }
}