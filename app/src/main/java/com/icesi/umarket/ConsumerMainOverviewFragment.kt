package com.icesi.umarket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.icesi.umarket.databinding.FragmentConsumerMainOverviewBinding
import com.icesi.umarket.model.Market
import com.icesi.umarket.model.Product
import com.icesi.umarket.model.ProductAdapter


class ConsumerMainOverviewFragment : Fragment() {

    private var _binding: FragmentConsumerMainOverviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var user: Seller

    //STATE
    private val adapter = ProductAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentConsumerMainOverviewBinding.inflate(inflater,container,false)
        //user = loadUser()!!

        getMarket()

        val productRecycler = binding.recyclerView
        productRecycler.setHasFixedSize(true)
        productRecycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        productRecycler.adapter = adapter

        return binding.root
    //return inflater.inflate(R.layout.fragment_consumer_main_overview, container, false)
    }

    fun getMarket(){
        Firebase.firestore.collection("markets").document("2c63f174-0beb-446f-b7c8-e4e41ad1e927").get()
            .addOnSuccessListener {
                val currentMarket = it.toObject(Market::class.java)
                val marketName = currentMarket?.marketName
                val marketImage = currentMarket?.imageID
                getProducts("2c63f174-0beb-446f-b7c8-e4e41ad1e927")
            }
    }

    fun getProducts(marketID: String){
        Firebase.firestore.collection("markets").document("2c63f174-0beb-446f-b7c8-e4e41ad1e927").collection("products").get()
            .addOnCompleteListener { product ->
                adapter.clear()
            for(doc in product.result!!) {
                val prod = doc.toObject(Product::class.java)
                adapter.addProduct(prod)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ConsumerMainOverviewFragment()

    }
}