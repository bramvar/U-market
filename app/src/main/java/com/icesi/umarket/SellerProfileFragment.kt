package com.icesi.umarket

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.icesi.umarket.databinding.FragmentConsumerMainOverviewBinding
import com.icesi.umarket.databinding.FragmentSellerProfileBinding
import com.icesi.umarket.model.Market
import com.icesi.umarket.model.Product
import com.icesi.umarket.model.ProductAdapter

class SellerProfileFragment : Fragment() {

    private lateinit var _binding: FragmentSellerProfileBinding
    private val binding get() = _binding!!
    lateinit var currentMarket: Market
    var adapter = ProductAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSellerProfileBinding.inflate(inflater,container,false)


        _binding.infoMarketProfile.text = currentMarket.marketDescription
        _binding.marketName.text = currentMarket.marketName

        val productsRecyclerView = binding.productsMarketInConsumer
        productsRecyclerView.setHasFixedSize(true)
        productsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        productsRecyclerView.adapter = adapter

        loadImage()
        loadProducts()
        // Inflate the layout for this fragment
        return _binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = SellerProfileFragment()
    }

    fun loadImage(){
        if (currentMarket.imageID != "") {
            Firebase.storage.reference.child("market-image-profile")
                .child(currentMarket.imageID!!).downloadUrl
                .addOnSuccessListener {
                    Glide.with(_binding.MarketImageProfile).load(it).into(_binding.MarketImageProfile)
                }
        }
    }

    fun loadProducts(){
        Firebase.firestore.collection("markets").document(currentMarket.id).collection("products").get()
            .addOnSuccessListener { task ->
            for(product in task.documents){
                var productObj = product.toObject(Product::class.java)
                adapter.addProduct(productObj!!)
            }
        }
    }
}