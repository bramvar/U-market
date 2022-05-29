package com.icesi.umarket

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.icesi.umarket.databinding.FragmentConsumerMainOverviewBinding
import com.icesi.umarket.model.*


class ConsumerMainOverviewFragment : Fragment() {

    private var _binding: FragmentConsumerMainOverviewBinding? = null
    private val binding get() = _binding!!

    //STATE
    private val adapter = MarketAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentConsumerMainOverviewBinding.inflate(inflater,container,false)
        //user = loadUser()!!

        getMarket()
        getUserData()
        val marketRecyclerView = binding.recyclerView
        marketRecyclerView.setHasFixedSize(true)
        marketRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        marketRecyclerView.adapter = adapter

        return binding.root
    }

    fun getUserData(){
        val prefs = context?.getSharedPreferences("u-market", Context.MODE_PRIVATE)
        val user = Gson().fromJson(prefs!!.getString("user",""), User::class.java)
        binding.consumerName.text = user.name
        loadProfileImg(user.img)
    }

    fun loadProfileImg(imageID: String){
        Firebase.storage.reference.child("profile").child(imageID).downloadUrl
            .addOnSuccessListener{
                Glide.with(binding.consumerProfile).load(it).into(binding.consumerProfile)
            }
    }

    fun getMarket(){
        Firebase.firestore.collection("markets").get()
            .addOnCompleteListener { market ->
                adapter.clear()
                for (doc in market.result!!) {
                    val market = doc.toObject(Market::class.java)
                    adapter.addMarket(market)
                }
            }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ConsumerMainOverviewFragment()
    }
}