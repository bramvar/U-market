package com.icesi.umarket.consumer

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.icesi.umarket.MainActivity
import com.icesi.umarket.databinding.FragmentConsumerProfileBinding
import com.icesi.umarket.model.User
import com.icesi.umarket.util.Util

class ConsumerProfileFragment : Fragment() {

    /// View
    private var _binding: FragmentConsumerProfileBinding? = null
    private val binding get() = _binding!!

    /// Object
    private lateinit var currentUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            startActivity(Intent(activity, MainActivity::class.java))
            requireActivity().finish()

        }
        return binding.root
    }

    private fun loadUserData(){
        binding.nameConsumer.text = currentUser.name
        binding.emailConsumer.text = currentUser.email
        binding.phoneConsumer.text = currentUser.phone
        Util.loadImage(currentUser.img, binding.profilePhotoConsumer, "profile")
    }

    fun setUser(user: User){
        currentUser = user
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