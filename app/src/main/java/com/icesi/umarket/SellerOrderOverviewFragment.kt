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
import com.google.gson.TypeAdapter
import com.icesi.umarket.databinding.FragmentSellerOrderOverviewBinding
import com.icesi.umarket.model.AuxOrder
import com.icesi.umarket.model.Market
import com.icesi.umarket.model.Order
import com.icesi.umarket.model.Seller
import com.icesi.umarket.model.adapters.SellerOrdersHistoryAdapter
import com.icesi.umarket.model.adapters.SellerOrdersToConfirmAdapter
import com.icesi.umarket.util.Util

class SellerOrderOverviewFragment : Fragment() {

    private var _binding: FragmentSellerOrderOverviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: Seller
    private lateinit var market: Market
    val adapterOrder = SellerOrdersToConfirmAdapter()
    val adapterOrderHistory = SellerOrdersHistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSellerOrderOverviewBinding.inflate(inflater, container, false)
        getMarket()
        initOrdersRecyclerView(binding.ordersToConfirmRecycler).adapter = adapterOrder
        initOrdersRecyclerView(binding.ordersHistoryRecycler).adapter = adapterOrderHistory
        return binding.root
    }

    fun setUser(user: Seller) {
        this.user = user
    }

    private fun initOrdersRecyclerView(recycler: RecyclerView): RecyclerView{
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        return recycler
    }

    private fun getOrders(marketID: String, typeOrder: String, typeAdapter: Boolean) {
        adapterOrderHistory.clear()
        adapterOrder.clear()
        Firebase.firestore.collection("markets").document(marketID).collection(typeOrder).get()
            .addOnCompleteListener { order ->
                for (doc in order.result!!) {
                    val orderID = doc.toObject(AuxOrder::class.java)
                    Firebase.firestore.collection("orders").document(orderID.idOrder).get()
                        .addOnSuccessListener { orderReceived ->
                            val order = orderReceived.toObject(Order::class.java)
                            when (typeAdapter) {
                                true -> adapterOrder.addOrder(order!!)
                                false -> adapterOrderHistory.addOrder(order!!)
                            }
                        }
                }
            }
    }

    fun reloadOrders(idOrder: String){
        Firebase.firestore.collection("orders").document(idOrder).get()
            .addOnSuccessListener { orderReceived ->
                val order = orderReceived.toObject(Order::class.java)
                adapterOrderHistory.addOrder(order!!)
            }
    }

    private fun getMarket() {
        adapterOrder.clear()
        Firebase.firestore.collection("markets").document(user.marketID).get()
            .addOnSuccessListener {
                val currentMarket = it.toObject(Market::class.java)
                market = currentMarket!!
                val marketName = currentMarket?.marketName
                binding.marketNameTextView.text = marketName

                Util.loadImage(currentMarket!!.imageID!!, binding.marketProfileImage, "market-image-profile")
                getOrders(user.marketID, "historyOrders", false)
                getOrders(user.marketID, "pendentOrders", true)
            }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SellerOrderOverviewFragment()
    }

    interface OnConfirmOrderListener{
        fun confirmOrder(orderID: String, idUser: String)
        fun cancelOrder(orderID: String, idUser: String)
        fun editOrder(orderID: String, idUser: String)
    }
}