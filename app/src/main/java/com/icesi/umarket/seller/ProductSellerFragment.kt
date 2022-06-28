package com.icesi.umarket.seller

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.icesi.umarket.databinding.FragmentProductSellerBinding
import com.icesi.umarket.model.Product
import com.icesi.umarket.model.Seller
import com.icesi.umarket.util.Util

class ProductSellerFragment : Fragment() {
    private lateinit var _binding: FragmentProductSellerBinding
    private val binding get() = _binding!!
    private lateinit var product: Product
    private lateinit var user: Seller
    lateinit var onProductSellerObserver: SellerMainOverviewFragment.OnProductsOnSellerObserver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductSellerBinding.inflate(inflater,container,false)
        buildProduct()

        binding.productSellerBackBtn.setOnClickListener {
            onProductSellerObserver.backToOverview()
        }

        binding.eraseProductSellerButton.setOnClickListener{
            onProductSellerObserver.deleteProduct(product)
        }

        binding.editProductSellerButton.setOnClickListener {
            onProductSellerObserver.showEditProduct(product)
        }

        return binding.root
    }

    private fun buildProduct(){
        binding.productNameSeller.text = product.name
        binding.productInfoSeller.text = product.description
        binding.priceProductSeller.text = "$" + product.price.toString()
        binding.amoutOfProductSellerView.text = product.amount.toString()
        Util.loadImage(product.imageID,_binding.productSellerImg,"product-images" )
    }

    fun setCurrentProduct(product: Product){
        this.product = product
    }

    fun setUser(user: Seller) {
        this.user = user
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProductSellerFragment()
    }
}
