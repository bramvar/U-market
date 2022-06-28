package com.icesi.umarket.seller

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.icesi.umarket.databinding.FragmentSellerMainOverviewBinding
import com.icesi.umarket.model.Market
import com.icesi.umarket.model.Product
import com.icesi.umarket.model.Seller
import com.icesi.umarket.model.adapters.ProductSellerAdapter
import com.icesi.umarket.util.Util

class SellerMainOverviewFragment : Fragment(), NewProductFragment.OnNewProductListener {

    /// View
    private var _binding: FragmentSellerMainOverviewBinding? = null
    private val binding get() = _binding!!
    val adapter = ProductSellerAdapter()

    /// Object
    private lateinit var user: Seller

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSellerMainOverviewBinding.inflate(inflater,container,false)
        getMarket()
        Util.initRecycler(binding.productsRecycler, requireActivity(), LinearLayoutManager.VERTICAL).adapter = adapter
        return binding.root
    }

    private fun getMarket(){
        Firebase.firestore.collection("markets").document(user.marketID).get()
            .addOnSuccessListener {
                val currentMarket = it.toObject(Market::class.java)
                val marketName = currentMarket?.marketName
                binding.marketNameTextView.text = marketName
                Util.loadImage(currentMarket!!.imageID,binding.marketProfileImage,"market-image-profile" )
                getProducts(user.marketID)
            }
    }

    private fun getProducts(marketID: String) {
        Firebase.firestore.collection("markets").document(marketID).collection("products").get()
            .addOnCompleteListener { product ->
                adapter.clear()
                for (doc in product.result!!) {
                    adapter.addProduct(doc.toObject(Product::class.java)!!)
                }
            }
    }

    fun setUser(user: Seller){
        this.user = user
    }

    override fun onNewProduct(id: String, productName: String, productPrice: Int, productDescription:String, productImage: String) {
        adapter.addProduct(Product(id,productName,productPrice,productDescription,productImage))
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SellerMainOverviewFragment()
    }

    interface OnProductsOnSellerObserver{
        fun sendProduct(product: Product)
        fun showEditProduct(product: Product)
        fun deleteProduct(product: Product)
        fun editProduct(product: Product)
        fun backToOverview()
    }
}