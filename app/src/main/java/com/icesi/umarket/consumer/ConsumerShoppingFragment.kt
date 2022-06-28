package com.icesi.umarket.consumer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.icesi.umarket.databinding.FragmentConsumerShoppingBinding
import com.icesi.umarket.model.Order
import com.icesi.umarket.model.User
import com.icesi.umarket.model.adapters.OrderAdapter
import com.icesi.umarket.util.Util

class ConsumerShoppingFragment : Fragment() {

    /// View
    private lateinit var _binding: FragmentConsumerShoppingBinding
    private val binding get() = _binding!!
    val adapter = OrderAdapter()

    /// Object
    private lateinit var currentUser: User

    /// Variable
    private var itHasOrders: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConsumerShoppingBinding.inflate(inflater,container,false)
        Util.initRecycler(binding.ordersRecyclerView, requireActivity(),LinearLayoutManager.VERTICAL ).adapter = adapter

        binding.consumerNameShopping.text = currentUser.name
        Util.loadImage(currentUser.img,_binding.consumerProfileShopping, "profile")

        loadOrders()
        return _binding.root
    }

    fun setUser(user: User){
        currentUser = user
    }

    companion object {
        @JvmStatic
        fun newInstance() = ConsumerShoppingFragment()
    }

    private fun loadOrders() {
        Firebase.firestore.collection("users").document(currentUser.id).collection("orders").get()
            .addOnSuccessListener {
                for (doc in it.documents) {
                    itHasOrders = true
                    val orderID = doc.toObject(Order::class.java)

                    Firebase.firestore.collection("orders").document(orderID!!.idOrder).get()
                        .addOnSuccessListener { order ->
                            adapter.addOrder(order.toObject(Order::class.java)!!)
                        }
                }

                if (itHasOrders) {
                    adapter.showOrders()
                }
            }
    }
}


