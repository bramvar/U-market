package com.icesi.umarket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.icesi.umarket.databinding.FragmentProductBinding
import com.icesi.umarket.databinding.FragmentProductSellerBinding
import com.icesi.umarket.databinding.FragmentSellerMainOverviewBinding
import com.icesi.umarket.model.Product

class ProductSellerFragment : Fragment() {
    private lateinit var _binding: FragmentProductSellerBinding
    private val binding get() = _binding!!
    lateinit var product: Product
    lateinit var onProductSellerObserver: SellerMainOverviewFragment.onProductsOnSellerObserver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductSellerBinding.inflate(inflater,container,false)
        _binding.productNameSeller.text = product.name
        _binding.productInfoSeller.text = product.description
        _binding.priceProductSeller.text = "$" + product.price.toString()
        loadImage()

        _binding.productSellerBackBtn.setOnClickListener {
            onProductSellerObserver.backToOverview()
        }

        return _binding.root
    }

    fun loadImage() {
        if (product.imageID != "") {
            Firebase.storage.reference.child("product-images")
                .child(product.imageID!!).downloadUrl
                .addOnSuccessListener {
                    Glide.with(_binding.productSellerImg).load(it).into(_binding.productSellerImg)
                }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProductSellerFragment()
    }
}
