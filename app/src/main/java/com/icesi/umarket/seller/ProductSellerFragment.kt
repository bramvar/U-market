package com.icesi.umarket.seller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.icesi.umarket.databinding.FragmentProductSellerBinding
import com.icesi.umarket.model.Product
import com.icesi.umarket.util.Util

class ProductSellerFragment : Fragment() {
    private lateinit var _binding: FragmentProductSellerBinding
    private val binding get() = _binding!!
    private lateinit var product: Product
    lateinit var onProductSellerObserver: SellerMainOverviewFragment.OnProductsOnSellerObserver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductSellerBinding.inflate(inflater,container,false)
        buildProduct()
        _binding.productSellerBackBtn.setOnClickListener {
            onProductSellerObserver.backToOverview()
        }
        return _binding.root
    }

    private fun buildProduct(){
        _binding.productNameSeller.text = product.name
        _binding.productInfoSeller.text = product.description
        _binding.priceProductSeller.text = "$" + product.price.toString()
        Util.loadImage(product.imageID,_binding.productSellerImg,"product-images" )
    }

    fun setCurrentProduct(product: Product){
        this.product = product
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProductSellerFragment()
    }
}
