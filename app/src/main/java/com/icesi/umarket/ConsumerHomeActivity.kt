package com.icesi.umarket

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.icesi.umarket.databinding.ActivityConsumerHomeBinding
import com.icesi.umarket.model.*

class ConsumerHomeActivity : AppCompatActivity(), ConsumerMainOverviewFragment.SellerObserver {


    private lateinit var binding: ActivityConsumerHomeBinding
    private lateinit var menuConsumer:BottomNavigationView

    ////// Init the fragment
    private  var consumerMainOverviewFragment = ConsumerMainOverviewFragment.newInstance()
    private  var consumerProfileFragment = ConsumerProfileFragment.newInstance()
    private  var consumerShoppingFragment= ConsumerShoppingFragment.newInstance()
    private  var consumerSellerProfileFragment= SellerProfileFragment.newInstance()
    private  var productFragment = ProductFragment.newInstance()
    private lateinit var currentUser: User
    private lateinit var shoppingCar: ShoppingCar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsumerHomeBinding.inflate(layoutInflater)
        menuConsumer = binding.menuConsumer
        setContentView(binding.root)

        currentUser = Gson().fromJson(
            intent.extras?.getString("currentUser",""),
            User::class.java
        )

        shoppingCar = ShoppingCar()
        ///// Init the currentUser in the fragments
        consumerProfileFragment.currentUser = currentUser
        consumerMainOverviewFragment.currentUser = currentUser
        consumerSellerProfileFragment.currentUser = currentUser
        consumerShoppingFragment.currentUser = currentUser

        ////// Load the listener
        consumerMainOverviewFragment.onSellerObserver = this
        consumerSellerProfileFragment.onProductObserver = this
        productFragment.onOrderObserver = this

        showFragment(consumerMainOverviewFragment, true)

        menuConsumer.setOnItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.homeItem -> showFragment(consumerMainOverviewFragment, true)
                R.id.profileItem -> showFragment(consumerProfileFragment, true)
                R.id.ordersItem -> showFragment(consumerShoppingFragment, false)
            }
            true
        }
    }

    override fun onBackPressed() {
    // super.onBackPressed();
    }

    fun showFragment(fragment: Fragment, eraseOrders: Boolean){
        if(eraseOrders){
            consumerShoppingFragment.adapter.clear()
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        consumerShoppingFragment.adapter.clear()
        transaction.commit()
    }

    override fun sendMarket(market: Market) {
        consumerSellerProfileFragment.currentMarket = market
        showFragment(consumerSellerProfileFragment, true)
    }

    override fun sendProduct(product: Product) {
        productFragment.product = product
        showFragment(productFragment, true)
    }

    override fun sendShoppingInfo(name: String, phone: String) {
        shoppingCar.consumerName = name
        shoppingCar.marketNumber = phone
    }

    override fun sendMessage(intent: Intent) {
        Firebase.firestore.collection("users").whereEqualTo("email", currentUser.email).get().addOnSuccessListener {
            for(doc in it.documents){
                var user= doc.toObject(User::class.java)!!
                for(order in shoppingCar.orders){
                    Firebase.firestore.collection("users").document(user.id).collection("orders").add(order)
                }
                backToMarkets()
                startActivity(intent)
            }
        }
    }

    override fun backToMarketBlank(){
        showFragment(consumerMainOverviewFragment, true)
    }

    override fun backToMarkets() {
        shoppingCar = ShoppingCar()
        consumerSellerProfileFragment.shoppingCar = shoppingCar
        showFragment(consumerMainOverviewFragment, true)
    }

    override fun loadOrder(order: Order) {
        shoppingCar.loadOrder(order)
        consumerSellerProfileFragment.shoppingCar = shoppingCar
        showFragment(consumerSellerProfileFragment, true)
    }
}