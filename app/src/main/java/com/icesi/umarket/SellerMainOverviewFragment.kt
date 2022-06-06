package com.icesi.umarket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.icesi.umarket.databinding.FragmentSellerMainOverviewBinding
import com.icesi.umarket.model.Market
import com.icesi.umarket.model.Product
import com.icesi.umarket.model.ProductAdapter
import com.icesi.umarket.model.ProductSellerAdapter
import java.util.*

class SellerMainOverviewFragment : Fragment(), NewProductFragment.OnNewProductListener {

    private var _binding: FragmentSellerMainOverviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var user: Seller

    //STATE
    private val adapter = ProductSellerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSellerMainOverviewBinding.inflate(inflater,container,false)

        user = loadUser()!!

        getMarket()

        val productRecycler = binding.productsRecycler
        productRecycler.setHasFixedSize(true)
        productRecycler.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        productRecycler.adapter = adapter

        return binding.root
    }

    fun getMarket(){
        Firebase.firestore.collection("markets").document(user.marketID).get()
            .addOnSuccessListener {
                val currentMarket = it.toObject(Market::class.java)
                val marketName = currentMarket?.marketName
                val marketImage = currentMarket?.imageID

                binding.marketNameTextView.text = marketName

                downloadMarketProfileImage(marketImage)
                getProducts(user.marketID)

            }
    }

    fun getProducts(marketID: String){
        Firebase.firestore.collection("markets").document(marketID).collection("products").get()
            .addOnCompleteListener { product ->
                adapter.clear()
                for(doc in product.result!!){
                    val prod =doc.toObject(Product::class.java)
                    adapter.addProduct(prod)
                }

            }
    }

    fun downloadMarketProfileImage(imageID: String?){
        if(imageID == null) return

        Firebase.storage.reference.child("market-image-profile").child(imageID).downloadUrl
            .addOnSuccessListener{
                Glide.with(binding.marketProfileImage).load(it).into(binding.marketProfileImage)
            }
    }

    fun loadUser(): Seller?{
        val sp = this.context?.getSharedPreferences("u-market", AppCompatActivity.MODE_PRIVATE)
        val json = sp?.getString("user","NO_USER")

        if(json == "NO_USER"){
            return null
        } else{
            return Gson().fromJson(json, Seller::class.java)
        }
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
        productDescription:String,
        productImage: String
    ) {
        val product = Product(id,productName,productPrice,productDescription,productImage)
        adapter.addProduct(product)
    }


}