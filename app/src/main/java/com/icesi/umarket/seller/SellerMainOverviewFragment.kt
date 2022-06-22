package com.icesi.umarket.seller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.icesi.umarket.databinding.FragmentSellerMainOverviewBinding
import com.icesi.umarket.model.Market
import com.icesi.umarket.model.Product
import com.icesi.umarket.model.Seller
import com.icesi.umarket.model.adapters.ProductSellerAdapter
import com.icesi.umarket.util.Util

class SellerMainOverviewFragment : Fragment(), NewProductFragment.OnNewProductListener {

    private var _binding: FragmentSellerMainOverviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: Seller

    //STATE
    val adapter = ProductSellerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSellerMainOverviewBinding.inflate(inflater,container,false)
        getMarket()
        initProductsRecyclerView(binding.productsRecycler)
        return binding.root
    }

    private fun  initProductsRecyclerView(recycler: RecyclerView){
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        recycler.adapter = adapter
    }

    private fun getMarket(){
        Firebase.firestore.collection("markets").document(user.marketID).get()
            .addOnSuccessListener {
                val currentMarket = it.toObject(Market::class.java)
                val marketName = currentMarket?.marketName
                binding.marketNameTextView.text = marketName
                Util.loadImage(user.marketID,binding.marketProfileImage,"market-image-profile" )
                getProducts(user.marketID)
            }
    }

    private fun getProducts(marketID: String) {
        Firebase.firestore.collection("markets").document(marketID).collection("products").get()
            .addOnCompleteListener { product ->
                adapter.clear()
                for (doc in product.result!!) {
                    val prod = doc.toObject(Product::class.java)
                    adapter.addProduct(prod)
                }
            }
    }

    fun setUser(user: Seller){
        this.user = user
    }

    override fun onNewProduct(
        id: String,
        productName: String,
        productPrice: Int,
        productDescription:String,
        productImage: String
    ) {
        val product = Product(id,productName,productPrice,productDescription,productImage)
        adapter.addProduct(product)
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
        fun backToOverview()
    }

}