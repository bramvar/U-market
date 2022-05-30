package com.icesi.umarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.icesi.umarket.model.User

class ConsumerHomeActivity : AppCompatActivity() {

    private lateinit var menuConsumer:BottomNavigationView
    private lateinit var consumerMainOverviewFragment: ConsumerMainOverviewFragment
    private lateinit var consumerProfileFragment: ConsumerProfileFragment

    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consumer_home)

        //Extras

        consumerMainOverviewFragment = ConsumerMainOverviewFragment.newInstance()
        consumerProfileFragment = ConsumerProfileFragment.newInstance()
        consumerProfileFragment.currentUser = currentUser

        menuConsumer = findViewById(R.id.menuConsumer)
        showFragment(consumerMainOverviewFragment)

        menuConsumer.setOnItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.homeItem -> showFragment(consumerMainOverviewFragment)
                R.id.profileItem -> showFragment(consumerProfileFragment)
                R.id.ordersItem -> showFragment(consumerMainOverviewFragment)
            }
            true
        }
    }
    fun showFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }
}