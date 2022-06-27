package com.icesi.umarket.consumer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.icesi.umarket.ProductFragment
import com.icesi.umarket.R
import com.icesi.umarket.databinding.ActivityConsumerHomeBinding
import com.icesi.umarket.model.*
import com.icesi.umarket.util.Util

class ConsumerHomeActivity : AppCompatActivity(), ConsumerMainOverviewFragment.SellerObserver,
    MarketProfileFragment.backButtonObserver{

    private lateinit var binding: ActivityConsumerHomeBinding
    private lateinit var menuConsumer:BottomNavigationView
    private lateinit var currentUser: User
    private var shoppingCar = ShoppingCar()
    private var isInProductView: Boolean = false

    ////// Init the fragment
    private  var consumerMainOverviewFragment = ConsumerMainOverviewFragment.newInstance()
    private  var consumerProfileFragment = ConsumerProfileFragment.newInstance()
    private  var consumerShoppingFragment= ConsumerShoppingFragment.newInstance()
    private  var consumerMarketProfileFragment= MarketProfileFragment.newInstance()
    private  var productFragment = ProductFragment.newInstance()
    var dialogFragment = ConfirmPurchaseDiaglogFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsumerHomeBinding.inflate(layoutInflater)
        menuConsumer = binding.menuConsumer
        setContentView(binding.root)

        currentUser = Gson().fromJson(intent.extras?.getString("currentUser",""), User::class.java)
        ///currentUser = Gson().fromJson(intent.extras?.getString("currentUser",""), User::class.java)

        loadUserInFragments()
        loadListeners()

        showFragment(consumerMainOverviewFragment, true)
        menuConsumer.setOnItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.homeItem -> showFragment(consumerMainOverviewFragment, true)
                R.id.profileItem -> showFragment(consumerProfileFragment, true)
                R.id.ordersItem -> showFragment(consumerShoppingFragment, true)
            }
            true
        }
    }

    fun loadListeners(){
        ////// Load the listener
        consumerMainOverviewFragment.onSellerObserver = this
        consumerMarketProfileFragment.onProductObserver = this
        productFragment.onOrderObserver = this
        dialogFragment.onConfirmPurchaseObserver = consumerMarketProfileFragment
    }

    fun loadUserInFragments(){
        ///// Init the currentUser in the fragments
        productFragment.setUser(currentUser)
        consumerProfileFragment.currentUser = currentUser
        consumerMainOverviewFragment.currentUser = currentUser
        consumerMarketProfileFragment.currentUser = currentUser
        consumerShoppingFragment.currentUser = currentUser

    }

    fun showFragment(fragment: Fragment, blockMenu: Boolean){
        binding.menuConsumer.isVisible = blockMenu
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        consumerShoppingFragment.adapter.clear()
        transaction.commit()
    }



    override fun sendMarket(market: Market) {
        consumerMarketProfileFragment.currentMarket = market
        showFragment(consumerMarketProfileFragment, false)
    }

    override fun sendProduct(product: Product) {
        productFragment.product = product
        isInProductView = true
        showFragment(productFragment, false)
    }

    override fun sendShoppingInfo(name: String, market: Market) {
        shoppingCar.consumerName = name
        shoppingCar.currentMarket = market
    }

    override fun sendMessage(intent: Intent) {
                backToMarkets()
                startActivity(intent)
    }

    override fun sendMarketInfo(market: Market){
        productFragment.currentMarket = market
    }

    override fun backToTheMainMarket(){
        isInProductView = false
        showFragment(consumerMarketProfileFragment, false)
    }

    override fun backToMarketBlank(){
        isInProductView = false
        showFragment(consumerMainOverviewFragment, false)
    }

    override fun backToMarkets() {
        shoppingCar = ShoppingCar()
        consumerMarketProfileFragment.shoppingCar = shoppingCar
        showFragment(consumerMainOverviewFragment, true)
    }

    override fun loadOrder(order: Order) {
        shoppingCar.loadOrder(order)
        consumerMarketProfileFragment.shoppingCar = shoppingCar
        showFragment(consumerMarketProfileFragment, false)
    }

    override fun askOrder(shoppingCar: ShoppingCar) {
        if(shoppingCar.getAmountOfOrders() > 0) {
            var orderText = shoppingCar.generateConfirmText()
            dialogFragment.orderText = orderText
            dialogFragment.show(supportFragmentManager, "PurchaseConfirmationDialog")
        }else{
            backToMarkets()
        }
    }

    override fun onBackPressed() {
        when(isInProductView){
            true ->  backToTheMainMarket()
            false -> askOrder(consumerMarketProfileFragment.shoppingCar)
        }
    }

    override fun backToMarket() {
        TODO("Not yet implemented")
    }

    override fun backToMainMarket() {
        TODO("Not yet implemented")
    }
}