package com.icesi.umarket.consumer

import android.content.Context
import android.os.Bundle
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
import com.icesi.umarket.databinding.FragmentConsumerShoppingBinding
import com.icesi.umarket.model.Order
import com.icesi.umarket.model.adapters.OrderAdapter
import com.icesi.umarket.model.User
import com.icesi.umarket.util.Util


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
        Util.initRecycler(_binding.ordersRecyclerView, requireActivity(),LinearLayoutManager.VERTICAL ).adapter = adapter

        _binding.consumerNameShopping.text = currentUser.name
        Util.loadImage(currentUser.img,_binding.consumerProfileShopping, "profile")

        loadOrders()
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
                    binding.infoTxt.setText("")
                }
            }
    }
}


