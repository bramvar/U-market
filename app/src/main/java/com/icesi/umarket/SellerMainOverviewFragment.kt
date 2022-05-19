package com.icesi.umarket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.icesi.umarket.databinding.FragmentSellerMainOverviewBinding
import com.icesi.umarket.model.Product
import com.icesi.umarket.model.ProductAdapter
import java.util.*

class SellerMainOverviewFragment : Fragment(), NewProductFragment.OnNewProductListener {

    private var _binding: FragmentSellerMainOverviewBinding? = null
    private val binding get() = _binding!!

    //STATE
    private val adapter = ProductAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSellerMainOverviewBinding.inflate(inflater,container,false)

        val productRecycler = binding.productsRecycler
        productRecycler.setHasFixedSize(true)
        productRecycler.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        productRecycler.adapter = adapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SellerMainOverviewFragment()
    }

    override fun onNewProduct(
        id: String,
        productName: String,
        productPrice: Int,
        productDescription: String,
        productImage: String,
    ) {
        val product = Product(id,productName,productPrice,productDescription,productImage)
        Firebase.firestore.collection("markets").document(marketName)
        adapter.addProduct(product)
    }


}