package com.icesi.umarket.consumer

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.icesi.umarket.databinding.FragmentConsumerMainOverviewBinding
import com.icesi.umarket.model.*
import com.icesi.umarket.model.adapters.MarketAdapter
import com.icesi.umarket.util.Util


class ConsumerMainOverviewFragment : Fragment() {

    private var _binding: FragmentConsumerMainOverviewBinding? = null
    private val binding get() = _binding!!

    lateinit var currentUser: User
    lateinit var onSellerObserver: SellerObserver

    //STATE
    private val adapter = MarketAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentConsumerMainOverviewBinding.inflate(inflater,container,false)
        adapter.onSellerObserver = onSellerObserver

        getMarkets()
        getUserData()
        Util.initRecycler(binding.recyclerView, requireActivity(), LinearLayoutManager.VERTICAL,).adapter = adapter

        return binding.root
    }


    fun getUserData(){
        binding.consumerName.text = currentUser.name
        Util.loadImage(currentUser.img,binding.consumerProfile,"profile" )
    }

    fun getMarkets() {
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

    interface SellerObserver{
        fun sendMarket(market: Market)
        fun sendProduct(product: Product)
        fun loadOrder(order: Order)
        fun sendShoppingInfo(name:String, market:Market)
        fun sendMessage(intent: Intent)
        fun backToMarkets()
        fun backToMarketBlank()
        fun backToTheMainMarket()
        fun sendMarketInfo(market: Market)
    }

}