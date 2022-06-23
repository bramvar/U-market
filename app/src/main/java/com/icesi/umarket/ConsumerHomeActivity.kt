package com.icesi.umarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.icesi.umarket.databinding.ActivityConsumerEditProfileBinding
import com.icesi.umarket.databinding.ActivityConsumerHomeBinding
import com.icesi.umarket.model.Market
import com.icesi.umarket.model.Product
import com.icesi.umarket.model.User

class ConsumerHomeActivity : AppCompatActivity(), ConsumerMainOverviewFragment.SellerObserver {

    ////// Init the fragment
    private lateinit var menuConsumer:BottomNavigationView
    private  var consumerMainOverviewFragment = ConsumerMainOverviewFragment.newInstance()
    private  var consumerProfileFragment = ConsumerProfileFragment.newInstance()
    private  var consumerShoppingFragment= ConsumerShoppingFragment.newInstance()
    private  var consumerSellerProfileFragment= SellerProfileFragment.newInstance()
    private  var productFragment = ProductFragment.newInstance()
    private lateinit var currentUser: User
    private lateinit var binding: ActivityConsumerHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsumerHomeBinding.inflate(layoutInflater)
        menuConsumer = binding.menuConsumer
        setContentView(binding.root)

        currentUser = Gson().fromJson(
            intent.extras?.getString("currentUser",""),
            User::class.java
        )

        ////// Load the listener
        consumerMainOverviewFragment.onSellerObserver = this
        //consumerSellerProfileFragment.adapter.onProductObserver = this
        ///// Init the currentUser in the fragments
        consumerProfileFragment.currentUser = currentUser
        consumerMainOverviewFragment.currentUser = currentUser

        showFragment(consumerMainOverviewFragment)

        menuConsumer.setOnItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.homeItem -> showFragment(consumerMainOverviewFragment)
                R.id.profileItem -> showFragment(consumerProfileFragment)
                R.id.ordersItem -> showFragment(consumerShoppingFragment)
            }
            true
        }
    }

    fun showFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }

    override fun sendMarket(market: Market) {
        consumerSellerProfileFragment.currentMarket = market
        showFragment(consumerSellerProfileFragment)
    }

    override fun sendProduct(product: Product) {
        productFragment.product = product
        showFragment(productFragment)
    }


}