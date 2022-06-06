package com.icesi.umarket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.icesi.umarket.databinding.FragmentSellerProfileBinding
import com.icesi.umarket.model.Market
import com.icesi.umarket.model.Product
import com.icesi.umarket.model.ProductSellerAdapter

class SellerProfileFragment : Fragment() {

    private lateinit var _binding: FragmentSellerProfileBinding
    private val binding get() = _binding!!
    private lateinit var user: Seller
    lateinit var currentMarket: Market

    var adapter = ProductSellerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSellerProfileBinding.inflate(inflater,container,false)

        user = loadUser()!!

        getMarket()

        val productsRecyclerView = binding.productsMarketInConsumer
        productsRecyclerView.setHasFixedSize(true)
        productsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        productsRecyclerView.adapter = adapter

        // Inflate the layout for this fragment
        return _binding.root

    }

    companion object {
        @JvmStatic
        fun newInstance() = SellerProfileFragment()
    }

    fun loadImage(){

    }

    fun loadUser(): Seller?{
        val sp = this.context?.getSharedPreferences("u-market", AppCompatActivity.MODE_PRIVATE)
        val json = sp?.getString("user","NO_USER")

        if(json == "NO_USER"){
            return null
        } else{
            return Gson().fromJson(json, Seller::class.java)
        }
    }

    fun getMarket(){
        Firebase.firestore.collection("markets").document(user.marketID).get()
            .addOnSuccessListener {
                val currentMarket = it.toObject(Market::class.java)
                val marketName = currentMarket?.marketName
                val marketImage = currentMarket?.imageID
                val marketDescription = currentMarket?.marketDescription

                binding.marketName.text = marketName
                binding.infoMarketProfile.text = marketDescription

                downloadMarketProfileImage(marketImage)
                getProducts(user.marketID)

            }
    }

    fun downloadMarketProfileImage(imageID: String?){
        if(imageID == null) return

        Firebase.storage.reference.child("market-image-profile").child(imageID).downloadUrl
            .addOnSuccessListener{
                Glide.with(binding.MarketImageProfile).load(it).into(binding.MarketImageProfile)
            }
    }

    fun getProducts(marketID: String){
        Firebase.firestore.collection("markets").document(marketID).collection("products").get()
            .addOnCompleteListener { product ->
                adapter.clear()
                for(doc in product.result!!){
                    val prod =doc.toObject(Product::class.java)
                    adapter.addProduct(prod)
                }

            }
    }

}