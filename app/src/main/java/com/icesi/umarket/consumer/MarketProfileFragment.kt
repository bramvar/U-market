package com.icesi.umarket.consumer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.icesi.umarket.databinding.FragmentMarketProfileBinding
import com.icesi.umarket.model.*
import com.icesi.umarket.model.adapters.ProductAdapter
import com.icesi.umarket.util.Util

class MarketProfileFragment : Fragment(), ConfirmPurchaseDialogFragment.ConfirmPurchaseObserver {

    /// View
    private lateinit var _binding: FragmentMarketProfileBinding
    private val binding get() = _binding!!
    var adapter = ProductAdapter()

    /// Objects
    private lateinit var currentMarket: Market
    private lateinit var currentUser: User
    private lateinit var shoppingCar: ShoppingCar

    /// Listeners
    lateinit var onProductObserver: ConsumerMainOverviewFragment.SellerObserver
    lateinit var onPurchaseObserver: BackButtonObserver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initListener()

        _binding = FragmentMarketProfileBinding.inflate(inflater,container,false)
        _binding.infoMarketProfile.text = currentMarket.marketDescription
        _binding.marketName.text = currentMarket.marketName

        Util.initRecycler(binding.productsMarketInConsumer, requireActivity(),LinearLayoutManager.HORIZONTAL).adapter = adapter
        Util.loadImage(currentMarket.imageID, binding.marketImageProfile,"market-image-profile" )

        loadProducts()

        _binding.sellerinfoBtn.setOnClickListener {
            onPurchaseObserver.askOrder(shoppingCar)
        }

        return _binding.root
    }

    private fun initListener(){
        adapter.onProductObserver = onProductObserver
        onProductObserver.sendMarketInfo(currentMarket)
        onProductObserver.sendShoppingInfo(currentUser.name, currentMarket)
        adapter.clear()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MarketProfileFragment()
    }

    private fun loadProducts(){
        Firebase.firestore.collection("markets").document(currentMarket.id).collection("products").get()
            .addOnSuccessListener { task ->
            for(product in task.documents){
                adapter.addProduct(product.toObject(Product::class.java)!!)
            }
        }
    }

    override fun confirm() {
        onProductObserver.sendMessage(shoppingCar.sendMessage())
    }

    override fun discard() {
        onProductObserver.backToMarkets()
    }

    fun setUser(user: User){
        currentUser = user
    }

    fun setMarket(market: Market){
        currentMarket = market
    }

    fun setShoppingCar(shoppingCar: ShoppingCar){
        this.shoppingCar = shoppingCar
    }

    fun getShoppingCar(): ShoppingCar{
        return shoppingCar
    }

    interface BackButtonObserver{
        fun askOrder(shoppingCar: ShoppingCar)
    }
}