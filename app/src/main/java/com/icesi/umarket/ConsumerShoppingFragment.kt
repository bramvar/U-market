package com.icesi.umarket

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.icesi.umarket.databinding.FragmentConsumerMainOverviewBinding
import com.icesi.umarket.databinding.FragmentConsumerShoppingBinding
import com.icesi.umarket.model.Order
import com.icesi.umarket.model.OrderAdapter
import com.icesi.umarket.model.User



class ConsumerShoppingFragment : Fragment() {

    lateinit var currentUser: User
    var itHasOrders: Boolean = false
    private lateinit var _binding: FragmentConsumerShoppingBinding
    private val binding get() = _binding!!
     val adapter = OrderAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConsumerShoppingBinding.inflate(inflater,container,false)
        var orderRecyclerView = _binding.ordersRecyclerView
        orderRecyclerView.setHasFixedSize(true)
        orderRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        orderRecyclerView.adapter = adapter

        val prefs = requireActivity().getSharedPreferences("u-market", Context.MODE_PRIVATE)
        val json = Gson().fromJson(prefs.getString("user",""), User::class.java)
        _binding.consumerNameShopping.text = json.name

        loadOrders()
        loadProfileImg(json.img)

        // Inflate the layout for this fragment
        return _binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = ConsumerShoppingFragment()
    }

    fun loadOrders() {
        Firebase.firestore.collection("users").document(currentUser.id).collection("orders").get()
            .addOnSuccessListener {
                for(doc in it.documents){
                    itHasOrders = true
                    adapter.addOrder(doc.toObject(Order::class.java)!!)
                }

                if(itHasOrders){
                    adapter.showOrders()
                }
            }
    }

    fun loadProfileImg(imageID: String){
        Firebase.storage.reference.child("profile").child(imageID).downloadUrl
            .addOnSuccessListener{
                Glide.with(_binding.consumerProfileShopping).load(it).into(_binding.consumerProfileShopping)
            }
    }
}


