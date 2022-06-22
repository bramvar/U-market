package com.icesi.umarket

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.icesi.umarket.databinding.FragmentSellerOrderOverviewBinding
import com.icesi.umarket.model.Market
import com.icesi.umarket.model.Order
import com.icesi.umarket.model.Seller
import com.icesi.umarket.model.adapters.SellerOrdersToConfirmAdapter

class SellerOrderOverviewFragment : Fragment() {

    private var _binding: FragmentSellerOrderOverviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: Seller
    val adapterOrder = SellerOrdersToConfirmAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSellerOrderOverviewBinding.inflate(inflater,container,false)
        user = loadUser()!!
        getMarket()
        initOrdersRecyclerView(binding.ordersToConfirmRecycler)
        return binding.root
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

    private fun initOrdersRecyclerView(recycler: RecyclerView){
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        recycler.adapter = adapterOrder
    }

    fun getOrdersToConfirm(marketID: String) {
        Firebase.firestore.collection("markets").document(marketID).collection("orders").get()
            .addOnCompleteListener { order ->
                for (doc in order.result!!) {
                    val order = doc.toObject(Order::class.java)
                    adapterOrder.addOrder(order)
                }
            }
    }

    fun getMarket(){
        adapterOrder.clear()
        Firebase.firestore.collection("markets").document(user.marketID).get()
            .addOnSuccessListener {
                val currentMarket = it.toObject(Market::class.java)
                val marketName = currentMarket?.marketName
                val marketImage = currentMarket?.imageID

                binding.marketNameTextView.text = marketName
                downloadMarketProfileImage(marketImage)
                getOrdersToConfirm(user.marketID)
            }
    }

    fun downloadMarketProfileImage(imageID: String?){
        if(imageID == null) return
        Firebase.storage.reference.child("market-image-profile").child(imageID).downloadUrl
            .addOnSuccessListener{
                Glide.with(binding.marketProfileImage).load(it).into(binding.marketProfileImage)
            }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SellerOrderOverviewFragment()
    }

    interface onConfirmOrderListener{
        fun confirmOrder(orderID: String)
        fun cancelOrder(orderID: String)
        fun editOrder(orderID: String)
    }
}