package com.icesi.umarket

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.icesi.umarket.databinding.FragmentConsumerProfileBinding
import com.icesi.umarket.databinding.FragmentConsumerShoppingBinding
import com.icesi.umarket.model.User

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ConsumerShoppingFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentConsumerShoppingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConsumerShoppingBinding.inflate(layoutInflater)

        val prefs = requireActivity().getSharedPreferences("u-market", Context.MODE_PRIVATE)
        val json = Gson().fromJson(prefs.getString("user",""), User::class.java)

        binding.consumerNameShopping.text = json.name
        loadProfileImg(json.img)

        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ConsumerShoppingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun loadProfileImg(imageID: String){

        Firebase.storage.reference.child("profile").child(imageID).downloadUrl
            .addOnSuccessListener{
                Glide.with(binding.consumerProfileShopping).load(it).into(binding.consumerProfileShopping)
            }
    }
}