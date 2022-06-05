package com.icesi.umarket

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.icesi.umarket.databinding.ActivityConsumerHomeBinding
import com.icesi.umarket.databinding.FragmentSellerProfileBinding
import com.icesi.umarket.model.*

class SellerProfileFragment : Fragment(), ConfirmPurchaseDiaglogFragment.ConfirmPurchaseObserver {

    private lateinit var _binding: FragmentSellerProfileBinding
    private val binding get() = _binding!!
    lateinit var currentMarket: Market
    lateinit var currentUser: User
    var shoppingCar: ShoppingCar = ShoppingCar()
    var adapter = ProductAdapter()
    lateinit var onProductObserver: ConsumerMainOverviewFragment.SellerObserver
    val dialogFragment = ConfirmPurchaseDiaglogFragment.newInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSellerProfileBinding.inflate(inflater,container,false)
        adapter.onProductObserver = onProductObserver
        _binding.infoMarketProfile.text = currentMarket.marketDescription
        _binding.marketName.text = currentMarket.marketName
        onProductObserver.sendShoppingInfo(currentUser.name, currentMarket.phoneNumber)

        val productsRecyclerView = binding.productsMarketInConsumer
        productsRecyclerView.setHasFixedSize(true)
        productsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        productsRecyclerView.adapter = adapter

        loadImage()
        loadProducts()

        _binding.sellerinfoBtn.setOnClickListener {
            if(shoppingCar.getAmountOfOrders() > 0) {
                dialogFragment.onConfirmPurchaseObserver = this
                dialogFragment.orderText = shoppingCar.generateConfirmText()
                dialogFragment.show(
                    requireActivity().supportFragmentManager,
                    "PurchaseConfirmationDialog"
                );
            }else{
                onProductObserver.backToMarkets()
            }
        }

        // Inflate the layout for this fragment
        return _binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = SellerProfileFragment()
    }

    fun loadImage(){
        if (currentMarket.imageID != "") {
            Firebase.storage.reference.child("market-image-profile")
                .child(currentMarket.imageID!!).downloadUrl
                .addOnSuccessListener {
                    Glide.with(_binding.MarketImageProfile).load(it).into(_binding.MarketImageProfile)
                }
        }
    }

    fun loadProducts(){
        Firebase.firestore.collection("markets").document(currentMarket.id).collection("products").get()
            .addOnSuccessListener { task ->
            for(product in task.documents){
                var productObj = product.toObject(Product::class.java)
                adapter.addProduct(productObj!!)
            }
        }
    }

    override fun confirm() {
        dialogFragment.onDestroy()
        shoppingCar.marketNumber = "3186775051"
        onProductObserver.sendMessage(shoppingCar.sendMessage())

    }

    override fun discard() {
        dialogFragment.onDestroy()
        onProductObserver.backToMarkets()
    }
}